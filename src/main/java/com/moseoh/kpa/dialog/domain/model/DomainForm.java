package com.moseoh.kpa.dialog.domain.model;

import com.intellij.openapi.util.text.StringUtil;
import com.moseoh.kpa.utils.Utils;
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
        return Utils.getInstance().pathToPackage(getDomainPath(),true);
    }

    public String getPackage(String filePath) {
        return Utils.getInstance().pathToPackage(getFilePath(filePath),true);
    }
}
