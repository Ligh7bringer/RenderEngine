package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

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

        Loader loader = new Loader();

        //lights
        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(0,1000,-7000), new Vector3f(0.1f,0.1f,0.1f)));
        lights.add(new Light(new Vector3f(185,10,-293), new Vector3f(1,0,0), new Vector3f(1f, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(370,17,-300), new Vector3f(0,1,0), new Vector3f(1f, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(293,7,-305), new Vector3f(1,1,0), new Vector3f(1f, 0.01f, 0.002f)));

        //guis
        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("healthbar"), new Vector2f(-0.6f, 0.7f), new Vector2f(0.25f, 0.25f));
        guis.add(gui);

        //player
        ModelData data = OBJFileLoader.loadOBJ("person");
        RawModel playerModel = loader.loadToVao(data);
        ModelTexture playerTex = new ModelTexture(loader.loadTexture("playerTexture"));
        TexturedModel playerTexModel = new TexturedModel(playerModel, playerTex);

        Player player = new Player(playerTexModel, new Vector3f(0,0,0), 0, 150,0, 0.7f);

        //terrain stuff
        TerrainTexture bg = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture r = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture g = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture b = new TerrainTexture(loader.loadTexture("path"));
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        TerrainTexturePack texturePack = new TerrainTexturePack(bg, r, g, b);

        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");

        List<Entity> trees = new ArrayList<>();
        List<Entity> trees2 = new ArrayList<>();
        List<Entity> grass = new ArrayList<>();
        List<Entity> fern = new ArrayList<>();
        Random random = new Random();

        // trees
        ModelData treeData = OBJFileLoader.loadOBJ("lowPolyTree");
        RawModel rawTreeModel = loader.loadToVao(treeData);
        TexturedModel treeModel = new TexturedModel(rawTreeModel, new ModelTexture(loader.loadTexture("lowPolyTree")));

        for(int i = 0; i < 50; i++) {
            float x = random.nextFloat() * 500;
            float z = random.nextFloat() * -500;
            float y = terrain.getHeightOfTerrain(x, z);
            trees.add(new Entity(treeModel, new Vector3f(x, y, z), 0, random.nextFloat() * 180f, 0f,
                    0.7f));
        }

        ModelData treeData2 = OBJFileLoader.loadOBJ("pine");
        RawModel rawTreeModel2 = loader.loadToVao(treeData2);
        TexturedModel treeModel2 = new TexturedModel(rawTreeModel2, new ModelTexture(loader.loadTexture("pine")));

        for(int i = 0; i < 100; i++) {
            float x = random.nextFloat() * 500;
            float z = random.nextFloat() * -500;
            float y = terrain.getHeightOfTerrain(x, z);
            trees2.add(new Entity(treeModel2, new Vector3f(x, y, z), 0, random.nextFloat() * 180f, 0f,
                    1f));
        }

        //fern
        ModelData fernData = OBJFileLoader.loadOBJ("fern");
        RawModel rawFernModel = loader.loadToVao(fernData);
        TexturedModel fernModel = new TexturedModel(rawFernModel, new ModelTexture(loader.loadTexture("fern")));
        fernModel.getTexture().hasTransparency(true);
        fernModel.getTexture().useFakeLighting(true);
        fernModel.getTexture().setNumOfRows(2);

        for(int i = 0; i < 200; i++) {
            float x = random.nextFloat() * 500;
            float z = random.nextFloat() * -500;
            float y = terrain.getHeightOfTerrain(x, z);
            fern.add(new Entity(fernModel, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 180f, 0f,
                    1f));
        }

        //lamps
        List<Entity> lamps = new ArrayList<>();
        ModelData lampData = OBJFileLoader.loadOBJ("lamp");
        RawModel rawLampModel = loader.loadToVao(lampData);
        TexturedModel lampModel = new TexturedModel(rawLampModel, new ModelTexture(loader.loadTexture("lamp")));
        lampModel.getTexture().useFakeLighting(true);

        lamps.add(new Entity(lampModel, new Vector3f(185, -4.7f, -293), 0, 0, 0, 1));
        lamps.add(new Entity(lampModel, new Vector3f(370, 4.2f, -300), 0, 0, 0, 1));
        lamps.add(new Entity(lampModel, new Vector3f(293, -6.8f, -305), 0, 0, 0, 1));

        Camera camera = new Camera(player);
        MasterRenderer renderer = new MasterRenderer(loader);
        GuiRenderer guiRenderer = new GuiRenderer(loader);

        while(!Display.isCloseRequested()) {
            camera.move();
            renderer.processTerrain(terrain);
            player.move(terrain);
            renderer.processEntity(player);
            for(Entity e : trees)
                renderer.processEntity(e);
            for(Entity e : grass)
                renderer.processEntity(e);
            for(Entity e : fern)
                renderer.processEntity(e);
            for(Entity e : trees2)
                renderer.processEntity(e);
            for(Entity e : lamps)
                renderer.processEntity(e);
            renderer.render(lights, camera);

            guiRenderer.render(guis);
            DisplayManager.updateDisplay();
        }

        guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }}
