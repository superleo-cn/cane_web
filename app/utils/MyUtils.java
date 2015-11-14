package utils;

import org.apache.commons.lang3.StringUtils;

public class MyUtils {

    public static String getUnicode(String val) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNoneEmpty(val)) {
            for (int i = 0; i < val.length(); i++) {
                sb.append("\\u " + Integer.toHexString(val.charAt(i)));
            }
        }
        return sb.toString();
    }

}
