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
import com.parkcar.controls.CarControl;
import com.parkcar.states.ViewState;
import io.github.jmecn.tiled.TmxLoader;
import io.github.jmecn.tiled.core.MapObject;
import io.github.jmecn.tiled.core.ObjectGroup;
import io.github.jmecn.tiled.core.TiledMap;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.World;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class ParkCar extends SimpleApplication {

    private static final String START_POINT_STR = "start_point";
    private static final String END_POINT_STR = "end_point";
    private static final String DEATH_POINT_STR = "death";
    
    private Picture car;
    private static ParkCar app = new ParkCar();
    private Node carNode;
    private Vector3f carLocation = new Vector3f(100, 150, 0);
    private float carHeading = 0f;
    private final float WHEEL_BASE = 180f;
    Vector3f wheelLocation = new Vector3f(FastMath.cos(carHeading), FastMath.sin(carHeading), 0).mult(WHEEL_BASE / 2); // Half from center
    private World<Body> world;

    public static void main(String[] args) {        
        AppSettings defaultSetting = new AppSettings(true);
        defaultSetting.setTitle("ParkCar");
        defaultSetting.setResolution(30 * 32, 20 * 32);
        defaultSetting.setFrequency(60);

        app.setShowSettings(false);
        app.setSettings(defaultSetting);
        app.start();
    }
    
    private final ActionListener actionListener = new ActionListener(){
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
    }; 
    
    @Override
    public void simpleInitApp() {
        world = new World();
        world.setGravity(new Vector2(0.0, -9.8)); // Gravity pointing down
        // register it
        assetManager.registerLoader(TmxLoader.class, "tmx", "tsx");

        // load tmx with it
        TiledMap map = (TiledMap) assetManager.loadAsset("Maps/map.tmx");

        // Load tileset by code
//        Tileset tileset = map.getTileset("CP_V1.0.4");

        ViewState viewState = new ViewState();
        viewState.initialize(stateManager, this);
        viewState.setMap(map);
        stateManager.attach(viewState);
        
//        MapRenderer mapRenderer = viewState.getMapRenderer();
//        mapRenderer.render();// prepare for physics
        
        
        // Render all layers
        
        // Render ob jects for collision detection
        ObjectGroup objects = (ObjectGroup) map.getLayer("objects");
        for (MapObject obj : objects.getObjects()) {
            String objName = obj.getName();
            float objX = (float)obj.getX();
            float objY = (float)obj.getY();
            float objHeight = (float)obj.getHeight();
            float objWidth = (float)obj.getWidth();

            if (START_POINT_STR.equals(objName)){                  
                float newY = 32 * 20 - (objY + objHeight);
                float newX = objX+ (objWidth / 4);
                carLocation = new Vector3f(newX, newY, 0);
            }
            if (END_POINT_STR.equals(objName)){}
            if (DEATH_POINT_STR.equals(objName)){
                Body deathBody = new Body();
                Rectangle deathShape = new Rectangle(objWidth, objHeight);
                deathBody.addFixture(deathShape);
                deathBody.setMass(MassType.INFINITE);
                deathBody.translate(objX, objY);
                world.addBody(deathBody);
            }
        }
        
//        PhysicsState physicsState = new PhysicsState();
//        physicsState.initialize(stateManager, this);
//        stateManager.attach(physicsState);

        // Camera for 2d
        cam.setParallelProjection(true);
        cam.setLocation(new Vector3f(0, 0, 0.5f));
        getFlyByCamera().setEnabled(false);

        Node carHolderNode = new Node("carHolder");

        carNode = new Node();
        carNode.setUserData("wheelBase", WHEEL_BASE);
        carNode.addControl(new CarControl(settings.getWidth(), settings.getHeight()));
        
        addCar(carHolderNode);
        registerInput();
        addWheels(carNode);
        
        carNode.setLocalTranslation(carLocation);
        carNode.attachChild(carHolderNode);

        guiNode.attachChild(carNode);
    }

    private void addCar(Node carHolderNode){
        car = new Picture("car");
        car.setImage(assetManager, "Textures/mainCar.png", true);
        car.setWidth(150);
        car.setHeight(300);
        car.setLocalTranslation(-130f, 100f, 0); // allign car over the wheels
        car.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.PI * -90 / 180, Vector3f.UNIT_Z)); // rotate in wheel direction
        
        // Physics shape
        Body carBody = new Body();
        Rectangle carShape = new Rectangle(150, 300);
        carBody.addFixture(new BodyFixture(carShape));
        carBody.setMass(MassType.NORMAL);
        carBody.translate(-130f, 100f);
        world.addBody(carBody);
        
        carHolderNode.attachChild(car);
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

        inputManager.addListener(actionListener, up, down, left, right);
    }

    public void addWheels(Node carNode) {
        Quad wheel = new Quad(20f, 30f);
        Material wheelMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wheelMat.setColor("Color", ColorRGBA.Blue);
        Vector3f fWheelLocation = carLocation.add(wheelLocation);
        Vector3f bWheelLocation = carLocation.subtract(wheelLocation);

//                System.out.println("Front " + carLocation.add(wheelLocation));
//        System.out.println("Back: " + carLocation.subtract(wheelLocation));
        Quad frontWheel = (Quad) wheel.clone();
        Geometry fGeomWheel = new Geometry("frontWheel", frontWheel);
        fGeomWheel.setMaterial(wheelMat);

        Node pivotNode = new Node("fWheelPivot");
        pivotNode.attachChild(fGeomWheel);
        pivotNode.setLocalTranslation(fWheelLocation);

        Quad backWheel = (Quad) wheel.clone();
        Geometry bGeomWheel = new Geometry("backWheel", backWheel);
        bGeomWheel.setMaterial(wheelMat);
        bGeomWheel.setLocalTranslation(bWheelLocation);

        carNode.attachChild(pivotNode);
        carNode.attachChild(bGeomWheel);
    }


    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        world.step(1);
        if(rootNode.getChild("car") != null){
            rootNode.getChild("car").setLocalTranslation(0, (float)world.getBody(0).getTransform().getTranslation().y, 0);   
        }
        System.out.println(world.getBody(0).getTransform().getTranslation());
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
