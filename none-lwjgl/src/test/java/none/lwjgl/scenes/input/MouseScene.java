package none.lwjgl.scenes.input;

import none.engine.Game;
import none.engine.component.AbsStructObject;
import none.engine.component.EngineObject;
import none.engine.component.Transform;
import none.engine.component.common.uuid.UUIDFactory;
import none.engine.component.input.*;
import none.engine.component.renderer.Renderable;
import none.engine.component.renderer.camera.Camera;
import none.engine.component.renderer.camera.OrthographicCamera;
import none.engine.component.renderer.primitives.Text;
import none.engine.scenes.Scene;
import org.joml.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Scene to Display-Mouse input.
 */
public class MouseScene extends AbsStructObject<EngineObject> implements Scene {
    public static final String NAME = MouseScene.class.getSimpleName();
    public static final Logger LOGGER = LoggerFactory.getLogger(MouseScene.class);

    private final UUIDFactory uuidFactory;
    private final ToggleCommand toggleCommand;
    private final ResetPosition resetPosition;
    private final LeftMouse leftMouse;
    private final RightMouse rightMouse;

    private OrthographicCamera orthographicCamera;

    private Mouse mouse;
    private Keyboard keyboard;

    private Renderable positionRenderable;
    private Renderable lockingRenderable;
    private Renderable clickingRenderable;
    private Renderable insideWindowRenderable;

    public MouseScene(UUIDFactory factory, Game game) {
        super(NAME, factory.createUUID(), game);

        uuidFactory = Objects.requireNonNull(factory, "factory");
        toggleCommand = new ToggleCommand();
        resetPosition = new ResetPosition();
        leftMouse = new LeftMouse();
        rightMouse = new RightMouse();
    }

    @Override
    public Camera getActiveCamera() {
        return orthographicCamera;
    }

    @Override
    public void init() {
        LOGGER.info("Press Enter or Space Key.");

        int range = 100;
        orthographicCamera = new OrthographicCamera(uuidFactory.createUUID(), getGame());
        orthographicCamera.setFrustum(-range, range, -range, range, -range, range);

        mouse = getGame().getInjector().getInstance(Mouse.class);
        mouse.registerCommand(leftMouse, MouseKey.LEFT);
        mouse.registerCommand(rightMouse, MouseKey.RIGHT);
        keyboard = getGame().getInjector().getInstance(Keyboard.class);
        keyboard.registerCommand(toggleCommand, Key.ENTER);
        keyboard.registerCommand(resetPosition, Key.SPACE);

        String message = buildPositionText();
        int textSize = 6;
        Text text = new Text(uuidFactory.createUUID(), message, textSize);
        Transform transform = new Transform(uuidFactory.createUUID(), new Vector3d(-message.length() * textSize / 2, 0, 0));

        positionRenderable = new Renderable("MousePositionText", uuidFactory.createUUID(), text, transform);
        addObject(positionRenderable);

        message = buildLockedText();
        text = new Text(uuidFactory.createUUID(), message, textSize);
        transform = new Transform(uuidFactory.createUUID(), new Vector3d(-message.length() * textSize / 2, -2 * textSize, 0));

        lockingRenderable = new Renderable("MouseLockedText", uuidFactory.createUUID(), text, transform);
        addObject(lockingRenderable);

        message = buildMouseClicksText();
        text = new Text(uuidFactory.createUUID(), message, textSize);
        transform = new Transform(uuidFactory.createUUID(), new Vector3d(-message.length() * textSize / 2, 2 * textSize, 0));

        clickingRenderable = new Renderable("MouseClickDown", uuidFactory.createUUID(), text, transform);
        addObject(clickingRenderable);

        message = buildInsideWindowText();
        text = new Text(uuidFactory.createUUID(), message, textSize);
        transform = new Transform(uuidFactory.createUUID(), new Vector3d(-message.length() * textSize / 2, 4 * textSize, 0));

        insideWindowRenderable = new Renderable("MouseInsideWindow", uuidFactory.createUUID(), text, transform);
        addObject(insideWindowRenderable);

        super.init();
    }

    @Override
    public void update(int deltaInMs) {
        super.update(deltaInMs);

        positionRenderable.getText().setText(buildPositionText());
        lockingRenderable.getText().setText(buildLockedText());
        clickingRenderable.getText().setText(buildMouseClicksText());
        insideWindowRenderable.getText().setText(buildInsideWindowText());

        if (keyboard.isCommandClicked(toggleCommand)) {
            mouse.lock();
        }
        if (keyboard.isCommandClicked(resetPosition)) {
            mouse.setMousePosition(getGame().getOptions().getDisplayWidth() / 2, getGame().getOptions().getDisplayHeight() / 2);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private String buildPositionText() {
        DecimalFormat decimalFormat = new DecimalFormat("000.####");
        double x = mouse.getMousePosition().x;
        double y = mouse.getMousePosition().y;

        return "[" + decimalFormat.format(x) + ", " + decimalFormat.format(y) + "]";
    }

    private String buildLockedText() {
        return "Mouse is locked: " + mouse.isLocked();
    }

    private String buildMouseClicksText() {
        return "Left-Down: " + mouse.isCommandDown(leftMouse) + " Right-Down: " + mouse.isCommandDown(rightMouse);
    }

    private String buildInsideWindowText() {
        return "Inside Window: " + mouse.isInWindow();
    }

    class ToggleCommand implements Command {

    }

    class ResetPosition implements Command {

    }

    class LeftMouse implements Command {

    }

    class RightMouse implements Command {

    }
}
