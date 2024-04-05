package com.virginonline.waliot.service;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface LocationService {
  ObjectNode getLocation(String geocode);
}
