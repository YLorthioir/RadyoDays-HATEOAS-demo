package fr.ylorthioir.infortunes.beer.domain;

public class BeerOutOfStockException extends RuntimeException {
    public BeerOutOfStockException(Long beerId) {
        super("Beer " + beerId + " is out of stock");
    }
}
