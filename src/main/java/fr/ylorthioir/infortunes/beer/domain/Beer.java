package fr.ylorthioir.infortunes.beer.domain;

import java.math.BigDecimal;

public class Beer {
    private final Long id;
    private String name;
    private final String brewery;
    private final String style;
    private BigDecimal price;
    private int stock;

    public Beer(Long id, String name, String brewery, String style, BigDecimal price, int stock) {
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative");
        this.id = id;
        this.name = name;
        this.brewery = brewery;
        this.style = style;
        this.price = price;
        this.stock = stock;
    }

    public void order() {
        if (stock == 0) throw new BeerOutOfStockException(id);
        stock--;
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) throw new IllegalArgumentException("Beer name cannot be blank");
        this.name = newName;
    }

    public void changePrice(BigDecimal newPrice) {
        if (newPrice == null || newPrice.signum() < 0) throw new IllegalArgumentException("Beer price cannot be negative");
        this.price = newPrice;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getBrewery() { return brewery; }
    public String getStyle() { return style; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
}
