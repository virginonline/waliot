package com.virginonline.waliot.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.virginonline.waliot.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LocationController {

  private final LocationService locationService;

  @GetMapping
  public ObjectNode getLocation(@RequestParam(name = "geocode") String geocode) {
    return locationService.getLocation(geocode);
  }
}
