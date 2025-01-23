package com.globallogic.usuarios.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneRequest {
  private Long number;

  @JsonProperty(value = "citycode")
  private Integer cityCode;

  @JsonProperty(value = "countrycode")
  private String countryCode;
}
