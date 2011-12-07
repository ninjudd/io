package io.core;

public class InputStream extends java.io.InputStream {
    final InputStreamable in;

    public InputStream(InputStreamable in){
        this.in = in;
    }

    public boolean markSupported(){
        return false;
    }

    public int read(){
        byte[] b = new byte[1];
        int bytesRead = in.read(b, 0, 1);

        if(-1 == bytesRead){
            return -1;
        } else{
            return b[0];
        }
    }

    public int read(byte[] b){
        return in.read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len){
        return in.read(b, off, len);
    }

    public int available(){
        return in.available();
    }

    public long skip(long n){
        return in.skip(n);
    }

    public void close(){
        in.close();
    }
}