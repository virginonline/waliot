package com.virginonline.waliot.apiclient;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "yandexapiclient", url = "${yandex.url}?apikey=${yandex.key}&format=json")
public interface YandexApiClient {

  @GetMapping
  ObjectNode get(@RequestParam String geocode);
}