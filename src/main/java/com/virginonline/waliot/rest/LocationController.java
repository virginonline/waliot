package com.virginonline.waliot.rest;

import com.virginonline.waliot.dto.GeoLocatorDto;
import com.virginonline.waliot.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LocationController {

  private final LocationService locationService;

  @GetMapping
  public ResponseEntity<GeoLocatorDto> getLocation(@RequestParam(name = "geocode") String geocode) {
    return ResponseEntity.ok(locationService.getLocation(geocode));
  }
}
