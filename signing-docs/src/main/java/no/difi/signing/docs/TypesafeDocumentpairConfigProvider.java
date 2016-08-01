package no.difi.signing.docs;

import com.typesafe.config.Config;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TypesafeDocumentpairConfigProvider {

    private Map<String, TypesafeDocumentpairConfig> documentpair;

    public TypesafeDocumentpairConfigProvider(Config config) {
        documentpair = config.getObject("document").keySet().stream()
                .map(key -> config.getConfig(String.format("document.%s", key)))
                .map(c -> new TypesafeDocumentpairConfig(c))
                .collect(Collectors.toMap(TypesafeDocumentpairConfig::getTitle, Function.identity()));
    }

    public TypesafeDocumentpairConfig getDocumentConfig(String name){return documentpair.get(name);}
}

    /*public TypesafeDocumentpairConfigProvider(String name) {
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
*/