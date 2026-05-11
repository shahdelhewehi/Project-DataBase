import java.sql.*;
import SQL_CLASSES.INSERT;
import SQL_CLASSES.DELETE_UPDATE ;
import SQL_CLASSES.SELECT;
import SQL_CLASSES.Inquiry_1_2;

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
            //!!! change "DESKTOP-HOAEFGL" to your server name
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
    ProjectMain app = new ProjectMain();   // move outside try so we can reuse 'app' later
        try {
            Inquiry_1_2 i1 = new Inquiry_1_2();
            Inquiry_1_2 i2 = new Inquiry_1_2();
            i1.Inquiry_1(app.getConn());
            i2.Inquiry_2(app.getConn());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
            //INSERT data = new INSERT();
//            // TEST CASE 1: Insert Professional
//// Inputs needed: PROFESSIONALID=501, FULLNAME="Sarah Johnson", ROLE="Music Producer"
//            System.out.println("📋 TEST 1: Inserting Professional...");
//            data.insertProfessional(app.getConn());  // Will prompt: 501, Sarah Johnson, Music Producer
//            System.out.println("✅ Professional inserted successfully!\n");
//
//// TEST CASE 2: Insert Studio
//// Inputs needed: STUDIOID=502, STUDIONAME="Studio Omega", STUDIOTYPE="Mixing", WING="South Wing", STUDIO_AVAILABILITY=true
//            System.out.println("📋 TEST 2: Inserting Studio...");
//            data.insertStudio(app.getConn());  // Will prompt: 502, Studio Omega, Mixing, South Wing, true
//            System.out.println("✅ Studio inserted successfully!\n");
//
//// TEST CASE 3: Insert Project
//// Inputs needed: PROJECTID=503, TITLE="Rock Album 2025", PROJECTDATE="2025-03-20", BUDGET=120000.0, DEADLINE="2025-09-15"
//            System.out.println("📋 TEST 3: Inserting Project...");
//            data.insertProject(app.getConn());  // Will prompt: 503, Rock Album 2025, 2025-03-20, 120000.0, 2025-09-15
//            System.out.println("✅ Project inserted successfully!\n");
//
//// TEST CASE 4: Insert Session
//// Inputs needed: SESSIONID=504, PROJECTID=503, STUDIOID=502, SESSIONDATE="2025-04-10", SESSIONSTART="2025-04-10 14:00:00.000", SESSIONEND="2025-04-10 22:00:00.000"
//            System.out.println("📋 TEST 4: Inserting Session...");
//            data.insertSessions(app.getConn());  // Will prompt: 504, 503, 502, 2025-04-10, 2025-04-10 14:00:00.000, 2025-04-10 22:00:00.000
//            System.out.println("✅ Session inserted successfully!\n");
//
//// TEST CASE 5: Insert Equipment
//// Inputs needed: EQUIPMENTID=505, NAME="SSL G-Plus Console", TYPE="Mixing Console", SERIALNUMBER="SN123456789"
//            System.out.println("📋 TEST 5: Inserting Equipment...");
//            data.insertEquipment(app.getConn());  // Will prompt: 505, SSL G-Plus Console, Mixing Console, SN123456789
//            System.out.println("✅ Equipment inserted successfully!\n");
//
//// TEST CASE 6: Insert Session_Equipment
//// Inputs needed: SESSIONID=504, EQUIPMENTID=505, RETURNCONDITION="Like New"
//            System.out.println("📋 TEST 6: Inserting Session_Equipment...");
//            data.insertSession_Equipment(app.getConn());  // Will prompt: 504, 505, Like New
//            System.out.println("✅ Session_Equipment inserted successfully!\n");
//
//// TEST CASE 7: Insert Session_Professional
//// Inputs needed: SESSIONID=504, PROFESSIONALID=501, ROLEINSESSION="Head Producer"
//            System.out.println("📋 TEST 7: Inserting Session_Professional...");
//            data.insertSession_Professional(app.getConn());  // Will prompt: 504, 501, Head Producer
//            System.out.println("✅ Session_Professional inserted successfully!\n");
//
//// TEST CASE 8: Additional Professional
//// Inputs needed: PROFESSIONALID=506, FULLNAME="Michael Chen", ROLE="Mastering Engineer"
//            System.out.println("📋 TEST 8: Inserting Additional Professional...");
//            data.insertProfessional(app.getConn());  // Will prompt: 506, Michael Chen, Mastering Engineer
//            System.out.println("✅ Additional Professional inserted successfully!\n");
//
//// TEST CASE 9: Additional Equipment
//// Inputs needed: EQUIPMENTID=507, NAME="Yamaha NS10 Monitors", TYPE="Studio Monitor", SERIALNUMBER="SN987123456"
//            System.out.println("📋 TEST 9: Inserting Additional Equipment...");
//            data.insertEquipment(app.getConn());  // Will prompt: 507, Yamaha NS10 Monitors, Studio Monitor, SN987123456
//            System.out.println("✅ Additional Equipment inserted successfully!\n");
//
//// TEST CASE 10: Another Session-Professional Link
//// Inputs needed: SESSIONID=504, PROFESSIONALID=506, ROLEINSESSION="Mastering Consultant"
//            System.out.println("📋 TEST 10: Inserting Another Session_Professional...");
//            data.insertSession_Professional(app.getConn());  // Will prompt: 504, 506, Mastering Consultant
//            System.out.println("✅ Additional Session_Professional inserted successfully!\n");
//
//            System.out.println("🎉 ALL TESTS COMPLETED SUCCESSFULLY!");
//
//        }
//        catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        // ========== NEW TESTS 11‑20 (DELETE, UPDATE, SELECT) ==========
//        DELETE_UPDATE deleteUpdateOps = new DELETE_UPDATE();
//        SELECT selectOps = new SELECT();
//
//        try {
//            // TEST CASE 11: Delete Professional
//            System.out.println("🗑️ TEST 11: Deleting Professional (ID=301)...");
//            deleteUpdateOps.deleteProf(301, app.getConn());
//            System.out.println();
//
//            // TEST CASE 12: Delete Studio
//            System.out.println("🗑️ TEST 12: Deleting Studio (ID=302)...");
//            deleteUpdateOps.deleteStudio(302, app.getConn());
//            System.out.println();
//
//            // TEST CASE 13: Update Equipment Condition
//            System.out.println("✏️ TEST 13: Updating Equipment Condition...");
//            deleteUpdateOps.updateEquipmentCondition(304, 305, "Scratched", app.getConn());
//            System.out.println();
//
//            // TEST CASE 14: Update Project
//            System.out.println("✏️ TEST 14: Updating Project Budget & Deadline...");
//            Date newDeadline = Date.valueOf("2025-12-31");
//            deleteUpdateOps.updateProject(303, 150000.00, newDeadline, app.getConn());
//            System.out.println();
//
//            // TEST CASE 15: Inquiry 3
//            System.out.println("🔍 TEST 15: Running Inquiry 3...");
//            deleteUpdateOps.TopEquipmentProfessional(app.getConn());
//
//            // TEST CASE 16: Inquiry 4
//            System.out.println("\n🔍 TEST 16: Running Inquiry 4...");
//            deleteUpdateOps.StudiosWithNoSessions(app.getConn());
//
//            // TEST CASE 17: Select all studios (single table)
//            System.out.println("\n🔍 TEST 17: Select all studios...");
//            selectOps.selectAllStudios(app.getConn());
//
//            // TEST CASE 18: Sessions with project & studio details (JOIN)
//            System.out.println("\n🔍 TEST 18: Sessions with project & studio details...");
//            selectOps.selectSessionsWithDetails(app.getConn());
//
//            // TEST CASE 19: Inquiry 5 – Equipment per project (last month)
//            System.out.println("\n🔍 TEST 19: Inquiry 5 – Equipment per project (last month)...");
//            selectOps.equipmentForProjectsLastMonth(app.getConn());
//
//            // TEST CASE 20: Inquiry 6 – Professionals with project count (last month)
//            System.out.println("\n🔍 TEST 20: Inquiry 6 – Professionals with project count (last month)...");
//            selectOps.professionalProjectCountLastMonth(app.getConn());
//
//            System.out.println("\n🎉 ALL DELETE/UPDATE/SELECT TESTS COMPLETED SUCCESSFULLY!");
//
//        } catch (Exception e) {
//            System.err.println("❌ Error in DELETE/UPDATE/SELECT tests: " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//            // ========== TEST CASE 11: Delete Professional ==========
//            System.out.println("🗑️ TEST 11: Deleting Professional (ID=301)...");
//            data.deleteProf(301, app.getConn());
//            System.out.println();
//
//            // ========== TEST CASE 12: Delete Studio ==========
//            System.out.println("🗑️ TEST 12: Deleting Studio (ID=302)...");
//            data.deleteStudio(302, app.getConn());
//            System.out.println();
//
//            // ========== TEST CASE 13: Update Equipment Condition ==========
//            System.out.println("✏️ TEST 13: Updating Equipment Condition...");
//            data.updateEquipmentCondition(304, 305, "Scratched", app.getConn());
//            System.out.println();
//
//            // ========== TEST CASE 14: Update Project ==========
//            System.out.println("✏️ TEST 14: Updating Project Budget & Deadline...");
//            Date newDeadline = Date.valueOf("2025-12-31");
//            data.updateProject(303, 150000.00, newDeadline, app.getConn());
//            System.out.println();
//
//            // ========== TEST CASE 15 ==========
//            System.out.println("🔍 TEST 15: Running Inquiry 3...");
//            data.TopEquipmentProfessional(app.getConn());
//
//            // ========== TEST CASE 16 ==========
//            System.out.println("\n🔍 TEST 16: Running Inquiry 4...");
//            data.StudiosWithNoSessions(app.getConn());
//
//            System.out.println("\n\n🎉 ALL TESTS COMPLETED SUCCESSFULLY!");
//
//        }
//        catch(Exception e) {
//            System.err.println("Error: " + e.getMessage());
//            e.printStackTrace();
//        }
    }
}
