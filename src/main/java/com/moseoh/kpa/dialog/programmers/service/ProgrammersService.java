package com.moseoh.kpa.dialog.programmers.service;

import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.moseoh.kpa.dialog.programmers.model.ProgrammersContent;
import com.moseoh.kpa.dialog.programmers.model.ProgrammersForm;
import com.moseoh.kpa.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProgrammersService {
    private final Utils utils;

    public ProgrammersService() {
        this.utils = Utils.getInstance();
    }

    public void create(ProgrammersForm form) {
        try {
            String packagePath = form.getPackagePath();
            VirtualFile folder = VfsUtil.createDirectories(packagePath);
            File tempFile = new File(".");
            String programmersContent = ProgrammersContent.KOTLIN.getContent();
            utils.writeActions(() -> {
                VirtualFile file = utils.createVirtualFile(folder, tempFile, "Solution.kt");
                utils.writeContent(file, programmersContent, getFreemarkerValues(form));
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private Map<String, String> getFreemarkerValues(ProgrammersForm form) {
        Map<String, String> values = new HashMap<>();
        values.put("package", form.getPackage());
        values.put("values", getValues(form));
        values.put("properties", getProperties(form));
        values.put("classContent", form.getClassContent());
        values.put("returnType", returnType(form));
        System.out.println(values);
        return values;
    }

    private String returnType(ProgrammersForm form) {
        String[] str = form.getClassContent().split("\n");
        for (String s : str) {
            if (!s.trim().startsWith("fun solution(")) continue;
            String temp = s.substring(s.indexOf(")"));
            if (temp.toLowerCase().indexOf("array") > 0) {
                return ".contentToString()";
            }
        }
        return "";
    }

    private String getValues(ProgrammersForm form) {
        String[] str = form.getExample().split("\n");
        String[] name = str[0].split("\t");
        String[] value = str[1].split("\t");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < name.length - 1; i++) {
            sb.append("val").append(" ");
            sb.append(name[i]).append(" ");
            sb.append("=").append(" ");
            sb.append(value[i].replaceAll("\\[", "{").replaceAll("\\]", "}"));
            if (i == name.length - 2) break;
            sb.append("\n\t");
        }

        return sb.toString();
    }

    private String getProperties(ProgrammersForm form) {
        String[] str = form.getExample().split("\n");
        String[] name = str[0].split("\t");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length - 1; i++) {
            sb.append(name[i]);
            if (i == name.length - 2) break;
            sb.append(",").append(" ");
        }
        return sb.toString();
    }
}
