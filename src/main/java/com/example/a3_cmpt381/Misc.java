package com.example.a3_cmpt381;

public class Misc {
    private Misc(){};

    static String getResource(Class<?> cls, String url) {
        return cls.getResource(url).toExternalForm();
    }
}
