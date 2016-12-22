package edu.nju.exam.listeners; /**
 * Created by zhouxiaofan on 2016/12/19.
 */

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

@WebListener()
public class CounterListener implements ServletContextListener,ServletContextAttributeListener,
        HttpSessionListener, HttpSessionAttributeListener,HttpSessionBindingListener {
    int counter=0;
    int userCounter=0;
    private ServletContext application = null;
    //    String filePath="src/main/webapp/counter.txt";
    String filePath="/Users/zhouxiaofan/Desktop/d/src/main/webapp/counter.txt";
    // Public constructor is required by servlet spec
    public CounterListener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
      this.application = sce.getServletContext();
      readCounterFile();
      application.setAttribute("webCounter", counter);
      application.setAttribute("userCounter", userCounter);
      System.out.println("Application initialized");
    }

    private void readCounterFile() {
        try {
            System.out.println("Reading Start");
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String s[] = reader.readLine().split(" ") ;
            counter = Integer.parseInt( s[0] );
            userCounter = Integer.parseInt( s[1] );

            reader.close();
            System.out.println("Reading " + counter);
            System.out.println("User " + userCounter);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
    }


    synchronized void writeCounter(ServletContextAttributeEvent scae) {
        ServletContext servletContext= scae.getServletContext();
        counter = Integer.parseInt((String) servletContext.getAttribute("webCounter"));
        userCounter = Integer.parseInt((String) servletContext.getAttribute("userCounter"));
        writeFile();
    }

    private void writeFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(Integer.toString(counter)+" "+Integer.toString(userCounter));
            writer.close();
            System.out.println("Writing");
        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void attributeAdded(ServletContextAttributeEvent servletContextAttributeEvent) {

    }

    public void attributeRemoved(ServletContextAttributeEvent servletContextAttributeEvent) {

    }

    public void attributeReplaced(ServletContextAttributeEvent servletContextAttributeEvent) {
        writeCounter(servletContextAttributeEvent);
    }

    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
        userCounter++;
        writeFile();
        this.application.setAttribute("userCounter",userCounter);

    }

    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        counter++;
        writeFile();
        this.application.setAttribute("webCounter",counter);
        System.out.println("sessionCreated userCounter = "+userCounter);
    }

    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        userCounter--;
        counter--;
        writeFile();
        this.application.setAttribute("webCounter",counter);
        this.application.setAttribute("userCounter",userCounter);
        System.out.println("sessionDestroyed userCounter = "+userCounter);
    }

    public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    public void valueUnbound(HttpSessionBindingEvent httpSessionBindingEvent) {

    }
}
