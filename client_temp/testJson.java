package ece465;

import ece465.service.Json.readJson;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.ArrayList;

public class testJson {
    public static void main(String[] args) {
        /*
        retrieveJsonWriter s = new retrieveJsonWriter();
        String json = s.generateJson("hi");
        System.out.println(json);
        readJson r = new readJson();
        r.read(json);
         */
        ArrayList<Integer> fid = new ArrayList<>();
        fid.add(1); fid.add(2); fid.add(3);
        ArrayList<String> hash = new ArrayList<>();
        hash.add("1"); hash.add("2"); hash.add("3");
        JsonObject obj = Json.createObjectBuilder()
                .add("action", Json.createObjectBuilder()
                        .add("action", "retrieve return"))
                .add("parameter", Json.createObjectBuilder()
                        .add("fid", Json.createArrayBuilder(fid))
                        .add("hash", Json.createArrayBuilder(hash))).build();
        System.out.println(obj.toString());
        readJson.read(obj.toString());
    }
}
