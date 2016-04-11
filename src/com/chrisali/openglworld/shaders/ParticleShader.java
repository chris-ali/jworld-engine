package com.chrisali.openglworld.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class ParticleShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = SHADER_ROOT_PATH + "shaders\\particleVertexShader.txt";
	private static final String FRAGMENT_FILE = SHADER_ROOT_PATH + "shaders\\particleFragmentShader.txt";

	private int location_modelViewMatrix;
	private int location_projectionMatrix;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadModelViewMatrix(Matrix4f modelView) {
		super.loadMatrix(location_modelViewMatrix, modelView);
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}
