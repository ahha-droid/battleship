package battleship;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LettersPanel extends GridPane{
	
	protected LettersPanel(int size) {
		super();
		
		for (int i=0; i<size; i++) {
			Text letter = new Text((char)('a'+i)+"");
			letter.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
			letter.setFill(Color.SADDLEBROWN);
			Field field2 = new Field(Color.BISQUE);
			StackPane spForLetters = new StackPane();
			spForLetters.getChildren().add(field2);
			spForLetters.getChildren().add(letter);
			add(spForLetters, 0, i);	
		}
	}
		
}