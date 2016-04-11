package com.chrisali.openglworld.particles;

import org.lwjgl.util.vector.Vector3f;

import com.chrisali.openglworld.renderengine.DisplayManager;

public class Particle {
	
	private static final float GRAVITY = -50;
	
	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float rotation;
	private float scale;

	private float elapsedTime = 0;

	public Particle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
		this.position = position;
		this.velocity = velocity;
		this.lifeLength = lifeLength;
		this.gravityEffect = gravityEffect;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.addParticle(this);
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public float getLifeLength() {
		return lifeLength;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public float getGravityEffect() {
		return gravityEffect;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}
	
	protected boolean update() {
		velocity.y += GRAVITY * gravityEffect * DisplayManager.getFrameTimeSeconds();
		Vector3f change = new Vector3f(velocity);
		change.scale(DisplayManager.getFrameTimeSeconds());
		Vector3f.add(change, position, position);
		elapsedTime += DisplayManager.getFrameTimeSeconds();
		
		return elapsedTime < lifeLength;
	}
}
