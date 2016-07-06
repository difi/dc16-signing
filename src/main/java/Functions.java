import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.direct.DirectJobStatusResponse;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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

public class Functions {

    private URL redirectURL; //Redirects the user to a sign-in portal, for example "BankID"
    private URL completionURL; //This URL is given to the user if everything goes "well" with the sign-in. Redirects the user back to our website
    private URL rejectionURL; //given to the user if he/she chooses to stop the signing process
    private URL errorURL; //given to the user if an error occurs during the signing process
    private URL statusURL; //can be called to give a "status" after the signing process. Status contains useful information, such as the signed documents
    private File completionDocument; //Stores the signed document
    private URL confirmationURL; //called in the very end to check if the whole signing process went as planned

    private SendHTTPRequest sendHTTPRequest;

    //If we had a user who clicked "start signing", a jobRequestURL would be posted in the back-end to start the signing process
    @RequestMapping("/")
    public void postJobRequestURL(){

    }


    @RequestMapping("/asice")
    public ModelAndView makeAsice() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
        GenerateAsice generateAsice = new GenerateAsice();
        generateAsice.setupKeystoreConfig();
        generateAsice.createAsice();

        SignatureJob signatureJob = generateAsice.getSignatureJob();
        KeyStoreConfig keyStoreConfig = generateAsice.getKeyStoreConfig();

        this.sendHTTPRequest = new SendHTTPRequest();
        sendHTTPRequest.sendRequest(signatureJob,keyStoreConfig);


        return new ModelAndView("redirect:" + sendHTTPRequest.getRedirectUrl());


    }

    @RequestMapping("/onCompletion")
    public void whenSigningComplete(){

    }

    @RequestMapping("/onError")
    public String whenSigningFails(){
        String status = sendHTTPRequest.checkStatus();
        return status;
    }

    @RequestMapping("/onRejection?")
    public String whenUserRejects(@RequestParam("status_query_token") String token){
        String status = sendHTTPRequest.checkStatus();
        return status;
    }

    //In order to get to the sign-in portal, such as BankID, the user needs a redirect-url and a valid token. This method checks if the token is valid
    public boolean checkToken(){
        return false;
    }

    //If the token is valid, the user gets a redirect-URL which takes him/her to the sign-in-portal (such as BankID)
    @RequestMapping("/getRedirectURL")
    public URL getRedirectURL(){
        if(checkToken()){
            return this.redirectURL;
        }
        else throw new IllegalStateException("Missing token");
    }

    //Exists in the library, is used to check if the signing process was sucessfull, or if the user rejected it, or if an error occured (see completion-, rejection-, and errorURL)
    public String signingStatus(){ //Fimmes i biblioteket
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
    @RequestMapping("/returnCOnfirmationURL")
    public URL returnConfirmationURL(){
        return this.confirmationURL;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Functions.class, args);
    }

}
