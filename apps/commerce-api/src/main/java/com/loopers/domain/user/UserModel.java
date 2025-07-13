package com.loopers.domain.user;


import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class UserModel extends BaseEntity {

    private String userId;
    private String emil;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate birthday;

    protected UserModel(String userId, String emil, Gender gender, LocalDate birthday) {
        this.userId = userId;
        this.emil = emil;
        this.gender = gender;
        this.birthday = birthday;
    }

}
