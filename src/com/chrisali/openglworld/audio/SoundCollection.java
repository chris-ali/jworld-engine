package com.chrisali.openglworld.audio;

import java.util.EnumMap;
import java.util.Map;

import org.lwjgl.util.vector.Vector3f;

/**
 * Static class that contains a repository of sounds to be played by triggering certain events
 * 
 * @author Christopher Ali
 *
 */
public class SoundCollection {
	
	/**
	 * Inner enums used to identify {@link SoundSource} objects in the soundSources
	 * EnumMap 
	 * 
	 * @author Christopher Ali
	 *
	 */
	public static enum SoundEvent {
		BOUNCE,
		BOUNCE_LOOP,
		BOUNCE_HIGH;
	}
	
	private static Map<SoundEvent, SoundSource> soundSources = new EnumMap<>(SoundEvent.class);
	
	/**
	 *	Fills soundSources EnumMap with {@link SoundSource} objects, which are references to audio
	 *  files in .Resources/Audio, and sets their initial properties
	 */
	public static void initializeSounds() {
		soundSources.put(SoundEvent.BOUNCE_LOOP, new SoundSource("Audio", "bounce"));
		soundSources.get(SoundEvent.BOUNCE_LOOP).setLooping(true);
		
		soundSources.put(SoundEvent.BOUNCE, new SoundSource("Audio", "bounce"));
		
		soundSources.put(SoundEvent.BOUNCE_HIGH, new SoundSource("Audio", "bounce"));
		soundSources.get(SoundEvent.BOUNCE_HIGH).setPitch(2);
		
		// Add new sounds here; can set looping/position properties etc
	}

	public static void play(SoundEvent event) {
		soundSources.get(event).play();
	}
	
	public static void stop(SoundEvent event) {
		soundSources.get(event).stop();
	}
	
	public static void setPitch(SoundEvent event, float pitch) {
		soundSources.get(event).setPitch(pitch);
	}
	
	public static void setVolume(SoundEvent event, float volume) {
		soundSources.get(event).setVolume(volume);
	}
	
	public static void setPosition(SoundEvent event, Vector3f position) {
		soundSources.get(event).setPosition(position);
	}
	
	public static void setVelocity(SoundEvent event, Vector3f velocity) {
		soundSources.get(event).setVelocity(velocity);
	}
	
	public static void cleanUp() {
		for (Map.Entry<SoundEvent, SoundSource> entry : soundSources.entrySet())
			entry.getValue().delete();
	}
}
