package com.znz;



import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.util.Properties;

/**
 * User: Terence
 * Date: 2014/5/27
 * Time: 16:48
 */
@Slf4j
public class WebBootstrap {



    /**
     * jetty开发模式时的webapp路径
     */

    public static void main(String[] args) throws Exception {

        startJetty("./znz-web/src/main/webapp/WEB-INF/web.xml", "./znz-web/src/main/webapp/", 8088);
    }

    private static void startJetty(String descriptor, String resourceBase, int port) throws Exception {
     /*   Server server = new Server(port);
        WebAppContext context = new WebAppContext();
        context.setDescriptor(descriptor);
        context.setResourceBase(resourceBase);
        context.setParentLoaderPriority(true);
        context.setContextPath("/znz-web");
        context.setClassLoader(
        Thread.currentThread().getContextClassLoader());
        server.setHandler(context);
        server.start();
        server.join();*/
        Server server = new Server(8080);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/znz-web");
        webapp.setResourceBase("d:/git/znz-web/src/main/webapp/");

        server.setHandler(webapp);
        server.start();
        server.join();
    }
}
