package none.lwjgl.components.input;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import none.engine.component.input.Key;
import none.engine.component.input.KeyboardComponent;
import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LWJGl implementation for KeyboardComponent.
 */
@Singleton
public class KeyboardComponentImpl extends KeyboardComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyboardComponentImpl.class);

    @Inject
    public KeyboardComponentImpl() {

    }

    @Override
    protected boolean isIdDown(Key key) {
        return Keyboard.isKeyDown(resolveKey(key));
    }

    private int resolveKey(Key key) {
        switch (key) {
            case NONE:
                return Keyboard.KEY_NONE;
            case Q:
                return Keyboard.KEY_Q;
            case W:
                return Keyboard.KEY_W;
            case E:
                return Keyboard.KEY_E;
            case R:
                return Keyboard.KEY_R;
            case T:
                return Keyboard.KEY_T;
            case Y:
                return Keyboard.KEY_Y;
            case U:
                return Keyboard.KEY_U;
            case I:
                return Keyboard.KEY_I;
            case O:
                return Keyboard.KEY_O;
            case P:
                return Keyboard.KEY_P;
            case A:
                return Keyboard.KEY_A;
            case S:
                return Keyboard.KEY_S;
            case D:
                return Keyboard.KEY_D;
            case F:
                return Keyboard.KEY_F;
            case G:
                return Keyboard.KEY_G;
            case H:
                return Keyboard.KEY_H;
            case J:
                return Keyboard.KEY_J;
            case K:
                return Keyboard.KEY_K;
            case L:
                return Keyboard.KEY_L;
            case Z:
                return Keyboard.KEY_Z;
            case X:
                return Keyboard.KEY_X;
            case C:
                return Keyboard.KEY_C;
            case V:
                return Keyboard.KEY_V;
            case B:
                return Keyboard.KEY_B;
            case N:
                return Keyboard.KEY_N;
            case M:
                return Keyboard.KEY_M;
            case I_1:
                return Keyboard.KEY_1;
            case I_2:
                return Keyboard.KEY_2;
            case I_3:
                return Keyboard.KEY_3;
            case I_4:
                return Keyboard.KEY_4;
            case I_5:
                return Keyboard.KEY_5;
            case I_6:
                return Keyboard.KEY_6;
            case I_7:
                return Keyboard.KEY_7;
            case I_8:
                return Keyboard.KEY_8;
            case I_9:
                return Keyboard.KEY_9;
            case I_0:
                return Keyboard.KEY_0;
            case ESCAPE:
                return Keyboard.KEY_ESCAPE;
            case TAB:
                return Keyboard.KEY_TAB;
            case LEFT_SHIFT:
                return Keyboard.KEY_LSHIFT;
            case LEFT_CONTROL:
                return Keyboard.KEY_LCONTROL;
            case LEFT_ALT:
                return Keyboard.KEY_LMENU;
            case SPACE:
                return Keyboard.KEY_SPACE;
            case RIGHT_ALT:
                return Keyboard.KEY_RMENU;
            case RIGHT_CONTROL:
                return Keyboard.KEY_RCONTROL;
            case RIGHT_SHIFT:
                return Keyboard.KEY_RSHIFT;
            case ENTER:
                return Keyboard.KEY_RETURN;
            case BACKSPACE:
                return Keyboard.KEY_BACK;
            case UP:
                return Keyboard.KEY_UP;
            case DOWN:
                return Keyboard.KEY_DOWN;
            case LEFT:
                return Keyboard.KEY_LEFT;
            case RIGHT:
                return Keyboard.KEY_RIGHT;
            default:
                LOGGER.error("Key {} is not implemented!", key);
                return Keyboard.KEY_NONE;
        }
    }
}
