package com.chrisali.openglworld;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.chrisali.openglworld.entities.Camera;
import com.chrisali.openglworld.entities.EntityCollections;
import com.chrisali.openglworld.entities.Light;
import com.chrisali.openglworld.entities.Player;
import com.chrisali.openglworld.interfaces.font.FontType;
import com.chrisali.openglworld.interfaces.font.GUIText;
import com.chrisali.openglworld.interfaces.font.TextMaster;
import com.chrisali.openglworld.models.TexturedModel;
import com.chrisali.openglworld.particles.Cloud;
import com.chrisali.openglworld.particles.ParticleMaster;
import com.chrisali.openglworld.particles.ParticleTexture;
import com.chrisali.openglworld.renderengine.DisplayManager;
import com.chrisali.openglworld.renderengine.Loader;
import com.chrisali.openglworld.renderengine.MasterRenderer;
import com.chrisali.openglworld.renderengine.OBJLoader;
import com.chrisali.openglworld.terrain.TerrainCollection;
import com.chrisali.openglworld.textures.ModelTexture;

public class RunWorld implements Runnable {
	
	private Loader loader;
	private MasterRenderer masterRenderer;
	private List<Light> lights;
	
	private TerrainCollection terrainCollection;
	private EntityCollections entities;
	
	private Camera camera;
	private Player player;
	
	private GUIText text;
	
	public static void main(String[] args) {new RunWorld().run();}

	public RunWorld() {
		//=================================== Set Up ==========================================================
		
		DisplayManager.createDisplay();
		DisplayManager.setHeight(900);
		DisplayManager.setWidth(1440);
		
		masterRenderer = new MasterRenderer();
		MasterRenderer.setSkyColor(new Vector3f(0.0f, 0.75f, 0.95f));
		MasterRenderer.setFogDensity(0.0015f);
		MasterRenderer.setFogGradient(1.5f);
		
		loader = new Loader();
		
		ParticleMaster.init(loader, masterRenderer.getProjectionMatrix());
		TextMaster.init(loader);
		
		//==================================== Sun ===========================================================
		
		lights = new ArrayList<>();
		lights.add(new Light(new Vector3f(20000, 40000, 20000), new Vector3f(0.8f, 0.8f, 0.8f)));
		
		//================================= Terrain ==========================================================
		
		terrainCollection = new TerrainCollection(4, loader);
		
		//================================= Entities ==========================================================
		
		entities = new EntityCollections(lights, terrainCollection.getTerrainArray(), loader);
		entities.createRandomStaticEntities();
		
		//================================== Player ===========================================================
		
		TexturedModel bunny =  new TexturedModel(OBJLoader.loadObjModel("bunny", "Entities", loader), 
			    								new ModelTexture(loader.loadTexture("bunny", "Entities")));
		player = new Player(bunny, new Vector3f(800, 0, 800), 0, 0, 0, 0.5f);
		
		entities.addToStaticEntities(player);
		
		camera = new Camera(player);
		camera.setChaseView(false);
		camera.setPilotPosition(new Vector3f(0, 0, 0));
		
		//=============================== Particles ==========================================================
		
		ParticleTexture clouds = new ParticleTexture(loader.loadTexture("clouds", "Particles"), 4, true);
		
		Random random = new Random();
		for (int i = 0; i < 1000; i++)
			new Cloud(clouds, new Vector3f(random.nextInt(800*5), 200, i*5), new Vector3f(0, 0, 0), 0, 200);
		
		//=============================== Interface ==========================================================
		
		FontType font = new FontType(loader.loadTexture("arial", "Fonts"), new File("Resources\\Fonts\\arial.fnt"));
		text = new GUIText("", 1, font, new Vector2f(0, 0), 1f, true);
	}	
	
	@Override
	public void run() {
		//=============================== Main Loop ==========================================================

		while (!Display.isCloseRequested()) {
			//--------- Movement ----------------
			camera.move();
			player.move(terrainCollection.getTerrainArray());
			
			//--------- Particles ---------------
			ParticleMaster.update(camera);
			
			//----------- UI --------------------
			text.setTextString(String.valueOf(player.getPosition().y));
			TextMaster.loadText(text);

			//------ Render Everything -----------
			masterRenderer.renderWholeScene(entities, terrainCollection.getTerrainArray(), 
											lights, camera, new Vector4f(0, 1, 0, 0));
			ParticleMaster.renderParticles(camera);
			TextMaster.render();
			
			DisplayManager.updateDisplay();
		}
		
		//================================ Clean Up ==========================================================
		
		ParticleMaster.cleanUp();
		TextMaster.cleanUp();
		masterRenderer.cleanUp();
		loader.cleanUp();
		
		DisplayManager.closeDisplay();
	}
}
