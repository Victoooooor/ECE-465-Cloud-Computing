package ece465.service.Json;

import ece465.handler.single.retrieve;
import ece465.util.fileInfo;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class searchReturnJsonWriter {
    public static String generateJson(ArrayList<fileInfo> input,String ip,Integer port){
        ArrayList<String> fname = new ArrayList<>();
        ArrayList<Integer> fid = new ArrayList<>();
        ArrayList<String> hash = new ArrayList<>();
        ArrayList<String> ips = new ArrayList<>();
        ArrayList<Integer> ports = new ArrayList<>();
        for(fileInfo f : input){

        }
        input.forEach(f->{
            fname.add(f.getFilename());
            fid.add(f.getFid());
            hash.add(f.getHash());
            ips.add(ip);
            ports.add(port);
        });
        JsonObject obj = Json.createObjectBuilder()
                .add("action", Json.createObjectBuilder()
                        .add("action", "search_return"))
                .add("parameter", Json.createObjectBuilder()
                        .add("filename", Json.createArrayBuilder(fname))
                        .add("fid", Json.createArrayBuilder(fid))
                        .add("hash", Json.createArrayBuilder(hash))
                        .add("ip",Json.createArrayBuilder(ips))
                        .add("port",Json.createArrayBuilder(ports))).build();
        return obj.toString();
    }
}
