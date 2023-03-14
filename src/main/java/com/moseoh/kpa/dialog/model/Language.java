package com.moseoh.kpa.dialog.model;

public enum Language {
    JAVA(".java"),
    KOTLIN(".kt");

    private final String extension;

    Language(String extensions) {
        this.extension = extensions;
    }

    public String getExtension() {
        return extension;
    }
}
