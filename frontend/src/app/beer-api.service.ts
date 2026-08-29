import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface HateoasResource {
  href: string;
}

export interface Beer {
  id: number;
  name: string;
  brewery: string;
  style: string;
  price: number;
  stock: number;
  _links: Record<string, HateoasResource>;
}

interface BeerCollection {
  _embedded?: { beerModelList?: Beer[] };
  _links: Record<string, HateoasResource>;
}

@Injectable({ providedIn: 'root' })
export class BeerApiService {
  private readonly http = inject(HttpClient);
  private readonly rootUrl = 'http://localhost:8080/api';

  getRoot(): Observable<BeerCollection> {
    return this.http.get<BeerCollection>(this.rootUrl);
  }

  follow<T>(link: HateoasResource): Observable<T> {
    return this.http.get<T>(this.resolve(link));
  }

  rename(link: HateoasResource, name: string): Observable<Beer> {
    return this.http.patch<Beer>(this.resolve(link), { name });
  }

  changePrice(link: HateoasResource, price: number): Observable<Beer> {
    return this.http.patch<Beer>(this.resolve(link), { price });
  }

  order(link: HateoasResource): Observable<Beer> {
    return this.http.post<Beer>(this.resolve(link), {});
  }

  private resolve(link: HateoasResource): string {
    // Un lien HAL peut être absolu ou relatif. URL garantit qu'un lien relatif
    // reste appelé par le backend et non par le serveur de développement Angular.
    const url = new URL(link.href, this.rootUrl);

    // En développement local, Spring peut générer http://localhost/... sans
    // reporter le port ; on complète alors le port du backend connu par le client.
    if (url.hostname === 'localhost' && !url.port) {
      url.port = '8080';
    }
    return url.toString();
  }
}
