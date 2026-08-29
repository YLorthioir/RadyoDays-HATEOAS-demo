package fr.ylorthioir.infortunes.beer.adapter.in.web;

import org.springframework.hateoas.RepresentationModel;

/** Représentation HATEOAS d'une erreur : même une erreur reste navigable. */
public class ApiErrorModel extends RepresentationModel<ApiErrorModel> {
    private final String message;

    public ApiErrorModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
