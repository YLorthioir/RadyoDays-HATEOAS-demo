# Infortunes Beer Bar

Démo de HATEOAS niveau 3 avec Spring Boot, H2 et un client Angular.
Le client ne construit pas les URLs métier : il commence par le point d'entrée `/api`, puis suit les liens retournés par l'API.

## Démarrer la démo

Dans un terminal :

```bash
mvn spring-boot:run
```

Dans un autre :

```bash
cd frontend
npm install
npm start
```

Puis ouvrir <http://localhost:4200>.

La base H2 est en mémoire et est recréée à chaque démarrage. Quatres bières sont chargées : Orval, Rocherfort 10, Chimay Bleu et Cardyo (fautes d'orthographe voulues).
La console H2 est disponible sur <http://localhost:8080/h2-console>, avec l'URL `jdbc:h2:mem:beerbar`, l'utilisateur `sa` et un mot de passe vide.

## Le parcours HATEOAS

Le client démarre par :

```http
GET http://localhost:8080/api
```

La réponse contient notamment :

```json
{
  "name": "Cercle info",
  "_links": {
    "self": { "href": "http://localhost:8080/api" },
    "beers": { "href": "http://localhost:8080/api/beer" }
  }
}
```

Le client suit ensuite `_links.beers.href` :

```http
GET /api/beer
```

La collection contient des représentations résumées. Chaque bière expose seulement un lien `self` vers sa représentation détaillée :

```json
{
  "name": "Orval",
  "_links": {
    "self": { "href": "http://localhost:8080/api/beer/1" }
  }
}
```

Après avoir suivi ce lien, le client découvre les actions réellement disponibles :

```json
{
  "name": "Orval",
  "price": 4.50,
  "stock": 12,
  "_links": {
    "self": { "href": ".../api/beer/1" },
    "beers": { "href": ".../api/beer" },
    "rename": { "href": ".../api/beer/1/name" },
    "change-price": { "href": ".../api/beer/1/price" },
    "order": { "href": ".../api/beer/1/order" }
  }
}
```

La Chimay Bleue est épuisée : son lien `order` n'est donc pas publié. Le client Angular masque automatiquement le bouton correspondant. C'est l'API qui indique les transitions possibles, et non le client qui recopie les règles métier.

## Actions métier

Les actions sont découvertes dans `_links`, puis appelées avec la méthode HTTP attendue :

```bash
curl -X PATCH http://localhost:8080/api/beer/2/name \
  -H 'Content-Type: application/json' \
  -d '{"name":"Rochefort 10"}'

curl -X PATCH http://localhost:8080/api/beer/1/price \
  -H 'Content-Type: application/json' \
  -d '{"price":7.00}'

curl -X POST http://localhost:8080/api/beer/1/order
```

Les noms `rename`, `change-price` et `order` reflètent les intentions métier du domaine. Une erreur `404` renvoie également des liens `root` et `beers` pour permettre au client de se réorienter.

## Organisation backend

```text
beer/
├── domain/                         # agrégat Beer et règles métier
├── application/
│   ├── port/in/                    # cas d'usage exposés
│   ├── port/out/                   # dépendances requises par l'application
│   └── service/                    # orchestration des cas d'usage
└── adapter/
    ├── config/                    # composition Spring
    ├── in/web/                    # HTTP, DTOs et HATEOAS
    └── out/persistence/           # JPA/H2
```

`BeerService` dépend uniquement des ports et le domaine ne dépend d'aucun framework. Spring assemble les composants dans `adapter/config`. L'assembleur transforme le domaine en représentation HTTP sans exposer les objets JPA.

## Organisation frontend

Le mini client se trouve dans `frontend/src/app` :

- `BeerApiService` suit les liens HAL et exécute les actions indiquées par l'API ;
- `AppComponent` affiche la collection, charge le détail et rend les actions découvertes ;
- aucune URL de bière ou d'action n'est codée dans le composant.

Le backend autorise `http://localhost:4200` via CORS pour le développement local.

## Tests et documentation API

```bash
mvn test
cd frontend && npm run build
```

Swagger UI est disponible sur <http://localhost:8080/swagger-ui.html> et le document OpenAPI sur <http://localhost:8080/v3/api-docs>.

## À retenir

Une API niveau 2 fournit des URLs connues à l'avance.
Une API niveau 3 fournit des liens représentant les transitions possibles.
Le client de cette démo ne sait pas que la commande est `/api/beer/{id}/order` : il suit le lien `order` fourni par le serveur.
