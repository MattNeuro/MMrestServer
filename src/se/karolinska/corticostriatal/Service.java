package se.karolinska.corticostriatal;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import se.karolinska.corticostriatal.handlers.ImageGetHandler;
import se.karolinska.corticostriatal.handlers.IndexHandler;
import se.karolinska.corticostriatal.handlers.ImageViewHandler;
import se.karolinska.corticostriatal.handlers.SetProperty;

/**
 *  HTTP service. When started, this will listen on the selected port for 
 *  incoming connections and HTTP requests.
 * 
 *  @author Matthijs
 */
public class Service {

    private static Service instance     =   null;
    private HttpServer     server       =   null;
    
    
    /**
     *  Start the HTTP service. If no server context exists, create one.
     */
    public static void start () throws Exception {
        if (instance == null)
            instance = new Service();
        instance.server.start();
    }
    
    /**
     *  Stop the HTTP service, if it exists.
     */
    public static void stop () {
        if (instance != null)
            instance.server.stop(0);
    }
    
    
    /**
     *  Create a new HTTP server instance. Register default handlers for the
     *  various contexts.
     * 
     * @throws Exception 
     */
    private Service() throws Exception {
        createServer();

        // Internal documentation handlers:
        server.createContext("/view/image/",    new ImageViewHandler());
        server.createContext("/",               new IndexHandler());
        
        // SET / GET request handlers:
        (server.createContext("/get/image/",    new ImageGetHandler())).getFilters().add(new ParameterFilter());
        (server.createContext("/set/property/", new SetProperty())).getFilters().add(new ParameterFilter());        
    }
    
    
    /**
     *  Attempt to create a server and set a default executer.
     * @throws IOException 
     */
    private void createServer () throws IOException {
        server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.setExecutor(null); // creates a default executor        
    }
}