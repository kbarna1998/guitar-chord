package service;

public class StringFormatter {

    public static String firstLetterUpperCase(String string) {
        if (string.isEmpty()) {
            return string;
        } else if (string.length() == 1) {
            return string.toUpperCase();
        } else {
            return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
        }
    }
}
