package myEngine;

public abstract class Scene {




    public Scene(){
        //init();  Sadly this does not work with the current structure... -> Todo change structure.
    }

    public void init (){

    }

    public abstract  void update(float dt);
}
