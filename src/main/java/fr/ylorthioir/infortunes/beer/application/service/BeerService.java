package fr.ylorthioir.infortunes.beer.application.service;

import fr.ylorthioir.infortunes.beer.application.port.in.BeerUseCase;
import fr.ylorthioir.infortunes.beer.application.port.out.BeerRepositoryPort;
import fr.ylorthioir.infortunes.beer.domain.Beer;
import java.util.List;
import java.math.BigDecimal;

public class BeerService implements BeerUseCase {
    private final BeerRepositoryPort repository;

    public BeerService(BeerRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<Beer> listBeers() { return repository.findAll(); }

    @Override
    public Beer getBeer(Long id) { return repository.findById(id).orElseThrow(() -> new BeerNotFoundException(id)); }

    @Override
    public Beer orderBeer(Long id) {
        Beer beer = getBeer(id);
        beer.order();
        return repository.save(beer);
    }

    @Override
    public Beer renameBeer(Long id, String name) {
        Beer beer = getBeer(id);
        beer.rename(name);
        return repository.save(beer);
    }

    @Override
    public Beer changeBeerPrice(Long id, BigDecimal price) {
        Beer beer = getBeer(id);
        beer.changePrice(price);
        return repository.save(beer);
    }

    public static class BeerNotFoundException extends RuntimeException {
        public BeerNotFoundException(Long id) { super("Beer " + id + " not found"); }
    }
}
