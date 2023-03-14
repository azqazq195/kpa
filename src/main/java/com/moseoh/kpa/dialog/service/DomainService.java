package com.moseoh.kpa.dialog.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.moseoh.kpa.dialog.model.DomainForm;
import com.moseoh.kpa.dialog.model.FileContent;
import com.moseoh.kpa.dialog.model.FileInfo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DomainService {
    public void create(DomainForm domainForm) {
        try {
            for (FileInfo fileInfo : FileInfo.values()) {
                create(domainForm, fileInfo);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void create(DomainForm domainForm, FileInfo fileInfo) throws IOException {
        String fileName = domainForm.getFileName(fileInfo.getFileNameSuffix());
        String filePath = domainForm.getFilePath(fileInfo.getFilePath());
        VirtualFile folder = VfsUtil.createDirectories(filePath);
        File tempFile = new File(".");
        String fileContent = FileContent.valueOf(fileInfo.name()).getContent();
        writeActions(() -> {
            VirtualFile file = createFile(folder, tempFile, fileName);
            writeContent(file, fileContent, getFreemarkerValues(domainForm, fileInfo));
        });
    }

    private Map<String, String> getFreemarkerValues(DomainForm domainForm, FileInfo fileInfo) {
        Map<String, String> values = new HashMap<>();
        values.put("package", domainForm.getPackage(fileInfo.getFilePath()));
        values.put("basePackage", domainForm.getBasePackage());
        values.put("domain", domainForm.getDomain());
        values.put("domainName", domainForm.getDomainName());
        return values;
    }

    private void writeActions(Runnable runnable) {
        ApplicationManager.getApplication().runWriteAction(runnable);
    }

    private VirtualFile createFile(VirtualFile folder, File tempFile, String fileName) {
        try {
            return folder.createChildData(tempFile, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeContent(VirtualFile file, String fileContent, Map<String, String> values) {
        writeActions(() -> {
            try {
                String content = fileContent;
                for (String key : values.keySet()) {
                    String regex = "\\$\\{" + key + "}";
                    content = content.replaceAll(regex, values.get(key));
                }
                file.setBinaryContent(content.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
