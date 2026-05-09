package SQL_CLASSES;

import java.sql.*;
import java.sql.PreparedStatement;

public class INSERT {
    public void insertProfessional(int PROFESSIONALID, String FULLNAME, String ROLE, Connection conn){
        String sql = "INSERT INTO PROFESSIONAL (PROFESSIONALLD, FULLNAME, ROLE) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);{
            pstmt.setInt(1, PROFESSIONALID);
            pstmt.setString(2, FULLNAME);
            pstmt.setString(3, ROLE);
            }
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void insertStudio(int STUDIOID, String STUDIONAME, String STUDIOTYPE, String WING, Boolean STUDIO_AVAILABILITY, Connection conn){
        String sql = "INSERT INTO STUDIO (STUDIOID, STUDIONAME, STUDIOTYPE, WING, STUDIO_AVAILABILITY) VALUES (?, ?, ?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);{
                pstmt.setInt(1, STUDIOID);
                pstmt.setString(2, STUDIONAME);
                pstmt.setString(3, STUDIOTYPE);
                pstmt.setString(4, WING);
                pstmt.setBoolean(5, STUDIO_AVAILABILITY);
            }
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void insertProject(int PROJECTID, String TITLE, Date PROJECTDATE, Double BUDGET, Date DEADLINE, Connection conn){
        String sql = "INSERT INTO PROJECT (PROJECTID, TITLE, PROJECTDATE, BUDGET, DEADLINE) VALUES (?, ?, ?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);{
                pstmt.setInt(1, PROJECTID);
                pstmt.setString(2,TITLE);
                pstmt.setDate(3, PROJECTDATE);
                pstmt.setDouble(4, BUDGET);
                pstmt.setDate(5, DEADLINE);

            }
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertSessions(int SESSIONID, int PROJECTID, int STUDIOID,
                               Date SESSIONDATE, Time SESSIONSTART, Time SESSIONEND, Connection conn)
    {
        String sql = "INSERT INTO SESSION (SESSIONID, PROJECTID, STUDIOID," +
                " SESSIONDATE, SESSIONSTART, SESSIONEND) VALUES (?, ?, ?,?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);{
                pstmt.setInt(1, SESSIONID);
                pstmt.setInt(2,PROJECTID);
                pstmt.setInt(3, STUDIOID);
                pstmt.setDate(4, SESSIONDATE);
                pstmt.setTime(5, SESSIONSTART);
                pstmt.setTime(6, SESSIONEND);

            }
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertEquipment(int EQUIPMENTID, String NAME,  String TYPE, int SERIALNUMBER, Connection conn)
    {
        String sql = "INSERT INTO EQUIPMENT (EQUIPMENTID, NAME, TYPE, SERIALNUMBER) VALUES (?, ?, ?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);{
                pstmt.setInt(1, EQUIPMENTID);
                pstmt.setString(2,NAME);
                pstmt.setString(3, TYPE);
                pstmt.setInt(4, SERIALNUMBER);

            }
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void insertSession_Equipment(int SESSIONID, int EQUIPMENTID, String RETURNCONDITION, Connection conn)
    {
        String sql = "INSERT INTO SESSION_EQUIPMENT (SESSIONID, EQUIPMENTID, RETURNCONDITION) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);{
                pstmt.setInt(1, SESSIONID);
                pstmt.setInt(2,EQUIPMENTID);
                pstmt.setString(3, RETURNCONDITION);
            }
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertSession_Professional(int SESSIONID, int PROFESSIONALID, String ROLEINSESSION, Connection conn)
    {
        String sql = "INSERT INTO SESSION_PROFESSIONAL (SESSIONID, PROFESSIONALLD, ROLEINSESSION) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);{
                pstmt.setInt(1, SESSIONID);
                pstmt.setInt(2,PROFESSIONALID);
                pstmt.setString(3, ROLEINSESSION);
            }
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
