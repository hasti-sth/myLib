package org.example.manager;

import org.example.model.Book;
import org.example.model.Member;
import org.example.model.enums.MemberLevel;

import java.util.HashMap;

public class Manager {
    HashMap<Member, BorrowManager>  memberBooks;

    public Manager() {
        memberBooks = new HashMap<>();
    }
    public void addMember (String name, String password, MemberLevel level) {
           if(isValidUsername(name)&&isValidPassword(password)) {
               if(memberBooks.keySet().stream().anyMatch(x -> x.getName().equals(name))) {
                   memberBooks.put(new Member(name , password,level,new MemberManager(level.getFee(),level.getMembershipDays())),new BorrowManager(level.getBookLimit(), level.getReturnLimit()));
                   System.out.println("you signedUp successfully");
               }
               else System.out.println("Member already exists");
           }
           else System.out.println("Invalid username-or-password");
        }
        public static boolean isValidUsername(String username) {
            return username != null && username.matches("^[a-zA-Z0-9_]{3,20}$");
        }

        public static boolean isValidPassword(String password) {
            return password != null && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+]).{8,20}$");
        }
    public void borrowBook(Book book, Member  member){
        if(canBorrowBook(book,member)){
            memberBooks.get(member).borrowBook(book);
        }
    }
    public void returnBook(Book book,Member  member){
        memberBooks.get(member).returnBook(book);
    }
    public boolean canBorrowBook(Book book,Member  member){
     return member.getMemberManager().hasMembership();
    }
    public Member theMemberLoggedIn(String username, String password){
        for(Member member : memberBooks.keySet()){
            if(member.getName().equals(username) && member.getPassword().equals(password)){
                return member;
            }
        }
        return null;
    }
    public BorrowManager getBorrowManager(Member member){
            return memberBooks.get(member);
    }
    }

