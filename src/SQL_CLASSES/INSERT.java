package SQL_CLASSES;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class INSERT {
    Scanner obj = new Scanner(System.in);

    public INSERT(){
        this.obj = obj;
    }
    public void insertProfessional(Connection conn){
        System.out.println("Enter Professional's ID: ");
        int PROFESSIONALID = obj.nextInt();
        obj.nextLine();

        System.out.println("Enter Professional's Name: ");
        String FULLNAME = obj.nextLine();

        System.out.println("Enter Professional's Role: ");
        String ROLE = obj.nextLine();

        String sql = "INSERT INTO PROFESSIONAL (PROFESSIONALLD, FULLNAME, ROLE) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);{
            pstmt.setInt(1, PROFESSIONALID);
            pstmt.setString(2, FULLNAME);
            pstmt.setString(3, ROLE);
            pstmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void insertStudio(Connection conn){

        System.out.println("Enter Studio's ID: ");
        int STUDIOID = obj.nextInt();
        obj.nextLine();

        System.out.println("Enter Studio's Name: ");
        String STUDIONAME = obj.nextLine();

        System.out.println("Enter Studio's Type: ");
        String STUDIOTYPE = obj.nextLine();

        System.out.println("Enter Studio's Wing: ");
        String WING = obj.nextLine();

        System.out.println("Enter Studio's Availability: ");
        Boolean STUDIO_AVAILABILITY = obj.nextBoolean();

        String sql = "INSERT INTO STUDIO (STUDIOID, STUDIONAME, STUDIOTYPE, WING, STUDIO_AVAILABILITY) VALUES (?, ?, ?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);{
                pstmt.setInt(1, STUDIOID);
                pstmt.setString(2, STUDIONAME);
                pstmt.setString(3, STUDIOTYPE);
                pstmt.setString(4, WING);
                pstmt.setBoolean(5, STUDIO_AVAILABILITY);
                pstmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void insertProject(Connection conn){
        String tempDate;//handle date temporarily b4 handling them for sql

        System.out.println("Enter Project's ID: ");
        int PROJECTID = obj.nextInt();
        obj.nextLine();

        System.out.println("Enter Project's Title: ");
        String TITLE = obj.nextLine();

        java.sql.Date PROJECTDATE;
        System.out.print("Enter Project Date (YYYY-MM-DD): ");
        tempDate = obj.nextLine();
        PROJECTDATE = java.sql.Date.valueOf(tempDate);

        System.out.println("Enter Project's Budget(eg: 500.0): ");
        Double BUDGET = obj.nextDouble();
        obj.nextLine();

        java.sql.Date DEADLINE;
        System.out.print("Enter Project's Deadline (YYYY-MM-DD): ");
        tempDate = obj.nextLine();
        DEADLINE = java.sql.Date.valueOf(tempDate);

        String sql = "INSERT INTO PROJECT (PROJECTID, TITLE, PROJECTDATE, BUDGET, DEADLINE) VALUES (?, ?, ?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);{
                pstmt.setInt(1, PROJECTID);
                pstmt.setString(2,TITLE);
                pstmt.setDate(3, PROJECTDATE);
                pstmt.setDouble(4, BUDGET);
                pstmt.setDate(5, DEADLINE);

                pstmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertSessions(Connection conn) {
        String tempDate;//handle date temporarily b4 handling them for sql
        String tempTimestamp;

        System.out.println("Enter Session's ID: ");
        int SESSIONID = obj.nextInt();
        obj.nextLine();

        System.out.println("Enter Project's ID: ");
        int PROJECTID= obj.nextInt();
        obj.nextLine();

        System.out.println("Enter Studio's ID: ");
        int STUDIOID= obj.nextInt();
        obj.nextLine();

        java.sql.Date SESSIONDATE;
        System.out.println("Enter Session's Date (YYYY-MM-DD): ");
        tempDate = obj.nextLine();
        SESSIONDATE = java.sql.Date.valueOf(tempDate);

        Timestamp SESSIONSTART;
        System.out.println("Enter Session's Start (YYYY-MM-DD HH:MM:SS.SSS): ");
        tempTimestamp = obj.nextLine();
        SESSIONSTART = java.sql.Timestamp.valueOf(tempTimestamp);

        Timestamp SESSIONEND;
        System.out.println("Enter Session's End (YYYY-MM-DD HH:MM:SS.SSS): ");
        tempTimestamp = obj.nextLine();
        SESSIONEND = java.sql.Timestamp.valueOf(tempTimestamp);

        String sql = "INSERT INTO SESSION (SESSIONID, PROJECTID, STUDIOID," +
                " SESSIONDATE, SESSIONSTART, SESSIONEND) VALUES (?, ?, ?,?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);{
                pstmt.setInt(1, SESSIONID);
                pstmt.setInt(2,PROJECTID);
                pstmt.setInt(3, STUDIOID);
                pstmt.setDate(4, SESSIONDATE);
                pstmt.setTimestamp(5, SESSIONSTART);
                pstmt.setTimestamp(6, SESSIONEND);

                pstmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertEquipment(Connection conn)
    {
        System.out.println("Enter Equipment's ID: ");
        int EQUIPMENTID = obj.nextInt();
        obj.nextLine();

        System.out.println("Enter Equipment's Name: ");
        String NAME = obj.nextLine();

        System.out.println("Enter Equipment's Type: ");
        String TYPE = obj.nextLine();

        System.out.println("Enter Equipment's Serial Number: ");
        String SERIALNUMBER = obj.nextLine();

        String sql = "INSERT INTO EQUIPMENT (EQUIPMENTID, NAME, TYPE, SERIALNUMBER) VALUES (?, ?, ?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);{
                pstmt.setInt(1, EQUIPMENTID);
                pstmt.setString(2,NAME);
                pstmt.setString(3, TYPE);
                pstmt.setString(4, SERIALNUMBER);

                pstmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void insertSession_Equipment( Connection conn)
    {
        System.out.println("Enter Session's Type: ");
        int SESSIONID = obj.nextInt();

        System.out.println("Enter Equipment's ID: ");
        int EQUIPMENTID = obj.nextInt();
        obj.nextLine();

        System.out.println("Enter Equipment's Return Condition: ");
        String RETURNCONDITION = obj.nextLine();

        String sql = "INSERT INTO SESSION_EQUIPMENT (SESSIONID, EQUIPMENTID, RETURNCONDITION) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);{
                pstmt.setInt(1, SESSIONID);
                pstmt.setInt(2,EQUIPMENTID);
                pstmt.setString(3, RETURNCONDITION);

                pstmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertSession_Professional( Connection conn)
    {

        System.out.println("Enter Session's ID: ");
        int SESSIONID = obj.nextInt();

        System.out.println("Enter Professional's ID: ");
        int PROFESSIONALID= obj.nextInt();
        obj.nextLine();

        System.out.println("Enter Professional's role in session: ");
        String ROLEINSESSION = obj.nextLine();

        String sql = "INSERT INTO SESSION_PROFESSIONAL (SESSIONID, PROFESSIONALLD, ROLEINSESSION) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);{
                pstmt.setInt(1, SESSIONID);
                pstmt.setInt(2,PROFESSIONALID);
                pstmt.setString(3, ROLEINSESSION);

                pstmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
