package no.difi.signing.api;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;

/**
 * Document instance containing metadata and access to input stream containing document for signing.
 */
public interface Document {

    String getToken();

    String getTitle();

    String getVersion();

    /**
     * Document instance for signing.
     */
    InputStream getInputStream() throws IOException;

    default byte[] getByteArray() throws IOException {
        try (InputStream inputStream = getInputStream()) {
            return ByteStreams.toByteArray(inputStream);
        }
    }
}
