package com.webprojectpattern.eshop.system.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int count = inputStream.read(buffer);
                if (count == -1) {
                    break;
                }
                output.write(buffer, 0, count);
            }
            output.flush();
            byte[] bytes = output.toByteArray();
            return bytes;
        } finally {
            inputStream.close();
            output.close();
        }
    }
}
