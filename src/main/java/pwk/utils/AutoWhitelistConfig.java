package pwk.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AutoWhitelistConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Path WHITELIST_CONFIG_FOLDER = Path.of("pwkutils");
    private static final Path WHITELIST_CONFIG_FILE = WHITELIST_CONFIG_FOLDER.resolve("autowhitelistconfig.json");

    public List<String> players = new ArrayList<>();

    public static AutoWhitelistConfig loadConfig() {
        try {
            if (!Files.exists(WHITELIST_CONFIG_FOLDER)) {
                Files.createDirectories(WHITELIST_CONFIG_FOLDER);
            }

            if (!Files.exists(WHITELIST_CONFIG_FILE)) {
                AutoWhitelistConfig cfg = new AutoWhitelistConfig();
                cfg.save();
                return cfg;
            }

            try (Reader reader = new FileReader(WHITELIST_CONFIG_FILE.toFile())) {
                return GSON.fromJson(reader, AutoWhitelistConfig.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new AutoWhitelistConfig();
        }
    }

    public void save() {
        try (Writer writer = new FileWriter(WHITELIST_CONFIG_FILE.toFile())) {
            GSON.toJson(this, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
