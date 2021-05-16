package com.example.web.controller;

import com.example.domain.Beer;
import com.example.repositories.BeerRepository;
import com.example.web.mappers.BeerMapper;
import com.example.web.model.BeerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

  private final BeerMapper beerMapper;
  private final BeerRepository beerRepository;

  public BeerController(BeerMapper beerMapper, BeerRepository beerRepository) {
    this.beerMapper = beerMapper;
    this.beerRepository = beerRepository;
  }

  @GetMapping("/{beerId}")
  public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId) {
    return beerRepository
        .findById(beerId)
        .map(beer -> new ResponseEntity<>(beerMapper.beerToBeerDto(beer), HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping
  public ResponseEntity<BeerDto> saveNewBeer(@RequestBody @Validated BeerDto beerDto) {
    Beer beerObject = beerRepository.save(beerMapper.beerDtoToBeer(beerDto));
    return Optional.of(beerObject)
        .filter(beer -> beer.getId() != null)
        .map(beer -> new ResponseEntity<>(beerMapper.beerToBeerDto(beer), HttpStatus.CREATED))
        .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
  }

  @PutMapping("/{beerId}")
  public ResponseEntity<BeerDto> updateBeerById(
      @PathVariable("beerId") UUID beerId, @RequestBody @Validated BeerDto beerDto) {
    Beer beerObject =
        beerRepository
            .findById(beerId)
            .map(
                beer -> {
                  beer.setBeerName(beerDto.getBeerName());
                  beer.setBeerStyle(beerDto.getBeerStyle().name());
                  beer.setPrice(beerDto.getPrice());
                  beer.setUpc(beerDto.getUpc());

                  return beerRepository.save(beer);
                })
            .orElse(null);
    return Optional.ofNullable(beerObject)
        .map(beer -> new ResponseEntity<>(beerMapper.beerToBeerDto(beer), HttpStatus.OK))
        .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }
}
