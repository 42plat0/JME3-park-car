package com.parkcar;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;

import com.jme3.scene.shape.Box;
import com.jme3.ui.Picture;

import com.jme3.scene.Node;

import com.jme3.math.Vector3f;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.KeyInput;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;


import com.parkcar.CarControl;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class ParkCar extends SimpleApplication implements ActionListener {

    private Picture car;
    private Node carNode;
    
    public static void main(String[] args) {
        ParkCar app = new ParkCar();
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {        
        // Camera for 2d
        cam.setParallelProjection(true);
        cam.setLocation(new Vector3f(0,0,0.5f));
        getFlyByCamera().setEnabled(false);
        
        
        car = new Picture("Car picture");
        car.setImage(assetManager, "Textures/mainCar.png", true);
        car.setWidth(settings.getWidth()/4);
        car.setHeight(settings.getHeight()/2);
        car.setPosition(200, 200);
        
        carNode = new Node();
        Quad frontWheel = new Quad(50f, 30f);
        Geometry fGeomWheel = new Geometry("front wheel", frontWheel);
        Material fMatWheel = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        fMatWheel.setColor("Color", ColorRGBA.Blue);
        fGeomWheel.setMaterial(fMatWheel);
        fGeomWheel.setLocalTranslation(250, 220, 0);
        
        Quad backWheel = new Quad(50f, 30f);
        Geometry bGeomWheel = new Geometry("back wheel", backWheel);
        Material bMatWheel = fMatWheel.clone();
        bGeomWheel.setMaterial(bMatWheel);
        bGeomWheel.setLocalTranslation(250, 340, 0);
        
        
        carNode.addControl(new CarControl(settings.getWidth(), settings.getHeight()));
        registerInput();

        carNode.attachChild(car);
        carNode.attachChild(fGeomWheel);
        carNode.attachChild(bGeomWheel);

        guiNode.attachChild(carNode);
    }
    
    public void registerInput(){
        String up = "up", down = "down", left = "left", right = "right";
        
        inputManager.addMapping(up, new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(up, new KeyTrigger(KeyInput.KEY_UP));
        
        inputManager.addMapping(right, new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(right, new KeyTrigger(KeyInput.KEY_RIGHT));

        inputManager.addMapping(left, new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(left, new KeyTrigger(KeyInput.KEY_LEFT));

        
        inputManager.addMapping(down, new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(down, new KeyTrigger(KeyInput.KEY_DOWN));

        inputManager.addListener(this, up, down, left, right);
    }
        
    @Override
    public void onAction(String name, boolean isPressed, float tpf){
        CarControl carControl = carNode.getControl(CarControl.class);
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

