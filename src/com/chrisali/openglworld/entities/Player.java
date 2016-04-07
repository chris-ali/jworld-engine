package com.chrisali.openglworld.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import com.chrisali.openglworld.models.TexturedModel;
import com.chrisali.openglworld.renderengine.DisplayManager;
import com.chrisali.openglworld.terrain.Terrain;

public class Player extends Entity {
	
	private static final float RUN_SPEED = 20;
	private static final float TURN_SPEED = 160;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	
	private float currentSpeed = 0;
	private float currentVerticalSpeed = 0;
	private float currentTurnSpeed = 0;
	
	private boolean isAirborne = false;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void move(Terrain[][] terrainArray) {
		checkInputs();
		
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = distance * (float)Math.sin(Math.toRadians(super.getRotY()));
		float dz = distance * (float)Math.cos(Math.toRadians(super.getRotY()));
		
		super.increasePosition(dx, 0, dz);
		currentVerticalSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, currentVerticalSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		
		Terrain terrain = Terrain.getCurrentTerrain(terrainArray, super.getPosition().x, super.getPosition().z);
		float terrainHeight = terrain.getTerrainHeight(super.getPosition().x, super.getPosition().z);
		
		if (super.getPosition().y < terrainHeight) {
			currentVerticalSpeed = 0;
			isAirborne = false;
			super.getPosition().y = terrainHeight;
		}
		
	}
	
	private void jump() {
		if(!isAirborne) {
			this.currentVerticalSpeed = JUMP_POWER;
			isAirborne = true;
		}
	}
	
	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed = RUN_SPEED;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
		} else {
			this.currentSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed = TURN_SPEED;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
			jump();
	}
	
}
