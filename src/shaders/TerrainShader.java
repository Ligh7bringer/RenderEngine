package shaders;

import com.sun.prism.ps.Shader;
import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import utils.Maths;

import java.util.List;

/**
 * Created by sgeor on 12/05/2017.
 */
public class TerrainShader extends ShaderProgram {

    private static final int MAX_LIGHTS = 4;

    private static final String VERTEX_FILE = "src/shaders/terrainVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/terrainFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPos[];
    private int location_lightColour[];
    private int location_attenuation[];
    private int location_shineDamper;
    private int location_reflect;
    private int location_skyColour;
    private int location_bgTex;
    private int location_rTex;
    private int location_gTex;
    private int location_bTex;
    private int location_blendMap;

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
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflect = super.getUniformLocation("reflectivity");
        location_skyColour = super.getUniformLocation("skyColour");
        location_bgTex = super.getUniformLocation("bgTex");
        location_rTex = super.getUniformLocation("rTex");
        location_gTex = super.getUniformLocation("gTex");
        location_bTex = super.getUniformLocation("bTex");
        location_blendMap = super.getUniformLocation("blendMap");

        location_lightPos = new int[MAX_LIGHTS];
        location_lightColour = new int[MAX_LIGHTS];
        location_attenuation = new int[MAX_LIGHTS];
        for(int i = 0; i < MAX_LIGHTS; i++) {
            location_lightPos[i] = super.getUniformLocation("lightPos[" + i + "]");
            location_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
            location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }

    public void loadTerrainTextures() {
        super.loadInt(location_bgTex, 0);
        super.loadInt(location_rTex, 1);
        super.loadInt(location_gTex, 2);
        super.loadInt(location_bTex, 3);
        super.loadInt(location_blendMap, 4);
    }

    public void loadSkyColour(Vector3f value) {
        super.loadVector(location_skyColour, value);
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
