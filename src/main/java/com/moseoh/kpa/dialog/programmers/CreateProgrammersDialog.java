package com.moseoh.kpa.dialog.programmers;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.uiDesigner.core.Spacer;
import com.moseoh.kpa.dialog.programmers.model.ProgrammersForm;
import com.moseoh.kpa.dialog.programmers.service.ProgrammersService;
import com.moseoh.kpa.utils.Store;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class CreateProgrammersDialog extends DialogWrapper {
    private final Store store;
    private final Project project;

    private TextFieldWithBrowseButton packagePathField;
    private JTextField nameField;
    private JTextArea exampleArea;
    private JTextArea classContentArea;

    public CreateProgrammersDialog(Project project) {
        super(true);
        this.project = project;
        this.store = Store.getInstance();

        setTitle("Create Programmers");
        setSize(800, -1);
        init();
    }

    @Override
    protected void doOKAction() {
        if (!isValidated()) return;

        String name = nameField.getText();
        String path = packagePathField.getText();
        String example = exampleArea.getText();
        String classContent = classContentArea.getText();
        store.setProgrammersPackagePath(path);

        ProgrammersForm programmersForm = ProgrammersForm.builder()
                .name(name)
                .packagePath(path + "/" + name)
                .example(example)
                .classContent(classContent)
                .build();

        ProgrammersService programmersService = new ProgrammersService();
        VirtualFile file = programmersService.create(programmersForm);
        FileEditorManager.getInstance(project).openTextEditor(new OpenFileDescriptor(project,file), true);
        super.doOKAction();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel content = new JPanel(new GridLayout(2, 1));
        JPanel sectionCenter = new JPanel(new GridLayout(2, 1));
        sectionCenter.add(packagePathGroupPanel());
        sectionCenter.add(nameGroupPanel());

        JPanel sectionBottom = new JPanel(new GridLayout(3, 1));
        JPanel spacer = new JPanel();
        spacer.setSize(-1, 50);
        sectionBottom.setSize(-1, 600);
        sectionBottom.add(exampleGroupPanel());
        sectionBottom.add(spacer);
        sectionBottom.add(classContentGroupPanel());

        content.add(sectionCenter);
        content.add(sectionBottom);
        return content;
    }

    private JPanel nameGroupPanel() {
        JPanel nameGroup = new JPanel(new GridLayout(2, 1));
        JPanel nameGroupContent = new JPanel(new GridLayout(1, 2));
        JLabel nameLabel = new JLabel("문제 이름: ");
        nameField = new JTextField();
        nameGroupContent.add(nameLabel);
        nameGroupContent.add(nameField);
        nameGroup.add(nameGroupContent);
        return nameGroup;
    }

    private JPanel exampleGroupPanel() {
        JPanel exampleGroupContent = new JPanel(new GridLayout(1,2));
        exampleGroupContent.setSize(600,300);
        JLabel exampleLabel = new JLabel("입출력 예: ");
        exampleArea = new JTextArea();
        exampleGroupContent.add(exampleLabel);
        exampleGroupContent.add(exampleArea);
        return exampleGroupContent;
    }

    private JPanel classContentGroupPanel() {
        JPanel classContentGroupContent = new JPanel(new GridLayout(1,2));
        classContentGroupContent.setSize(600,500);
        JLabel classContentLabel = new JLabel("class 본문: ");
        classContentArea = new JTextArea();
        classContentGroupContent.add(classContentLabel);
        classContentGroupContent.add(classContentArea);
        return classContentGroupContent;
    }


    private JPanel packagePathGroupPanel() {
        JPanel packagePathGroup = new JPanel(new GridLayout(2, 1));
        JPanel packagePathGroupContent = new JPanel(new GridLayout(1, 2));
        JLabel packagePathLabel = new JLabel("패키지 경로: ");
        packagePathField = new TextFieldWithBrowseButton();
        packagePathField.setEditable(false);
        packagePathField.addActionListener(e -> {
            VirtualFile selectedFile = chooseFolder();
            if (selectedFile != null) {
                packagePathField.setText(selectedFile.getPath());
            }
        });
        if (store.getProgrammersPackagePath() != null) {
            packagePathField.setText(store.getProgrammersPackagePath());
        }
        packagePathGroupContent.add(packagePathLabel);
        packagePathGroupContent.add(packagePathField);
        packagePathGroup.add(packagePathGroupContent);
        return packagePathGroup;
    }


    private VirtualFile chooseFolder() {
        String path = packagePathField.getText();
        if (path.isEmpty()) {
            if (store.getProgrammersPackagePath() == null) {
                path = project.getBasePath();
            } else {
                path = store.getProgrammersPackagePath();
            }
        }
        VirtualFile initialFile = LocalFileSystem.getInstance().findFileByPath(Objects.requireNonNull(path));
        if (initialFile == null) {
            initialFile = LocalFileSystem.getInstance().findFileByPath(Objects.requireNonNull(project.getBasePath()));
        }
        FileChooserDescriptor chooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        chooserDescriptor.setRoots(initialFile);
        return FileChooser.chooseFile(
                chooserDescriptor,
                project,
                initialFile
        );
    }

    public boolean isValidated() {
        if (nameField.getText().isEmpty()) {
            return false;
        }

        if (packagePathField.getText().isEmpty()) {
            return false;
        }

        if(exampleArea.getText().isEmpty()) {
            return false;
        }

        return !classContentArea.getText().isEmpty();
    }
}
