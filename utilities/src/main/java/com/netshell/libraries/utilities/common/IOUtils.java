package com.netshell.libraries.utilities.common;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Abhishek
 * @since 12/6/2016.
 */
public final class IOUtils {

    /**
     * Serializes a {@link Serializable} object into an array of bytes
     *
     * @param serializable object to be serialized.
     * @param <T>          type of object
     * @return bytes representing the serialized object
     * @throws IOException exception is thrown by underlying stream
     */
    public static <T extends Serializable> byte[] toObjectBytes(final T serializable) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeObject(serializable);
        }
        return out.toByteArray();
    }

    /**
     * Read a serialized object of type {@code tClass} from {@code bytes}
     * It also supports classloading from {@link Thread#contextClassLoader}
     *
     * @param bytes  array representing serialized object.
     * @param tClass class type of object
     * @param <T>    type of object
     * @return deserialize object of type {@code tClass}
     * @throws IOException            exception raised by underlying InputStream
     * @throws ClassNotFoundException exception is raised if the specified class is not found
     */
    public static <T extends Serializable> T toBytesObject(final byte[] bytes, final Class<T> tClass) throws IOException, ClassNotFoundException {
        final ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        Object o;
        try (final ObjectInput ois = new ContextObjectInputStream(in)) {
            o = ois.readObject();
        }
        return tClass.cast(o);
    }

    /**
     * Copies the content from {@code inputStream} to {@code outputStream}.
     * Although it works on stream it neither opens nor closes them.
     * This should be handled by callers of this function/
     *
     * @param inputStream  stream to read content from
     * @param outputStream stream to write content to
     * @throws IOException exception thrown by either stream.
     */
    public static void inputToOutputStream(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[2048];
        int len = 0, offset = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, offset, len);
            offset += len;
        }
        outputStream.flush();
    }

    public static byte[] compress(final byte[] bytes) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (final GZIPOutputStream os = new GZIPOutputStream(out)) {
            os.write(bytes);
            os.flush();
        }
        return out.toByteArray();
    }

    public static byte[] decompress(final byte[] compressedBytes) throws IOException {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (GZIPInputStream zip = new GZIPInputStream(new ByteArrayInputStream(compressedBytes))) {
            inputToOutputStream(zip, os);
        }
        return os.toByteArray();
    }

    /**
     *
     */
    private static final class ContextObjectInputStream extends ObjectInputStream {

        ContextObjectInputStream(final InputStream in) throws IOException {
            super(in);
        }

        @Override
        protected Class<?> resolveClass(final ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            try {
                return super.resolveClass(desc);
            } catch (final ClassNotFoundException e) {
                return Thread.currentThread().getContextClassLoader().loadClass(desc.getName());
            }
        }
    }
}
