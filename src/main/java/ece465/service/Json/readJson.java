package ece465.service.Json;

import javax.json.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class readJson {
    public static ArrayList<returnInfo> read(String inputJson){
        ArrayList<returnInfo> result = new ArrayList<>();

        JsonReader jsonReader = Json.createReader(new StringReader(inputJson));
        JsonObject obj = jsonReader.readObject();

        JsonObject actionObj = obj.getJsonObject("action");
        JsonObject paramObj = obj.getJsonObject("parameter");

        String action = actionObj.getString("action");

        ArrayList<String> param = new ArrayList<>();

        if(action.equals("retrieve")){
            String filename = paramObj.getString("filename");
            param.add(filename);
            returnInfo r = new returnInfo(action, param);
            result.add(r);
            return result;
        } else if(action.equals("retrieve return")){
            //ArrayList<Integer> fids = new ArrayList<>();
            //ArrayList<Object> hashes = new ArrayList<>();

            JsonArray fid = paramObj.getJsonArray("fid");
            JsonArray hash = paramObj.getJsonArray("hash");

            if(fid.size() != hash.size()){
                System.out.println("Error!");
                return null;
            } else{
                for (int i = 0; i < fid.size(); i++) {
                    //fids.add(fid.getInt(i));
                    //hashes.add(hash.getString(i));
                    returnInfo r = new returnInfo(action, fid.getInt(i), hash.getString(i));
                    result.add(r);
                    r.pprint();
                }
                return result;
            }
        }


        /*
        for (String key : paramObj.keySet()) {
            param.add(paramObj.getString(key));
        }
         */
        return null;
    }

    public static class returnInfo{
        public int action;
        public String filename;
        public Integer fid;
        public String hash;
        public returnInfo(String act, ArrayList<String> param){
            if(act.equals("retrieve")){
                this.action = 0;
                this.filename = param.remove(0);
            }
        }
        public returnInfo(String act, int fid, String hash){
            if(act.equals("retrieve return")){
                this.action = 1;
                this.fid = fid;
                this.hash = hash;
            }

        }
        public void pprint(){
            System.out.println("fid: " + this.fid + "\thash: " + this.hash);
        }
    }
}
