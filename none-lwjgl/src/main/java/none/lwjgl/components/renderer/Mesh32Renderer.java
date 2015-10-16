package none.lwjgl.components.renderer;

import none.engine.Game;
import none.engine.component.AbsObject;
import none.engine.component.common.uuid.UUIDFactory;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

/**
 * Renderer for a Player.
 */
public class Mesh32Renderer extends AbsObject {

    public Mesh32Renderer(UUIDFactory factory, Game game) {
        super("Player32Renderer", factory.createUUID(), game);
    }

    public void draw(GlMesh mesh, GlTexture texture) {
        activeTexture(texture);
        drawOpenGl(mesh);
    }

    private void activeTexture(GlTexture texture) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureId());
    }

    private void drawOpenGl(GlMesh mesh) {
        GL30.glBindVertexArray(mesh.getVertexArrayId());

        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndicesCount(), GL11.GL_UNSIGNED_BYTE, 0);

        GL30.glBindVertexArray(0);
    }
}
