package textures;

/**
 * Created by Svetlozar Georgiev on 02/05/2017.
 */
public class ModelTexture {
    private int textureID;
    private int normalMap;

    private float shineDamper = 1;
    private float reflectivity = 0;

    private int numOfRows = 1;

    private boolean hasTransparency = false;
    private boolean useFakeLighting = false;

    public ModelTexture(int id) {
        this.textureID = id;
    }

    public int getID() {
        return textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public boolean hasTransparency() {
        return hasTransparency;
    }

    public void hasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public boolean useFakeLighting() {
        return useFakeLighting;
    }

    public void useFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    public int getNormalMap() {
        return normalMap;
    }

    public void setNormalMap(int normalMap) {
        this.normalMap = normalMap;
    }
}
