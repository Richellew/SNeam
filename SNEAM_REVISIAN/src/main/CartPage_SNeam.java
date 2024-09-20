package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;

import modul.Models_Cart;
import util.SNeam_DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CartPage_SNeam implements EventHandler<MouseEvent> {
	private Scene CartCustomer_Scene;

	private GridPane gp_CartCustomer, gp_Title, gp_Price, gp_Button;
	private BorderPane bp_CartCustomer;

	private Menu dashboardCart, logoutMenu;
	private MenuBar menu_Cart;
	private MenuItem cartHome, homeMenu, logout;

	private Label lbl_CartTitle, lbl_GrossPrice;
	private TableView<Models_Cart> tv_Cart;
	private Button btn_CheckOut;
	private ObservableList<Models_Cart> cartlist;

	private String selectedGameID;
	private String currentUserID;
	private Map<String, Models_Cart> cart;

	SNeam_DatabaseManager connect = SNeam_DatabaseManager.getInstance();

	private void initalize_Cart() {
		bp_CartCustomer = new BorderPane();

		gp_CartCustomer = new GridPane();
		gp_Title = new GridPane();
		gp_Price = new GridPane();
		gp_Button = new GridPane();

		dashboardCart = new Menu("Dashboard");
		logoutMenu = new Menu("Log Out");

		menu_Cart = new MenuBar();
		menu_Cart.getMenus().addAll(dashboardCart, logoutMenu);
		cartHome = new MenuItem("Cart");
		homeMenu = new MenuItem("Home");
		logout = new MenuItem("Log Out");

		lbl_CartTitle = new Label("Your Cart");
		lbl_GrossPrice = new Label();

		cartlist = FXCollections.observableArrayList();
		
		tv_Cart = new TableView(cartlist);
		btn_CheckOut = new Button("Check Out");

		cart = new HashMap<>();

	}

	private void layout_Cart() {
// MenuBar
		dashboardCart.getItems().addAll(homeMenu, cartHome);
		logoutMenu.getItems().add(logout);
		menu_Cart.setPadding(new Insets(5, 10, 5, 10));

// GridPane Layout
		gp_CartCustomer.setPadding(new Insets(10));
		gp_CartCustomer.setVgap(5);
		gp_CartCustomer.setHgap(5);
		gp_CartCustomer.setAlignment(Pos.CENTER);

		gp_Title.setPadding(new Insets(10));
		gp_Title.setAlignment(Pos.CENTER);

		gp_Price.setPadding(new Insets(10));
		gp_Price.setAlignment(Pos.CENTER);

		gp_Button.setPadding(new Insets(10));
		gp_Button.setAlignment(Pos.CENTER);

// Label Layout
		// Title
		gp_CartCustomer.add(gp_Title, 0, 0, 1, 1);
		gp_Title.add(lbl_CartTitle, 0, 0);
		lbl_CartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		lbl_CartTitle.setPrefSize(220, 30);
		lbl_CartTitle.setAlignment(Pos.CENTER);
		// Table
		gp_CartCustomer.add(tv_Cart, 0, 1);
		// Price
		gp_CartCustomer.add(gp_Price, 0, 2, 1, 1);
		gp_Price.add(lbl_GrossPrice, 0, 0);
		lbl_GrossPrice.setFont(Font.font("Arial", FontWeight.LIGHT, 20));
		lbl_GrossPrice.setPrefSize(300, 30);
		lbl_GrossPrice.setAlignment(Pos.CENTER);
		// Button
		gp_CartCustomer.add(gp_Button, 0, 3, 1, 1);
		gp_Button.add(btn_CheckOut, 0, 0);
		btn_CheckOut.setPrefSize(100, 40);
		btn_CheckOut.setMaxWidth(100);

// GridPane Alignment
		GridPane.setHalignment(gp_Price, HPos.CENTER);
	}

	private void setTable() {
		TableColumn<Models_Cart, String> col_Name = new TableColumn<>("Name");
		col_Name.setCellValueFactory(new PropertyValueFactory<>("gameName"));
		col_Name.setMinWidth(300);

		TableColumn<Models_Cart, String> col_Price = new TableColumn<>("Price");
		col_Price.setCellValueFactory(new PropertyValueFactory<>("gamePrice"));
		col_Price.setMinWidth(90);

		TableColumn<Models_Cart, String> col_Quantity = new TableColumn<>("Quantity");
		col_Quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		col_Quantity.setMinWidth(90);

		TableColumn<Models_Cart, String> col_Total = new TableColumn<>("Total");
		col_Total.setCellValueFactory(new PropertyValueFactory<>("total"));
		col_Total.setMinWidth(90);

		tv_Cart.getColumns().addAll(col_Name, col_Price, col_Quantity, col_Total);

	}

	private void eventHandler(Stage CartStage) {
		logout.setOnAction(e -> {
			LoginPage_SNeam loginPage = new LoginPage_SNeam();
			try {
				loginPage.start(new Stage());
				CartStage.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		btn_CheckOut.setOnAction(event -> {
			try {
				if (cartlist.isEmpty()) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Invalid Request");
					alert.setHeaderText("Cart is empty");
					alert.setContentText("Transaction not possible");
					alert.showAndWait();

				}
				String id = generateID();
				String query = String.format("INSERT INTO transactionheader(TransactionID, UserID) VALUES ('%s','%s')", id, currentUserID);
				connect.execUpdate(query);
				
				for (Models_Cart models_Cart : cartlist) {
					String querydetail = String.format("INSERT INTO transactiondetail(TransactionID, GameID, Quantity) VALUES ('%s','%s','%d')", id, models_Cart.getGameID(), models_Cart.getQuantity());
					connect.execUpdate(querydetail);
				}
				String querydetele = String.format("DELETE FROM cart WHERE UserID = '%s'", currentUserID);
				connect.execUpdate(querydetele);
				setcart();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		homeMenu.setOnAction(event -> {
			new HomePageCustomer_SNeam(CartStage, selectedGameID, currentUserID);
		});
	}

	public String generateID() {
		String query = "SELECT MAX(CAST(SUBSTRING(TransactionID, 3) AS UNSIGNED)) FROM transactionheader";
		ResultSet resultSet = connect.execQuery(query);
		int userIndex;

		try {
			if (resultSet.next()) {
				int highestIndex = resultSet.getInt(1);

				userIndex = highestIndex + 1;

				String formattedIndex = String.format("%03d", userIndex);

				String transactionID = "TR" + formattedIndex;
				
				return transactionID;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setcart() {
		cartlist.clear();
		String query = String.format(
				"SELECT * FROM cart JOIN game ON cart.GameID = game.GameID WHERE cart.UserID = '%s';", currentUserID);
		ResultSet result = SNeam_DatabaseManager.getInstance().execQuery(query);
		int subtotal = 0;

		try {
			while (result.next()) {
				String gameID = result.getString("GameID");
				String gameName = result.getString("GameName");
				int gamePrice = result.getInt("Price");
				int quantity = result.getInt("Quantity");
				int total = gamePrice * quantity;
				subtotal += total;

				Models_Cart cart = new Models_Cart(currentUserID, gameID, quantity, gameName, gamePrice, total);
				cartlist.add(cart);

			}
			lbl_GrossPrice.setText("Gross Price: Rp. " + subtotal);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

	public CartPage_SNeam(Stage CartStage, String selectedGameID, String currentUserID) {
		this.selectedGameID = selectedGameID;
		this.currentUserID = currentUserID;
		this.cart = new HashMap<>();

		initalize_Cart();
		layout_Cart();
		setTable();
		setcart();
		eventHandler(CartStage);

		bp_CartCustomer.setTop(menu_Cart);
		bp_CartCustomer.setCenter(gp_CartCustomer);

		CartCustomer_Scene = new Scene(bp_CartCustomer, 960, 740);

		CartStage.setTitle("SNeam");
		CartStage.setScene(CartCustomer_Scene);
		CartStage.setResizable(false);
		CartStage.show();
	}

	@Override
	public void handle(MouseEvent arg0) {
	}
}