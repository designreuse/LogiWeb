package com.tsystems.javaschool.timber.logiweb.view.controllers;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by tims on 3/3/2016.
 */
@WebListener
public class ContextController implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        //setting up log4j
        ServletContext context = event.getServletContext();
        String rootPath = context.getRealPath("/");
        System.setProperty("rootPath", rootPath);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}
