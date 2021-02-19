package ece465.service.Json;

import javax.json.Json;
import javax.json.JsonObject;

public class searchJsonWriter {
    public static String generateJson(String filename){
        JsonObject obj = Json.createObjectBuilder()
                .add("action", Json.createObjectBuilder()
                    .add("action", "search"))
                .add("parameter", Json.createObjectBuilder()
                    .add("filename", filename)).build();
        return obj.toString();
    }
}
