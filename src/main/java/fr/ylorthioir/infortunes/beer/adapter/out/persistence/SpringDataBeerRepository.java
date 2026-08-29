package fr.ylorthioir.infortunes.beer.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataBeerRepository extends JpaRepository<BeerJpaEntity, Long> { }
