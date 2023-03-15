package com.moseoh.kpa.dialog.domain;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.uiDesigner.core.Spacer;
import com.moseoh.kpa.dialog.domain.model.DomainForm;
import com.moseoh.kpa.dialog.domain.model.Language;
import com.moseoh.kpa.dialog.domain.service.DomainService;
import com.moseoh.kpa.utils.Store;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class CreateDomainDialog extends DialogWrapper {
    private final Store store;
    private final Project project;

    JPanel contentPane;
    JPanel sectionCenter, sectionEmpty;
    JPanel domainGroup, domainPathGroup;
    JLabel domainGroupError, domainPathGroupError;

    private JTextField domainField;
    private TextFieldWithBrowseButton domainPathField;

    public CreateDomainDialog(Project project) {
        super(true);
        this.project = project;
        this.store = Store.getInstance();

        setTitle("Create Domain");
        setSize(500, -1);
        init();
    }


    @Override
    protected void doOKAction() {
        if (!isValidated()) return;

        String path = domainPathField.getText();
        store.setDomainPath(path);

        DomainForm domainForm = DomainForm.builder()
                .domain(domainField.getText())
                .domainPath(path)
                .language(Language.KOTLIN)
                .build();

        DomainService domainService = new DomainService();
        domainService.create(domainForm);

        super.doOKAction();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        contentPane = new JPanel(new GridLayout(3, 1));

        sectionCenter = new JPanel(new GridLayout(2, 1));
        sectionCenter.add(domainGroupPanel());
        sectionCenter.add(domainPathGroupPanel());

        sectionEmpty = new JPanel(new GridLayout(1, 1));
        sectionEmpty.add(new Spacer());

        contentPane.add(sectionCenter);
        contentPane.add(sectionEmpty);
        return contentPane;
    }

    private JPanel domainGroupPanel() {
        domainGroup = new JPanel(new GridLayout(2, 1));
        JPanel domainGroupContent = new JPanel(new GridLayout(1, 2));
        JLabel domainLabel = new JLabel("Domain Name: ");
        domainField = new JTextField();
        domainGroupContent.add(domainLabel);
        domainGroupContent.add(domainField);
        domainGroup.add(domainGroupContent);
        return domainGroup;
    }


    private JPanel domainPathGroupPanel() {
        domainPathGroup = new JPanel(new GridLayout(2, 1));
        JPanel domainPathGroupContent = new JPanel(new GridLayout(1, 2));
        JLabel domainPathLabel = new JLabel("Domain path: ");
        domainPathField = new TextFieldWithBrowseButton();
        domainPathField.setEditable(false);
        domainPathField.addActionListener(e -> {
            VirtualFile selectedFile = chooseFolder();
            if (selectedFile != null) {
                domainPathField.setText(selectedFile.getPath());
            }
        });
        if (store.getDomainPath() != null) {
            domainPathField.setText(store.getDomainPath());
        }
        domainPathGroupContent.add(domainPathLabel);
        domainPathGroupContent.add(domainPathField);
        domainPathGroup.add(domainPathGroupContent);
        return domainPathGroup;
    }


    private VirtualFile chooseFolder() {
        String path = domainPathField.getText();
        if (path.isEmpty()) {
            if (store.getDomainPath() == null) {
                path = project.getBasePath();
            } else {
                path = store.getDomainPath();
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

    private boolean isValidated() {
        if (domainField.getText().isEmpty()) {
            domainGroupError = new JLabel("Domain name is empty.");
            domainGroup.add(domainGroupError);
            return false;
        } else {
            if (domainGroupError != null) {
                domainGroup.remove(domainGroupError);
            }
        }

        if (domainPathField.getText().isEmpty()) {
            domainPathGroupError = new JLabel("Domain name is empty.");
            domainPathGroup.add(domainPathGroupError);
            return false;
        } else {
            if (domainPathGroupError != null) {
                domainPathGroup.remove(domainPathGroupError);
            }
        }

        return true;
    }


}
