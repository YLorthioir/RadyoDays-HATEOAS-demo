package fr.ylorthioir.infortunes.beer.adapter.in.web;

import fr.ylorthioir.infortunes.beer.domain.Beer;
import java.math.BigDecimal;
import org.springframework.hateoas.RepresentationModel;

public class BeerModel extends RepresentationModel<BeerModel> {
    private final Long id;
    private final String name;
    private final String brewery;
    private final String style;
    private final BigDecimal price;
    private final int stock;

    public BeerModel(Beer beer) {
        id=beer.getId();
        name=beer.getName();
        brewery=beer.getBrewery();
        style=beer.getStyle();
        price=beer.getPrice();
        stock=beer.getStock();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getBrewery() { return brewery; }
    public String getStyle() { return style; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
}
