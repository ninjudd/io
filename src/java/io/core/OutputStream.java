package io.core;

public class OutputStream extends java.io.OutputStream {
    final OutputStreamable out;

    public OutputStream(OutputStreamable out){
        this.out = out;
    }

    public void write(int i){
        byte[] b = new byte[1];
        b[0] = (byte)i;
        out.write(b, 0, 1);
    }

    public void write(byte[] b){
        out.write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len){
        out.write(b, off, len);
    }

    public void flush(){
        out.flush();
    }

    public void close(){
        out.close();
    }
}