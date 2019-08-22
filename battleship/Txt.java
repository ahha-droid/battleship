package battleship;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Txt extends Text{
	
	protected Txt(String str) {
		super(str);
		setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
		setFill(Color.SADDLEBROWN);
	}
}