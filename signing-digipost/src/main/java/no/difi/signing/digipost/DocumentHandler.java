package no.difi.signing.digipost;

import com.google.common.io.ByteStreams;
import no.digipost.signature.client.direct.DirectDocument;
import no.digipost.signature.client.portal.PortalDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DocumentHandler {


    /**
     * Returns a DirectDocument from a given pdf
     *
     * @param pdfPath Path to pdf
     */
    public static DirectDocument pdfToDirectDocument(String pdfPath) throws IOException {

        try (FileInputStream inputStream = new FileInputStream(pdfPath)) {
            return DirectDocument.builder("Subject", "document.pdf", ByteStreams.toByteArray(inputStream)).build();
        }
    }

    public static PortalDocument pdfToPortalDocument(String pdfPath) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(pdfPath)) {
            return PortalDocument.builder("Subject", "document.pdf", ByteStreams.toByteArray(inputStream)).build();
        }
    }

    /**
     * @return StringBuilder-object which is used to find the path to "dokumentTilSignering"
     */
    public static StringBuilder setAbsolutePathToPDF(File dokumentTilSignering) {
        StringBuilder stringBuilder = new StringBuilder(dokumentTilSignering.getAbsolutePath());

        for (int i = 0; i < stringBuilder.length(); i++) {
            if (stringBuilder.charAt(i) == '%') {
                stringBuilder.replace(i, i + 3, " ");
            }
        }
        return stringBuilder;
    }


}
