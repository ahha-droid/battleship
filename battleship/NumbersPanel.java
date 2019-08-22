package battleship;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class NumbersPanel extends GridPane{  
	
	protected NumbersPanel(int size) {
		super();
		
		for (int i=0; i<size; i++) {
			Text number = new Text(i+1+"");
			number.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
			number.setFill(Color.SADDLEBROWN);
			Field field = new Field(Color.BISQUE);
			StackPane spForNumbers = new StackPane();
			spForNumbers.getChildren().add(field);
			spForNumbers.getChildren().add(number);		
			add(spForNumbers, i, 0);
		}
	}
}