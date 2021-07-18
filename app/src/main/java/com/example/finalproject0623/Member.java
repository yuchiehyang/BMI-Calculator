package com.example.finalproject0623;

//CHECKBOX: RECYCLERVIEW 採用的類別
public class Member {
    private String memberName;
    private int age;
    private int bmi;


    public Member(){
        //empty constructor needed
    }

    public String getMemberName() {
        return memberName;
    }

    public int getAge() {
        return age;
    }

    public int getBmi() {
        return bmi;
    }

    public Member(String memberName, int age, int bmi){
        this.memberName = memberName;
        this.age = age;
        this.bmi = bmi;
    }
}
