package flatland.io.core;

public interface OutputStreamable {
    public void write(byte[] b, int off, int len);

    public void flush();

    public void close();
}
