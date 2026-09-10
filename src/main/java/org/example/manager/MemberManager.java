package org.example.manager;

import org.example.dao.MemberDAO;
import org.example.model.Member;
import org.example.model.enums.MemberLevel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MemberManager {
    private final MemberDAO memberDAO;

    public MemberManager() {
        memberDAO = new MemberDAO();
    }

    public void renewMember(Member member) {
        member.setLastRenewalDate(LocalDate.now());
        memberDAO.updateMember(member);
    }

    public static int getRemainDays(Member member) {
        return (int)(member.getMembershipDays()-ChronoUnit.DAYS.between(member.getLastRenewalDate(), LocalDate.now()));
    }

    public static boolean hasMembership(Member member){
        return getRemainDays(member)>0;
    }

    public void addMember (String name, String password, MemberLevel level) {
    if(isValidUsername(name)) {
        if(memberDAO.getAllUserNames().stream().noneMatch(x -> x.equals(name))) {
            Member member=new Member(name,password,level,LocalDate.now());
            memberDAO.addMember(member);
            System.out.println("you signedUp successfully");
        }
        else System.out.println("Member already exists");
    }
    else System.out.println("Invalid username-or-password");
    }

    public Member theMemberLoggedIn(String username, String password){
        return memberDAO.getMember(username,password);
    }

    public static boolean isValidUsername(String username) {
    return username != null && username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

}
