package SQL_CLASSES;

import java.sql.*;

public class DELETE_UPDATE {
    //  Delete a Professional by their ID
    public void deleteProf(int PROFESSIONALID, Connection conn) {
        String deleteLinks = "DELETE FROM SESSION_PROFESSIONAL WHERE PROFESSIONALLD = ?";
        String deletePro   = "DELETE FROM PROFESSIONAL WHERE PROFESSIONALLD = ?";
        try {
            PreparedStatement pstmt1 = conn.prepareStatement(deleteLinks);
            pstmt1.setInt(1, PROFESSIONALID);
            pstmt1.executeUpdate();

            PreparedStatement pstmt2 = conn.prepareStatement(deletePro);
            pstmt2.setInt(1, PROFESSIONALID);
            int rows = pstmt2.executeUpdate();

            if (rows > 0)
                System.out.println(" Professional with ID " + PROFESSIONALID + " deleted successfully.");
            else
                System.out.println(" No Professional found with ID " + PROFESSIONALID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //  Delete a Studio by its ID
    public void deleteStudio(int STUDIOID, Connection conn) {
        String deleteSessionEquipment = "DELETE FROM SESSION_EQUIPMENT WHERE SESSIONID IN (SELECT SESSIONID FROM SESSION WHERE STUDIOID = ?)";
        String deleteSessionProfessional = "DELETE FROM SESSION_PROFESSIONAL WHERE SESSIONID IN (SELECT SESSIONID FROM SESSION WHERE STUDIOID = ?)";
        String deleteSessions = "DELETE FROM SESSION WHERE STUDIOID = ?";
        String deleteStudio   = "DELETE FROM STUDIO WHERE STUDIOID = ?";
        try {
            PreparedStatement p1 = conn.prepareStatement(deleteSessionEquipment);
            p1.setInt(1, STUDIOID);
            p1.executeUpdate();

            PreparedStatement p2 = conn.prepareStatement(deleteSessionProfessional);
            p2.setInt(1, STUDIOID);
            p2.executeUpdate();

            PreparedStatement p3 = conn.prepareStatement(deleteSessions);
            p3.setInt(1, STUDIOID);
            p3.executeUpdate();

            PreparedStatement p4 = conn.prepareStatement(deleteStudio);
            p4.setInt(1, STUDIOID);
            int rows = p4.executeUpdate();

            if (rows > 0)
                System.out.println("✅ Studio with ID " + STUDIOID + " deleted successfully.");
            else
                System.out.println("⚠️ No Studio found with ID " + STUDIOID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Update the return condition of equipment in a session
    public void updateEquipmentCondition(int SESSIONID, int EQUIPMENTID, String newCondition, Connection conn) {
        String sql = "UPDATE SESSION_EQUIPMENT SET RETURNCONDITION = ? " +
                     "WHERE SESSIONID = ? AND EQUIPMENTID = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newCondition);
            pstmt.setInt(2, SESSIONID);
            pstmt.setInt(3, EQUIPMENTID);
            int rows = pstmt.executeUpdate();

            if (rows > 0)
                System.out.println("✅ Equipment condition updated to '" + newCondition + "' for Session " + SESSIONID);
            else
                System.out.println("⚠️ No matching Session_Equipment record found.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Update a Project's budget and deadline
    public void updateProject(int PROJECTID, double newBudget, Date newDeadline, Connection conn) {
        String sql = "UPDATE PROJECT SET BUDGET = ?, DEADLINE = ? WHERE PROJECTID = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, newBudget);
            pstmt.setDate(2, newDeadline);
            pstmt.setInt(3, PROJECTID);
            int rows = pstmt.executeUpdate();

            if (rows > 0)
                System.out.println("✅ Project " + PROJECTID + " updated: Budget=" + newBudget + ", Deadline=" + newDeadline);
            else
                System.out.println("⚠️ No Project found with ID " + PROJECTID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //  Professional who managed the highest VARIETY of different equipment last month
    public void inquiry3_TopEquipmentProfessional(Connection conn) {
        String sql =
            "SELECT TOP 1 " +
            "    P.PROFESSIONALLD, " +
            "    P.FULLNAME, " +
            "    P.ROLE, " +
            "    COUNT(DISTINCT SE.EQUIPMENTID) AS UNIQUE_EQUIPMENT_COUNT " +
            "FROM PROFESSIONAL P " +
            "JOIN SESSION_PROFESSIONAL SP ON P.PROFESSIONALLD = SP.PROFESSIONALLD " +
            "JOIN SESSION_EQUIPMENT SE   ON SP.SESSIONID = SE.SESSIONID " +
            "JOIN SESSION S              ON SP.SESSIONID = S.SESSIONID " +
            "WHERE MONTH(S.SESSIONDATE) = MONTH(DATEADD(MONTH, -1, GETDATE())) " +
            "  AND YEAR(S.SESSIONDATE)  = YEAR(DATEADD(MONTH, -1, GETDATE())) " +
            "GROUP BY P.PROFESSIONALLD, P.FULLNAME, P.ROLE " +
            "ORDER BY UNIQUE_EQUIPMENT_COUNT DESC";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("\n📊 INQUIRY 3: Professional with highest equipment variety last month");
            System.out.println("─────────────────────────────────────────────────────────────");
            System.out.printf("%-5s %-25s %-20s %-10s%n",
                    "ID", "Full Name", "Role", "Unique Equipment");
            System.out.println("─────────────────────────────────────────────────────────────");
            if (rs.next()) {
                System.out.printf("%-5d %-25s %-20s %-10d%n",
                        rs.getInt("PROFESSIONALLD"),
                        rs.getString("FULLNAME"),
                        rs.getString("ROLE"),
                        rs.getInt("UNIQUE_EQUIPMENT_COUNT"));
            } else {
                System.out.println("No data found for last month.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //  Studios that did NOT host any production sessions last month
    public void inquiry4_StudiosWithNoSessions(Connection conn) {
        String sql =
            "SELECT S.STUDIOID, S.STUDIONAME, S.STUDIOTYPE, S.WING " +
            "FROM STUDIO S " +
            "WHERE S.STUDIOID NOT IN ( " +
            "    SELECT DISTINCT STUDIOID FROM SESSION " +
            "    WHERE MONTH(SESSIONDATE) = MONTH(DATEADD(MONTH, -1, GETDATE())) " +
            "      AND YEAR(SESSIONDATE)  = YEAR(DATEADD(MONTH, -1, GETDATE())) " +
            ")";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("\n📊 INQUIRY 4: Studios with NO sessions last month");
            System.out.println("───────────────────────────────────────────────────────");
            System.out.printf("%-5s %-20s %-20s %-15s%n",
                    "ID", "Studio Name", "Type", "Wing");
            System.out.println("───────────────────────────────────────────────────────");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-20s %-20s %-15s%n",
                        rs.getInt("STUDIOID"),
                        rs.getString("STUDIONAME"),
                        rs.getString("STUDIOTYPE"),
                        rs.getString("WING"));
            }
            if (!found) System.out.println("All studios had sessions last month.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
