package com.kh.Final_Project.recipes.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rcpSeq;
    private String rcpNm;
    private String rcpWay2;
    private String rcpPat2;
    private String infoWgt;
    private String infoEng;
    private String infoCar;
    private String infoPro;
    private String infoFat;
    private String infoNa;
    private String hashTag;
    private String attFileNoMain;
    private String attFileNoMk;
    @Column(length = 1000) // 혹은 적절한 최대 길이로 조정
    private String rcpPartsDtls;

    private String manual01;
    private String manualImg01;
    private String manual02;
    private String manualImg02;
    private String manual03;
    private String manualImg03;
    private String manual04;
    private String manualImg04;
    private String manual05;
    private String manualImg05;
    private String manual06;
    private String manualImg06;
    private String manual07;
    private String manualImg07;
    private String manual08;
    private String manualImg08;
    private String manual09;
    private String manualImg09;
    private String manual10;
    private String manualImg10;
    private String manual11;
    private String manualImg11;
    private String manual12;
    private String manualImg12;
    private String manual13;
    private String manualImg13;
    private String manual14;
    private String manualImg14;
    private String manual15;
    private String manualImg15;
    private String manual16;
    private String manualImg16;
    private String manual17;
    private String manualImg17;
    private String manual18;
    private String manualImg18;
    private String manual19;
    private String manualImg19;
    private String manual20;
    private String manualImg20;

    private String rcpNaTip; // 저감 조리법 TIP
}
