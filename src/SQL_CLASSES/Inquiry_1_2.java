package SQL_CLASSES;

import java.sql.*;

public class Inquiry_1_2 {
    //===========INQUIRY1 : MOST DEMANDED SKILL=======/
    public void Inquiry_1(Connection conn){
        String sql = "SELECT TOP 1 ROLE, COUNT(*) AS AssignmentCount\n" +
                "FROM PROFESSIONAL p\n" +
                "INNER JOIN SESSION_PROFESSIONAL sp\n" +
                "ON  sp.PROFESSIONALLD = p.PROFESSIONALLD\n" +
                "GROUP BY ROLE\n" +
                "ORDER BY AssignmentCount DESC;";

        try {
            Statement s = conn.createStatement();
            ResultSet result = s.executeQuery(sql);
            if(result.next()){
                String skill = result.getString("ROLE");
                int assignmentCount = result.getInt("AssignmentCount");
                System.out.println("Most Demanded Skill:"+ " "+ skill + " assigned: " + assignmentCount + " times");
            }
            else{
                System.out.println("Not found");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    //===========INQUIRY2: production project had no sessions in last month=======
    public void Inquiry_2(Connection conn){
        String sql = "SELECT pr.PROJECTID , TITLE \n" +
                "FROM PROJECT pr\n" +
                "LEFT OUTER JOIN SESSION s\n" +
                "ON pr.PROJECTID= s.PROJECTID\n" +
                "AND s.SESSIONDATE >= (SELECT DATEADD( Month, -1, MAX(SESSIONDATE)) FROM SESSION)\n" +
                "where s.SESSIONID IS NULL;";

        try {
            Statement s = conn.createStatement();
            ResultSet result = s.executeQuery(sql);
            System.out.println("Projects: ");
            if(!result.next()){
                System.out.println("Not found");
            }
            while(result.next()){
                String title  = result.getString("TITLE");
                int ID = result.getInt("PROJECTID");
                System.out.println("ID: " + ID + ", Title: " + title );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
