package com.chrisali.openglworld.audio;

import java.util.EnumMap;
import java.util.Map;

import org.lwjgl.util.vector.Vector3f;

public class SoundCollection {
	public static enum SoundEvent {
		BOUNCE,
		BOUNCE_LOOP,
		BOUNCE_HIGH,
		ENGINE_1_LOW,
		ENGINE_1_MED,
		ENGINE_1_HIGH,
		ENGINE_1_MAX;
	}
	
	private static Map<SoundEvent, SoundSource> soundSources = new EnumMap<>(SoundEvent.class);
	
	public static void initializeSounds() {
		soundSources.put(SoundEvent.BOUNCE_LOOP, new SoundSource("Audio", "bounce"));
		soundSources.get(SoundEvent.BOUNCE_LOOP).setLooping(true);
		
		soundSources.put(SoundEvent.BOUNCE, new SoundSource("Audio", "bounce"));
		
		soundSources.put(SoundEvent.BOUNCE_HIGH, new SoundSource("Audio", "bounce"));
		soundSources.get(SoundEvent.BOUNCE_HIGH).setPitch(2);
		
		soundSources.put(SoundEvent.ENGINE_1_LOW, new SoundSource("Audio", "engineLow"));
		soundSources.get(SoundEvent.ENGINE_1_LOW).setVolume(0);
		soundSources.get(SoundEvent.ENGINE_1_LOW).setLooping(true);
		
		soundSources.put(SoundEvent.ENGINE_1_MED, new SoundSource("Audio", "engineMed"));
		soundSources.get(SoundEvent.ENGINE_1_MED).setVolume(0);
		soundSources.get(SoundEvent.ENGINE_1_MED).setLooping(true);
		
		soundSources.put(SoundEvent.ENGINE_1_HIGH, new SoundSource("Audio", "engineHigh"));
		soundSources.get(SoundEvent.ENGINE_1_HIGH).setVolume(0);
		soundSources.get(SoundEvent.ENGINE_1_HIGH).setLooping(true);
		
		soundSources.put(SoundEvent.ENGINE_1_MAX, new SoundSource("Audio", "engineMax"));
		soundSources.get(SoundEvent.ENGINE_1_MAX).setVolume(0);
		soundSources.get(SoundEvent.ENGINE_1_MAX).setLooping(true);
		
		// Add new sounds here; can set looping/position properties etc
	}
	
	public static void play(SoundEvent event) {
		soundSources.get(event).play();
	}
	
	public static void stop(SoundEvent event) {
		soundSources.get(event).stop();
	}
	
	public static void setRPM(float RPM) {
		float gainLow  = (float) ((RPM >  300 && RPM < 1800) ? Math.cos((RPM-600)/500) : 0);
		float pitchLow = (float) ((RPM >  300 && RPM < 1800) ? ((1.5-0.75)*(RPM-300))/(1800-300) + 0.75 : 0);
		soundSources.get(SoundEvent.ENGINE_1_LOW).setVolume(gainLow);
		soundSources.get(SoundEvent.ENGINE_1_LOW).setPitch(pitchLow);
		
		float gainMed 	= (float) ((RPM > 600 && RPM < 2000) ? Math.cos((RPM-1500)/400) : 0);
		float pitchMed  = (float) ((RPM > 600 && RPM < 2000) ? ((1.5-0.75)*(RPM-600))/(2000-600) + 0.75 : 0);
		soundSources.get(SoundEvent.ENGINE_1_MED).setVolume(gainMed);
		soundSources.get(SoundEvent.ENGINE_1_MED).setPitch(pitchMed);
		
		float gainHi   = (float) ((RPM > 1500 && RPM < 2500) ? Math.cos((RPM-2000)/300) : 0);
		float pitchHi  = (float) ((RPM > 1500 && RPM < 2500) ? ((1.5-0.75)*(RPM-1500))/(2500-1500) + 0.75 : 0);
		soundSources.get(SoundEvent.ENGINE_1_HIGH).setVolume(gainHi);		
		soundSources.get(SoundEvent.ENGINE_1_HIGH).setPitch(pitchHi);
		
		float gainMax  = (float) ((RPM > 1900 && RPM < 3000) ? Math.cos((RPM-2600)/400)*2 : 0);
		float pitchMax = (float) ((RPM > 1900 && RPM < 3000) ? ((1.25-0.95)*(RPM-1900))/(3000-1900) + 0.95 : 0);
		soundSources.get(SoundEvent.ENGINE_1_MAX).setVolume(gainMax);
		soundSources.get(SoundEvent.ENGINE_1_MAX).setPitch(pitchMax);
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
