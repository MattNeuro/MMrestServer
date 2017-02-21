package se.karolinska.corticostriatal;

import com.google.gson.internal.LinkedTreeMap;

/**
 *  Message object. This serves as a base class for the JSON conversion; public
 *  fields added to this class' payload will automatically be converted into
 *  JSON data and send along.
 * 
 *  @author Matthijs
 */
public class Message {
 
    public String           error   = null;
    public String           status;
    public LinkedTreeMap    payload;
   
    
    public Message (String status) {
        this.status     = status;
        this.payload    = new LinkedTreeMap();
    }
}