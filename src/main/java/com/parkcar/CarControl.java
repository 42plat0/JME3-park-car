/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.parkcar;


import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
/**
 *
 * @author arpyg
 */
public class CarControl extends AbstractControl {

    private final float STEER_ANGLE = FastMath.PI / 8;
    private final float MAX_SPEED = 300f;
    private float accel = 8f;

    public boolean up, down, left, right;
    private float carHeading = 0f;
    private float steerAngle = 0f; // current front wheel angle (radians)
    private float currAngle = 0f;
    private float velocity = 0f;      // current velocity in px/sec

    private int width, height;
    
    public CarControl(int width, int height){
        // ?? 
        this.width = width;
        this.height = height;
    }
    
    @Override
    protected void controlUpdate(float tpf){        
        Node car = (Node) spatial;
        Node frontWheel = (Node) car.getChild("fWheelPivot");
        Geometry backWheel = (Geometry) car.getChild("backWheel");

        handleSteerInput(tpf);
        handleAccelerationInput();
        
        Vector3f carLocation = car.getLocalTranslation();
        
        // Deal with front wheel location updates on input
        Vector3f fWheelLocation = carLocation.add(new Vector3f(FastMath.cos(carHeading), FastMath.sin(carHeading), 0).mult(140f / 2));
        Vector3f fwt = frontWheel.getLocalTranslation();
        Vector3f fwDir = new Vector3f(FastMath.cos(carHeading + steerAngle), FastMath.sin(carHeading + steerAngle), 0);
        // Calculate new position based on calculated instead of retrieved location
        // to prevent stacking wheels on top and making the whole thing crumble to horrible dust
        Vector3f fwNewPos = fWheelLocation.add(fwDir.mult(velocity * tpf));
        frontWheel.setLocalTranslation(fwNewPos);
        
        // Deal with back wheel location updates on input
        Vector3f bWheelLocation = carLocation.subtract(new Vector3f(FastMath.cos(carHeading), FastMath.sin(carHeading), 0).mult(140f / 2));
        Vector3f bwt = backWheel.getLocalTranslation();
        Vector3f bwDir = new Vector3f(FastMath.cos(carHeading), FastMath.sin(carHeading), 0);
        // Calculate new position based on calculated instead of retrieved location 
        // to prevent stacking wheels on top and making the whole thing crumble to horrible dust
        Vector3f bwNewPos = bWheelLocation.add(bwDir.mult(velocity * tpf));
        backWheel.setLocalTranslation(bwNewPos);
        
        // Update car location
        car.setLocalTranslation(bwNewPos.add(fwNewPos).divide(2));
        carHeading = FastMath.atan2(fwt.getY() - bwt.getY(), fwt.getX() - bwt.getX());

       
        Quaternion fwCurrRot = frontWheel.getLocalRotation();
        Quaternion ROTATION_ANGLE = new Quaternion().fromAngleAxis(currAngle, Vector3f.UNIT_Z);
        
        frontWheel.setLocalRotation(fwCurrRot);
   
    }
    
    private void handleSteerInput(float tpf){
        float ANGLE = tpf * 2f;
        
        if (right){
            steerAngle -= ANGLE;
        } else if (left){
            steerAngle += ANGLE;
        } else{
            steerAngle = FastMath.interpolateLinear(0.2f, steerAngle, 0);
        }
        steerAngle = FastMath.clamp(steerAngle, -STEER_ANGLE, STEER_ANGLE);
        
        currAngle += steerAngle;
    }
    
    private void handleAccelerationInput(){
        if (up){
            velocity += accel;
        }
        else if (down){
            velocity -= accel;
        } else{
            velocity = FastMath.interpolateLinear(0.5f, velocity, 0);
        }
        velocity = FastMath.clamp(velocity, -MAX_SPEED, MAX_SPEED);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp){
        
    }
    
    
    public float degToRad(float deg){
        return deg * FastMath.DEG_TO_RAD;
    }
}