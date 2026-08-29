package fr.ylorthioir.infortunes.beer.adapter.in.web;

import fr.ylorthioir.infortunes.beer.domain.Beer;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class BeerModelAssembler implements RepresentationModelAssembler<Beer, BeerModel> {
    /** Représentation détaillée : elle expose les actions métier disponibles. */
    public BeerModel toModel(Beer beer) {

        BeerModel model = new BeerModel(beer);

        // Lien de navigation vers la ressource courante.
        model.add(linkTo(methodOn(BeerController.class).getBeer(beer.getId())).withSelfRel());

        // Lien vers la collection pour revenir au catalogue.
        model.add(linkTo(methodOn(BeerController.class).listBeers()).withRel("beers"));

        // Le lien conditionnel n'est exposé que si la commande est possible.
        // Le client n'a donc pas besoin de connaître cette règle métier à l'avance.
        if (beer.getStock() > 0) {
            model.add(linkTo(methodOn(BeerController.class).orderBeer(beer.getId())).withRel("order"));
        }

        // Le lien indique la ressource à modifier. L'affordance décrit en plus
        // la méthode PATCH attendue et permet aux clients HATEOAS outillés
        // de découvrir cette action et son formulaire JSON.
        // Ces liens représentent des intentions métier, et non une opération CRUD générique.
        model.add(linkTo(methodOn(BeerController.class).renameBeer(beer.getId(), null)).withRel("rename")
                .andAffordance(afford(methodOn(BeerController.class).renameBeer(beer.getId(), null))));
        model.add(linkTo(methodOn(BeerController.class).changeBeerPrice(beer.getId(), null)).withRel("change-price")
                .andAffordance(afford(methodOn(BeerController.class).changeBeerPrice(beer.getId(), null))));
        return model;
    }

    /**
     * Représentation de collection : elle permet de naviguer vers une bière,
     * mais ne publie pas ses actions de modification ou de commande.
     */
    public BeerModel toSummaryModel(Beer beer) {
        BeerModel model = new BeerModel(beer);
        model.add(linkTo(methodOn(BeerController.class).getBeer(beer.getId())).withSelfRel());
        return model;
    }
}
