package com.parkcar;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.ui.Picture;
import com.jme3.math.Vector3f;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.KeyInput;

import com.parkcar.CarControl;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class ParkCar extends SimpleApplication implements ActionListener {

    private Picture car;
    
    public static void main(String[] args) {
        ParkCar app = new ParkCar();
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        car = new Picture("Car picture");
        
        // Camera for 2d
        cam.setParallelProjection(true);
        cam.setLocation(new Vector3f(0,0,0.5f));
        getFlyByCamera().setEnabled(false);
        
        car.setImage(assetManager, "Textures/mainCar.png", true);
        car.setWidth(settings.getWidth()/4);
        car.setHeight(settings.getHeight()/2);
        car.setPosition(settings.getWidth()/4, settings.getHeight()/4);
        car.addControl(new CarControl(settings.getWidth(), settings.getHeight()));
        registerInput();
        guiNode.attachChild(car);
    }
    
    public void registerInput(){
        inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addListener(this, "up", "down");
    }
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf){
        CarControl carControl = car.getControl(CarControl.class);
        if ("up".equals(name)){
            carControl.up = isPressed;
        }
        else if ("down".equals(name)){
            carControl.down = isPressed;

        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}

