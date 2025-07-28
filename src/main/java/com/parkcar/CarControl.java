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

/**
 *
 * @author arpyg
 */
public class CarControl extends AbstractControl {
    
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
        Vector3f cords = new Vector3f(0, 0, 0);
        Spatial car = spatial;

        System.out.println(car.getClass());
        // Need: car position centre
        // Wheel baseline and two wheels at the ends
        // Rotate rear one
        // Move in the direction it's pointing

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
}
