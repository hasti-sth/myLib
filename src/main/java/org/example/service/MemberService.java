package org.example.service;

import org.example.dao.MemberDAO;
import org.example.model.Member;
import org.example.model.enums.MemberLevel;
import org.example.util.PasswordUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class MemberService {

    private final MemberDAO memberDAO;

    public MemberService(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }


    public Member getMemberById(int id) {
        return memberDAO.getMemberById(id);
    }


    public Member getMemberByUsername(String username) {
        return memberDAO.getMemberByUsername(username);
    }


    public Member addMember(
            String name,
            String password,
            MemberLevel level) {

        if (!isValidUsername(name)) {
            throw new IllegalArgumentException(
                    "Invalid username"
            );
        }

        if (memberDAO.getMemberByUsername(name) != null) {
            throw new IllegalArgumentException(
                    "Member already exists"
            );
        }

        Member member =
                new Member(
                        name,
                        PasswordUtil.hashPassword(password),
                        level,
                        LocalDate.now()
                );

        memberDAO.addMember(member);

        return member;
    }


    public boolean updateMember(Member member) {
        return memberDAO.updateMember(member);
    }


    public boolean deleteMember(int id) {
        return memberDAO.deleteMember(id);
    }


    public void renewMember(Member member) {

        member.setLastRenewalDate(LocalDate.now());

        memberDAO.updateMember(member);
    }


    public int getRemainDays(Member member) {

        return (int) (
                member.getMembershipDays()
                        - ChronoUnit.DAYS.between(
                        member.getLastRenewalDate(),
                        LocalDate.now()
                )
        );
    }


    public boolean hasMembership(Member member) {

        return getRemainDays(member) > 0;
    }


    public boolean isValidUsername(String username) {

        return username != null
                && username.matches(
                "^[a-zA-Z0-9_]{3,20}$"
        );
    }
}
