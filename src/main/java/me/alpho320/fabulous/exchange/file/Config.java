package me.alpho320.fabulous.exchange.file;

import me.alpho320.fabulous.exchange.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config extends YamlConfiguration {

    private final File file;

    public Config() { //"plugin/Fabulous/CinematicWalks/config.yml"
        file = new File(Main.getInstance().getDataFolder(), "config.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            saveDefaults();
        }
        reload();
    }

    private void saveDefaults() {
        Main.getInstance().saveResource("config.yml", false);
    }

    public void reload() {
        try {
            super.load(this.file);
        } catch (Exception ignored) {
        }
    }

    public void save() {
        try {
            super.save(this.file);
        } catch (Exception ignored) {
        }
    }
}
