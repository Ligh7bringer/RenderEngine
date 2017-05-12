package shaders;

import com.sun.prism.ps.Shader;
import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import utils.Maths;

/**
 * Created by sgeor on 12/05/2017.
 */
public class TerrainShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/shaders/terrainVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/terrainFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPos;
    private int location_lightColour;
    private int location_shineDamper;
    private int location_reflect;

    public TerrainShader() {
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
