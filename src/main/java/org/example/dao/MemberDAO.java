package org.example.dao;

import org.example.database.DatabaseConnection;
import org.example.model.Member;
import org.example.model.enums.MemberLevel;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

    @Repository
    public class MemberDAO {

        public void addMember(Member member) {

            String sql = """
                INSERT INTO Members
                (name, password, memberLevel, lastRenewalDate)
                VALUES (?, ?, ?, ?)
                RETURNING id
                """;

            try (
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement statement = conn.prepareStatement(sql)
            ) {

                statement.setString(1, member.getName());
                statement.setString(2, member.getPassword());
                statement.setString(3, member.getLevel().name());
                statement.setDate(
                        4,
                        java.sql.Date.valueOf(member.getLastRenewalDate())
                );

                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    member.setId(rs.getInt("id"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public Member getMemberById(int id) {

            String sql = """
                SELECT *
                FROM Members
                WHERE id = ?
                """;

            try (
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement statement = conn.prepareStatement(sql)
            ) {

                statement.setInt(1, id);

                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    return mapResultSetToMember(rs);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        public Member getMemberByUsername(String username) {

            String sql = """
                SELECT *
                FROM Members
                WHERE name = ?
                """;

            try (
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement statement = conn.prepareStatement(sql)
            ) {

                statement.setString(1, username);

                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    return mapResultSetToMember(rs);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        public boolean updateMember(Member member) {

            String sql = """
                UPDATE Members
                SET name = ?,
                    memberLevel = ?,
                    lastRenewalDate = ?
                WHERE id = ?
                """;

            try (
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement statement = conn.prepareStatement(sql)
            ) {

                statement.setString(1, member.getName());
                statement.setString(2, member.getLevel().name());
                statement.setDate(
                        3,
                        java.sql.Date.valueOf(member.getLastRenewalDate())
                );
                statement.setInt(4, member.getId());

                return statement.executeUpdate() > 0;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }


        public boolean deleteMember(int id) {

            String sql = """
                DELETE FROM Members
                WHERE id = ?
                """;

            try (
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement statement = conn.prepareStatement(sql)
            ) {

                statement.setInt(1, id);

                return statement.executeUpdate() > 0;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }


        private Member mapResultSetToMember(ResultSet rs) throws Exception {

            int id = rs.getInt("id");

            String name = rs.getString("name");
            String password = rs.getString("password");

            MemberLevel level =
                    MemberLevel.valueOf(
                            rs.getString("memberLevel")
                    );

            LocalDate lastRenewalDate =
                    rs.getDate("lastRenewalDate").toLocalDate();

            Member member =
                    new Member(
                            name,
                            password,
                            level,
                            lastRenewalDate
                    );

            member.setId(id);

            return member;
        }
    }


