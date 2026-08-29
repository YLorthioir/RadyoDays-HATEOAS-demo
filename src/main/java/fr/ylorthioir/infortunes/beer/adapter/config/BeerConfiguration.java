package fr.ylorthioir.infortunes.beer.adapter.config;

import fr.ylorthioir.infortunes.beer.application.port.in.BeerUseCase;
import fr.ylorthioir.infortunes.beer.application.port.out.BeerRepositoryPort;
import fr.ylorthioir.infortunes.beer.application.service.BeerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeerConfiguration {

    @Bean
    BeerUseCase beerUseCase(BeerRepositoryPort repository) {
        return new BeerService(repository);
    }
}
