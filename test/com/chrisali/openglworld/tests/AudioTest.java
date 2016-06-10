package com.chrisali.openglworld.tests;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Vector3f;

import com.chrisali.openglworld.audio.AudioMaster;
import com.chrisali.openglworld.audio.SoundCollection;
import com.chrisali.openglworld.audio.SoundSource;
import com.chrisali.openglworld.audio.SoundCollection.SoundEvent;

public class AudioTest {

	public static void main(String[] args) {
		AudioTest test = new AudioTest();
		
		//test.AudioTestLoop();
		//test.AudioTestMovement();
		//test.AudioTestPitch();
		//test.AudioTestSoundCollection();
		//test.AudioTestRPM();
		//test.AudioTestFlaps();
		//test.AudioTestGear();
		//test.AudioTestStall();
		test.AudioTestWind();
		
		test.finish();
	}
	
	private Vector3f playerPostion = new Vector3f(0, 0, 0);
	private Vector3f playerVelocity = new Vector3f(0, 0, 0);
	
	private Vector3f sourcePosition = new Vector3f(8, 0, 2);
	private Vector3f sourceVelocity = new Vector3f(-0.02f, 0, 0);
	
	Map<String, SoundSource> soundSources = new HashMap<>();
	
	private AudioTest() {
		AudioMaster.init();
		AudioMaster.setListenerData(playerPostion, playerVelocity);
		
		soundSources.put("bounce", new SoundSource("Audio", "bounce"));
		soundSources.get("bounce").setLooping(true);
		
		soundSources.put("bounceHigh", new SoundSource("Audio", "bounce"));
		soundSources.get("bounceHigh").setPitch(2);
	}
	
	private void AudioTestLoop() {
		System.out.println("Type \'p\' to pause/resume loop. \'o\' plays a higher pitch sound and \'q\' quits looping test");
		
		soundSources.get("bounce").play();
		
		char in = ' ';
		
		while (in != 'q') {
			try {in = (char) System.in.read();} 
			catch (IOException e) {System.err.println("Invalid command!");}
			
			switch (in){
			case 'p':
				if (soundSources.get("bounce").isPlaying())
					soundSources.get("bounce").pause();
				else
					soundSources.get("bounce").resume();
				break;
			case 'o':
				
				soundSources.get("bounceHigh").play();
				break;
			default:
				break;
			}
		}
	}
	
	private void AudioTestMovement() {
		System.out.println("Starting moving sound test");
		
		int counter = 0, dT = 10;
		
		while (counter < 10000) {
			sourcePosition.x += sourceVelocity.x;
			soundSources.get("bounce").setPosition(sourcePosition);
			
			if (Math.abs(sourcePosition.x) >= 9)
				sourceVelocity.x *= -1;
			
			try {Thread.sleep(dT);} 
			catch (InterruptedException e) {}
			
			counter += dT;
		}
		
		soundSources.get("bounce").stop();
		
		System.out.println("Done!");
	}
	
	private void AudioTestPitch() {
		System.out.println("Starting sound pitch test");
		
		soundSources.get("bounce").play();
		
		int counter = 0, dT = 10;
		float pitch = 0.5f;
		while (counter < 5000) {
			pitch = (counter/5000f)+0.5f;
			soundSources.get("bounce").setPitch(pitch);
			
			try {Thread.sleep(dT);} 
			catch (InterruptedException e) {}
			
			counter += dT;
		}
		
		soundSources.get("bounce").stop();
		
		System.out.println("Done!");
	}
	
	private void AudioTestRPM() {
		System.out.println("Starting RPM Test");
		
		SoundCollection.initializeSounds();
		
		float rpm = 500, dT = 1;
		while (rpm < 2700) {
			SoundCollection.setRPM(rpm);
			
			try {Thread.sleep((int)dT * 5);} 
			catch (InterruptedException e) {}
			
			rpm += dT;
		}
		
		try {Thread.sleep(5000);} 
		catch (InterruptedException e) {}
		
		SoundCollection.cleanUp();
		
		System.out.println("Done!");
	}
	
	private void AudioTestGear() {
		System.out.println("Starting Gear Test");
		
		SoundCollection.initializeSounds();
		
		float gear = 1.0f;
		
		SoundCollection.setControl(SoundEvent.GEAR, gear);
		
		try {Thread.sleep(5000);} 
		catch (InterruptedException e) {}
		
		gear = 0.0f;
		
		SoundCollection.setControl(SoundEvent.GEAR, gear);
		
		SoundCollection.cleanUp();
		
		System.out.println("Done!");
	}
	
	private void AudioTestFlaps() {
		System.out.println("Starting Flaps Test");
		
		SoundCollection.initializeSounds();
		
		for (float flaps = 0; flaps < 30; flaps += 1.0) {
			SoundCollection.setControl(SoundEvent.FLAPS, flaps);
			
			try {Thread.sleep(200);} 
			catch (InterruptedException e) {}
		}
		
		try {Thread.sleep(1000);} 
		catch (InterruptedException e) {}
		
		for (float flaps = 30; flaps > 0; flaps -= 1.0) {
			SoundCollection.setControl(SoundEvent.FLAPS, flaps);
			
			try {Thread.sleep(200);} 
			catch (InterruptedException e) {}
		}
		
		SoundCollection.cleanUp();
		
		System.out.println("Done!");
	}
	
	private void AudioTestStall() {
		System.out.println("Starting Stall Test");
		
		SoundCollection.initializeSounds();
		
		for (float alpha = 0; alpha < 0.5f; alpha += 0.02) {
			SoundCollection.setStallHorn(alpha, 0.25f);
			
			try {Thread.sleep(20);} 
			catch (InterruptedException e) {}
		}
		
		try {Thread.sleep(1000);} 
		catch (InterruptedException e) {}
		
		for (float alpha = 0.5f; alpha > 0.0f; alpha -= 0.02) {
			SoundCollection.setStallHorn(alpha, 0.25f);
			
			try {Thread.sleep(200);} 
			catch (InterruptedException e) {}
		}
		
		SoundCollection.cleanUp();
		
		System.out.println("Done!");
	}
	
	private void AudioTestWind() {
		System.out.println("Starting Wind Test");
		
		SoundCollection.initializeSounds();
		
		for (float trueAirspeed = 30; trueAirspeed < 300; trueAirspeed += 1.0) {
			SoundCollection.setWind(trueAirspeed);
			
			try {Thread.sleep(50);} 
			catch (InterruptedException e) {}
		}
		
		try {Thread.sleep(1000);} 
		catch (InterruptedException e) {}
		
		SoundCollection.cleanUp();
		
		System.out.println("Done!");
	} 
	
	private void AudioTestSoundCollection() {
		System.out.println("Starting sound collection test");
		
		SoundCollection.initializeSounds();
		
		SoundCollection.play(SoundEvent.BOUNCE_LOOP);
		
		try {Thread.sleep(500);} 
		catch (InterruptedException e) {}
		
		SoundCollection.stop(SoundEvent.BOUNCE_LOOP);
		
		try {Thread.sleep(500);} 
		catch (InterruptedException e) {}
		
		SoundCollection.play(SoundEvent.BOUNCE_HIGH);
		
		try {Thread.sleep(500);} 
		catch (InterruptedException e) {}
		
		SoundCollection.cleanUp();
		
		System.out.println("Done");
	}
	
	private void finish() {
		for (Map.Entry<String, SoundSource> entry : soundSources.entrySet())
			entry.getValue().delete();
		
		AudioMaster.cleanUp();
	}
}
