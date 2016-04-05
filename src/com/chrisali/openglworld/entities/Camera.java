package com.chrisali.openglworld.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private Vector3f position = new Vector3f(0,1,0);
	private float pitch;
	private float roll;
	private float yaw;
	
	private float cameraSpeed = 0.2f;
	
	public Camera() {}
	
	public void move() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
			position.z -= cameraSpeed;
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
			position.z += cameraSpeed;
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
			position.x -= cameraSpeed;
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
			position.x += cameraSpeed;
		if(Keyboard.isKeyDown(Keyboard.KEY_R))
			position.y += cameraSpeed;
		if(Keyboard.isKeyDown(Keyboard.KEY_F))
			position.y -= cameraSpeed;
		if(Keyboard.isKeyDown(Keyboard.KEY_Q))
			yaw -= cameraSpeed*10;
		if(Keyboard.isKeyDown(Keyboard.KEY_E))
			yaw += cameraSpeed*10;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getRoll() {
		return roll;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getCameraSpeed() {
		return cameraSpeed;
	}

	public void setCameraSpeed(float cameraSpeed) {
		this.cameraSpeed = cameraSpeed;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
}
