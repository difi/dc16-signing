package no.difi.signing.digipost;

import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.asice.DumpDocumentBundleToDisk;
import no.digipost.signature.client.core.SignatureJob;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class AsiceDumper {

    /**
     * Dumps a zip file of the document bundle to disk. Only for testing purposes, not used for anything.
     *
     * @param documentBundle
     * @param signatureJob
     * @throws IOException
     */
    public static boolean dumper(DocumentBundle documentBundle, SignatureJob signatureJob) {

        Path path = FileSystems.getDefault().getPath("", "");
        DumpDocumentBundleToDisk dumper = new DumpDocumentBundleToDisk(path);
        InputStream asiceInputStream = documentBundle.getInputStream();
        try {
            dumper.process(signatureJob, asiceInputStream);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}
