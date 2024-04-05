package com.virginonline.waliot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.virginonline.waliot.apiclient.YandexApiClient;
import com.virginonline.waliot.exception.LocationNotFound;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

  @Mock private YandexApiClient mockYandexApiClient;

  @InjectMocks private LocationServiceImpl locationServiceImplUnderTest;

  @BeforeEach
  void setUp() {
    locationServiceImplUnderTest = new LocationServiceImpl(mockYandexApiClient);
  }

  @Test
  void testGetLocationFromCoordinates() {
    // Arrange
    final ObjectNode expectedResult = new ObjectNode(new JsonNodeFactory(false));
    var coordinates =
        Arrays.stream("25.197300,55.274243".split(",")).mapToDouble(Double::parseDouble).toArray();
    // Act
    final Optional<ObjectNode> jsonNodes = Optional.of(new ObjectNode(new JsonNodeFactory(false)));
    when(mockYandexApiClient.get("%f,%f".formatted(coordinates[0], coordinates[1])))
        .thenReturn(jsonNodes);
    final ObjectNode result =
        locationServiceImplUnderTest.getLocation("%f,%f".formatted(coordinates[0], coordinates[1]));
    // Assert
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  void testGetLocationFromStreet() {
    // Arrange
    final ObjectNode expectedResult = new ObjectNode(new JsonNodeFactory(false));
    // Act
    final Optional<ObjectNode> jsonNodes = Optional.of(new ObjectNode(new JsonNodeFactory(false)));
    when(mockYandexApiClient.get("бул+Мухаммед+Бин+Рашид,+дом+1")).thenReturn(jsonNodes);
    final ObjectNode result =
        locationServiceImplUnderTest.getLocation("бул+Мухаммед+Бин+Рашид,+дом+1");
    // Assert
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  void testGetLocation_YandexApiClientReturnsPresent() {
    // Arrange
    final ObjectNode expectedResult = new ObjectNode(new JsonNodeFactory(false));

    // Act
    final Optional<ObjectNode> jsonNodes = Optional.of(new ObjectNode(new JsonNodeFactory(false)));
    when(mockYandexApiClient.get("geocode")).thenReturn(jsonNodes);
    final ObjectNode result = locationServiceImplUnderTest.getLocation("geocode");

    // Assert
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  void testGetLocation() {
    // Arrange
    final ObjectNode expectedResult = new ObjectNode(new JsonNodeFactory(false));

    // Act
    final Optional<ObjectNode> jsonNodes = Optional.of(new ObjectNode(new JsonNodeFactory(false)));
    when(mockYandexApiClient.get("geocode")).thenReturn(jsonNodes);
    final ObjectNode result = locationServiceImplUnderTest.getLocation("geocode");

    // Assert
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  void testGetLocation_YandexApiClientReturnsAbsent() {
    // Arrange
    when(mockYandexApiClient.get("geocode")).thenReturn(Optional.empty());

    // Act & Assert
    assertThatThrownBy(() -> locationServiceImplUnderTest.getLocation("geocode"))
        .isInstanceOf(LocationNotFound.class);
  }
}
