package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by sgeor on 11/05/2017.
 */
public class Camera {

    private float distanceFromPlayer = 50;
    private float angleAroundPlayer = 0;

    private Vector3f position = new Vector3f(100, 35, 50);
    private float pitch = 20;
    private float yaw;
    private float roll;

    private Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
    }

    private void calculateCameraPosition(float horizontalDist, float verticalDist) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizontalDist * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDist * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = 5 + player.getPosition().y + verticalDist;
    }

    private float calculateHorizontalDistance() {
        float hD = (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
       /* if(hD < 2)
            hD = 2; */
        return hD;
    }

    private float calculateVerticalDistance() {
        float vD = (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
        /*if(vD < 2)
            vD = 2; */
        return vD;
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        distanceFromPlayer -= zoomLevel;
    }

    private void calculatePitch() {
        if(Mouse.isButtonDown(1)) {
            float pitchChange = Mouse.getDY() * 0.1f;
            pitch -= pitchChange;
            /*if(pitch < 0)
                pitch = 0;
            else if(pitch > 90)
                pitch = 90;
            */
        }
    }

    private void calculateAngleAroundPlayer() {
        if(Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * 0.3f;
            angleAroundPlayer -= angleChange;
        }
    }

    public void invertPitch() {
        this.pitch = -pitch;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
