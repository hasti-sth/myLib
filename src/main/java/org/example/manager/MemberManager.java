package org.example.manager;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MemberManager {
    private final double membershipFee;
    private final int membershipDays;
    private LocalDate lastRenewalDate;

    public MemberManager(double membershipFee, int membershipDays) {
        this.membershipFee = membershipFee;
        this.membershipDays = membershipDays;
        lastRenewalDate = LocalDate.now();
    }
    public void renewMember() {
        lastRenewalDate = LocalDate.now();
    }
    public LocalDate getLastRenewalDate() {
        return lastRenewalDate;
    }
    public double getMembershipFee() {
        return membershipFee;
    }
    public int getMembershipDays() {
        return membershipDays;
    }
    public int getRemainDays() {
        return (int)(membershipDays-ChronoUnit.DAYS.between(lastRenewalDate, LocalDate.now()));
    }
    public boolean hasMembership(){return getRemainDays()>0;}
}
