package battleship;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class EndPanel extends GridPane {
	
	protected EndPanel(MainPanel mainPanel, String winner) {
		super();
		mainPanel.menuNBR=0;
		
		setPadding(new Insets(80,0,0,100));
		setHgap(20);
		setVgap(20);
		
		Text tWIs = new Text("The winner is: ");
		tWIs.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 25));
		tWIs.setFill(Color.SADDLEBROWN);
		Text win = new Text(winner);
		win.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 25));
		win.setFill(Color.SADDLEBROWN);
		
		add(tWIs, 1, 0);
		add(win, 1, 1);
		
		// wyswietl plansze graczy - widok wszystkich statkow i trafien przeciwnika
		GridPane boardPl1 = mainPanel.viewOfBoardPl1toPl1;
		GridPane boardPl2 = mainPanel.viewOfBoardPl2toPl2;
		boardPl1.setVisible(true);
		boardPl2.setVisible(true);
		
		// obliczenie celnosci
		int accuracy1=(mainPanel.shoots1==0? 0 : (int)(((double)mainPanel.shoots1accurate/(double)mainPanel.shoots1)*100));
		int accuracy2=(mainPanel.shoots2==0? 0 : (int)(((double)mainPanel.shoots2accurate/(double)mainPanel.shoots2)*100));
		
		//wyswietlenie podsumowania
		add(new Txt(mainPanel.nameOfPl1 + ": "), 0, 2);
		add(new Txt("Celnosc: "+ accuracy1 +"%"), 0, 3);
		add(new Txt("Liczba oddanych strzalow: "+ mainPanel.shoots1), 0, 4);
		add(boardPl1, 0,5);
		
		add(new Txt(mainPanel.nameOfPl2 + ": "), 2, 2);
		add(new Txt("Celnosc: "+ accuracy2+"%"), 2, 3);
		add(new Txt("Liczba oddanych strzalow: "+ mainPanel.shoots2), 2, 4);
		add(boardPl2, 2,5);
		
		Timeline timer = new Timeline( new KeyFrame(Duration.seconds(5), event -> {boardPl1.setVisible(true); boardPl2.setVisible(true); }));
		timer.play();
	}
	
}