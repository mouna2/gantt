/*
 * Created on 02.04.2005
 */
package net.sourceforge.ganttproject.gui.options;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.swing.JXDatePicker;

import net.sourceforge.ganttproject.gui.options.model.BooleanOption;
import net.sourceforge.ganttproject.gui.options.model.ChangeValueDispatcher;
import net.sourceforge.ganttproject.gui.options.model.ChangeValueEvent;
import net.sourceforge.ganttproject.gui.options.model.ChangeValueListener;
import net.sourceforge.ganttproject.gui.options.model.ColorOption;
import net.sourceforge.ganttproject.gui.options.model.DateOption;
import net.sourceforge.ganttproject.gui.options.model.EnumerationOption;
import net.sourceforge.ganttproject.gui.options.model.GPOption;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.gui.options.model.StringOption;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author bard
 */
public class OptionsPageBuilder {
    I18N myi18n = new I18N();
    private Component myParentComponent;
    
    public OptionsPageBuilder() {
        this(null);
    }
    
    public OptionsPageBuilder(Component parentComponent) {
        myParentComponent = parentComponent;
    }
    
    public void setI18N(I18N i18n) {
    	myi18n = i18n;
    }
    
    public void setOptionKeyPrefix(String optionKeyPrefix) {
        myi18n.myOptionKeyPrefix = optionKeyPrefix;
    }
    public JComponent buildPage(GPOptionGroup[] optionGroups, String pageID) {
        JPanel result = new JPanel(new BorderLayout());
        result.setBorder(new EmptyBorder(0, 5, 0, 5));
        TopPanel topPanel = new TopPanel(myi18n.getPageTitle(pageID), myi18n
                .getPageDescription(pageID));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        result.add(topPanel, BorderLayout.NORTH);
        JComponent planePage = buildPlanePage(optionGroups);
        result.add(planePage, BorderLayout.CENTER);
        return result;
    }

    public JComponent buildPlanePage(GPOptionGroup[] optionGroups) {
        final JComponent optionsPanel = new JPanel(new SpringLayout());
        for (int i = 0; i < optionGroups.length; i++) {
            optionsPanel.add(createGroupComponent(optionGroups[i]));
        }
        SpringUtilities.makeCompactGrid(optionsPanel, optionGroups.length, 1,
                0, 0, 5, 5);
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.add(optionsPanel, BorderLayout.NORTH);
        resultPanel.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                optionsPanel.getComponent(0).requestFocus();
            }
            
        });
        return resultPanel;
    }

    public JComponent createGroupComponent(GPOptionGroup group) {
        JPanel optionsPanel = new JPanel(new SpringLayout());
        if (group.isTitled()) {
            Border lineBorder = BorderFactory.createMatteBorder(1,0,0,0,Color.BLACK);
            optionsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder,myi18n
                    .getOptionGroupLabel(group)));
        }
        GPOption[] options = group.getOptions();
        for (int i = 0; i < options.length; i++) {
            GPOption nextOption = options[i];
            final Component nextComponent = createOptionComponent(nextOption);
            if (needsLabel(nextOption)) {
                Component nextLabel =createOptionLabel(options[i]);
                optionsPanel.add(nextLabel);
                optionsPanel.add(nextComponent);                
            }
            else {
                optionsPanel.add(nextComponent);
                optionsPanel.add(new JPanel());
            }
            if (i==0) {
                optionsPanel.addFocusListener(new FocusAdapter() {
                    public void focusGained(FocusEvent e) {
                        super.focusGained(e);
                        nextComponent.requestFocus();
                    }
                    
                });
            }
        }
        if (options.length > 0) {
            SpringUtilities.makeCompactGrid(optionsPanel, options.length, 2, 0,
                    0, 3, 3);
        }
        return optionsPanel;
    }

    private boolean needsLabel(GPOption nextOption) {
        return false==nextOption instanceof BooleanOption;
    }

    public Component createStandaloneOptionPanel(GPOption option) {
        JPanel optionPanel = new JPanel(new BorderLayout());
        Component  optionComponent = createOptionComponent(option); 
        if (needsLabel(option)) {
            optionPanel.add(createOptionLabel(option), BorderLayout.WEST);
            optionPanel.add(optionComponent, BorderLayout.CENTER);
        }
        else {
            optionPanel.add(optionComponent, BorderLayout.WEST);
        }
        JPanel result = new JPanel(new BorderLayout());
        result.add(optionPanel, BorderLayout.NORTH);
        return result;
    }
    
    private Component createOptionLabel(GPOption option) {
        JLabel nextLabel = new JLabel(myi18n.getOptionLabel(option));
        nextLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
        return nextLabel;
    }
    private Component createOptionComponent(GPOption option) {
        Component result = null;
        if (option instanceof EnumerationOption) {
            result = createEnumerationComponent((EnumerationOption) option);
        } else if (option instanceof BooleanOption) {
            result = createBooleanComponent((BooleanOption) option);
        }
        else if (option instanceof ColorOption) {
            result = createColorComponent((ColorOption)option);
        }
        else if (option instanceof DateOption) {
            result = createDateComponent((DateOption)option);
        }
        else if (option instanceof GPOptionGroup) {
            result = createButtonComponent((GPOptionGroup)option);
        }
        else if (option instanceof StringOption) {
            result = createStringComponent((StringOption)option);
        }
        if (result == null) {
            result = new JLabel("Unknown option class=" + option.getClass());
        }
        return result;
    }

    private Component createStringComponent(final StringOption option) {
        final JTextField result = new JTextField(option.getValue());
        result.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                option.setValue(result.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                option.setValue(result.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                option.setValue(result.getText());
            }
        });
        return result;
    }

    private Component createButtonComponent(GPOptionGroup optionGroup) {
        Action action = new AbstractAction(myi18n.getAdvancedActionTitle()) {
            public void actionPerformed(ActionEvent e) {
                System.err
                        .println("[OptionsPageBuilder] createButtonComponent: ");
            }
            
        };
        JButton result = new JButton(action);
        return result;
    }
    private Component createBooleanComponent(BooleanOption option) {
        JCheckBox result = new JCheckBox(new BooleanOptionAction(option));
        result.setText(myi18n.getOptionLabel(option));
        result.setHorizontalAlignment(JCheckBox.LEFT);
        result.setHorizontalTextPosition(SwingConstants.TRAILING);
        result.setSelected(option.isChecked());
        ComponentOrientation componentOrientation = GanttLanguage.getInstance().getComponentOrientation();
        result.setComponentOrientation(componentOrientation);
        return result;
    }

    private JComboBox createEnumerationComponent(EnumerationOption option) {
        ComboBoxModel model = new EnumerationOptionComboBoxModel(option);
        JComboBox result = new JComboBox(model);
        return result;
    }

    private Component createColorComponent(final ColorOption option) {
        final JButton colorButton = new JButton();
        Action action = new AbstractAction(myi18n.getColorButtonText(option)) {
            public void actionPerformed(ActionEvent e) {
                ActionListener onOkPressing = new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Color color = ourColorChooser.getColor();
                        colorButton.setBackground(color);
                        option.setValue(color);
                    }
                };
                ActionListener onCancelPressing = new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // nothing to do for "Cancel"
                    }
                };
                JDialog dialog = JColorChooser.createDialog(myParentComponent,
                        myi18n.getColorChooserTitle(option), true,
                        ourColorChooser, onOkPressing, onCancelPressing);
                ourColorChooser.setColor(colorButton.getBackground());
                dialog.setVisible(true);
            };
        };
        colorButton.setAction(action);
        colorButton.setBackground(option.getValue());
        return colorButton;
    }
    
    private Component createDateComponent(final DateOption option) {
        final JXDatePicker result = new JXDatePicker();
        result.setDate(option.getValue());
        result.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                option.setValue(((JXDatePicker)e.getSource()).getDate());
            }
        });
        if (option instanceof ChangeValueDispatcher) {
            ((ChangeValueDispatcher)option).addChangeValueListener(new ChangeValueListener() {
                public void changeValue(ChangeValueEvent event) {
                    assert event.getNewValue() instanceof Date;
                    result.setDate((Date) event.getNewValue());
                }
            });
        }
        
        return result;
    }
    
    public static class I18N {
        private String myOptionKeyPrefix = "option.";
        private String myOptionGroupKeyPrefix = "optionGroup.";
        private String myOptionPageKeyPrefix = "optionPage.";
    	
    	public I18N() {
    	}
    	
    	protected String getValue(String key) {
    		return GanttLanguage.getInstance().getText(key);
    	}
        public String getPageTitle(String pageID) {
            return GanttLanguage.getInstance().getText(
                    myOptionPageKeyPrefix + pageID + ".title");
        }

        public String getPageDescription(String pageID) {
            return GanttLanguage.getInstance().getText(
                    myOptionPageKeyPrefix + pageID + ".description");
        }

        public String getOptionGroupLabel(GPOptionGroup group) {
            return GanttLanguage.getInstance().getText(
                    myOptionGroupKeyPrefix + group.getID() + ".label");
        }

        public String getOptionLabel(GPOption option) {
            return GanttLanguage.getInstance().getText(
                    myOptionKeyPrefix + option.getID() + ".label");
        }

        String getAdvancedActionTitle() {
            return GanttLanguage.getInstance().getText("optionAdvanced.label");
        }
        
        String getColorButtonText(ColorOption colorOption) {
            return GanttLanguage.getInstance().getText("colorButton");
        }
        
        String getColorChooserTitle(ColorOption colorOption) {
            return GanttLanguage.getInstance().getText("selectColor");
        }
    }
    
    private static JColorChooser ourColorChooser = new JColorChooser();
    
    static {
        ImageIcon calendarImage = new ImageIcon(OptionsPageBuilder.class.getResource(
        "/icons/calendar_16.gif"));        
        Icon nextMonth = new ImageIcon(OptionsPageBuilder.class
                .getResource("/icons/nextmonth.gif"));
        Icon prevMonth = new ImageIcon(OptionsPageBuilder.class
                .getResource("/icons/prevmonth.gif"));
        UIManager.put("JXDatePicker.arrowDown.image", calendarImage);
        UIManager.put("JXMonthView.monthUp.image", prevMonth);
        UIManager.put("JXMonthView.monthDown.image", nextMonth);
        UIManager.put("JXMonthView.monthCurrent.image", calendarImage);        
    }
}
