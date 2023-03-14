package com.moseoh.kpa.dialog.model;

import lombok.Getter;

@Getter
public enum FileInfo {
    CONTROLLER("Controller", "/controller", "controller.ftl"),
    DTO("Dto", "/controller/dto", "dto.ftl"),
    ENTITY("", "/entity", "entity.ftl"),
    REPOSITORY("Repository", "/entity/repository", "repository.ftl"),
    SERVICE("Service", "/service", "service.ftl");

    private final String fileNameSuffix;
    private final String filePath;
    private final String templateName;

    FileInfo(String fileNameSuffix, String filePath, String templateName) {
        this.fileNameSuffix = fileNameSuffix;
        this.filePath = filePath;
        this.templateName = templateName;
    }
}
