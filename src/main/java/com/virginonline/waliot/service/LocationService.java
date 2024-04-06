package com.virginonline.waliot.service;

import com.virginonline.waliot.dto.GeoLocatorDto;

public interface LocationService {
  GeoLocatorDto getLocation(String geocode);
}
