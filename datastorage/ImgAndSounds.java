package main.datastorage;

//A class for a data Storage of the images and effects
public class ImgAndSounds {

    public final String name; //This is shown in the settings dropdown e.g "AllenBird"
    public final String imagePath; //e.g "./AllenBird.png"
    public final String jumpSoundPath; //e.g "./AllenBird.wav"

    public ImgAndSounds(String name, String imagePath, String jumpSoundPath) {
        this.name = name;
        this.imagePath = imagePath;
        this.jumpSoundPath = jumpSoundPath;
    }

    //Overriding this so the name won't show ugly.
    //JComboBox is used in the main.ui.SettingMenu class to display whatever
    //toString() returns for each item.
    @Override
    public String toString() {
        return name;
    }
}
