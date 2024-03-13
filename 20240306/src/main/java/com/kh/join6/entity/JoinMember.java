package com.kh.join6.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JoinMember {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String passwordConfirm;
    private String name;
    private String birthYear;
    private String birthMonth;
    private String birthDay;
    private String gender;
    private String phoneNumber;
}