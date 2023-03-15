package com.moseoh.kpa.dialog.domain.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.moseoh.kpa.dialog.domain.model.DomainForm;
import com.moseoh.kpa.dialog.domain.model.FileContent;
import com.moseoh.kpa.dialog.domain.model.FileInfo;
import com.moseoh.kpa.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DomainService {
    private final static Utils utils = Utils.getInstance();

    public void create(DomainForm form) {
        try {
            for (FileInfo fileInfo : FileInfo.values()) {
                create(form, fileInfo);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void create(DomainForm form, FileInfo fileInfo) throws IOException {
        String fileName = form.getFileName(fileInfo.getFileNameSuffix());
        String filePath = form.getFilePath(fileInfo.getFilePath());
        VirtualFile folder = VfsUtil.createDirectories(filePath);
        File tempFile = new File(".");
        String fileContent = FileContent.valueOf(fileInfo.name()).getContent();
        utils.writeActions(() -> {
            VirtualFile file = utils.createVirtualFile(folder, tempFile, fileName);
            utils.writeContent(file, fileContent, getFreemarkerValues(form, fileInfo));
        });
    }

    private Map<String, String> getFreemarkerValues(DomainForm form, FileInfo fileInfo) {
        Map<String, String> values = new HashMap<>();
        values.put("package", form.getPackage(fileInfo.getFilePath()));
        values.put("basePackage", form.getBasePackage());
        values.put("domain", form.getDomain());
        values.put("domainName", form.getDomainName());
        return values;
    }

}
