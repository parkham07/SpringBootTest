package kr.co.parkham.config.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Slf4j
@Configuration
public class CustomServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.setProperty("log4jdbc.log4j2.properties.file", "/logback/log4jdbc.log4j2.properties");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		log.info("contextDestroyed called");
	}
}