/*
 * Created on 01.05.2005
 */
package net.sourceforge.ganttproject.importer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.ganttproject.filter.ExtensionBasedFileFilter;
import net.sourceforge.ganttproject.gui.FileChooserPageBase;
import net.sourceforge.ganttproject.gui.TextFieldAndFileChooserComponent;
import net.sourceforge.ganttproject.gui.options.OptionsPageBuilder;
import net.sourceforge.ganttproject.gui.options.SpringUtilities;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.gui.projectwizard.WizardPage;
import net.sourceforge.ganttproject.importer.ImportFileWizardImpl.State;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author bard
 */
class FileChooserPage extends FileChooserPageBase {

    private State myState;

    public FileChooserPage(State state) {
        myState = state;
    }

    
    protected String getFileChooserTitle() {
        return GanttLanguage.getInstance().getText("importerFileChooserPageTitle");
    }


    public String getTitle() {
        return GanttLanguage.getInstance().getText("importerFileChooserPageTitle");
    }


    public void setActive(boolean b) {
        if (b == false) {
            super.setActive(b);
            myState.setFile(getSelectedFile());
        } else {
            if (myState.getFile() != null) {
                setSelectedFile(myState.getFile());
            }
            super.setActive(b);
        }
    }


	protected FileFilter createFileFilter() {
		return new ExtensionBasedFileFilter(
                myState.myImporter.getFileNamePattern(), myState.myImporter
                        .getFileTypeDescription());
	}
	
	protected GPOptionGroup[] getOptionGroups() {
		return myState.myImporter==null ? new GPOptionGroup[0] : myState.myImporter.getSecondaryOptions();
	}

	protected void onFileChosen(File file) {
		myState.setFile(file);
	}

}
