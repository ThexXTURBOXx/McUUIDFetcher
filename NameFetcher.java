//package your.package.here;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Helper-class for getting names of players.
 */
public final class NameFetcher {

    private static final String NAME_URL = "https://sessionserver.mojang.com"
            + "/session/minecraft/profile/";

    private NameFetcher() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the name of the searched player.
     *
     * @param uuid The UUID of a player.
     * @return The name of the given player.
     */
    public static String getName(UUID uuid) {
        return getName(uuid.toString());
    }

    /**
     * Returns the name of the searched player.
     *
     * @param uuid The UUID of a player (can be trimmed or the normal version).
     * @return The name of the given player.
     */
    public static String getName(String uuid) {
        uuid = uuid.replace("-", "");
        String output = callURL(NAME_URL + uuid);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 20000; i++) {
            if (output.charAt(i) == 'n' && output.charAt(i + 1) == 'a'
                    && output.charAt(i + 2) == 'm'
                    && output.charAt(i + 3) == 'e') {
                for (int k = i + 9; k < 20000; k++) {
                    char curr = output.charAt(k);
                    if (curr != '"') {
                        result.append(curr);
                    } else {
                        break;
                    }
                }
                break;
            }
        }
        return result.toString();
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
