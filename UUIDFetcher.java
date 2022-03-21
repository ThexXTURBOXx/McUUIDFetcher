//package your.package.here;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/*
Uncomment this if you want the helper method for BungeeCord:
import net.md_5.bungee.api.connection.ProxiedPlayer;
*/

/*
Uncomment this if you want the helper method for Bukkit/Spigot:
import org.bukkit.entity.Player;
*/

/**
 * Helper-class for getting UUIDs of players.
 */
public final class UUIDFetcher {

    private static final String UUID_URL = "https://api.mojang.com/users"
            + "/profiles/minecraft/";

    private UUIDFetcher() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the UUID of the searched player.
     *
     * @param player The player.
     * @return The UUID of the given player.
     */
    //Uncomment this if you want the helper method for BungeeCord:
    /*
    public static UUID getUUID(ProxiedPlayer player) {
        return getUUID(player.getName());
    }
    */

    /**
     * Returns the UUID of the searched player.
     *
     * @param player The player.
     * @return The UUID of the given player.
     */
    //Uncomment this if you want the helper method for Bukkit/Spigot:
    /*
    public static UUID getUUID(Player player) {
        return getUUID(player.getName());
    }
    */

    /**
     * Returns the UUID of the searched player.
     *
     * @param playername The name of the player.
     * @return The UUID of the given player.
     */
    public static UUID getUUID(String playername) {
        String output = callURL(UUID_URL + playername);
        StringBuilder uuid = new StringBuilder();
        readData(output, uuid);
        return UUID.fromString(uuid.toString());
    }

    private static void readData(String toRead, StringBuilder result) {
        for (int i = toRead.length() - 3, j = 31; i >= 0; i--, j--) {
            if (toRead.charAt(i) != '"') {
                if ((j == 19) || (j == 15) || (j == 11) || (j == 7))
                    result.insert(0, '-');
                result.insert(0, toRead.charAt(i));
            } else {
                break;
            }
        }
    }

    private static String callURL(String urlStr) {
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn;
        InputStreamReader in;
        try {
            URL url = new URL(urlStr);
            urlConn = url.openConnection();
            if (urlConn != null) {
                urlConn.setReadTimeout(60 * 1000);
            }
            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(), StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(in);
                int cp;
                while ((cp = bufferedReader.read()) != -1) {
                    sb.append((char) cp);
                }
                bufferedReader.close();
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
