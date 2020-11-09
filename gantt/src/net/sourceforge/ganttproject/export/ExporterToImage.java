/*
 * Created on 28.05.2005
 */
package net.sourceforge.ganttproject.export;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;

import javax.imageio.ImageIO;

import GanttExportSettings;
import GanttGraphicArea;
import IGanttProject;
import net.sourceforge.ganttproject.chart.Chart;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.options.model.EnumerationOption;
import net.sourceforge.ganttproject.gui.options.model.GPAbstractOption;
import net.sourceforge.ganttproject.gui.options.model.GPOption;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.language.GanttLanguage;

/**
 * @author bard
 */
public class ExporterToImage implements Exporter {

    static class FileTypeOption extends GPAbstractOption implements
            EnumerationOption {
        static final String[] FILE_FORMAT_ID = new String[] {
                "impex.image.fileformat.png", "impex.image.fileformat.jpeg" };

        static final String[] FILE_EXTENSION = new String[] { "png", "jpg" };

        private String myValue = FILE_FORMAT_ID[0];

        FileTypeOption() {
            super("impex.image.fileformat");
        }

        public String[] getAvailableValues() {
            return FILE_FORMAT_ID;
        }

        public void setValue(String value) {
            myValue = value;
        }

        public String getValue() {
            return myValue;
        }

        String proposeFileExtension() {
            for (int i = 0; i < FileTypeOption.FILE_FORMAT_ID.length; i++) {
                if (myValue.equals(FILE_FORMAT_ID[i])) {
                    return FILE_EXTENSION[i];
                }
            }
            throw new IllegalStateException("Selected format=" + myValue
                    + " has not been found in known formats:"
                    + Arrays.asList(FILE_FORMAT_ID));
        }

        public String getPersistentValue() {
            return null;
        }

        public void loadPersistentValue(String value) {
        }

        public boolean isChanged() {
            return false;
        }
    }

    private IGanttProject myProject;

    private UIFacade myUIFacade;

    private FileTypeOption myFileTypeOption = new FileTypeOption();

    private GPOptionGroup myOptions = new GPOptionGroup("impex.image",
            new GPOption[] { myFileTypeOption });

    private Chart myGanttChart;

    public ExporterToImage() {
        myOptions.setTitled(false);
    }

    public String getFileTypeDescription() {
        return MessageFormat.format(GanttLanguage.getInstance().getText(
                "impex.image.description"),
                new Object[] { proposeFileExtension() });
    }

    public GPOptionGroup getOptions() {
        return myOptions;
    }

    public GPOptionGroup[] getSecondaryOptions() {
        //return myGanttChart.getOptionGroups();
        return null;
    }    
    
    public String getFileNamePattern() {
        return proposeFileExtension();
    }

    public void setContext(IGanttProject project, UIFacade uiFacade) {
        myProject = project;
        myUIFacade = uiFacade;
        myGanttChart = uiFacade.getGanttChart().createCopy();
    }

    public void run(File outputFile,ExportFinalizationJob finalizationJob) throws Exception {
        Chart chart = myUIFacade.getActiveChart();
        if (chart==null) {
            chart = myUIFacade.getGanttChart();
        }
        RenderedImage renderedImage = chart.getRenderedImage(new GanttExportSettings());
        ImageIO.write(renderedImage, myFileTypeOption.proposeFileExtension(),
                outputFile);
        finalizationJob.run(new File[] { outputFile });
    }

    public String proposeFileExtension() {
        return myFileTypeOption.proposeFileExtension();
    }

    public String[] getFileExtensions() {
        return FileTypeOption.FILE_EXTENSION;
    }

}
