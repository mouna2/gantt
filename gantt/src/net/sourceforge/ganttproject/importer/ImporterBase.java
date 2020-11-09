package net.sourceforge.ganttproject.importer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import GanttProject;
import net.sourceforge.ganttproject.gui.GanttDialogInfo;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;

public class ImporterBase {
    
    private List myPreImporterProcessors = new ArrayList();
    

    public String getFileTypeDescription() {
        return null;
    }

    public String getFileNamePattern() {
        return null;
    }
    
    public GPOptionGroup[] getSecondaryOptions() {
    	return new GPOptionGroup[0];
    }
    

}
