package none.lwjgl.components.renderer;

import com.google.inject.Inject;
import none.engine.Game;
import none.engine.component.AbsObject;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.renderer.camera.CameraComponent;
import none.engine.component.renderer.camera.OrthographicCamera;
import none.engine.component.renderer.camera.PerspectiveCamera;
import org.apache.commons.lang3.Validate;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.FloatBuffer;

/**
 * CameraComponent.
 */
public class Camera32Renderer extends AbsObject {
    private static final Logger LOGGER = LoggerFactory.getLogger(Camera32Renderer.class);
    private static final int MATRIX_BUFFER_SIZE = 16;
    private static final float HALF = 2f;
    private static final int TWO = 2;
    private static final double HALF_CIRCLE_IN_DEG = 180d;
    private int projectionId;
    private int viewId;

    private FloatBuffer projectionBuffer;
    private FloatBuffer viewBuffer;
    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;

    @Inject
    protected Camera32Renderer(UUIDFactory factory, Game game) {
        super("CameraRenderer", factory.createUUID(), game);

        projectionBuffer = BufferUtils.createFloatBuffer(MATRIX_BUFFER_SIZE);
        viewBuffer = BufferUtils.createFloatBuffer(MATRIX_BUFFER_SIZE);
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
    }

    public void setProjectionId(int projectionId) {
        this.projectionId = projectionId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public void draw(CameraComponent camera) {
        Validate.notNull(camera, "A Scene has to have a camera.");
        if (!(camera instanceof OrthographicCamera || camera instanceof PerspectiveCamera)) {
            LOGGER.warn("Unknown Camera: {}.", camera);
        }

        updateProjectionMatrix(camera);
        updateViewMatrix(camera);

        GL20.glUniformMatrix4(projectionId, false, projectionBuffer);
        GL20.glUniformMatrix4(viewId, false, viewBuffer);
    }

    private void updateProjectionMatrix(CameraComponent camera) {
        projectionMatrix.setZero();

        if (camera instanceof PerspectiveCamera) {
            perspectiveCamera((PerspectiveCamera) camera);
        } else if (camera instanceof OrthographicCamera) {
            orthographicCamera((OrthographicCamera) camera);
        }

        projectionMatrix.store(projectionBuffer);
        projectionBuffer.flip();
    }

    private void orthographicCamera(OrthographicCamera camera) {
        projectionMatrix.m00 = (float) (2 / (camera.getRightClippingPlane() - camera.getLeftClippingPlane()));
        projectionMatrix.m30 = (float) (-((camera.getRightClippingPlane() + camera.getLeftClippingPlane()) /
                (camera.getRightClippingPlane() - camera.getLeftClippingPlane())));

        projectionMatrix.m11 = (float) (2 / (camera.getTopClippingPlane() - camera.getBottomClippingPlane()));
        projectionMatrix.m31 = (float) (-((camera.getTopClippingPlane() + camera.getBottomClippingPlane()) /
                (camera.getTopClippingPlane() - camera.getBottomClippingPlane())));

        projectionMatrix.m22 = (float) (-2 / (camera.getFarClippingPlane() - camera.getNearClippingPlane()));
        projectionMatrix.m32 = (float) (-((camera.getFarClippingPlane() + camera.getNearClippingPlane()) /
                (camera.getFarClippingPlane() - camera.getNearClippingPlane())));

        projectionMatrix.m33 = 1;
    }

    private void perspectiveCamera(PerspectiveCamera camera) {
        float fieldOfView = camera.getFieldOfView();
        float aspectRatio = camera.getAspectRatio();
        float nearPlane = camera.getNearPlane();
        float farPlane = camera.getFarPlane();

        float yScale = this.coTangent(this.degreesToRadians(fieldOfView / HALF));
        float xScale = yScale / aspectRatio;
        float frustumLength = farPlane - nearPlane;

        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((farPlane + nearPlane) / frustumLength);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((TWO * nearPlane * farPlane) / frustumLength);
        projectionMatrix.m33 = 0;
    }

    private void updateViewMatrix(CameraComponent camera) {
        org.joml.Matrix4f matrix = new org.joml.Matrix4f();
        matrix.identity();

        org.joml.Vector3f eye = new org.joml.Vector3f((float) camera.getCameraPosition().x, (float) camera.getCameraPosition().y, (float) camera.getCameraPosition().z);
        org.joml.Vector3f center = new org.joml.Vector3f((float) camera.getObjectPosition().x, (float) camera.getObjectPosition().y, (float) camera.getObjectPosition().z);
        org.joml.Vector3f up = new org.joml.Vector3f((float) camera.getUpDirection().x, (float) camera.getUpDirection().y, (float) camera.getUpDirection().z);

        viewBuffer.clear();
        matrix.lookAt(eye, center, up).get(viewBuffer);
    }

    private float coTangent(float angle) {
        return (float) (1f / Math.tan(angle));
    }

    private float degreesToRadians(float degrees) {
        return degrees * (float) (Math.PI / HALF_CIRCLE_IN_DEG);
    }
}
