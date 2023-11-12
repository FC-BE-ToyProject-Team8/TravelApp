package kr.co.fastcampus.travel.common.secure.domain;

import lombok.Builder;

@Builder
public class Token {

  private String grantType;
  private String accessToken;
  private String refreshToken;
}
