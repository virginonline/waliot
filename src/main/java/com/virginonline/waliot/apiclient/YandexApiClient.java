package com.virginonline.waliot.apiclient;

import com.virginonline.waliot.dto.GeoLocatorDto;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "yandexapiclient", url = "${yandex.url}?apikey=${yandex.key}&format=json")
public interface YandexApiClient {
  @GetMapping
  Optional<GeoLocatorDto> get(@RequestParam String geocode);
}
