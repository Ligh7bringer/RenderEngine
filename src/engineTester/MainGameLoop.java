package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;

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
        Light light = new Light(new Vector3f(3000,2000,2000), new Vector3f(1,1,1));
        Terrain terrain = new Terrain(-0.5f, -1, loader, new ModelTexture(loader.loadTexture("grass")));

        List<Entity> trees = new ArrayList<>();
        List<Entity> trees2 = new ArrayList<>();
        List<Entity> grass = new ArrayList<>();
        List<Entity> fern = new ArrayList<>();
        Random random = new Random();

        // trees
        ModelData treeData = OBJFileLoader.loadOBJ("lowPolyTree");
        RawModel rawTreeModel = loader.loadToVao(treeData);
        TexturedModel treeModel = new TexturedModel(rawTreeModel, new ModelTexture(loader.loadTexture("lowPolyTree")));

        for(int i = 0; i < 100; i++) {
            float x = random.nextFloat() * 500;
            float y = 0f;
            float z = random.nextFloat() * -500;
            trees.add(new Entity(treeModel, new Vector3f(x, y, z), 0, random.nextFloat() * 180f, 0f,
                    1.5f));
        }

        ModelData treeData2 = OBJFileLoader.loadOBJ("tree");
        RawModel rawTreeModel2 = loader.loadToVao(treeData2);
        TexturedModel treeModel2 = new TexturedModel(rawTreeModel2, new ModelTexture(loader.loadTexture("tree")));

        for(int i = 0; i < 100; i++) {
            float x = random.nextFloat() * 500;
            float y = 0f;
            float z = random.nextFloat() * -500;
            trees2.add(new Entity(treeModel2, new Vector3f(x, y, z), 0, random.nextFloat() * 180f, 0f,
                    5f));
        }

        //grass
        ModelData grassData = OBJFileLoader.loadOBJ("grassModel");
        RawModel rawGrassModel = loader.loadToVao(grassData);
        TexturedModel grassModel = new TexturedModel(rawGrassModel, new ModelTexture(loader.loadTexture("grassTex")));
        grassModel.getTexture().hasTransparency(true);
        grassModel.getTexture().useFakeLighting(true);

        for(int i = 0; i < 200; i++) {
            float x = random.nextFloat() * 500;
            float y = 0f;
            float z = random.nextFloat() * -500;
            grass.add(new Entity(grassModel, new Vector3f(x, y, z), 0, random.nextFloat() * 180f, 0f,
                    1f));
        }

        //fern
        ModelData fernData = OBJFileLoader.loadOBJ("fern");
        RawModel rawFernModel = loader.loadToVao(fernData);
        TexturedModel fernModel = new TexturedModel(rawFernModel, new ModelTexture(loader.loadTexture("fern")));
        fernModel.getTexture().hasTransparency(true);
        fernModel.getTexture().useFakeLighting(true);

        for(int i = 0; i < 200; i++) {
            float x = random.nextFloat() * 500;
            float y = 0f;
            float z = random.nextFloat() * -500;
            fern.add(new Entity(fernModel, new Vector3f(x, y, z), 0, random.nextFloat() * 180f, 0f,
                    1f));
        }


        Camera camera = new Camera();        MasterRenderer renderer = new MasterRenderer();

        while(!Display.isCloseRequested()) {
            camera.move();
            renderer.processTerrain(terrain);
            for(Entity e : trees)
                renderer.processEntity(e);
            for(Entity e : grass)
                renderer.processEntity(e);
            for(Entity e : fern)
                renderer.processEntity(e);
            for(Entity e : trees2)
                renderer.processEntity(e);
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }}
