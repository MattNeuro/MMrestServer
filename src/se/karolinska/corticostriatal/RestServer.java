package se.karolinska.corticostriatal;

import mmcorej.CMMCore;
import mmcorej.StrVector;
import org.micromanager.api.ScriptInterface;

/**
 *  Implement a REST service hook into MicroManager. This enables external
 *  applications to retrieve information about the running system, and perhaps
 *  some day manipulate it.
 * 
 *  @author Matthijs Dorst, Karolinska Institutet, Stockholm, Sweden.
 */
public class RestServer implements org.micromanager.api.MMPlugin {

    public static ScriptInterface   si      = null;
    public static CMMCore           core    = null;
    

    @Override
    public void setApp(ScriptInterface si) {
        System.out.println("Ok!");
        RestServer.si   = si;
        RestServer.core = si.getMMCore();
        try {
            Service.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {

    }
    
    
    /**
     *  General purpose information members.
     */
    @Override public String getDescription() { return "Enable external access to MM data."; }
    @Override public String getInfo()        { return "REST-full interface server.";        }
    @Override public String getVersion()     { return "1";               }
    @Override public String getCopyright()   { return "Matthijs Dorst";  }
    @Override public void dispose()          {                           }    
}