package crypto.stackrs.stackrsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StackrsServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(StackrsServiceApplication.class, args);
  }

}
