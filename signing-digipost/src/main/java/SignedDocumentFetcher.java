import no.digipost.signature.client.direct.DirectClient;
import no.digipost.signature.client.direct.DirectJobStatus;
import no.digipost.signature.client.direct.DirectJobStatusResponse;

import java.io.*;

public class SignedDocumentFetcher {
    private DirectClient client;
    private DirectJobStatusResponse statusResponse;
    private StatusReader statusReader;

    SignedDocumentFetcher(DirectClient client, StatusReader statusReader){
        this.client = client;
        this.statusResponse = client.getStatusChange();
        this.statusReader = statusReader;

    }

    public InputStream getSignedDocuments(String format) {
        if(statusResponse.is(DirectJobStatus.SIGNED)){
            if(format == "xades"){
                return client.getXAdES(statusResponse.getxAdESUrl());
            } else if (format == "pades") {
                return client.getPAdES(statusResponse.getpAdESUrl());
            }
        }
        return null;
    }

    public String getPades() throws IOException{

    if (this.statusReader.getStatusResponse().is(this.statusReader.getStatusResponse().getStatus().SIGNED)) {

        InputStream pAdESStream = client.getPAdES(this.statusReader.getStatusResponse().getpAdESUrl());

        OutputStream outputStream = new FileOutputStream(System.getProperty("user.dir") + "pAdESTest.pdf");
        int read = 0;

        byte[] bytes = new byte[999999999];
        while ((read = pAdESStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }
        //pAdESStream.read(buffer);

        //File targetFile = new File(System.getProperty("user.dir") + "pAdES.pdf");
        //OutputStream outStream = new FileOutputStream(targetFile);
        //outStream.write(buffer);
        //this.statusReader.confirmProcessedSignatureJob();

        return "fetched pade";
    }
    else return "failed";
    }

    public String getXades() throws IOException{

        if (this.statusReader.getStatusResponse().is(this.statusReader.getStatusResponse().getStatus().SIGNED)) {
            InputStream xAdESStream = client.getXAdES(this.statusReader.getStatusResponse().getxAdESUrl());
            byte[] buffer = new byte[xAdESStream.available()];
            xAdESStream.read(buffer);

            File targetFile = new File(System.getProperty("user.dir") + "xAdES.xml");
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
            this.statusReader.confirmProcessedSignatureJob();

            return "fetched xade";
        }
        else return "failed";
    }








}
