package com.virginonline.waliot.service;

import com.virginonline.waliot.apiclient.YandexApiClient;
import com.virginonline.waliot.dto.GeoLocatorDto;
import com.virginonline.waliot.exception.CoordinatesException;
import com.virginonline.waliot.exception.LocationNotFound;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

  private final YandexApiClient yandexApiClient;

  @Cacheable(value = "location", key = "#geocode")
  @Override
  public GeoLocatorDto getLocation(String geocode) {
    if (geocode == null || geocode.isEmpty()) {
      throw new IllegalArgumentException("Geocode cannot be null or empty");
    }

    if (isCoordinates(geocode)) {
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
  private GeoLocatorDto getLocationFromStreet(String geocode) {
    log.info("Geocode: {}", geocode);

    return yandexApiClient
        .get(geocode)
        .orElseThrow(
            () -> new LocationNotFound("Could not get geocode from %s".formatted(geocode)));
  }

  /**
   * Get coordinates from longitude and latitude
   *
   * @param lat
   * @param lon
   * @return object with coordinates
   */
  private GeoLocatorDto getLocationFromCoordinates(double lat, double lon) {
    log.info("Latitude: {}, Longitude: {}", lat, lon);
    // Latitude must be a number between -90 and 90 and Longitude must be a number between -180 and
    // 180
    if (Math.abs(lat) <= 90 && Math.abs(lon) <= 180) {
      return yandexApiClient
          .get("%f,%f".formatted(lat, lon))
          .orElseThrow(
              () ->
                  new LocationNotFound("Could not get coordinates from %f,%f".formatted(lat, lon)));
    } else {
      throw new CoordinatesException(
          "lat and lon must be between -90 and 90 and -180 and 180 \n lat: %f lon: %f"
              .formatted(lat, lon));
    }
  }

  /**
   * Check if geocode contains coordinates
   *
   * @param geocode
   * @return true if geocode contains coordinates
   */
  private boolean isCoordinates(String geocode) {
    String[] parts = geocode.split(",");
    if (parts.length == 2) {
      try {
        Arrays.stream(parts).mapToDouble(Double::parseDouble).toArray();
        return true;
      } catch (NumberFormatException e) {
        return false;
      }
    }
    return false;
  }
}
