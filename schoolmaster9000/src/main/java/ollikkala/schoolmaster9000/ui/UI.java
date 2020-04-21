/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ollikkala.schoolmaster9000.dao.CourseDao;
import ollikkala.schoolmaster9000.dao.SchoolDao;
import ollikkala.schoolmaster9000.dao.UserDao;
import ollikkala.schoolmaster9000.domain.Course;
import ollikkala.schoolmaster9000.domain.SchoolService;
import ollikkala.schoolmaster9000.domain.User;
import ollikkala.schoolmaster9000.domain.UserService;

/**
 *
 * @author anttiollikkala
 */
public class UI extends Application {

    private User currentUser;
    private UserDao userDao;
    private CourseDao courseDao;
    private SchoolDao schoolDao;
    private Connection connection;
    private SchoolService schoolService;
    private UserService userService;
    private Stage stage;
    private Scene ascene;

    @Override
    public void init() throws Exception {
        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        this.schoolService = new SchoolService(this.connection);
        
        this.courseDao = new CourseDao(this.connection);
        this.schoolDao = new SchoolDao(this.connection);
        this.userDao = new UserDao(this.connection);
        this.userService = new UserService(this.userDao);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        BorderPane loginScreen = this.getLoginScene();

        Scene scene;
        if (this.schoolDao.GetSchoolName().equals("")) {
            scene = new Scene(this.getCreateNewScoolView(), 640, 480);
        } else {
            scene = new Scene(loginScreen, 640, 480);
        }
        stage.setScene(scene);
        stage.show();
    }

    private Scene getLoggedInScene() {
        SplitPane sp = new SplitPane();
        TreeView<String> leftMenu = this.getLeftMenu();
        VBox rightControl = new VBox();
        HBox leftControl = new HBox(leftMenu);
        HBox.setHgrow(leftControl, Priority.ALWAYS);
        leftControl.setMaxWidth(250);
        leftControl.setMinWidth(200);
        sp.getItems().addAll(leftControl, rightControl);
        leftMenu.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    rightControl.getChildren().clear();
                    switch (newValue.getValue()) {
                        case "Tilastot":
                            rightControl.getChildren().add(this.getStatisticsView());
                            break;
                        case "Omat tiedot":
                            rightControl.getChildren().add(this.getEditPersonalInformationView());
                            break;
                        case "Kurssit":
                            rightControl.getChildren().add(this.getCoursesView());
                            break;
                        default:
                            break;
                    }
                });

        leftMenu.getSelectionModel().select(leftMenu.getRoot().getChildren().get(0));
        Scene scene = new Scene(sp, 960, 720);
        this.ascene = scene;
        return scene;
    }

    private BorderPane getCreateNewScoolView() {
        BorderPane bp = new BorderPane();
        Pane pane = new Pane();
        VBox stack = new VBox();
        stack.setMaxWidth(300);
        stack.setAlignment(Pos.CENTER);
        stack.setSpacing(5);
        Label otsikko = new Label("Luo uusi koulu");
        otsikko.setFont(new Font("System", 24));
        stack.getChildren().add(otsikko);
        stack.getChildren().add(new Label("Koulun nimi"));
        TextField schoolNameField = new TextField();
        stack.getChildren().add(schoolNameField);

        stack.getChildren().add(new Label("Etunimesi"));
        TextField firstNameField = new TextField();
        stack.getChildren().add(firstNameField);

        stack.getChildren().add(new Label("Sukunimesi"));
        TextField lastNameField = new TextField();
        stack.getChildren().add(lastNameField);

        stack.getChildren().add(new Label("Sähköpostiosoitteesi"));
        TextField emailField = new TextField();
        stack.getChildren().add(emailField);

        stack.getChildren().add(new Label("Valitse salasana"));
        TextField passwordField = new PasswordField();
        stack.getChildren().add(passwordField);

        Button loginButton = new Button("Kirjaudu");
        loginButton.setOnAction((ActionEvent e) -> {
            this.userDao.install();
            this.schoolDao.install();
            this.courseDao.install();
            this.schoolDao.create(schoolNameField.getText());
            User principal = new User(firstNameField.getText(), lastNameField.getText(), emailField.getText());
            principal.setPassword(passwordField.getText());
            this.userDao.create(principal);
            this.userDao.setPrincipal(principal.id());
            principal.setPrincipal(true);
            this.currentUser = principal;
            this.stage.setScene(this.getLoggedInScene());
        });

        stack.getChildren().add(loginButton);
        bp.setCenter(stack);
        return bp;
    }

    private VBox getEditPersonalInformationView() {
        VBox stack = new VBox();
        stack.setSpacing(5);
        stack.setPadding(new Insets(10, 10, 10, 10));

        Label otsikko = new Label("Henkilötiedot");
        otsikko.setFont(new Font("System", 24));
        stack.getChildren().add(otsikko);

        stack.getChildren().add(new Label("Etunimi"));
        TextField firstName = new TextField(this.currentUser.firstName());
        firstName.setEditable(false);
        stack.getChildren().add(firstName);

        stack.getChildren().add(new Label("Etunimi"));
        TextField lastName = new TextField(this.currentUser.lastName());
        lastName.setEditable(false);
        stack.getChildren().add(lastName);

        stack.getChildren().add(new Label("Opiskelijanumero"));
        TextField studentId = new TextField(String.valueOf(this.currentUser.id()));
        studentId.setEditable(false);
        stack.getChildren().add(studentId);

        stack.getChildren().add(new Label("Sähköposti"));
        TextField emailField = new TextField(this.currentUser.email());
        emailField.setEditable(false);
        stack.getChildren().add(emailField);
        return stack;
    }

    private VBox getStatisticsView() {
        VBox stack = new VBox();
        stack.setSpacing(5);
        stack.setPadding(new Insets(10, 10, 10, 10));
        Label otsikko = new Label("Tilastot");
        otsikko.setFont(new Font("System", 24));
        stack.getChildren().add(otsikko);
        return stack;
    }

    private VBox getCoursesView() {
        VBox stack = new VBox();
        stack.setSpacing(5);
        stack.setPadding(new Insets(10, 10, 10, 10));
        Label otsikko = new Label("Kurssit");
        otsikko.setFont(new Font("System", 24));
        stack.getChildren().add(otsikko);
        TableView table = new TableView();

        TableColumn idColumn = new TableColumn("id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        table.getColumns().add(idColumn);
        
        TableColumn nameColumn = new TableColumn("nimi");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().add(nameColumn);
        
        TableColumn identifierColumn = new TableColumn("tunnus");
        identifierColumn .setCellValueFactory(new PropertyValueFactory<>("identifier"));
        table.getColumns().add(identifierColumn);
        
        TableColumn participantCountColumn = new TableColumn("osallistujia");
        participantCountColumn .setCellValueFactory(new PropertyValueFactory<>("participantCount"));
        table.getColumns().add(participantCountColumn);
        
        stack.getChildren().add(table);
        
        ArrayList<Course> courses = this.courseDao.getAll();
        
        courses.forEach( course -> {
            table.getItems().add(course);
        });
        
        return stack;
    }

    private TreeView getLeftMenu() {
        TreeItem<String> rootItem = new TreeItem<>("Inbox");
        TreeView<String> tree = new TreeView<>(rootItem);
        rootItem.getChildren().add(new TreeItem<>("Tilastot"));

        TreeItem kurssitItem = new TreeItem<>("Kurssit");
        rootItem.getChildren().add(kurssitItem);
        rootItem.getChildren().add(new TreeItem<>("Omat tiedot"));
        if (this.currentUser.isPrincipal() || this.currentUser.isTeacher()) {
            TreeItem opiskelijatItem = new TreeItem<>("Opiskelijat");
            rootItem.getChildren().add(opiskelijatItem);
            opiskelijatItem.getChildren().add(new TreeItem<>("Uusi Opiskelija"));
            kurssitItem.getChildren().add(new TreeItem<>("Uusi kurssi"));
        }
        if (this.currentUser.isPrincipal()) {
            TreeItem opettajatItem = new TreeItem<>("Opettajat");
            rootItem.getChildren().add(opettajatItem);
            opettajatItem.getChildren().add(new TreeItem<>("Uusi opettaja"));
        }
        tree.setShowRoot(false);
        return tree;
    }

    private BorderPane getLoginScene() {
        BorderPane bp = new BorderPane();
        VBox stack = new VBox();
        stack.setMaxWidth(300);
        stack.setAlignment(Pos.CENTER);
        stack.setSpacing(5);
        
        Label title = new Label(this.schoolDao.GetSchoolName());
        title.setFont(new Font("System", 24));
        stack.getChildren().add(title);
        
        stack.getChildren().add(new Label("Sähköpostiosoite"));
        TextField emailField = new TextField();
        stack.getChildren().add(emailField);
        stack.getChildren().add(new Label("Salasana"));
        PasswordField passwordField = new PasswordField();
        stack.getChildren().add(passwordField);
        Button loginButton = new Button("Kirjaudu");
        loginButton.setOnAction((ActionEvent e) -> {
            User user = this.userService.login(emailField.getText(), passwordField.getText());
            if (user != null) {
                this.currentUser = user;
                this.stage.setScene(this.getLoggedInScene());
            } else {
                emailField.setText("");
                passwordField.setText("");
            }
        });

        stack.getChildren().add(loginButton);
        bp.setCenter(stack);
        return bp;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
