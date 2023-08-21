package net.nimajnebec.autoelytra;

public class Configuration {
    private static boolean autoEquipEnabled = true;

    public static void setAutoEquipEnabled(boolean value) {
        autoEquipEnabled = value;
    }

    public static boolean isAutoEquipEnabled() {
        return autoEquipEnabled;
    }

}
