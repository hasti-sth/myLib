package org.example.dao;

import org.example.database.DatabaseConnection;
import org.example.model.Member;
import org.example.model.enums.MemberLevel;
import org.example.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    public void addMember(Member member) {
        String sql= """
                INSERT INTO Members
                (name,password,memberLevel,lastRenewalDate)
                VALUES(?,?,?,?)
                RETURNING id
                """;
        try(
                Connection conn= DatabaseConnection.getConnection();
                PreparedStatement statement=conn.prepareStatement(sql);
        ){
            statement.setString(1, member.getName());
            statement.setString(2,member.getPassword());
            statement.setString(3,member.getLevel().name());
            statement.setDate(4,java.sql.Date.valueOf(member.getLastRenewalDate()));

            ResultSet rs=statement.executeQuery();
            if(rs.next()) member.setId(rs.getInt("id"));

        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public void updateMember(Member member) {
        String sql= """
                UPDATE Members
                SET memberLevel=?,lastRenewalDate=?
                WHERE id=?;
        """;

        try(
                Connection conn= DatabaseConnection.getConnection();
                PreparedStatement statement=conn.prepareStatement(sql);
        ){
            statement.setString(1,member.getLevel().name());
            statement.setDate(2,java.sql.Date.valueOf(member.getLastRenewalDate()));
            statement.setInt(3,member.getId());

            statement.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public void deleteMember(Member member) {
        String sql= """
                DELETE FROM Members
                WHERE id=?;
        """;

        try(
                Connection conn= DatabaseConnection.getConnection();
                PreparedStatement statement=conn.prepareStatement(sql);
        ){
            statement.setInt(1,member.getId());

            statement.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public List<String> getAllUserNames() {
        List<String> list = new ArrayList<>();
        String sql="""
                SELECT name FROM Members
        """;

        try(
                Connection conn= DatabaseConnection.getConnection();
                PreparedStatement statement=conn.prepareStatement(sql);
        ){
            ResultSet rs=statement.executeQuery();
            while(rs.next()){
                list.add(rs.getString("name"));
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        return list;
    }

    public Member getMember(String username,String password){
        Member member=null;
        String sql="""
                SELECT*FROM Members
                WHERE name=?;
        """;
        try(
                Connection conn= DatabaseConnection.getConnection();
                PreparedStatement statement=conn.prepareStatement(sql);
        ){
            statement.setString(1,username);

            ResultSet rs=statement.executeQuery();
            if(rs.next()){
            String pass=rs.getString("password");
            //if(PasswordUtil.checkPassword(password,pass)){

            LocalDate date=rs.getDate("lastRenewalDate").toLocalDate();
            MemberLevel level=MemberLevel.valueOf(rs.getString("memberLevel"));

            member=new Member(username,password,level,date);
        }}        catch(Exception e){
            System.out.println(e);
        }
        return member;
    }
    }


