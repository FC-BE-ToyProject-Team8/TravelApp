package kr.co.fastcampus.travel.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.co.fastcampus.travel.common.baseentity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 30, unique = true)
    private String nickname;

    @Column(nullable = false, length = 1000)
    private String password;
}
