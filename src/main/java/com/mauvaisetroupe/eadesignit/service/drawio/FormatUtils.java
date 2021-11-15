package com.mauvaisetroupe.eadesignit.service.drawio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class FormatUtils {

    protected static final int IO_BUFFER_SIZE = 4 * 1024;

    /**
     * Applies a standard inflate algo to the input byte array
     * @param binary the byte array to inflate
     * @return the inflated String
     *
     */
    public static String inflate(byte[] binary) throws IOException {
        StringBuffer result = new StringBuffer();
        InputStream in = new InflaterInputStream(new ByteArrayInputStream(binary), new Inflater(true));

        while (in.available() != 0) {
            byte[] buffer = new byte[IO_BUFFER_SIZE];
            int len = in.read(buffer, 0, IO_BUFFER_SIZE);

            if (len <= 0) {
                break;
            }

            result.append(new String(buffer, 0, len));
        }

        in.close();

        return result.toString();
    }

    /**
     * Applies a standard deflate algo to the input String
     * @param inString the String to deflate
     * @return the deflated byte array
     *
     */
    public static byte[] deflate(String inString) throws IOException {
        Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION, true);
        byte[] inBytes = inString.getBytes("UTF-8");
        deflater.setInput(inBytes);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(inBytes.length);
        deflater.finish();
        byte[] buffer = new byte[IO_BUFFER_SIZE];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index
            outputStream.write(buffer, 0, count);
        }

        outputStream.close();
        byte[] output = outputStream.toByteArray();

        return output;
    }
}
