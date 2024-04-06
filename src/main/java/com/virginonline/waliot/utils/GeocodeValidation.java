package com.virginonline.waliot.utils;

import java.util.Arrays;

/** Validation methods for geocode */
public class GeocodeValidation {

  /**
   * Validates the geocode.
   *
   * @param geocode the geocode to be validated
   */
  public static void validateGeocode(String geocode) {
    if (geocode == null || geocode.isEmpty()) {
      throw new IllegalArgumentException("Geocode cannot be null or empty");
    }
  }

  /**
   * Check if geocode contains coordinates
   *
   * @param geocode geocode of coordinates
   * @return true if geocode contains coordinates
   */
  public static boolean isCoordinates(String geocode) {
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

  /**
   * A function that parses a string of coordinates separated by commas into an array of doubles.
   *
   * @param geocode the string containing the coordinates to be parsed
   * @return an array of doubles containing the parsed coordinates
   */
  public static double[] parseCoordinates(String geocode) {
    return Arrays.stream(geocode.split(",")).mapToDouble(Double::parseDouble).toArray();
  }

  /**
   * @see <a href="https://en.wikipedia.org/wiki/Latitude#The_graticule_on_the_sphere">Latitude must
   *     be a number between -90 and 90 and Longitude must be a number between -180 and 180 </a>
   */
  public static boolean isValidCoordinates(double lat, double lon) {
    return Math.abs(lat) <= 90 && Math.abs(lon) <= 180;
  }
}
