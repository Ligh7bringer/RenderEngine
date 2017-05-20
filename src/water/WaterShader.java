package water;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.ShaderProgram;
import utils.Maths;
import entities.Camera;
import entities.Light;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/water/waterVertex.txt";
	private final static String FRAGMENT_FILE = "src/water/waterFragment.txt";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
    private int location_reflectionTex;
    private int location_refractionTex;
    private int location_dudvMap;
    private int location_depthMap;
    private int location_moveFactor;
    private int location_cameraPos;
    private int location_normalMap;
    private int location_lightColour;
    private int location_lightPos;

    public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttribs() {
		bindAttrib(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
        location_reflectionTex = getUniformLocation("reflectionTex");
        location_refractionTex = getUniformLocation("refractionTex");
        location_dudvMap = getUniformLocation("dudvMap");
        location_moveFactor = getUniformLocation("moveFactor");
        location_cameraPos = getUniformLocation("cameraPos");
        location_normalMap = getUniformLocation("normalMap");
        location_lightColour = getUniformLocation("lightColour");
        location_lightPos = getUniformLocation("lightPos");
        location_depthMap = getUniformLocation("depthMap");
	}

	public void loadLight(Light light) {
        super.loadVector(location_lightColour, light.getColour());
        super.loadVector(location_lightPos, light.getPosition());
    }

	public void loadMoveFactor(float value) {
        super.loadFloat(location_moveFactor, value);
    }

	public void connectTextureUnits() {
        super.loadInt(location_reflectionTex, 0);
        super.loadInt(location_refractionTex, 1);
        super.loadInt(location_dudvMap, 2);
        super.loadInt(location_normalMap, 3);
        super.loadInt(location_depthMap, 4);
    }

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
        super.loadVector(location_cameraPos, camera.getPosition());
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}
