package com.chrisali.openglworld.terrain;

import com.chrisali.openglworld.renderengine.Loader;
import com.chrisali.openglworld.textures.TerrainTexture;
import com.chrisali.openglworld.textures.TerrainTexturePack;

public class TerrainCollection {
	
	private Terrain[][] terrainArray;
	
	public TerrainCollection(int numTerrains, Loader loader) {
		terrainArray = new Terrain[numTerrains/2][numTerrains/2];

		TerrainTexturePack texturePack = createTexturePack("grassy3", "dirt", "mud", "mossPath256", loader);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap", "terrain"));
	
		for (int i = 0; i < terrainArray.length; i++) {
			for (int j = 0; j < terrainArray.length; j++) {
				terrainArray[i][j] = new Terrain(i, j, "heightMap", "terrain", loader, texturePack, blendMap);
			}
		}
	}
	
	private TerrainTexturePack createTexturePack(String backgroundTextureName, 
							String rTextureName, String gTextureName, String bTextureName, Loader loader) {
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture(backgroundTextureName, "terrain"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture(rTextureName, "terrain"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture(gTextureName, "terrain"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture(bTextureName, "terrain"));
		
		return new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture); 
	}

	public Terrain[][] getTerrainArray() {
		return terrainArray;
	}
}
