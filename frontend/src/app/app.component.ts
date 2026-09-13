import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Beer, BeerApiService, HateoasResource } from './beer-api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <main>
      <header>
        <span class="badge">HATEOAS · Quand l’API guide le client</span>
        <h1>{{ barName || 'Beer Bar' }}</h1>
        <p>Le client suit les liens fournis par l'API.</p>
        <details class="menu-easter-egg">
          <summary>La carte 🍺</summary>
          <img src="assets/IMG_4734.jpg" alt="La carte du bar : une sélection de bières à 1,40 €" loading="lazy">
        </details>
        @if(president) {
        <h6>Gloire à {{ president }}</h6>
        }
      </header>

      @if (error) {
        <p class="error">{{ error }}</p>
      }
      @if (catalogLink) {
        <button class="link-button" (click)="loadCatalog()">↻ Recharger le catalogue</button>
      }

      @if (beers.length) {
      <section class="layout">
        <div class="catalog">
          <h2>Le catalogue</h2>
          <p class="hint">Les actions apparaissent seulement sur la représentation détaillée.</p>
          @for (beer of beers; track beer.id) {
          <button class="beer-card" (click)="openBeer(beer)">
            <span class="beer-icon">🍺</span>
            <span><strong>{{ beer.name }}</strong><small>{{ beer.brewery }} · {{ beer.style }}</small></span>
            <span class="price">{{ beer.price | number:'1.2-2' }} €</span>
          </button>
          }
        </div>

        @if (selectedBeer; as beer) {
        <article class="detail">
          <span class="badge">GET one · représentation détaillée</span>
          <h2>{{ beer.name }}</h2>
          <p>{{ beer.brewery }} · {{ beer.style }}</p>
          <div class="facts"><span>{{ beer.price | number:'1.2-2' }} €</span><span>{{ beer.stock }} en stock</span></div>

          <div class="actions">
            <h3>Actions découvertes dans _links</h3>
            @if (beer._links['rename']; as renameLink) {
              <label>Nouveau nom <input [(ngModel)]="newName"></label>
              <button (click)="rename(renameLink)">Renommer</button>
            }
            @if (beer._links['change-price']; as priceLink) {
              <label>Nouveau prix <input type="number" min="0" step="0.10" [(ngModel)]="newPrice"></label>
              <button (click)="changePrice(priceLink)">Changer le prix</button>
            }
            @if (beer._links['order']; as orderLink) {
              <button class="primary" (click)="order(orderLink)">Commander une bière</button>
            } @else {
              <p class="sold-out">Commande indisponible : stock épuisé.</p>
            }
          </div>
        </article>
        }
      </section>
      }
    </main>
  `,
  styles: [`
    .menu-easter-egg { margin-top: 16px; }
    .menu-easter-egg summary { width: fit-content; color: #754617; cursor: pointer; }
    .menu-easter-egg img { display: block; width: 100%; max-width: 420px; height: auto; margin-top: 12px; border-radius: 12px; }
  `]
})
export class AppComponent {
  private readonly api = inject(BeerApiService);
  barName = '';
  president ?: string;
  beers: Beer[] = [];
  selectedBeer?: Beer;
  catalogLink?: HateoasResource;
  newName = '';
  newPrice = 0;
  error = '';

  constructor() { this.loadRoot(); }

  loadRoot(): void {
    this.api.getRoot().subscribe({
      next: root => {
        this.barName = (root as unknown as { name?: string }).name ?? 'Infortunes Beer Bar';
        this.president = (root as unknown as { president?: string }).president;
        this.catalogLink = root._links['beers'];
        this.loadCatalog();
      }, error: () => this.error = 'Impossible de joindre le bar. Démarrez d’abord Spring Boot.'
    });
  }

  loadCatalog(): void {
    if (!this.catalogLink) return;
    this.api.follow<{ _embedded?: { beerModelList?: Beer[] } }>(this.catalogLink).subscribe({
      next: collection => this.beers = collection._embedded?.beerModelList ?? [],
      error: () => this.error = 'Impossible de charger le catalogue.'
    });
  }

  openBeer(summary: Beer): void {
    this.api.follow<Beer>(summary._links['self']).subscribe({
      next: beer => {
        this.selectedBeer = beer;
        this.newName = beer.name;
        this.newPrice = beer.price;
        },
      error: () => this.error = 'Impossible de charger cette bière.'
    });
  }

  rename(link: HateoasResource): void {
    this.api.rename(link, this.newName).subscribe(beer => this.refresh(beer));
  }

  changePrice(link: HateoasResource): void {
    this.api.changePrice(link, this.newPrice).subscribe(beer => this.refresh(beer));
  }

  order(link: HateoasResource): void {
    this.api.order(link).subscribe(beer => this.refresh(beer));
  }

  private refresh(beer: Beer): void {
    this.selectedBeer = beer;
    this.loadCatalog();
  }
}
