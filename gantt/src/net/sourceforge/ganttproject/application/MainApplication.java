/*
 * Created on 25.04.2005
 */
package net.sourceforge.ganttproject.application;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.eclipse.core.runtime.IPlatformRunnable;

import GPLogger;
import GanttProject;

/**
 * @author bard
 */
public class MainApplication implements IPlatformRunnable {
    private Object myLock = new Object();

    // The hack with waiting is necessary because when you
    // launch Runtime Workbench in Eclipse, it exists as soon as
    // GanttProject.main() method exits
    // without Eclipse, Swing thread continues execution. So we wait until main
    // window closes
    public Object run(Object args) throws Exception {
        Thread.currentThread().setContextClassLoader(
                getClass().getClassLoader());
        GPLogger.setup();
        String[] cmdLine = (String[]) args;
        WindowAdapter closingListener = new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                myLock.notify();
            }
        };
        GanttProject.setWindowListener(closingListener);
        GanttProject.main(cmdLine);
        synchronized (myLock) {
            myLock.wait();
        }
        return null;
    }

}
