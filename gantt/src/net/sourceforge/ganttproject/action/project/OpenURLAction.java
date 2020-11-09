/*
 * Created on 07.10.2005
 */
package net.sourceforge.ganttproject.action.project;

import java.awt.event.ActionEvent;

import GanttProject;
import net.sourceforge.ganttproject.action.GPAction;

public class OpenURLAction extends GPAction {
    private final GanttProject myMainFrame;
    
    OpenURLAction(GanttProject mainFrame) {
        myMainFrame = mainFrame;
    }
    protected String getIconFilePrefix() {
        return null;
    }

    public void actionPerformed(ActionEvent e) {
        if (myMainFrame.checkCurrentProject()) {
            myMainFrame.openURL();
        }
    }

    protected String getLocalizedName() {
        return getI18n("openFromServer");
    }

    protected String getTooltipText() {
        return getLocalizedName();
    }

    
}
