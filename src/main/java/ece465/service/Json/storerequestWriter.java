package ece465.service.Json;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.ArrayList;

public class storerequestWriter {//json for action and filenames (filenames/directories to be stored into the db)
    public static String generateJson(ArrayList<String> filename){
        JsonObject obj = Json.createObjectBuilder()
                .add("action", Json.createObjectBuilder()
                        .add("action", "add"))
                .add("parameter", Json.createObjectBuilder()
                        .add("filename", Json.createArrayBuilder(filename))).build();
        return obj.toString();
    }
}
