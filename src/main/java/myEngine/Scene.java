package myEngine;

public abstract class Scene {




    public Scene(){
        init();
    }

    public void init (){

    }

    public abstract  void update(float dt);
}
