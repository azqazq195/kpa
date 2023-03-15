package com.moseoh.kpa.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.moseoh.kpa.dialog.domain.CreateDomainDialog;

import javax.swing.*;

public class KPAToolWindow {
    private JButton createDomain;
    private JPanel kpaToolWindowContent;

    public KPAToolWindow(Project project, ToolWindow toolWindow) {
        createDomain.addActionListener(e -> new CreateDomainDialog(project).show());
    }

    public JPanel getContent() {
        return kpaToolWindowContent;
    }

}
