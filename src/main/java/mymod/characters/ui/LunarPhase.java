package mymod.characters.ui;

public enum LunarPhase {
    NUEVA("Nueva"), CRECIENTE("Creciente"), LLENA("Llena"), MENGUANTE("Menguante");

    private final String imageName;
    LunarPhase(String imageName) { this.imageName = imageName; }
    public String getImageName() { return imageName; }
    public LunarPhase next() { return values()[(ordinal() + 1) % values().length]; }
}
