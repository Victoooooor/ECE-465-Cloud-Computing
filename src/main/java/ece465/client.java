package ece465;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ece465.handler.multi.getHash;
import ece465.handler.single.fetch;
import ece465.handler.single.retrieve;
import ece465.handler.single.store;
import ece465.node.*;
import ece465.node.server;
import ece465.service.Json.*;
import ece465.util.DBconnection;
import ece465.util.fileInfo;
import io.norberg.automatter.gson.AutoMatterTypeAdapterFactory;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.awt.print.PrinterGraphics;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.initExceptionHandler;
import static spark.Spark.options;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import spark.ResponseTransformer;

import static spark.Spark.*;

public class client {
    public static void main(String[] args) {
        ece465.node.client c= null;
        c = new ece465.node.client();
        boolean exit = false;
        staticFiles.location("/public"); // Static files

        Gson gson =
                new GsonBuilder().registerTypeAdapterFactory(new AutoMatterTypeAdapterFactory()).create();

        ResponseTransformer responseTransformer =
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

        client_handler handler = new client_handler(gson);

        System.out.println("in run");

        get("/ping", (req, res) -> "OK");
        get("/hi", handler::hi, gson::toJson);

        ece465.node.client finalC = c;
        get("/search/:filename", (req, res) -> handler.search(req, res, finalC), gson::toJson);
        get("/fetch/:fid", handler::fetch, gson::toJson);
        post("/upload/:filename", handler::upload, gson::toJson);

//        String search_result = null;
//        Scanner sc = new Scanner(System.in);
//
//        System.out.println("Please select an action to perform: ");
//        System.out.println("1 - Store files to database.");
//        System.out.println("2 - Search files from database");
//        System.out.println("3 = Exit");
//
//        Integer option = sc.nextInt();
//
//        switch (option){
//            case 1:
//                System.out.println("Please enter pathname for the files you want to store, " +
//                        "separated by newline character (one file per line), press enter again to finish.");
//                ArrayList<String> listing=new ArrayList<>();
//                listing.clear();
//                String fp = null;
//                fp = sc.nextLine();
//                while(!(fp = sc.nextLine()).isBlank()) {
//                    listing.add(fp);
//                }
//                System.out.println(listing);
//                try {
//                    c.send(new Socket("0.0.0.0", 4666), storerequestWriter.generateJson(listing));
//                } catch (IOException e){
//                    e.printStackTrace();
//                }
//                break;
//            case 2:
//                Scanner scanner = new Scanner(System.in);
//                System.out.println("Please enter a search word: ");
//                String searchword = scanner.nextLine();
//                System.out.println(searchword);
//                try {
//                    search_result = c.send(new Socket("0.0.0.0", 4666), searchJsonWriter.generateJson(searchword));
//                    System.out.println("search result: " + search_result);
//                    ArrayList<readJson.returnInfo> returned = readJson.read(search_result);
//
//                    System.out.println("Pleas select the files you want to retrieve, " +
//                            "separated by newline character (one file per line), press enter again to finish.");
//                    for (int i = 0; i < returned.size(); i++) {
//                        System.out.println((i + 1) + " - " + returned.get(i).filename);
//                    }
//                    String fn;
//                    ArrayList<readJson.returnInfo> fetching = new ArrayList<>();
//                    fn = scanner.nextLine();
//                    int n = Integer.parseInt(fn);
//                    fetching.add(returned.get(n-1));
//                    while (!(fn = scanner.nextLine()).isBlank()) {
//                        n = Integer.parseInt(fn);
//                        fetching.add(returned.get(n - 1));
//                    }
//                    for(readJson.returnInfo a: fetching){
//                        System.out.println(a.filename);
//                    }
//                    c.receive(fetching, 0);
//                } catch (IOException e){
//                    e.printStackTrace();
//                }
//                break;
//            case 3:
//                exit = true;
//                break;
//        }
            /*
            try {
                //ArrayList<String> listing=new ArrayList<>();
                //listing.add(".\\");
                //c.send(new Socket("0.0.0.0",4666), storerequestWriter.generateJson(listing));
                search_result = c.send(new Socket("0.0.0.0", 4666), searchJsonWriter.generateJson(".j"));
                System.out.println("search result: " + search_result);
                ArrayList<readJson.returnInfo> returned = readJson.read(search_result);
                c.receive(returned, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
             */
    }
    private static class client_handler{
        private final Gson gson;
        private DBconnection DB_con;
        private retrieve RT;
        private ConcurrentLinkedQueue<fileInfo> result;
        private getHash HAS;
        private peerlist peers;
        public client_handler(final Gson gson){
            this.gson = gson;

            this.DB_con = new DBconnection();
            this.RT = new retrieve(DB_con);
            this.HAS = new getHash(DB_con);
            this.peers=new peerlist();
        }

        public String hi(Request request, Response response) {
            System.out.println(request.toString());
            return "hi";
        }

        public List<readJson.returnInfo> search(Request request, Response response, ece465.node.client c){
            String filename = request.params(":filename");
            System.out.println("Search file: " + filename);
            ArrayList<readJson.returnInfo> returned = new ArrayList<>();
            String search_result = null;

            try {
                search_result = c.send(new Socket("0.0.0.0", 4666), searchJsonWriter.generateJson(filename));
                System.out.println("search result: " + search_result);
                returned = readJson.read(search_result);
            } catch (IOException e){
                e.printStackTrace();
            }

            return returned;
        }

        public String upload(Request request, Response response) throws IOException, ServletException, SQLException {
            System.out.println("upload called");
            MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
            request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
            Part file = request.raw().getPart("file"); //file is name of the upload form
            store st = new store(DB_con);
            st.store(request.params("filename"),file.getInputStream());

            response.header("data", "okk");
            response.body("alright");

            return "OK";
        }

        public OutputStream fetch(Request request, Response response){

            return null;
        }
    }
}