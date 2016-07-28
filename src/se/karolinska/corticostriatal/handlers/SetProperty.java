package se.karolinska.corticostriatal.handlers;

import com.google.gson.Gson;
import java.io.IOException;
import org.micromanager.utils.ReportingUtils;
import se.karolinska.corticostriatal.Message;

/**
 *  Handle change requests.
 * 
 *  @author Matthijs
 */
public class SetProperty extends Handler {

    
    @Override
    protected String getResponse() throws IOException {
        ReportingUtils.logMessage("Generating SET response.");
        
        try {
            message         = new Message("OK");
            if (!params.containsKey("label") || !params.containsKey("propName") || !params.containsKey("propValue"))
                throw new MissingKeyException();
            
            
        } catch (MissingKeyException e) {
            message         = new Message("ERROR");
            message.error   = "SetProperty requests require the fields 'label', 'propName' and 'propValue' to be set.";
        } catch (Exception e) {
            message         = new Message("ERROR");
            message.error   = "Could not handle SET request.";
            ReportingUtils.logError(e);
        }
        Gson gson           = new Gson();
        String response     = gson.toJson(message);
        return response;        
    }
 
    
    private class MissingKeyException extends Exception { }
}