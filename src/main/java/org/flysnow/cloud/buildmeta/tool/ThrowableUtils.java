package org.flysnow.cloud.buildmeta.tool;

/**
 * trace exception in log4j
 */
public class ThrowableUtils {
	/**
	 * make exception stack trace become string
	 *
	 * @param e
	 * @return
	 */
	public static String stackTrace2String(Throwable e) {
		StringBuffer sb = new StringBuffer();
		sb.append(e.toString()).append("\r\n");
		StackTraceElement[] traces = e.getStackTrace();
		for (StackTraceElement trace : traces) {
			sb.append(trace.toString()).append("\r\n");
		}
		return sb.toString();
	}
}