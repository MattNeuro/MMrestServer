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
public class SetHandler extends Handler {

    
    @Override
    protected String getResponse() throws IOException {
        ReportingUtils.logMessage("Generating SET response.");
        
        try {
            message         = new Message("OK");
            message.payload.put("image", getImage());
            
            if (params.containsKey("cameraProperties"))
                loadCameraProperties();
            
            if (params.containsKey("tags"))
                loadImageTags();            
        } catch (Exception e) {
            message         = new Message("ERROR");
            message.error   = "Could not handle SET request.";
            ReportingUtils.logError(e);
        }
        Gson gson           = new Gson();
        String response     = gson.toJson(message);
        return response;        
    }
   
}