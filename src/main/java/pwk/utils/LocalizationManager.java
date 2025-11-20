package pwk.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;

public class LocalizationManager {
    private static final Gson GSON = new Gson();
    private static JsonObject data;
    private static final File EXTERNAL_FILE = new File("pwkutils/messages.json");

    private static final String PREFIX = "§8[§3PWKUtils§8] §7";


    public static void load() {
        try {
            if (!EXTERNAL_FILE.exists()) {
                EXTERNAL_FILE.getParentFile().mkdirs();

                try (InputStream stream = LocalizationManager.class
                        .getClassLoader()
                        .getResourceAsStream("assets/pwkutils/messages.json")) {

                    if (stream == null) {
                        System.err.println("[PWKUtils] Default messages.json not found in mod!");
                        data = new JsonObject();
                        return;
                    }

                    try (FileOutputStream out = new FileOutputStream(EXTERNAL_FILE)) {
                        stream.transferTo(out);
                        System.out.println("[PWKUtils] Created external messages.json on the server.");
                    }
                }
            }

            try (FileReader reader = new FileReader(EXTERNAL_FILE)) {
                data = GSON.fromJson(reader, JsonObject.class);
                System.out.println("[PWKUtils] Loaded messages.json from server folder.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String key, Object... args) {
        if (data == null || !data.has(key)) return PREFIX + key;
        String msg = String.format(data.get(key).getAsString(), args);
        return PREFIX + msg;
    }
}
