package com.chrisali.openglworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.chrisali.openglworld.entities.Camera;
import com.chrisali.openglworld.entities.Entity;
import com.chrisali.openglworld.entities.Light;
import com.chrisali.openglworld.models.TexturedModel;
import com.chrisali.openglworld.renderengine.DisplayManager;
import com.chrisali.openglworld.renderengine.Loader;
import com.chrisali.openglworld.renderengine.MasterRenderer;
import com.chrisali.openglworld.renderengine.OBJLoader;
import com.chrisali.openglworld.terrain.Terrain;
import com.chrisali.openglworld.textures.ModelTexture;

public class RunWorld {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer();
		
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
		Camera camera = new Camera();
		
		TexturedModel tree =  new TexturedModel(OBJLoader.loadObjModel("tree", loader), 
											    new ModelTexture(loader.loadTexture("tree")));
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), 
		  										new ModelTexture(loader.loadTexture("grassTexture")));
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		
		TexturedModel fern =  new TexturedModel(OBJLoader.loadObjModel("fern", loader), 
				  								new ModelTexture(loader.loadTexture("fern")));
		fern.getTexture().setHasTransparency(true);
		
//		ModelTexture treeTexture = treeModel.getTexture();
//		treeTexture.setShineDamper(5);
//		treeTexture.setReflectivity(1);
		
		List<Entity> entities = new ArrayList<>();
		Random random = new Random();
		for (int i=0; i<500; i++) {
			entities.add(new Entity(tree, new Vector3f(((random.nextFloat() * 800) - 400), 
														0, random.nextFloat() * -600), 0, 0, 0, 3));
			entities.add(new Entity(grass, new Vector3f(((random.nextFloat() * 800) - 400), 
														0, random.nextFloat() * -600), 0, 0, 0, 1));
			entities.add(new Entity(fern, new Vector3f(((random.nextFloat() * 800) - 400), 
														0, random.nextFloat() * -600), 0, 0, 0, 0.6f));
		}
		
		
		List<Terrain> terrains = new ArrayList<>();
		terrains.add(new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass"))));
		terrains.add(new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass"))));
		terrains.add(new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass"))));
		terrains.add(new Terrain(-1, 0, loader, new ModelTexture(loader.loadTexture("grass"))));
		
		while (!Display.isCloseRequested()) {
			camera.move();
			
			for(Entity entity : entities)
				renderer.processEntity(entity);
			
			for(Terrain terrain : terrains)
				renderer.processTerrain(terrain);
			
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}

		renderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
