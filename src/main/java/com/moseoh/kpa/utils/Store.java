package com.moseoh.kpa.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Store {
    private static Store instance;

    public static Store getInstance() {
        if (instance == null) {
            instance = new Store();
        }
        return instance;
    }

    private String domainPath;

}
