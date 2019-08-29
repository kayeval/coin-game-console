package model.util;

public class Helper {
    public static String toTitleCase(String input) {
        return String.format("%s", input.substring(0, 1) + input.substring(1).toLowerCase());
    }
}
