package none.lwjgl.components.assets;

import none.engine.component.common.uuid.RandomUUID;
import none.engine.component.model.Face;
import none.engine.component.model.Model;
import none.engine.component.model.Vertex;
import org.joml.Vector2d;
import org.joml.Vector3d;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Unit-Test for ObjModelHandler.
 */
public class ObjModelHandlerTest {
    Vertex first = new Vertex(new Vector3d(1, 1, 0), new Vector3d(0.5, 0.5, 0), new Vector2d(0, 0.1));
    Vertex second = new Vertex(new Vector3d(1, -1, 0), new Vector3d(0.5, -0.5, 0), new Vector2d(0.2, 0.3));
    Vertex third = new Vertex(new Vector3d(-1, 1, 0), new Vector3d(-0.5, 0.5, 0), new Vector2d(0.4, 0.5));
    Vertex forth = new Vertex(new Vector3d(-1, -1, 0), new Vector3d(-0.5, -0.5, 0), new Vector2d(0.6, 0.7));

    @Test
    public void testLoadModel() {
        String fileName = "models/quad.obj";
        ObjModelHandler modelHandler = new ObjModelHandler(new RandomUUID(), new LwjglAssets());
        Model model = modelHandler.loadModel(fileName);

        assertNotNull(model);
        assertThat(model.getFaces().size(), is(2));
        assertThat(model.getVertices().size(), is(4));
        assertThat(model.getSourcePath(), equalTo(fileName));

        assertCorrectVertices(model.getVertices());
        assertIsCorrectFace(model.getFaces().get(0));
        assertIsCorrectFace(model.getFaces().get(1));

        modelHandler.disposeModel(model);
    }

    private void assertCorrectVertices(List<Vertex> vertices) {
        assertTrue(vertices.contains(first));
        assertTrue(vertices.contains(second));
        assertTrue(vertices.contains(third));
        assertTrue(vertices.contains(forth));
    }

    private void assertIsCorrectFace(Face face) {
        Vertex[] vertices = face.getVertices();

        if (vertices[0].equals(first)) {
            //f 1/1/1 2/2/2 3/3/3
            assertThat(vertices[1], equalTo(second));
            assertThat(vertices[2], equalTo(third));

        } else if (vertices[0].equals(second)) {
            //f 2/2/2 4/4/4 3/3/3
            assertThat(vertices[1], equalTo(forth));
            assertThat(vertices[2], equalTo(third));

        } else {
            fail("Object File did not load as expected.");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void loadNotExistingModel() {
        String fileName = "models/rlyNotExisting.obj";
        ObjModelHandler modelHandler = new ObjModelHandler(new RandomUUID(), new LwjglAssets());
        modelHandler.loadModel(fileName);
    }

    @Test(expected = IllegalStateException.class)
    public void loadModelWithContentError() {
        String fileName = "models/quad3DTexture.obj";
        ObjModelHandler modelHandler = new ObjModelHandler(new RandomUUID(), new LwjglAssets());
        modelHandler.loadModel(fileName);
    }
}