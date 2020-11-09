package net.sourceforge.ganttproject.gui;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: Provide the properties of selected task</p>
 *
 * <p>Copyright: Copyright (c) 2003</p>
 *
 * <p>Company: </p>
 *
 * @author ganttproject
 *
 * @version 1.0
 *
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swing.JXDatePicker;

import GanttCalendar;
import GanttGraphicArea;
import GanttProject;
import GanttTask;
import GanttTree2;
import IGanttProject;
import Mediator;
import net.sourceforge.ganttproject.gui.options.SpringUtilities;
import net.sourceforge.ganttproject.gui.taskproperties.CustomColumnsPanel;
import net.sourceforge.ganttproject.gui.taskproperties.TaskAllocationsPanel;
import net.sourceforge.ganttproject.gui.taskproperties.TaskDependenciesPanel;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import net.sourceforge.ganttproject.roles.RoleManager;
import net.sourceforge.ganttproject.shape.JPaintCombo;
import net.sourceforge.ganttproject.shape.ShapeConstants;
import net.sourceforge.ganttproject.shape.ShapePaint;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskContainmentHierarchyFacade;
import net.sourceforge.ganttproject.task.TaskImpl;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.TaskMutator;
import net.sourceforge.ganttproject.util.BrowserControl;

/**
 * Real panel for editing task properties
 */
public class GanttTaskPropertiesBean

extends JPanel {

    private JXDatePicker myStartDatePicker;
    private JXDatePicker myEndDatePicker;
    private JXDatePicker myThirdDatePicker;
    // Input attributes

    // protected GanttTask selectedTask; //Task whose properties will be shown

    protected GanttTask[] selectedTasks;

    //private GanttTree2 tree; // GanttTree that contain all the tasks information

    private GanttLanguage language = GanttLanguage.getInstance(); // language

    // Output attributes: you can find the definition is GanttTask

    // private String name;

    private int length;

    private int percentComplete;

    private int priority;

    private GanttCalendar start;

    private GanttCalendar end;

    private GanttCalendar third;

    private boolean bilan;

	private boolean isProjectTask;

    private String notes;

    // private GanttTask selectedTaskClone;

    // private Hashtable managerHash;
    //
    // private Hashtable assignedResources = new Hashtable();
    // private attributes for internal use

    GridBagConstraints gbc = new GridBagConstraints();

    FlowLayout flowL = new FlowLayout(FlowLayout.LEFT, 10, 10);

    JTabbedPane tabbedPane; // TabbedPane that include the following four items

    JPanel generalPanel;

    JPanel predecessorsPanel;

    JPanel resourcesPanel;

    JPanel notesPanel;

    // Components on general pannel

    JPanel firstRowPanel1; // components in first row

    JTextField nameField1;

    JTextField durationField1;

    JLabel nameLabel1;

    JLabel durationLabel1;

    JLabel lblWebLink;

    JTextField tfWebLink;

    JButton bWebLink;

    JPanel secondRowPanel1; // components in second row

    JSpinner percentCompleteSlider;

    JLabel percentCompleteLabel1;

    JLabel priorityLabel1;

    JComboBox priorityComboBox;

    JPanel thirdRowPanel1; // componets in third row

    JTextField startDateField1;

    JTextField finishDateField1;

    JTextField thirdDateField1;

    JLabel startDateLabel1;

    JLabel finishDateLabel1;

    JComboBox thirdDateComboBox;

    JButton startDateButton1;

    JButton finishDateButton1;

    JButton thirdDateButton1;

    JPanel lastRowPanel1; // components in last row

    JPanel webLinkPanel; // components in web link panel

    JLabel mileStoneLabel1;

    JCheckBox mileStoneCheckBox1;

    JCheckBox projectTaskCheckBox1;

    JButton colorButton;


    boolean isColorChanged;

    JButton colorSpace;

    JPanel colorPanel;

    /** Shape chooser combo Box */
    JPaintCombo shapeComboBox;

    // Components on predecessors pannel

    JLabel nameLabel2; // first row, here the textfield is un-editable

    JLabel durationLabel2;

    JTextField nameField2;

    JPanel firstRowPanel2;

    JScrollPane predecessorsScrollPane; // second row, a table

    JLabel nameLabelNotes;

    JLabel durationLabelNotes;

    JTextField nameFieldNotes;

    JTextField durationFieldNotes;

    JScrollPane scrollPaneNotes;

    JTextArea noteAreaNotes;

    JPanel firstRowPanelNotes;

    JPanel secondRowPanelNotes;

    // Component on the SOUTH ok CANCEL buttons

    //public JButton okButton;

    //JButton cancelButton;

    //JPanel southPanel;

    private boolean onlyOneTask = false;

    private String taskWebLink;

    private boolean taskIsMilestone;

    private GanttCalendar taskStartDate;

    private GanttCalendar taskThirdDate;

    private int taskThirdDateConstraint;

	private boolean taskIsProjectTask;

    private int taskLength;

    private String taskNotes;

    private int taskCompletionPercentage;

    private int taskPriority;

    private ShapePaint taskShape;

    private CustomColumnsPanel myCustomColumnPanel = null;

    // private ResourcesTableModel myResourcesTableModel;
    private TaskDependenciesPanel myDependenciesPanel;

    private TaskAllocationsPanel[] myAllocationsPanel;

    //private boolean isStartFixed;

//    private boolean isFinishFixed;

    private final HumanResourceManager myHumanResourceManager;

    private final RoleManager myRoleManager;

    private Task myUnpluggedClone;
	private TaskManager myTaskManager;
	private IGanttProject myProject;
	private UIFacade myUIfacade;

    /** add a component to container by using GridBagConstraints. */

    private void addUsingGBL(Container container, Component component,

    GridBagConstraints gbc, int x,

    int y, int w, int h) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        gbc.weighty = 0;
        container.add(component, gbc);
    }

    /** set the first row in all the tabbed pane. thus give them a common look */

    private void setFirstRow(Container container, GridBagConstraints gbc,
            JLabel nameLabel, JTextField nameField, JLabel durationLabel,
            JTextField durationField) {
        container.setLayout(new GridBagLayout());
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets.right = 15;
        gbc.insets.left = 10;
        gbc.insets.top = 10;
        addUsingGBL(container, nameLabel, gbc, 0, 0, 1, 1);
        addUsingGBL(container, nameField, gbc, 1, 0, 1, 1);
        addUsingGBL(container, durationLabel, gbc, 2, 0, 1, 1);
        gbc.weightx = 1;
        addUsingGBL(container, durationField, gbc, 3, 0, 1, 1);
    }

    /** Construct the general panel */
    private void constructGeneralPanel() {
        generalPanel = new JPanel(new GridBagLayout());
        // first row
        nameLabel1 = new JLabel(language.getText("name") + ":");
        nameField1 = new JTextField(20);
        nameField1.setName("name_of_task");

        if (!onlyOneTask) {
            nameLabel1.setVisible(false);
            nameField1.setVisible(false);
        }

        durationLabel1 = new JLabel(language.getText("length") + ":");
        durationField1 = new JTextField(8);
        durationField1.setName("length");
        firstRowPanel1 = new JPanel(flowL);
        setFirstRow(firstRowPanel1, gbc, nameLabel1, nameField1,
                durationLabel1, durationField1);
        // second row
        percentCompleteLabel1 = new JLabel(language.getText("advancement")); // Progress
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
        percentCompleteSlider = new JSpinner(spinnerModel);

        secondRowPanel1 = new JPanel(flowL);
        secondRowPanel1.add(percentCompleteLabel1);
        // secondRowPanel1.add(percentCompleteField1);
        secondRowPanel1.add(percentCompleteSlider);
        priorityLabel1 = new JLabel(language.getText("priority"));
        secondRowPanel1.add(priorityLabel1);
        priorityComboBox = new JComboBox();
        priorityComboBox.addItem(language.getText("low"));
        priorityComboBox.addItem(language.getText("normal"));
        priorityComboBox.addItem(language.getText("hight"));
        priorityComboBox.setEditable(false);

        secondRowPanel1.add(priorityComboBox);

        // third row

        startDateLabel1 = new JLabel(language.getText("dateOfBegining") + ":");
        startDateField1 = new JTextField(12);
        startDateField1.setEditable(false);
        finishDateLabel1 = new JLabel(language.getText("dateOfEnd") + ":");

        finishDateField1 = new JTextField(12);
        finishDateField1.setEditable(false);
        thirdDateComboBox = new JComboBox();
        thirdDateComboBox.addItem("");
        thirdDateComboBox.addItem(language.getText("earliestBegin"));
        thirdDateComboBox.setName("third");
        thirdDateComboBox.setSelectedIndex(selectedTasks[0]
                .getThirdDateConstraint());
        thirdDateField1 = new JTextField(12);
        thirdDateField1.setEditable(false);

        ImageIcon icon = new ImageIcon(getClass().getResource(
                "/icons/calendar_16.gif"));

        startDateButton1 = new TestGanttRolloverButton(icon);
        startDateButton1.setName("start");
        startDateButton1.setToolTipText(GanttProject.getToolTip(language
                .getText("chooseDate")));
        finishDateButton1 = new TestGanttRolloverButton(icon);
        finishDateButton1.setName("finish");
        finishDateButton1.setToolTipText(GanttProject.getToolTip(language
                .getText("chooseDate")));
        thirdDateButton1 = new TestGanttRolloverButton(icon);
        thirdDateButton1.setName("third");
        thirdDateButton1.setToolTipText(GanttProject.getToolTip(language
                .getText("chooseDate")));

        if (selectedTasks[0].getThirdDateConstraint() == 0)
            thirdDateButton1.setEnabled(false);


        thirdRowPanel1 = new JPanel(flowL);
        thirdRowPanel1.setBorder(new TitledBorder(new EtchedBorder(), language
                .getText("date")));

        JPanel startDatePanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 5, 0));
        startDatePanel.add(startDateLabel1);
        myStartDatePicker = createDatePicker(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setStart(new GanttCalendar(((JXDatePicker)e.getSource()).getDate()), false);
            }
        });
        startDatePanel.add(myStartDatePicker);
        //startDatePanel.add(startDateButton1);


        JPanel finishDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5,
                0));
        finishDatePanel.add(finishDateLabel1);
        myEndDatePicker = createDatePicker(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setEnd(new GanttCalendar(((JXDatePicker)e.getSource()).getDate()), false);
            }
        });
        finishDatePanel.add(myEndDatePicker);
        //finishDatePanel.add(finishDateButton1);

        JPanel thirdDatePanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 5, 0));
        thirdDatePanel.add(thirdDateComboBox);
        myThirdDatePicker = createDatePicker(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setThird(new GanttCalendar(((JXDatePicker)e.getSource()).getDate()), false);
            }
        });
        thirdDatePanel.add(myThirdDatePicker);
        //thirdDatePanel.add(thirdDateButton1);

        // thirdRowPanel1.add(startDatePanel);
        // thirdRowPanel1.add(finishDatePanel);
        // thirdRowPanel1.add(thirdDatePanel);
        addUsingGBL(thirdRowPanel1, startDatePanel, gbc, 0, 0, 1, 1);
        addUsingGBL(thirdRowPanel1, finishDatePanel, gbc, 0, 1, 1, 1);
        addUsingGBL(thirdRowPanel1, thirdDatePanel, gbc, 0, 2, 1, 1);

        // fourth row
		JCheckBox checkBox = constructCheckBox ();
        lastRowPanel1 = new JPanel(flowL);
		if (checkBox != null)
			lastRowPanel1.add(checkBox);


        JPanel shapePanel = new JPanel();
        shapePanel.setLayout(new BorderLayout());
        JLabel lshape = new JLabel("  " + language.getText("shape") + " ");
        shapeComboBox = new JPaintCombo(ShapeConstants.PATTERN_LIST);

        shapePanel.add(lshape, BorderLayout.WEST);
        shapePanel.add(shapeComboBox, BorderLayout.CENTER);

        colorButton = new JButton(language.getText("colorButton"));
        colorButton.setBackground(selectedTasks[0].getColor());
        final String colorChooserTitle = language.getText("selectColor");
        colorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog dialog;
                dialog = JColorChooser.createDialog(GanttTaskPropertiesBean.this, colorChooserTitle,
                        true, GanttDialogProperties.colorChooser,
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                colorButton
                                        .setBackground(GanttDialogProperties.colorChooser
                                                .getColor());
                                isColorChanged = true;
                            }
                        }

                        , new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                // nothing to do for "Cancel"
                            }
                        });

                /*
                 * AbstractColorChooserPanel[] panels =
                 * GanttDialogProperties.colorChooser.getChooserPanels();
                 * GanttDialogProperties.colorChooser.removeChooserPanel(panels[0]);
                 * GanttDialogProperties.colorChooser.addChooserPanel(panels[0]);
                 */

                GanttDialogProperties.colorChooser.setColor(colorButton
                        .getBackground());
                dialog.show();
            }
        });

        colorSpace = new JButton(language.getText("defaultColor"));
        colorSpace.setBackground(GanttGraphicArea.taskDefaultColor);
        colorSpace.setToolTipText(GanttProject.getToolTip(language
                .getText("resetColor")));
        colorSpace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colorButton.setBackground(GanttGraphicArea.taskDefaultColor);
                isColorChanged = true;
            }
        });

        colorPanel = new JPanel();
        colorPanel.setLayout(new BorderLayout());
        colorPanel.add(colorButton, "West");
        colorPanel.add(colorSpace, "Center");
        colorPanel.add(shapePanel, BorderLayout.EAST);
        lastRowPanel1.add(colorPanel);

        // ---Set GridBagConstraints constant
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets.right = 15;
        gbc.insets.left = 10;
        gbc.insets.top = 10;
        addUsingGBL(generalPanel, firstRowPanel1, gbc, 0, 0, 1, 1);
        addUsingGBL(generalPanel, secondRowPanel1, gbc, 0, 1, 1, 1);
        addUsingGBL(generalPanel, thirdRowPanel1, gbc, 0, 2, 1, 1);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weighty = 1;
        generalPanel.add(lastRowPanel1, gbc);

        // The panel for the web link
        webLinkPanel = new JPanel(flowL);
        lblWebLink = new JLabel(language.getText("webLink"));
        webLinkPanel.add(lblWebLink);
        tfWebLink = new JTextField(30);
        webLinkPanel.add(tfWebLink);
        bWebLink = new TestGanttRolloverButton(new ImageIcon(getClass()
                .getResource("/icons/web_16.gif")));
        bWebLink.setToolTipText(GanttProject.getToolTip(language
                .getText("openWebLink")));
        webLinkPanel.add(bWebLink);

        bWebLink.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // link to open the web link
                try {
                    if (!BrowserControl.displayURL(tfWebLink.getText())) {
                        GanttDialogInfo gdi = new GanttDialogInfo(null,
                                GanttDialogInfo.ERROR,
                                GanttDialogInfo.YES_OPTION, language
                                        .getText("msg4"), language
                                        .getText("error"));
                        gdi.show();
                    }
                } catch (Exception ex) {
                }
            }
        });

        gbc.gridy = 4;
        generalPanel.add(webLinkPanel, gbc);

    }

    /** Add the differents action listener on the differents widgets */
    public void addActionListener(ActionListener l) {

        nameField1.addActionListener(l);

        startDateButton1.addActionListener(l);

        finishDateButton1.addActionListener(l);

        thirdDateButton1.addActionListener(l);

        thirdDateComboBox.addActionListener(l);

        durationField1.addActionListener(l);

    }

    /** Change the name of the task on all textfiled of task name */
    public void changeNameOfTask() {
        if (nameField1 != null && nameFieldNotes != null) {
            String nameOfTask = nameField1.getText().trim();
            nameField1.setText(nameOfTask);
            if (onlyOneTask)
                myDependenciesPanel.nameChanged(nameOfTask);
            myAllocationsPanel[0].nameChanged(nameOfTask);
            nameFieldNotes.setText(nameOfTask);
        }
    }

    private void constructCustomColumnPanel(IGanttProject project) {
        myCustomColumnPanel = new CustomColumnsPanel(
        		project.getTaskCustomColumnManager(),
        		project.getCustomColumnsStorage(), myUIfacade);
    }

    /** Construct the predecessors tabbed pane */

    private void constructPredecessorsPanel() {
        myDependenciesPanel = new TaskDependenciesPanel(selectedTasks[0]);
        predecessorsPanel = myDependenciesPanel.getComponent();
    }

    /** Construct the resources panel */

    private void constructResourcesPanel() {
        myAllocationsPanel = new TaskAllocationsPanel[selectedTasks.length];
        for (int i = 0; i < myAllocationsPanel.length; i++) {
            myAllocationsPanel[i] = new TaskAllocationsPanel(selectedTasks[i],
                    myHumanResourceManager, myRoleManager, onlyOneTask);
            if (i != 0)
                myAllocationsPanel[i].getComponent();
        }
        resourcesPanel = myAllocationsPanel[0].getComponent();
    }

    /** construct the notes pannel */

    private void constructNotesPanel() {

        notesPanel = new JPanel(new GridBagLayout());

        // first row

        nameLabelNotes = new JLabel(language.getText("name") + ":");

        nameFieldNotes = new JTextField(20);

        if (!onlyOneTask) {
            nameLabelNotes.setVisible(false);
            nameFieldNotes.setVisible(false);
        }

        durationLabelNotes = new JLabel(language.getText("length") + ":");

        durationFieldNotes = new JTextField(8);

        nameFieldNotes.setEditable(false);

        durationFieldNotes.setEditable(false);

        firstRowPanelNotes = new JPanel();

        setFirstRow(firstRowPanelNotes, gbc, nameLabelNotes, nameFieldNotes,

        durationLabelNotes, durationFieldNotes);

        secondRowPanelNotes = new JPanel();

        secondRowPanelNotes.setBorder(new TitledBorder(new EtchedBorder(),
                language.getText("notesTask") + ":"));

        noteAreaNotes = new JTextArea(8, 40);
        noteAreaNotes.setLineWrap(true);
        noteAreaNotes.setWrapStyleWord(true);
        noteAreaNotes.setBackground(new Color(1.0f, 1.0f, 1.0f));

        scrollPaneNotes = new JScrollPane(noteAreaNotes);

        secondRowPanelNotes.add(scrollPaneNotes);

        JButton bdate = new TestGanttRolloverButton(new ImageIcon(getClass()
                .getResource("/icons/clock_16.gif")));
        bdate.setToolTipText(GanttProject.getToolTip(language
                .getText("putDate")));
        bdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                noteAreaNotes.append("\n" + GanttCalendar.getDateAndTime()
                        + "\n");
            }
        });
        secondRowPanelNotes.add(bdate);

        gbc.anchor = GridBagConstraints.WEST;

        gbc.insets.right = 15;

        gbc.insets.left = 10;

        gbc.insets.top = 10;

        gbc.weighty = 0;

        addUsingGBL(notesPanel, firstRowPanelNotes, gbc, 0, 0, 1, 1);

        gbc.weighty = 1;

        gbc.gridx = 0;

        gbc.gridy = 1;

        gbc.gridwidth = 1;

        gbc.gridheight = 1;

        notesPanel.add(secondRowPanelNotes, gbc);

    }

    /** Constructor */

    public GanttTaskPropertiesBean(GanttTask[] selectedTasks, IGanttProject project, UIFacade uifacade) {
        this.onlyOneTask = false;
        if (selectedTasks.length == 1)
            this.onlyOneTask = true;
        this.selectedTasks = selectedTasks;
        setInitialValues(selectedTasks[0]);
        myHumanResourceManager = (HumanResourceManager) project.getHumanResourceManager();
        myRoleManager = project.getRoleManager();
        myTaskManager = project.getTaskManager();
        myProject = project;
        myUIfacade = uifacade;
//		setTree(tree);
		init();

        // this.managerHash = managerHash;

        setSelectedTask();
    }

    private JXDatePicker createDatePicker(ActionListener listener) {
        ImageIcon calendarImage = new ImageIcon(getClass().getResource(
        "/icons/calendar_16.gif"));
        Icon nextMonth = new ImageIcon(getClass()
                .getResource("/icons/nextmonth.gif"));
        Icon prevMonth = new ImageIcon(getClass()
                .getResource("/icons/prevmonth.gif"));
        UIManager.put("JXDatePicker.arrowDown.image", calendarImage);
        UIManager.put("JXMonthView.monthUp.image", prevMonth);
        UIManager.put("JXMonthView.monthDown.image", nextMonth);
        UIManager.put("JXMonthView.monthCurrent.image", calendarImage);
        JXDatePicker result = new JXDatePicker();
        result.addActionListener(listener);
        return result;
    }
    /** Init the widgets */
    public void init() {

        tabbedPane = new JTabbedPane();
        tabbedPane.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                changeNameOfTask();
                fireDurationChanged();
            }
        });
        constructGeneralPanel();

        tabbedPane.addTab(language.getText("general"), new ImageIcon(getClass()
                .getResource("/icons/properties_16.gif")), generalPanel);

        if (onlyOneTask) {
            constructPredecessorsPanel();
            tabbedPane.addTab(language.getText("predecessors"), new ImageIcon(
                    getClass().getResource("/icons/relashion.gif")),
                    predecessorsPanel);
        }

        constructResourcesPanel();

        tabbedPane.addTab(GanttProject.correctLabel(language.getText("human")),
                new ImageIcon(getClass().getResource("/icons/res_16.gif")),
                resourcesPanel);

        constructNotesPanel();

        tabbedPane.addTab(language.getText("notesTask"), new ImageIcon(
                getClass().getResource("/icons/note_16.gif")), notesPanel);

        setLayout(new BorderLayout());

        add(tabbedPane, BorderLayout.CENTER);

        constructCustomColumnPanel(myProject);
        tabbedPane.addTab(language.getText("customColumns"), new ImageIcon(
                getClass().getResource("/icons/custom.gif")),
                myCustomColumnPanel);
        tabbedPane.addFocusListener(new FocusAdapter() {
            private boolean isFirstFocusGain = true;
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (isFirstFocusGain) {
                    nameField1.requestFocus();
                    isFirstFocusGain = false;
                }
            }
        });
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(2,0,5,0));
    }

    // Input methods

    /**
     * as the name indicated, it will not replace the original GanttTask in the
     * Tree.
     */

    public Task[] getReturnTask() {
        GanttTask[] returnTask = new GanttTask[selectedTasks.length];

        for (int i = 0; i < selectedTasks.length; i++) {
            if (myAllocationsPanel[0].getTableModel().isChanged()) {
                if (i != 0)
                    copyValues(myAllocationsPanel[0].getTableModel(),
                            myAllocationsPanel[i].getTableModel());

            }
            myAllocationsPanel[i].getTableModel().commit();
            returnTask[i] = selectedTasks[i];

            // returnTask.setTaskID(selectedTask.getTaskID());
            TaskMutator mutator = returnTask[i].createMutator();

            if (onlyOneTask) {
                mutator.setName(getTaskName()); // getName()
				mutator.setProjectTask (false);
            }
            if (this.taskWebLink != null && !this.taskWebLink.equals(getWebLink()))
                returnTask[i].setWebLink(getWebLink()); // getName()
			if (mileStoneCheckBox1 != null) {
				if (this.taskIsMilestone != isBilan())
	                mutator.setMilestone(isBilan());
			}
            else if (projectTaskCheckBox1 != null) {
				if (this.taskIsProjectTask != isProjectTask())
	                mutator.setProjectTask(isProjectTask());
            }
            if (!this.taskStartDate.equals(getStart()))
                mutator.setStart(getStart());
            if (((this.taskThirdDate == null) && (getThird() != null))
                    || ((this.taskThirdDate != null) && (getThird() == null))
                    || ((this.taskThirdDate != null) && (!this.taskThirdDate
                            .equals(getThird()))))
                mutator.setThird(getThird(), getThirdDateConstraint());

            if (getLength() > 0) {
                    mutator.setDuration(returnTask[i].getManager()
                            .createLength(getLength()));
            }
            if (!this.taskNotes.equals(getNotes()))
                returnTask[i].setNotes(getNotes());
            if (this.taskCompletionPercentage != getPercentComplete())
                mutator.setCompletionPercentage(getPercentComplete());
            if (this.taskPriority != getPriority())
                returnTask[i].setPriority(getPriority());
//            if (this.taskIsStartFixed != isStartFixed)
//                returnTask[i].setStartFixed(isStartFixed);
//            if (this.taskIsFinishFixed != isFinishFixed)
//                returnTask[i].setFinishFixed(isFinishFixed);
            if (isColorChanged)
                returnTask[i].setColor(colorButton.getBackground());
            if ((((this.taskShape == null) && (shapeComboBox.getSelectedIndex() != 0)))
                    || ((this.taskShape != null) && (!this.taskShape
                            .equals((ShapePaint) shapeComboBox
                                    .getSelectedPaint()))))
                returnTask[i].setShape(new ShapePaint(
                        (ShapePaint) shapeComboBox.getSelectedPaint(),
                        Color.white, returnTask[i].getColor()));
            if (returnTask[i].getShape() != null)
                returnTask[i].setShape(new ShapePaint(returnTask[i].getShape(),
                        Color.white, returnTask[i].getColor()));

            mutator.commit();
            if (onlyOneTask) {
                myDependenciesPanel.getTableModel().commit();
            }
            returnTask[i].applyThirdDateConstraint();
        }

        return returnTask;

    }

    /** as the name indicated */

    public void setSelectedTask() {

        // this.selectedTask = selectedTask;

        nameField1.setText(selectedTasks[0].getName());

        // nameField2.setText(selectedTask.toString());

        nameFieldNotes.setText(selectedTasks[0].toString());

        setName(selectedTasks[0].toString());

        durationField1.setText(selectedTasks[0].getLength() + "");

        // durationField2.setText(selectedTask.getLength() + "");

        durationFieldNotes.setText(selectedTasks[0].getLength() + "");

        percentCompleteSlider.setValue(new Integer(selectedTasks[0]
                .getCompletionPercentage()));
        percentCompleteLabel1.setText(language.getText("advancement"));

        priorityComboBox.setSelectedIndex(selectedTasks[0].getPriority());

        startDateField1.setText(selectedTasks[0].getStart().toString());

        finishDateField1.setText(selectedTasks[0].getEnd().toString());

        if (selectedTasks[0].isMilestone()) {
        	enableMilestoneFriendlyControls(false);
        }
        if (selectedTasks[0].getThird() != null) {
            thirdDateField1.setText(selectedTasks[0].getThird().toString());
            setThird(selectedTasks[0].getThird().Clone(), true);
        }

        setStart(selectedTasks[0].getStart().Clone(), true);

        setEnd(selectedTasks[0].getEnd().Clone(), true);

        bilan = selectedTasks[0].isMilestone();

		isProjectTask = selectedTasks[0].isProjectTask();

		if (mileStoneCheckBox1 != null)
			mileStoneCheckBox1.setSelected(bilan);
        else if (projectTaskCheckBox1 != null)
			projectTaskCheckBox1.setSelected(isProjectTask);

        tfWebLink.setText(selectedTasks[0].getWebLink());

        if (selectedTasks[0].shapeDefined()) {
            for (int j = 0; j < ShapeConstants.PATTERN_LIST.length; j++) {
                if (selectedTasks[0].getShape().equals(
                        ShapeConstants.PATTERN_LIST[j])) {
                    shapeComboBox.setSelectedIndex(j);
                    break;
                }
            }
        }

        noteAreaNotes.setText(selectedTasks[0].getNotes());
        //setStartFixed(selectedTasks[0].isStartFixed());
//        setFinishFixed(selectedTasks[0].isFinishFixed());
        myUnpluggedClone = selectedTasks[0].unpluggedClone();
    }

    void enableMilestoneFriendlyControls(boolean enable) {
    	finishDateField1.setEnabled(enable);
    	myEndDatePicker.setEnabled(enable);
    	durationField1.setEnabled(enable);
    }
    // Output methods

    /** as the name indicated */

    public boolean isBilan() {

        bilan = mileStoneCheckBox1.isSelected();

        return bilan;

    }

    public boolean isProjectTask() {

        isProjectTask = projectTaskCheckBox1.isSelected();

        return isProjectTask;

    }

    /** as the name indicated */

//    public GanttCalendar getEnd() {
//
//        return end;
//
//    }


    /** as the name indicated */

    public GanttCalendar getThird() {
        return third;
    }

    public int getThirdDateConstraint() {
        return thirdDateComboBox.getSelectedIndex();
    }

    /** as the name indicated */

    public int getLength() {
    	try {
    		length = Integer.parseInt(durationField1.getText().trim());
    	} catch (NumberFormatException e) {

    	}
    	return length;
    }

    public void fireDurationChanged() {
        String value = durationField1.getText();
        try {
            int duration = Integer.parseInt(value);
            changeLength(duration);
        } catch (NumberFormatException e) {

        }

    }

    /** Set the duration of the task */
    public void changeLength(int _length) {
        if (_length <= 0) {
            _length = 1;
        }

        durationField1.setText(_length + "");
        if (onlyOneTask)
            myDependenciesPanel.durationChanged(_length);
        myAllocationsPanel[0].durationChanged(_length);
        durationFieldNotes.setText(_length + "");
        length = _length;
        // change the end date
        GanttCalendar _end = start.newAdd(length);
        this.end = _end;
        finishDateField1.setText(_end.toString());
    }

    /** as the name indicated */

    public String getNotes() {

        notes = noteAreaNotes.getText();

        return notes;

    }

    /** Return the name of the task */

    public String getTaskName() {
        String text = nameField1.getText();
        return text == null ? "" : text.trim();
    }

    /** @return the web link of the task. */
    public String getWebLink() {
        String text = tfWebLink.getText();
        return text == null ? "" : text.trim();
    }

    /** as the name indicated */

    public int getPercentComplete() {

        percentComplete = ((Integer) percentCompleteSlider.getValue())
                .hashCode();

        return percentComplete;

    }

    /** Return the priority level of the task */

    public int getPriority() {
        priority = priorityComboBox.getSelectedIndex();
        return priority;
    }

//    public void setStartFixed(boolean startFixed) {
//        isStartFixed = startFixed;
//        startDateField1.setForeground(isStartFixed ? Color.BLACK : Color.GRAY);
//    }

//    public void setFinishFixed(boolean startFixed) {
//        isFinishFixed = startFixed;
//        finishDateField1
//                .setForeground(isFinishFixed ? Color.BLACK : Color.GRAY);
//    }

    /** Return the start date of the task */
    public GanttCalendar getStart() {
        //start.setFixed(isStartFixed);
        return start;
    }

    public GanttCalendar getFinish() {
//        end.setFixed(isFinishFixed);
        return end;
    }

    /** Change the start date of the task */
    public void setStart(GanttCalendar dstart, boolean test) {
        myStartDatePicker.setDate(dstart.getTime());
        this.start = dstart;
        if (test == true) {
            return;
        }

//        this.setStartFixed(dstart.isFixed());

        if (this.start.compareTo(this.end) < 0) {
            adjustLength();
        } else {
            GanttCalendar _end = start.newAdd(this.taskLength);
            this.end = _end;
            //finishDateField1.setText(_end.toString());
            this.myEndDatePicker.setDate(this.end.getTime());
        }
    }

    /** Change the end date of the task */
    public void setEnd(GanttCalendar dend, boolean test) {
        myEndDatePicker.setDate(dend.getTime());
        this.end = dend;
        if (test == true) {
            return;
        }
//        this.setFinishFixed(dend.isFixed());

        if (this.start.compareTo(this.end) < 0) {
            adjustLength();
        } else {
            GanttCalendar _start = this.end.newAdd(-length);
            this.start = _start;
            startDateField1.setText(_start.toString());
        }
    }

    /** Change the third date of the task */
    public void setThird(GanttCalendar dthird, boolean test) {
        myThirdDatePicker.setDate(dthird.getTime());
        this.third = dthird;
        if (test) {
            return;
        }

        switch (thirdDateComboBox.getSelectedIndex()) {
        case TaskImpl.EARLIESTBEGIN:
            thirdDateButton1.setEnabled(true);
            break;
        case TaskImpl.NONE:
            thirdDateButton1.setEnabled(false);
            break;
        }
    }

    private void adjustLength() {
        myUnpluggedClone.setStart(this.start);
        myUnpluggedClone.setEnd(this.end);
        length = (int) myUnpluggedClone.getDuration().getLength();
        durationField1.setText("" + length);
        // durationField2.setText(""+length);
        myAllocationsPanel[0].durationChanged(length);
        durationFieldNotes.setText("" + length);
    }

    private void setInitialValues(GanttTask task) {
        this.taskWebLink = task.getWebLink();
        this.taskIsMilestone = task.isMilestone();
        this.taskStartDate = task.getStart();
        this.taskLength = task.getLength();
        this.taskNotes = task.getNotes();
        this.taskCompletionPercentage = task.getCompletionPercentage();
        this.taskPriority = task.getPriority();
        //this.taskIsStartFixed = task.isStartFixed();
//        this.taskIsFinishFixed = task.isFinishFixed();
        this.taskShape = task.getShape();
        this.taskThirdDate = task.getThird();
        this.taskThirdDateConstraint = task.getThirdDateConstraint();
		this.taskIsProjectTask = task.isProjectTask();
    }

    private void copyValues(ResourcesTableModel original,
            ResourcesTableModel clone) {
        for (int i = 0; i < clone.getRowCount(); i++)
            clone.setValueAt(null, i, 1);
        for (int j = 0; j < original.getRowCount(); j++)
            for (int k = 0; k < original.getColumnCount(); k++)
                clone.setValueAt(original.getValueAt(j, k), j, k);
    }

    private boolean canBeProjectTask(Task testedTask, TaskContainmentHierarchyFacade taskHierarchy) {
    	Task[] nestedTasks = taskHierarchy.getNestedTasks(testedTask);
    	if (nestedTasks.length==0) {
    		return false;
    	}
    	for (Task parent = taskHierarchy.getContainer(testedTask); parent!=null; parent = taskHierarchy.getContainer(parent)) {
    		if (parent.isProjectTask()) {
    			return false;
    		}
    	}
    	for (int i=0; i<nestedTasks.length; i++) {
    		if (isProjectTaskOrContainsProjectTask(nestedTasks[i])) {
    			return false;
    		}
    	}
    	return true;
    }
	private boolean isProjectTaskOrContainsProjectTask(Task task) {
		if (task.isProjectTask()) {
			return true;
		}
		boolean result = false;
		Task[] nestedTasks = task.getNestedTasks();
		for (int i=0; i<nestedTasks.length; i++) {
			if (isProjectTaskOrContainsProjectTask(nestedTasks[i])) {
				result = true;
				break;
			}
		}
		return result;
	}

	private JCheckBox constructCheckBox () {
		boolean canBeProjectTask = true;
		boolean canBeMilestone = true;
		TaskContainmentHierarchyFacade taskHierarchy = myTaskManager.getTaskHierarchy();
		for (int i = 0 ; i < selectedTasks.length ; i++) {
			canBeMilestone &= !taskHierarchy.hasNestedTasks(selectedTasks[i]);
			canBeProjectTask &= canBeProjectTask(selectedTasks[i], taskHierarchy);
		}
		assert false==(canBeProjectTask && canBeMilestone);
		final JCheckBox result;
		if (canBeProjectTask) {
			result = new JCheckBox (language.getText("projectTask"));
			projectTaskCheckBox1 = result;
		}
		else if (canBeMilestone) {
			mileStoneCheckBox1 = new JCheckBox(new AbstractAction(language.getText("meetingPoint")) {
				public void actionPerformed(ActionEvent arg0) {
					enableMilestoneFriendlyControls(!isBilan());
				}
			});
			result = mileStoneCheckBox1;
		}
		else {
			result = null;
		}
		return result;
	}

}
