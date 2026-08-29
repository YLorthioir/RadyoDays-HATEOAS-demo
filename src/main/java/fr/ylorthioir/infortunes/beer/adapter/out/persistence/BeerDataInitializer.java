package fr.ylorthioir.infortunes.beer.adapter.out.persistence;

import fr.ylorthioir.infortunes.beer.application.port.out.BeerRepositoryPort;
import fr.ylorthioir.infortunes.beer.domain.Beer;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeerDataInitializer {
    @Bean CommandLineRunner loadBeers(BeerRepositoryPort repository) {
        return args -> {
            if (repository.findAll().isEmpty()) {
                repository.save(new Beer(null, "Orval", "Brasserie d'Orval", "Trappiste", new BigDecimal("4.50"), 12));
                repository.save(new Beer(null, "Rocherfort 10", "Abbaye Notre-Dame de Saint-Remy", "Trappiste", new BigDecimal("5.90"), 8));
                repository.save(new Beer(null, "Chimay Bleue", "Abbaye Notre-Dame de Scourmont", "Bière médicinale", new BigDecimal("5.50"), 0));
                repository.save(new Beer(null, "Cardyo", "Non-abbaye de Radyo", "Blonde dans l'idée d'une API", new BigDecimal("0"), 0));
            }
        };
    }
}
