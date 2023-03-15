package com.moseoh.kpa;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.moseoh.kpa.dialog.programmers.CreateProgrammersDialog;

public class CreateProgrammers extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        new CreateProgrammersDialog(e.getProject()).show();
    }
}
