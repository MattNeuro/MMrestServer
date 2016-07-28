package se.karolinska.corticostriatal.handlers;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import ij.ImagePlus;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import mmcorej.StrVector;
import mmcorej.TaggedImage;
import org.json.JSONObject;
import org.micromanager.utils.ReportingUtils;
import se.karolinska.corticostriatal.Message;
import se.karolinska.corticostriatal.RestServer;

/**
 *  Image retrieval handler. This handles REST requests for images, on the 
 *  path /get/image/. Additional parameters may be specified to modify the
 *  response.
 * 
 *  @author Matthijs
 */

public class ImageGetHandler extends GetHandler {
    
    private final List<String> allowedFormats    = Arrays.asList("JPEG", "PNG", "GIF", "BMP", "WBMP");
    
    
    @Override
    protected String getResponse () throws IOException {
        ReportingUtils.logMessage("Generating image retrieval response.");
        
        try {
            message         = new Message("OK");
            message.payload.put("image", getImage());
            
            if (params.containsKey("cameraProperties"))
                loadCameraProperties();
            
            if (params.containsKey("tags"))
                loadImageTags();            
        } catch (Exception e) {
            message         = new Message("ERROR");
            message.error   = "Error retrieving live image.";
        }

        Gson gson           = new Gson();
        String response     = gson.toJson(message);
        return response;
    }
    

    /**
     *  Load the camera properties and include them in the response. If the 
     *  request included a "cameraProperties" key, and if the value of this 
     *  parameter equals 1, include a list of camera properties in the return
     *  message.
     */
    private void loadCameraProperties () throws Exception {
        if (Integer.parseInt((String) params.get("cameraProperties")) != 1)
            return;
        
        String      value;
        String      camera      = RestServer.core.getCameraDevice();
        StrVector   properties  = RestServer.core.getDevicePropertyNames(camera);
        LinkedTreeMap map       = new LinkedTreeMap();
        
        for (String property : properties) {
            value = RestServer.core.getProperty(camera, property);
            map.put(property, value);
        }
        message.payload.put("cameraProperties", map);
    }
    
    
    
    /**
     *  Retrieve the current live image window contents as Base64 encoded 
     *  character array. This is formatted as a JPEG image, to decrease 
     *  bandwidth requirement. 
     * 
     *  @return A base64 image-string.
     *  @throws IOException: most notably when no live image window can be read.
     */
    private String getImage () throws IOException, Exception {
        ij.gui.ImageWindow window   = RestServer.si.getSnapLiveWin();
        ImagePlus imagePlus         = window.getCanvas().getImage();
        BufferedImage image         = imagePlus.getBufferedImage();
        
        String imageFormat          = getImageFormat();
        message.payload.put("imageFormat", imageFormat);
        
        // Encode image into desired encoding, write it to byte-stream.
        ByteArrayOutputStream out   = new ByteArrayOutputStream(1000);
        ImageIO.write(image, imageFormat, out);
        out.flush();
        String encodedImage         = Base64.encode(out.toByteArray());
        out.close();
        return encodedImage;
    }
    
    
    /**
     *  Get the last tagged image' tags.
     * 
     *  @return
     *  @throws Exception 
     */
    private void loadImageTags () throws Exception {
        if (Integer.parseInt((String) params.get("tags")) != 1)
            return;
        
        LinkedTreeMap   map     = new LinkedTreeMap();
        TaggedImage     image   = RestServer.core.getLastTaggedImage();
        JSONObject      tags    = image.tags;
        
        for (Iterator<String> iterator = tags.keys(); iterator.hasNext(); ) {
            String key = iterator.next();
            map.put(key, tags.get(key));
        }
        message.payload.put("tags", map);
    }
    
    
    /** 
     *  Check desired image output encoding format.
     */
    private String getImageFormat () {
        String format           = "JPEG"; // default format.
        if (!params.containsKey("imageFormat"))
            return format;
        
        String requestedFormat  = (String) params.get("imageFormat");
        requestedFormat = requestedFormat.toUpperCase();
        if (allowedFormats.contains(requestedFormat))
            format = requestedFormat;
        return format;
    }
}
