package fr.ylorthioir.infortunes.beer.adapter.in.web;

import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController @RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class BarController {
    @GetMapping public BarModel bar() {

        BarModel model = new BarModel("Cercle info", "Damien Bibi Fricot");

        model.add(linkTo(methodOn(BarController.class).bar())
                .withSelfRel(), linkTo(methodOn(BeerController.class)
                .listBeers()).withRel("beers"));

        return model;
    }
}
