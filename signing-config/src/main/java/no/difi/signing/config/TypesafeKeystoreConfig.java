package no.difi.signing.config;

import com.typesafe.config.Config;

public class TypesafeKeystoreConfig {

    private String keystore;
    private String name;
    private String password;

    public TypesafeKeystoreConfig(Config keystoreConfig){
        System.out.println(keystoreConfig.entrySet().toString());
        this.keystore = keystoreConfig.getString("keystore");
        this.name = keystoreConfig.getString("name");
        this.password = keystoreConfig.getString("password");


        System.out.println(this.keystore);
        System.out.println(this.name);
        System.out.println(this.password);
    }

    public String getKeystore() {
        return this.keystore;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }
}


