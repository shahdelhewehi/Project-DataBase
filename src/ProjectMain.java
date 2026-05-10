import java.sql.*;
import SQL_CLASSES.INSERT;
import SQL_CLASSES.DELETE_UPDATE ;

public class ProjectMain{
    Connection connection;
    //db connection
    ProjectMain(){
        try {
            this.connection = getConnection();
            System.out.println(" db connection successful!");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private Connection getConnection()  throws SQLException{
        try{
            //!!!
            String url = "jdbc:sqlserver://DESKTOP-HOAEFGL;databaseName=Studio;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
            //String user = System.getenv("DB_USER");
            //String password = System.getenv("DB_PASSWORD");
        //get values from the system environment
//        String url = System.getenv("DB_URL");
//        String user = System.getenv("DB_USER");
//        String password = System.getenv("DB_PASSWORD");
//        if (url == null || user == null || password == null) {
//            throw new SQLException("Database environment variables (DB_URL, DB_USER, DB_PASSWORD) are not set!");
//        }
        return DriverManager.getConnection(url);
        }
        catch (SQLException e) {
            throw e;
        }
    }
    public Connection getConn() {
        return connection;
    }
    public static void main(String[] args) {
        try {
        ProjectMain app = new ProjectMain();
        INSERT data = new INSERT();
            // ========== TEST CASE 1: Insert Professional ==========
            System.out.println("📋 TEST 1: Inserting Professional...");
            data.insertProfessional(301, "Sarah Johnson", "Music Producer", app.getConn());
            System.out.println("✅ Professional inserted successfully!\n");

            // ========== TEST CASE 2: Insert Studio ==========
            System.out.println("📋 TEST 2: Inserting Studio...");
            data.insertStudio(302, "Studio Omega", "Mixing", "South Wing", true, app.getConn());
            System.out.println("✅ Studio inserted successfully!\n");

            // ========== TEST CASE 3: Insert Project ==========
            System.out.println("📋 TEST 3: Inserting Project...");
            Date projectDate = Date.valueOf("2025-03-20");
            Date deadline = Date.valueOf("2025-09-15");
            data.insertProject(303, "Rock Album 2025", projectDate, 120000.00, deadline, app.getConn());
            System.out.println("✅ Project inserted successfully!\n");

            // ========== TEST CASE 4: Insert Session ==========
            System.out.println("📋 TEST 4: Inserting Session...");
            Date sessionDate = Date.valueOf("2025-04-10");
            Time startTime = Time.valueOf("14:00:00");
            Time endTime = Time.valueOf("22:00:00");
            data.insertSessions(304, 303, 302, sessionDate, startTime, endTime, app.getConn());
            System.out.println("✅ Session inserted successfully!\n");

            // ========== TEST CASE 5: Insert Equipment ==========
            System.out.println("📋 TEST 5: Inserting Equipment...");
            data.insertEquipment(305, "SSL G-Plus Console", "Mixing Console", 123456789, app.getConn());
            System.out.println("✅ Equipment inserted successfully!\n");

            // ========== TEST CASE 6: Insert Session_Equipment ==========
            System.out.println("📋 TEST 6: Inserting Session_Equipment...");
            data.insertSession_Equipment(304, 305, "Like New", app.getConn());
            System.out.println("✅ Session_Equipment inserted successfully!\n");

            // ========== TEST CASE 7: Insert Session_Professional ==========
            System.out.println("📋 TEST 7: Inserting Session_Professional...");
            data.insertSession_Professional(304, 301, "Head Producer", app.getConn());
            System.out.println("✅ Session_Professional inserted successfully!\n");

            // ========== TEST CASE 8: Additional Professional ==========
            System.out.println("📋 TEST 8: Inserting Additional Professional...");
            data.insertProfessional(306, "Michael Chen", "Mastering Engineer", app.getConn());
            System.out.println("✅ Additional Professional inserted successfully!\n");

            // ========== TEST CASE 9: Additional Equipment ==========
            System.out.println("📋 TEST 9: Inserting Additional Equipment...");
            data.insertEquipment(307, "Yamaha NS10 Monitors", "Studio Monitor", 987123456, app.getConn());
            System.out.println("✅ Additional Equipment inserted successfully!\n");

            // ========== TEST CASE 10: Another Session-Professional Link ==========
            System.out.println("📋 TEST 10: Inserting Another Session_Professional...");
            data.insertSession_Professional(304, 306, "Mastering Consultant", app.getConn());
            System.out.println("✅ Additional Session_Professional inserted successfully!\n");
            System.out.println("🎉 ALL TESTS COMPLETED SUCCESSFULLY!");
            System.out.println("\n💡 Now run the verification SQL queries in SSMS to confirm data.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
            // ========== TEST CASE 11: Delete Professional ==========
            System.out.println("🗑️ TEST 11: Deleting Professional (ID=301)...");
            data.deleteProf(301, app.getConn());
            System.out.println();

            // ========== TEST CASE 12: Delete Studio ==========
            System.out.println("🗑️ TEST 12: Deleting Studio (ID=302)...");
            data.deleteStudio(302, app.getConn());
            System.out.println();

            // ========== TEST CASE 13: Update Equipment Condition ==========
            System.out.println("✏️ TEST 13: Updating Equipment Condition...");
            data.updateEquipmentCondition(304, 305, "Scratched", app.getConn());
            System.out.println();

            // ========== TEST CASE 14: Update Project ==========
            System.out.println("✏️ TEST 14: Updating Project Budget & Deadline...");
            Date newDeadline = Date.valueOf("2025-12-31");
            data.updateProject(303, 150000.00, newDeadline, app.getConn());
            System.out.println();

            // ========== TEST CASE 15 ==========
            System.out.println("🔍 TEST 15: Running Inquiry 3...");
            data.TopEquipmentProfessional(app.getConn());

            // ========== TEST CASE 16 ==========
            System.out.println("\n🔍 TEST 16: Running Inquiry 4...");
            data.StudiosWithNoSessions(app.getConn());

            System.out.println("\n\n🎉 ALL TESTS COMPLETED SUCCESSFULLY!");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
