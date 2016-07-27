import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import no.digipost.signature.client.core.SignatureJob;
import no.digipost.signature.client.security.KeyStoreConfig;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;



/**
 * This class is a sceleton for the signing-flow.
 */
@RestController
@Controller
@EnableAutoConfiguration
public class DigipostSpringConnector {

    private URL completionURL; //Can not remove
    private String statusQueryToken;
    private StatusReader statusReader;
    private SignedDocumentFetcher signedDocumentFetcher;
    private PortalSignedDocumentFetcher portalSignedDocumentFetcher;
    private PortalJobPoller poller;

    private TypesafeDocumentConfigProvider documentConfigProvider;
    private TypesafeDocumentConfig documentConfig;

    private TypesafeServerConfigProvider serverConfigProvider;
    private TypesafeServerConfig serverConfig;

    private TypesafeKeystoreConfigProvider keystoreConfigProvider;
    private TypesafeKeystoreConfig keystoreConfig;

    private SigningServiceConnector signingServiceConnector;

    private String[] exitUrls;

    public DatabaseSignatureStorage storage = new DatabaseSignatureStorage();
    public SignatureJobModel s;
    private String senderPid;

    /**
     * This is the mapping for starting the process. It should probably have a parameter designating the correct document by ID
     * from the SignatureDatabase.
     *
     * @return ModelAndView - A model and a view.
     * @throws IOException
     */

    @Autowired
    public void setupConfig() throws URISyntaxException{
        Config configFile = ConfigFactory.load("application.conf");
        documentConfigProvider = new TypesafeDocumentConfigProvider(configFile);
        serverConfigProvider = new TypesafeServerConfigProvider(configFile);
        keystoreConfigProvider = new TypesafeKeystoreConfigProvider(configFile);

        this.documentConfig = documentConfigProvider.getByEmail("eulverso2@gmail.com");
        this.serverConfig = serverConfigProvider.getByName("default");
        this.keystoreConfig = keystoreConfigProvider.getByName("default");
        exitUrls = new String[] {serverConfig.getCompletionUri().toString(),serverConfig.getRejectionUri().toString(),serverConfig.getErrorUri().toString()};
    }

    @RequestMapping("/test")
    public String test() {
        return "Hello";
    }

    @RequestMapping("/asice")
    public ModelAndView makeAsice(HttpServletRequest request) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, URISyntaxException {
        setupConfig();
        senderPid = request.getHeader("X-DifiProxy-pid");
        System.out.println(documentConfig.getSender());
        System.out.println(documentConfig.getSigner());
        s = new SignatureJobModel("Ikke signert", documentConfig.getSender(), documentConfig.getSigner(), senderPid);
        storage.insertSignaturejobToDB(s);

        AsiceMaker asiceMaker = new AsiceMaker();
        SetupClientConfig clientConfig = new SetupClientConfig("Direct");

        clientConfig.initialize(asiceMaker.getContactInfo(),documentConfig.getSender());
        asiceMaker.createAsice(s.getSigner(), s.getSender(), exitUrls, clientConfig.getClientConfiguration());

        SignatureJob signatureJob = asiceMaker.getSignatureJob();
        KeyStoreConfig keyStoreConfig = clientConfig.getKeyStoreConfig();

        this.signingServiceConnector = new SigningServiceConnector();
        signingServiceConnector.sendRequest(signatureJob, keyStoreConfig);


        return new ModelAndView("redirect:" + signingServiceConnector.getRedirectUrl());

    }

    /**
     * The user is redirected to one of these three from the signing portal. The function should get the token
     * as a query parameter. TODO: Find out what else it should do.
     */
    @RequestMapping("/onCompletion")
    public String whenSigningComplete(@RequestParam("status_query_token") String token) {
        this.statusQueryToken = token;
        if(this.statusReader == null){
            this.statusReader = new StatusReader(signingServiceConnector.getDirectClient().get(), signingServiceConnector.getDirectJobResponse().get(), this.statusQueryToken);
        }
        storage.updateStatus(s, statusReader.getStatus());
        return statusReader.getStatus().concat("<br> <a href='http://localhost:8080/getXades'> Click here to get Xades </a>")
                                        .concat("<br> <a href='http://localhost:8080/getPades'> Click here to get Pades");


    }

    @RequestMapping("/onError")
    public String whenSigningFails(@RequestParam("status_query_token") String token) {
        this.statusQueryToken = token;
        if(this.statusReader == null){
            this.statusReader = new StatusReader(signingServiceConnector.getDirectClient().get(), signingServiceConnector.getDirectJobResponse().get(), this.statusQueryToken);
        }
        storage.updateStatus(s, statusReader.getStatus());
        return statusReader.getStatus();

    }

    @RequestMapping("/onRejection")
    public String whenUserRejects(@RequestParam("status_query_token") String token) {
        //String status = signingServiceConnector.checkStatus();
        this.statusQueryToken = token;
        if(this.statusReader == null){ //Added later for testing purposes
            this.statusReader = new StatusReader(signingServiceConnector.getDirectClient().get(), signingServiceConnector.getDirectJobResponse().get(), this.statusQueryToken);
        }
        storage.updateStatus(s, statusReader.getStatus());
        return statusReader.getStatus();

        //Returnerer statusChange.toString()
    }


    @RequestMapping(value = "/getPades", produces = "application/pdf")
    public byte[] getPades() throws IOException {
        if (this.signedDocumentFetcher != null) {
            return signedDocumentFetcher.getPades();
        } else if (this.signingServiceConnector != null) {
            this.signedDocumentFetcher = new SignedDocumentFetcher(this.signingServiceConnector.getDirectClient().get(), this.statusReader);
            return signedDocumentFetcher.getPades();
        }
        return "Unable to fetch Pade".getBytes();
        // status was either REJECTED or FAILED, XAdES and PAdES are not available.
    }

    @RequestMapping(value = "/getXades", produces = "application/xml")
    public byte[] getXades() throws IOException {
        if (this.signedDocumentFetcher != null) {
            return signedDocumentFetcher.getXades();

        } else if(this.signingServiceConnector != null){
            this.signedDocumentFetcher = new SignedDocumentFetcher(this.signingServiceConnector.getDirectClient().get(),this.statusReader);

            return signedDocumentFetcher.getXades();
        }
        return "Unable to fetch Xade".getBytes();
        // status was either REJECTED or FAILED, XAdES and PAdES are not available.
    }


  /*  //In order to get to the sign-in portal, such as BankID, the user needs a redirect-url and a valid token. This method checks if the token is valid
    public boolean checkToken() {
        return false;
    }

    //Exists in the library, is used to check if the signing process was sucessfull, or if the user rejected it, or if an error occured (see completion-, rejection-, and errorURL)
    public String signingStatus() { //Finnes i biblioteket
        return null;
    }

    //Returnes one of the three URLS (completion, rejection, and errorURL) based on how the signing (aka the "job") went
    @RequestMapping("/getJobStatus") //Can not remove
    public URL getJobStatus() {
        //Based on signingStatus
        return this.completionURL;
        //or return this.rejectionURL
        //or return this.errorURL
    }*/

    public void setSignedDocumentFetcher(SignedDocumentFetcher signedDocumentFetcher){
        this.signedDocumentFetcher = signedDocumentFetcher;
    }

    public void setSigningServiceConnector(SigningServiceConnector signingServiceConnector){
        this.signingServiceConnector = signingServiceConnector;
    }

    public void setStatusReader(StatusReader statsreader){
        this.statusReader = statsreader;
    }

    public void setStatusQueryToken(String token){
        this.statusQueryToken = token;
    }

    public void setStorage(){
        storage.insertSignaturejobToDB(s);
    }



}
