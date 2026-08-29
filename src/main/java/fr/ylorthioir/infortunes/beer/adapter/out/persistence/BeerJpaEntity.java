package fr.ylorthioir.infortunes.beer.adapter.out.persistence;

import fr.ylorthioir.infortunes.beer.domain.Beer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class BeerJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String brewery;
    private String style;
    private BigDecimal price;
    private int stock;

    protected BeerJpaEntity() { }

    public BeerJpaEntity(Beer beer) {
        this.id = beer.getId(); this.name = beer.getName(); this.brewery = beer.getBrewery();
        this.style = beer.getStyle(); this.price = beer.getPrice(); this.stock = beer.getStock();
    }

    public Beer toDomain() { return new Beer(id, name, brewery, style, price, stock); }
}
