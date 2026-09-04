package org.example.model.enums;

public enum MemberLevel {
    GOLD(500,7,30,400),
    SILVER(300,5,25,300),
    BRONZE(100,3,20,200);

    private double fee;
    private int bookLimit;
    private int returnLimit;
    private int membershipDays;
    MemberLevel(double fee, int bookLimit, int returnLimit,int membershipDays) {
        this.fee = fee;
        this.bookLimit = bookLimit;
        this.returnLimit = returnLimit;
        this.membershipDays = membershipDays;
    }
    public double getFee() {
        return fee;
    }
    public int getBookLimit() {
        return bookLimit;
    }
    public int getReturnLimit() {
        return returnLimit;
    }
    public int getMembershipDays() {return membershipDays;}
}
