package main;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import util.SNeam_DatabaseManager;

public class LoginPage_SNeam extends Application{
	private Scene loginScene;

	private GridPane gp_Login, gp_TitleLogin, gp_ButtonLogin;
	private BorderPane bp_Login;
	
	private Label lbl_TitleLogin, lbl_Username, lbl_Password;
	private TextField tf_Username;
	private PasswordField pf_Password;
	
	private Button btn_Login;
	
	private Menu menu;
	private MenuBar menu_Login;
	private MenuItem registerMenu, loginMenu;
	
	public static String loggedInUsername;
	String loggedInUserID = "YourLoggedUserID";
	
	private SNeam_DatabaseManager connect = SNeam_DatabaseManager.getInstance();
	
	void initialize_Menu () {
		bp_Login = new BorderPane();
		
		gp_Login = new GridPane();
		gp_TitleLogin = new GridPane();
		gp_ButtonLogin = new GridPane ();
		
		lbl_TitleLogin = new Label ("LOGIN");		
		lbl_Username = new Label ("Username");
		lbl_Password = new Label ("Password");
		
		tf_Username = new TextField();
		pf_Password = new PasswordField();
		
		btn_Login = new Button ("Sign In");
		
		menu = new Menu ("Menu");
		menu_Login = new MenuBar (menu);
		loginMenu = new MenuItem ("Login");
		registerMenu = new MenuItem ("Register");
	}
	
	void layout_Menu () {
// GridPane Layout
	gp_Login.setPadding(new Insets(10));
	gp_Login.setHgap(10);
	gp_Login.setVgap(10);
	gp_Login.setAlignment(Pos.CENTER);

// MenuBar
	menu_Login = new MenuBar (menu);
	menu.getItems().add(loginMenu);
	menu.getItems().add(registerMenu);
	
// Title Label Layout
	gp_TitleLogin.setPadding(new Insets(10));
	gp_TitleLogin.setHgap(10);
	gp_TitleLogin.setVgap(10);
	gp_TitleLogin.setAlignment(Pos.CENTER);
	
	lbl_TitleLogin.setFont(Font.font("Arial", FontWeight.BOLD,54));
	// Title Login
		gp_Login.add(gp_TitleLogin, 0, 0, 1, 1);
		gp_TitleLogin.add(lbl_TitleLogin, 0, 0);

// TextField & Label Layout
	// UserName
	gp_Login.add(lbl_Username, 0, 1);
	gp_Login.add(tf_Username, 0, 2);
		tf_Username.setPrefSize(220, 20);
		tf_Username.setMaxWidth(220);
	
	// Password
	gp_Login.add(lbl_Password, 0, 3);
	gp_Login.add(pf_Password, 0, 4);
		pf_Password.setPrefSize(220, 20);
		pf_Password.setMaxWidth(220);
	
// Button Layout
	gp_ButtonLogin.setPadding(new Insets(10));
	gp_ButtonLogin.setHgap(10);
	gp_ButtonLogin.setVgap(10);
	gp_ButtonLogin.setAlignment(Pos.CENTER);
	
	gp_Login.add(gp_ButtonLogin, 0, 5, 1, 1);
	gp_ButtonLogin.add(btn_Login, 0, 0);
		btn_Login.setPrefSize(60, 30);
		btn_Login.setMaxWidth(60);
	}
	
	private void eventHandlerLogin (Stage LoginStage) {
		registerMenu.setOnAction(e -> {
			RegisterPage_SNeam registerPage = new RegisterPage_SNeam ();
			try {
				registerPage.start(new Stage());
				LoginStage.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		
		btn_Login.setOnMouseClicked(event -> {
			String username = tf_Username.getText();
			String password = pf_Password.getText();
			
				String query = "SELECT * FROM User WHERE Username = '" + username + 
						"' AND Password = '" + password + "'";
				
				ResultSet rs = connect.execQuery(query);
			try {
				if (rs.next()) {
					String role = rs.getString("Role");
					loggedInUsername = rs.getString("Username");
					loggedInUserID = rs.getString("UserID");
					
					if ("admin".equals(role)) {
						new HomePageAdmin_SNeam(LoginStage, loggedInUsername, loggedInUserID);
					
					} else if ("customer".equals(role)) {
						new HomePageCustomer_SNeam(LoginStage, loggedInUsername, loggedInUserID);
					}
				} else {
					Alert alert = new Alert (Alert.AlertType.WARNING);
					alert.setTitle("Invalid Request");
					alert.setHeaderText("Wrong Credentials");
					alert.setContentText("Email or password is invalid");
					alert.showAndWait();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void start(Stage LoginStage) throws Exception {
		initialize_Menu ();
		layout_Menu();
		eventHandlerLogin(LoginStage);
		
		bp_Login.setTop(menu_Login);
		bp_Login.setCenter(gp_Login);
		
		loginScene = new Scene (bp_Login, 960, 540);
		
		LoginStage.setTitle("SNeam");
		LoginStage.setScene(loginScene);
		LoginStage.setResizable(false);
		LoginStage.show();
		
	}

	public static void main(String[] args) {
		launch (args);
	}	
}