package com.parkcar;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
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
    private Vector3f carLocation = new Vector3f(300, 150, 0);
    private float carHeading = 0f;
    private final float WHEEL_BASE = 180f;
    Vector3f wheelLocation = new Vector3f(FastMath.cos(carHeading), FastMath.sin(carHeading), 0).mult(WHEEL_BASE / 2); // Half from center

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

        Node carHolderNode = new Node("carHolder");
        
        car = new Picture("car");
        car.setImage(assetManager, "Textures/mainCar.png", true);
        car.setWidth(150);
        car.setHeight(300);
        car.setLocalTranslation(-130f, 100f, 0); // allign car over the wheels
        car.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.PI * -90 / 180, Vector3f.UNIT_Z)); // rotate in wheel direction

        carNode = new Node();
        carNode.setUserData("wheelBase", WHEEL_BASE);
        carNode.addControl(new CarControl(settings.getWidth(), settings.getHeight()));
        carHolderNode.attachChild(car);

        registerInput();
        addWheels(carNode);
        
        carNode.setLocalTranslation(carLocation);
        carNode.attachChild(carHolderNode);
        
//        carNode.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.PI / 2, Vector3f.UNIT_Z));
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

    public void addCar(Node carNode){
        
    }
    
    public void addWheels(Node carNode) {
        Quad wheel = new Quad(20f, 30f);
        Material wheelMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wheelMat.setColor("Color", ColorRGBA.Blue);
//        Vector3f fWheelLocation = carLocation.add(wheelLocation);
//        Vector3f bWheelLocation = carLocation.subtract(wheelLocation);

//                System.out.println("Front " + carLocation.add(wheelLocation));
//        System.out.println("Back: " + carLocation.subtract(wheelLocation));

        Quad frontWheel = (Quad) wheel.clone();
        Geometry fGeomWheel = new Geometry("frontWheel", frontWheel);
        fGeomWheel.setMaterial(wheelMat);

        Node pivotNode = new Node("fWheelPivot");
        pivotNode.attachChild(fGeomWheel);
//        pivotNode.setLocalTranslation(fWheelLocation);
        
        Quad backWheel = (Quad) wheel.clone();
        Geometry bGeomWheel = new Geometry("backWheel", backWheel);
        bGeomWheel.setMaterial(wheelMat);
//        bGeomWheel.setLocalTranslation(bWheelLocation);
        
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
