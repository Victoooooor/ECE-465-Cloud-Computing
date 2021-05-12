package ece465.service.Json;

import javax.json.Json;
import javax.json.JsonObject;

public class confirmationMsgJsonWriter {
    public static String generateJson(String ip, Integer port){
        JsonObject obj = Json.createObjectBuilder()
                .add("action", Json.createObjectBuilder()
                        .add("action", "confirmation_msg"))
                .add("parameter", Json.createObjectBuilder()
                        .add("ip", ip))
                .add("port", port).build();
        return obj.toString();
    }
}
