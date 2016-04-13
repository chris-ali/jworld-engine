package com.chrisali.openglworld.renderengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.chrisali.openglworld.entities.Camera;
import com.chrisali.openglworld.entities.Entity;
import com.chrisali.openglworld.entities.EntityCollections;
import com.chrisali.openglworld.entities.Light;
import com.chrisali.openglworld.models.TexturedModel;
import com.chrisali.openglworld.shaders.StaticShader;
import com.chrisali.openglworld.shaders.TerrainShader;
import com.chrisali.openglworld.terrain.Terrain;

public class MasterRenderer {
	private static float fov = 70;
	private static float nearPlane = 0.1f;
	private static float farPlane = 1000;
	
	private static float skyRed = 0.5f;
	private static float skyGreen = 0.5f;
	private static float skyBlue = 0.5f;
	
	private static float fogDensity = 0.002f;
	private static float fogGradient = 1.5f;
	
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
	
	public void renderWholeScene(EntityCollections entities, Terrain[][] terrainArray, List<Light> lights, Camera camera) {
		for(Entity entity : entities.getStaticEntities())
			processEntity(entity);
		
		for(Entity entity : entities.getLitEntities())
			processEntity(entity);
		
		for (int i = 0; i < terrainArray.length; i++) {
			for (int j = 0; j < terrainArray.length; j++) {
				processTerrain(terrainArray[i][j]);
			}
		}
		
		render(lights, camera);
	}

	private void render(List<Light> lights, Camera camera) {
		prepare();

		staticShader.start();
		staticShader.loadSkyColor(skyRed, skyGreen, skyBlue);
		staticShader.loadFog(fogDensity, fogGradient);
		staticShader.loadLights(lights);
		staticShader.loadViewMatrix(camera);
		entityRenderer.render(entities);
		staticShader.stop();
		
		terrainShader.start();
		terrainShader.loadSkyColor(skyRed, skyGreen, skyBlue);
		terrainShader.loadFog(fogDensity, fogGradient);
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
		GL11.glClearColor(skyRed, skyGreen, skyBlue, 1);
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = farPlane - nearPlane;
        
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((farPlane + nearPlane) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * nearPlane * farPlane) / frustum_length);
        projectionMatrix.m33 = 0;
	}

	private void processEntity(Entity entity) {
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
	
	private void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public void cleanUp() {
		staticShader.cleanUp();
		terrainShader.cleanUp();
	}

	public static Vector3f getSkyColor() {
		return new Vector3f(skyRed, skyGreen, skyBlue);
	}
	
	public static void setSkyColor(Vector3f skyColor) {
		skyRed = skyColor.x; 
		skyGreen = skyColor.y;
		skyBlue = skyColor.z;
	}

	public static float getFogDensity() {
		return fogDensity;
	}
	
	public static void setFogDensity(float fogDens) {
		fogDensity = fogDens;
	}

	public static float getFogGradient() {
		return fogGradient;
	}
	
	public static void setFogGradient(float fogGrad) {
		fogGradient = fogGrad;
	}

	public static float getFov() {
		return fov;
	}
}
