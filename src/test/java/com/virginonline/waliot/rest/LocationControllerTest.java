package com.virginonline.waliot.rest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.virginonline.waliot.dto.GeoLocatorDto;
import com.virginonline.waliot.dto.GeoLocatorDto.ResponseDto;
import com.virginonline.waliot.dto.GeoLocatorDto.ResponseDto.GeoObjectCollectionDto;
import com.virginonline.waliot.dto.GeoLocatorDto.ResponseDto.GeoObjectCollectionDto.FeatureMemberDto;
import com.virginonline.waliot.dto.GeoLocatorDto.ResponseDto.GeoObjectCollectionDto.FeatureMemberDto.GeoObjectDto;
import com.virginonline.waliot.dto.GeoLocatorDto.ResponseDto.GeoObjectCollectionDto.FeatureMemberDto.GeoObjectDto.PointDto;
import com.virginonline.waliot.service.LocationService;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LocationController.class)
class LocationControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private LocationService locationService;

  @Test
  @DisplayName("Get location from coordinates")
  public void testGetLocation_Success_From_Coordinates() throws Exception {

    // Arrange
    GeoLocatorDto expectedGeoLocatorDto =
        new GeoLocatorDto(
            new ResponseDto(
                new GeoObjectCollectionDto(
                    Collections.singletonList(
                        new FeatureMemberDto(
                            new GeoObjectDto(
                                new PointDto("55.274247 25.19718"),
                                "1, Мухаммед Бин Рашид бульвар",
                                "Даунтаун Дубай, Заабиль, эмират Дубай, Объединенные Арабские Эмираты"))))));

    when(locationService.getLocation(anyString())).thenReturn(expectedGeoLocatorDto);

    // Act & Assert
    mockMvc
        .perform(get("/").param("geocode", "55.275154,25.197115"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.response").exists())
        .andExpect(jsonPath("$.response.GeoObjectCollection").exists())
        .andExpect(jsonPath("$.response.GeoObjectCollection.featureMember").isArray())
        .andExpect(
            jsonPath("$.response.GeoObjectCollection.featureMember[0].GeoObject.name")
                .value("1, Мухаммед Бин Рашид бульвар"))
        .andExpect(
            jsonPath("$.response.GeoObjectCollection.featureMember[0].GeoObject.description")
                .value("Даунтаун Дубай, Заабиль, эмират Дубай, Объединенные Арабские Эмираты"));

    // Assert
    verify(locationService, times(1)).getLocation("55.275154,25.197115");
  }

  @Test
  @DisplayName("Get location from street")
  public void testGetLocation_Success_From_Street() throws Exception {

    // Arrange
    String streetAndHouse = "бул+Мухаммед+Бин+Рашид,+дом+1";
    GeoLocatorDto expectedGeoLocatorDto =
        new GeoLocatorDto(
            new ResponseDto(
                new GeoObjectCollectionDto(
                    Collections.singletonList(
                        new FeatureMemberDto(
                            new GeoObjectDto(
                                new PointDto("55.274247 25.19718"),
                                "1, Мухаммед Бин Рашид бульвар",
                                "Даунтаун Дубай, Заабиль, эмират Дубай, Объединенные Арабские Эмираты"))))));
    when(locationService.getLocation(streetAndHouse)).thenReturn(expectedGeoLocatorDto);

    // Act & Assert
    mockMvc
        .perform(get("/").param("geocode", streetAndHouse))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.response").exists())
        .andExpect(jsonPath("$.response.GeoObjectCollection").exists())
        .andExpect(jsonPath("$.response.GeoObjectCollection.featureMember").isArray())
        .andExpect(
            jsonPath("$.response.GeoObjectCollection.featureMember[0].GeoObject.name")
                .value("1, Мухаммед Бин Рашид бульвар"))
        .andExpect(
            jsonPath("$.response.GeoObjectCollection.featureMember[0].GeoObject.description")
                .value("Даунтаун Дубай, Заабиль, эмират Дубай, Объединенные Арабские Эмираты"));

    // Assert
    verify(locationService, times(1)).getLocation(streetAndHouse);
  }
}
