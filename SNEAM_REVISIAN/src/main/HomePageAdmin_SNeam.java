package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import modul.Models_Game;
import util.SNeam_DatabaseManager;

public class HomePageAdmin_SNeam implements EventHandler<MouseEvent> {
private Scene homeAdminScene;
	
	private GridPane gp_HomeAdmin, gp_HomeTitle, gp_HomePrice, gp_TableView, 
			gp_LabelGrouping, gp_Button, gp_AdminTitle, gp_BottomLayout;
	private BorderPane bp_HomeAdmin;
	
	private Label lbl_HomeTitle, lbl_GameTitle, lbl_GameDescription, lbl_GamePrice,
	lbl_AdminMenu, lbl_GameTitleFill, lbl_GameDescriptionFill, lbl_GamePriceFill;
	
	private TextField tf_GameTitle, tf_GamePrice;
	
	private TextArea ta_GameDescription;
	
	private Menu logoutMenu;
	private MenuBar menu_Home;
	private MenuItem logout;
	
	private TableView <Models_Game> tv_Game;
	
	ScrollPane sp_HomeAdmin;
	
	private Button btn_AddGame, btn_UpdateGame, btn_DeleteGame;
	
	private VBox vbox_HomeAdmin;

	SNeam_DatabaseManager connect = SNeam_DatabaseManager.getInstance();
	
	String username = LoginPage_SNeam.loggedInUsername;
	private int existingGameCount = 0;
	
	private String selectedGameID;
	private String currentUserID;
	
	private boolean display = false;
	private Models_Game displayedGame;
	
	private void initialize_Menu () {
		bp_HomeAdmin = new BorderPane ();
		
		gp_HomeAdmin = new GridPane ();
		gp_HomePrice = new GridPane ();
		gp_HomeTitle = new GridPane ();
		gp_TableView = new GridPane ();
		
		gp_LabelGrouping = new GridPane ();
		gp_Button = new GridPane ();
		gp_AdminTitle = new GridPane ();
		gp_BottomLayout = new GridPane ();
		
		lbl_HomeTitle = new Label ("Hello, Admin");
		lbl_GameTitle = new Label ();
		lbl_GameDescription = new Label ();
		lbl_GamePrice = new Label ();
		
		tf_GamePrice = new TextField();
		tf_GameTitle = new TextField();
		ta_GameDescription = new TextArea();
		
		lbl_AdminMenu = new Label ("Admin Menu");
		lbl_GameTitleFill = new Label ("Game Title");
		lbl_GameDescriptionFill = new Label ("Game Description");
		lbl_GamePriceFill = new Label ("Price");
		
		logoutMenu = new Menu ("Log Out");
			menu_Home = new MenuBar();
			menu_Home.getMenus().add(logoutMenu);
		logout = new MenuItem ("Log Out");
		
		tv_Game = new TableView ();
		
		btn_AddGame = new Button("Add Game");
		btn_UpdateGame = new Button("Update Game");
		btn_DeleteGame = new Button("Delete Game");
		
		sp_HomeAdmin = new ScrollPane();
		
		vbox_HomeAdmin = new VBox ();
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

	
	private void setLayout () {
			// Center Table
// GridPane layout
	// GridPane general
	gp_HomeAdmin.setPadding(new Insets(10));
	gp_HomeAdmin.setHgap(10);
	gp_HomeAdmin.setVgap(5);
	gp_HomeAdmin.setAlignment(Pos.TOP_CENTER);
	
	// GridPane Title
	gp_HomeTitle.setPadding(new Insets(10));
	gp_HomeTitle.setVgap(5);
	gp_HomeTitle.setAlignment(Pos.CENTER);
	
	// GridPane HomePrice
	gp_HomePrice.setPadding(new Insets(10));
	gp_HomePrice.setVgap(5);
	gp_HomePrice.setAlignment(Pos.CENTER);
	
// MenuBar
	logoutMenu.getItems().add(logout);
		menu_Home.setPadding(new Insets(5, 10, 5, 10));
		
// Label Layout
	// Title
	gp_HomeAdmin.add(gp_HomeTitle, 0, 0, 3, 1);
	gp_HomeTitle.add(lbl_HomeTitle, 0, 0);
		lbl_HomeTitle.setFont(Font.font("Arial", FontWeight.BOLD , 30));
		// Game Title
		gp_HomeAdmin.add(lbl_GameTitle, 1, 1, 2, 1);
			lbl_GameTitle.setWrapText(true);
			lbl_GameTitle.setPrefSize(220, 60);
				lbl_GameTitle.setFont(Font.font("Arial", FontWeight.BOLD , 18));
				lbl_GameTitle.setAlignment(Pos.CENTER);
		// Game Description
		gp_HomeAdmin.add(lbl_GameDescription, 1, 2);
			lbl_GameDescription.setWrapText(true);
			lbl_GameDescription.setPrefSize(220, 120);
			lbl_GameDescription.setMaxWidth(200);
				lbl_GameDescription.setFont(new Font ("Arial", 11));
				lbl_GameDescription.setAlignment(Pos.CENTER_LEFT);
		// Game Price
		gp_HomeAdmin.add(gp_HomePrice, 1, 3);
		gp_HomePrice.add(lbl_GamePrice, 0, 0);
			lbl_GamePrice.setPrefSize(220, 40);
				lbl_GamePrice.setAlignment(Pos.CENTER);
		
// Table Layout
	gp_HomeAdmin.add(gp_TableView, 0, 1, 1, 5);
	gp_TableView.add(tv_Game, 0, 0);
	
			
			// Bottom Table
// GridPane Layout
	gp_BottomLayout.setPadding(new Insets(10));
	gp_BottomLayout.setVgap(10);
	gp_BottomLayout.setHgap(10);
	gp_BottomLayout.setAlignment(Pos.CENTER);
	
	gp_Button.setPadding(new Insets (10));
	gp_Button.setVgap(10);
	gp_Button.setHgap(10);
	gp_Button.setAlignment(Pos.CENTER);
	
	gp_AdminTitle.setPadding(new Insets(10));
	gp_AdminTitle.setVgap(5);
	gp_AdminTitle.setAlignment(Pos.CENTER);

// Title Layout
	gp_BottomLayout.add(gp_AdminTitle, 0, 0, 3, 1);
	gp_AdminTitle.add(lbl_AdminMenu, 0, 0);
		lbl_AdminMenu.setFont(Font.font("Arial", FontWeight.BOLD, 22));
	
// Label Layout
	gp_BottomLayout.add(gp_LabelGrouping, 0, 1, 1, 1);
	
	gp_LabelGrouping.add(lbl_GameTitleFill, 0, 1);
	gp_LabelGrouping.add(tf_GameTitle, 0, 2);
		gp_LabelGrouping.add(lbl_GameDescriptionFill, 0, 3);
		gp_LabelGrouping.add(ta_GameDescription, 0, 4);
			gp_LabelGrouping.add(lbl_GamePriceFill, 0, 5);
			gp_LabelGrouping.add(tf_GamePrice, 0, 6);
			// Label Design
				// Game Title 
				lbl_GameTitleFill.setPrefSize(500, 30);
				lbl_GameTitleFill.setMaxWidth(500);
					tf_GameTitle.setPrefSize(500, 30);
					tf_GameTitle.setMaxWidth(500);
				// Game Description
				lbl_GameDescriptionFill.setPrefSize(500, 30);
				lbl_GameDescriptionFill.setMaxWidth(500);
					ta_GameDescription.setPrefSize(500, 200);
					ta_GameDescription.setMaxWidth(500);
						ta_GameDescription.setWrapText(true);
				// Game Price
				lbl_GamePriceFill.setPrefSize(500, 30);
				lbl_GamePriceFill.setMaxWidth(500);
					tf_GamePrice.setPrefSize(500, 30);
					tf_GamePrice.setMaxWidth(500);

// Button Layout
	gp_BottomLayout.add(gp_Button, 1, 1, 1, 1);
		gp_Button.add(btn_AddGame, 1, 2);
		gp_Button.add(btn_UpdateGame, 1, 4);
		gp_Button.add(btn_DeleteGame, 1, 6);
		// Button Design
			btn_AddGame.setPrefSize(150, 75);
			btn_UpdateGame.setPrefSize(150, 75);
			btn_DeleteGame.setPrefSize(150, 75);
			
// VBox
	vbox_HomeAdmin.getChildren().addAll(gp_HomeAdmin, gp_BottomLayout);	
	
// ScrollPane
	sp_HomeAdmin.setContent(vbox_HomeAdmin);
	sp_HomeAdmin.setFitToWidth(true);
	sp_HomeAdmin.setFitToHeight(true);
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
					tf_GameTitle.setText(gameName);
					ta_GameDescription.setText(gameDesc);
					tf_GamePrice.setText(String.valueOf(gamePrice));
		}
	

			private void eventHandler (Stage HomeStageAdmin) {
				tv_Game.setOnMouseClicked(event -> {
					Models_Game selectedGame = tv_Game.getSelectionModel().getSelectedItem();
					
					if (selectedGame != null) {
						display_Game(selectedGame);
						
						if (!display || !selectedGame.equals(displayedGame)) {
							displayedGame = selectedGame;
							display = true;
						} else {
							lbl_GameTitle.setText("");
							lbl_GameDescription.setText("");
							lbl_GamePrice.setText("");
							display = false;
							displayedGame = null;
								tf_GameTitle.clear();
								ta_GameDescription.clear();
								tf_GamePrice.clear();
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
						HomeStageAdmin.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				});
				
			btn_AddGame.setOnMouseClicked(event -> {
				String gameName = tf_GameTitle.getText();
				String gameDesc = ta_GameDescription.getText();
				String gamePriceString = tf_GamePrice.getText();
				
				if (gameName.isEmpty()) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("No Game Title Warning");
					alert.setHeaderText("You haven't inputted a game title");
					alert.setContentText("Please fill the game title form");
					alert.showAndWait();
					return;
				} 
					
					if (gameName.length() > 50) {
						Alert alert = new Alert(Alert.AlertType.WARNING);
						alert.setTitle("Game Title Lenght Validation Warning");
						alert.setHeaderText("Game title cannot exceed 20 character");
						alert.setContentText("Game title is too long");
						alert.showAndWait();
						return;
					} 
				
						if (gameDesc.isEmpty()) {
							Alert alert = new Alert(Alert.AlertType.WARNING);
							alert.setTitle("No Game Description Warning");
							alert.setHeaderText("You haven't inputted a game description");
							alert.setContentText("Please fill the game description form");
							alert.showAndWait();
							return;
						}
						
							if (gameDesc.length() > 250) {
								Alert alert = new Alert(Alert.AlertType.WARNING);
								alert.setTitle("Game Description Lenght Validation Warning");
								alert.setHeaderText("Game title cannot exceed 250 character");
								alert.setContentText("Game description is too long");
								alert.showAndWait();
								return;
							}
						
								if (gamePriceString.isEmpty()) {
									Alert alert = new Alert(Alert.AlertType.WARNING);
									alert.setTitle("No Game Price Warning");
									alert.setHeaderText("You haven't inputted a game price");
									alert.setContentText("Please fill the game price form");
									alert.showAndWait();
									return;
								}  
								
									if (!gamePriceString.matches("\\d+")) {
										Alert alert = new Alert(Alert.AlertType.WARNING);
										alert.setTitle("Game Price Format Validation");
										alert.setHeaderText("You haven't fill game price using number");
										alert.setContentText("Please input the price with number");
										alert.showAndWait();
										return;
									}  
									
										if (gamePriceString.length() > 10) {
											Alert alert = new Alert(Alert.AlertType.WARNING);
											alert.setTitle("Invalid Input");
											alert.setHeaderText("Price cannot exceed 9999999999");
											alert.setContentText("Price is too high");
											alert.showAndWait();
											return;
										}  
				
					int gamePrice = Integer.parseInt(gamePriceString);
					String gameID = generateNewGameID();
					
					String query = String.format("INSERT INTO game (GameID, GameName, GameDescription, "
							+ "Price) VALUES ('%s', '%s', '%s', %d)",
		                    gameID, gameName, gameDesc, gamePrice);
					connect.execUpdate(query);
					
					tf_GameTitle.clear();
					ta_GameDescription.clear();
					tf_GamePrice.clear();
					
					tv_Game.getItems().clear();
					getData();
				});
			
			btn_UpdateGame.setOnMouseClicked(event -> {
				Models_Game selectedGame = tv_Game.getSelectionModel().getSelectedItem();
				if (selectedGame == null) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("No game selected");
					alert.setHeaderText("Please select a game in the table to update.");
					alert.showAndWait();
					
					} else {
						String gameName = tf_GameTitle.getText();
						String gameDesc = ta_GameDescription.getText();
						String gamePriceString = tf_GamePrice.getText();
					
						boolean update = false;
						int gamePrice = 0;
						
						if (gameName.length() > 50) {
							Alert alert = new Alert(Alert.AlertType.WARNING);
							alert.setTitle("Game Title Lenght Validation Warning");
							alert.setHeaderText("Game title cannot exceed 20 character");
							alert.setContentText("Game title is too long");
							alert.showAndWait();
						
				            update = true;
						} 
						if (!gamePriceString.isEmpty()) {
							if (!gamePriceString.matches("\\d+")) {
								Alert alert = new Alert(Alert.AlertType.WARNING);
								alert.setTitle("Game Price Format Validation");
								alert.setHeaderText("You haven't filled in the game price using numbers");
								alert.setContentText("Please input the price with numbers");
								alert.showAndWait();
								
								update = true;
								
							} else {
								gamePrice = Integer.parseInt(gamePriceString);
								if (gamePriceString.length() > 10) {
				            		
									Alert alert = new Alert(Alert.AlertType.WARNING);
									alert.setTitle("Invalid Input");
									alert.setHeaderText("Price cannot exceed 9999999999");
									alert.setContentText("Price is too high");
									alert.showAndWait();
									
									update = true;
									}
								}
							}
						
						if (!update && (!gameName.isEmpty() || !gameDesc.isEmpty() || !gamePriceString.isEmpty())) {
							try {
								String updateQuery = "UPDATE game SET ";
								if (!gameName.isEmpty() && gameName.length() <= 50) {
									updateQuery += "GameName = '" + gameName + "'";
								}
								
									if (!gameDesc.isEmpty() && gameDesc.length() <= 250) {
										if (!updateQuery.endsWith("SET ")) {
											updateQuery += ", ";
										}
										updateQuery += "GameDescription = '" + gameDesc + "'";
									}
								
										if (!gamePriceString.isEmpty() && gamePriceString.matches("\\d{1,10}")) {
											if (!updateQuery.endsWith("SET ") && !updateQuery.endsWith(", ")) {
												updateQuery += ", ";
											}
										updateQuery += "Price = " + gamePriceString;
										}
							
								updateQuery += " WHERE GameID = '" + selectedGameID + "'";

			                connect.execUpdate(updateQuery);
							
			                if (!gameName.isEmpty()) {
			                    lbl_GameTitle.setText(gameName);
			                    selectedGame.setGameName(gameName);
			                }
			                if (!gameDesc.isEmpty()) {
			                    lbl_GameDescription.setText(gameDesc);
			                    selectedGame.setGameDesc(gameDesc);
			                }
			                if (!gamePriceString.isEmpty()) {
			                    lbl_GamePrice.setText("Rp" + gamePrice);
			                    selectedGame.setGamePrice(gamePrice);
			                }
							
							selectedGame.setGameName(gameName);
							selectedGame.setGameDesc(gameDesc);
							selectedGame.setGamePrice(gamePrice);
							
							tf_GameTitle.clear();
							ta_GameDescription.clear();
							tf_GamePrice.clear();
							
							tv_Game.getItems().clear();
							
							getData();
					
					} catch (Exception e) {
						e.printStackTrace();
						}	
					}
				}
			});

			btn_DeleteGame.setOnMouseClicked(event -> {
				Models_Game selectedGame = tv_Game.getSelectionModel().getSelectedItem();
				if (selectedGame == null) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("No game selected");
					alert.setHeaderText("Please select a game in the table to delete.");
					alert.showAndWait();
				} else {
					Alert validateDelete = new Alert(Alert.AlertType.CONFIRMATION);
					validateDelete.setTitle("Delete game");
					validateDelete.setHeaderText("Are you sure you want to delete?");
					
					ButtonType btnOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
					ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
					
				validateDelete.getButtonTypes().setAll(btnOk, btnCancel);
				
				Optional<ButtonType> result = validateDelete.showAndWait();
				
				if (result.isPresent() && result.get() == btnOk) {
					try {
						String querydeletecart = String.format("DELETE FROM cart WHERE GameID = '%s'", selectedGameID);
						connect.execUpdate(querydeletecart);
						
						String querytransactiondetail = String.format("DELETE FROM transactiondetail WHERE GameID = '%s'", selectedGameID);
						connect.execUpdate(querytransactiondetail);
						
						String query = String.format("DELETE FROM game WHERE GameID = '%s'", selectedGameID);
						connect.execUpdate(query);
						
						lbl_GameTitle.setText("");
						lbl_GameDescription.setText("");
						lbl_GamePrice.setText("");
						
						tf_GameTitle.clear();
						ta_GameDescription.clear();
						tf_GamePrice.clear();
						
						tv_Game.getItems().clear();
						getData();
						
				} catch (Exception e) {
					e.printStackTrace();
					}
				}
			}
		});
	}
			
				private String generateNewGameID() {
				int maxGameID = 0;
				
				String queryMaxID = "SELECT MAX(SUBSTRING(GameID, 3)) AS MaxID FROM game";
				ResultSet rs = connect.execQuery(queryMaxID);
				try {
					if (rs.next()) {
						maxGameID = rs.getInt("MaxID");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				int nextGameIndex = maxGameID + 1;
				String gameID = String.format("GA%03d", nextGameIndex);
				
				return gameID;
				}
	
				
	public HomePageAdmin_SNeam(Stage LoginStage, String loggedInUserName, String loggedInUserID) {
	this.currentUserID = loggedInUserID;
	initialize_Menu ();
	setTable ();
		setLayout ();
		getData ();
		eventHandler (LoginStage);
		
		bp_HomeAdmin.setTop(menu_Home);
		bp_HomeAdmin.setCenter(sp_HomeAdmin);
		
		homeAdminScene = new Scene (bp_HomeAdmin, 960, 740);
		
		System.out.println("Logged-in User ID: " + this.currentUserID);
		
		String username = LoginPage_SNeam.loggedInUsername;
			lbl_HomeTitle.setText("Hello, "+username);
		
			LoginStage.setTitle("SNeam");
			LoginStage.setScene(homeAdminScene);
			LoginStage.setResizable(false);
			LoginStage.show();	
	}
	
	@Override
	public void handle(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}