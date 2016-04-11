package com.chrisali.openglworld;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.chrisali.openglworld.entities.Camera;
import com.chrisali.openglworld.entities.EntityCollections;
import com.chrisali.openglworld.entities.Light;
import com.chrisali.openglworld.entities.Player;
import com.chrisali.openglworld.interfaces.font.FontType;
import com.chrisali.openglworld.interfaces.font.GUIText;
import com.chrisali.openglworld.interfaces.font.TextMaster;
import com.chrisali.openglworld.interfaces.ui.InterfaceTexture;
import com.chrisali.openglworld.models.TexturedModel;
import com.chrisali.openglworld.particles.ParticleMaster;
import com.chrisali.openglworld.particles.ParticleSystem;
import com.chrisali.openglworld.particles.ParticleTexture;
import com.chrisali.openglworld.renderengine.DisplayManager;
import com.chrisali.openglworld.renderengine.InterfaceRenderer;
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
		ParticleMaster.init(loader, masterRenderer.getProjectionMatrix());
		InterfaceRenderer interfaceRenderer = new InterfaceRenderer(loader);
		
		TextMaster.init(loader);
		FontType font = new FontType(loader.loadTexture("arial", "fonts"), new File("res\\fonts\\arial.fnt"));
		new GUIText("Test Text", 1, font, new Vector2f(0, 0), 1f, true);
		
		//==================================== Sun ============================================================
		
		List<Light> lights = new ArrayList<>();
		lights.add(new Light(new Vector3f(20000, 40000, 20000), new Vector3f(0.8f, 0.8f, 0.8f)));
		
		//================================= Terrain ==========================================================
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy3", "terrain"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt", "terrain"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("mud", "terrain"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("mossPath256", "terrain"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap", "terrain"));
		
		int numTerrains = 4;
		Terrain[][] terrainArray = new Terrain[numTerrains/2][numTerrains/2];
		
		for (int i = 0; i < terrainArray.length; i++) {
			for (int j = 0; j < terrainArray.length; j++) {
				terrainArray[i][j] = new Terrain(i, j, "heightMap", "terrain", loader, texturePack, blendMap);
			}
		}

		//=============================== Particles ==========================================================
		
		ParticleTexture fire = new ParticleTexture(loader.loadTexture("fire", "particles"), 4, true);
		
		ParticleSystem fireSystem = new ParticleSystem(fire, 300, 8, 0.1f, 1, 8.6f);
		fireSystem.setLifeError(0.1f);
		fireSystem.setSpeedError(0.25f);
		fireSystem.setScaleError(0.5f);
		fireSystem.randomizeRotation();
		
		//================================= Entities ==========================================================
		
		EntityCollections entities = new EntityCollections(lights, terrainArray, loader);
		entities.createRandomStaticEntities();
		
		//============================= Lit Entities =========================================================
		
		//entities.createRandomLitEntities();
		
		//================================== Player ===========================================================
		
		TexturedModel bunny =  new TexturedModel(OBJLoader.loadObjModel("bunny", "entities", loader), 
			    								new ModelTexture(loader.loadTexture("bunny", "entities")));
		Player player = new Player(bunny, new Vector3f(200, 0, 100), 0, 0, 0, 0.5f);
		
		entities.addToStaticEntities(player);
		
		Camera camera = new Camera(player);
		camera.setMouseSensitivity(0.2f);
		
		//=============================== Interface ==========================================================
		
		List<InterfaceTexture> interfaceTextures = new ArrayList<>();
		interfaceTextures.add(new InterfaceTexture(loader.loadTexture("tree", "entities"), new Vector2f(0.25f, 0.25f), new Vector2f(0.0f, 0.0f)));
		
		//=============================== Main Loop ==========================================================
		
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move(terrainArray);
			
			fireSystem.generateParticles(new Vector3f(player.getPosition()));
			
			ParticleMaster.update(camera);
			
//			System.out.printf("%.1f, %.1f, %.1f, %.1f, %.1f\n", player.getPosition().x,
//															    player.getPosition().y,
//															    player.getPosition().z,
//															    Terrain.getCurrentTerrain(terrainArray, player.getPosition().x, player.getPosition().z).getX() - player.getPosition().x, 
//															    Terrain.getCurrentTerrain(terrainArray, player.getPosition().x, player.getPosition().z).getZ() - player.getPosition().z);
			
			masterRenderer.renderWholeScene(entities, terrainArray, lights, camera);
			
			ParticleMaster.renderParticles(camera);
			interfaceRenderer.render(interfaceTextures);
			TextMaster.render();
			DisplayManager.updateDisplay();
		}
		
		//================================ Clean Up ==========================================================
		ParticleMaster.cleanUp();
		TextMaster.cleanUp();
		masterRenderer.cleanUp();
		interfaceRenderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}

}
