import org.springframework.boot.SpringApplication;

/**
 * Created by camp-nto on 07.07.2016.
 */
public class ApplicationMain {

    public static void main(String[] args) throws Exception {
        Object[] sources = {IndexController.class, };
        SpringApplication.run(sources, args);
    }
}
