package net.sourceforge.ganttproject.gui;

import java.awt.Component;

import javax.swing.JComponent;

public abstract class UIUtil {
    public static void setEnabledTree(JComponent root, boolean isEnabled) {
        root.setEnabled(isEnabled);
        Component[] components = root.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JComponent) {
                setEnabledTree((JComponent) components[i], isEnabled);
            }
        }
    }

}
