package ece465.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Comm {
    private final String IP;
    private final long hashBase;
    private Map<Integer, Boolean> pool;

    public Comm(String IP){
        this.IP = IP;
        pool = new HashMap<>();
        pool.clear();
        for(int i = 0; i < 100; i++){
            this.pool.put(i, false);
        }

        String[] ipAddressInArray = IP.split("\\.");

        long longIP = 0;
        for (int i = 0; i < ipAddressInArray.length; i++) {
            int power = 3 - i;
            int ip = Integer.parseInt(ipAddressInArray[i]);
            longIP += ip * Math.pow(256, power);
        }
        hashBase = longIP*100;
        System.out.println(longIP);
    }

    public long generateActionID(){
        while(true) {
            for(Map.Entry<Integer, Boolean> entry : this.pool.entrySet()) {
                if (!entry.getValue()) {
                    entry.setValue(true);
                    return (hashBase + entry.getKey());
                }
            }
            for(Map.Entry<Integer, Boolean> entry : this.pool.entrySet()) {
                entry.setValue(false);
            }
        }
    }

}
