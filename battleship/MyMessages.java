package battleship;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MyMessages{
	protected Alert alert = new Alert(AlertType.ERROR);
	
	protected MyMessages(String title, String contentAllert) {
		alert.setTitle(title);
		alert.setHeaderText(contentAllert);
		alert.setContentText(null);
	}
	
}