package ece465.handler.multi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class store_producer implements Runnable{
    private final pipeline mypipe;
    private final ArrayList<String> dirlist;
    public store_producer(ArrayList<String> dir,pipeline inpipe) {
        mypipe=inpipe;
        dirlist=dir;
    }

    @Override
    public void run() {
        dirlist.forEach(directory->{
            try (Stream<Path> walk = Files.walk(Paths.get(directory))) {
            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());
            result.forEach(fpath -> mypipe.queue(fpath));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        mypipe.queue("");

    }
}
