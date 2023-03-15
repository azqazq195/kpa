package com.moseoh.kpa.dialog.programmers.model;

import lombok.Getter;

@Getter
public enum ProgrammersContent {
    KOTLIN("package ${package}\n" +
            "\n" +
            "fun main(args: Array<String>) {\n" +
            "    ${values}\n"+
            "    println(Solution().solution(${properties})${returnType})\n" +
            "}\n" +
            "\n" +
            "${classContent}");

    private final String content;

    ProgrammersContent(String content) {
        this.content = content;
    }
}
