package main;

import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import modul.Models_Game;
import util.SNeam_DatabaseManager;
import jfxtras.labs.scene.control.window.Window;

import java.sql.ResultSet;
import java.sql.SQLException;

import util.*;

public class CartDetailWindow_Sneam extends Stage {
	private GridPane gp_Window, gp_Title, gp_Button, gp_Price, gp_Spinner;
	
	private Label lbl_GameTitle, lbl_GameDesc, lbl_GamePrice; 
	private Spinner <Integer> sr_quantity;
	
	private Button btn_AddCart;
	
	SNeam_DatabaseManager connect = SNeam_DatabaseManager.getInstance();
	
	private VBox vbox_Layout;
	
	private String selectedGameID;
	private String currentUserID;
	
private void initialize_Window () {
	gp_Title = new GridPane ();
	gp_Window = new GridPane ();
	gp_Button = new GridPane ();
	gp_Price = new GridPane ();
	gp_Spinner = new GridPane ();
	
	lbl_GameTitle = new Label ();
	lbl_GamePrice = new Label ();
	lbl_GameDesc = new Label ();
	
	sr_quantity = new Spinner<>(0, 10, 0);
	
	btn_AddCart = new Button ("Add To Cart");
	
	vbox_Layout = new VBox ();
}

private void layout_Window () {
// Label
	gp_Window.setPadding(new Insets(10));
	gp_Window.setVgap(20);
	gp_Window.setAlignment(Pos.TOP_CENTER);
			gp_Price.setPadding(new Insets(10));
			gp_Price.setVgap(5);
			gp_Price.setAlignment(Pos.CENTER);
	
	gp_Window.add(lbl_GameDesc, 0, 1);
	
	gp_Window.add(gp_Price, 0, 2, 3, 1);
		gp_Price.add(lbl_GamePrice, 0, 0);
		lbl_GameDesc.setWrapText(true);
		// Design
		lbl_GameTitle.setFont(Font.font("Arial", FontWeight.BOLD, 15));
			lbl_GameTitle.setPrefSize(260, 80);
			lbl_GameTitle.setAlignment(Pos.CENTER);
			
		lbl_GameDesc.setFont(Font.font("Arial", 15));
			lbl_GameDesc.setPrefSize(260, 180);
			
		lbl_GamePrice.setFont(Font.font("Arial", 15));
			lbl_GamePrice.setPrefWidth(260);
			lbl_GamePrice.setPrefSize(260, 80);
			lbl_GamePrice.setAlignment(Pos.CENTER);

// GridPane Alignment
	GridPane.setHalignment(lbl_GameDesc, HPos.CENTER);
	GridPane.setHalignment(lbl_GamePrice, HPos.CENTER);

// Spinner Layout
	gp_Spinner.setPadding(new Insets(10));
	gp_Spinner.setAlignment(Pos.CENTER);
	
	sr_quantity.setPrefSize(200, 80);
	
	gp_Window.add(gp_Spinner, 0, 3, 1, 1);
	gp_Spinner.add(sr_quantity, 0, 0);
		
// Button Layout	
	gp_Button.setPadding(new Insets(10));
	gp_Button.setVgap(10);
	gp_Button.setAlignment(Pos.CENTER);
	
	gp_Button.add(btn_AddCart, 0, 0);
	btn_AddCart.setPrefSize(130, 40);
	btn_AddCart.setMaxHeight(50);
		

// GridPane Layout
	gp_Title.setPadding(new Insets(10));
	gp_Window.setVgap(5);
	gp_Title.setAlignment(Pos.TOP_CENTER);
	
	gp_Window.add(gp_Title, 0, 0, 1, 1);
	gp_Title.add(lbl_GameTitle, 0, 0);
	
	vbox_Layout.getChildren().addAll(gp_Window, gp_Button);
}

public void displaySelectedGame(Models_Game selectedGame, String gameID, String userID) {
	lbl_GameTitle.setText(selectedGame.getGameName());
	lbl_GameDesc.setText(selectedGame.getGameDesc());
	lbl_GamePrice.setText("Rp" + String.valueOf(selectedGame.getGamePrice()));
	
	this.selectedGameID = gameID;
	this.currentUserID = userID;
	
	String query = String.format("SELECT * FROM cart WHERE UserID = '%s' AND GameID = '%s'", currentUserID, gameID);
	ResultSet rs = connect.execQuery(query);
	
	try {
		if (rs.next()) {
			int quantity = rs.getInt("Quantity");
			sr_quantity.getValueFactory().setValue(quantity);
		} else {
			sr_quantity.getValueFactory().setValue(0);
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
}
	
private void eventHandler () {
	btn_AddCart.setOnAction(e -> {
		if (selectedGameID != null && currentUserID != null ) {
			int quantity = sr_quantity.getValue();
			if (quantity > 0) {
				saveToCart(currentUserID, selectedGameID, quantity);
			}
			close();
		}
	});
	
	sr_quantity.valueProperty().addListener((observable, old_Value, new_Value) -> {
		int quantity = new_Value;	
		if (quantity == 0) {
		
		}
    });
}

private void saveToCart(String userID, String gameID, int quantity) {
    
    SNeam_DatabaseManager connect = SNeam_DatabaseManager.getInstance();
    String query1 = String.format("SELECT * FROM cart WHERE UserID = '%s' AND GameID = '%s'", currentUserID, gameID);
    ResultSet rs = connect.execQuery(query1); 
    boolean checkcart = false;
    
    try {
		while(rs.next()) {
			checkcart = true;
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
    if (checkcart) {
		String queryupdate = String.format("UPDATE cart SET Quantity= Quantity+ %d  WHERE UserID = '%s' AND GameID = '%s'",quantity, currentUserID, gameID);
		connect.execUpdate(queryupdate);
	}	
    else {
		 String query = String.format("INSERT INTO cart (UserID, GameID, Quantity) VALUES ('%s', '%s', %d)", userID, gameID, quantity);
    connect.execUpdate(query);
	}
}

public Window CreateDetailWindow_Sneam() {
		initialize_Window ();
		layout_Window ();
		eventHandler();
		
		Window wd_CartDetail = new Window("Add To Cart");
			wd_CartDetail.getContentPane().getChildren().add(vbox_Layout);
			 vbox_Layout.setStyle("-fx-background-color: white;");
			wd_CartDetail.setPrefSize(260, 350);
			
			sr_quantity.getValueFactory().setValue(0);
		
        Scene window_Scene = new Scene(wd_CartDetail, 260, 350);
        setScene(window_Scene);
        
       return wd_CartDetail;
	}

}