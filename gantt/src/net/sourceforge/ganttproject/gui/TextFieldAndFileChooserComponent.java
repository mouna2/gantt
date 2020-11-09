/*
 * Created on 01.05.2005
 */
package net.sourceforge.ganttproject.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author bard
 */
public class TextFieldAndFileChooserComponent {
    private TestGanttRolloverButton myChooserButton;

    private JTextField myTextField;

    private File myFile;

    private FileFilter myFileFilter;

    private String myDialogCaption;

    private Component myComponent;

    private Component myParentComponent;

    private int myFileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES;

    public TextFieldAndFileChooserComponent(final Component parentComponent,
            String dialogCaption) {
        myDialogCaption = dialogCaption;
        myParentComponent = parentComponent;
        initComponents();
    }

    public TextFieldAndFileChooserComponent(String label, String dialogCaption) {
        Box innerBox = Box.createHorizontalBox();
        innerBox.add(new JLabel(label));
        innerBox.add(Box.createHorizontalStrut(2));
        myParentComponent = innerBox;
        myDialogCaption = dialogCaption;
        initComponents();
        innerBox.add(myComponent);
        myComponent = innerBox;
    }

    private void initComponents() {
        myChooserButton = new TestGanttRolloverButton(new ImageIcon(getClass()
                .getResource("/icons/open_16.gif")));
        myTextField = new JTextField();
        myTextField.setColumns(40);
        myTextField.setEditable(false);
        Box box = Box.createHorizontalBox();
        box.add(myTextField);
        box.add(myChooserButton);
        myComponent = box;
        myChooserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showFileChooser();
            }
        });
    }

    public Component getComponent() {
        return myComponent;
    }

    public File getFile() {
        return myFile;
    }

    public void setFile(File file) {
        myFile = file;
        myTextField.setText(file == null ? "" : file.getAbsolutePath()); 
    }

    public void setFileFilter(FileFilter filter) {
        myFileFilter = filter;
    }

    public void showFileChooser() {
        System.out.println(myTextField.getText());
        JFileChooser fc = new JFileChooser(new File(myTextField.getText()));
        fc.setDialogTitle(myDialogCaption);
        fc.setApproveButtonToolTipText(myDialogCaption);
        fc.setFileSelectionMode(myFileSelectionMode);
        // Remove the possibility to use a file filter for all files
        FileFilter[] filefilters = fc.getChoosableFileFilters();
        for (int i = 0; i < filefilters.length; i++) {
            fc.removeChoosableFileFilter(filefilters[i]);
        }

        fc.addChoosableFileFilter(myFileFilter);
        int returnVal = fc.showDialog(myParentComponent, GanttLanguage
                .getInstance().getText("ok"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            onFileChosen(fc.getSelectedFile());
        }
    }

    protected void onFileChosen(File file) {
        myFile = file;
        myTextField.setText(myFile.getAbsolutePath());

    }

    public void setFileSelectionMode(int mode) {
        myFileSelectionMode = mode;
    }

}
