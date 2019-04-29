/**
 * 
 */
package java.com.xqtv.paopao.dataaccess.spring;

import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.com.xqtv.paopao.dataaccess.log.BCLogger;

/**
 * @author Richard
 * 
 */
public class QuartzContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		WebApplicationContext webApplicationContext = (WebApplicationContext) arg0.getServletContext()
				.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		org.quartz.impl.StdScheduler startQuertz = (org.quartz.impl.StdScheduler) webApplicationContext
				.getBean("startQuertz");
		if (startQuertz != null) {
			startQuertz.shutdown();

		}
		BCLogger.error(getClass(), "Stop quertz!!!");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// 不做任何事情
	}
}
