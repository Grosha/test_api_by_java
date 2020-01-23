package helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHelper {

    public static boolean matchExpression(String text, String expression) {
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
