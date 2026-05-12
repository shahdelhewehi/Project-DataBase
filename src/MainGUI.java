import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import java.sql.*;

public class MainGUI extends Application {

    Connection conn;

    ObservableList<String[]> professionals = FXCollections.observableArrayList();
    ObservableList<String[]> studios       = FXCollections.observableArrayList();
    ObservableList<String[]> projects      = FXCollections.observableArrayList();
    ObservableList<String[]> equipment     = FXCollections.observableArrayList();
    ObservableList<String[]> sessions      = FXCollections.observableArrayList();

    static final String BG      = "#0D0F14";
    static final String SIDEBAR = "#13161E";
    static final String CARD    = "#1A1E2A";
    static final String ACCENT  = "#6C63FF";
    static final String ACCENT2 = "#A78BFA";
    static final String TEXT    = "#E8EAF0";
    static final String MUTED   = "#6B7280";
    static final String BORDER  = "#252836";
    static final String SUCCESS = "#10B981";
    static final String DANGER  = "#EF4444";

    BorderPane root;
    VBox sidebar;
    StackPane contentArea;
    String activeSection = "Dashboard";

    private Connection getConnection() throws SQLException {
        String url =
                "jdbc:sqlserver://localhost\\SQLEXPRESS;" +
                        "databaseName=Studio;" +
                        "encrypt=true;" +
                        "trustServerCertificate=true;" +
                        "integratedSecurity=true;";
        return DriverManager.getConnection(url);
    }

    private void loadAllData() {
        try {
            professionals.clear();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM PROFESSIONAL");
            while (rs.next())
                professionals.add(new String[]{
                        String.valueOf(rs.getInt("PROFESSIONALLD")),
                        rs.getString("FULLNAME"),
                        rs.getString("ROLE")
                });

            studios.clear();
            rs = conn.createStatement().executeQuery("SELECT * FROM STUDIO");
            while (rs.next())
                studios.add(new String[]{
                        String.valueOf(rs.getInt("STUDIOID")),
                        rs.getString("STUDIONAME"),
                        rs.getString("STUDIOTYPE"),
                        rs.getString("WING"),
                        rs.getBoolean("STUDIO_AVAILABILITY") ? "Yes" : "No"
                });

            projects.clear();
            rs = conn.createStatement().executeQuery("SELECT * FROM PROJECT");
            while (rs.next())
                projects.add(new String[]{
                        String.valueOf(rs.getInt("PROJECTID")),
                        rs.getString("TITLE"),
                        rs.getDate("PROJECTDATE").toString(),
                        String.valueOf(rs.getDouble("BUDGET")),
                        rs.getDate("DEADLINE").toString()
                });

            equipment.clear();
            rs = conn.createStatement().executeQuery("SELECT * FROM EQUIPMENT");
            while (rs.next())
                equipment.add(new String[]{
                        String.valueOf(rs.getInt("EQUIPMENTID")),
                        rs.getString("NAME"),
                        rs.getString("TYPE"),
                        rs.getString("SERIALNUMBER")
                });

            sessions.clear();
            rs = conn.createStatement().executeQuery("SELECT * FROM SESSION");
            while (rs.next())
                sessions.add(new String[]{
                        String.valueOf(rs.getInt("SESSIONID")),
                        String.valueOf(rs.getInt("PROJECTID")),
                        String.valueOf(rs.getInt("STUDIOID")),
                        rs.getDate("SESSIONDATE").toString(),
                        rs.getTimestamp("SESSIONSTART").toString(),
                        rs.getTimestamp("SESSIONEND").toString()
                });

        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load data: " + e.getMessage());
        }
    }

    void deleteFromDB(String table, String id) {
        try {
            String sql = switch (table) {
                case "Professionals" -> "DELETE FROM SESSION_PROFESSIONAL WHERE PROFESSIONALLD = ?; DELETE FROM PROFESSIONAL WHERE PROFESSIONALLD = ?";
                case "Studios"       -> "DELETE FROM STUDIO WHERE STUDIOID = ?";
                case "Projects"      -> "DELETE FROM PROJECT WHERE PROJECTID = ?";
                case "Equipment"     -> "DELETE FROM EQUIPMENT WHERE EQUIPMENTID = ?";
                case "Sessions"      -> "DELETE FROM SESSION WHERE SESSIONID = ?";
                default -> null;
            };
            if (sql != null) {
                if (table.equals("Professionals")) {
                    PreparedStatement ps1 = conn.prepareStatement("DELETE FROM SESSION_PROFESSIONAL WHERE PROFESSIONALLD = ?");
                    ps1.setInt(1, Integer.parseInt(id));
                    ps1.executeUpdate();
                    PreparedStatement ps2 = conn.prepareStatement("DELETE FROM PROFESSIONAL WHERE PROFESSIONALLD = ?");
                    ps2.setInt(1, Integer.parseInt(id));
                    ps2.executeUpdate();
                } else if (table.equals("Studios")) {
                    PreparedStatement ps1 = conn.prepareStatement("DELETE FROM SESSION_EQUIPMENT WHERE SESSIONID IN (SELECT SESSIONID FROM SESSION WHERE STUDIOID = ?)");
                    ps1.setInt(1, Integer.parseInt(id)); ps1.executeUpdate();
                    PreparedStatement ps2 = conn.prepareStatement("DELETE FROM SESSION_PROFESSIONAL WHERE SESSIONID IN (SELECT SESSIONID FROM SESSION WHERE STUDIOID = ?)");
                    ps2.setInt(1, Integer.parseInt(id)); ps2.executeUpdate();
                    PreparedStatement ps3 = conn.prepareStatement("DELETE FROM SESSION WHERE STUDIOID = ?");
                    ps3.setInt(1, Integer.parseInt(id)); ps3.executeUpdate();
                    PreparedStatement ps4 = conn.prepareStatement("DELETE FROM STUDIO WHERE STUDIOID = ?");
                    ps4.setInt(1, Integer.parseInt(id)); ps4.executeUpdate();
                } else {
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setInt(1, Integer.parseInt(id));
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            showAlert("Delete Error", e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) {
        try {
            conn = getConnection();
            loadAllData();
        } catch (SQLException e) {
            showAlert("Connection Error", "Could not connect to database: " + e.getMessage());
        }
        root = new BorderPane();
        root.setStyle("-fx-background-color:" + BG + ";");
        sidebar     = buildSidebar();
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color:" + BG + ";");
        contentArea.setPadding(new Insets(30));
        root.setLeft(sidebar);
        root.setCenter(contentArea);
        showDashboard();
        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("Studio Management System");
        stage.setScene(scene);
        stage.show();
    }

    VBox buildSidebar() {
        VBox sb = new VBox(4);
        sb.setPrefWidth(230);
        sb.setStyle("-fx-background-color:" + SIDEBAR + "; -fx-border-color:" + BORDER + "; -fx-border-width:0 1 0 0;");
        sb.setPadding(new Insets(24, 16, 24, 16));
        Label logo = new Label("🎵 StudioMS");
        logo.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        logo.setTextFill(Color.web(ACCENT2));
        logo.setPadding(new Insets(0, 0, 24, 8));
        sb.getChildren().add(logo);
        sb.getChildren().add(sectionLabel("MAIN"));
        sb.getChildren().add(navBtn("📊  Dashboard",     "Dashboard"));
        sb.getChildren().add(sectionLabel("MANAGE"));
        sb.getChildren().add(navBtn("👤  Professionals", "Professionals"));
        sb.getChildren().add(navBtn("🏢  Studios",        "Studios"));
        sb.getChildren().add(navBtn("📁  Projects",       "Projects"));
        sb.getChildren().add(navBtn("🔧  Equipment",      "Equipment"));
        sb.getChildren().add(navBtn("🎬  Sessions",       "Sessions"));
        sb.getChildren().add(sectionLabel("REPORTS"));
        sb.getChildren().add(navBtn("📋  Inquiries",      "Inquiries"));
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sb.getChildren().add(spacer);
        Label ver = new Label("v1.0.0");
        ver.setFont(Font.font(11));
        ver.setTextFill(Color.web(MUTED));
        ver.setPadding(new Insets(0, 0, 0, 8));
        sb.getChildren().add(ver);
        return sb;
    }

    Label sectionLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("SansSerif", FontWeight.BOLD, 10));
        l.setTextFill(Color.web(MUTED));
        l.setPadding(new Insets(16, 0, 6, 8));
        return l;
    }

    Button navBtn(String text, String section) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(10, 12, 10, 14));
        btn.setFont(Font.font("SansSerif", 13));
        styleNavBtn(btn, section.equals(activeSection));
        btn.setOnAction(e -> {
            activeSection = section;
            refreshSidebar();
            switch (section) {
                case "Dashboard"     -> showDashboard();
                case "Professionals" -> showProfessionals();
                case "Studios"       -> showStudios();
                case "Projects"      -> showProjects();
                case "Equipment"     -> showEquipment();
                case "Sessions"      -> showSessions();
                case "Inquiries"     -> showInquiries();
            }
        });
        return btn;
    }

    void styleNavBtn(Button btn, boolean active) {
        if (active) {
            btn.setStyle("-fx-background-color:" + ACCENT + "22; -fx-text-fill:" + ACCENT2 +
                    "; -fx-background-radius:8; -fx-border-color:" + ACCENT + "; -fx-border-radius:8; -fx-border-width:0 0 0 3;");
        } else {
            btn.setStyle("-fx-background-color:transparent; -fx-text-fill:" + TEXT + "; -fx-background-radius:8; -fx-cursor:hand;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color:" + CARD + "; -fx-text-fill:" + TEXT + "; -fx-background-radius:8; -fx-cursor:hand;"));
            btn.setOnMouseExited(e  -> btn.setStyle("-fx-background-color:transparent; -fx-text-fill:" + TEXT + "; -fx-background-radius:8; -fx-cursor:hand;"));
        }
    }

    void refreshSidebar() {
        sidebar.getChildren().clear();
        Label logo = new Label("🎵 StudioMS");
        logo.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        logo.setTextFill(Color.web(ACCENT2));
        logo.setPadding(new Insets(0, 0, 24, 8));
        sidebar.getChildren().add(logo);
        sidebar.getChildren().add(sectionLabel("MAIN"));
        sidebar.getChildren().add(navBtn("📊  Dashboard",     "Dashboard"));
        sidebar.getChildren().add(sectionLabel("MANAGE"));
        sidebar.getChildren().add(navBtn("👤  Professionals", "Professionals"));
        sidebar.getChildren().add(navBtn("🏢  Studios",        "Studios"));
        sidebar.getChildren().add(navBtn("📁  Projects",       "Projects"));
        sidebar.getChildren().add(navBtn("🔧  Equipment",      "Equipment"));
        sidebar.getChildren().add(navBtn("🎬  Sessions",       "Sessions"));
        sidebar.getChildren().add(sectionLabel("REPORTS"));
        sidebar.getChildren().add(navBtn("📋  Inquiries",      "Inquiries"));
        Region spacer = new Region(); VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebar.getChildren().add(spacer);
        Label ver = new Label("v1.0.0"); ver.setFont(Font.font(11)); ver.setTextFill(Color.web(MUTED)); ver.setPadding(new Insets(0,0,0,8));
        sidebar.getChildren().add(ver);
    }

    void showDashboard() {
        VBox page = new VBox(24);
        page.setPadding(new Insets(10, 0, 0, 0));
        Label title = new Label("Dashboard");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 28));
        title.setTextFill(Color.web(TEXT));
        Label sub = new Label("Welcome back — Studio Management System");
        sub.setFont(Font.font(14)); sub.setTextFill(Color.web(MUTED));
        HBox stats = new HBox(16);
        stats.getChildren().addAll(
                statCard("👤", "Professionals", String.valueOf(professionals.size()), ACCENT),
                statCard("🏢", "Studios",        String.valueOf(studios.size()),       "#06B6D4"),
                statCard("📁", "Projects",       String.valueOf(projects.size()),      SUCCESS),
                statCard("🔧", "Equipment",      String.valueOf(equipment.size()),     "#F59E0B"),
                statCard("🎬", "Sessions",       String.valueOf(sessions.size()),      ACCENT2)
        );
        Label qLabel = new Label("Quick Actions");
        qLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        qLabel.setTextFill(Color.web(TEXT));
        HBox actions = new HBox(12);
        actions.getChildren().addAll(
                quickAction("+ Add Professional", ACCENT,    () -> { activeSection="Professionals"; refreshSidebar(); showProfessionals(); }),
                quickAction("+ Add Studio",        "#06B6D4", () -> { activeSection="Studios";       refreshSidebar(); showStudios(); }),
                quickAction("+ Add Project",       SUCCESS,   () -> { activeSection="Projects";      refreshSidebar(); showProjects(); }),
                quickAction("+ Add Session",       ACCENT2,   () -> { activeSection="Sessions";      refreshSidebar(); showSessions(); }),
                quickAction("View Inquiries",      "#F59E0B", () -> { activeSection="Inquiries";     refreshSidebar(); showInquiries(); })
        );
        page.getChildren().addAll(title, sub, stats, qLabel, actions);
        contentArea.getChildren().setAll(page);
    }

    VBox statCard(String icon, String label, String value, String color) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setPrefWidth(160);
        card.setStyle("-fx-background-color:" + CARD + "; -fx-background-radius:12; -fx-border-color:" + BORDER + "; -fx-border-radius:12;");
        card.setEffect(new DropShadow(15, Color.web(color, 0.2)));
        Label ic  = new Label(icon);  ic.setFont(Font.font(24));
        Label val = new Label(value); val.setFont(Font.font("Georgia", FontWeight.BOLD, 32)); val.setTextFill(Color.web(color));
        Label lbl = new Label(label); lbl.setFont(Font.font(13)); lbl.setTextFill(Color.web(MUTED));
        card.getChildren().addAll(ic, val, lbl);
        return card;
    }

    Button quickAction(String text, String color, Runnable action) {
        Button btn = new Button(text);
        btn.setPadding(new Insets(10, 20, 10, 20));
        btn.setFont(Font.font("SansSerif", FontWeight.BOLD, 13));
        btn.setStyle("-fx-background-color:" + color + "; -fx-text-fill:white; -fx-background-radius:8; -fx-cursor:hand;");
        btn.setOnAction(e -> action.run());
        return btn;
    }

    VBox buildTablePage(String titleText, String icon, String[] cols,
                        ObservableList<String[]> data, String[] labels, Runnable onAdd) {
        VBox page = new VBox(20);
        page.setPadding(new Insets(10, 0, 0, 0));
        Label title = new Label(icon + "  " + titleText);
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        title.setTextFill(Color.web(TEXT));
        HBox topBar = new HBox(12);
        topBar.setAlignment(Pos.CENTER_LEFT);
        TextField search = new TextField();
        search.setPromptText("Search...");
        search.setPrefWidth(280);
        search.setPadding(new Insets(9, 14, 9, 14));
        search.setStyle("-fx-background-color:" + CARD + "; -fx-text-fill:" + TEXT +
                "; -fx-prompt-text-fill:" + MUTED + "; -fx-background-radius:8; -fx-border-color:" + BORDER + "; -fx-border-radius:8;");
        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setPadding(new Insets(9, 18, 9, 18));
        refreshBtn.setFont(Font.font("SansSerif", FontWeight.BOLD, 13));
        refreshBtn.setStyle("-fx-background-color:" + CARD + "; -fx-text-fill:" + TEXT + "; -fx-background-radius:8; -fx-cursor:hand; -fx-border-color:" + BORDER + "; -fx-border-width:1;");
        refreshBtn.setOnAction(e -> loadAllData());
        Button addBtn = new Button("+ Add");
        addBtn.setPadding(new Insets(9, 18, 9, 18));
        addBtn.setFont(Font.font("SansSerif", FontWeight.BOLD, 13));
        addBtn.setStyle("-fx-background-color:" + ACCENT + "; -fx-text-fill:white; -fx-background-radius:8; -fx-cursor:hand;");
        addBtn.setOnAction(e -> onAdd.run());
        topBar.getChildren().addAll(search, sp, refreshBtn, addBtn);
        TableView<String[]> table = new TableView<>();
        table.setStyle("-fx-background-color:" + CARD + "; -fx-border-color:" + BORDER + "; -fx-border-radius:12; -fx-background-radius:12;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for (int i = 0; i < cols.length; i++) {
            final int idx = i;
            TableColumn<String[], String> col = new TableColumn<>(cols[i]);
            col.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(
                    p.getValue().length > idx ? p.getValue()[idx] : ""));
            table.getColumns().add(col);
        }
        TableColumn<String[], String> delCol = new TableColumn<>("Action");
        delCol.setCellFactory(tc -> new TableCell<>() {
            final Button del = new Button("Delete");
            {
                del.setStyle("-fx-background-color:" + DANGER + "33; -fx-text-fill:" + DANGER +
                        "; -fx-background-radius:6; -fx-cursor:hand; -fx-font-size:11;");
                del.setOnAction(e -> {
                    String[] row = getTableView().getItems().get(getIndex());
                    deleteFromDB(titleText, row[0]);
                    data.remove(row);
                });
            }
            @Override protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty); setGraphic(empty ? null : del);
            }
        });
        table.getColumns().add(delCol);
        table.setItems(data);
        VBox.setVgrow(table, Priority.ALWAYS);
        page.getChildren().addAll(title, topBar, table);
        return page;
    }

    void showAddDialog(String titleText, String[] fieldLabels, java.util.function.Consumer<String[]> onSave) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Add " + titleText);
        dialog.setHeaderText(null);
        DialogPane dp = dialog.getDialogPane();
        dp.setStyle("-fx-background-color:" + CARD + ";");
        VBox form = new VBox(14);
        form.setPadding(new Insets(20));
        TextField[] fields = new TextField[fieldLabels.length];
        Label heading = new Label("New " + titleText);
        heading.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        heading.setTextFill(Color.web(TEXT));
        form.getChildren().add(heading);
        for (int i = 0; i < fieldLabels.length; i++) {
            Label lbl = new Label(fieldLabels[i]);
            lbl.setFont(Font.font(12)); lbl.setTextFill(Color.web(MUTED));
            fields[i] = new TextField();
            fields[i].setPromptText(fieldLabels[i]);
            fields[i].setPadding(new Insets(8, 12, 8, 12));
            fields[i].setStyle("-fx-background-color:#252836; -fx-text-fill:" + TEXT +
                    "; -fx-prompt-text-fill:" + MUTED + "; -fx-background-radius:6; -fx-border-color:" + BORDER + "; -fx-border-radius:6;");
            form.getChildren().addAll(lbl, fields[i]);
        }
        dp.setContent(form);
        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dp.getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);
        Button saveButton = (Button) dp.lookupButton(saveBtn);
        saveButton.setStyle("-fx-background-color:" + ACCENT + "; -fx-text-fill:white; -fx-background-radius:6;");
        dialog.setResultConverter(bt -> {
            if (bt == saveBtn) {
                String[] result = new String[fields.length];
                for (int i = 0; i < fields.length; i++) result[i] = fields[i].getText();
                return result;
            }
            return null;
        });
        dialog.showAndWait().ifPresent(onSave);
    }

    void showProfessionals() {
        String[] cols   = {"ID", "Full Name", "Role"};
        String[] labels = {"Professional ID", "Full Name", "Role"};
        contentArea.getChildren().setAll(
                buildTablePage("Professionals", "👤", cols, professionals, labels, () ->
                        showAddDialog("Professional", labels, vals -> {
                            try {
                                PreparedStatement ps = conn.prepareStatement(
                                        "INSERT INTO PROFESSIONAL (PROFESSIONALLD, FULLNAME, ROLE) VALUES (?, ?, ?)");
                                ps.setInt(1, Integer.parseInt(vals[0]));
                                ps.setString(2, vals[1]);
                                ps.setString(3, vals[2]);
                                ps.executeUpdate();
                                loadAllData();
                                showProfessionals();
                            } catch (SQLException e) { showAlert("Error", e.getMessage()); }
                        })));
    }

    void showStudios() {
        String[] cols   = {"ID", "Name", "Type", "Wing", "Available"};
        String[] labels = {"Studio ID", "Studio Name", "Type", "Wing", "Available (true/false)"};
        contentArea.getChildren().setAll(
                buildTablePage("Studios", "🏢", cols, studios, labels, () ->
                        showAddDialog("Studio", labels, vals -> {
                            try {
                                PreparedStatement ps = conn.prepareStatement(
                                        "INSERT INTO STUDIO (STUDIOID, STUDIONAME, STUDIOTYPE, WING, STUDIO_AVAILABILITY) VALUES (?, ?, ?, ?, ?)");
                                ps.setInt(1, Integer.parseInt(vals[0]));
                                ps.setString(2, vals[1]);
                                ps.setString(3, vals[2]);
                                ps.setString(4, vals[3]);
                                ps.setBoolean(5, Boolean.parseBoolean(vals[4]));
                                ps.executeUpdate();
                                loadAllData();
                                showStudios();
                            } catch (SQLException e) { showAlert("Error", e.getMessage()); }
                        })));
    }

    void showProjects() {
        String[] cols   = {"ID", "Title", "Date", "Budget", "Deadline"};
        String[] labels = {"Project ID", "Title", "Date (YYYY-MM-DD)", "Budget", "Deadline (YYYY-MM-DD)"};
        contentArea.getChildren().setAll(
                buildTablePage("Projects", "📁", cols, projects, labels, () ->
                        showAddDialog("Project", labels, vals -> {
                            try {
                                PreparedStatement ps = conn.prepareStatement(
                                        "INSERT INTO PROJECT (PROJECTID, TITLE, PROJECTDATE, BUDGET, DEADLINE) VALUES (?, ?, ?, ?, ?)");
                                ps.setInt(1, Integer.parseInt(vals[0]));
                                ps.setString(2, vals[1]);
                                ps.setDate(3, Date.valueOf(vals[2]));
                                ps.setDouble(4, Double.parseDouble(vals[3]));
                                ps.setDate(5, Date.valueOf(vals[4]));
                                ps.executeUpdate();
                                loadAllData();
                                showProjects();
                            } catch (SQLException e) { showAlert("Error", e.getMessage()); }
                        })));
    }

    void showEquipment() {
        String[] cols   = {"ID", "Name", "Type", "Serial Number"};
        String[] labels = {"Equipment ID", "Name", "Type", "Serial Number"};
        contentArea.getChildren().setAll(
                buildTablePage("Equipment", "🔧", cols, equipment, labels, () ->
                        showAddDialog("Equipment", labels, vals -> {
                            try {
                                PreparedStatement ps = conn.prepareStatement(
                                        "INSERT INTO EQUIPMENT (EQUIPMENTID, NAME, TYPE, SERIALNUMBER) VALUES (?, ?, ?, ?)");
                                ps.setInt(1, Integer.parseInt(vals[0]));
                                ps.setString(2, vals[1]);
                                ps.setString(3, vals[2]);
                                ps.setString(4, vals[3]);
                                ps.executeUpdate();
                                loadAllData();
                                showEquipment();
                            } catch (SQLException e) { showAlert("Error", e.getMessage()); }
                        })));
    }

    void showSessions() {
        String[] cols   = {"ID", "Project ID", "Studio ID", "Date", "Start", "End"};
        String[] labels = {"Session ID", "Project ID", "Studio ID", "Date (YYYY-MM-DD)",
                "Start (YYYY-MM-DD HH:MM:SS)", "End (YYYY-MM-DD HH:MM:SS)"};
        contentArea.getChildren().setAll(
                buildTablePage("Sessions", "🎬", cols, sessions, labels, () ->
                        showAddDialog("Session", labels, vals -> {
                            try {
                                PreparedStatement ps = conn.prepareStatement(
                                        "INSERT INTO SESSION (SESSIONID, PROJECTID, STUDIOID, SESSIONDATE, SESSIONSTART, SESSIONEND) VALUES (?, ?, ?, ?, ?, ?)");
                                ps.setInt(1, Integer.parseInt(vals[0]));
                                ps.setInt(2, Integer.parseInt(vals[1]));
                                ps.setInt(3, Integer.parseInt(vals[2]));
                                ps.setDate(4, Date.valueOf(vals[3]));
                                ps.setTimestamp(5, Timestamp.valueOf(vals[4]));
                                ps.setTimestamp(6, Timestamp.valueOf(vals[5]));
                                ps.executeUpdate();
                                loadAllData();
                                showSessions();
                            } catch (SQLException e) { showAlert("Error", e.getMessage()); }
                        })));
    }

    void showInquiries() {
        VBox page = new VBox(20);
        page.setPadding(new Insets(10, 0, 0, 0));
        Label title = new Label("Inquiries & Reports");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        title.setTextFill(Color.web(TEXT));
        Label sub = new Label("Run analytical queries on your studio data");
        sub.setFont(Font.font(14)); sub.setTextFill(Color.web(MUTED));
        VBox cards = new VBox(12);
        cards.getChildren().addAll(
                inquiryCard("Most Demanded Skill",  "The most assigned professional role across all sessions.", ACCENT),
                inquiryCard("Inactive Projects",     "Projects with no sessions in the last month.",            "#F59E0B"),
                inquiryCard("Top Equipment Pro",     "Professional with highest equipment variety last month.", ACCENT2),
                inquiryCard("Idle Studios",          "Studios that had no sessions last month.",                DANGER),
                inquiryCard("Equipment Per Project", "Equipment items used per project last month.",            "#06B6D4"),
                inquiryCard("Professional Stats",    "Professionals and their project count last month.",       SUCCESS)
        );
        ScrollPane scroll = new ScrollPane(cards);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color:transparent; -fx-background:transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);
        page.getChildren().addAll(title, sub, scroll);
        contentArea.getChildren().setAll(page);
    }

    HBox inquiryCard(String name, String desc, String color) {
        HBox card = new HBox(16);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(18, 20, 18, 20));
        card.setStyle("-fx-background-color:" + CARD + "; -fx-background-radius:10; -fx-border-color:" + BORDER + "; -fx-border-radius:10;");
        Circle dot = new Circle(6, Color.web(color));
        VBox info = new VBox(4);
        Label n = new Label(name); n.setFont(Font.font("Georgia", FontWeight.BOLD, 14)); n.setTextFill(Color.web(TEXT));
        Label d = new Label(desc); d.setFont(Font.font(12)); d.setTextFill(Color.web(MUTED));
        info.getChildren().addAll(n, d);
        HBox.setHgrow(info, Priority.ALWAYS);
        Button run = new Button("Run");
        run.setStyle("-fx-background-color:" + color + "22; -fx-text-fill:" + color +
                "; -fx-background-radius:6; -fx-cursor:hand; -fx-border-color:" + color + "; -fx-border-radius:6; -fx-border-width:1;");
        run.setOnAction(e -> runInquiry(name));
        card.getChildren().addAll(dot, info, run);
        return card;
    }

    void runInquiry(String name) {
        if (conn == null) { showAlert("Error", "No database connection."); return; }
        StringBuilder result = new StringBuilder();
        try {
            ResultSet rs;
            switch (name) {
                case "Most Demanded Skill" -> {
                    rs = conn.createStatement().executeQuery(
                            "SELECT TOP 1 ROLE, COUNT(*) AS AssignmentCount FROM PROFESSIONAL p " +
                                    "INNER JOIN SESSION_PROFESSIONAL sp ON sp.PROFESSIONALLD = p.PROFESSIONALLD " +
                                    "GROUP BY ROLE ORDER BY AssignmentCount DESC");
                    if (rs.next())
                        result.append("Most demanded role: ").append(rs.getString("ROLE"))
                                .append("\nAssigned: ").append(rs.getInt("AssignmentCount")).append(" times");
                    else result.append("No data found.");
                }
                case "Inactive Projects" -> {
                    rs = conn.createStatement().executeQuery(
                            "SELECT pr.PROJECTID, TITLE FROM PROJECT pr " +
                                    "LEFT OUTER JOIN SESSION s ON pr.PROJECTID = s.PROJECTID " +
                                    "AND s.SESSIONDATE >= (SELECT DATEADD(Month, -1, MAX(SESSIONDATE)) FROM SESSION) " +
                                    "WHERE s.SESSIONID IS NULL");
                    boolean found = false;
                    while (rs.next()) { result.append("• ").append(rs.getString("TITLE")).append("\n"); found = true; }
                    if (!found) result.append("All projects have sessions last month.");
                }
                case "Top Equipment Pro" -> {
                    rs = conn.createStatement().executeQuery(
                            "SELECT TOP 1 P.PROFESSIONALLD, P.FULLNAME, P.ROLE, COUNT(DISTINCT SE.EQUIPMENTID) AS UNIQUE_EQUIPMENT_COUNT " +
                                    "FROM PROFESSIONAL P " +
                                    "JOIN SESSION_PROFESSIONAL SP ON P.PROFESSIONALLD = SP.PROFESSIONALLD " +
                                    "JOIN SESSION_EQUIPMENT SE ON SP.SESSIONID = SE.SESSIONID " +
                                    "JOIN SESSION S ON SP.SESSIONID = S.SESSIONID " +
                                    "WHERE MONTH(S.SESSIONDATE) = MONTH(DATEADD(MONTH, -1, GETDATE())) " +
                                    "AND YEAR(S.SESSIONDATE) = YEAR(DATEADD(MONTH, -1, GETDATE())) " +
                                    "GROUP BY P.PROFESSIONALLD, P.FULLNAME, P.ROLE ORDER BY UNIQUE_EQUIPMENT_COUNT DESC");
                    if (rs.next())
                        result.append("Professional: ").append(rs.getString("FULLNAME"))
                                .append("\nRole: ").append(rs.getString("ROLE"))
                                .append("\nUnique Equipment: ").append(rs.getInt("UNIQUE_EQUIPMENT_COUNT"));
                    else result.append("No data found for last month.");
                }
                case "Idle Studios" -> {
                    rs = conn.createStatement().executeQuery(
                            "SELECT S.STUDIOID, S.STUDIONAME, S.WING FROM STUDIO S " +
                                    "WHERE S.STUDIOID NOT IN (SELECT DISTINCT STUDIOID FROM SESSION " +
                                    "WHERE MONTH(SESSIONDATE) = MONTH(DATEADD(MONTH, -1, GETDATE())) " +
                                    "AND YEAR(SESSIONDATE) = YEAR(DATEADD(MONTH, -1, GETDATE())))");
                    boolean found = false;
                    while (rs.next()) { result.append("• ").append(rs.getString("STUDIONAME")).append(" (").append(rs.getString("WING")).append(")\n"); found = true; }
                    if (!found) result.append("All studios had sessions last month.");
                }
                case "Equipment Per Project" -> {
                    rs = conn.createStatement().executeQuery(
                            "SELECT p.TITLE, e.NAME AS EQUIPMENT_NAME, e.TYPE " +
                                    "FROM PROJECT p JOIN SESSION s ON p.PROJECTID = s.PROJECTID " +
                                    "JOIN SESSION_EQUIPMENT se ON s.SESSIONID = se.SESSIONID " +
                                    "JOIN EQUIPMENT e ON se.EQUIPMENTID = e.EQUIPMENTID " +
                                    "WHERE s.SESSIONDATE >= (SELECT DATEADD(MONTH, -1, MAX(SESSIONDATE)) FROM SESSION) " +
                                    "ORDER BY p.PROJECTID");
                    boolean found = false;
                    while (rs.next()) { result.append("• [").append(rs.getString("TITLE")).append("] ").append(rs.getString("EQUIPMENT_NAME")).append(" - ").append(rs.getString("TYPE")).append("\n"); found = true; }
                    if (!found) result.append("No equipment data found for last month.");
                }
                case "Professional Stats" -> {
                    rs = conn.createStatement().executeQuery(
                            "SELECT p.FULLNAME, p.ROLE, COUNT(DISTINCT s.PROJECTID) AS PROJECT_COUNT " +
                                    "FROM PROFESSIONAL p JOIN SESSION_PROFESSIONAL sp ON p.PROFESSIONALLD = sp.PROFESSIONALLD " +
                                    "JOIN SESSION s ON sp.SESSIONID = s.SESSIONID " +
                                    "WHERE s.SESSIONDATE >= (SELECT DATEADD(MONTH, -1, MAX(SESSIONDATE)) FROM SESSION) " +
                                    "GROUP BY p.PROFESSIONALLD, p.FULLNAME, p.ROLE ORDER BY PROJECT_COUNT DESC");
                    boolean found = false;
                    while (rs.next()) { result.append("• ").append(rs.getString("FULLNAME")).append(" (").append(rs.getString("ROLE")).append(") — ").append(rs.getInt("PROJECT_COUNT")).append(" project(s)\n"); found = true; }
                    if (!found) result.append("No data found for last month.");
                }
            }
        } catch (SQLException e) {
            result.append("Query error: ").append(e.getMessage());
        }
        showAlert(name, result.toString());
    }

    void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.getDialogPane().setStyle("-fx-background-color:" + CARD + "; -fx-font-size:13;");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}