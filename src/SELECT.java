import java.sql.*;

public class SELECT {

    // INQUIRY 5: Equipment items used for each project (last month)
    public void equipmentForProjectsLastMonth(Connection conn) {
        String sql =
            "SELECT p.PROJECTID, p.TITLE, e.EQUIPMENTID, e.NAME AS EQUIPMENT_NAME, e.TYPE " +
            "FROM PROJECT p " +
            "JOIN SESSION s ON p.PROJECTID = s.PROJECTID " +
            "JOIN SESSION_EQUIPMENT se ON s.SESSIONID = se.SESSIONID " +
            "JOIN EQUIPMENT e ON se.EQUIPMENTID = e.EQUIPMENTID " +
            "WHERE s.SESSIONDATE >= (SELECT DATEADD(MONTH, -1, MAX(SESSIONDATE)) FROM SESSION) " +
            "ORDER BY p.PROJECTID, e.NAME";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n📋 Equipment items utilized per project (last month)");
            System.out.println("──────────────────────────────────────────────────────────────────────────");
            System.out.printf("%-5s %-25s %-5s %-30s %-20s%n",
                    "ProjID", "Project Title", "EquipID", "Equipment Name", "Type");
            System.out.println("──────────────────────────────────────────────────────────────────────────");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-25s %-5d %-30s %-20s%n",
                        rs.getInt("PROJECTID"),
                        rs.getString("TITLE"),
                        rs.getInt("EQUIPMENTID"),
                        rs.getString("EQUIPMENT_NAME"),
                        rs.getString("TYPE"));
            }
            if (!found) System.out.println("No equipment data for projects last month.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // INQUIRY 6: Professionals – full profile + number of projects contributed (last month)
    public void professionalProjectCountLastMonth(Connection conn) {
        String sql =
            "SELECT p.PROFESSIONALLD, p.FULLNAME, p.ROLE, COUNT(DISTINCT s.PROJECTID) AS PROJECT_COUNT " +
            "FROM PROFESSIONAL p " +
            "JOIN SESSION_PROFESSIONAL sp ON p.PROFESSIONALLD = sp.PROFESSIONALLD " +
            "JOIN SESSION s ON sp.SESSIONID = s.SESSIONID " +
            "WHERE s.SESSIONDATE >= (SELECT DATEADD(MONTH, -1, MAX(SESSIONDATE)) FROM SESSION) " +
            "GROUP BY p.PROFESSIONALLD, p.FULLNAME, p.ROLE " +
            "ORDER BY PROJECT_COUNT DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n👥 Professionals – project contributions (last month)");
            System.out.println("─────────────────────────────────────────────────────────────────");
            System.out.printf("%-5s %-25s %-20s %-10s%n",
                    "ID", "Full Name", "Role", "Projects");
            System.out.println("─────────────────────────────────────────────────────────────────");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-25s %-20s %-10d%n",
                        rs.getInt("PROFESSIONALLD"),
                        rs.getString("FULLNAME"),
                        rs.getString("ROLE"),
                        rs.getInt("PROJECT_COUNT"));
            }
            if (!found) System.out.println("No professional contributions last month.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Simple SELECT from a single table (Studios)
    public void selectAllStudios(Connection conn) {
        String sql = "SELECT * FROM STUDIO";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n🏢 All Studios");
            System.out.println("────────────────────────────────────────────────────────────────────");
            System.out.printf("%-3s %-20s %-15s %-10s %-15s%n",
                    "ID", "Name", "Type", "Wing", "Available");
            System.out.println("────────────────────────────────────────────────────────────────────");
            while (rs.next()) {
                System.out.printf("%-3d %-20s %-15s %-10s %-15s%n",
                        rs.getInt("STUDIOID"),
                        rs.getString("STUDIONAME"),
                        rs.getString("STUDIOTYPE"),
                        rs.getString("WING"),
                        rs.getBoolean("STUDIO_AVAILABILITY") ? "Yes" : "No");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // SELECT using JOIN (Sessions with Project title & Studio name)
    public void selectSessionsWithDetails(Connection conn) {
        String sql =
            "SELECT s.SESSIONID, p.TITLE AS PROJECT_TITLE, st.STUDIONAME, s.SESSIONDATE " +
            "FROM SESSION s " +
            "JOIN PROJECT p ON s.PROJECTID = p.PROJECTID " +
            "JOIN STUDIO st ON s.STUDIOID = st.STUDIOID " +
            "ORDER BY s.SESSIONDATE DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("\n📅 Sessions with Project & Studio details");
            System.out.println("─────────────────────────────────────────────────────────────────");
            System.out.printf("%-5s %-25s %-20s %-12s%n",
                    "SessID", "Project Title", "Studio Name", "Date");
            System.out.println("─────────────────────────────────────────────────────────────────");
            while (rs.next()) {
                System.out.printf("%-5d %-25s %-20s %-12s%n",
                        rs.getInt("SESSIONID"),
                        rs.getString("PROJECT_TITLE"),
                        rs.getString("STUDIONAME"),
                        rs.getDate("SESSIONDATE").toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}