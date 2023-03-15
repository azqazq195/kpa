package com.moseoh.kpa.dialog.domain.model;

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
