package net.sourceforge.ganttproject.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileFilter;


import net.sourceforge.ganttproject.gui.options.OptionsPageBuilder;
import net.sourceforge.ganttproject.gui.options.SpringUtilities;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.gui.projectwizard.WizardPage;
import net.sourceforge.ganttproject.language.GanttLanguage;

public abstract class FileChooserPageBase implements WizardPage {
    private JPanel myComponent;
	private TextFieldAndFileChooserComponent myChooser;
	private OptionsPageBuilder myOptionsBuilder;
	private JPanel mySecondaryOptionsComponent;
	
	protected FileChooserPageBase() {
		myOptionsBuilder = new OptionsPageBuilder();
        mySecondaryOptionsComponent = new JPanel(new BorderLayout()); 
	}
    
    protected abstract String getFileChooserTitle();
    
	public Component getComponent() {
        myComponent = new JPanel(new BorderLayout());
        myChooser = new TextFieldAndFileChooserComponent(GanttLanguage.getInstance().getText("file")+":",
        		getFileChooserTitle()) {
            protected void onFileChosen(File file) {
            	FileChooserPageBase.this.onFileChosen(file);
            	super.onFileChosen(file);
            }
        };
        JComponent contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(myChooser.getComponent(), BorderLayout.NORTH);
        contentPanel.add(mySecondaryOptionsComponent, BorderLayout.CENTER);
//        SpringUtilities.makeCompactGrid(contentPanel, 2, 1,
//                0, 0, 5, 5);
        myComponent.add(contentPanel, BorderLayout.NORTH);
        return myComponent;
    }

    public void setActive(boolean b) {
    	GPOptionGroup[] optionGroups = getOptionGroups();
        if (b == false) {
        	for (int i=0; i<optionGroups.length; i++) {
        		optionGroups[i].commit();
        	}
        } else {
        	for (int i=0; i<optionGroups.length; i++) {
        		optionGroups[i].lock();
        	}
        	if (mySecondaryOptionsComponent!=null){
                mySecondaryOptionsComponent.removeAll();
            }        	
        	//mySecondaryOptionsComponent= new JPanel(new BorderLayout());
            mySecondaryOptionsComponent.add(myOptionsBuilder.buildPlanePage(optionGroups), BorderLayout.NORTH); 
            //myComponent.add(mySecondaryOptionsComponent, BorderLayout.CENTER);
            mySecondaryOptionsComponent.invalidate();
            myComponent.invalidate();
            myChooser.setFileFilter(createFileFilter());
        }
    }
    

	protected void setSelectedFile(File file) {
		myChooser.setFile(file);
	}

	protected File getSelectedFile() {
		return myChooser.getFile();
	}
	
	protected void setOptionsEnabled(boolean enabled) {
		if (mySecondaryOptionsComponent!=null) {
			setEnabledTree(mySecondaryOptionsComponent, enabled);
		}
	}
	
    private void setEnabledTree(JComponent root, boolean isEnabled) {
    	UIUtil.setEnabledTree(root, isEnabled);
    }
	
    
	protected abstract FileFilter createFileFilter();

	protected abstract GPOptionGroup[] getOptionGroups();

	protected abstract void onFileChosen(File file);

}
