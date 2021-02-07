package ece465.handler.multi;

import ece465.handler.single.fetch;
import ece465.util.DBconnection;
import ece465.util.fileInfo;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentLinkedQueue;

public class fetch_multi implements Runnable{

    Logger LOG =Logger.getLogger(fetch_multi.class);

    private DBconnection con;
    private ConcurrentLinkedQueue<fileInfo> tofetch;
    private fileInfo current;

    public fetch_multi(DBconnection con_in,ConcurrentLinkedQueue<fileInfo> result){
        LOG.debug("fetch_multi CONSTRUCTOR");
        con=con_in;
        tofetch=result;
        LOG.debug("fetch_multi CONSTRUCTOR - DONE");
    }
    @Override
    public void run() {
        fetch FF=new fetch(con);
        while((current=tofetch.poll())!=null){
            System.out.println(Thread.currentThread().getId()+": "+current.getFilename());
            FF.getfile(current.getFid());
        }
    }
}
