import java.sql.*;
import java.util.Scanner;

public class ProjectMain {

    Connection connection;
    Scanner sc = new Scanner(System.in);

    ProjectMain() {
        try {
            this.connection = getConnection();
            System.out.println("DB connection successful!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() throws SQLException {
        String url =
                "jdbc:sqlserver://localhost\\SQLEXPRESS;" +
                        "databaseName=Studio;" +
                        "encrypt=true;" +
                        "trustServerCertificate=true;" +
                        "integratedSecurity=true;";

        return DriverManager.getConnection(url);
    }

    public Connection getConn() {
        return connection;
    }

    public static void main(String[] args) {

        ProjectMain app = new ProjectMain();

        INSERT insertOps = new INSERT();
        DELETE_UPDATE deleteOps = new DELETE_UPDATE();
        SELECT selectOps = new SELECT();
        Inquiry_1_2 inquiryOps = new Inquiry_1_2();

        Scanner sc = app.sc;
        Connection conn = app.getConn();

        while (true) {

            System.out.println("\n========== STUDIO SYSTEM MENU ==========");
            System.out.println("1  - Insert Professional");
            System.out.println("2  - Insert Studio");
            System.out.println("3  - Insert Project");
            System.out.println("4  - Insert Session");
            System.out.println("5  - Insert Equipment");
            System.out.println("6  - Insert Session_Equipment");
            System.out.println("7  - Insert Session_Professional");

            System.out.println("8  - Delete Professional");
            System.out.println("9  - Delete Studio");

            System.out.println("10 - Update Equipment Condition");
            System.out.println("11 - Update Project");

            System.out.println("12 - Inquiry 1");
            System.out.println("13 - Inquiry 2");
            System.out.println("14 - Top Equipment Professional");
            System.out.println("15 - Studios With No Sessions");

            System.out.println("16 - Select All Studios");
            System.out.println("17 - Sessions With Details");
            System.out.println("18 - Equipment per Project Last Month");
            System.out.println("19 - Professionals Project Count Last Month");

            System.out.println("0  - Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // important fix

            try {

                switch (choice) {

                    case 1 -> insertOps.insertProfessional(conn);
                    case 2 -> insertOps.insertStudio(conn);
                    case 3 -> insertOps.insertProject(conn);
                    case 4 -> insertOps.insertSessions(conn);
                    case 5 -> insertOps.insertEquipment(conn);
                    case 6 -> insertOps.insertSession_Equipment(conn);
                    case 7 -> insertOps.insertSession_Professional(conn);

                    case 8 -> {
                        System.out.print("Enter Professional ID: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        deleteOps.deleteProf(id, conn);
                    }

                    case 9 -> {
                        System.out.print("Enter Studio ID: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        deleteOps.deleteStudio(id, conn);
                    }

                    case 10 -> {
                        System.out.print("Session ID: ");
                        int sid = sc.nextInt();

                        System.out.print("Equipment ID: ");
                        int eid = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Condition: ");
                        String cond = sc.nextLine();

                        deleteOps.updateEquipmentCondition(sid, eid, cond, conn);
                    }

                    case 11 -> {
                        System.out.print("Project ID: ");
                        int pid = sc.nextInt();

                        System.out.print("Budget: ");
                        double budget = sc.nextDouble();
                        sc.nextLine();

                        System.out.print("Deadline (YYYY-MM-DD): ");
                        Date d = Date.valueOf(sc.nextLine());

                        deleteOps.updateProject(pid, budget, d, conn);
                    }

                    case 12 -> inquiryOps.Inquiry_1(conn);
                    case 13 -> inquiryOps.Inquiry_2(conn);
                    case 14 -> deleteOps.TopEquipmentProfessional(conn);
                    case 15 -> deleteOps.StudiosWithNoSessions(conn);

                    case 16 -> selectOps.selectAllStudios(conn);
                    case 17 -> selectOps.selectSessionsWithDetails(conn);
                    case 18 -> selectOps.equipmentForProjectsLastMonth(conn);
                    case 19 -> selectOps.professionalProjectCountLastMonth(conn);

                    case 0 -> {
                        System.out.println("Bye 👋");
                        sc.close();
                        return;
                    }

                    default -> System.out.println("Invalid choice!");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
