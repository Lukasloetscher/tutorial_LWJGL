package myEngine;

public class LevelScene extends Scene{
    public LevelScene(){

    }

    @Override
    public void update(float dt) {
        System.out.println("FPS: " + (1.0 / dt));
    }
}
