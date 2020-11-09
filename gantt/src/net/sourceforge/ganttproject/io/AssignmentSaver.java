package net.sourceforge.ganttproject.io;


import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import IGanttProject;
import net.sourceforge.ganttproject.resource.HumanResource;
import net.sourceforge.ganttproject.roles.Role;
import net.sourceforge.ganttproject.task.ResourceAssignment;
import net.sourceforge.ganttproject.task.Task;

class AssignmentSaver extends SaverBase {
    void save(IGanttProject project, TransformerHandler handler) throws SAXException {
        AttributesImpl attrs = new AttributesImpl();
        startElement("allocations", handler);
        Task[] tasks = project.getTaskManager().getTasks();
        for (int i = 0; i < tasks.length; i++) {
            Task task = tasks[i];
            ResourceAssignment[] assignments = task.getAssignments();
            for (int j = 0; j < assignments.length; j++) {
                int task_id = task.getTaskID();
                ResourceAssignment next = assignments[j];

                Role roleForAssignment = next.getRoleForAssignment();
                if (roleForAssignment == null) {
                    if (next.getResource() instanceof HumanResource) {
                        roleForAssignment = ((HumanResource) next
                                .getResource()).getRole();
                    }
                }
                addAttribute("task-id", String.valueOf(task_id), attrs);
                addAttribute("resource-id", String.valueOf(next.getResource().getId()), attrs);
                addAttribute("function", roleForAssignment.getPersistentID(), attrs);
                addAttribute("responsible", String.valueOf(next.isCoordinator()), attrs);
                addAttribute("load", String.valueOf(next.getLoad()), attrs);
                emptyElement("allocation", attrs, handler);
            }
        }
        endElement("allocations", handler);
    }

}
