/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.parkcar.states;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import io.github.jmecn.tiled.core.TiledMap;
import io.github.jmecn.tiled.renderer.MapRenderer;
import io.github.jmecn.tiled.renderer.factory.DefaultMaterialFactory;
import io.github.jmecn.tiled.renderer.factory.DefaultMeshFactory;
import io.github.jmecn.tiled.renderer.factory.DefaultSpriteFactory;
import io.github.jmecn.tiled.renderer.factory.MaterialFactory;
import io.github.jmecn.tiled.renderer.queue.YAxisComparator;

/**
 *
 * @author arpyg
 */
public class ViewState extends BaseAppState {
    public final int TILE_SIZE = 32; // px
    public final int TILE_COUNT_HEIGHT = 20;
    
    // Tiled Map
    private TiledMap map;
    private MapRenderer mapRenderer;

    // The rootNode
    private final Node rootNode;

    private ViewPort viewPort;
    private MaterialFactory materialFactory;

    public ViewState() {
        rootNode = new Node("Tiled Map Root");
    }

    @Override
    protected void initialize(Application app) {
        AssetManager assetManager = app.getAssetManager();
        materialFactory = new DefaultMaterialFactory(assetManager);
        viewPort = app.getViewPort();
        // The camera
        Camera cam = app.getCamera();

        // sort by y-axis
        viewPort.getQueue().setGeometryComparator(RenderQueue.Bucket.Opaque, new YAxisComparator());

        float near = -1000f;
        float far = 10f;

        float ratio = (float) cam.getWidth() / cam.getHeight();
        float viewHeight = TILE_SIZE * TILE_COUNT_HEIGHT;
        float viewWidth = ratio * viewHeight;
        float halfWidth = viewWidth * 0.5f;
        float halfHeight = viewHeight * 0.5f;
        cam.setParallelProjection(true);
        cam.setFrustum(near, far, -halfWidth, halfWidth, halfHeight, -halfHeight);
        cam.setLocation(new Vector3f(halfWidth, 0, halfHeight));
        cam.lookAtDirection(new Vector3f(0f, -1f, 0f), new Vector3f(0f, 0f, -1f));

        if (this.map != null) {
            viewPort.setBackgroundColor(map.getBackgroundColor());
            mapRenderer.getSpriteFactory().setMaterialFactory(materialFactory);
        }
    }

    @Override
    protected void cleanup(Application app) {
        rootNode.detachAllChildren();
    }

    @Override
    protected void onEnable() {
        ((SimpleApplication) getApplication()).getRootNode().attachChild(rootNode);
    }

    @Override
    protected void onDisable() {
        rootNode.removeFromParent();
    }

    @Override
    public void update(float tpf) {
        if (mapRenderer != null) {
            mapRenderer.render();
        }
    }
    /**
     * Set map. It will instance a new MapRenderer and create visual parts for this map.
     * 
     * @param map the tiled map
     */
    public void setMap(TiledMap map) {
        if (!isInitialized()) {
            throw new IllegalStateException("ViewState is not initialized.");
        }
        rootNode.detachAllChildren();

        if (map == null) {
            return;
        }
        
        viewPort.setBackgroundColor(map.getBackgroundColor());

        if (this.map != map) {
            this.map = map;
        }

        mapRenderer = MapRenderer.create(map);

        // new sprite factory. the materialFactory is set in initialize()
        DefaultSpriteFactory spriteFactory = new DefaultSpriteFactory();
        spriteFactory.setMeshFactory(new DefaultMeshFactory(map));
        spriteFactory.setMaterialFactory(materialFactory);

        mapRenderer.setSpriteFactory(spriteFactory);

        // create the visual part for the map
        rootNode.attachChild(mapRenderer.getRootNode());
    }

    public MapRenderer getMapRenderer() {
        return mapRenderer;
    }

}