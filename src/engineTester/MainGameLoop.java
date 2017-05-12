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

        RawModel model = OBJLoader.loadObjModel("cube", loader);
        ModelTexture texture = new ModelTexture(loader.loadTexture("checker"));
        TexturedModel dragon = new TexturedModel(model, texture);
        ModelTexture tex = dragon.getTexture();
        tex.setShineDamper(10);
        tex.setReflectivity(1);

        //Entity entity = new Entity(texturedModel, new Vector3f(0, -5, -25), 0, 0, 0, 0.1f);
        Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(	0.541f, 0.169f, 0.886f));

        Camera camera = new Camera();
        List<Entity> dragons = new ArrayList<>();
        Random random = new Random();

        for(int i = 0; i < 200; i++) {
            float x = random.nextFloat() * 100 - 50;
            float y = random.nextFloat() * 100 - 50;
            float z = random.nextFloat() * -300;
            dragons.add(new Entity(dragon, new Vector3f(x, y, z), random.nextFloat() * 180f, random.nextFloat() * 180f, 0f,
                    1f));
        }

        MasterRenderer renderer = new MasterRenderer();

        while(!Display.isCloseRequested()) {
            camera.move();
            for(Entity e : dragons) {
                renderer.processEntity(e);
            }
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }}
