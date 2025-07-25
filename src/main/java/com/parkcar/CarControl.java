/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.parkcar;


import com.jme3.scene.control.AbstractControl;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.math.Vector3f;

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
        Spatial car = spatial;
        Vector3f cords = new Vector3f(0, 0, 0);
        // TODO read
        // https://engineeringdotnet.blogspot.com/2010/04/simple-2d-car-physics-in-games.html
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
