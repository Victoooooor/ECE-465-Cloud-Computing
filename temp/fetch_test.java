package ece465;
import ece465.handler.multi.pipeline;
import ece465.handler.multi.store_consumer;
import ece465.handler.multi.store_producer;
import ece465.handler.single.fetch;
import ece465.handler.single.store;
import ece465.util.DBconnection;

public class fetch_test {
    public static void main(String[] args) {
        DBconnection DB_con=new DBconnection();
        fetch FF=new fetch(DB_con);
        FF.getfile(1666);

    }
}
