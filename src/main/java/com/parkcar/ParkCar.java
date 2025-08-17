package com.parkcar;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class ParkCar extends SimpleApplication implements ActionListener {

    private Picture car;
    private Node carNode;
    private Vector3f carLocation = new Vector3f(150, 10, 0);
    private float carHeading = 0f;

    public static void main(String[] args) {
        ParkCar app = new ParkCar();
  
        AppSettings defaultSetting = new AppSettings(true);
        defaultSetting.setTitle("ParkCar");
        defaultSetting.setHeight(950);
        defaultSetting.setWidth(1600);
        defaultSetting.setFrequency(60);

        app.setShowSettings(false);
        app.setSettings(defaultSetting);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        // Camera for 2d
        cam.setParallelProjection(true);
        cam.setLocation(new Vector3f(0, 0, 0.5f));
        getFlyByCamera().setEnabled(false);
      
        settings.setWidth(5000);
        settings.setHeight(5000);
//        car = new Picture("car");
//        car.setImage(assetManager, "Textures/mainCar.png", true);
//        car.setWidth(settings.getWidth() / 4);
//        car.setHeight(settings.getHeight() / 2);
//        car.setPosition(200, 200);

        carNode = new Node();
        carNode.addControl(new CarControl(settings.getWidth(), settings.getHeight()));
        registerInput();

//        carNode.attachChild(car);
        addWheels(carNode);
        carNode.setLocalTranslation(carLocation);

        guiNode.attachChild(carNode);
    }

    public void registerInput() {
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

    public void addWheels(Node carNode) {
    
        float wheelBase = 140f;

        float wheelWidth = 20f;
        float wheelHeight = 30f;
        
        Vector3f fWheelLocation = carLocation.add(new Vector3f(FastMath.cos(carHeading), FastMath.sin(carHeading), 0).mult(wheelBase / 2));
        Vector3f bWheelLocation = carLocation.subtract(new Vector3f(FastMath.cos(carHeading), FastMath.sin(carHeading), 0).mult(wheelBase / 2));

        Quad frontWheel = new Quad(wheelWidth, wheelHeight);
        Geometry fGeomWheel = new Geometry("frontWheel", frontWheel);
        Material fMatWheel = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        fMatWheel.setColor("Color", ColorRGBA.Blue);
        fGeomWheel.setMaterial(fMatWheel);
//        fGeomWheel.setLocalTranslation(270, frontWheelY, 0);

        Node pivotNode = new Node("fWheelPivot");
        pivotNode.attachChild(fGeomWheel);
        pivotNode.setLocalTranslation(fWheelLocation);
        
        Quad backWheel = new Quad(wheelWidth, wheelHeight);
        Geometry bGeomWheel = new Geometry("backWheel", backWheel);
        Material bMatWheel = fMatWheel.clone();
        bGeomWheel.setMaterial(bMatWheel);
        bGeomWheel.setLocalTranslation(bWheelLocation);
        
        carNode.attachChild(pivotNode);
        carNode.attachChild(bGeomWheel);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        CarControl carControl = carNode.getControl(CarControl.class);
        if ("left".equals(name)) {
            carControl.left = isPressed;
        } else if ("right".equals(name)) {
            carControl.right = isPressed;
        }

        if ("up".equals(name)) {
            carControl.up = isPressed;

        } else if ("down".equals(name)) {
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
