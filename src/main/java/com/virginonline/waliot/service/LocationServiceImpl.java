package com.virginonline.waliot.service;

import static com.virginonline.waliot.utils.GeocodeValidation.isCoordinates;
import static com.virginonline.waliot.utils.GeocodeValidation.isValidCoordinates;
import static com.virginonline.waliot.utils.GeocodeValidation.parseCoordinates;
import static com.virginonline.waliot.utils.GeocodeValidation.validateGeocode;

import com.virginonline.waliot.apiclient.YandexApiClient;
import com.virginonline.waliot.dto.GeoLocatorDto;
import com.virginonline.waliot.exception.CoordinatesException;
import com.virginonline.waliot.exception.LocationNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

  private final YandexApiClient yandexApiClient;

  /**
   * Get coordinates from geocode
   *
   * @param geocode geocode of the address or coordinates
   * @return {@link GeoLocatorDto} from cache or from api if not in cache
   */
  @Cacheable(value = "location", key = "#geocode")
  @Override
  public GeoLocatorDto getLocation(String geocode) {
    validateGeocode(geocode);
    if (isCoordinates(geocode)) {
      var coordinates = parseCoordinates(geocode);
      return getLocationFromCoordinates(coordinates[0], coordinates[1]);
    }
    return getLocationFromStreet(geocode);
  }

  /**
   * Get coordinates from geocode
   *
   * @param geocode geocode of the address
   * @return {@link GeoLocatorDto} with coordinates
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
   * @param lat latitude
   * @param lon longitude
   * @return {@link GeoLocatorDto} with coordinates
   */
  private GeoLocatorDto getLocationFromCoordinates(double lat, double lon) {
    log.info("Latitude: {}, Longitude: {}", lat, lon);
    if (isValidCoordinates(lat, lon)) {
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
}
