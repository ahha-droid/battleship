package battleship;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.control.Label;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.io.File;
import java.io.FileWriter;

import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class GamePanel extends GridPane {
	protected boolean flagPl1=false, flagPl2=false, doTurn=false, endTurn=false; // ustalone w if
	protected boolean onePlayer, goodPC; // ustalone i ustalone w if
	protected GridPane boardPl1, boardPl2, viewOfBoardPl1, viewOfBoardPl2, viewOfBoardPl1toPl1, viewOfBoardPl2toPl2; // budowane na podstawie tableboard
	protected char[][] tableBoardPl1, tableBoardPl2; //ustalone 
	protected String nameOfPl1, nameOfPl2; //ustalone 
	protected int nbOfFieldsSh, shoots1, shoots2, shoots1accurate, shoots2accurate, size;  // ustalone w if i ustalone
	protected Label[] txt;
	protected MainPanel mainPanel;
	protected int xPC, yPC; //ustalone w if
	
	protected GamePanel(MainPanel mainPanel) {
		super();
		
		/// okreslenie pol klasy
		setPadding(new Insets(80,0,0,100));		setHgap(40);		setVgap(20);
		mainPanel.menuNBR=2;
		mainPanel.viewOfBoardPl1 = new GridPane();	mainPanel.viewOfBoardPl2 = new GridPane();
		mainPanel.viewOfBoardPl1toPl1 = new GridPane();	mainPanel.viewOfBoardPl2toPl2 = new GridPane();
		this.mainPanel=mainPanel;
		
		goodPC=false;
		shoots1 = 0; shoots2 = 0; shoots1accurate = 0; shoots2accurate = 0; size = mainPanel.size; xPC=0; yPC=0;
		nbOfFieldsSh = mainPanel.nbOfS1+mainPanel.nbOfS2*2+mainPanel.nbOfS3*3+mainPanel.nbOfS4*4;
		onePlayer=mainPanel.onePlayer;
		if (mainPanel.load==1) {
			xPC=mainPanel.xPC; yPC=mainPanel.yPC; flagPl1=mainPanel.flagPl1; flagPl2=mainPanel.flagPl2; doTurn=mainPanel.doTurn; endTurn=mainPanel.endTurn; goodPC=mainPanel.goodPC;
			shoots1 = mainPanel.shoots1; shoots2 = mainPanel.shoots2; shoots1accurate = mainPanel.shoots1accurate; shoots2accurate = mainPanel.shoots2accurate;
			mainPanel.boardPl1 = new GridPane(); mainPanel.boardPl2 = new GridPane();
		}
		boardPl1 = mainPanel.boardPl1; boardPl2 = mainPanel.boardPl2; 
		viewOfBoardPl1 = mainPanel.viewOfBoardPl1;  viewOfBoardPl2 = mainPanel.viewOfBoardPl2;
		viewOfBoardPl1toPl1 = mainPanel.viewOfBoardPl1toPl1; viewOfBoardPl2toPl2 = mainPanel.viewOfBoardPl2toPl2; 
		tableBoardPl1 = mainPanel.tableBoardPl1; tableBoardPl2 = mainPanel.tableBoardPl2; 
		nameOfPl1 = mainPanel.nameOfPl1; nameOfPl2 = mainPanel.nameOfPl2;
		//
		
		////ustawienie widoku 
		NumbersPanel rowOfNumbers1 = new NumbersPanel(size);
		LettersPanel colOfLetters1 = new LettersPanel(size);
		NumbersPanel rowOfNumbers2 = new NumbersPanel(size);
		LettersPanel colOfLetters2 = new LettersPanel(size);
		
		fillBoard(tableBoardPl1, viewOfBoardPl1, viewOfBoardPl1toPl1, boardPl1);
		fillBoard(tableBoardPl2, viewOfBoardPl2, viewOfBoardPl2toPl2, boardPl2);
		
		GridPane panel1 = doPanel(nameOfPl1, rowOfNumbers1, colOfLetters1, boardPl1, viewOfBoardPl1, viewOfBoardPl1toPl1);
		GridPane panel2 = doPanel(nameOfPl2, rowOfNumbers2, colOfLetters2, boardPl2, viewOfBoardPl2, viewOfBoardPl2toPl2);
		
		add(panel1,1,1);
		add(panel2,3,1);
			
		txt = new Label[] {	new Label("It`s turn of "+nameOfPl1), new Label("It`s turn of "+nameOfPl2), new Label("Click here ")};
		
		Circle circle = new Circle(20);
		Stop[] stops = new Stop[] { new Stop(0, Color.BLACK), new Stop(1, Color.DARKVIOLET)};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 1, true, CycleMethod.REPEAT, stops);
		circle.setFill(lg1);
	
		Button playBTN = new Button();
		playBTN.setShape(circle); 
		playBTN.setOpacity(0.38);
		playBTN.setMaxSize(40,40);
		playBTN.setMinSize(40,40);
		add(circle, 2,2);
		add(playBTN, 2,2);
		add(txt[0], 1,2); add(txt[1], 3,2); add(txt[2], 2,3);
		
		boardPl1.setVisible(false);
		boardPl2.setVisible(false);
		viewOfBoardPl1.setVisible(true);
		viewOfBoardPl2.setVisible(true);
		viewOfBoardPl1toPl1.setVisible(false);
		viewOfBoardPl2toPl2.setVisible(false);
		txt[1].setVisible(false);
		////////////
		
		if (onePlayer==false) {  //opcja gry dla dwoch graczy
			playBTN.setOnMouseClicked(new EventHandler<MouseEvent>() {public void handle(MouseEvent me) {
				if (flagPl1==false && flagPl2==false) {  // na poczatku gry
					flagPl1=true; 
					flagPl2=false;
					viewOfBoardPl1.setVisible(false);
					viewOfBoardPl2.setVisible(true);
					viewOfBoardPl1toPl1.setVisible(true);
					viewOfBoardPl2toPl2.setVisible(false);
					txt[2].setVisible(false);
				}
				else if (flagPl1==false && flagPl2==true && doTurn==true && endTurn==true) { // po wykonaniu ruchu przez 2 gracza, widok przed ruchem 1
					flagPl1=true; 											// bo teraz jest ruch 1
					flagPl2=false;
					doTurn=false;
					endTurn=false;
					viewOfBoardPl1.setVisible(false);						// 
					viewOfBoardPl2.setVisible(true);						// widzi widok planszy2
					viewOfBoardPl1toPl1.setVisible(true);					// i widok swojej dla siebie
					viewOfBoardPl2toPl2.setVisible(false);
					txt[0].setText("It`s turn of "+nameOfPl1);
					txt[0].setVisible(true);
					txt[1].setVisible(false);
					txt[2].setVisible(false);
				}
				else if (flagPl1==true && flagPl2==false && doTurn==true && endTurn==true) { // po wykonaniu ruchu przez 1 gracza, widok planszy przed ruchem 2
					flagPl1=false; 
					flagPl2=true;	
					doTurn=false;
					endTurn=false;										// bo teraz jest ruch 
					viewOfBoardPl1.setVisible(true);						// widzi widok planszy1
					viewOfBoardPl2.setVisible(false);
					viewOfBoardPl1toPl1.setVisible(false);
					viewOfBoardPl2toPl2.setVisible(true);					// i widok swojej dla siebie
					txt[0].setVisible(false);
					txt[1].setText("It`s turn of "+nameOfPl2);
					txt[1].setVisible(true);
					txt[2].setVisible(false);
				}
			}});
		}
		else { // opcja gry z komputerem
			flagPl2=true; doTurn=true; endTurn=true;
			playBTN.setOnMouseClicked(new EventHandler<MouseEvent>() {public void handle(MouseEvent me) {
				viewOfBoardPl1toPl1.setVisible(true);
				viewOfBoardPl2.setVisible(true);
				
				if (flagPl1==false && flagPl2==true && doTurn==true && endTurn==true) {
					flagPl1=true; 											
					flagPl2=false;
					doTurn=false;
					endTurn=false;
					txt[0].setText("It`s turn of "+nameOfPl1);
					txt[0].setVisible(true);
					txt[1].setVisible(false);
					txt[2].setVisible(false);
				}
				else if (flagPl1==true && flagPl2==false && doTurn==true && endTurn==true) {
					flagPl1=false; 
					flagPl2=true;	
					txt[0].setVisible(false);
					txt[1].setText("It`s turn of "+nameOfPl2);
					txt[1].setVisible(true);
					txt[2].setVisible(false);
					pcTurn(); 									// podczas ruchu PC, nie ma opcji klikniecia na widok planszy gracza (bo jest unvisible) wywolywana jest za to metoda ruchu PC		
				}
			}});
		}
		
		// zapisz
		mainPanel.menu41.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				if (mainPanel.menuNBR==2) {
					save(mainPanel.stage, mainPanel);
				}
				else {
					MyMessages mm=new MyMessages("opcja dostepna podczas gry", "Aby zapisac stan gry, musisz najpierw ja rozpoczac");
					mm.alert.showAndWait();
				}
			}
		});
	}
	
	private void fieldMouseEvents(Field field, MainPanel mainPanel) {
		field.setOnMouseEntered(e ->{			// podswietlamy tylko pola na ktore nie zostal oddany strzal
			if (field.getFill().equals(Color.CORNSILK) && (flagPl1!=false || flagPl2!=false)&& doTurn==false && endTurn==false) field.setFill(Color.LIGHTSKYBLUE);});
			 
		field.setOnMouseExited(e -> {
			if (field.getFill().equals(Color.LIGHTSKYBLUE))field.setFill(Color.CORNSILK);});
		
		field.setOnMouseClicked(e ->{ 			// mozna kliknac tylko podswietlone pole
			if (field.getFill().equals(Color.LIGHTSKYBLUE)){
				if (flagPl1==true) changeColor(mainPanel, field,  tableBoardPl2, viewOfBoardPl1toPl1, viewOfBoardPl2toPl2, viewOfBoardPl1, viewOfBoardPl2); 
				else if(flagPl2==true ) changeColor(mainPanel,  field,  tableBoardPl1, viewOfBoardPl2toPl2, viewOfBoardPl1toPl1, viewOfBoardPl2, viewOfBoardPl1);
			};});
	}
	
	private void changeColor(MainPanel mainPanel, Field field, char[][] tableBoardENEMY, GridPane viewOfBoardOURforWE, GridPane viewOfBoardENEMYforENEMY, GridPane viewOfBoardOUR, GridPane viewOfBoardENEMY) {
		int col = GridPane.getColumnIndex(field);
		int row = GridPane.getRowIndex(field);
		if (tableBoardENEMY[row][col]=='0'||tableBoardENEMY[row][col]=='*') {  //jezeli trafimy w wode -> przy odczycie niewiadoma - cornsilk
			field.setFill(Color.DEEPSKYBLUE); 				// wypelnij viewOfBoardENEMY
			tableBoardENEMY[row][col]='^';						// zmien wartosc w tableBoardPl -> przy odczycie blekit - pudlo
			Field field2 = new Field(Color.DEEPSKYBLUE);	 // wypelnij viewOfBoardPl
			viewOfBoardENEMYforENEMY.add(field2, col, row); // uzupelnij tez widok strzalu w widoku planszy ENEMY for ENEMY
							// ruch skonczony -> pokaz druga plansze
			if (flagPl1==true) shoots1++;
			else  shoots2++;
			endTurn=true;
			if (onePlayer==false) {
				viewOfBoardOUR.setVisible(true);
				timerSetOff(viewOfBoardOURforWE);				
			}
			else {
				goodPC=false;
				doTurn=true;
				if (flagPl1==true) txt[0].setText("Turn of "+nameOfPl1 +" - end");// zmiana napisu
				if (flagPl2==true) txt[1].setText("Turn of "+nameOfPl2 +" - end");// zmiana napisu
				txt[2].setVisible(true);
			}
		}
		else if (tableBoardENEMY[row][col]=='X') {					// -> przy odczycie niewiadoma - cornsilk
				field.setFill(Color.CRIMSON); 						// wypelnij viewOfBoardENEMY
				viewOfBoardENEMYforENEMY.add(field, col, row);	
				tableBoardENEMY[row][col]='1';						// zmien wartosc w tableBoardPl
				Field field2 = new Field(Color.CRIMSON);
				viewOfBoardENEMY.add(field2, col, row);			 // uzupelnij tez widok strzalu w widoku planszy ENEMY for ENEMY
				int sas = checkNearX(col, row, tableBoardENEMY);
				if (onePlayer==true) {
					xPC=col; 	
					yPC=row;	
					goodPC=true;
				}
			if (sas==0) { 
				changeNear1(col, row, tableBoardENEMY,  viewOfBoardENEMY); 
				changeWaterNearShip(col, row, tableBoardENEMY, viewOfBoardENEMY);
				goodPC=false;
			}
			
			if (flagPl1==true) {shoots1++; shoots1accurate++;}
			else  {shoots2++; shoots2accurate++;}
			if (shoots1accurate==nbOfFieldsSh) {setEndPanel(mainPanel, nameOfPl1);}
			if (shoots2accurate==nbOfFieldsSh) {setEndPanel(mainPanel, nameOfPl2);}
			if (onePlayer==true && flagPl2==true&&shoots2accurate!=nbOfFieldsSh) pcTurn();
		}
	}
	
	// sprawdz czy statek zostal zatopiony
	private int checkNearX(int col, int row, char[][] tableBoardENEMY) {
		int[][] tab1= {{row+1, row+2, row+3},{col,col,col}};
		int[][] tab2= {{row-1, row-2, row-3},{col,col,col}};
		int[][] tab3= {{row, row, row},{col+1,col+2,col+3}};
		int[][] tab4= {{row, row, row},{col-1,col-2,col-3}};
		
		if (checkX(tab1, tableBoardENEMY)==1) return 1;
		if (checkX(tab2, tableBoardENEMY)==1) return 1;
		if (checkX(tab3, tableBoardENEMY)==1) return 1;
		if (checkX(tab4, tableBoardENEMY)==1) return 1;
		
		return 0;
	}
	
	// jesli statek zostal zatopiony zmien kolor z czerwonego na brazowy
	private void changeNear1(int col, int row, char[][] tableBoardENEMY, GridPane viewOfBoardENEMY) {
		viewOfBoardENEMY.add(new Field(Color.SADDLEBROWN), col, row);
		tableBoardENEMY[row][col]='4';
		
		int[][] tab1= {{row+1, row+2, row+3},{col,col,col}};
		int[][] tab2= {{row-1, row-2, row-3},{col,col,col}};
		int[][] tab3= {{row, row, row},{col+1,col+2,col+3}};
		int[][] tab4= {{row, row, row},{col-1,col-2,col-3}};
		
		fillFields(tab1, tableBoardENEMY, viewOfBoardENEMY, 2);
		fillFields(tab2, tableBoardENEMY, viewOfBoardENEMY, 2);	
		fillFields(tab3, tableBoardENEMY, viewOfBoardENEMY, 2);
		fillFields(tab4, tableBoardENEMY, viewOfBoardENEMY, 2);
	}
	
	// jesli statek zostal zatopiony zmien pobliska wode
	private void changeWaterNearShip(int col, int row, char[][] tableBoardENEMY, GridPane viewOfBoardENEMY) {
		changeNearWater(col, row, tableBoardENEMY, viewOfBoardENEMY);
		
		int[][] tab1= {{row+1, row+2, row+3},{col,col,col}};
		int[][] tab2= {{row-1, row-2, row-3},{col,col,col}};
		int[][] tab3= {{row, row, row},{col+1,col+2,col+3}};
		int[][] tab4= {{row, row, row},{col-1,col-2,col-3}};
		
		fillFields(tab1, tableBoardENEMY, viewOfBoardENEMY, 1);
		fillFields(tab2, tableBoardENEMY, viewOfBoardENEMY, 1);	
		fillFields(tab3, tableBoardENEMY, viewOfBoardENEMY, 1);
		fillFields(tab4, tableBoardENEMY, viewOfBoardENEMY, 1);
	}
	
	// sprawdz czy obok jest statek dla konkretnych komorek
	private int checkX(int[][] tab, char[][] tableBoardENEMY) {
		
		if (tab[0][0]>=0 && tab[0][0]<size && tab[1][0]>=0 && tab[1][0]<size) {
			if (tableBoardENEMY[tab[0][0]][tab[1][0]]=='X') return 1;
			if (tab[0][1]>=0 && tab[0][1]<size && tab[1][1]>=0 && tab[1][1]<size) {
				if (tableBoardENEMY[tab[0][0]][tab[1][0]]=='1'&& tableBoardENEMY[tab[0][1]][tab[1][1]]=='X') return 1;
				if (tab[0][2]>=0 && tab[0][2]<size && tab[1][2]>=0 && tab[1][2]<size) {
					if (tableBoardENEMY[tab[0][0]][tab[1][0]]=='1'&& tableBoardENEMY[tab[0][1]][tab[1][1]]=='1'&& tableBoardENEMY[tab[0][2]][tab[1][2]]=='X') return 1;}
			}
		}
		return 0;
	}
	
	// wypelnij pola, w zaleznosci od miejsca wywolania funkcji - statku lub pobliskiej wody
	private void fillFields(int[][] tab, char[][] tableBoardENEMY, GridPane viewOfBoardENEMY, int choose) {
		char d; char f;
		
		switch (choose) {
		case 1: d=f='4'; break;
		default: d='1'; f='4'; break;}
		
		if (tab[0][0]>=0 && tab[0][0]<size && tab[1][0]>=0 && tab[1][0]<size) {
			if (tableBoardENEMY[tab[0][0]][tab[1][0]]==d) {
				if (choose==1) changeNearWater(tab[1][0], tab[0][0], tableBoardENEMY, viewOfBoardENEMY); 
				if (choose==2) {viewOfBoardENEMY.add(new Field(Color.SADDLEBROWN), tab[1][0], tab[0][0]); 
					tableBoardENEMY[tab[0][0]][tab[1][0]]=f;		
				}
				if (tab[0][1]>=0 && tab[0][1]<size && tab[1][1]>=0 && tab[1][1]<size) {
					if (tableBoardENEMY[tab[0][1]][tab[1][1]]==d) {
						if (choose==1) changeNearWater(tab[1][1], tab[0][1], tableBoardENEMY, viewOfBoardENEMY); 
						if (choose==2) {viewOfBoardENEMY.add(new Field(Color.SADDLEBROWN), tab[1][1], tab[0][1]); 
							tableBoardENEMY[tab[0][1]][tab[1][1]]=f;		
						}
						if (tab[0][2]>=0 && tab[0][2]<size && tab[1][2]>=0 && tab[1][2]<size) {
							if (tableBoardENEMY[tab[0][2]][tab[1][2]]==d) {
								if (choose==1) changeNearWater(tab[1][2], tab[0][2], tableBoardENEMY, viewOfBoardENEMY); 	
								if (choose==2) {viewOfBoardENEMY.add(new Field(Color.SADDLEBROWN), tab[1][2], tab[0][2]); 
									tableBoardENEMY[tab[0][2]][tab[1][2]]=f;		
								}
							}
						}
					}
				}
			}
		}
	}

	// zmiana pol sasiadujacej wody dla konkretnych komorek statku
	private void changeNearWater(int col, int row, char[][] tableBoardENEMY, GridPane viewOfBoardENEMY) {
		for (int r=(row-1); r<=(row+1); r++) {
			for (int c=(col-1); c<=(col+1); c++) {
				if (!(c==col && r==row)) {
					if (r>=0 && r<size && c>=0 && c<size) {
						if (tableBoardENEMY[r][c]=='*'||tableBoardENEMY[r][c]=='^') {
							viewOfBoardENEMY.add(new Field(Color.DEEPSKYBLUE), c, r); 
							if (tableBoardENEMY[r][c]=='*') tableBoardENEMY[r][c]='#';
						}
					}
				}
			}
		}
	}
	
	//opoznienie - po wykonaniu ruchu gracz moze zaobserwowac zaistniala zmiane
	public void timerSetOff(GridPane gp) {
		Timeline timer = new Timeline( new KeyFrame(Duration.seconds(1), event -> {
			gp.setVisible(false); 
			doTurn=true;
			if (flagPl1==true) txt[0].setText("Turn of "+nameOfPl1 +" - end");// zmiana napisu
			if (flagPl2==true) txt[1].setText("Turn of "+nameOfPl2 +" - end");// zmiana napisu
			txt[2].setVisible(true);
			}));
			timer.play();
	}
	
	// wywolanie panelu podsumowania
	public void setEndPanel(MainPanel mainPanel, String nameOfPl) {
		mainPanel.shoots1 = shoots1;
		mainPanel.shoots2 = shoots2;
		mainPanel.shoots1accurate = shoots1accurate;
		mainPanel.shoots2accurate = shoots2accurate;
		
		mainPanel.getChildren().clear();
		EndPanel endPanel = new EndPanel(mainPanel, nameOfPl);
		mainPanel.getChildren().add(endPanel);
	}
	
	// tworzenie widoku nowych boardow w opariu o Array list
	public void fillBoard(char[][] tableBoard, GridPane viewOfBoard, GridPane viewOfBoardPltoPl, GridPane boardPl) {
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				Field field2;
				Field field4;
				Field field3;
				switch (tableBoard[j][i]) {
					case '0' : 
					case '^' : 	field2 = new Field(Color.CORNSILK); break;
					case '*' :
					case '#' :	field2 = new Field(Color.ANTIQUEWHITE); break;
					default: 	field2 = new Field(Color.SADDLEBROWN); break;  //4, 1, X
				}
				switch (tableBoard[j][i]) {
					case 'X' : 
					case '0' :  
					case '*' :	field3 = new Field(Color.CORNSILK); break;
					case '1' :	field3 = new Field(Color.CRIMSON); break;
					case '^' : 
					case '#' : 	field3 = new Field(Color.DEEPSKYBLUE); break;
					default:	field3 = new Field(Color.SADDLEBROWN); break;		//4
				}
				switch (tableBoard[j][i]) {
				case '0' : 	field4 = new Field(Color.CORNSILK); break;
				case 'X' :  field4 = new Field(Color.SADDLEBROWN); break;
				case '^' :	field4 = new Field(Color.DEEPSKYBLUE); break;
				case '*' :  
				case '#' : 	field4 = new Field(Color.ANTIQUEWHITE); break;
				default:	field4 = new Field(Color.CRIMSON); break;		// 1 4
				}
				boardPl.add(field2, i, j);
				fieldMouseEvents(field3, mainPanel);
				viewOfBoard.add(field3, i, j);
				viewOfBoardPltoPl.add(field4, i, j);
			}
		}
	}
	
	// ustawienie boardow +
	private GridPane doPanel(String nameOfPl, NumbersPanel numbers, LettersPanel letters, 
			GridPane boardPl, GridPane viewOfBoard, GridPane viewOfBoardPltoPl) {
		
		GridPane panel = new GridPane();
		StackPane spPl = new StackPane();
		panel.setHgap(5);
		panel.setVgap(5);
		panel.add(new Txt(nameOfPl+"`s board"), 1, 0);
		panel.add(numbers, 1,1);
		panel.add(letters, 0,2);
		spPl.getChildren().addAll(boardPl, viewOfBoard, viewOfBoardPltoPl);
		panel.add(spPl,1,2);
		
		return panel;
	}
	
	@SuppressWarnings("static-access")  // zwraca field na podstawie col i row
	private Field getNodeByRowColumnIndex(final int row, final int col, GridPane gridPane) {
	    Field field = null;
	    ObservableList<Node> childrens = gridPane.getChildren();

	    for (Node f : childrens) {
	        if(gridPane.getRowIndex(f) == row && gridPane.getColumnIndex(f) == col) {
	            field = (Field) f;
	        }
	    }
	    return field;
	}
	
	private void pcTurn() {
		int colComp=(int)(Math.random()*size);
		int rowComp=(int)(Math.random()*size);
		
		if (goodPC==true) { /// jezeli trafil statek - niech kolejny strzal sprawdza komorki obok
			int iteration = 0;
			
			colComp=xPC;
			rowComp=yPC;	
			while (!(tableBoardPl1[rowComp][colComp]=='X' || tableBoardPl1[rowComp][colComp]=='*' || tableBoardPl1[rowComp][colComp]=='0'||iteration==20)) {
				colComp=2*size;
				rowComp=2*size;
				
				int vertHor=(int)(Math.random()*2);
				int newXPC=xPC, newYPC=yPC;
				
				while (!(rowComp>=0 && rowComp<size && colComp>=0 && colComp<size )){
					vertHor=(int)(Math.random()*2);
					newXPC=xPC; newYPC=yPC;
					switch (vertHor) {
						case 1: newXPC= xPC+3-((int)(Math.random()*2)+1)*2;break;
						default: newYPC= yPC+3-((int)(Math.random()*2)+1)*2;break;
					}	
					colComp=newXPC;
					rowComp=newYPC;	
				}
				iteration++;
			}
			if (iteration>19) goodPC=false;
		}
		
		if (goodPC==false) {
		colComp=(int)(Math.random()*size);
		rowComp=(int)(Math.random()*size);
		
		while (!(tableBoardPl1[rowComp][colComp]=='X' || tableBoardPl1[rowComp][colComp]=='*' || tableBoardPl1[rowComp][colComp]=='0')) {
			colComp=(int)(Math.random()*size);
			rowComp=(int)(Math.random()*size);
		}}
		changeColor(mainPanel,  getNodeByRowColumnIndex(rowComp, colComp, viewOfBoardPl1),  tableBoardPl1, viewOfBoardPl2toPl2, viewOfBoardPl1toPl1, viewOfBoardPl2, viewOfBoardPl1);
	}			
	
	protected void save(Stage stage, MainPanel mainPanel) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Zapisz biezaca gre");
		File file = fileChooser.showSaveDialog(stage);
            
		if(file != null){SaveFile(mainPanel, file);}
	}	
	
	private void SaveFile(MainPanel mainPanel, File file) {
		try{
			FileWriter fWriter=new FileWriter(file);
			String table1Str = new String();
			String table2Str = new String();
			for (int i=0; i<size; i++) {
				for (int j=0; j<size; j++) {
					table1Str = table1Str + tableBoardPl1[i][j];
					table2Str = table2Str + tableBoardPl2[i][j];
				}
			}
			fWriter.write(
					(flagPl1==false? "0":"1")+"-"
						+(flagPl2==false? "0":"1")+"-"
						+(doTurn==false? "0":"1")+"-"
						+(endTurn==false? "0":"1")+"-"
						+(onePlayer==false? "0":"1")+"-"
						+(goodPC==false? "0":"1")+"-"
						+nameOfPl1+"-"
						+nameOfPl2+"-"
						+mainPanel.nbOfS1+"-"
						+mainPanel.nbOfS2+"-"
						+mainPanel.nbOfS3+"-"
						+mainPanel.nbOfS4+"-"
						+shoots1+"-"
						+shoots2+"-"
						+shoots1accurate+"-"
						+shoots2accurate+"-"
						+size+"-"
						+xPC+"-"
						+yPC+"-"
						+table1Str+"-"
						+table2Str+"-"
					);
			fWriter.close();
			}
		catch(Exception e){ e.getMessage(); }    
	}
	
}