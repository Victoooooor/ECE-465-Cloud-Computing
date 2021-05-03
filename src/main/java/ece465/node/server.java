package ece465.node;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ece465.handler.multi.getHash;
import ece465.handler.single.retrieve;
import ece465.service.Json.*;
import ece465.service.threaded_store;
import ece465.util.DBconnection;
import ece465.util.fileInfo;
import ece465.handler.single.fetch;

import spark.Spark.*;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.initExceptionHandler;
import static spark.Spark.options;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;

import io.norberg.automatter.AutoMatter;
import io.norberg.automatter.gson.AutoMatterTypeAdapterFactory;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

public class server {
    private static ServerSocket server;
    private DBconnection DB_con;
    private retrieve RT;
    private ConcurrentLinkedQueue<fileInfo> result;
    private getHash HAS;
    private peerlist peers;
    private Gson gson;
    ResponseTransformer responseTransformer;

    public server(int portnum){
        try {
            server = new ServerSocket(portnum);
            DB_con = new DBconnection();
            RT = new retrieve(DB_con);
            HAS = new getHash(DB_con);
            peers=new peerlist();
        } catch (IOException e) {
            System.err.println("Server port non available: "+portnum);
            e.printStackTrace();
        }
    }

    private class client_handler implements Runnable{
        private Socket client;
        private DataInputStream in = null;
        private DataOutputStream out = null;
        private Gson gson;
        private request_handler handler;

        public client_handler(Socket client, Gson gson){
            this.client=client;
            this.gson = gson;
            this.handler = new request_handler(gson);
        }
        @Override
        public void run() {

            System.out.println("in run");


            get("/ping", (req, res) -> "OK");
            get("/hi", (req, res) -> this.handler.hi(req, res), gson::toJson);

            get("/search/:filename", (req, res) -> this.handler.search(req, res), gson::toJson);
            get("/fetch/:filename", (req, res) -> "fetch called");
            post("/upload", (req, res) -> this.handler.upload(req, res), gson::toJson);


//            try{
//                in = new DataInputStream(new BufferedInputStream(client.getInputStream()));
//                out = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
//            } catch (IOException e) {
//                System.err.println("client get Stream failed");
//                System.exit(-1);
//            }
//            try {
//                System.out.println("reading");
//                String fromclient=in.readUTF();
//                System.out.println("From Client text: "+ fromclient);
//                ArrayList<readJson.returnInfo> read = readJson.read(fromclient);
//                allread:for(readJson.returnInfo Info:read) {
//                    System.out.println(Info.action);
//                    switch (Info.action) {
//                        case 0://search by filename
//                            System.out.println("Search file: " + Info.filename);
//                            try {
//                                RT.startSearch(Info.filename, 0);
//                                result = RT.getResult();
//                                System.out.println("Search Done");
//                                ArrayList<String> netresult=peers.broadcast(fromclient,0);
//                                ArrayList<readJson.returnInfo> fromnet=new ArrayList<>();
//                                netresult.forEach(f->{
//                                    fromnet.addAll(readJson.read(f));
//                                });
//                                String fromlocal=searchReturnJsonWriter.generateJson(HAS.get(result), server.getInetAddress().toString().split("/")[1], server.getLocalPort());
//                                fromnet.addAll(readJson.read(fromlocal));
//                                out.writeUTF(readJsonWriter.generateJson(fromnet));
//                                out.flush();
//                            } catch (Exception e) {
//                                System.err.println("Server Processing Search error");
//                                e.printStackTrace();
//                            }
//                            break;
//                        case 1://only client
//                            ;
//                            break;
//                        case 2://fetch
//                            System.out.println("Fetching: ");
//                            fetch ft = new fetch(DB_con);
//                            ft.getfile(Info.fid, out);
//                            break;
//                        case 3://save to db
//                            threaded_store.store(DB_con, read, 0);
//                            out.writeUTF("Write to DB done");
//                            out.flush();
//                            break allread;
//                        default:
//                            ;
//                            break;
//                    }
//                    System.out.println("Done processing request");
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                in.close();
//                out.close();
//                client.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        public class request_handler{
            private final Gson gson;
            public request_handler(final Gson gson){
                this.gson = gson;
            }

            public String hi(Request request, Response response) {
                System.out.println(request.toString());
                String hi = "hi";
                return hi;
            }

            public List<readJson.returnInfo> search(Request request, Response response){
                String filename = request.params(":filename");
                System.out.println("Search file: " + filename);
                ArrayList<readJson.returnInfo> fromnet=new ArrayList<>();
                try {
                    RT.startSearch(filename, 0);
                    result = RT.getResult();
                    System.out.println("Search Done");
                    ArrayList<String> netresult=peers.broadcast(request.toString(),0);
                    //ArrayList<readJson.returnInfo> fromnet=new ArrayList<>();
                    netresult.forEach(f->{
                        fromnet.addAll(readJson.read(f));
                    });
                    String fromlocal=searchReturnJsonWriter.generateJson(HAS.get(result), server.getInetAddress().toString().split("/")[1], server.getLocalPort());
                    fromnet.addAll(readJson.read(fromlocal));
//                    out.writeUTF(readJsonWriter.generateJson(fromnet));
//                    out.flush();
                } catch (Exception e) {
                    System.err.println("Server Processing Search error");
                    e.printStackTrace();
                }
                return fromnet;
            }

            public String upload(Request request, Response response) throws IOException, ServletException {
                System.out.println("upload called");
                MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
                request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
                Part file = request.raw().getPart("file"); //file is name of the upload form

                response.header("data", "okk");
                response.body("alright");

                return "OK";
            }
        }
    }

    public void start(){
        staticFiles.location("/public"); // Static files

        this.gson =
                new GsonBuilder().registerTypeAdapterFactory(new AutoMatterTypeAdapterFactory()).create();

        this.responseTransformer =
                model -> {
                    if (model == null){
                        return "";
                    }
                    return gson.toJson(model);
                };

        initExceptionHandler(Throwable::printStackTrace);
        options(
                "/*",
                (request, response) -> {
                    String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers", "*");
                    }

                    String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
                        response.header("Access-Control-Allow-Methods", "*");
                    }

                    return "OK";
                });

        before(
                (req, res) -> {
                    res.header("Access-Control-Allow-Origin", "*");
                    res.header("Access-Control-Allow-Headers", "*");
                    res.type("application/json");
                });
        while(true){
            client_handler ch;
            try {
                System.out.println("waiting");
                ch=new client_handler(server.accept(), this.gson);
                Thread T=new Thread(ch);
                T.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void stop(){
        try {
             server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}