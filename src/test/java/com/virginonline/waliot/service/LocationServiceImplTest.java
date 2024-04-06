package com.virginonline.waliot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.virginonline.waliot.apiclient.YandexApiClient;
import com.virginonline.waliot.dto.GeoLocatorDto;
import com.virginonline.waliot.dto.GeoLocatorDto.ResponseDto;
import com.virginonline.waliot.dto.GeoLocatorDto.ResponseDto.GeoObjectCollectionDto;
import com.virginonline.waliot.dto.GeoLocatorDto.ResponseDto.GeoObjectCollectionDto.FeatureMemberDto;
import com.virginonline.waliot.dto.GeoLocatorDto.ResponseDto.GeoObjectCollectionDto.FeatureMemberDto.GeoObjectDto;
import com.virginonline.waliot.dto.GeoLocatorDto.ResponseDto.GeoObjectCollectionDto.FeatureMemberDto.GeoObjectDto.PointDto;
import com.virginonline.waliot.exception.CoordinatesException;
import com.virginonline.waliot.exception.LocationNotFound;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

  @Mock private YandexApiClient mockYandexApiClient;

  private LocationServiceImpl locationServiceImplUnderTest;

  @BeforeEach
  void setUp() {
    locationServiceImplUnderTest = new LocationServiceImpl(mockYandexApiClient);
  }

  @Test
  @DisplayName("Should get location from geocode")
  void testGetLocation() {
    // Arrange
    final Optional<GeoLocatorDto> geoLocatorDto =
        Optional.of(
            new GeoLocatorDto(
                new ResponseDto(
                    new GeoObjectCollectionDto(
                        List.of(
                            new FeatureMemberDto(
                                new GeoObjectDto(new PointDto("pos"), "name", "description")))))));
    when(mockYandexApiClient.get("geocode")).thenReturn(geoLocatorDto);
    final GeoLocatorDto expectedResult =
        new GeoLocatorDto(
            new ResponseDto(
                new GeoObjectCollectionDto(
                    List.of(
                        new FeatureMemberDto(
                            new GeoObjectDto(new PointDto("pos"), "name", "description"))))));

    // Act
    final GeoLocatorDto result = locationServiceImplUnderTest.getLocation("geocode");

    // Assert
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Should throw LocationNotFound")
  void testGetLocation_YandexApiClientReturnsAbsent() {
    // Arrange
    when(mockYandexApiClient.get("geocode")).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> locationServiceImplUnderTest.getLocation("geocode"))
        .isInstanceOf(LocationNotFound.class);
  }

  @Test
  @DisplayName("Should get location from coordinates")
  void testGetLocationFromCoordinates() {
    // Arrange
    final Optional<GeoLocatorDto> geoLocatorDto =
        Optional.of(
            new GeoLocatorDto(
                new ResponseDto(
                    new GeoObjectCollectionDto(
                        List.of(
                            new FeatureMemberDto(
                                new GeoObjectDto(
                                    new PointDto("25.197300 55.274243"),
                                    "Name",
                                    "Description")))))));
    when(mockYandexApiClient.get("%f,%f".formatted(25.197300, 55.274243)))
        .thenReturn(geoLocatorDto);
    final GeoLocatorDto expectedResult =
        new GeoLocatorDto(
            new ResponseDto(
                new GeoObjectCollectionDto(
                    List.of(
                        new FeatureMemberDto(
                            new GeoObjectDto(
                                new PointDto("25.197300 55.274243"), "Name", "Description"))))));

    // Act
    final GeoLocatorDto result = locationServiceImplUnderTest.getLocation("25.197300,55.274243");

    // Assert
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Should get location from street")
  void testGetLocationFromStreet() {
    // Arrange
    final Optional<GeoLocatorDto> geoLocatorDto =
        Optional.of(
            new GeoLocatorDto(
                new ResponseDto(
                    new GeoObjectCollectionDto(
                        List.of(
                            new FeatureMemberDto(
                                new GeoObjectDto(null, "Name", "Description")))))));
    when(mockYandexApiClient.get("бул+Мухаммед+Бин+Рашид,+дом+1")).thenReturn(geoLocatorDto);
    final GeoLocatorDto expectedResult =
        new GeoLocatorDto(
            new ResponseDto(
                new GeoObjectCollectionDto(
                    List.of(new FeatureMemberDto(new GeoObjectDto(null, "Name", "Description"))))));

    // Act
    final GeoLocatorDto result =
        locationServiceImplUnderTest.getLocation("бул+Мухаммед+Бин+Рашид,+дом+1");

    // Assert
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  @DisplayName("Should throw IllegalArgumentException")
  void testGetLocationWithNullGeocode() {
    // Act & Assert
    assertThatThrownBy(() -> locationServiceImplUnderTest.getLocation(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Geocode cannot be null or empty");
    assertThatThrownBy(() -> locationServiceImplUnderTest.getLocation(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Geocode cannot be null or empty");
  }

  @Test
  @DisplayName("Should throw LocationNotFound")
  void testGetLocationWithInvalidGeocode() {
    // Arrange
    final String invalidGeocode = "invalid_geocode";

    // Act & Assert
    assertThatThrownBy(() -> locationServiceImplUnderTest.getLocation(invalidGeocode))
        .isInstanceOf(LocationNotFound.class)
        .hasMessage("Could not get geocode from %s".formatted(invalidGeocode));
  }

  @Test
  @DisplayName("Should throw CoordinatesException")
  void testGetLocationWithInvalidCoordinates() {
    // Arrange
    final String invalidCoordinates = "1000.333,2000.333";

    // Act & Assert
    assertThatThrownBy(() -> locationServiceImplUnderTest.getLocation(invalidCoordinates))
        .isInstanceOf(CoordinatesException.class);
  }
}
