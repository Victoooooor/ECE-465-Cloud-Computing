package ece465.handler.multi;

import ece465.handler.single.fetch;
import ece465.util.DBconnection;
import ece465.util.fileInfo;

import java.util.concurrent.ConcurrentLinkedQueue;

public class fetch_multi implements Runnable{
private DBconnection con;
private ConcurrentLinkedQueue<fileInfo> tofetch;
private fileInfo current;
public fetch_multi(DBconnection con_in,ConcurrentLinkedQueue<fileInfo> result){
    con=con_in;
    tofetch=result;
}
    @Override
    public void run() {
        fetch FF=new fetch(con);
        while((current=tofetch.poll())!=null){
            System.out.println(current.getFilename());
        }
    }
}
