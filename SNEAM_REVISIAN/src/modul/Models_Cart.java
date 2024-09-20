package modul;

public class Models_Cart {
	protected String userID;
	protected String gameID;
	protected int quantity;
	protected String gameName;
	protected int gamePrice;
	protected String gameDesc;
	protected int total;
	public Models_Cart(String userID, String gameID, int quantity, String gameName, int gamePrice, String gameDesc,
			int total) {
		super();
		this.userID = userID;
		this.gameID = gameID;
		this.quantity = quantity;
		this.gameName = gameName;
		this.gamePrice = gamePrice;
		this.gameDesc = gameDesc;
		this.total = total;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getGameID() {
		return gameID;
	}
	public void setGameID(String gameID) {
		this.gameID = gameID;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public int getGamePrice() {
		return gamePrice;
	}
	public void setGamePrice(int gamePrice) {
		this.gamePrice = gamePrice;
	}
	public String getGameDesc() {
		return gameDesc;
	}
	public void setGameDesc(String gameDesc) {
		this.gameDesc = gameDesc;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Models_Cart(String userID, String gameID, int quantity, String gameName, int gamePrice, int total) {
		super();
		this.userID = userID;
		this.gameID = gameID;
		this.quantity = quantity;
		this.gameName = gameName;
		this.gamePrice = gamePrice;
		this.total = total;
	}
	           
	
	}