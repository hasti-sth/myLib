package org.example.model;

import org.example.model.enums.MemberLevel;
import org.example.manager.MemberManager;

public class Member {
    private final String name ;
    private final String password ;
    private MemberManager memberManager;
    private final MemberLevel level;


    public Member(String name, String password, MemberLevel level, MemberManager memberManager) {
        this.name = name;
        this.password = password;
        this.level = level;
        this.memberManager = memberManager;
    }

    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public MemberLevel getLevel() {return level;}
     public MemberManager getMemberManager() {return memberManager;}
}
