package kr.co.fastcampus.travel.domain.secure;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
public class Token {

  private String grantType;
  private String accessToken;
  private String refreshToken;
}
