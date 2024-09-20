package modul;

public class Models_TransactionHeader {
	protected int TransactionID, UserID;

	public Models_TransactionHeader(int transactionID, int userID) {
		super();
		TransactionID = transactionID;
		UserID = userID;
	}

	public int getTransactionID() {
		return TransactionID;
	}

	public void setTransactionID(int transactionID) {
		TransactionID = transactionID;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}
	
	
}
