# None Game Engine

A simple GameEngine.

## For a showcase have a look at none-goldminer

Valid Keyboard-Input:
    Enter, Escape, Arrow, Space and the 1 Key.

With IDE:
Needed VM-Arguments:
    -Djava.library.path=target/natives

Working Path: (replace /path/to)
    /path/to/none/none-goldminer

On Console: (replace /path/to)
```bash
cd /path/to/none/none-goldminer/target
java -Djava.library.path="natives/" -jar none-goldminer-0.1-SNAPSHOT.jar
```

Developed and tested on Linux/Fedora 22 (with Nouveau driver)

## Want to find out whats needed to build own game?

Have a look at.

* none/none-lwjgl/src/test/none/lwjgl/components/physic/PhysicSandbox
* none/none-lwjgl/src/test/none/lwjgl/components/renderer/RendererApp

Basically you describe your game in an composite-Pattern of EngineObject's, what you have to do:

* Write at least one class implementing the Scene-Interface. (Containing the Tree)
* In case you want something rendered, you need one parent EngineObject with three children. A (Mesh OR Sprite), Texture and an TransformComponent.
* In case you want keyboard input, create a class that implements the Command-Interface. Register this class to the KeyboardComponent.
