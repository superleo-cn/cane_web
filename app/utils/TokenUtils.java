package utils;

import org.h2.util.StringUtils;
import play.Play;
import play.api.libs.Codecs;

public class TokenUtils {

    public static String getToken(String deviceId) {
        String secret = Play.application().configuration().getString("play.crypto.secret");
        return Codecs.sha1(deviceId + "_" + secret);
    }

    public static Boolean compareToken(String deviceId, String sha1) {
        String secret = getToken(deviceId);
        if (StringUtils.equals(secret, sha1)) {
            return true;
        }
        return false;
    }

}
