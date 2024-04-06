package com.virginonline.waliot.apiclient;

import com.virginonline.waliot.dto.GeoLocatorDto;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client for Yandex Geocoder api
 *
 * @see <a href="https://yandex.com/dev/geocode/doc/ru/">Geocoder api docs</a>
 */
@FeignClient(value = "yandexapiclient", url = "${yandex.url}?apikey=${yandex.key}&format=json")
public interface YandexApiClient {

  /**
   * @param geocode geocode of the address or coordinates
   * @return optional GeoLocatorDto
   */
  @GetMapping
  Optional<GeoLocatorDto> get(@RequestParam String geocode);
}
