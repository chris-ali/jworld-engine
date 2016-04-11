package com.chrisali.openglworld.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class ParticleShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = SHADER_ROOT_PATH + "shaders\\particleVertexShader.txt";
	private static final String FRAGMENT_FILE = SHADER_ROOT_PATH + "shaders\\particleFragmentShader.txt";

	private int location_numberOfAtlasRows;
	private int location_projectionMatrix;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_numberOfAtlasRows = super.getUniformLocation("numberOfAtlasRows");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "textureOffsets");
		super.bindAttribute(6, "blendFactor");
		
	}
	
	public void loadNumberOfAtlasRows(float numberRows) {
		super.loadFloat(location_numberOfAtlasRows, numberRows);
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}
