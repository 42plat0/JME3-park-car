/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.parkcar.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.world.World;

/**
 *
 * @author arpyg
 */
public class PhysicsState extends BaseAppState {
    private SimpleApplication app;
    private World<Body> world = new World<Body>();
    
    @Override
    protected void initialize(Application app) {
        //It is technically safe to do all initialization and cleanup in the
        //onEnable()/onDisable() methods. Choosing to use initialize() and
        //cleanup() for this is a matter of performance specifics for the
        //implementor.
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        
        this.app = (SimpleApplication) app;
        world.addBody(createBody());
    }

    @Override
    protected void cleanup(Application app) {
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
    }

    //onEnable()/onDisable() can be used for managing things that should
    //only exist while the state is enabled. Prime examples would be scene
    //graph attachment or input listener attachment.
    @Override
    protected void onEnable() {
        //Called when the state is fully enabled, ie: is attached and
        //isEnabled() is true or when the setEnabled() status changes after the
        //state is attached.
    }

    @Override
    protected void onDisable() {
        //Called when the state was previously enabled but is now disabled
        //either because setEnabled(false) was called or the state is being
        //cleaned up.
    }

    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
        world.update(tpf);
    }
    
    private Body createBody(){
        Body body = new Body();
        body.addFixture(Geometry.createCircle(1.0));
        body.translate(1.0, 0.0);
        body.setMass(MassType.NORMAL);
        return body;
    }
    
    private Body createBody(Convex convex, double x, double y, MassType massType){
        Body body = new Body();
        body.addFixture(convex);
        body.translate(x, y);
        body.setMass(massType);
        return body;
    }
}
