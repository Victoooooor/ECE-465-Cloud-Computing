package ece465.handler.multi;
import java.io.File;
import java.util.*;
public class pipeline {
    public pipeline(){}
    private final List<String> fpipe=new ArrayList<String>();
    private long len=0;
    private long pos=0;
    public synchronized void queue(String inFile){
        while (len-pos>50) {
            try {
                fpipe.forEach(path->System.out.println(path));
                wait();
            } catch (InterruptedException e) {}
        }
        len++;
        fpipe.add(inFile);
        notifyAll();
        return;
    }
    public synchronized String fetch(){
        while(pos>=len){
            try{
                wait();
            }catch(InterruptedException e) {}
        }
        notifyAll();
        if(fpipe.get((int) pos)=="")    return fpipe.get((int) pos);
        return fpipe.get((int) pos++);
    }
}
