package net.sourceforge.ganttproject.importer;

import java.awt.Container;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import CustomPropertyManager;
import GanttProject;
import GanttProjectImpl;
import IGanttProject;
import PrjInfos;
import net.sourceforge.ganttproject.document.Document;
import net.sourceforge.ganttproject.document.DocumentCreator;
import net.sourceforge.ganttproject.document.DocumentManager;
import net.sourceforge.ganttproject.document.FileDocument;
import net.sourceforge.ganttproject.gui.GanttDialogInfo;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.TableHeaderUIFacade;
import net.sourceforge.ganttproject.io.GPSaver;
import net.sourceforge.ganttproject.io.GanttXMLOpen;
import net.sourceforge.ganttproject.language.GanttLanguage;
import net.sourceforge.ganttproject.parser.AllocationTagHandler;
import net.sourceforge.ganttproject.parser.BlankLineTagHandler;
import net.sourceforge.ganttproject.parser.DependencyTagHandler;
import net.sourceforge.ganttproject.parser.GPParser;
import net.sourceforge.ganttproject.parser.HolidayTagHandler;
import net.sourceforge.ganttproject.parser.ParserFactory;
import net.sourceforge.ganttproject.parser.ResourceTagHandler;
import net.sourceforge.ganttproject.parser.RoleTagHandler;
import net.sourceforge.ganttproject.parser.TaskTagHandler;
import net.sourceforge.ganttproject.parser.VacationTagHandler;
import net.sourceforge.ganttproject.resource.HumanResourceManager;
import net.sourceforge.ganttproject.resource.ResourceManager;
import net.sourceforge.ganttproject.roles.RoleManager;
import net.sourceforge.ganttproject.roles.RoleManagerImpl;
import net.sourceforge.ganttproject.task.CustomColumnsManager;
import net.sourceforge.ganttproject.task.CustomColumnsStorage;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.TaskManagerImpl;

public class ImporterFromGanttFile extends ImporterBase implements Importer {

	private UIFacade myUIFacade;

	public String getFileNamePattern() {
        return "xml|gan";
    }

    public String getFileTypeDescription() {
        return GanttLanguage.getInstance().getText("ganttprojectFiles");
    }

    public void run(GanttProject project, final UIFacade uiFacade, final File selectedFile, final boolean bMerge) {
        myUIFacade = project.getUIFacade();
		final GanttProject targetProject = project;
		final BufferProject bufferProject = createBufferProject(targetProject, uiFacade);
		project.getUndoManager().undoableEdit("Import",
                new Runnable() {
					public void run() {
						openDocument(targetProject, bufferProject, uiFacade, selectedFile, bMerge);
						myUIFacade.getTaskTree().getVisibleFields().importData(bufferProject.getVisibleFields());
					}
				});
    }


    private static class TaskFieldImpl implements TableHeaderUIFacade.Column {
    	private final String myID;
		private final int myOrder;
		private final int myWidth;

		TaskFieldImpl(String id, int order, int width) {
    		myID = id;
    		myOrder = order;
    		myWidth = width;
    	}
		public String getID() {
			return myID;
		}

		public int getOrder() {
			return myOrder;
		}

		public int getWidth() {
			return myWidth;
		}
		public boolean isVisible() {
			return true;
		}
		public String getName() {
			return null;
		}
    	
    }
    private static class VisibleFieldsImpl implements TableHeaderUIFacade {
		private List myFields = new ArrayList();
		public void add(String name, int order, int width) {
			myFields.add(new TaskFieldImpl(name, order, width));
		}
		public void clear() {
			myFields.clear();
		}
		public Column getField(int index) {
			return (Column) myFields.get(index);
		}
		public int getSize() {
			return myFields.size();
		}
		public void importData(TableHeaderUIFacade source) {
			for (int i=0; i<source.getSize(); i++) {
				Column nextField = source.getField(i);
				myFields.add(nextField);
			}
		}
	}
    class BufferProject extends GanttProjectImpl implements ParserFactory {
		PrjInfos myProjectInfo = new PrjInfos();
		final DocumentManager myDocumentManager;
		final TaskManager myTaskManager;
		final UIFacade myUIfacade;
		private final TableHeaderUIFacade myVisibleFields = new VisibleFieldsImpl();
		
		BufferProject(GanttProject targetProject, UIFacade uiFacade) {
			myDocumentManager = new DocumentCreator(this, uiFacade, this) {
				protected TableHeaderUIFacade getVisibleFields() {
					return myVisibleFields;
				}
			};
			myTaskManager = targetProject.getTaskManager().emptyClone();
			myUIfacade = uiFacade;
		}
		public TableHeaderUIFacade getVisibleFields() {
			return myVisibleFields;
		}
		public GPParser newParser() {
			return new GanttXMLOpen(myProjectInfo, getUIConfiguration(), getTaskManager(), myUIfacade);
		}
		public GPSaver newSaver() {
			return null;
		}
		public DocumentManager getDocumentManager() {
			return myDocumentManager;
		}
		public TaskManager getTaskManager() {
			return myTaskManager;
		}
		public CustomColumnsStorage getCustomColumnsStorage() {
			return myTaskManager.getCustomColumnStorage();
		}
		public CustomColumnsManager getTaskCustomColumnManager() {
			// TODO Auto-generated method stub
			return super.getTaskCustomColumnManager();
		}
	}
    private BufferProject createBufferProject(
    		final GanttProject targetProject, final UIFacade uiFacade) {
    	return new BufferProject(targetProject, uiFacade);
    };

	protected Document getDocument(File selectedFile) {
        return new FileDocument(selectedFile);
    }

    protected void openDocument(GanttProject targetProject, IGanttProject bufferProject, UIFacade uiFacade, File selectedFile,
            boolean bMerge) {
        try {
        	Document document = bufferProject.getDocumentManager().getDocument(selectedFile.getAbsolutePath());
        	document.read();
            targetProject.getRoleManager().importData(bufferProject.getRoleManager());
            {
            	CustomPropertyManager targetResCustomPropertyMgr = targetProject.getResourceCustomPropertyManager();
            	targetResCustomPropertyMgr.importData(bufferProject.getResourceCustomPropertyManager());
            }
            Map original2ImportedResource = 
            	targetProject.getHumanResourceManager().importData(bufferProject.getHumanResourceManager());
       
            {
            	CustomColumnsStorage targetCustomColumnStorage = targetProject.getCustomColumnsStorage();
            	targetCustomColumnStorage.importData(bufferProject.getCustomColumnsStorage());
            }
            TaskManagerImpl origTaskManager = (TaskManagerImpl) targetProject.getTaskManager();
            try {
	            origTaskManager.setEventsEnabled(false);
	            Map original2ImportedTask = origTaskManager.importData(bufferProject.getTaskManager());
	            origTaskManager.importAssignments(
	            		bufferProject.getTaskManager(),
	                    targetProject.getHumanResourceManager(), 
	                    original2ImportedTask,
	                    original2ImportedResource);
            }
            finally {
            	origTaskManager.setEventsEnabled(true);
            }
            targetProject.getResourcePanel().getResourceTreeTableModel().updateResources();
        	/*
            TaskManager taskManager = bufferProject.getTaskManager().emptyClone();
            // ResourceManager resourceManager =
            // project.getHumanResourceManager();
            ResourceManager resourceManager = new HumanResourceManager(bufferProject
                    .getRoleManager().getDefaultRole());
            RoleManager roleManager = new RoleManagerImpl();
            GanttXMLOpen opener = new GanttXMLOpen(bufferProject.prjInfos, bufferProject
                    .getArea(), taskManager, bufferProject.getUIFacade());
            opener.addTagHandler(opener.getDefaultTagHandler());
            TaskTagHandler taskHandler = new TaskTagHandler(taskManager, opener
                    .getContext());
            opener.addTagHandler(taskHandler);
            BlankLineTagHandler blankLineHandler = new BlankLineTagHandler(bufferProject.getUIFacade().getGanttChart());
            opener.addTagHandler(blankLineHandler);
            ResourceTagHandler resourceHandler = new ResourceTagHandler(
                    resourceManager, roleManager, bufferProject.getResourceCustomPropertyManager());
            opener.addTagHandler(resourceHandler);
            DependencyTagHandler dependencyHandler = new DependencyTagHandler(
                    opener.getContext(), taskManager, myUIFacade);
            opener.addTagHandler(dependencyHandler);
            RoleTagHandler roleHandler = new RoleTagHandler(roleManager);
            opener.addTagHandler(roleHandler);
            opener.addParsingListener(dependencyHandler);
            opener.addParsingListener(resourceHandler);
            AllocationTagHandler allocationHandler = new AllocationTagHandler(
                    resourceManager, taskManager, roleManager);
            opener.addTagHandler(allocationHandler);
            opener.addParsingListener(allocationHandler);
            VacationTagHandler vacationHandler = new VacationTagHandler(
                    resourceManager);
            opener.addTagHandler(vacationHandler);

            //
            if (bMerge == false) {
                HolidayTagHandler holidayHandler = new HolidayTagHandler(
                        bufferProject);

                opener.addTagHandler(holidayHandler);
                opener.addParsingListener(holidayHandler);
            } else {
                opener.isMerging(true);
            }
            opener.load(document.getInputStream());

            bufferProject.getRoleManager().importData(roleManager);
            Map original2ImportedResource = bufferProject.getHumanResourceManager()
                    .importData(resourceManager);
            TaskManagerImpl origTaskManager = (TaskManagerImpl) bufferProject.getTaskManager();
            try {
	            origTaskManager.setEventsEnabled(false);
	            Map original2ImportedTask = origTaskManager.importData(taskManager);
	            origTaskManager.importAssignments(
	            		taskManager,
	                    bufferProject.getHumanResourceManager(), original2ImportedTask,
	                    original2ImportedResource);
            }
            finally {
            	origTaskManager.setEventsEnabled(true);
            }
            bufferProject.getResourcePanel().getResourceTreeTableModel()
                    .updateResources();
                    */
        } catch (IOException e) {
            uiFacade.showErrorDialog(e);
        }

    }
}
