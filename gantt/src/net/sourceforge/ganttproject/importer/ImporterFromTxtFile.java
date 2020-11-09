package net.sourceforge.ganttproject.importer;

import java.io.File;

import GanttProject;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.io.GanttTXTOpen;
import net.sourceforge.ganttproject.language.GanttLanguage;

public class ImporterFromTxtFile extends ImporterBase implements Importer {

    public String getFileNamePattern() {
        return "txt";
    }

    public String getFileTypeDescription() {
        return GanttLanguage.getInstance().getText("textFiles");
    }

    public void run(GanttProject project, UIFacade uiFacade, File selectedFile, boolean merge) {
        GanttTXTOpen opener = new GanttTXTOpen(project.getTree(), project,
                project.getArea(), project.getTaskManager());
        opener.load(selectedFile);
        project.setModified();
    }

}
