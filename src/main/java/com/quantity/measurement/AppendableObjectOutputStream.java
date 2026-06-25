package com.quantity.measurement;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream;

/**
 * Enhances ObjectOutputStream to append elements to files without writing duplicate headers.
 */
public class AppendableObjectOutputStream extends ObjectOutputStream {

    public AppendableObjectOutputStream(OutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        // Do not write a stream header to avoid corruption when appending to an existing stream
    }
}
