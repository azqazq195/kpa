package com.moseoh.kpa.dialog.model;

import com.intellij.openapi.util.text.StringUtil;
import lombok.Builder;

@Builder
public class DomainForm {
    private String domain;
    private String domainPath;
    private Language language;

    public String getDomain() {
        return domain;
    }

    public String getDomainName() {
        return StringUtil.toTitleCase(domain);
    }

    public String getFileName(String fileNameSuffix) {
        return getDomainName() + fileNameSuffix + language.getExtension();
    }

    public String getDomainPath() {
        return domainPath + "/" + domain;
    }

    public String getFilePath(String filePath) {
        return getDomainPath() + filePath;
    }

    public String getBasePackage() {
        return pathToPackage(getDomainPath());
    }

    public String getPackage(String filePath) {
        return pathToPackage(getFilePath(filePath));
    }

    private String pathToPackage(String path) {
        String[] arr = path.split("/");
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals("src")) {
                index = i;
                break;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = index + 3; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i != arr.length - 1) sb.append(".");
        }
        return sb.toString();
    }
}
