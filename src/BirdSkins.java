public class BirdSkins {
    public final String name; //This is shown in the settings dropdown e.g "AllenBird"
    public final String imagePath; //e.g "./AllenBird.png"
    public final String jumpSoundPath; //e.g "./AllenBird.wav"

    public BirdSkins(String name, String imagePath, String jumpSoundPath) {
        this.name = name;
        this.imagePath = imagePath;
        this.jumpSoundPath = jumpSoundPath;
    }

    //Overriding this so the name won't show ugly.
    //JComboBox is used in the SettingMenu class to display whatever
    //toString() returns for each item.
    @Override
    public String toString() {
        return name;
    }
}
