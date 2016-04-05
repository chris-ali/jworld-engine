package com.chrisali.openglworld.renderengine;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.chrisali.openglworld.models.RawModel;

public class Loader {
	
	private List<Integer> vaoList = new ArrayList<>();
	private List<Integer> vboList = new ArrayList<>();
	private List<Integer> textureList = new ArrayList<>();
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();

		return new RawModel(vaoID, indices.length);
	}
	
	public int loadTexture(String fileName) {
		Texture texture = null;
		
		try { texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));} 
		catch (IOException e) {e.printStackTrace();}
		
		int textureID = texture.getTextureID();
		textureList.add(textureID);
		
		return textureID;
	}
	
	public void cleanUp() {
		for (int vao : vaoList)
			GL30.glDeleteVertexArrays(vao);
		for (int vbo : vboList)
			GL15.glDeleteBuffers(vbo);
		for (int texture : textureList)
			GL11.glDeleteTextures(texture);
	}

	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaoList.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vboList.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	private void bindIndicesBuffer(int[] indices) {
		int vboId = GL15.glGenBuffers();
		vboList.add(vboId);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}