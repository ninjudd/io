package io.core;

public interface IReadable {
    public int read(byte[] b, int off, int len);

    public void close();

    public int available();

    public long skip(long n);
}
