package battleship;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Field extends Rectangle {
	
	protected Field(Color color) {
		super(20,20);
		setFill(color);
		setArcWidth(3);
		setArcHeight(3);
		setStroke(Color.BLACK);
		setStrokeWidth(0.2);
		}
	
}