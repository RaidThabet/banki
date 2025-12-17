package tp.securite.banki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class BankiApplication {

    public static void main(final String[] args) {
        SpringApplication.run(BankiApplication.class, args);
    }

}
