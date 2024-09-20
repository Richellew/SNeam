package main;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
import modul.Models_Game;
import util.SNeam_DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;

import jfxtras.labs.scene.control.window.Window;

import util.*;

public class HomePageCustomer_SNeam implements EventHandler<MouseEvent>{
	private Scene homeCustomerScene;
	
	private GridPane gp_HomeCustomer, gp_HomeTitle, gp_HomePrice, gp_HomeButton, gp_TableView;
	private BorderPane bp_HomeCustomer;
	
	private Label lbl_HomeTitle, lbl_GameTitle, lbl_GameDescription, lbl_GamePrice;
	
	private Menu dashboardMenu, logoutMenu;
	private MenuBar menu_Home;
	private MenuItem cartHome, homeMenu, logout;
	
	private TableView <Models_Game> tv_Game;
	
	private Button btn_AddToChart;
	private CartDetailWindow_Sneam wd_Cart;
	
	private String currentUser;
	
	private String selectedGameID;
	private String currentUserID;
	
	SNeam_DatabaseManager connect = SNeam_DatabaseManager.getInstance();
	
	private boolean display = false;
	private Models_Game displayedGame;
	
	void initialize_Menu () {
	gp_HomeCustomer = new GridPane ();
	gp_HomeTitle = new GridPane ();
	gp_HomePrice = new GridPane ();
	gp_HomeButton = new GridPane ();
	gp_TableView = new GridPane ();
	
	bp_HomeCustomer = new BorderPane ();
	
	lbl_HomeTitle = new Label ();
	lbl_GameTitle = new Label ();
	lbl_GameDescription = new Label ();
	lbl_GamePrice = new Label ();
	
	dashboardMenu = new Menu ("Dashboard");
	logoutMenu = new Menu ("Log Out");
	
		menu_Home = new MenuBar();
			menu_Home.getMenus().addAll(dashboardMenu, logoutMenu);
	cartHome = new MenuItem ("Cart");
	homeMenu = new MenuItem ("Home");
	logout = new MenuItem ("Log Out");
	
	tv_Game = new TableView ();
	wd_Cart = new CartDetailWindow_Sneam();
	
	btn_AddToChart = new Button ("Add To Cart");
	}
	
	void layout_Menu () {
// GridPane Layout
	gp_HomeCustomer.setPadding(new Insets(5));
	gp_HomeCustomer.setHgap(10);
	gp_HomeCustomer.setVgap(3);
	gp_HomeCustomer.setAlignment(Pos.TOP_CENTER);

// MenuBar
	dashboardMenu.getItems().addAll(homeMenu, cartHome);
	logoutMenu.getItems().add(logout);
		menu_Home.setPadding(new Insets(5, 10, 5, 10));

// Title Label Layout
	gp_HomeTitle.setPadding(new Insets(10));
	gp_HomeTitle.setHgap(10);
	gp_HomeTitle.setVgap(10);
	gp_HomeTitle.setAlignment(Pos.CENTER);
	
	lbl_HomeTitle.setFont(Font.font("Arial", FontWeight.BOLD , 30));
	// Title Home
		gp_HomeCustomer.add(gp_HomeTitle, 0, 0, 3, 1);
		gp_HomeTitle.add(lbl_HomeTitle, 0, 0);
	
// Label Description Layout
	// Game Title $ Description
	gp_HomeCustomer.add(lbl_GameTitle, 1, 1, 2, 1);
	gp_HomeCustomer.add(lbl_GameDescription, 1, 2);
		lbl_GameTitle.setPrefSize(220, 60);
//		lbl_GameTitle.setMaxWidth(180);
			lbl_GameTitle.setFont(Font.font("Arial", FontWeight.BOLD , 18));
			lbl_GameTitle.setWrapText(true);
				
			lbl_GameDescription.setFont(new Font ("Arial", 11));
			lbl_GameDescription.setWrapText(true);
				GridPane.setColumnSpan(lbl_GamePrice, 2);
				GridPane.setRowSpan(lbl_GameDescription, 2);
		lbl_GameDescription.setPrefSize(220, 120);
		lbl_GameDescription.setMaxWidth(200);

// Price
	gp_HomePrice.setAlignment(Pos.CENTER);
		
	gp_HomeCustomer.add(gp_HomePrice, 1, 4);
	gp_HomePrice.add(lbl_GamePrice, 0, 0);
		lbl_GamePrice.setPrefSize(220, 40);
//		lbl_GamePrice.setMaxWidth(180);
			lbl_GamePrice.setAlignment(Pos.CENTER);
		
// Table Layout
	gp_HomeCustomer.add(gp_TableView, 0, 1, 1, 5);
	gp_TableView.add(tv_Game, 0, 0);

// Button Layout
	gp_HomeButton.setAlignment(Pos.CENTER);
	// Layout
	gp_HomeCustomer.add(gp_HomeButton, 1, 5);
	gp_HomeButton.add(btn_AddToChart, 0, 0);
		btn_AddToChart.setPrefSize(210, 35);
//		btn_AddToChart.setMaxWidth(200);
			btn_AddToChart.setAlignment(Pos.CENTER);
			
// GridPane Alignment
	GridPane.setHalignment(gp_HomeTitle, HPos.CENTER);
	GridPane.setHalignment(gp_HomeButton, HPos.RIGHT);
	GridPane.setHalignment(gp_HomePrice, HPos.CENTER);
	}
	
	private void setTable () {
		TableColumn <Models_Game, String> col_GameName = new TableColumn<>();
			col_GameName.setCellValueFactory(new PropertyValueFactory<>("gameName"));
			col_GameName.setPrefWidth(238);
	
	tv_Game.setPrefWidth(240);
	tv_Game.setPrefHeight(320);
	
	tv_Game.getColumns().addAll(col_GameName);
	tv_Game.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
	tv_Game.setPlaceholder(new Label(""));
	tv_Game.widthProperty().addListener((observable, oldValue, newValue) -> {
	TableHeaderRow header = (TableHeaderRow) tv_Game.lookup("TableHeaderRow");
	header.setPrefHeight(0);
	header.setMinHeight(0);
	header.setMaxHeight(0);
	header.setVisible(false);
});

tv_Game.getItems().clear();
}

private void getData () {
String query = "SELECT * FROM game";
ResultSet rs = connect.execQuery(query);

try {
	while (rs.next()) {
		String gameName = rs.getString("GameName");
		String gameDesc = rs.getString("GameDescription");
		int gamePrice = rs.getInt("Price");
		
		tv_Game.getItems().add(
			new Models_Game("", gameName, gameDesc, gamePrice));
	}
} catch (SQLException e) {
	e.printStackTrace();
	}
}

private void display_Game (Models_Game game) {
	String gameName = game.getGameName();
	String gameDesc = game.getGameDesc();
	int gamePrice = game.getGamePrice();
	
		lbl_GameTitle.setText(gameName);
		lbl_GameDescription.setText(gameDesc);
		lbl_GamePrice.setText("Rp"+String.valueOf(gamePrice));
}

private void eventHandler (Stage HomeStageCustomer) {
	btn_AddToChart.setVisible(false);
	
	tv_Game.setOnMouseClicked(event -> {
		Models_Game selectedGame = tv_Game.getSelectionModel().getSelectedItem();
		
		if (selectedGame != null) {
			display_Game(selectedGame);
			
			if (!display || !selectedGame.equals(displayedGame)) {
				btn_AddToChart.setVisible(true);
				displayedGame = selectedGame;
				display = true;
			} else {
				lbl_GameTitle.setText("");
				lbl_GameDescription.setText("");
				lbl_GamePrice.setText("");
				display = false;
				btn_AddToChart.setVisible(false);
				displayedGame = null;
			}
			String selectedGameName = selectedGame.getGameName();
			try {
				String query = "SELECT GameID FROM game WHERE GameName = '" + selectedGameName + "'";
				ResultSet rs = connect.execQuery(query);
				
			if (rs.next()) {
				selectedGameID = rs.getString("GameID");
				System.out.println("GameID from Database: " + selectedGameID);
				}
			rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				}
			}
		});
	
	logout.setOnAction(e -> {
		LoginPage_SNeam loginPage = new LoginPage_SNeam ();
		try {
			loginPage.start(new Stage());
			HomeStageCustomer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	});
	
	btn_AddToChart.setOnMouseClicked(event -> {
		Models_Game selectedGame = tv_Game.getSelectionModel().getSelectedItem();
		if (selectedGame != null) {
			Window wd_CartDetail = wd_Cart.CreateDetailWindow_Sneam();
			wd_Cart.displaySelectedGame(selectedGame, selectedGameID, currentUserID);
			wd_Cart.show();	
			
			wd_CartDetail.setResizableWindow(false);

		}
	    });
	
	cartHome.setOnAction(event -> {
		new CartPage_SNeam(HomeStageCustomer, selectedGameID, currentUserID);
	});
	}

	public HomePageCustomer_SNeam(Stage HomeStageCustomer, String loggedInUsername, String loggedInUserID) {
	this.currentUserID = loggedInUserID;
		initialize_Menu();
		layout_Menu();
		setTable();
		getData();
		eventHandler(HomeStageCustomer);
		
		System.out.println("Logged-in User ID: " + this.currentUserID);
		
		String username = LoginPage_SNeam.loggedInUsername;
			lbl_HomeTitle.setText("Hello, "+username);
		
		bp_HomeCustomer.setTop(menu_Home);
		bp_HomeCustomer.setCenter(gp_HomeCustomer);
		
		homeCustomerScene = new Scene (bp_HomeCustomer, 960, 740);
		
		HomeStageCustomer.setTitle("SNeam");
		HomeStageCustomer.setScene(homeCustomerScene);
		HomeStageCustomer.setResizable(false);
		HomeStageCustomer.show();	
	}

	@Override
	public void handle(MouseEvent args0) {
	}
}