import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

/**
 * Created by camp-nto on 08.07.2016.
 */

@EnableAutoConfiguration
@RestController
public class DigipostSpringConnector {

    //TODO: Decide which of these are stored here or just in the signingServiceConnector object.
    private URL completionURL; //Can not remove
    private String statusQueryToken;
    private StatusReader statusReader;





    private String[] exitUrls = {
            "http://localhost:8080/onCompletion","http://localhost:8080/onRejection","http://localhost:8080/onError"
    };
    private SigningServiceConnector signingServiceConnector;

    /**
     * This is the mapping for starting the process. It should probably have a parameter designating the correct document by ID
     * from the SignatureDatabase.
     * @return ModelAndView - A model and a view.
     * @throws IOException
     *
     */
    @RequestMapping("/asice")
    public ModelAndView makeAsice() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig();

        clientConfig.setupKeystoreConfig(asiceMaker.getContactInfo());
        clientConfig.setupClientConfiguration("991825827");

        asiceMaker.createAsice("17079493538","123456789",exitUrls,clientConfig.getClientConfiguration());

        SignatureJob signatureJob = asiceMaker.getSignatureJob();
        KeyStoreConfig keyStoreConfig = clientConfig.getKeyStoreConfig();

        this.signingServiceConnector = new SigningServiceConnector();
        signingServiceConnector.sendRequest(signatureJob,keyStoreConfig);


        return new ModelAndView("redirect:" + signingServiceConnector.getRedirectUrl());

    }

    /**
     * The user is redirected to one of these three from the signing portal. The function should get the token
     * as a query parameter. TODO: Find out what else it should do.
     */
    @RequestMapping("/onCompletion")
    public String whenSigningComplete(@RequestParam("status_query_token") String token){
        this.statusQueryToken = token;
        this.statusReader = new StatusReader(signingServiceConnector.getDirectClient(), signingServiceConnector.getDirectJobResponse(), this.statusQueryToken);
        return statusReader.getStatus();

    }

    @RequestMapping("/onError")
    public String whenSigningFails(@RequestParam("status_query_token") String token){
        this.statusQueryToken = token;
        this.statusReader = new StatusReader(signingServiceConnector.getDirectClient(), signingServiceConnector.getDirectJobResponse(), this.statusQueryToken);
        return statusReader.getStatus();

    }

    @RequestMapping("/onRejection")
    public String whenUserRejects(@RequestParam("status_query_token") String token){
        //String status = signingServiceConnector.checkStatus();
        this.statusQueryToken = token;
        this.statusReader = new StatusReader(signingServiceConnector.getDirectClient(), signingServiceConnector.getDirectJobResponse(), this.statusQueryToken);
        return statusReader.getStatus();
        //Returnerer statusChange.toString()
    }

    @RequestMapping("/getDocument")
    public String getSignedDocument(@RequestParam("document_type") String document_type){
        if (this.statusReader.getStatusResponse().is(this.statusReader.getStatusResponse().getStatus().SIGNED)) {
            if(document_type == "xades") {
                InputStream xAdESStream = signingServiceConnector.getDirectClient().getXAdES(this.statusReader.getStatusResponse().getxAdESUrl());
                return "fetched xade";
            } else if(document_type == "pades") {
                InputStream pAdESStream = signingServiceConnector.getDirectClient().getPAdES(this.statusReader.getStatusResponse().getpAdESUrl());
                return "fetched pade";
            }
            else return "failed";
        } else {
            return "failed2";
            // status was either REJECTED or FAILED, XAdES and PAdES are not available.
        }
    }

    @RequestMapping("/getPades")
    public String getPades() throws IOException{
        if (this.statusReader.getStatusResponse().is(this.statusReader.getStatusResponse().getStatus().SIGNED)) {
                InputStream pAdESStream = signingServiceConnector.getDirectClient().getPAdES(this.statusReader.getStatusResponse().getpAdESUrl());
                byte[] buffer = new byte[pAdESStream.available()];
                pAdESStream.read(buffer);

                File targetFile = new File(System.getProperty("user.dir") + "targetFile2.pdf");
                OutputStream outStream = new FileOutputStream(targetFile);
                outStream.write(buffer);
                return "fetched pade";
            }
            else return "failed";
            // status was either REJECTED or FAILED, XAdES and PAdES are not available.
        }

    @RequestMapping("/getXades")
    public String getXades() throws IOException{
        if (this.statusReader.getStatusResponse().is(this.statusReader.getStatusResponse().getStatus().SIGNED)) {
            InputStream xAdESStream = signingServiceConnector.getDirectClient().getXAdES(this.statusReader.getStatusResponse().getxAdESUrl());
            byte[] buffer = new byte[xAdESStream.available()];
            xAdESStream.read(buffer);

            File targetFile = new File(System.getProperty("user.dir") + "targetFile.xml");
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
            return "fetched xade";
        }
        else return "failed";
        // status was either REJECTED or FAILED, XAdES and PAdES are not available.
    }


    //In order to get to the sign-in portal, such as BankID, the user needs a redirect-url and a valid token. This method checks if the token is valid
    public boolean checkToken(){
        return false;
    }

    //Exists in the library, is used to check if the signing process was sucessfull, or if the user rejected it, or if an error occured (see completion-, rejection-, and errorURL)
    public String signingStatus(){ //Finnes i biblioteket
        return null;
    }

    //Returnes one of the three URLS (completion, rejection, and errorURL) based on how the signing (aka the "job") went
    @RequestMapping("/getJobStatus") //Can not remove
    public URL getJobStatus(){
        //Based on signingStatus
        return this.completionURL;
        //or return this.rejectionURL
        //or return this.errorURL
    }


}
