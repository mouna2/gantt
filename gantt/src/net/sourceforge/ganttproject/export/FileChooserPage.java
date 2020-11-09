package net.sourceforge.ganttproject.export;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileFilter;

import IGanttProject;
import net.sourceforge.ganttproject.document.Document;
import net.sourceforge.ganttproject.export.ExportFileWizardImpl.State;
import net.sourceforge.ganttproject.filter.ExtensionBasedFileFilter;
import net.sourceforge.ganttproject.gui.FileChooserPageBase;
import net.sourceforge.ganttproject.gui.TextFieldAndFileChooserComponent;
import net.sourceforge.ganttproject.gui.options.OptionsPageBuilder;
import net.sourceforge.ganttproject.gui.options.SpringUtilities;
import net.sourceforge.ganttproject.gui.options.model.GPOption;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.gui.projectwizard.WizardPage;
import net.sourceforge.ganttproject.language.GanttLanguage;

class FileChooserPage extends FileChooserPageBase {

	private static File ourLastSelectedFile;

    private State myState;

    //private TextFieldAndFileChooserComponent myChooser;

    private IGanttProject myProject;

    //private JPanel myComponent;

    //private JComponent mySecondaryOptionsComponent;

    //private OptionsPageBuilder myOptionsBuilder;


	private GPOptionGroup myWebPublishingGroup;

    FileChooserPage(State state, IGanttProject project) {
        myState = state;
        myProject = project;
        myWebPublishingGroup = new GPOptionGroup("exporter.webPublishing", new GPOption[]{state.getPublishInWebOption()});
        myWebPublishingGroup.setTitled(false);
        //myOptionsBuilder = new OptionsPageBuilder();
    }

    protected String getFileChooserTitle() {
        return GanttLanguage.getInstance().getText("selectFileToExport");
    }


    public String getTitle() {
        return GanttLanguage.getInstance().getText("selectFileToExport");
    }


    protected void onFileChosen(File file) {
        String proposedExtension = myState.getExporter()
                .proposeFileExtension();
        if(proposedExtension != null) {
	        if (false == file.getName().toLowerCase().endsWith(
	                proposedExtension)) {
	            file = new File(file.getAbsolutePath() + "."
	                    + proposedExtension);
	        }
        }
        myState.setFile(file);
    }

    public void setActive(boolean b) {
        if (b == false) {
            myState.setFile(getSelectedFile());
            ourLastSelectedFile = getSelectedFile();
            super.setActive(b);
        } else {
        	super.setActive(b);
            File proposedFile = proposeOutputFile(myProject);
            if (proposedFile == null) {
                setSelectedFile(myState.getFile());
                return;
            }
            if (false == proposedFile.equals(getSelectedFile())) {
                setSelectedFile(proposedFile);
                myState.setFile(proposedFile);
            }
        }
    }

    public File proposeOutputFile(IGanttProject project) {
        return proposeOutputFile(project, myState.getExporter());
    }

    static File proposeOutputFile(IGanttProject project, Exporter exporter) {
        String proposedExtension = exporter.proposeFileExtension();
        if (proposedExtension == null) {
            return null;
        }
        if (ourLastSelectedFile != null) {
            String name = ourLastSelectedFile.getAbsolutePath();
            int lastDot = name.lastIndexOf('.');
            String extension = lastDot >=0 ? name.substring(lastDot + 1) : "";
            if (extension.equals(proposedExtension)) {
            	return ourLastSelectedFile;
            }
        }
        File userHome = new File(System.getProperty("user.home"));
        File result = new File(userHome, project.getProjectName() + "."
                + proposedExtension);
        Document projectDocument = project.getDocument();
        if (projectDocument != null) {
            File localFile = new File(projectDocument.getFilePath());
            if (localFile.exists()) {
                String name = localFile.getAbsolutePath();
                int lastDot = name.lastIndexOf('.');
                name = name.substring(0, lastDot) + "." + proposedExtension;
                result = new File(name);
            } else {
                File directory = localFile.getParentFile();
                if (directory.exists()) {
                    result = new File(directory, project.getProjectName() + "."
                            + proposedExtension);
                }
            }
        }
        return result;

    }

	protected FileFilter createFileFilter() {
		return new ExtensionBasedFileFilter(
                myState.getExporter().getFileNamePattern(), myState.getExporter()
                        .getFileTypeDescription());
	}

	protected GPOptionGroup[] getOptionGroups() {
		GPOptionGroup[] exporterOptions = null;
		if (myState.getExporter()!=null) {
			exporterOptions = myState.getExporter().getSecondaryOptions();
		}
		if (exporterOptions==null) {
			return new GPOptionGroup[] {myWebPublishingGroup};
		}
		GPOptionGroup[] result = new GPOptionGroup[exporterOptions.length+1];
		result[0] = myWebPublishingGroup;
		System.arraycopy(exporterOptions, 0, result, 1, exporterOptions.length);
		return result;
	}

}
