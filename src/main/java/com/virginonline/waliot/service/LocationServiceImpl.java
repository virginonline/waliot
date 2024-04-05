package com.virginonline.waliot.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.virginonline.waliot.apiclient.YandexApiClient;
import com.virginonline.waliot.exception.CoordinatesException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

  private final YandexApiClient yandexApiClient;

  @Cacheable(value = "location", key = "geocode")
  @Override
  public ObjectNode getLocation(String geocode) {
    if (geocode == null || geocode.isEmpty()) {
      throw new IllegalArgumentException("Geocode cannot be null or empty");
    }

    if (geocode.contains(",")) {
      var coordinates =
          Arrays.stream(geocode.split(",")).mapToDouble(Double::parseDouble).toArray();
      return getLocationFromCoordinates(coordinates[0], coordinates[1]);
    }
    return getLocationFromStreet(geocode);
  }

  /**
   * Get coordinates from geocode
   *
   * @param geocode
   * @return object with coordinates
   */
  private ObjectNode getLocationFromStreet(String geocode) {
    // TODO
    return yandexApiClient.get(geocode);
  }

  /**
   * Get coordinates from longitude and latitude
   *
   * @param lat
   * @param lon
   * @return object with coordinates
   */
  private ObjectNode getLocationFromCoordinates(double lat, double lon) {
    // Latitude must be a number between -90 and 90 and Longitude must be a number between -180 and
    // 180
    if (Math.abs(lat) <= 90 && Math.abs(lon) <= 180) {
      return yandexApiClient.get("%f,%f".formatted(lat, lon));
    } else {
      throw new CoordinatesException(
          "lat and lon must be between -90 and 90 and -180 and 180 \n lat: %f lon: %f"
              .formatted(lat, lon));
    }
  }
}
