package ece465.handler.single;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class hash {
    private final int buffersize=8*1024;
    private BufferedInputStream BuffIn;
    private int count;
    public String gethash(String fname){

        byte[] buffer= new byte[buffersize];

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BuffIn = null;
        try {
            BuffIn = new BufferedInputStream(new FileInputStream(fname));
            while ((count = BuffIn.read(buffer)) > 0) {
                digest.update(buffer, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                BuffIn.close();
            } catch (IOException e) {}
        }
        byte[] hash = digest.digest();
        StringBuilder str = new StringBuilder();
        for (byte b : hash) {
            str.append(String.format("%02x", b));
        }
        return str.toString();
    }
}
