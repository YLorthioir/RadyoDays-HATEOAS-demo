package fr.ylorthioir.infortunes.beer.adapter.in.web;

import fr.ylorthioir.infortunes.beer.application.port.in.BeerUseCase;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/beer")
@CrossOrigin(origins = "http://localhost:4200")
public class BeerController {

    private final BeerUseCase beers;
    private final BeerModelAssembler assembler;

    public BeerController(BeerUseCase beers, BeerModelAssembler assembler) {
        this.beers = beers;
        this.assembler = assembler;
    }

    // Niveau 3 REST : les liens de la réponse indiquent au client comment poursuivre
    // sa navigation, sans qu'il ait à reconstruire les URLs de l'API.
    @GetMapping
    public CollectionModel<BeerModel> listBeers() {

        // La collection expose uniquement des résumés. Pour découvrir les commandes,
        // le client suit d'abord le lien self d'une bière vers sa représentation détaillée.
        List<BeerModel> models = beers.listBeers().stream().map(assembler::toSummaryModel).toList();

        return CollectionModel.of(models, linkTo(methodOn(BeerController.class).listBeers())
                        .withSelfRel(),
                linkTo(methodOn(BarController.class).bar()).withRel("bar"));
    }

    // Le contrôleur orchestre uniquement : la recherche est portée par BeerUseCase,
    // puis l'assembler transforme le domaine en représentation JSON HATEOAS.
    @GetMapping("/{id}")
    public ResponseEntity<BeerModel> getBeer(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toModel(beers.getBeer(id)));
    }

    // "order" représente une action métier.
    @PostMapping("/{id}/order")
    public ResponseEntity<BeerModel> orderBeer(@PathVariable Long id) {
        return ResponseEntity.ok(assembler.toModel(beers.orderBeer(id)));
    }

    // L'API expose des intentions métier explicites plutôt qu'un update CRUD générique.
    @PatchMapping(value = "/{id}/name", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BeerModel> renameBeer(@PathVariable Long id, @RequestBody RenameBeerRequest request) {
        return ResponseEntity.ok(assembler.toModel(beers.renameBeer(id, request.name())));
    }

    @PatchMapping(value = "/{id}/price", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BeerModel> changeBeerPrice(@PathVariable Long id,
                                                     @RequestBody ChangeBeerPriceRequest request) {
        return ResponseEntity.ok(assembler.toModel(beers.changeBeerPrice(id, request.price())));
    }
}
