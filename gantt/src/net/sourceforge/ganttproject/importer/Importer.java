package net.sourceforge.ganttproject.importer;

import java.io.File;

import GanttProject;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;

public interface Importer {
    String getFileTypeDescription();

    String getFileNamePattern();

    GPOptionGroup[] getSecondaryOptions();
    
    void run(GanttProject project, UIFacade facade, File selectedFile, boolean merge);
    
    String EXTENSION_POINT_ID = "net.sourceforge.ganttproject.importer";
}
