package java.com.xqtv.paopao.dataaccess.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * log4j2正常log包装类，用于定义log格式
 *
 * @author Richard
 *
 */
public class BCLogger {
	private static final Logger logger = LogManager.getLogger(BCLogger.class.getName());

	/**
	 * Logs a message with SEVERE level.
	 *
	 * @param classname
	 *            Class logging the message.
	 * @param msg
	 *            Message to be logged.
	 */
	@SuppressWarnings("rawtypes")
	public static void error(Class classname, String msg) {
		StringBuffer buf = new StringBuffer(200);
		buf.append("(" + classname.getSimpleName() + ") " + msg);
		logMessage(classname, buf.toString(), Level.ERROR);
	}

	/**
	 * Logs a message with SEVERE level.
	 *
	 * @param classname
	 *            Class logging the message.
	 * @param msg
	 *            Message to be logged.
	 * @param e
	 *            Exception to be printed
	 */
	@SuppressWarnings("rawtypes")
	public static void error(Class classname, String msg, Throwable e) {
		StringBuffer buf = new StringBuffer(200);
		buf.append("(" + classname.getName() + ") " + msg);
		logMessage(classname, buf.toString(), e, Level.ERROR);
	}

	/**
	 * Logs a message with SEVERE level.
	 *
	 * @param classname
	 *            Class logging the message.
	 * @param msg
	 *            Message to be logged.
	 */
	@SuppressWarnings("rawtypes")
	public static void warn(Class classname, String msg) {
		logMessage(classname, msg, Level.WARN);
	}

	/**
	 * Logs a message with SEVERE level.
	 *
	 * @param classname
	 *            Class logging the message.
	 * @param msg
	 *            Message to be logged.
	 * @param e
	 *            Exception to be printed
	 */
	@SuppressWarnings("rawtypes")
	public static void warn(Class classname, String msg, Throwable e) {
		logMessage(classname, msg, e, Level.WARN);
	}

	/**
	 * Logs a message with INFO level.
	 *
	 * @param classname
	 *            Class logging the message. Ignored, not logged.
	 * @param msg
	 *            Message to be logged.
	 */
	@SuppressWarnings("rawtypes")
	public static void info(Class classname, String msg) {
		logMessage(classname, msg, Level.INFO);
	}

	/**
	 * Logs a message with INFO level.
	 *
	 * @param classname
	 *            Class logging the message. Ignored, not logged.
	 * @param msg
	 *            Message to be logged.
	 * @param e
	 *            Exception to be printed
	 */
	@SuppressWarnings("rawtypes")
	public static void info(Class classname, String msg, Throwable e) {
		logMessage(classname, msg, e, Level.INFO);
	}

	/**
	 * Logs a message with FINE level for not debug and WARNING level for debug
	 *
	 * @param classname
	 *            Class logging the message. Ignored, not logged.
	 * @param msg
	 *            Message to be logged.
	 */
	@SuppressWarnings("rawtypes")
	public static void debug(Class classname, String msg) {
		StringBuffer buf = new StringBuffer(200);
		buf.append("(" + classname.getName() + ") " + msg);
		logMessage(classname, buf.toString(), Level.DEBUG);
	}

	@SuppressWarnings("rawtypes")
	private static void logMessage(Class classname, String msg, Level level) {
		logger.log(level, msg);
	}

	@SuppressWarnings("rawtypes")
	private static void logMessage(Class classname, String msg, Throwable e, Level level) {
		logger.log(level, msg, e);
	}
}
