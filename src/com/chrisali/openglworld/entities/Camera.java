package com.chrisali.openglworld.entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private Vector3f position = new Vector3f(0,1,0);
	
	// Camera's own angles
	private float phi;
	private float theta = 20f;
	private float psi;
	
	private float cameraPanSpeed = 0.2f;
	private float mouseSensitivity = 0.2f;
	
	// Camera in relation to player/ownship
	private float cameraDistance = 25.0f;
	private float cameraPsi = 0.0f;
	
	private Player player;
	
	public Camera(Player player) {
		this.player = player;
	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		float camAngle = player.getRotY() + cameraPsi;
		float offsetX = horizontalDistance * (float) Math.sin(Math.toRadians(camAngle));
		float offsetZ = horizontalDistance * (float) Math.cos(Math.toRadians(camAngle));
		
		position.x = player.getPosition().x - offsetX;
		position.y = player.getPosition().y + verticalDistance;
		position.z = player.getPosition().z - offsetZ;
	}
	
	private float calculateHorizontalDistanceToPlayer() {
		return  (cameraDistance * (float) Math.cos(Math.toRadians(theta)));
	}
	
	private float calculateVerticalDistanceToPlayer() {
		return  (cameraDistance * (float) Math.sin(Math.toRadians(theta)));
	}
	
	private void calculateCameraZoomToPlayer() {
		float zoomLevel = Mouse.getDWheel() * 0.05f;
		cameraDistance -= zoomLevel;
	}
	
	private void calculateCameraPitchToPlayer() {
		if(Mouse.isButtonDown(1)) {
			float dTheta = Mouse.getDY() * mouseSensitivity;
			theta += dTheta;
		}
	}
	
	private void calculateCameraYawToPlayer() {
		if(Mouse.isButtonDown(1)) {
			float dPsi = Mouse.getDX() * mouseSensitivity;
			cameraPsi += dPsi;
		}
	}
	
	public void move() {
//		if(Keyboard.isKeyDown(Keyboard.KEY_W))
//			position.z -= cameraSpeed;
//		if(Keyboard.isKeyDown(Keyboard.KEY_S))
//			position.z += cameraSpeed;
//		if(Keyboard.isKeyDown(Keyboard.KEY_A))
//			position.x -= cameraSpeed;
//		if(Keyboard.isKeyDown(Keyboard.KEY_D))
//			position.x += cameraSpeed;
//		if(Keyboard.isKeyDown(Keyboard.KEY_R))
//			position.y += cameraPanSpeed;
//		if(Keyboard.isKeyDown(Keyboard.KEY_F))
//			position.y -= cameraPanSpeed;
		
		calculateCameraZoomToPlayer();
		calculateCameraPitchToPlayer();
		calculateCameraYawToPlayer();
				
		float horizontalDistance = calculateHorizontalDistanceToPlayer();
		float verticalDistance = calculateVerticalDistanceToPlayer();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		
		this.psi = 180 - (player.getRotY() + cameraPsi); 
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return theta;
	}
	
	public float getRoll() {
		return phi;
	}
	
	public float getYaw() {
		return psi;
	}
	
	public float getCameraSpeed() {
		return cameraPanSpeed;
	}

	public void setCameraSpeed(float cameraSpeed) {
		this.cameraPanSpeed = cameraSpeed;
	}
	
	public float getMouseSensitivity() {
		return mouseSensitivity;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setPitch(float pitch) {
		this.theta = pitch;
	}

	public void setRoll(float roll) {
		this.phi = roll;
	}

	public void setYaw(float yaw) {
		this.psi = yaw;
	}

	public void setMouseSensitivity(float mouseSensitivity) {
		this.mouseSensitivity = mouseSensitivity;
	}
}
