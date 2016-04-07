package com.chrisali.openglworld.renderengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import com.chrisali.openglworld.entities.Camera;
import com.chrisali.openglworld.entities.Entity;
import com.chrisali.openglworld.entities.Light;
import com.chrisali.openglworld.models.TexturedModel;
import com.chrisali.openglworld.shaders.StaticShader;
import com.chrisali.openglworld.shaders.TerrainShader;
import com.chrisali.openglworld.terrain.Terrain;

public class MasterRenderer {
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private static final float SKY_RED = 0.5f;
	private static final float SKY_GREEN = 0.5f;
	private static final float SKY_BLUE = 0.5f;
	
	private static final float FOG_DENSITY = 0.002f;
	private static final float FOG_GRADIENT = 1.5f;
	
	private StaticShader staticShader = new StaticShader();
	private TerrainShader terrainShader = new TerrainShader();
	
	private EntityRenderer entityRenderer;
	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
	
	private TerrainRenderer terrainRenderer;
	private List<Terrain> terrains = new ArrayList<>();
	
	private Matrix4f projectionMatrix;
	
	public MasterRenderer() {
		enableCulling();
		createProjectionMatrix();
		
		entityRenderer = new EntityRenderer(staticShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void render(List<Light> lights, Camera camera) {
		prepare();

		staticShader.start();
		staticShader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
		staticShader.loadFog(FOG_DENSITY, FOG_GRADIENT);
		staticShader.loadLights(lights);
		staticShader.loadViewMatrix(camera);
		entityRenderer.render(entities);
		staticShader.stop();
		
		terrainShader.start();
		terrainShader.loadSkyColor(SKY_RED, SKY_GREEN, SKY_BLUE);
		terrainShader.loadFog(FOG_DENSITY, FOG_GRADIENT);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		entities.clear();
		terrains.clear();
	}
	
	private void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(SKY_RED, SKY_GREEN, SKY_BLUE, 1);
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
        
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
	}

	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		
		if(batch!=null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void cleanUp() {
		staticShader.cleanUp();
		terrainShader.cleanUp();
	}
}
