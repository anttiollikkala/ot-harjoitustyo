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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
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
    private TreeView<String> leftMenu;

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
        if (this.schoolDao.getSchoolName().equals("")) {
            scene = new Scene(this.getCreateNewScoolView(), 640, 480);
        } else {
            scene = new Scene(loginScreen, 640, 480);
        }
        stage.setScene(scene);
        stage.show();
    }

    private Scene getLoggedInScene() {
        SplitPane sp = new SplitPane();
        TreeView<String> leftm = this.getLeftMenu();
        VBox rightControl = new VBox();
        this.leftMenu = leftm;
        HBox leftControl = new HBox(leftm);
        HBox.setHgrow(leftControl, Priority.ALWAYS);
        leftControl.setMaxWidth(250);
        leftControl.setMinWidth(200);
        sp.getItems().addAll(leftControl, rightControl);
        leftm.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    rightControl.getChildren().clear();
                    switch (newValue.getValue()) {
                        case "Omat tiedot":
                            rightControl.getChildren().add(this.getEditPersonalInformationView());
                            break;
                        case "Kurssit":
                            rightControl.getChildren().add(this.getCoursesView());
                            break;
                        case "Uusi kurssi":
                            rightControl.getChildren().add(this.getNewCourseView());
                            break;
                        case "Uusi opettaja":
                            rightControl.getChildren().add(this.getNewTeacherView());
                            break;
                        case "Opettajat":
                            rightControl.getChildren().add(this.getTeachersView());
                            break;
                        case "Opiskelijat":
                            rightControl.getChildren().add(this.getStudentsView());
                            break;
                        case "Uusi opiskelija":
                            rightControl.getChildren().add(this.getNewStudentView());
                            break;
                        default:
                            break;
                    }
                });

        leftm.getSelectionModel().select(leftm.getRoot().getChildren().get(0));
        Scene scene = new Scene(sp, 960, 720);
        this.ascene = scene;
        return scene;
    }

    private BorderPane getCreateNewScoolView() {
        BorderPane bp = new BorderPane();
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
            if (schoolNameField.getText().equals("") || firstNameField.getText().equals("") || lastNameField.getText().equals("") || emailField.getText().equals("") || passwordField.getText().equals("")) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Virhe");
                alert.setHeaderText(null);
                alert.setContentText("Kaikki kentät tulee olla täytetty!");
                alert.showAndWait();
            } else {
                this.userDao.install();
                this.schoolDao.install();
                this.courseDao.install();
                this.schoolDao.create(schoolNameField.getText());
                User principal = new User(firstNameField.getText(), lastNameField.getText(), emailField.getText());
                principal.setPassword(passwordField.getText());
                this.userDao.create(principal);
                this.userDao.setPrincipal(principal.getId());
                principal.setPrincipal(true);
                this.currentUser = principal;
                this.stage.setScene(this.getLoggedInScene());
            }
        });

        stack.getChildren().add(loginButton);
        bp.setCenter(stack);
        return bp;
    }

    private VBox getEditPersonalInformationView() {
        VBox stack = new VBox();
        stack.setSpacing(5);
        stack.setPadding(new Insets(10, 10, 10, 10));

        Label otsikko = new Label("Omat tiedot");
        otsikko.setFont(new Font("System", 24));
        stack.getChildren().add(otsikko);

        stack.getChildren().add(new Label("ID"));
        TextField studentId = new TextField(String.valueOf(this.currentUser.getId()));
        studentId.setEditable(false);
        stack.getChildren().add(studentId);

        stack.getChildren().add(new Label("Etunimi"));
        TextField firstName = new TextField(this.currentUser.getFirstName());
        firstName.setEditable(false);
        stack.getChildren().add(firstName);

        stack.getChildren().add(new Label("Etunimi"));
        TextField lastName = new TextField(this.currentUser.getLastName());
        lastName.setEditable(false);
        stack.getChildren().add(lastName);

        stack.getChildren().add(new Label("Sähköposti"));
        TextField emailField = new TextField(this.currentUser.getEmail());
        emailField.setEditable(false);
        stack.getChildren().add(emailField);

        if (this.currentUser.isStudent()) {

            Label myCoursesTitle = new Label("Omat kurssit");
            myCoursesTitle.setFont(new Font("System", 18));
            stack.getChildren().add(myCoursesTitle);

            TableView table = new TableView();

            TableColumn idColumn = new TableColumn("id");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            table.getColumns().add(idColumn);

            TableColumn nameColumn = new TableColumn("nimi");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            table.getColumns().add(nameColumn);

            TableColumn identifierColumn = new TableColumn("tunnus");
            identifierColumn.setCellValueFactory(new PropertyValueFactory<>("identifier"));
            table.getColumns().add(identifierColumn);

            TableColumn participantCountColumn = new TableColumn("osallistujia");
            participantCountColumn.setCellValueFactory(new PropertyValueFactory<>("participantsCount"));
            table.getColumns().add(participantCountColumn);

            TableColumn studyPointsColumn = new TableColumn("Opintopisteet");
            studyPointsColumn.setCellValueFactory(new PropertyValueFactory<>("studyPoints"));
            table.getColumns().add(studyPointsColumn);

            TableColumn teacherColumn = new TableColumn("Opettaja");
            teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
            table.getColumns().add(teacherColumn);

            TableColumn actionCol = new TableColumn("");
            CourseDao courseDao = this.courseDao;
            User currentUser = this.currentUser;
            Callback<TableColumn<Course, Integer>, TableCell<Course, Integer>> cellFactory
                    = (final TableColumn<Course, Integer> param) -> {
                        final TableCell<Course, Integer> cell = new TableCell<Course, Integer>() {
                    final Button btn = new Button();

                    @Override
                    public void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        btn.setMaxSize(100, 35);
                        btn.setFont(new Font("System", 9));
                        setText(null);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Course course = getTableView().getItems().get(getIndex());
                            if (course.getParticipants().contains(currentUser)) {
                                btn.setText("Poistu");
                                btn.setOnAction(e -> {
                                    courseDao.deleteParticipation(currentUser.getId(), course.getId());
                                    table.getItems().clear();
                                    courseDao.getAll().forEach(c -> {
                                        if (c.getParticipants().contains(currentUser)) {
                                            table.getItems().add(c);
                                        }
                                    });
                                });
                            }
                            setGraphic(btn);
                        }
                    }
                };
                        return cell;
                    };
            actionCol.setCellFactory(cellFactory);
            table.getColumns().add(actionCol);
            stack.getChildren().add(table);

            ArrayList<Course> courses = this.courseDao.getAll();
            courses.forEach(course -> {
                if (course.getParticipants().contains(this.currentUser)) {
                    table.getItems().add(course);
                }
            });

        }

        return stack;
    }

    private VBox getNewCourseView() {
        VBox stack = new VBox();
        stack.setSpacing(5);
        stack.setPadding(new Insets(10, 10, 10, 10));
        Label otsikko = new Label("Kurssit");
        otsikko.setFont(new Font("System", 24));
        stack.getChildren().add(otsikko);

        stack.getChildren().add(new Label("Kurssin nimi"));
        TextField courseName = new TextField();
        stack.getChildren().add(courseName);

        stack.getChildren().add(new Label("Kurssin tunnus"));
        TextField courseIdentifier = new TextField();
        stack.getChildren().add(courseIdentifier);

        stack.getChildren().add(new Label("Kurssin kesto periodeina"));
        ObservableList<Integer> duration = FXCollections.observableArrayList(1, 2);
        ComboBox durationBox = new ComboBox(duration);
        durationBox.setMaxWidth(10000);
        stack.getChildren().add(durationBox);

        stack.getChildren().add(new Label("Kurssin opintopisteet"));
        ObservableList<Integer> studyPoints = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ComboBox studyPointsBox = new ComboBox(studyPoints);
        studyPointsBox.setMaxWidth(10000);
        stack.getChildren().add(studyPointsBox);
        studyPointsBox.setPromptText("Valitse opintopisteiden määrä");

        stack.getChildren().add(new Label("Opettaja"));
        ObservableList<User> options = FXCollections.observableArrayList();
        ArrayList<User> teachers = this.userDao.getTeachers();

        if (this.currentUser.isTeacher()) {
            options.add(currentUser);
        } else {
            for (User teacher : teachers) {
                options.add(teacher);
            }
        }

        ComboBox comboBox = new ComboBox(options);
        comboBox.setPromptText("Valitse opettaja");
        comboBox.setMaxWidth(10000);

        stack.getChildren().add(comboBox);

        Button btn = new Button("Luo");

        btn.setOnAction((ActionEvent e) -> {
            Integer studyPointsValue = (Integer) studyPointsBox.getValue();
            User teacher = (User) comboBox.getValue();
            Integer durationValue = (Integer) durationBox.getValue();
            if (studyPointsValue != null && teacher != null && durationValue != null && !courseName.getText().equals("") && !courseIdentifier.getText().equals("")) {
                Course course = new Course(courseName.getText(), courseIdentifier.getText(), studyPointsValue, durationValue, teacher);
                if (this.courseDao.add(course)) {
                    this.leftMenu.getSelectionModel().select(this.leftMenu.getSelectionModel().getSelectedItem().getParent());
                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Virhe");
                    alert.setHeaderText(null);
                    alert.setContentText("Samalla nimellä tai tunnuksella on jo kurssi!");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Virhe");
                alert.setHeaderText(null);
                alert.setContentText("Kaikki kentät tulee olla täytetty!");
                alert.showAndWait();
            }
        });

        stack.getChildren().add(btn);
        return stack;
    }

    private VBox getNewStudentView() {
        VBox stack = new VBox();
        stack.setSpacing(5);
        stack.setPadding(new Insets(10, 10, 10, 10));
        Label otsikko = new Label("Uusi opiskelija");
        otsikko.setFont(new Font("System", 24));
        stack.getChildren().add(otsikko);

        stack.getChildren().add(new Label("Etunimi"));
        TextField firstNameField = new TextField();
        stack.getChildren().add(firstNameField);

        stack.getChildren().add(new Label("Sukunimi"));
        TextField lastNameField = new TextField();
        stack.getChildren().add(lastNameField);

        stack.getChildren().add(new Label("Sähköposti"));
        TextField emailField = new TextField();
        stack.getChildren().add(emailField);

        stack.getChildren().add(new Label("Salasana"));
        TextField pwField = new PasswordField();
        stack.getChildren().add(pwField);

        Button btn = new Button("Luo");

        btn.setOnAction((ActionEvent e) -> {
            if (firstNameField.getText().equals("") || lastNameField.getText().equals("") || emailField.getText().equals("") || pwField.getText().equals("")) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Virhe");
                alert.setHeaderText(null);
                alert.setContentText("Kaikki kentät tulee olla täytetty!");
                alert.showAndWait();
            } else {
                User student = new User(firstNameField.getText(), lastNameField.getText(), emailField.getText());
                student.setPassword(pwField.getText());
                this.userDao.create(student);
                this.userDao.setStudent(student.getId());
                this.leftMenu.getSelectionModel().select(this.leftMenu.getSelectionModel().getSelectedItem().getParent());
            }
        });

        stack.getChildren().add(btn);
        return stack;
    }

    private VBox getNewTeacherView() {
        VBox stack = new VBox();
        stack.setSpacing(5);
        stack.setPadding(new Insets(10, 10, 10, 10));
        Label otsikko = new Label("Uusi opettaja");
        otsikko.setFont(new Font("System", 24));
        stack.getChildren().add(otsikko);

        stack.getChildren().add(new Label("Etunimi"));
        TextField firstNameField = new TextField();
        stack.getChildren().add(firstNameField);

        stack.getChildren().add(new Label("Sukunimi"));
        TextField lastNameField = new TextField();
        stack.getChildren().add(lastNameField);

        stack.getChildren().add(new Label("Sähköposti"));
        TextField emailField = new TextField();
        stack.getChildren().add(emailField);

        stack.getChildren().add(new Label("Salasana"));
        TextField pwField = new PasswordField();
        stack.getChildren().add(pwField);

        Button btn = new Button("Luo");

        btn.setOnAction((ActionEvent e) -> {
            if (firstNameField.getText().equals("") || lastNameField.getText().equals("") || emailField.getText().equals("") || pwField.getText().equals("")) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Virhe");
                alert.setHeaderText(null);
                alert.setContentText("Kaikki kentät tulee olla täytetty!");
                alert.showAndWait();
            } else {
                User teacher = new User(firstNameField.getText(), lastNameField.getText(), emailField.getText());
                teacher.setPassword(pwField.getText());
                this.userDao.create(teacher);
                this.userDao.setTeacher(teacher.getId());
                this.leftMenu.getSelectionModel().select(this.leftMenu.getSelectionModel().getSelectedItem().getParent());
            }
        });

        stack.getChildren().add(btn);
        return stack;
    }

    private VBox getTeachersView() {
        VBox stack = new VBox();
        stack.setSpacing(5);
        stack.setPadding(new Insets(10, 10, 10, 10));
        Label otsikko = new Label("Opettajat");
        otsikko.setFont(new Font("System", 24));
        stack.getChildren().add(otsikko);
        TableView table = new TableView();

        TableColumn idColumn = new TableColumn("id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        table.getColumns().add(idColumn);

        TableColumn firstName = new TableColumn("Etunimi");
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        table.getColumns().add(firstName);

        TableColumn lastName = new TableColumn("Sukunimi");
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        table.getColumns().add(lastName);

        TableColumn email = new TableColumn("Sähköposti");
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        table.getColumns().add(email);

        stack.getChildren().add(table);

        ArrayList<User> teachers = this.userDao.getTeachers();

        teachers.forEach(teacher -> {
            table.getItems().add(teacher);
        });

        return stack;
    }

    private VBox getStudentsView() {
        VBox stack = new VBox();
        stack.setSpacing(5);
        stack.setPadding(new Insets(10, 10, 10, 10));
        Label otsikko = new Label("Opiskelijat");
        otsikko.setFont(new Font("System", 24));
        stack.getChildren().add(otsikko);
        TableView table = new TableView();

        TableColumn idColumn = new TableColumn("id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        table.getColumns().add(idColumn);

        TableColumn firstName = new TableColumn("Etunimi");
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        table.getColumns().add(firstName);

        TableColumn lastName = new TableColumn("Sukunimi");
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        table.getColumns().add(lastName);

        TableColumn email = new TableColumn("Sähköposti");
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        table.getColumns().add(email);

        stack.getChildren().add(table);

        ArrayList<User> students = this.userDao.getStudents();

        students.forEach(student -> {
            table.getItems().add(student);
        });

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
        identifierColumn.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        table.getColumns().add(identifierColumn);

        TableColumn participantCountColumn = new TableColumn("osallistujia");
        participantCountColumn.setCellValueFactory(new PropertyValueFactory<>("participantsCount"));
        table.getColumns().add(participantCountColumn);

        TableColumn studyPointsColumn = new TableColumn("Opintopisteet");
        studyPointsColumn.setCellValueFactory(new PropertyValueFactory<>("studyPoints"));
        table.getColumns().add(studyPointsColumn);

        TableColumn teacherColumn = new TableColumn("Opettaja");
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
        table.getColumns().add(teacherColumn);

        if (this.currentUser.isStudent()) {
            TableColumn actionCol = new TableColumn("");
            CourseDao courseDao = this.courseDao;
            User currentUser = this.currentUser;
            Callback<TableColumn<Course, Integer>, TableCell<Course, Integer>> cellFactory
                    = (final TableColumn<Course, Integer> param) -> {
                        final TableCell<Course, Integer> cell = new TableCell<Course, Integer>() {
                    final Button btn = new Button();

                    @Override
                    public void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        btn.setMaxSize(100, 35);
                        btn.setFont(new Font("System", 9));
                        setText(null);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Course course = getTableView().getItems().get(getIndex());
                            if (course.getParticipants().contains(currentUser)) {
                                btn.setText("Poistu");
                                btn.setOnAction(e -> {
                                    courseDao.deleteParticipation(currentUser.getId(), course.getId());
                                    table.getItems().clear();
                                    courseDao.getAll().forEach(c -> {
                                        table.getItems().add(c);
                                    });
                                });
                            } else {
                                btn.setText("Osallistu");
                                btn.setOnAction(e -> {
                                    courseDao.addParticipation(currentUser.getId(), course.getId());
                                    table.getItems().clear();
                                    courseDao.getAll().forEach(c -> {
                                        table.getItems().add(c);
                                    });
                                });
                            }

                            setGraphic(btn);
                        }
                    }
                };
                        return cell;
                    };
            actionCol.setCellFactory(cellFactory);
            table.getColumns().add(actionCol);
        }

        stack.getChildren().add(table);

        ArrayList<Course> courses = this.courseDao.getAll();
        courses.forEach(course -> {
            table.getItems().add(course);
        });

        return stack;
    }

    private TreeView getLeftMenu() {
        TreeItem<String> rootItem = new TreeItem<>("Inbox");
        TreeView<String> tree = new TreeView<>(rootItem);
        rootItem.getChildren().add(new TreeItem<>("Omat tiedot"));
        TreeItem kurssitItem = new TreeItem<>("Kurssit");
        rootItem.getChildren().add(kurssitItem);
        if (this.currentUser.isPrincipal() || this.currentUser.isTeacher()) {
            TreeItem opiskelijatItem = new TreeItem<>("Opiskelijat");
            rootItem.getChildren().add(opiskelijatItem);
            opiskelijatItem.getChildren().add(new TreeItem<>("Uusi opiskelija"));
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

        Label title = new Label(this.schoolDao.getSchoolName());
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
