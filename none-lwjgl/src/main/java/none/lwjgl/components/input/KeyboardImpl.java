package none.lwjgl.components.input;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import none.engine.component.input.Key;
import none.engine.component.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * LWJGl implementation for Keyboard.
 */
@Singleton
public class KeyboardImpl extends Keyboard {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyboardImpl.class);

    private List<Character> currentPressedCharacters;

    @Inject
    public KeyboardImpl() {
        currentPressedCharacters = new ArrayList<>();
    }

    @Override
    public List<Character> getCurrentCharacters() {
        return currentPressedCharacters;
    }

    @Override
    public void update(int deltaInMs) {
        currentPressedCharacters.clear();
        while (org.lwjgl.input.Keyboard.next()) {
            if (org.lwjgl.input.Keyboard.getEventKeyState()) {
                currentPressedCharacters.add(org.lwjgl.input.Keyboard.getEventCharacter());
            }
        }

        super.update(deltaInMs);
    }

    @Override
    protected boolean isIdDown(Key key) {
        return org.lwjgl.input.Keyboard.isKeyDown(resolveKey(key));
    }

    private int resolveKey(Key key) {
        switch (key) {
            case NONE:
                return org.lwjgl.input.Keyboard.KEY_NONE;
            case Q:
                return org.lwjgl.input.Keyboard.KEY_Q;
            case W:
                return org.lwjgl.input.Keyboard.KEY_W;
            case E:
                return org.lwjgl.input.Keyboard.KEY_E;
            case R:
                return org.lwjgl.input.Keyboard.KEY_R;
            case T:
                return org.lwjgl.input.Keyboard.KEY_T;
            case Y:
                return org.lwjgl.input.Keyboard.KEY_Y;
            case U:
                return org.lwjgl.input.Keyboard.KEY_U;
            case I:
                return org.lwjgl.input.Keyboard.KEY_I;
            case O:
                return org.lwjgl.input.Keyboard.KEY_O;
            case P:
                return org.lwjgl.input.Keyboard.KEY_P;
            case A:
                return org.lwjgl.input.Keyboard.KEY_A;
            case S:
                return org.lwjgl.input.Keyboard.KEY_S;
            case D:
                return org.lwjgl.input.Keyboard.KEY_D;
            case F:
                return org.lwjgl.input.Keyboard.KEY_F;
            case G:
                return org.lwjgl.input.Keyboard.KEY_G;
            case H:
                return org.lwjgl.input.Keyboard.KEY_H;
            case J:
                return org.lwjgl.input.Keyboard.KEY_J;
            case K:
                return org.lwjgl.input.Keyboard.KEY_K;
            case L:
                return org.lwjgl.input.Keyboard.KEY_L;
            case Z:
                return org.lwjgl.input.Keyboard.KEY_Z;
            case X:
                return org.lwjgl.input.Keyboard.KEY_X;
            case C:
                return org.lwjgl.input.Keyboard.KEY_C;
            case V:
                return org.lwjgl.input.Keyboard.KEY_V;
            case B:
                return org.lwjgl.input.Keyboard.KEY_B;
            case N:
                return org.lwjgl.input.Keyboard.KEY_N;
            case M:
                return org.lwjgl.input.Keyboard.KEY_M;
            case I_1:
                return org.lwjgl.input.Keyboard.KEY_1;
            case I_2:
                return org.lwjgl.input.Keyboard.KEY_2;
            case I_3:
                return org.lwjgl.input.Keyboard.KEY_3;
            case I_4:
                return org.lwjgl.input.Keyboard.KEY_4;
            case I_5:
                return org.lwjgl.input.Keyboard.KEY_5;
            case I_6:
                return org.lwjgl.input.Keyboard.KEY_6;
            case I_7:
                return org.lwjgl.input.Keyboard.KEY_7;
            case I_8:
                return org.lwjgl.input.Keyboard.KEY_8;
            case I_9:
                return org.lwjgl.input.Keyboard.KEY_9;
            case I_0:
                return org.lwjgl.input.Keyboard.KEY_0;
            case ESCAPE:
                return org.lwjgl.input.Keyboard.KEY_ESCAPE;
            case TAB:
                return org.lwjgl.input.Keyboard.KEY_TAB;
            case LEFT_SHIFT:
                return org.lwjgl.input.Keyboard.KEY_LSHIFT;
            case LEFT_CONTROL:
                return org.lwjgl.input.Keyboard.KEY_LCONTROL;
            case LEFT_ALT:
                return org.lwjgl.input.Keyboard.KEY_LMENU;
            case SPACE:
                return org.lwjgl.input.Keyboard.KEY_SPACE;
            case RIGHT_ALT:
                return org.lwjgl.input.Keyboard.KEY_RMENU;
            case RIGHT_CONTROL:
                return org.lwjgl.input.Keyboard.KEY_RCONTROL;
            case RIGHT_SHIFT:
                return org.lwjgl.input.Keyboard.KEY_RSHIFT;
            case ENTER:
                return org.lwjgl.input.Keyboard.KEY_RETURN;
            case BACKSPACE:
                return org.lwjgl.input.Keyboard.KEY_BACK;
            case UP:
                return org.lwjgl.input.Keyboard.KEY_UP;
            case DOWN:
                return org.lwjgl.input.Keyboard.KEY_DOWN;
            case LEFT:
                return org.lwjgl.input.Keyboard.KEY_LEFT;
            case RIGHT:
                return org.lwjgl.input.Keyboard.KEY_RIGHT;
            default:
                LOGGER.error("Key {} is not implemented!", key);
                return org.lwjgl.input.Keyboard.KEY_NONE;
        }
    }
}
