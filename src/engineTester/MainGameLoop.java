package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.opengl.Texture;
import particles.Particle;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import utils.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Svetlozar Georgiev on 01/05/2017.
 */
public class MainGameLoop {
    public static void main(String[] args) {
        DisplayManager.createDisplay();
        System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));

        List<Entity> entities = new ArrayList<>();
        List<Terrain> terrains = new ArrayList<>();

        Loader loader = new Loader();
        TextMaster.init(loader);
        Random random = new Random();

        //player
        ModelData data = OBJFileLoader.loadOBJ("person");
        RawModel playerModel = loader.loadToVao(data);
        ModelTexture playerTex = new ModelTexture(loader.loadTexture("playerTexture"));
        TexturedModel playerTexModel = new TexturedModel(playerModel, playerTex);
        Player player = new Player(playerTexModel, new Vector3f(0,0, 0), 0, 150,0, 0.7f);
        entities.add(player);

        //camera
        Camera camera = new Camera(player);

        MasterRenderer renderer = new MasterRenderer(loader, camera);
        GuiRenderer guiRenderer = new GuiRenderer(loader);

        //particles
        ParticleMaster.init(loader, renderer.getProjMatrix());

        //text
        FontType font = new FontType(loader.loadFont("candara"), new File("res/candara.fnt"));
        GUIText text = new GUIText("A sample string of text!", 1, font, new Vector2f(0.0f, 0.0f), 1f, true);
        text.setColour(0.1f, 0.1f, 0.1f);

        //lights
        List<Light> lights = new ArrayList<>();
        Light sun = new Light(new Vector3f(1000000,1500000,-1000000), new Vector3f(0.2f,0.2f,0.2f));
        lights.add(sun);
        lights.add(new Light(new Vector3f(185,10,-293), new Vector3f(1,0,0), new Vector3f(1f, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(370,17,-300), new Vector3f(0,1,0), new Vector3f(1f, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(293,7,-305), new Vector3f(1,1,0), new Vector3f(1f, 0.01f, 0.002f)));

        //terrain stuff
        TerrainTexture bg = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture r = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture g = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture b = new TerrainTexture(loader.loadTexture("path"));
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        TerrainTexturePack texturePack = new TerrainTexturePack(bg, r, g, b);

        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
        terrains.add(terrain);


        // trees
        ModelData treeData = OBJFileLoader.loadOBJ("lowPolyTree");
        RawModel rawTreeModel = loader.loadToVao(treeData);
        TexturedModel treeModel = new TexturedModel(rawTreeModel, new ModelTexture(loader.loadTexture("lowPolyTree")));

        for(int i = 0; i < 50; i++) {
            float x = random.nextFloat() * 500;
            float z = random.nextFloat() * -500;
            float y = terrain.getHeightOfTerrain(x, z);
            //entities.add(new Entity(treeModel, new Vector3f(x, y, z), 0, random.nextFloat() * 180f, 0f,
                    //0.7f));
        }

        ModelData treeData2 = OBJFileLoader.loadOBJ("pine");
        RawModel rawTreeModel2 = loader.loadToVao(treeData2);
        TexturedModel treeModel2 = new TexturedModel(rawTreeModel2, new ModelTexture(loader.loadTexture("pine")));

        for(int i = 0; i < 50; i++) {
            float x = random.nextFloat() * 500;
            float z = random.nextFloat() * -500;
            float y = terrain.getHeightOfTerrain(x, z);
           entities.add(new Entity(treeModel2, new Vector3f(x, y, z), 0, random.nextFloat() * 180f, 0f,
                    1f));
        }

        //fern
        ModelData fernData = OBJFileLoader.loadOBJ("fern");
        RawModel rawFernModel = loader.loadToVao(fernData);
        TexturedModel fernModel = new TexturedModel(rawFernModel, new ModelTexture(loader.loadTexture("fern")));
        fernModel.getTexture().hasTransparency(true);
        fernModel.getTexture().useFakeLighting(true);
        fernModel.getTexture().setNumOfRows(2);

        for(int i = 0; i < 100; i++) {
            float x = random.nextFloat() * 500;
            float z = random.nextFloat() * -500;
            float y = terrain.getHeightOfTerrain(x, z);
            entities.add(new Entity(fernModel, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 180f, 0f,
                    1f));
        }

        //lamps
        List<Entity> lamps = new ArrayList<>();
        ModelData lampData = OBJFileLoader.loadOBJ("lamp");
        RawModel rawLampModel = loader.loadToVao(lampData);
        TexturedModel lampModel = new TexturedModel(rawLampModel, new ModelTexture(loader.loadTexture("lamp")));
        lampModel.getTexture().useFakeLighting(true);

        entities.add(new Entity(lampModel, new Vector3f(185, -4.7f, -293), 0, 0, 0, 1));
        entities.add(new Entity(lampModel, new Vector3f(370, 4.2f, -300), 0, 0, 0, 1));
        entities.add(new Entity(lampModel, new Vector3f(293, -6.8f, -305), 0, 0, 0, 1));

        //box
        ModelData boxData = OBJFileLoader.loadOBJ("lamp");
        RawModel boxRawModel = loader.loadToVao(boxData);
        TexturedModel texturedBox = new TexturedModel(boxRawModel, new ModelTexture(loader.loadTexture("lamp")));
        Entity box = new Entity(texturedBox, new Vector3f(10, terrain.getHeightOfTerrain(10, 10), 10), 0, 0, 0, 1);
        entities.add(box);

        //water stuff
        WaterFrameBuffers fbos = new WaterFrameBuffers();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjMatrix(), fbos);
        List<WaterTile> waters = new ArrayList<>();
        WaterTile water = new WaterTile(200, -155, 1);
        waters.add(water);

        List<GuiTexture> guis = new ArrayList<>();
        guis.add(new GuiTexture(fbos.getRefractionDepthTexture(), new Vector2f(-0.5f, 0.5f), new Vector2f(0.25f, 0.25f)));

        //normal map entities
        List<Entity> normalMapEntities = new ArrayList<>();
        TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("crate", loader), new ModelTexture(
                loader.loadTexture("crate")));
        barrelModel.getTexture().setNormalMap(loader.loadTexture("crateNormal"));
        barrelModel.getTexture().setShineDamper(10);
        barrelModel.getTexture().setReflectivity(0.5f);
        normalMapEntities.add(new Entity(barrelModel, new Vector3f(30, 10, -30), 0, 0, 0, 0.1f));

        //particle system
        ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("particleTex/cosmic"), 4);
        particleTexture.setAdditive(true);

        ParticleSystem particleSystem = new ParticleSystem(particleTexture, 100, 10, 0.1f, 1, 1.6f);
        particleSystem.randomizeRotation();
        particleSystem.setDirection(new Vector3f(0, 1, 0), 0.1f);
        particleSystem.setLifeError(0.1f);
        particleSystem.setSpeedError(0.25f);
        particleSystem.setScaleError(0.5f);

        //GuiTexture shadowMap = new GuiTexture(renderer.getShadowMapTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f));
        //guis.add(shadowMap);

        while(!Display.isCloseRequested()) {
            player.move(terrain);
            camera.move();

            particleSystem.generateParticles(player.getPosition());

            ParticleMaster.update(camera);

            renderer.renderShadowMap(entities, sun);

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            //render reflection
            fbos.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - water.getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();
            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight()+1f));
            camera.getPosition().y += distance;
            camera.invertPitch();

            //render refraction
            fbos.bindRefractionFrameBuffer();
            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight() + 1f));

            //render scene
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            fbos.unbindCurrentFrameBuffer();
            renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 100000));
            waterRenderer.render(waters, camera, sun);

            ParticleMaster.renderParticles(camera);
            TextMaster.render();

            //guiRenderer.render(guis);
            DisplayManager.updateDisplay();
        }


        ParticleMaster.cleanUp();
        TextMaster.cleanUp();
        fbos.cleanUp();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }}
