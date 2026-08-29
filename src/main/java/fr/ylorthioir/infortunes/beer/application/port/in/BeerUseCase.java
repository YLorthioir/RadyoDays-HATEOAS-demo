package fr.ylorthioir.infortunes.beer.application.port.in;

import fr.ylorthioir.infortunes.beer.domain.Beer;
import java.util.List;
import java.math.BigDecimal;

public interface BeerUseCase {
    List<Beer> listBeers();
    Beer getBeer(Long id);
    Beer orderBeer(Long id);
    Beer renameBeer(Long id, String name);
    Beer changeBeerPrice(Long id, BigDecimal price);
}
