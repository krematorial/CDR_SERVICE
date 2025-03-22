package kulagoSA.CDRPOST.config;

import kulagoSA.CDRPOST.service.DataGeneratorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(DataGeneratorService dataGeneratorService) {
        return args -> {
            dataGeneratorService.generateSQLFiles();
        };
    }
}