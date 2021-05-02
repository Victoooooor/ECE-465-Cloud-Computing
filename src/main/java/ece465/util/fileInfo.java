package ece465.util;
/*
public class fileInfo {
    private final int fid;
    private final String filename;
    private String hash;

    public fileInfo(int fid, String filename){
        this.fid = fid;
        this.filename = filename;
    }

    public void setHash(String hash){ this.hash = hash; }

    public int getFid() {
        return fid;
    }

    public String getFilename() {
        return filename;
    }

    public String getHash() { return hash; }
}
*/
public class fileInfo{
    private final int fid;
    private final String filename;
    private String hash;
    private String IP;
    private int port;

    public fileInfo(int fid, String filename){
        this.fid = fid;
        this.filename = filename;
    }

    public fileInfo(int fid, String filename, String hash){
        this.fid = fid;
        this.filename = filename;
        this.hash = hash;
    }

    public fileInfo(int fid, String filename, String IP, int port){
        this.fid = fid;
        this.filename = filename;
        this.IP = IP;
        this.port = port;
    }

    public fileInfo(int fid, String filename, String hash, String IP, int port){
        this.fid = fid;
        this.filename = filename;
        this.hash = hash;
        this.IP = IP;
        this.port = port;
    }

    public void setIP(String IP){ this.IP = IP; }

    public void setPort(int port){ this.port = port; }

    public void setHash(String hash){ this.hash = hash; }

    public int getFid() { return fid; }

    public String getFilename() { return filename; }

    public String getHash() { return hash; }

    public String getIP() { return IP; }

    public int getPort() { return port; }
}
