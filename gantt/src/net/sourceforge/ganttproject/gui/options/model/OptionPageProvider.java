package net.sourceforge.ganttproject.gui.options.model;

import java.awt.Component;

import IGanttProject;
import net.sourceforge.ganttproject.gui.UIFacade;

public interface OptionPageProvider {
    GPOptionGroup[] getOptionGroups(IGanttProject project, UIFacade uiFacade);

    String getPageID();
    
    boolean hasCustomComponent();
    Component buildPageComponent(IGanttProject project, UIFacade uiFacade);
}
