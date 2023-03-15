package com.moseoh.kpa.utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Utils {
    private static Utils instance;

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }
        return instance;
    }

    public String pathToPackage(String path, boolean isSpring) {
        String[] arr = path.split("/");
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals("src")) {
                index = i;
                break;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = isSpring ? index + 3: index; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i != arr.length - 1) sb.append(".");
        }
        return sb.toString();
    }

    public void writeActions(Runnable runnable) {
        ApplicationManager.getApplication().runWriteAction(runnable);
    }

    public VirtualFile createVirtualFile(VirtualFile folder, File tempFile, String fileName) {
        try {
            return folder.createChildData(tempFile, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeContent(VirtualFile file, String fileContent, Map<String, String> values) {
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

    public String getClipboard() {
        CopyPasteManager manager = CopyPasteManager.getInstance();
        StringSelection data = (StringSelection) manager.getContents();
        if (data == null) {
            throw new RuntimeException("clipboard is null");
        }
        try {
            return StringUtil.notNullize((String) data.getTransferData(DataFlavor.stringFlavor));
        } catch (UnsupportedFlavorException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
