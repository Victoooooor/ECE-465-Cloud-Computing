package ece465.service.Json;

import ece465.handler.single.retrieve;
import ece465.util.fileInfo;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class retrieveReturnJsonWriter {
    public static String generateJson(ArrayList<fileInfo> input){
        ArrayList<Integer> fid = new ArrayList<>();
        ArrayList<String> hash = new ArrayList<>();
        for(fileInfo f : input){
            fid.add(f.getFid());
            hash.add(f.getHash());
        }

        JsonObject obj = Json.createObjectBuilder()
                .add("action", Json.createObjectBuilder()
                        .add("action", "retrieve return"))
                .add("parameter", Json.createObjectBuilder()
                        .add("fid", Json.createArrayBuilder(fid))
                        .add("hash", Json.createArrayBuilder(hash))).build();
        return obj.toString();
    }
}