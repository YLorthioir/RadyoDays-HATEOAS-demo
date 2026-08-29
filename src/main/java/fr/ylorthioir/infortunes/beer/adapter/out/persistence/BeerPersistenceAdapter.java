package fr.ylorthioir.infortunes.beer.adapter.out.persistence;

import fr.ylorthioir.infortunes.beer.application.port.out.BeerRepositoryPort;
import fr.ylorthioir.infortunes.beer.domain.Beer;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class BeerPersistenceAdapter implements BeerRepositoryPort {

    private final SpringDataBeerRepository repository;

    public BeerPersistenceAdapter(SpringDataBeerRepository repository) {
        this.repository = repository;

    }
    public List<Beer> findAll() {
        return repository.findAll().stream().map(BeerJpaEntity::toDomain).toList();
    }

    public Optional<Beer> findById(Long id) {
        return repository.findById(id).map(BeerJpaEntity::toDomain);
    }
    public Beer save(Beer beer) {
        return repository.save(new BeerJpaEntity(beer)).toDomain();
    }
}
