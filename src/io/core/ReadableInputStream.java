package io.core;

import java.io.InputStream;

public class ReadableInputStream extends InputStream{
    private IReadable reader;

    public ReadableInputStream( IReadable r){
        this.reader = r;
    }

    public boolean markSupported(){
        return false;
    }

    public int read(){
        byte[] b = new byte[1];
        int bytesRead = reader.read(b, 0, 1);

        if(-1 == bytesRead){
            return -1;
        } else{
            return b[0];
        }
    }

    public int read(byte[] b){
        return reader.read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len){
        return reader.read(b, off, len);
    }

    public int available(){
        return reader.available();
    }

    public void close(){
        reader.close();
    }

    public long skip(long n){
        return reader.skip(n);
    }
}