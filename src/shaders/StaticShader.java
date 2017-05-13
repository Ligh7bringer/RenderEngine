package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import utils.Maths;

/**
 * Created by Svetlozar Georgiev on 02/05/2017.
 */
public class StaticShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPos;
    private int location_lightColour;
    private int location_shineDamper;
    private int location_reflect;
    private int location_fakeLighting;
    private int location_skyColour;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttribs() {
        super.bindAttrib(0, "position");
        super.bindAttrib(1, "texCoords");
        super.bindAttrib(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
       location_transformationMatrix = super.getUniformLocation("transformationMatrix");
       location_projectionMatrix = super.getUniformLocation("projectionMatrix");
       location_viewMatrix = super.getUniformLocation("viewMatrix");
       location_lightPos = super.getUniformLocation("lightPos");
       location_lightColour = super.getUniformLocation("lightColour");
       location_shineDamper = super.getUniformLocation("shineDamper");
       location_reflect = super.getUniformLocation("reflectivity");
       location_fakeLighting = super.getUniformLocation("useFakeLighting");
       location_skyColour = super.getUniformLocation("skyColour");
    }

    public void loadSkyColour(Vector3f value) {
        super.loadVector(location_skyColour, value);
    }

    public void loadFakeLighting(boolean value) {
        super.loadBool(location_fakeLighting, value);
    }

    public void loadShineVars(float damper, float reflect) {
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflect, reflect);
    }

    public void loadLight(Light light) {
        super.loadVector(location_lightPos, light.getPosition());
        super.loadVector(location_lightColour, light.getColour());
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }
}
