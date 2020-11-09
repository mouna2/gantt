/*
 * Created on 01.05.2005
 */
package net.sourceforge.ganttproject.export;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import GanttOptions;
import IGanttProject;
import net.sourceforge.ganttproject.action.GPAction;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author bard
 */
public class ExportFileAction extends GPAction {
    private IGanttProject myProject;

    private UIFacade myUIFacade;

    private GanttOptions myOptions;

    public ExportFileAction(UIFacade uiFacade, IGanttProject project,
            GanttOptions options) {
        super(null, "16");
        /*putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E,
                MENU_MASK));*/
        myProject = project;
        myUIFacade = uiFacade;
        myOptions = options;
    }

    protected String getIconFilePrefix() {
        return "export_";
    }

    public void actionPerformed(ActionEvent e) {
        ExportFileWizardImpl wizard = new ExportFileWizardImpl(myUIFacade,
                myProject, myOptions);
        wizard.show();
        // myProject.export();
    }

    protected String getLocalizedName() {
        return getI18n("export");
    }
}
