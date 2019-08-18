package ca.cmpt213.as5;

import ca.cmpt213.as5.model.Model;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ca.cmpt213.as5.model.FileProcessor;

@SpringBootApplication
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
