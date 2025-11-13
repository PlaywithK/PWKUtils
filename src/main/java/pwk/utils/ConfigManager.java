package pwk.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    //Prefixes
    private static final String DEFAULT_PREFIX_OP = "§4Admin §8| §c";
    private static final String DEFAULT_PREFIX_NON_OP = "§2Player §8| §a";

    //JoinLeave
    private static final String DEFAULT_JOIN = "§a» §8|§r {player}";
    private static final String DEFAULT_LEAVE = "§c« §8|§r {player}";
    private static final boolean DEFAULT_CHANGE_JOIN_LEAVE = true;

    //Chat
    private static final String DEFAULT_CHAT_FORMAT = "{player}§8: §7{message}";
    private static final boolean DEFAULT_ALLOW_CHAT_FORMATTING = true;

    //Welcome Message
    private static final boolean DEFAULT_SHOW_WELCOME_MESSAGE = true;
    private static final String DEFAULT_WELCOME = "§7>————— INFO —————<\n\n§eWelcome!\n\n§7>——————————————<";


    //Tablist
    private static final String DEFAULT_TABLIST_HEADER = "§3This is a header!\n§7>————————————<";
    private static final String DEFAULT_TABLIST_FOOTER = "§7>————————————<§6\nThank you for using §3§lPWKUtils§6!";
    private static final boolean DEFAULT_SHOW_ONLINE_PLAYERS = true;

    //Werte setzten
    private static String prefixOp = DEFAULT_PREFIX_OP;
    private static String prefixNonOp = DEFAULT_PREFIX_NON_OP;

    private static String joinMessage = DEFAULT_JOIN;
    private static String leaveMessage = DEFAULT_LEAVE;
    private static boolean changeJoinLeave = DEFAULT_CHANGE_JOIN_LEAVE;

    private static String chatFormat = DEFAULT_CHAT_FORMAT;
    private static boolean allowChatFormatting = DEFAULT_ALLOW_CHAT_FORMATTING;

    private static String welcomeMessage = DEFAULT_WELCOME;
    private static boolean showWelcomeMessage = DEFAULT_SHOW_WELCOME_MESSAGE;

    private static String tablistHeader = DEFAULT_TABLIST_HEADER;
    private static String tablistFooter = DEFAULT_TABLIST_FOOTER;
    private static boolean showOnlinePlayers = DEFAULT_SHOW_ONLINE_PLAYERS;

    private static final String CONFIG_FOLDER = "pwkutils";
    private static final String CONFIG_FILE_NAME = "config.json";

    public static void loadConfig() {
        try {
            File folder = new File(CONFIG_FOLDER);
            if (!folder.exists() && !folder.mkdirs()) {
                System.err.println("Fehler beim Erstellen des Config-Ordners: " + folder.getAbsolutePath());
            }

            File configFile = new File(folder, CONFIG_FILE_NAME);

            if (!configFile.exists()) {
                JsonObject defaultJson = new JsonObject();
                defaultJson.addProperty("prefixOp", DEFAULT_PREFIX_OP);
                defaultJson.addProperty("prefixNonOp", DEFAULT_PREFIX_NON_OP);


                defaultJson.addProperty("changeJoinLeaveMessage", DEFAULT_CHANGE_JOIN_LEAVE);
                defaultJson.addProperty("joinMessage", DEFAULT_JOIN);
                defaultJson.addProperty("leaveMessage", DEFAULT_LEAVE);

                defaultJson.addProperty("allowChatFormatting", DEFAULT_ALLOW_CHAT_FORMATTING);
                defaultJson.addProperty("chatFormat", DEFAULT_CHAT_FORMAT);

                defaultJson.addProperty("showWelcomeMessage", DEFAULT_SHOW_WELCOME_MESSAGE);
                defaultJson.addProperty("welcomeMessage", DEFAULT_WELCOME);

                defaultJson.addProperty("tablistHeader", DEFAULT_TABLIST_HEADER);
                defaultJson.addProperty("tablistFooter", DEFAULT_TABLIST_FOOTER);
                defaultJson.addProperty("showOnlinePlayers", DEFAULT_SHOW_ONLINE_PLAYERS);

                try (FileWriter writer = new FileWriter(configFile)) {
                    GSON.toJson(defaultJson, writer);
                }
                System.out.println("Default Config erstellt: " + configFile.getAbsolutePath());
            }

            //Config laden
            try (FileReader reader = new FileReader(configFile)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                if (json.has("prefixOp")) prefixOp = json.get("prefixOp").getAsString();
                if (json.has("prefixNonOp")) prefixNonOp = json.get("prefixNonOp").getAsString();

                if (json.has("changeJoinLeaveMessage")) changeJoinLeave = json.get("changeJoinLeaveMessage").getAsBoolean();
                if (json.has("joinMessage")) joinMessage = json.get("joinMessage").getAsString();
                if (json.has("leaveMessage")) leaveMessage = json.get("leaveMessage").getAsString();
                if (json.has("allowChatFormatting")) allowChatFormatting = json.get("allowChatFormatting").getAsBoolean();
                if (json.has("chatFormat")) chatFormat = json.get("chatFormat").getAsString();

                if (json.has("showWelcomeMessage")) showWelcomeMessage = json.get("showWelcomeMessage").getAsBoolean();
                if (json.has("welcomeMessage")) welcomeMessage = json.get("welcomeMessage").getAsString();

                if (json.has("tablistHeader")) tablistHeader = json.get("tablistHeader").getAsString();
                if (json.has("tablistFooter")) tablistFooter = json.get("tablistFooter").getAsString();
                if (json.has("showOnlinePlayers")) showOnlinePlayers = json.get("showOnlinePlayers").getAsBoolean();
            }

        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Config: " + e.getMessage());
        }
    }

    //Join/Leave
    public static boolean shouldChangeJoinLeave() { return changeJoinLeave; }
    public static String getJoinMessage(String playerName) { return joinMessage.replace("{player}", playerName); }
    public static String getLeaveMessage(String playerName) { return leaveMessage.replace("{player}", playerName); }

    //Chat
    public static boolean isChatFormattingAllowed() { return allowChatFormatting; }
    public static String getChatFormat(String playerName, String message) {
        return chatFormat.replace("{player}", playerName).replace("{message}", message);
    }

    // Tablist
    public static String getTablistHeader() { return tablistHeader; }
    public static String getTablistFooter(int online, int max) {
        if (showOnlinePlayers) {
            return tablistFooter + "\n§5§lOnline: §a" + online + "§7/§2" + max;
        }
        return tablistFooter;
    }
    public static boolean shouldShowOnlinePlayers() { return showOnlinePlayers; }

    //Prefixes
    public static String getOpPrefix() { return prefixOp; }
    public static String getNonOpPrefix() { return prefixNonOp; }

    //Welcome Message
    public static String getWelcomeMessage() { return welcomeMessage; }
    public static boolean shouldShowWelcomeMessage() {
        return showWelcomeMessage;
    }


}
