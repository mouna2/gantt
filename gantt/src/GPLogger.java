

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sourceforge.ganttproject.gui.UIFacade;


public class GPLogger {
	private static Logger ourLogger = Logger.getLogger("org.ganttproject");
	private static Handler ourHandler;
	private static UIFacade ourUIFacade;

	public static void setup() {
        ourHandler = new ConsoleHandler();
		ourLogger.addHandler(ourHandler);
		ourLogger.setLevel(Level.ALL);
		ourHandler.setFormatter(new java.util.logging.SimpleFormatter());
	}

	public static boolean log(Throwable e) {
		if (ourHandler == null) {
			return false;
		}
		ourLogger.log(Level.WARNING, e.getMessage(), e);
		if (ourUIFacade != null) {
			ourUIFacade.logErrorMessage(e);
		}
		return true;
	}

	public static void log(String message) {
		ourLogger.log(Level.INFO, message);
	}

	public static void setUIFacade(UIFacade uifacade) {
		ourUIFacade = uifacade;
	}
}
