package main.datastorage.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

//A class for a data storage of sounds
public class Sounds {
        public static void playSound(String resourcePath) {

            //This plays a one-shot sound (It just play once)
            try {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                        Sounds.class.getResource(resourcePath)
                );
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
                //Clip is open because closing in would immediately cut the sound right away
            } catch (Exception e) {
                System.out.println("Could not play sound lol: " + resourcePath);
                e.printStackTrace();
                //If the file is missing it prints out "Could not play sound lol: "
            }
        }
        //Returns the clip that you control your play through (e.g playing or stopping)
        //This is used for background music, where you need to start it, to loop it continuously, and stop it later
        public static Clip loadLoopingClip(String resourcePath){
            try {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                        Sounds.class.getResource(resourcePath)
                );
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                return clip;
            } catch (Exception e) {
                System.out.println("Couldn't load music: " + resourcePath);
                e.printStackTrace();
                return null;
            }
        }
}