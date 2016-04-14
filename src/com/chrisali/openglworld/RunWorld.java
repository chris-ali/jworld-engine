package com.chrisali.openglworld;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.chrisali.openglworld.particles.Cloud;
import com.chrisali.openglworld.particles.ParticleMaster;
import com.chrisali.openglworld.particles.ParticleTexture;
import com.chrisali.openglworld.renderengine.DisplayManager;
import com.chrisali.openglworld.renderengine.InterfaceRenderer;
import com.chrisali.openglworld.renderengine.Loader;
import com.chrisali.openglworld.renderengine.MasterRenderer;
import com.chrisali.openglworld.renderengine.OBJLoader;
import com.chrisali.openglworld.terrain.TerrainCollection;
import com.chrisali.openglworld.textures.ModelTexture;

public class RunWorld {

	public static void main(String[] args) {new RunWorld();}
	
	public RunWorld() {
		//=================================== Set Up ==========================================================
		
		DisplayManager.createDisplay();
		DisplayManager.setHeight(900);
		DisplayManager.setWidth(1440);
		
		Loader loader = new Loader();
		MasterRenderer masterRenderer = new MasterRenderer();
		MasterRenderer.setSkyColor(new Vector3f(0.0f, 0.75f, 0.95f));
		MasterRenderer.setFogDensity(0.002f);
		MasterRenderer.setFogGradient(1.5f);
		
		ParticleMaster.init(loader, masterRenderer.getProjectionMatrix());
		InterfaceRenderer interfaceRenderer = new InterfaceRenderer(loader);
		TextMaster.init(loader);
		
		//==================================== Sun ============================================================
		
		List<Light> lights = new ArrayList<>();
		lights.add(new Light(new Vector3f(20000, 40000, 20000), new Vector3f(0.8f, 0.8f, 0.8f)));
		
		//================================= Terrain ==========================================================
		
		TerrainCollection terrainCollection = new TerrainCollection(4, loader);

		//=============================== Particles ==========================================================
		
//		ParticleTexture fire = new ParticleTexture(loader.loadTexture("fire", "particles"), 4, true);
//		
//		ParticleSystem fireSystem = new ParticleSystem(fire, 300, 8, 0.1f, 1, 8.6f);
//		fireSystem.setLifeError(0.1f);
//		fireSystem.setSpeedError(0.25f);
//		fireSystem.setScaleError(0.5f);
//		fireSystem.randomizeRotation();
		
		ParticleTexture clouds = new ParticleTexture(loader.loadTexture("clouds", "particles"), 4, true);
		
		Random random = new Random();
		for (int i = 0; i < 1000; i++)
			new Cloud(clouds, new Vector3f(random.nextInt(800*5), 200, i*5), new Vector3f(0, 0, 0), 0, 100);
		
		//================================= Entities ==========================================================
		
		EntityCollections entities = new EntityCollections(lights, terrainCollection.getTerrainArray(), loader);
		entities.createRandomStaticEntities();
		
		//============================= Lit Entities =========================================================
		
		//entities.createRandomLitEntities();
		
		//================================== Player ===========================================================
		
		TexturedModel bunny =  new TexturedModel(OBJLoader.loadObjModel("bunny", "entities", loader), 
			    								new ModelTexture(loader.loadTexture("bunny", "entities")));
		Player player = new Player(bunny, new Vector3f(800, 0, 800), 0, 0, 0, 0.5f);
		
		entities.addToStaticEntities(player);
		
		Camera camera = new Camera(player);
		
		//=============================== Interface ==========================================================
		
		List<InterfaceTexture> interfaceTextures = new ArrayList<>();
		interfaceTextures.add(new InterfaceTexture(loader.loadTexture("tree", "entities"), new Vector2f(0.25f, 0.25f), new Vector2f(0.0f, 0.0f)));
		
		FontType font = new FontType(loader.loadTexture("arial", "fonts"), new File("res\\fonts\\arial.fnt"));
		GUIText text = new GUIText("Test Text", 1, font, new Vector2f(0, 0), 1f, true);
		
		//=============================== Main Loop ==========================================================
		
		while (!Display.isCloseRequested()) {
			camera.move();
			player.move(terrainCollection.getTerrainArray());
			
//			fireSystem.generateParticles(new Vector3f(player.getPosition()));
			
			ParticleMaster.update(camera);
			
			text.setTextString(String.valueOf(player.getPosition().y));
			TextMaster.loadText(text);

			masterRenderer.renderWholeScene(entities, terrainCollection.getTerrainArray(), lights, camera);
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
