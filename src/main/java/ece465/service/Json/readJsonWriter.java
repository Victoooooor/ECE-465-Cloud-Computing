package ece465.service.Json;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.UUID;

public class readJsonWriter {
    public static String generateJson(ArrayList<readJson.returnInfo> tostring){
        String act=null;
        switch (tostring.get(0).action){
            case 0:
                act="search";
                break;
            case 1:
                act="search_return";
                break;
            case 2:
                act="fetch";
                break;
            case 3:
                act="add";
                break;
        }
        ArrayList<String> fname = new ArrayList<>();
        ArrayList<Integer> fid = new ArrayList<>();
        ArrayList<String> hash = new ArrayList<>();
        ArrayList<String> ips = new ArrayList<>();
        ArrayList<Integer> ports = new ArrayList<>();

        tostring.forEach(f->{
            fname.add(f.filename);
            fid.add(f.fid);
            hash.add(f.hash);
            ips.add(f.ip);
            ports.add(f.port);
        });
        JsonObject obj = Json.createObjectBuilder()
                .add("requestID", UUID.randomUUID().toString())
                .add("action", Json.createObjectBuilder()
                        .add("action", act))
                .add("parameter", Json.createObjectBuilder()
                        .add("filename", Json.createArrayBuilder(fname))
                        .add("fid", Json.createArrayBuilder(fid))
                        .add("hash", Json.createArrayBuilder(hash))
                        .add("ip",Json.createArrayBuilder(ips))
                        .add("port",Json.createArrayBuilder(ports))).build();
        return obj.toString();
    }
}
