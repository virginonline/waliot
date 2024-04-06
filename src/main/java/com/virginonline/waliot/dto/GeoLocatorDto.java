package com.virginonline.waliot.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

/**
 * Geolocator response object
 *
 * @param response object with coordinates
 * @see <a href="https://yandex.com/dev/geocode/doc/ru/response#params">Geocoder response
 *     parameters</a>
 */
public record GeoLocatorDto(@JsonProperty("response") ResponseDto response)
    implements Serializable {

  @JsonCreator
  public GeoLocatorDto {}

  public record ResponseDto(
      @JsonProperty("GeoObjectCollection") GeoObjectCollectionDto geoObjectCollection) {

    @JsonCreator
    public ResponseDto {}

    public record GeoObjectCollectionDto(
        @JsonProperty("featureMember") List<FeatureMemberDto> featureMemberList) {

      @JsonCreator
      public GeoObjectCollectionDto {}

      public record FeatureMemberDto(@JsonProperty("GeoObject") GeoObjectDto geoObject) {

        @JsonCreator
        public FeatureMemberDto {}

        public record GeoObjectDto(
            @JsonProperty("Point") PointDto point,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description) {

          @JsonCreator
          public GeoObjectDto {}

          public record PointDto(@JsonProperty("pos") String pos) {

            @JsonCreator
            public PointDto {}
          }
        }
      }
    }
  }
}
