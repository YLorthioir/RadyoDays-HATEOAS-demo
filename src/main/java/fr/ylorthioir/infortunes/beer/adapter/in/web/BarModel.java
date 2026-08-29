package fr.ylorthioir.infortunes.beer.adapter.in.web;

import org.springframework.hateoas.RepresentationModel;
public class BarModel extends RepresentationModel<BarModel> {

    private final String name;
    private final String president;

    public BarModel(String name,
            String president
    ) {
        this.name=name;
        this.president = president;
    }

    public String getName() {
        return name;
    }

    public String getPresident() {
        return president;
    }
}
