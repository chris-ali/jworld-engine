package com.chrisali.openglworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.chrisali.openglworld.entities.Camera;
import com.chrisali.openglworld.entities.Entity;
import com.chrisali.openglworld.entities.Light;
import com.chrisali.openglworld.entities.Player;
import com.chrisali.openglworld.interfaces.InterfaceRenderer;
import com.chrisali.openglworld.interfaces.InterfaceTexture;
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
		MasterRenderer masterRenderer = new MasterRenderer();
		InterfaceRenderer interfaceRenderer = new InterfaceRenderer(loader);
		
		//==================================== Sun ============================================================
		
		List<Light> lights = new ArrayList<>();
		lights.add(new Light(new Vector3f(20000, 40000, 20000), new Vector3f(0.002f, 0.002f, 0.002f)));

		
		//================================== Player ===========================================================
		
		TexturedModel bunny =  new TexturedModel(OBJLoader.loadObjModel("bunny", "entities", loader), 
			    								new ModelTexture(loader.loadTexture("bunny", "entities")));
		Player player = new Player(bunny, new Vector3f(200, 0, 100), 0, 0, 0, 0.5f);
		
		Camera camera = new Camera(player);
		camera.setMouseSensitivity(0.2f);
		
		//================================= Terrain ==========================================================
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy3", "terrain"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt", "terrain"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("mud", "terrain"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("mossPath256", "terrain"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap", "terrain"));
		
		int numTerrains = 2;
		Terrain[][] terrainArray = new Terrain[2][2];
		
		for (int i = 0; i < numTerrains; i++) {
			for (int j = 0; j < numTerrains; j++) {
				terrainArray[i][j] = new Terrain(i, j, "heightMap", "terrain", loader, texturePack, blendMap);
			}
		}
		
		//================================= Entities ==========================================================
		
		TexturedModel tree1 =  new TexturedModel(OBJLoader.loadObjModel("pine", "entities", loader), 
											    new ModelTexture(loader.loadTexture("pine", "entities")));
		TexturedModel tree2 =  new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", "entities", loader), 
			    								new ModelTexture(loader.loadTexture("lowPolyTree", "entities")));
//		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", "entities", loader), 
//		  										new ModelTexture(loader.loadTexture("grassTexture", "entities")));
//		TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("grassModel", "entities", loader), 
//												new ModelTexture(loader.loadTexture("flower", "entities")));
		TexturedModel fern =  new TexturedModel(OBJLoader.loadObjModel("fern", "entities", loader), 
				  								new ModelTexture(loader.loadTexture("fern", "entities")));
		TexturedModel lamp =  new TexturedModel(OBJLoader.loadObjModel("lamp", "entities", loader), 
												new ModelTexture(loader.loadTexture("lamp", "entities")));
		
//		grass.getTexture().setHasTransparency(true);
//		grass.getTexture().setUseFakeLighting(true);
//		flower.getTexture().setHasTransparency(true);
//		flower.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);
		fern.getTexture().setNumberOfAtlasRows(2);
		
		List<Entity> entities = new ArrayList<>();
		Random random = new Random();
		for (int i=0; i<400; i++) {
			float x, y, z;
			
//			if (i % 7 == 0) {
//				x = (random.nextFloat() * 800) - 400;
//				z = random.nextFloat() * -600;
//				y = Terrain.getCurrentTerrain(terrainArray, x, z).getTerrainHeight(x, z);
//				
//				entities.add(new Entity(flower, new Vector3f(x, y, z), 0, random.nextFloat()*360, 0, random.nextFloat()*1 + 2));
//				
//				x = (random.nextFloat() * 800) - 400;
//				z = random.nextFloat() * -600;
//				y = Terrain.getCurrentTerrain(terrainArray, x, z).getTerrainHeight(x, z);
//				
//				entities.add(new Entity(grass, new Vector3f(x, y, z), 0, random.nextFloat()*360, 0, random.nextFloat()* 1 + 1));
//			}
			
			if (i % 3 == 0) {
				x = (random.nextFloat() * 800) - 400;
				z = random.nextFloat() *  600;
				y = Terrain.getCurrentTerrain(terrainArray, x, z).getTerrainHeight(x, z);
				
				entities.add(new Entity(tree1, new Vector3f(x, y, z), 0, random.nextFloat()*360, 0, random.nextFloat()* 1 + 1));
				
				x = (random.nextFloat() * 800) - 400;
				z = random.nextFloat() *  600;
				y = Terrain.getCurrentTerrain(terrainArray, x, z).getTerrainHeight(x, z);
				
				entities.add(new Entity(tree2, new Vector3f(x, y, z), 0, random.nextFloat()*360, 0, random.nextFloat()* 0.1f + 0.6f));
				
				x = (random.nextFloat() * 800) - 400;
				z = random.nextFloat() *  600;
				y = Terrain.getCurrentTerrain(terrainArray, x, z).getTerrainHeight(x, z);
				
				entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat()*360, 0, random.nextFloat()*1 + 0.5f));
			}
		}
		
		//============================= Lit Entities =========================================================
		
		entities.add(new Entity(lamp, new Vector3f(185, -4.7f, 293), 0, 0, 0, 1));
		lights.add(new Light(new Vector3f(185, 10, 293), new Vector3f(2, 0, 2), new Vector3f(1, 0.01f, 0.002f)));
		
		entities.add(new Entity(lamp, new Vector3f(370, -7.0f, 300), 0, 0, 0, 1));
		lights.add(new Light(new Vector3f(370, 0, 300), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
		
		entities.add(new Entity(lamp, new Vector3f(293, 6.8f, 305), 0, 0, 0, 1));
		lights.add(new Light(new Vector3f(293, 10, 305), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f)));
		
		lights.add(new Light(new Vector3f(player.getPosition().x, player.getPosition().y+10, player.getPosition().z), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f)));
		
		//=============================== Interface ==========================================================
		
		List<InterfaceTexture> interfaceTextures = new ArrayList<>();
		interfaceTextures.add(new InterfaceTexture(loader.loadTexture("tree", "entities"), new Vector2f(0.25f, 0.25f), new Vector2f(0.025f, 0.025f)));
		
		//=============================== Main Loop ==========================================================
		
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move(terrainArray);
			
//			System.out.printf("%.1f, %.1f, %.1f, %.1f, %.1f\n", player.getPosition().x,
//															    player.getPosition().y,
//															    player.getPosition().z,
//															    Terrain.getCurrentTerrain(terrainArray, player.getPosition().x, player.getPosition().z).getX() - player.getPosition().x, 
//															    Terrain.getCurrentTerrain(terrainArray, player.getPosition().x, player.getPosition().z).getZ() - player.getPosition().z);
			
			masterRenderer.processEntity(player);
			
			for(Entity entity : entities)
				masterRenderer.processEntity(entity);
			
			for(Entity entity : entities)
				masterRenderer.processEntity(entity);
			
			for (int i = 0; i < numTerrains; i++) {
				for (int j = 0; j < numTerrains; j++) {
					masterRenderer.processTerrain(terrainArray[i][j]);
				}
			}
			
			masterRenderer.render(lights, camera);
			interfaceRenderer.render(interfaceTextures);
			DisplayManager.updateDisplay();
		}
		
		//================================ Clean Up ==========================================================

		masterRenderer.cleanUp();
		interfaceRenderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
