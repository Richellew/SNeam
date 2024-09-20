package main;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.SNeam_DatabaseManager;

public class RegisterPage_SNeam extends Application {
	private Scene registerScene;
	
	private BorderPane bp_Register;
	private GridPane gp_TitleRegister, gp_Register, gp_ButtonRegister;
	
	private Label lbl_TitleRegister, lbl_Register, lbl_Username, lbl_Email, lbl_Password, lbl_ConfirmPassword, lbl_PhoneNumber;
		private TextField tf_RegisterEmail, tf_RegisterUsername, tf_RegisterPhoneNumber;
			private PasswordField pf_Password, pf_ConfirmPassword;
	
	private Menu menu;
	private MenuBar menu_Register;
	private MenuItem registerMenu, loginMenu;
	
	private Button btn_SignUp;
	
	private Alert alert;
	
	private SNeam_DatabaseManager connect = SNeam_DatabaseManager.getInstance();
	
	private int userIndex = 0; 
	
	void initialize_Menu () {
		bp_Register = new BorderPane ();

		gp_Register = new GridPane ();
		gp_TitleRegister = new GridPane ();
		gp_ButtonRegister = new GridPane ();
		
		lbl_TitleRegister = new Label ("REGISTER");
		lbl_Username = new Label ("Username");
		lbl_Email = new Label ("Email");
		lbl_Password = new Label ("Password");
		lbl_ConfirmPassword = new Label ("Confirm Password");
		lbl_PhoneNumber = new Label ("Phone Number");
			tf_RegisterEmail = new TextField ();
			tf_RegisterUsername = new TextField ();
			tf_RegisterPhoneNumber = new TextField ();
				pf_Password = new PasswordField ();
				pf_ConfirmPassword = new PasswordField ();
		
		menu = new Menu ("Menu");
		menu_Register = new MenuBar (menu);
		loginMenu = new MenuItem ("Login");
		registerMenu = new MenuItem ("Register");
				
		btn_SignUp = new Button ("Sign Up");
	}
	
	void layout_Menu () {
// GridPane Layout
	gp_Register.setPadding(new Insets(10));
	gp_Register.setHgap(10);
	gp_Register.setVgap(10);
	gp_Register.setAlignment(Pos.CENTER);
	
// MenuBar
	menu.getItems().add(loginMenu);
	menu.getItems().add(registerMenu);
	
// Title Label Layout
	gp_TitleRegister.setPadding(new Insets(10));
	gp_TitleRegister.setHgap(10);
	gp_TitleRegister.setVgap(10);
	gp_TitleRegister.setAlignment(Pos.CENTER);
	
	lbl_TitleRegister.setFont(Font.font("Arial", FontWeight.BOLD, 40));
	// Title Register
		gp_Register.add(gp_TitleRegister, 0, 0, 1, 1);
		gp_TitleRegister.add(lbl_TitleRegister, 0, 0);
		
// TextField $ Label Layout
	// UserName
	gp_Register.add(lbl_Username, 0, 1);
	gp_Register.add(tf_RegisterUsername, 0, 2);
		lbl_Username.setPrefSize(220, 20);
		lbl_Username.setMaxWidth(220);
		tf_RegisterUsername.setPrefSize(220, 20);
		tf_RegisterUsername.setMaxWidth(220);
	
	// Email
	gp_Register.add(lbl_Email, 0, 3);
	gp_Register.add(tf_RegisterEmail, 0, 4);
		lbl_Email.setPrefSize(220, 20);
		lbl_Email.setMaxWidth(220);
		tf_RegisterEmail.setPrefSize(220, 20);
		tf_RegisterEmail.setMaxWidth(220);
		
	// Password
	gp_Register.add(lbl_Password, 0, 5);
	gp_Register.add(pf_Password, 0, 6);
		lbl_Password.setPrefSize(220, 20);
		lbl_Password.setMaxWidth(220);
		pf_Password.setPrefSize(220, 20);
		pf_Password.setMaxWidth(220);
			
	// Confirm Password
	gp_Register.add(lbl_ConfirmPassword, 0, 7);
	gp_Register.add(pf_ConfirmPassword, 0, 8);
		lbl_ConfirmPassword.setPrefSize(220, 20);
		lbl_ConfirmPassword.setMaxWidth(220);
		pf_ConfirmPassword.setPrefSize(220, 20);
		pf_ConfirmPassword.setMaxWidth(220);
			
	// Phone Number
	gp_Register.add(lbl_PhoneNumber, 0, 9);
	gp_Register.add(tf_RegisterPhoneNumber, 0, 10);
		lbl_PhoneNumber.setPrefSize(220, 20);
		lbl_PhoneNumber.setMaxWidth(220);
		tf_RegisterPhoneNumber.setPrefSize(220, 20);
		tf_RegisterPhoneNumber.setMaxWidth(220);
		
// Button Layout
	gp_ButtonRegister.setPadding(new Insets(10));
	gp_ButtonRegister.setHgap(10);
	gp_ButtonRegister.setVgap(10);
	gp_ButtonRegister.setAlignment(Pos.CENTER);
	
	gp_Register.add(gp_ButtonRegister, 0, 11, 1, 1);
	gp_ButtonRegister.add(btn_SignUp, 0, 0);
		btn_SignUp.setPrefSize(60, 30);
		btn_SignUp.setMaxWidth(60);
	}

private void eventHandler (Stage RegisterStage) {
	alert = new Alert (AlertType.WARNING);
	alert.setTitle("Invalid Input");
	
	loginMenu.setOnAction(e -> {
		LoginPage_SNeam loginPage = new LoginPage_SNeam ();
		try {
			loginPage.start(new Stage());
			RegisterStage.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	});
	
	btn_SignUp.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent arg0) {
	        String username = tf_RegisterUsername.getText();
	        String email = tf_RegisterEmail.getText();
	        String password = pf_Password.getText();
	        String confirmPassword = pf_ConfirmPassword.getText();
	        String phoneNumber = tf_RegisterPhoneNumber.getText();

	        if (username.length() < 4 || username.length() > 20) {
	            alert.setHeaderText("Username is invalid");
	            alert.setContentText("Username must contain 4 – 20 characters");
	            alert.show();
	            return;
	        } else if (!email.contains("@")) {
	            alert.setHeaderText("Email is invalid");
	            alert.setContentText("Email must contain '@' in it");
	            alert.show();
	            return;
	        } else if (password.length() < 6 || password.length() > 20) {
	            alert.setHeaderText("Password is invalid");
	            alert.setContentText("Password must contain 6 - 20 Characters");
	            alert.show();
	            return;
	        } else if (!AlphaNumericValidation(password)) {
	            alert.setHeaderText("Password is invalid");
	            alert.setContentText("Password must be alphanumeric");
	            alert.show();
	            return;
	        } else if (!password.equals(confirmPassword)) {
	            alert.setHeaderText("Password does not match");
	            alert.setContentText("Confirm Password must be the same as Password");
	            alert.show();
	            return;
	        } else if (!phoneNumber.matches("\\d{9,20}")) {
	            alert.setHeaderText("Phone number is invalid");
	            alert.setContentText("Phone Number must be numeric and 9 - 20 numbers");
	            alert.show();
	            return;
	        } else {
	        	   String indexQuery = "SELECT MAX(CAST(SUBSTRING(UserID, 3) AS UNSIGNED)) FROM user";
	               ResultSet resultSet = connect.execQuery(indexQuery);

	               try {
	                   if (resultSet.next()) {
	                       int highestIndex = resultSet.getInt(1);
	                       
	                       userIndex = highestIndex + 1;
	                       
	                       String formattedIndex = String.format("%03d", userIndex);

	                       String userID = "AC" + formattedIndex;

	                       String query = String.format(
	                               "INSERT INTO user " +
	                                       "(UserID, username, email, password, phoneNumber, role) " +
	                                       "VALUES ('%s', '%s','%s','%s','%s','%s')",
	                               userID, username, email, password, phoneNumber, "customer");

	                       connect.execUpdate(query);

	                       Alert alertSuccess = new Alert(AlertType.INFORMATION, "Registration successful!");
	                       alertSuccess.showAndWait();
	                     
	                   }
	               } catch (SQLException e) {
	                   e.printStackTrace();
	               }
	        }
	    }
	       });
	
	}

private boolean AlphaNumericValidation (String validation) {
	return validation.matches("[a-zA-Z0-9]+");
}


	@Override
	public void start(Stage RegisterStage) throws Exception {
		initialize_Menu ();
		layout_Menu();
		eventHandler(RegisterStage);
		
		bp_Register.setTop(menu_Register);
		bp_Register.setCenter(gp_Register);
		
		registerScene = new Scene (bp_Register, 960, 540);
		
		RegisterStage.setTitle("SNeam");
		RegisterStage.setScene(registerScene);
		RegisterStage.setResizable(false);
		RegisterStage.show();
	}
	
	public static void main(String[] args) {
		launch (args);
	}

}