package ece465.service.Json;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class readJson {
    public ArrayList<returnInfo> read(String inputJson){
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
            Object[] fid = paramObj.getJsonArray("fid").toArray();
            ArrayList<Object> fids = new ArrayList<>(Arrays.asList(fid));

            Object[] hash = paramObj.getJsonArray("hash").toArray();
            ArrayList<Object> hashes = new ArrayList<>(Arrays.asList(hash));

            if(fids.size() != hashes.size()){
                System.out.println("Error!");
                return null;
            } else{
                for(int i=0; i< fids.size(); i++){
                    returnInfo r = new returnInfo(action, fids.get(i), hashes.get(i));
                    result.add(r);
                    r.pprint();
                }
                return result;
            }
        }



        for (String key : paramObj.keySet()) {
            param.add(paramObj.getString(key));
        }
        return null;

    }

    public class returnInfo{
        int action;
        String filename;
        Integer fid;
        String hash;
        public returnInfo(String act, ArrayList<String> param){
            if(act.equals("retrieve")){
                this.action = 1;
                this.filename = param.remove(0);
            }
        }
        public returnInfo(String act, Object fid, Object hash){
            if(act.equals("retrieve return")){
                this.action = 2;
                this.fid = fid;
                this.hash = (String) hash;
            }

        }
        public void pprint(){
            System.out.println("fid: " + this.fid + "\thash: " + this.hash);
        }
    }
}
