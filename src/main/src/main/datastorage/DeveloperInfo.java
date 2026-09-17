package main.datastorage;

//A class for a data storage of the Author info (e.g name, face, and it's sound)
public class DeveloperInfo {

    public final String label; //Shown in the About menu, e.g "BULAN, Elijah"
    public final String imagepath; //e.g "./ingamepics/faces/allen.png"
    public final String soundPath; //e.g "./main.DataStorage.audio.Sounds/authors/allenjump.wav"

    public DeveloperInfo(String label, String Imagepath, String SoundPath) {
        this.label = label;
        this.imagepath = Imagepath;
        this.soundPath = SoundPath;
    }

    //Overriding this so the label shows cleanly wherever toString() is used.
    @Override
    public String toString() {
        return label;
    }
}
