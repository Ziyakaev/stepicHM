package app;

import accounts.AccountService;
import dbService.DBException;
import dbService.DBService;
import dbService.dataSets.UsersDataSet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.SessionsServlet;
import servlets.SignInServlet;
import servlets.SignUpServlet;
import servlets.UsersServlet;

import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws Exception {
        DBService dbService = new DBService();
        dbService.printConnectionInfo();
        System.out.println("name is "+   dbService.getUser("tully"));
        try {
            long userId=dbService.addUser("test");
            System.out.println("Added user id: " +userId);
            UsersDataSet dataSet = dbService.getUser(userId);
            System.out.println("User data set: "+dataSet);
        }catch (DBException e){
            e.printStackTrace();
        }
        AccountService accountService = new AccountService();

        accountService.addNewUser(new accounts.UserProfile("admin"));
        accountService.addNewUser(new accounts.UserProfile("test"));
        ServletContextHandler context=new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new UsersServlet(accountService)),"/api/v1/users");
        context.addServlet(new ServletHolder(new SessionsServlet(accountService)),"/api/v1/sessions");
        context.addServlet(new ServletHolder(new SignInServlet(accountService)),"/signin");
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)),"/signup");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("public_html");

        HandlerList handlers= new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler,context});

        Server server = new Server(8080);
        server.setHandler(handlers);
        server.start();
        Logger.getGlobal().info("Server started");

        server.join();
    }
}
