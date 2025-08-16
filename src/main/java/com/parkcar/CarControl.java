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

    public boolean up, down, left, right;
    private float carHeading = 0f;
    private float accel = 8f;
    private float steerAngle = 0f; // current front wheel angle (radians)

        private float maxSpeed = 300f;    // px/sec^2
        private float wheelBase = 140f;
        private float velocity = 0f;      // current velocity in px/sec
        private float theta = 0f;
            private Vector3f position = new Vector3f();
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
        
        Vector3f fwt = frontWheel.getWorldTranslation();
        Vector3f bwt = backWheel.getWorldTranslation();
        
        carHeading = FastMath.atan2(fwt.getY() - bwt.getY(), fwt.getX() - bwt.getX());
        
        Vector3f bwDir = new Vector3f(FastMath.cos(carHeading), FastMath.sin(carHeading), 0);
        Vector3f fwDir = new Vector3f(FastMath.cos(carHeading + steerAngle), FastMath.sin(carHeading + steerAngle), 0);

        Vector3f bwNewPos = bwt.add(bwDir.mult(velocity * tpf));
        Vector3f fwNewPos = fwt.add(fwDir.mult(velocity * tpf));
        
        backWheel.setLocalTranslation(bwNewPos);
        frontWheel.setLocalTranslation(fwNewPos);
        
        
        frontWheel.setLocalRotation(new Quaternion().fromAngleAxis(steerAngle, Vector3f.UNIT_Z));

   
    }
    
    private void handleSteerInput(float tpf){
        if (right){
            steerAngle -= tpf * 2f;
        } else if (left){
            steerAngle += tpf * 2f;
        } else{
            steerAngle = FastMath.interpolateLinear(0.2f, steerAngle, 0);
        }
        steerAngle = FastMath.clamp(steerAngle, -STEER_ANGLE, STEER_ANGLE);

    }
    
    private void handleAccelerationInput(){
        if (up){
            velocity += accel;
        }
        else if (down){
            velocity -= accel;
        } else{
            velocity = 0;
        }
        velocity = FastMath.clamp(velocity, -maxSpeed, maxSpeed);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp){
        
    }
    
    
    public float degToRad(float deg){
        return deg * FastMath.DEG_TO_RAD;
    }
}