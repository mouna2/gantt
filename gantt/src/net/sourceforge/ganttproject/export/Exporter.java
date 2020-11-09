/*
 * Created on 02.05.2005
 */
package net.sourceforge.ganttproject.export;

import java.io.File;

import IGanttProject;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;

/**
 * @author bard
 */
public interface Exporter {
    String getFileTypeDescription();

    GPOptionGroup getOptions();

    GPOptionGroup[] getSecondaryOptions();
    
    String getFileNamePattern();

    void run(File outputFile, ExportFinalizationJob finalizationJob) throws Exception;

    // File proposeOutputFile(IGanttProject project);
    String proposeFileExtension();

    String[] getFileExtensions();

    void setContext(IGanttProject project, UIFacade uiFacade);
}
