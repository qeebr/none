package none.lwjgl.components.assets;

import jdk.nashorn.internal.ir.annotations.Ignore;
import none.engine.component.assets.Assets;
import none.engine.component.common.uuid.RandomUUID;
import none.lwjgl.components.renderer.GlMesh;
import org.joml.Vector3d;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ObjMeshHandlerTest {

    @Test
    @Ignore
    public void testLoadMesh() throws Exception {
        ObjMeshHandlerTestable testable = new ObjMeshHandlerTestable();
        testable.loadMesh("models/simpleCube.obj");
    }

    class TestAssets extends Assets {

    }

    class ObjMeshHandlerTestable extends ObjMeshHandler {
        ObjMeshHandlerTestable() {
            super(new RandomUUID(), new TestAssets());
        }

        @Override
        protected GlMesh handoverOpenGl(String path, List<Integer> indices, List<Vector3d> uvs, List<Vector3d> normals,
                                        FloatBuffer verticesBuffer, FloatBuffer normalBuffer, FloatBuffer textureBuffer, ByteBuffer indicesBuffer) {
            assertThat(indices.size(), is(36));
            assertThat(uvs.size(), is(8));
            assertThat(normals.size(), is(8));

            return new GlMesh(UUID.randomUUID(), path, 0, 0, 0, false, false, false, new ArrayList<>());
        }
    }
}