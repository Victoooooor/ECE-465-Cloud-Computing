package ece465.handler.multi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class store_producer implements Runnable{
    private final pipeline mypipe;
    private final String directory;
    public store_producer(String dir,pipeline inpipe) {
        mypipe=inpipe;
        directory=dir;
    }

    @Override
    public void run() {
        try (Stream<Path> walk = Files.walk(Paths.get(directory))) {
            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());
            result.forEach(fpath -> mypipe.queue(fpath));
            mypipe.queue("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
