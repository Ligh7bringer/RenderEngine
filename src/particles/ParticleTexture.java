package particles;

/**
 * Created by sgeor on 26/05/2017.
 */
public class ParticleTexture {

    private int textureID;
    private int numberOfRows;
    private boolean isAdditive = false;

    public ParticleTexture(int textureID, int numberOfRows) {
        this.textureID = textureID;
        this.numberOfRows = numberOfRows;
    }

    public boolean isAdditive() {
        return isAdditive;
    }

    public void setAdditive(boolean additive) {
        isAdditive = additive;
    }

    public int getTextureID() {
        return textureID;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }
}
