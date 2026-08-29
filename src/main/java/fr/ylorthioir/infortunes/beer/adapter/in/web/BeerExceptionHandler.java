package fr.ylorthioir.infortunes.beer.adapter.in.web;

import fr.ylorthioir.infortunes.beer.application.service.BeerService.BeerNotFoundException;
import fr.ylorthioir.infortunes.beer.domain.BeerOutOfStockException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestControllerAdvice
public class BeerExceptionHandler {

    @ExceptionHandler({BeerNotFoundException.class, BeerOutOfStockException.class})
    public ResponseEntity<ApiErrorModel> handleNotAvailable(RuntimeException exception) {
        ApiErrorModel error = new ApiErrorModel(exception.getMessage());

        // On conserve le statut HTTP 404 : il ne s'agit pas d'une redirection.
        // Les liens permettent néanmoins au client HATEOAS de se réorienter.
        error.add(
                linkTo(methodOn(BarController.class).bar()).withRel("root"),
                linkTo(methodOn(BeerController.class).listBeers()).withRel("beers"));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
