/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.parkcar;


import com.jme3.scene.control.AbstractControl;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.material.Material;

import com.jme3.scene.Geometry;
import com.jme3.math.Vector3f;
import com.jme3.ui.Picture;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
/**
 *
 * @author arpyg
 */
public class CarControl extends AbstractControl {

    private final float STEER_ANGLE = FastMath.PI / 8;

    public boolean up, down, left, right;
    private final float speed = 5f;
    
    private int width, height;
    
    public CarControl(int width, int height){
        // ?? 
        this.width = width;
        this.height = height;
    }
    
    @Override
    protected void controlUpdate(float timePerFrame){
        Vector3f cords = new Vector3f(0, 0, 230);
        Node car = (Node) spatial;

        Geometry frontWheel = (Geometry) car.getChild("frontWheel");
        // Need: car position centre
        // Wheel baseline and two wheels at the ends
        // Rotate rear one -- CURRENT how to move rear wheel?
        // Move in the direction it's pointing

        
        Quaternion currentWheelRotation = frontWheel.getLocalRotation();
        Vector3f axis = new Vector3f();
        float angle = currentWheelRotation.toAngleAxis(axis) * 0.5f * 0.5f;
        
        Quaternion carRotation = car.getLocalRotation();
        
        Vector3f carRotated = carRotation.mult(Vector3f.UNIT_Z);
        Vector3f rotated = currentWheelRotation.mult(Vector3f.UNIT_Z);
        
        System.out.println(angle);

//                System.out.println(carRotation.mult(currentWheelRotation));
//                System.out.println(carRotation.add(currentWheelRotation));

        // Go with car in direction pointe dby wheel
        // https://hub.jmonkeyengine.org/t/solved-rotating-vector3f-by-angle/36168/7
        Quaternion newQ = new Quaternion();
        
        if (right){
            newQ.fromAngleAxis(STEER_ANGLE, new Vector3f(0, 0, -1));
        } else if (left){
            newQ.fromAngleAxis(STEER_ANGLE, new Vector3f(0, 0, 1));
        } else{
            newQ.fromAngleAxis(STEER_ANGLE, new Vector3f(0, 0, 0));
        }
        frontWheel.setLocalRotation(newQ);
//        Vector3f newWheelPos = q.mult(currentWheelPosition);
//        frontWheel.setLocalTranslation(newWheelPos);

        if (up){
            cords.setY(speed);
        }
        else if (down){
            cords.setY(-speed);
        }
        
        car.move(cords);
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp){
        
    }
    
    
    public float degToRad(float deg){
        return deg * FastMath.DEG_TO_RAD;
    }
}
