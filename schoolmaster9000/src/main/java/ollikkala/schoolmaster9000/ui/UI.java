/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ollikkala.schoolmaster9000.ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Optional;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import ollikkala.schoolmaster9000.dao.CourseDao;
import ollikkala.schoolmaster9000.dao.SchoolDao;
import ollikkala.schoolmaster9000.dao.UserDao;
import ollikkala.schoolmaster9000.domain.Completion;
import ollikkala.schoolmaster9000.domain.Course;
import ollikkala.schoolmaster9000.domain.CourseService;
import ollikkala.schoolmaster9000.domain.Student;
import ollikkala.schoolmaster9000.domain.Teacher;
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
    private UserService userService;
    private CourseService courseService;
    private Stage stage;
    private TreeView<String> leftMenu;
    private VBox rightView;

    @Override
    public void init() throws Exception {
        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        this.courseDao = new CourseDao(this.connection);
        this.schoolDao = new SchoolDao(this.connection);
        this.userDao = new UserDao(this.connection);
        this.userService = new UserService(this.userDao, this.courseDao);
        this.courseService = new CourseService(this.courseDao, this.userDao);
        this.userService.setCourseService(this.courseService);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Scene scene;
        if (this.schoolDao.getSchoolName().equals("")) scene = new Scene(this.getCreateNewScoolView(), 640, 480);
        else scene = new Scene(this.getLoginScene(), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    private Scene getLoggedInScene() {
        SplitPane sp = new SplitPane();
        this.leftMenu = this.getLeftMenu();
        this.rightView = new VBox();
        HBox leftControl = new HBox(this.leftMenu);
        HBox.setHgrow(leftControl, Priority.ALWAYS);
        leftControl.setMaxWidth(250);
        leftControl.setMinWidth(200);
        sp.getItems().addAll(leftControl, this.rightView);
        this.leftMenu.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        switch (newValue.getValue()) {
                            case "Omat tiedot":
                                this.setRightViewTo(this.getEditPersonalInformationView());
                                break;
                            case "Kurssit":
                                this.setRightViewTo(this.getCoursesView());
                                break;
                            case "Uusi kurssi":
                                this.setRightViewTo(this.getNewCourseView());
                                break;
                            case "Uusi opettaja":
                                this.setRightViewTo(this.getNewTeacherView());
                                break;
                            case "Opettajat":
                                this.setRightViewTo(this.getTeachersView());
                                break;
                            case "Opiskelijat":
                                this.setRightViewTo(this.getStudentsView());
                                break;
                            case "Uusi opiskelija":
                                this.setRightViewTo(this.getNewStudentView());
                                break;
                            default:
                                break;
                        }
                    }
                });
        this.leftMenu.getSelectionModel().select(this.leftMenu.getRoot().getChildren().get(0));
        return new Scene(sp, 960, 720);
    }

    private void setRightViewTo(Node view) {
        this.rightView.getChildren().clear();
        this.rightView.getChildren().add(view);
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
                this.currentUser = this.userService.createPrincipal(firstNameField.getText(), lastNameField.getText(), emailField.getText(), passwordField.getText());
                this.stage.setScene(this.getLoggedInScene());
            }
        });

        stack.getChildren().add(loginButton);
        bp.setCenter(stack);
        return bp;
    }

    private Label makeTitle(String titleString) {
        Label title = new Label(titleString);
        title.setFont(new Font("System", 24));
        return title;
    }

    private VBox makeTitledStack(String titleString) {
        VBox stack = new VBox();
        stack.setSpacing(5);
        stack.setPadding(new Insets(10, 10, 10, 10));
        Label title = this.makeTitle(titleString);
        stack.getChildren().add(title);
        return stack;
    }

    private TextField addLabeledTextField(VBox target, String defaultValue, String labelString) {
        target.getChildren().add(new Label(labelString));
        TextField field = new TextField(defaultValue);
        target.getChildren().add(field);
        return field;
    }

    private VBox getEditPersonalInformationView() {
        VBox stack = this.makeTitledStack("Omat tiedot");

        TextField firstName = this.addLabeledTextField(stack, String.valueOf(this.currentUser.getFirstName()), "Etunimi");
        firstName.setEditable(false);

        TextField lastName = this.addLabeledTextField(stack, String.valueOf(this.currentUser.getLastName()), "Sukunimi");
        lastName.setEditable(false);

        TextField emailField = this.addLabeledTextField(stack, String.valueOf(this.currentUser.getEmail()), "Sähköposti");
        emailField.setEditable(false);

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
                                    courseDao.getAllCourses().forEach(c -> {
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

            ArrayList<Course> courses = this.courseDao.getAllCourses();
            courses.forEach(course -> {
                if (course.getParticipants().contains(this.currentUser)) {
                    table.getItems().add(course);
                }
            });

        }

        return stack;
    }

    private Dialog<Pair<Integer, String>> getCourseCompletionDialog() {
        Dialog<Pair<Integer, String>> dialog = new Dialog<>();
        dialog.setTitle("Anna suorituksen tiedot. Molemmat tulee täyttää!");
        VBox stack = new VBox();

        stack.getChildren().add(new Label("Arvosana"));
        ObservableList<Integer> studyPoints = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        ComboBox gradeBox = new ComboBox(studyPoints);
        gradeBox.setMaxWidth(10000);
        stack.getChildren().add(gradeBox);

        stack.getChildren().add(new Label("Kommentti"));
        TextField comment = new TextField();
        stack.getChildren().add(comment);

        ButtonType loginButtonType = new ButtonType("Tallenna", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            return new Pair<>((Integer) gradeBox.getValue(), comment.getText());
        });
        dialog.getDialogPane().setContent(stack);
        return dialog;
    }

    private VBox getCoursePage(int courseId) {
        Course course = this.courseService.getCourseById(courseId);
        VBox stack = this.makeTitledStack(course.getName());
        stack.getChildren().add(new Label("Opettaja: " + course.getTeacherName()));
        stack.getChildren().add(new Label("Opintopisteitä tarjolla: " + course.getStudyPoints()));
        stack.getChildren().add(new Label("Osallistujia kurssilla: " + course.getParticipantsCount()));
        stack.getChildren().add(new Label("Kurssin suorittanut: " + course.getCompletions().size()));

        Label myCoursesTitle = new Label("Osallistujat");
        myCoursesTitle.setFont(new Font("System", 18));
        stack.getChildren().add(myCoursesTitle);

        TableView participantTable = new TableView();

        participantTable.setFixedCellSize(25);
        participantTable.prefHeightProperty().bind(Bindings.size(participantTable.getItems()).multiply(participantTable.getFixedCellSize()).add(30));

        TableColumn firstName = new TableColumn("Etunimi");
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        participantTable.getColumns().add(firstName);

        TableColumn lastName = new TableColumn("Sukunimi");
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        participantTable.getColumns().add(lastName);

        TableColumn email = new TableColumn("Sähköposti");
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        participantTable.getColumns().add(email);

        if (course.getTeacher().equals(this.currentUser) || this.currentUser.isPrincipal()) {
            TableColumn actionCol = new TableColumn("");
            CourseDao courseDao = this.courseDao;
            User currentUser = this.currentUser;
            UI _this = this;
            Callback<TableColumn<User, Integer>, TableCell<User, Integer>> cellFactory
                    = (final TableColumn<User, Integer> param) -> {
                        final TableCell<User, Integer> cell = new TableCell<User, Integer>() {
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
                            User user = getTableView().getItems().get(getIndex());
                            btn.setText("Merkitse suoritetuksi");
                            btn.setOnAction(e -> {
                                Dialog<Pair<Integer, String>> dialog = _this.getCourseCompletionDialog();
                                Optional<Pair<Integer, String>> result = dialog.showAndWait();
                                result.ifPresent(data -> {
                                    if (data.getKey() != null && data.getValue() != null) {
                                        _this.courseDao.makeParticipationCompleted(course.getId(), user.getId(), data.getKey(), data.getValue());
                                        _this.setRightViewTo(_this.getCoursePage(courseId));
                                    }
                                });
                            });
                            setGraphic(btn);
                        }
                    }
                };
                        return cell;
                    };
            actionCol.setCellFactory(cellFactory);
            participantTable.getColumns().add(actionCol);
        }

        stack.getChildren().add(participantTable);

        ArrayList<User> students = course.getParticipants();

        students.forEach(student -> {
            participantTable.getItems().add(student);
        });

        Label completersTitle = new Label("Suoritukset");
        completersTitle.setFont(new Font("System", 18));
        stack.getChildren().add(completersTitle);

        TableView completionsTable = new TableView();

        completionsTable.setFixedCellSize(25);
        completionsTable.prefHeightProperty().bind(Bindings.size(completionsTable.getItems()).multiply(completionsTable.getFixedCellSize()).add(30));

        TableColumn studentName = new TableColumn("Opiskelija");
        studentName.setCellValueFactory(new PropertyValueFactory<>("student"));
        completionsTable.getColumns().add(studentName);

        TableColumn grade = new TableColumn("Arvosana");
        grade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        completionsTable.getColumns().add(grade);

        TableColumn comment = new TableColumn("Kommentti");
        comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        completionsTable.getColumns().add(comment);

        stack.getChildren().add(completionsTable);

        ArrayList<Completion> completions = course.getCompletions();

        completions.forEach(completion -> {
            completionsTable.getItems().add(completion);
        });

        return stack;
    }

    private VBox getStudentPage(int id) {
        Student student = this.userService.getStudentById(id);
        VBox stack = this.makeTitledStack(student.getFirstName() + " " + student.getLastName());
        stack.getChildren().add(new Label(student.getEmail()));
        stack.getChildren().add(new Label("Opintopisteet: " + student.getStudyPoints()));

        Label participationsTitle = new Label("Osallistumiset");
        participationsTitle.setFont(new Font("System", 18));
        stack.getChildren().add(participationsTitle);

        TableView participationsTable = new TableView();

        participationsTable.setFixedCellSize(25);
        participationsTable.prefHeightProperty().bind(Bindings.size(participationsTable.getItems()).multiply(participationsTable.getFixedCellSize()).add(30));

        TableColumn courseCol = new TableColumn("Kurssi");
        courseCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        participationsTable.getColumns().add(courseCol);

        student.getParticipations().forEach(participation -> {
            participationsTable.getItems().add(participation);
        });

        stack.getChildren().add(participationsTable);

        Label completionsTitle = new Label("Suoritukset");
        completionsTitle.setFont(new Font("System", 18));
        stack.getChildren().add(completionsTitle);

        TableView completionsTable = new TableView();

        completionsTable.setFixedCellSize(25);
        completionsTable.prefHeightProperty().bind(Bindings.size(completionsTable.getItems()).multiply(completionsTable.getFixedCellSize()).add(30));

        TableColumn courseColumn = new TableColumn("Kurssi");
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        completionsTable.getColumns().add(courseColumn);

        TableColumn grade = new TableColumn("Arvosana");
        grade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        completionsTable.getColumns().add(grade);

        TableColumn comment = new TableColumn("Kommentti");
        comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        completionsTable.getColumns().add(comment);

        student.getCompletions().forEach(completion -> {
            completionsTable.getItems().add(completion);
        });

        stack.getChildren().add(completionsTable);

        return stack;
    }

    private VBox getTeacherPage(int id) {
        Teacher teacher = this.userService.getTeacherById(id);
        VBox stack = this.makeTitledStack(teacher.getFirstName() + " " + teacher.getLastName());
        stack.getChildren().add(new Label(teacher.getEmail()));

        Label teachingsTitle = new Label("Opettaa kursseilla");
        teachingsTitle.setFont(new Font("System", 18));
        stack.getChildren().add(teachingsTitle);

        TableView coursesTable = new TableView();

        coursesTable.setFixedCellSize(25);
        coursesTable.prefHeightProperty().bind(Bindings.size(coursesTable.getItems()).multiply(coursesTable.getFixedCellSize()).add(30));

        TableColumn courseCol = new TableColumn("Kurssi");
        courseCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        coursesTable.getColumns().add(courseCol);

        teacher.getCourses().forEach(participation -> {
            coursesTable.getItems().add(participation);
        });

        stack.getChildren().add(coursesTable);
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
                Course course = this.courseService.create(courseName.getText(), courseIdentifier.getText(), studyPointsValue, durationValue, teacher.getId());
                if (course != null) {
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
                this.userService.createStudent(firstNameField.getText(), lastNameField.getText(), emailField.getText(), pwField.getText());
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
                User teacher = this.userService.createTeacher(firstNameField.getText(), lastNameField.getText(), emailField.getText(), pwField.getText());
                if (teacher != null) {
                    this.leftMenu.getSelectionModel().select(this.leftMenu.getSelectionModel().getSelectedItem().getParent());
                }

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

        stack.getChildren().add(new Label("Klikkaamalla riviä pääset tarkastelemaan opettajan tietoja"));
        
        TableColumn firstName = new TableColumn("Etunimi");
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        table.getColumns().add(firstName);

        TableColumn lastName = new TableColumn("Sukunimi");
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        table.getColumns().add(lastName);

        TableColumn email = new TableColumn("Sähköposti");
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        table.getColumns().add(email);

        table.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.getItem() != null) {
                    this.leftMenu.getSelectionModel().clearSelection();
                    this.setRightViewTo(this.getTeacherPage(row.getItem().getId()));
                }
            });
            return row;
        });

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

        stack.getChildren().add(new Label("Klikkaamalla riviä pääset tarkastelemaan opiskelijan tietoja"));
        
        TableColumn firstName = new TableColumn("Etunimi");
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        table.getColumns().add(firstName);

        TableColumn lastName = new TableColumn("Sukunimi");
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        table.getColumns().add(lastName);

        TableColumn email = new TableColumn("Sähköposti");
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        table.getColumns().add(email);

        table.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.getItem() != null) {
                    this.leftMenu.getSelectionModel().clearSelection();
                    this.setRightViewTo(this.getStudentPage(row.getItem().getId()));
                }
            });
            return row;
        });

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

        stack.getChildren().add(new Label("Klikkaamalla kurssin riviä pääset kurssisivulle"));

        TableColumn nameColumn = new TableColumn("Nimi");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().add(nameColumn);

        TableColumn identifierColumn = new TableColumn("Tunnus");
        identifierColumn.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        table.getColumns().add(identifierColumn);

        TableColumn participantCountColumn = new TableColumn("Osallistujia");
        participantCountColumn.setCellValueFactory(new PropertyValueFactory<>("participantsCount"));
        table.getColumns().add(participantCountColumn);

        TableColumn completionsCountColumn = new TableColumn("Suorittaneita");
        completionsCountColumn.setCellValueFactory(new PropertyValueFactory<>("completionsCount"));
        table.getColumns().add(completionsCountColumn);

        TableColumn teacherColumn = new TableColumn("Opettaja");
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacherName"));
        table.getColumns().add(teacherColumn);

        if (this.currentUser.isStudent()) {
            TableColumn actionCol = new TableColumn("");
            CourseDao courseDao = this.courseDao;
            User currentUser = this.currentUser;
            UI _this = this;
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
                            for (Completion completion : course.getCompletions()) {
                                if (completion.getStudent().equals(_this.currentUser)) {
                                    setText("suoritettu");
                                    return;
                                }
                            }
                            if (course.getParticipants().contains(currentUser)) {
                                btn.setText("Poistu");
                                btn.setOnAction(e -> {
                                    courseDao.deleteParticipation(currentUser.getId(), course.getId());
                                    table.getItems().clear();
                                    _this.courseService.getAll().forEach(c -> {
                                        table.getItems().add(c);
                                    });
                                });
                            } else {
                                btn.setText("Osallistu");
                                btn.setOnAction(e -> {
                                    courseDao.createParticipation(currentUser.getId(), course.getId());
                                    table.getItems().clear();
                                    _this.courseService.getAll().forEach(c -> {
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

        table.setRowFactory(tv -> {
            TableRow<Course> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.getItem() != null) {
                    this.leftMenu.getSelectionModel().clearSelection();
                    this.setRightViewTo(this.getCoursePage(row.getItem().getId()));
                }
            });
            return row;
        });

        stack.getChildren().add(table);

        ArrayList<Course> courses = this.courseService.getAll();
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
