package none.engine.component.renderer.camera;

import none.engine.Game;

import java.util.UUID;

/**
 * A camera using orthographic projection.
 */
public class OrthographicCamera extends Camera {

    private double leftClippingPlane;
    private double rightClippingPlane;
    private double bottomClippingPlane;
    private double topClippingPlane;
    private double nearClippingPlane;
    private double farClippingPlane;

    public OrthographicCamera(UUID id, Game game) {
        super(OrthographicCamera.class.getSimpleName(), id, game);
    }

    public void setFrustum(double leftClippingPlane, double rightClippingPlane,
                           double bottomClippingPlane, double topClippingPlane,
                           double nearClippingPlane, double farClippingPlane) {
        this.leftClippingPlane = leftClippingPlane;
        this.rightClippingPlane = rightClippingPlane;
        this.bottomClippingPlane = bottomClippingPlane;
        this.topClippingPlane = topClippingPlane;
        this.nearClippingPlane = nearClippingPlane;
        this.farClippingPlane = farClippingPlane;
    }

    public double getLeftClippingPlane() {
        return leftClippingPlane;
    }

    public double getRightClippingPlane() {
        return rightClippingPlane;
    }

    public double getBottomClippingPlane() {
        return bottomClippingPlane;
    }

    public double getTopClippingPlane() {
        return topClippingPlane;
    }

    public double getNearClippingPlane() {
        return nearClippingPlane;
    }

    public double getFarClippingPlane() {
        return farClippingPlane;
    }
}
