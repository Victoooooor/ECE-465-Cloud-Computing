package ece465.service.Json;

import javax.json.*;
import java.io.StringReader;
import java.util.ArrayList;

public class readJson {
    public static ArrayList<returnInfo> read(String inputJson){
        ArrayList<returnInfo> result = new ArrayList<>();

        JsonReader jsonReader = Json.createReader(new StringReader(inputJson));
        JsonObject obj = jsonReader.readObject();

        JsonObject actionObj = obj.getJsonObject("action");
        JsonObject paramObj = obj.getJsonObject("parameter");

        String action = actionObj.getString("action");

        if(action.equals("retrieve")){
            String filename = paramObj.getString("filename");
            returnInfo r = new returnInfo(action, filename);
            result.add(r);
            r.pprint0();
            return result;
        } else if(action.equals("retrieve return")){

            JsonArray fid = paramObj.getJsonArray("fid");
            JsonArray hash = paramObj.getJsonArray("hash");

            if(fid.size() != hash.size()){
                System.out.println("Error!");
                return null;
            } else{
                for (int i = 0; i < fid.size(); i++) {
                    returnInfo r = new returnInfo(action, fid.getInt(i), hash.getString(i));
                    result.add(r);
                    r.pprint1();
                }
                return result;
            }
        }
        return null;
    }

    public static class returnInfo{
        public int action;
        public String filename;
        public Integer fid;
        public String hash;
        public returnInfo(String act, String filename){
            if(act.equals("retrieve")){
                this.action = 0;
                this.filename = filename;
            }
        }
        public returnInfo(String act, int fid, String hash){
            if(act.equals("retrieve return")){
                this.action = 1;
                this.fid = fid;
                this.hash = hash;
            }

        }
        public void pprint0() { System.out.println("filename: " + filename); }
        public void pprint1(){
            System.out.println("fid: " + this.fid + "\thash: " + this.hash);
        }
    }
}
