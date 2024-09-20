package modul;

public class Models_Game {
	protected String gameID;
	protected String gameName;
	protected String gameDesc;
	protected int gamePrice;
	
	public Models_Game(String gameID, String gameName, String gameDesc, int gamePrice) {
		super();
		this.gameID = gameID;
		this.gameName = gameName;
		this.gameDesc = gameDesc;
		this.gamePrice = gamePrice;
	}

	public String getGameID() {
		return gameID;
	}
	public void setGameID(String gameID) {
		this.gameID = gameID;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getGameDesc() {
		return gameDesc;
	}
	public void setGameDesc(String gameDesc) {
		this.gameDesc = gameDesc;
	}
	public int getGamePrice() {
		return gamePrice;
	}
	public void setGamePrice(int gamePrice) {
		this.gamePrice = gamePrice;
	}
}
