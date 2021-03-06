package renderEngine;

import entities.Camera;
import entities.Entity;
import models.TexturedModel;
import normalMappingRenderer.NormalMappingRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shaders.StaticShader;
import entities.Light;
import shaders.TerrainShader;
import shadows.ShadowMapMasterRenderer;
import skybox.SkyboxRenderer;
import terrains.Terrain;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sgeor on 12/05/2017.
 */
public class MasterRenderer {

    public static final float FOV = 70;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 1000f;

    public static final float RED = 0.5444f;
    public static final float GREEN = 0.62f;
    public static final float BLUE = 0.69f;

    private Matrix4f projMatrix;

    private StaticShader shader = new StaticShader();
    private EntityRenderer renderer;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
    private Map<TexturedModel, List<Entity>> normalMapEntities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();

    private boolean polygonMode = false;

    private SkyboxRenderer skyboxRenderer;

    private NormalMappingRenderer normalMapRenderer;

    private ShadowMapMasterRenderer shadowMapRenderer;

    public MasterRenderer(Loader loader, Camera cam) {
        enableCulling();
        //GL11.glEnable(GL13.GL_MULTISAMPLE);
        // GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        createProjectionMatrix();
        renderer = new EntityRenderer(shader, projMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projMatrix);
        normalMapRenderer = new NormalMappingRenderer(projMatrix);
        this.shadowMapRenderer = new ShadowMapMasterRenderer(cam);
    }

    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void renderScene(List<Entity> entities, List<Entity> normalMapEntities, List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clipPlane) {
        for(Terrain t : terrains) {
            processTerrain(t);
        }
        for(Entity e : entities) {
            processEntity(e);
        }
        for(Entity e : normalMapEntities) {
            processNormalMapEntity(e);
        }
        render(lights, camera, clipPlane);
    }

    public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
        prepare();
        shader.start();
        shader.loadClipPlane(clipPlane);
        shader.loadSkyColour(new Vector3f(RED, GREEN, BLUE));
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();
        normalMapRenderer.render(normalMapEntities, clipPlane, lights, camera);
        terrainShader.start();
        terrainShader.loadClipPlane(clipPlane);
        terrainShader.loadSkyColour(new Vector3f(RED, GREEN, BLUE));
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix());
        terrainShader.stop();
        skyboxRenderer.render(camera, RED, GREEN, BLUE);
        terrains.clear();
        entities.clear();
        normalMapEntities.clear();
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if(batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void processNormalMapEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = normalMapEntities.get(entityModel);
        if(batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            normalMapEntities.put(entityModel, newBatch);
        }
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(RED, GREEN, BLUE, 1);
        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
    }

    public void renderShadowMap(List<Entity> entityList, Light sun) {
        for(Entity e : entityList) {
            processEntity(e);
        }

        shadowMapRenderer.render(entities, sun);
        entities.clear();
    }

    public int getShadowMapTexture() {
        return shadowMapRenderer.getShadowMap();
    }

    public void cleanUp(){
        shader.cleanUp();
        terrainShader.cleanUp();
        normalMapRenderer.cleanUp();
        shadowMapRenderer.cleanUp();
    }


    private void createProjectionMatrix() {
        projMatrix = new Matrix4f();
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projMatrix.m00 = x_scale;
        projMatrix.m11 = y_scale;
        projMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projMatrix.m23 = -1;
        projMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projMatrix.m33 = 0;
    }

    public Matrix4f getProjMatrix() {
        return projMatrix;
    }
}
