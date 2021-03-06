package none.lwjgl;

import com.google.inject.Binder;
import com.google.inject.Module;
import none.engine.component.assets.*;
import none.engine.component.input.Keyboard;
import none.engine.component.input.Mouse;
import none.engine.component.physic.MasterPhysic;
import none.engine.component.renderer.MasterRenderer;
import none.engine.component.sound.MasterPlayer;
import none.engine.component.ui.factories.UiFactory;
import none.lwjgl.components.assets.*;
import none.lwjgl.components.input.KeyboardImpl;
import none.lwjgl.components.input.MouseImpl;
import none.lwjgl.components.physic.MasterPhysicImpl;
import none.lwjgl.components.renderer.Master32Renderer;
import none.lwjgl.components.sound.MasterAlPlayer;
import none.lwjgl.components.ui.UiGlFactory;

import java.util.Objects;

/**
 * A Module for LWJGL.
 */
public class LwjglModule implements Module {
    private final Assets assets;

    public LwjglModule(Assets assets) {
        this.assets = Objects.requireNonNull(assets, "assets");
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(Assets.class).toInstance(assets);

        binder.bind(ShaderHandler.class).to(SimpleShaderHandler.class);
        binder.bind(MeshHandler.class).to(MeshHandlerImpl.class);
        binder.bind(TextureHandler.class).to(SimpleTextureHandler.class);
        binder.bind(SoundHandler.class).to(WaveSoundHandler.class);
        binder.bind(PhysicHandler.class).to(ObjRigidBodyHandler.class);
        binder.bind(ModelHandler.class).to(ObjModelHandler.class);

        binder.bind(MasterRenderer.class).to(Master32Renderer.class);
        binder.bind(MasterPlayer.class).to(MasterAlPlayer.class);
        binder.bind(MasterPhysic.class).to(MasterPhysicImpl.class);

        binder.bind(Keyboard.class).to(KeyboardImpl.class);
        binder.bind(Mouse.class).to(MouseImpl.class);

        binder.bind(UiFactory.class).to(UiGlFactory.class);
    }
}
