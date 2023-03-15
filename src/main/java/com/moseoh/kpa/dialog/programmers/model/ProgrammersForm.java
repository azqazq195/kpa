package com.moseoh.kpa.dialog.programmers.model;

import com.moseoh.kpa.utils.Utils;
import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class ProgrammersForm {
    private String name;
    private String packagePath;
    private String example;
    private String classContent;

    public String getExample() {
        return example;
    }

    public String getClassContent() {
        return classContent;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public String getPackage() {
        return Utils.getInstance().pathToPackage(packagePath, false);
    }
}
