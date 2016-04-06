package com.chrisali.openglworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.chrisali.openglworld.entities.Camera;
import com.chrisali.openglworld.entities.Entity;
import com.chrisali.openglworld.entities.Light;
import com.chrisali.openglworld.entities.Player;
import com.chrisali.openglworld.models.TexturedModel;
import com.chrisali.openglworld.renderengine.DisplayManager;
import com.chrisali.openglworld.renderengine.Loader;
import com.chrisali.openglworld.renderengine.MasterRenderer;
import com.chrisali.openglworld.renderengine.OBJLoader;
import com.chrisali.openglworld.terrain.Terrain;
import com.chrisali.openglworld.textures.ModelTexture;
import com.chrisali.openglworld.textures.TerrainTexture;
import com.chrisali.openglworld.textures.TerrainTexturePack;

public class RunWorld {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		MasterRenderer renderer = new MasterRenderer();
		Light light = new Light(new Vector3f(20000, 40000, 20000), new Vector3f(1, 1, 1));
		
		//================================== Player ===========================================================
		
		TexturedModel bunny =  new TexturedModel(OBJLoader.loadObjModel("bunny", "entities", loader), 
			    								new ModelTexture(loader.loadTexture("bunny", "entities")));
		Player player = new Player(bunny, new Vector3f(100, 0, -50), 0, 0, 0, 0.5f);
		
		Camera camera = new Camera(player);
		camera.setMouseSensitivity(0.2f);
		
		//================================= Entities ==========================================================
		
		TexturedModel tree1 =  new TexturedModel(OBJLoader.loadObjModel("tree", "entities", loader), 
											    new ModelTexture(loader.loadTexture("tree", "entities")));
		TexturedModel tree2 =  new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", "entities", loader), 
			    								new ModelTexture(loader.loadTexture("lowPolyTree", "entities")));
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", "entities", loader), 
		  										new ModelTexture(loader.loadTexture("grassTexture", "entities")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", "entities", loader), 
												new ModelTexture(loader.loadTexture("flower", "entities")));
		TexturedModel fern =  new TexturedModel(OBJLoader.loadObjModel("fern", "entities", loader), 
				  								new ModelTexture(loader.loadTexture("fern", "entities")));
		
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);
		
		List<Entity> entities = new ArrayList<>();
		Random random = new Random();
		for (int i=0; i<400; i++) {
			if (i % 7 == 0) {
				entities.add(new Entity(flower, new Vector3f(((random.nextFloat() * 400) - 200), 0, random.nextFloat() * -400), 
						0, random.nextFloat()*360, 0, random.nextFloat()*1 + 2));
				entities.add(new Entity(fern, new Vector3f(((random.nextFloat() * 800) - 400),	0, random.nextFloat() * -600), 
						0, random.nextFloat()*360, 0, random.nextFloat()*1 + 0.5f));
			}
			if (i % 3 == 0) {
				entities.add(new Entity(tree1, new Vector3f(((random.nextFloat() * 800) - 400),	0, random.nextFloat() * -600), 
						0, random.nextFloat()*360, 0, random.nextFloat()* 1 + 5));
				entities.add(new Entity(tree2, new Vector3f(((random.nextFloat() * 800) - 400),	0, random.nextFloat() * -600), 
						0, random.nextFloat()*360, 0, random.nextFloat()* 0.1f + 0.6f));
				entities.add(new Entity(grass, new Vector3f(((random.nextFloat() * 800) - 400), 0, random.nextFloat() * -600), 
						0, random.nextFloat()*360, 0, random.nextFloat()* 1 + 1));
			}
		}
		
		//================================= Terrain ==========================================================
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy3", "terrain"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt", "terrain"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("mud", "terrain"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("mossPath256", "terrain"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap", "terrain"));
		
		List<Terrain> terrains = new ArrayList<>();
		terrains.add(new Terrain( 0,  0, "heightMap", "terrain", loader, texturePack, blendMap));
		terrains.add(new Terrain( 0, -1, "heightMap", "terrain", loader, texturePack, blendMap));
		terrains.add(new Terrain(-1, -1, "heightMap", "terrain", loader, texturePack, blendMap));
		terrains.add(new Terrain(-1,  0, "heightMap", "terrain", loader, texturePack, blendMap));
		
		//=============================== Main Loop ==========================================================
		
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move();
			
			renderer.processEntity(player);
			
			for(Entity entity : entities)
				renderer.processEntity(entity);
			
			for(Terrain terrain : terrains)
				renderer.processTerrain(terrain);
			
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		//================================ Clean Up ==========================================================

		renderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
