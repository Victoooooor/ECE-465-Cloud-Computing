package ece465.util;

public class fileInfo {
    private final int fid;
    private final String filename;

    public fileInfo(int fid, String filename){
        this.fid = fid;
        this.filename = filename;
    }

    public int getFid() {
        return fid;
    }

    public String getFilename() {
        return filename;
    }
}

