package com.chrisali.openglworld.entities;

import org.lwjgl.util.vector.Vector3f;

import com.chrisali.openglworld.models.TexturedModel;

public class Ownship extends Entity {

	public Ownship(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void move(Vector3f position, float phi, float theta, float psi) {
		super.setPosition(position);
		
		super.setRotZ(phi);
		super.setRotX(theta);
		super.setRotY(psi);
	}
	
}
