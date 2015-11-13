package none.engine;

import none.engine.component.AbsStructObject;
import none.engine.component.renderer.camera.CameraComponent;
import none.engine.scenes.Scene;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class SceneManagerTest {
    private Scene sceneA;
    private Scene sceneB;

    private SceneManager manager;

    @Before
    public void setup() {
        Scene sceneA = spy(new SceneA());
        Scene sceneB = spy(new SceneB());

        setup(sceneA, sceneB, SceneA.NAME);
    }

    private void setup(Scene sceneA, Scene sceneB, String firstScene) {
        List<Scene> sceneList = new ArrayList<>();
        sceneList.add(sceneA);
        sceneList.add(sceneB);

        this.sceneA = sceneA;
        this.sceneB = sceneB;
        this.manager = new SceneManager(mock(Game.class), sceneList, firstScene);
    }

    @Test
    public void testStartSceneActive() {
        int delta = 42;

        manager.init();
        manager.update(delta);
        manager.dispose();

        verify(sceneA, times(1)).init();
        verify(sceneA, times(1)).update(delta);
        verify(sceneA, times(1)).dispose();

        verify(sceneB, times(0)).init();
        verify(sceneB, times(0)).update(delta);
        verify(sceneB, times(0)).dispose();
    }

    @Test
    public void testSceneChangeOutside() {
        int delta = 42;

        manager.init();
        manager.update(delta);

        manager.changeScene(SceneB.NAME);

        manager.update(delta);
        manager.dispose();

        verify(sceneA, times(1)).init();
        verify(sceneA, times(1)).update(delta);
        verify(sceneA, times(1)).dispose();

        verify(sceneB, times(1)).init();
        verify(sceneB, times(1)).update(delta);
        verify(sceneB, times(1)).dispose();
    }

    @Test
    public void testSceneChangeInside() {
        SceneChanger changer = new SceneChanger();
        SceneChanger newSceneA = spy(changer);
        Scene newSceneB = spy(new SceneB());
        setup(newSceneA, newSceneB, SceneChanger.NAME);
        newSceneA.setManager(manager);

        int delta = 42;

        manager.init();

        manager.update(delta);

        manager.update(delta);

        manager.dispose();

        verify(sceneA, times(1)).init();
        verify(sceneA, times(1)).update(delta);
        verify(sceneA, times(1)).dispose();

        verify(sceneB, times(1)).init();
        verify(sceneB, times(1)).update(delta);
        verify(sceneB, times(1)).dispose();
    }

    @Test
    public void testGetScene() {
        SceneA resolvedSceneA = manager.getScene(SceneA.NAME);
        SceneB resolvedSceneB = manager.getScene(SceneB.NAME);

        assertThat(resolvedSceneA, is(this.sceneA));
        assertThat(resolvedSceneB, is(this.sceneB));
    }

    @Test(expected = ClassCastException.class)
    public void testWrongGetScene() {
        //CLASS IS IMPORTANT!
        SceneA scene = manager.getScene(SceneB.NAME);
        scene.init();
    }

    private class SceneA extends AbsStructObject implements Scene {
        public static final String NAME = "SceneA";

        public SceneA() {
            super(NAME, UUID.randomUUID());
        }

        @Override
        public CameraComponent getActiveCamera() {
            return null;
        }
    }

    private class SceneB extends AbsStructObject implements Scene {
        public static final String NAME = "SceneB";

        public SceneB() {
            super(NAME, UUID.randomUUID());
        }

        @Override
        public CameraComponent getActiveCamera() {
            return null;
        }
    }

    private class SceneChanger extends AbsStructObject implements Scene {
        public static final String NAME = "SceneChanger";
        private SceneManager myManager;

        public SceneChanger() {
            super(NAME, UUID.randomUUID());
        }

        public void setManager(SceneManager manager) {
            this.myManager = manager;
        }

        @Override
        public void update(int deltaInMs) {
            super.update(deltaInMs);

            this.myManager.changeScene(SceneB.NAME);
        }

        @Override
        public CameraComponent getActiveCamera() {
            return null;
        }
    }
}