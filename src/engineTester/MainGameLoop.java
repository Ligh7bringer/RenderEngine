package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
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

        RawModel model = OBJLoader.loadObjModel("lowPolyTree", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("lowPolyTree"));
        TexturedModel cube = new TexturedModel(model, texture);
        ModelTexture tex = cube.getTexture();
        tex.setShineDamper(10);
        tex.setReflectivity(0.5f);

        Light light = new Light(new Vector3f(3000,2000,2000), new Vector3f(1,1,1));
        Terrain terrain = new Terrain(-0.5f, -1, loader, new ModelTexture(loader.loadTexture("grass")));

        List<Entity> trees = new ArrayList<>();
        List<Entity> grass = new ArrayList<>();
        List<Entity> fern = new ArrayList<>();
        Random random = new Random();

        for(int i = 0; i < 200; i++) {
            float x = random.nextFloat() * 500;
            float y = 0f;
            float z = random.nextFloat() * -500;
            trees.add(new Entity(cube, new Vector3f(x, y, z), 0, random.nextFloat() * 180f, 0f,
                    1.5f));
        }

        model = OBJLoader.loadObjModel("grassModel", loader);
        texture = new ModelTexture(loader.loadTexture("grassTex"));
        cube = new TexturedModel(model, texture);
        tex = cube.getTexture();
        tex.setShineDamper(20);
        tex.setReflectivity(1);
        tex.hasTransparency(true);
        tex.useFakeLighting(true);

        for(int i = 0; i < 100; i++) {
            float x = random.nextFloat() * 500;
            float y = 0f;
            float z = random.nextFloat() * -500;
            grass.add(new Entity(cube, new Vector3f(x, y, z), 0, random.nextFloat() * 180f, 0f,
                    1f));
        }

        model = OBJLoader.loadObjModel("fern", loader);
        texture = new ModelTexture(loader.loadTexture("fern"));
        cube = new TexturedModel(model, texture);
        tex = cube.getTexture();
        tex.setShineDamper(20);
        tex.setReflectivity(1);
        tex.hasTransparency(true);
        tex.useFakeLighting(true);

        for(int i = 0; i < 100; i++) {
            float x = random.nextFloat() * 500;
            float y = 0f;
            float z = random.nextFloat() * -500;
            fern.add(new Entity(cube, new Vector3f(x, y, z), 0, random.nextFloat() * 180f, 0f,
                    0.5f));
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
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }}
