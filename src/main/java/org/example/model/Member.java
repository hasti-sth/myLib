package org.example.model;

import org.example.model.enums.MemberLevel;
import org.example.manager.MemberManager;

import java.time.LocalDate;

public class Member {
    private int id;
    private final String name ;
    private final String password ;
    private LocalDate lastRenewalDate;
    private final MemberLevel level;


    public Member(String name, String password, MemberLevel level,LocalDate lastRenewalDate) {
        this.name = name;
        this.password = password;
        this.level = level;
        this.lastRenewalDate=lastRenewalDate;
    }

    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public MemberLevel getLevel() {return level;}
    public LocalDate getLastRenewalDate() {return lastRenewalDate;}
    public void setLastRenewalDate(LocalDate lastRenewalDate) {this.lastRenewalDate = lastRenewalDate;}
    public int getId() {return id;}
    public void setId(int id){this.id=id;}
    public double getMembershipFee() {return level.getFee();}
    public int getMembershipDays() {return level.getMembershipDays();}
    public int getBookLimit(){return level.getBookLimit();}
    public int getReturnLimit(){return level.getReturnLimit();}
}
