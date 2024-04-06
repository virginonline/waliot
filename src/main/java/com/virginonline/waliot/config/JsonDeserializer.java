package com.virginonline.waliot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virginonline.waliot.dto.GeoLocatorDto;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

@RequiredArgsConstructor
public class JsonDeserializer implements RedisSerializer<GeoLocatorDto> {

  private final ObjectMapper objectMapper;

  @Override
  public byte[] serialize(GeoLocatorDto geoLocatorDto) throws SerializationException {
    try {
      return objectMapper.writeValueAsBytes(geoLocatorDto);
    } catch (IOException e) {
      throw new SerializationException("Error serializing GeoLocatorDto", e);
    }
  }

  @Override
  public GeoLocatorDto deserialize(byte[] bytes) throws SerializationException {
    try {
      return objectMapper.readValue(bytes, GeoLocatorDto.class);
    } catch (IOException e) {
      throw new SerializationException("Error deserializing GeoLocatorDto", e);
    }
  }
}
