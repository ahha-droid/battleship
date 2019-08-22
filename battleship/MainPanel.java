package battleship;

import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainPanel extends VBox{
	protected int size, nbOfS1, nbOfS2, nbOfS3, nbOfS4, //ustalone w choosePanel
		shoots1, shoots2, shoots1accurate, shoots2accurate,
		xPC, yPC;
	protected int set1pl=0;				 //ustalone w setPanel ????
	protected String nameOfPl1, nameOfPl2; //ustalone w choosePanel
	protected GridPane boardPl1, boardPl2, //ustalone w setPanel wszystkie statki i pola puste - moje
		viewOfBoardPl1, viewOfBoardPl2, // trafiony i puste moje w przeciwnika
		viewOfBoardPl1toPl1, viewOfBoardPl2toPl2; // moje statki + trafy przeciwnika w nie
	protected char[][] tableBoardPl1, tableBoardPl2; //ustalone w setPanel
	protected boolean onePlayer, //ustalone w choosePanel
		flagPl1, flagPl2, doTurn, endTurn, goodPC;
	protected int menuNBR;
	protected MenuItem menu41;
	protected MenuItem menu12;
	protected Stage stage;
	protected int load;
	
	protected MainPanel(){
		super();
	}
	
}