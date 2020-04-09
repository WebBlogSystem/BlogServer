package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {
    public static boolean isPattern(String source,String pattern){
        return Pattern.matches(pattern,source);
    }
    public static void getMatcherStrings(String source,String matcher) {
        Pattern compile = Pattern.compile(matcher);
        System.out.println(compile);
        Matcher m = compile.matcher(source);
        while (m.find()) {
            System.out.println(m.group(0));
        }
    }
}
