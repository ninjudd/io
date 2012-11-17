package flatland.io.core;

public interface InputStreamable {
    public int read(byte[] b, int off, int len);

    public int available();

    public long skip(long n);

    public void close();
}
