package com.radioforge.observatory;

final class Json {
    private Json() {}
    static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
    static String q(String s) { return "\"" + esc(s) + "\""; }
}
