package none.engine.component.renderer;

import none.engine.component.AbsObject;
import none.engine.component.Transform;
import none.engine.component.renderer.primitives.Mesh;
import none.engine.component.renderer.primitives.Sprite;
import none.engine.component.renderer.primitives.Text;
import org.apache.commons.lang3.Validate;

import java.util.UUID;

/**
 * A Renderable.
 */
public class Renderable extends AbsObject {

    private final Type type;
    private final boolean visible;

    private final Mesh mesh;
    private final Sprite sprite;
    private final Text text;

    private final Texture texture;

    private final Transform transform;

    public Renderable(String name, UUID id, Text text, Transform transform) {
        this(name, id, null, null, text, null, transform, Type.TEXT_BASED);
    }

    public Renderable(String name, UUID id, Sprite sprite, Texture texture, Transform transform) {
        this(name, id, null, sprite, null, texture, transform, Type.SPRITE_BASED);
    }

    public Renderable(String name, UUID id, Mesh mesh, Texture texture, Transform transform) {
        this(name, id, mesh, null, null, texture, transform, Type.MESH_BASED);
    }

    private Renderable(String name, UUID id, Mesh mesh, Sprite sprite, Text text, Texture texture, Transform transform, Type type) {
        super(name, id);

        Validate.notNull(transform);

        if (type == Type.MESH_BASED) {
            Validate.notNull(mesh);
            Validate.notNull(texture);
        } else if (type == Type.SPRITE_BASED) {
            Validate.notNull(sprite);
            Validate.notNull(texture);
        } else if (type == Type.TEXT_BASED) {
            Validate.notNull(text);
        }

        this.type = type;
        this.mesh = mesh;
        this.sprite = sprite;
        this.text = text;
        this.texture = texture;
        this.transform = transform;

        this.visible = true;
    }

    public boolean isVisible() {
        return visible;
    }

    public Type getType() {
        return type;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Text getText() {
        return text;
    }

    public Texture getTexture() {
        return texture;
    }

    public Transform getTransform() {
        return transform;
    }

    public enum Type {
        MESH_BASED,
        SPRITE_BASED,
        TEXT_BASED
    }
}
