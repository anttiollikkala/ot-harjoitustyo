/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.ui;

import java.sql.Connection;
import java.sql.DriverManager;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ollikkala.schoolmaster9000.domain.SchoolService;
import ollikkala.schoolmaster9000.domain.UserService;

/**
 *
 * @author anttiollikkala
 */
public class UI extends Application {

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
        this.userService = new UserService(this.connection);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        BorderPane loginScreen = this.getLoginScene();

        Scene scene;
        if (this.userService.getCount() == 0) {
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
        stack.getChildren().add(new TextField());
        stack.getChildren().add(new Label("Nimesi"));
        stack.getChildren().add(new TextField());
        stack.getChildren().add(new Label("Sähköpostiosoitteesi"));
        stack.getChildren().add(new TextField());
        stack.getChildren().add(new Label("Valitse salasana"));
        stack.getChildren().add(new PasswordField());
        Button loginButton = new Button("Kirjaudu");
        loginButton.setOnAction((ActionEvent e) -> {
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
        TextField firstName = new TextField("Testi");
        firstName.setEditable(false);
        stack.getChildren().add(firstName);

        stack.getChildren().add(new Label("Etunimi"));
        TextField lastName = new TextField("Käyttäjä");
        lastName.setEditable(false);
        stack.getChildren().add(lastName);

        stack.getChildren().add(new Label("Opiskelijanumero"));
        TextField studentId = new TextField("023452131");
        studentId.setEditable(false);
        stack.getChildren().add(studentId);

        stack.getChildren().add(new Label("Sähköposti"));
        stack.getChildren().add(new TextField("testikayttaja@helsinki.fi"));

        stack.getChildren().add(new Label("Uusi salasana"));
        stack.getChildren().add(new PasswordField());

        Button saveButton = new Button("Tallenna");
        stack.getChildren().add(saveButton);

        saveButton.setOnAction((ActionEvent e) -> {
            System.out.println("Clicked!");
        });
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
        return stack;
    }

    private TreeView getLeftMenu() {
        TreeItem<String> rootItem = new TreeItem<>("Inbox");
        TreeView<String> tree = new TreeView<>(rootItem);
        rootItem.getChildren().add(new TreeItem<>("Tilastot"));
        rootItem.getChildren().add(new TreeItem<>("Kurssit"));
        rootItem.getChildren().add(new TreeItem<>("Omat tiedot"));
        tree.setShowRoot(false);
        return tree;
    }

    private BorderPane getLoginScene() {
        BorderPane bp = new BorderPane();
        VBox stack = new VBox();
        stack.setMaxWidth(300);
        stack.setAlignment(Pos.CENTER);
        stack.setSpacing(5);
        stack.getChildren().add(new Label("Sähköpostiosoite"));
        stack.getChildren().add(new TextField());
        stack.getChildren().add(new Label("Salasana"));
        stack.getChildren().add(new PasswordField());
        Button loginButton = new Button("Kirjaudu");
        loginButton.setOnAction((ActionEvent e) -> {
            this.stage.setScene(this.getLoggedInScene());
        });

        stack.getChildren().add(loginButton);
        bp.setCenter(stack);
        return bp;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
