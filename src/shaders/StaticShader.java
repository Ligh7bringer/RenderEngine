package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import utils.Maths;

import java.util.List;

/**
 * Created by Svetlozar Georgiev on 02/05/2017.
 */
public class StaticShader extends ShaderProgram {

    private static final int MAX_LIGHTS = 4;

    private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPos[];
    private int location_lightColour[];
    private int location_shineDamper;
    private int location_reflect;
    private int location_fakeLighting;
    private int location_skyColour;
    private int location_numOfRows;
    private int location_offset;
    private int location_attenuation[];
    private int location_clipPlane;

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
       location_shineDamper = super.getUniformLocation("shineDamper");
       location_reflect = super.getUniformLocation("reflectivity");
       location_fakeLighting = super.getUniformLocation("useFakeLighting");
       location_skyColour = super.getUniformLocation("skyColour");
       location_numOfRows = super.getUniformLocation("numOfRows");
       location_offset = super.getUniformLocation("offset");
       location_clipPlane = super.getUniformLocation("clipPlane");

       location_lightPos = new int[MAX_LIGHTS];
       location_lightColour = new int[MAX_LIGHTS];
       location_attenuation = new int[MAX_LIGHTS];
       for(int i = 0; i < MAX_LIGHTS; i++) {
           location_lightPos[i] = super.getUniformLocation("lightPos[" + i + "]");
           location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
           location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
       }
    }

    public void loadClipPlane(Vector4f plane) {
        super.loadVector4f(location_clipPlane, plane);
    }

    public void loadNumOfRows(int value) {
        super.loadFloat(location_numOfRows, value);
    }

    public void loadOffset(float x, float y) {
        Vector2f offset = new Vector2f(x, y);
        super.loadVector2f(location_offset, offset);
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

    public void loadLights(List<Light> lights) {
        for(int i = 0; i < MAX_LIGHTS; i++) {
            if(i < lights.size()) {
                super.loadVector(location_lightPos[i], lights.get(i).getPosition());
                super.loadVector(location_lightColour[i], lights.get(i).getColour());
                super.loadVector(location_attenuation[i], lights.get(i).getAttenuation());
            } else {
                super.loadVector(location_lightPos[i], new Vector3f(0, 0, 0));
                super.loadVector(location_lightColour[i], new Vector3f(0, 0, 0));
                super.loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
            }
        }
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
