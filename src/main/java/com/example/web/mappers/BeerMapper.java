package com.example.web.mappers;

import com.example.domain.Beer;
import com.example.web.model.BeerDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    uses = DateMapper.class,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BeerMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "beerName", target = "beerName")
  @Mapping(source = "beerStyle", target = "beerStyle")
  @Mapping(source = "version", target = "version")
  @Mapping(source = "upc", target = "upc")
  @Mapping(source = "price", target = "price")
  @Mapping(source = "createdDate", target = "createdDate")
  @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
  @Mapping(source = "quantityToBrew", target = "quantityOnHand")
  BeerDto beerToBeerDto(Beer beer);

  @InheritInverseConfiguration
  Beer beerDtoToBeer(BeerDto beerDto);
}
