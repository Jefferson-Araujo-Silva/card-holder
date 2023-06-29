package cardholder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CardHolderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardHolderApplication.class, args);
    }

}
