/*
 * Created on 02.04.2005
 */
package net.sourceforge.ganttproject.gui.options.model;

/**
 * @author bard
 */
public class GPOptionGroup {
    private final String myID;

    private final GPOption[] myOptions;

    private boolean isTitled = true;

    public GPOptionGroup(String id, GPOption[] options) {
        myID = id;
        myOptions = options;
    }

    public String getID() {
        return myID;
    }

    public GPOption[] getOptions() {
        return myOptions;
    }

    public GPOption getOption(String optionID) {
        assert optionID!=null;
        for (int i=0; i<myOptions.length; i++) {
            if (myOptions[i].getID().equals(optionID)) {
                return myOptions[i];
            }
        }
        return null;
    }
    
    public void lock() {
        for (int i = 0; i < myOptions.length; i++) {
            myOptions[i].lock();
        }
    }

    public void commit() {
        for (int i = 0; i < myOptions.length; i++) {
            myOptions[i].commit();
        }
    }

    public void rollback() {
        for (int i = 0; i < myOptions.length; i++) {
            myOptions[i].rollback();
        }
    }

    public boolean isTitled() {
        return isTitled;
    }

    public void setTitled(boolean isTitled) {
        this.isTitled = isTitled;
    }
}
