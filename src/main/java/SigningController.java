import no.digipost.signature.client.asice.DocumentBundle;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

/**
 * This class is a sceleton for the signin-flow.
 *
 */
@RestController
@EnableAutoConfiguration
@Controller


public class SigningController {

    //TODO: Decide which of these are stored here or just in the signingServiceConnector object.
    private URL redirectURL; //Redirects the user to a sign-in portal, for example "BankID"
    private URL completionURL; //This URL is given to the user if everything goes "well" with the sign-in. Redirects the user back to our website
    private URL rejectionURL; //given to the user if he/she chooses to stop the signing process
    private URL errorURL; //given to the user if an error occurs during the signing process
    private URL statusURL; //can be called to give a "status" after the signing process. Status contains useful information, such as the signed documents
    private File completionDocument; //Stores the signed document
    private URL confirmationURL; //called in the very end to check if the whole signing process went as planned
    private String[] exitUrls = {
            "http://localhost:8080/onCompletion","http://localhost:8080/onRejection","http://localhost:8080/onError"
    };
    private SigningServiceConnector signingServiceConnector;

    /**
     * This is the mapping for starting the process. It should probably have a parameter designating the correct document by ID
     * from the database.
     * @return ModelAndView - A model and a view.
     * @throws IOException
     *
     */
    @RequestMapping("/asice")
    public ModelAndView makeAsice() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
        AsiceMaker asiceMaker = new AsiceMaker();
        asiceMaker.setupKeystoreConfig();
        asiceMaker.createAsice("1707949358","123456789",exitUrls);

        SignatureJob signatureJob = asiceMaker.getSignatureJob();
        KeyStoreConfig keyStoreConfig = asiceMaker.getKeyStoreConfig();

        this.signingServiceConnector = new SigningServiceConnector();
        signingServiceConnector.sendRequest(signatureJob,keyStoreConfig);


        return new ModelAndView("redirect:" + signingServiceConnector.getRedirectUrl());

    }

    /**
     * The user is redirected to one of these three from the signing portal. The function should get the token
     * as a query parameter. TODO: Find out what else it should do.
     */
    @RequestMapping("/onCompletion")
    public String whenSigningComplete(){
        String status = signingServiceConnector.checkStatus();
        return status;
    }

    @RequestMapping("/onError")
    public String whenSigningFails(){
        String status = signingServiceConnector.checkStatus();
        return status;
    }

    @RequestMapping("/onRejection")
    public String whenUserRejects(@RequestParam("status_query_token") String token){
        String status = signingServiceConnector.checkStatus();
        return status;
        //Returnerer statusChange.toString()
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
    @RequestMapping("/getJobStatus")
    public URL getJobStatus(){
        //Based on signingStatus
        return this.completionURL;
        //or return this.rejectionURL
        //or return this.errorURL
    }

    //Returns statusURL to get data about the signing, when the signing is over
    @RequestMapping("/getStatusURL")
    public URL getStatusURL(){
        return this.statusURL;
    }

    //Returnes the signed document
    @RequestMapping("/getCompletionDocument")
    public File getCompletionDocument(){
        return this.completionDocument;
    }

    //Returnes the URL which can be used to check if the whole process went well
    @RequestMapping("/returnConfirmationURL")
    public URL returnConfirmationURL(){
        return this.confirmationURL;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SigningController.class, args);
    }

}
