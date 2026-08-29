package fr.ylorthioir.infortunes.beer.application.port.out;

import fr.ylorthioir.infortunes.beer.domain.Beer;
import java.util.List;
import java.util.Optional;

public interface BeerRepositoryPort {
    List<Beer> findAll();
    Optional<Beer> findById(Long id);
    Beer save(Beer beer);
}
