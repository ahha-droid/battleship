package battleship;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ColorMenuItem extends MenuItem {
	protected MenuItem menu;
	
	protected ColorMenuItem(Color color, String colortxt, VBox stage) {
		super();
		Rectangle recColor= new Rectangle(10,10);
		recColor.setStroke(Color.BLACK);
		recColor.setFill(color);
		recColor.setStrokeWidth(0.5);
		menu = new MenuItem(colortxt, recColor);
		menu.setOnAction(new EventHandler<ActionEvent>(){public void handle(ActionEvent e){
			stage.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, new Insets(5))));}});
	}
	
}
