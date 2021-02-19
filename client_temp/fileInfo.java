package ece465.util;

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

