package ece465.service.Json;

import ece465.util.fileInfo;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.UUID;

public class fetchJsonWriter {
    public static String generateJson(readJson.returnInfo input){

        JsonObject obj = Json.createObjectBuilder()
                .add("requestID", UUID.randomUUID().toString())
                .add("action", Json.createObjectBuilder()
                        .add("action", "fetch"))
                .add("parameter", Json.createObjectBuilder()
                        .add("fid", input.fid)).build();
        return obj.toString();
    }
}