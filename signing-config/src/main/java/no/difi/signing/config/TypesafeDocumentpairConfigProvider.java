package no.difi.signing.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TypesafeDocumentpairConfigProvider {

    private Map<String, TypesafeDocumentpairConfig> documentpairs;

    public TypesafeDocumentpairConfigProvider() {
        System.out.println(System.getProperty("user.dir"));
        List<Config> configList = new ArrayList<>();
        System.out.println(System.getProperty("user.dir").concat("\\src\\test\\resources\\docs"));
        File dir = new File(System.getProperty("user.dir").concat("\\src\\test\\resources\\docs"));
        File[] configListing = dir.listFiles();


        if(configListing != null){
            for(File config : configListing){
                String filename = config.getName();
                String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
                //String extension = config.getName().split(".")[0];
                System.out.println(extension);
                if(extension.contains("conf")){
                    System.out.println("extension equaled conf");
                    System.out.println(config.getAbsolutePath());
                    Config configFilename = ConfigFactory.parseFile(config);
                    configFilename.entrySet().stream().forEach(System.out::println);
                    configList.add(configFilename);

                }
            }
        }
        configList.stream().forEach(System.out::println);
        documentpairs = new HashMap<>();
        for (Config config : configList){
            Map<String, TypesafeDocumentpairConfig> newDocumentpairs;
            config.entrySet().stream().forEach(System.out::println);
            config.getObject("document").keySet().stream().forEach(System.out::println);
            newDocumentpairs = config.getObject("document").keySet().stream()
                    .map(z -> config.getConfig(String.format("document.%s",z)))
                    .map(z -> new TypesafeDocumentpairConfig(z))
                    .collect(Collectors.toMap(TypesafeDocumentpairConfig::getTitle, Function.identity()));
            documentpairs.putAll(newDocumentpairs);
        }

        System.out.println(documentpairs.toString());
    }

    public TypesafeDocumentpairConfig getByTitle(String title){
        return documentpairs.get(title);
    }
}
