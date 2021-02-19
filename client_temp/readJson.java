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

        if(action.equals("search")){
            String filename = paramObj.getString("filename");
            returnInfo r = new returnInfo(action, filename);
            result.add(r);
            r.pprint0();
            return result;
        } else if(action.equals("search_return")){
            JsonArray fname = paramObj.getJsonArray("filename");
            JsonArray fid = paramObj.getJsonArray("fid");
            JsonArray hash = paramObj.getJsonArray("hash");
            JsonArray ip = paramObj.getJsonArray("ip");
            JsonArray port = paramObj.getJsonArray("port");
            if(fid.size() != hash.size()){
                System.out.println("Error!");
                return null;
            } else{
                for (int i = 0; i < fid.size(); i++) {
                    returnInfo r = new returnInfo(action, fname.getString(i), fid.getInt(i), hash.getString(i),ip.getString(i),port.getInt(i));
                    result.add(r);
                    r.pprint1();
                }
                return result;
            }
        }else if(action.equals("fetch")){
            Integer fid = paramObj.getInt("fid");
            returnInfo r = new returnInfo(action, fid);
            result.add(r);
            return result;
        }else if(action.equals("add")){
            JsonArray fname = paramObj.getJsonArray("filename");
            for(int i=0;i< fname.size();i++){
                System.out.println("String name:::: "+ fname.getString(i));
                result.add(new returnInfo(action,fname.getString(i)));
            }
            return result;
        }
        return null;
    }

    public static class returnInfo{
        public int action;
        public String filename;
        public Integer fid;
        public String hash;
        public String ip;
        public Integer port;
        public returnInfo(String act, String filename){
            if(act.equals("search")){
                this.action = 0;
                this.filename = filename;
            }
            else if(act.equals("add")){
                this.action=3;
                this.filename=filename;
            }
        }
        public returnInfo(String act, String fname, int fid, String hash,String ip, Integer port){
            if(act.equals("search_return")){
                this.action = 1;
                this.filename=fname;
                this.fid = fid;
                this.hash = hash;
                this.ip=ip;
                this.port=port;
            }

        }
        public returnInfo(String act, int fid){
            if(act.equals("fetch")){
                this.action=2;
                this.fid=fid;
            }
        }
        public void pprint0() { System.out.println("filename: " + filename); }
        public void pprint1(){
            System.out.println("fid: " + this.fid + "\thash: " + this.hash);
        }
    }
}
