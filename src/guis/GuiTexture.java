package guis;

import org.lwjgl.util.vector.Vector2f;

/**
 * Created by sgeor on 14/05/2017.
 */
public class GuiTexture {

    private int tex;
    private Vector2f position;
    private Vector2f scale;

    public GuiTexture(int tex, Vector2f position, Vector2f scale) {
        this.tex = tex;
        this.position = position;
        this.scale = scale;
    }

    public int getTex() {
        return tex;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }
}
