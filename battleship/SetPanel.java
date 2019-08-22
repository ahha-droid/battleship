package battleship;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class SetPanel extends GridPane {
	private DataFormat buttonFormat;
	private Button draggingButton;
	protected Integer nrOfFields;
	private int size;
	private boolean vertical;
	protected char[][] tableBoardPl;
	protected char[][] tableBoardPlComp;
	private int nbOfSetsShips=0;
	
	protected SetPanel(MainPanel mainPanel, DataFormat buttonFormat) {
		super();
		setPadding(new Insets(80,0,0,100));
		setHgap(5);
		setVgap(5);
		
		this.buttonFormat = buttonFormat;
		this.size=mainPanel.size;
		tableBoardPl = new char[size][size];
		mainPanel.menuNBR=1;
		
		///plansza
		GridPane boardPl = new GridPane();
		
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				Field field = new Field(Color.CORNSILK);
				tableBoardPl[i][j]='0';
				boardPl.add(field, i, j);
				addDropHandling(field);
			}
		}
		//////
		//// wyglad planszy
		if(mainPanel.set1pl==0) add(new Txt(mainPanel.nameOfPl1+"`s board"), 1, 0);
		else add(new Txt(mainPanel.nameOfPl2+"`s board"), 1, 0);
		
		add(boardPl,1,2);
		NumbersPanel rowOfNumbers = new NumbersPanel(size);
		LettersPanel colOfLetters = new LettersPanel(size);
		add(rowOfNumbers,1,1);
		add(colOfLetters,0,2);
		
		GridPane gpForBtns = new GridPane();
		gpForBtns.setPadding(new Insets(40,0,0,0));
		gpForBtns.setVgap(10);
		Button nextBtn = new Button("Dalej");
		gpForBtns.add(new Txt("To rotate the ship, click RMB or double-click LMB."), 0, 0);
		gpForBtns.add(nextBtn,0,1);
		
		add(gpForBtns,1,3);
		
		GridPane gpForShips = new GridPane();
		gpForShips.setPadding(new Insets(0,0,0,80));
		
		FlowPane ships = new FlowPane();
		ships.setHgap(20);
		ships.setVgap(10);
		gpForShips.add(ships, 1, 1);
		//////
		
		/////// zbuduj statki
		for (int i=0; i<mainPanel.nbOfS1; i++) buildShip(1, ships);
		for (int i=0; i<mainPanel.nbOfS2; i++) buildShip(2, ships);
		for (int i=0; i<mainPanel.nbOfS3; i++) buildShip(3, ships);
		for (int i=0; i<mainPanel.nbOfS4; i++) buildShip(4, ships);
		
		add(gpForShips, 2, 2);
		
		nextBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				if (nbOfSetsShips==(mainPanel.nbOfS1+mainPanel.nbOfS2+mainPanel.nbOfS3+mainPanel.nbOfS4)) {   // jesli rozmieszczono wszystkie statki
					if (mainPanel.set1pl==0) {				//jesli to bylo 1 wywolanie setpanel
						mainPanel.tableBoardPl1=tableBoardPl;
						mainPanel.boardPl1=boardPl;
						mainPanel.set1pl=1;
						mainPanel.getChildren().clear();
						if (mainPanel.onePlayer==true) {	// jesli gra z pc -> przejdx dalej do gamepanel
							mainPanel.boardPl2=compBoard(mainPanel);
							mainPanel.tableBoardPl2=tableBoardPlComp;
							mainPanel.getChildren().clear();
							GamePanel gamePanel = new GamePanel(mainPanel);
							mainPanel.getChildren().add(gamePanel);
						}
						else {								// jesli zas 2 graczy -> wywolaj po raz drugi setpanel
							SetPanel setPanel = new SetPanel(mainPanel, buttonFormat);
							mainPanel.getChildren().add(setPanel);
						}
					}
					else {									// jesli bylo to drugie wywolanie setpanel -> przejdz do gry
						mainPanel.tableBoardPl2=tableBoardPl;
						mainPanel.boardPl2=boardPl;
						mainPanel.getChildren().clear();
						GamePanel gamePanel = new GamePanel(mainPanel);
						mainPanel.getChildren().add(gamePanel);
					}
				}
				else {
					MyMessages mm=new MyMessages("pozostaly nierozmieszczone statki", "Aby rozpoczac gre, musisz rozmiescic wszystkie statki");
					mm.alert.showAndWait();
				}
			}
		});
		
	}
	
	// budowanie statku
	private void buildShip(int i, FlowPane ships) {
		Ship ship = new Ship(i);
		ships.getChildren().add(ship);
		dragShip(ship);
	}
	
	// przeciaganie statku
	private void dragShip(Ship ship) {
		ship.setOnDragDetected(e -> {
		 Dragboard db = ship.startDragAndDrop(TransferMode.MOVE);
		 db.setDragView(ship.snapshot(null, null)); 
		 ClipboardContent cc = new ClipboardContent();
		 cc.put(buttonFormat, " "); 
		 db.setContent(cc); 
		 draggingButton = ship;	
		 nrOfFields = ship.getNrOfFields();
		 this.vertical=ship.vertical;
    });}
	
	@SuppressWarnings("static-access")
	private void addDropHandling(Field field) {
		
		 field.setOnDragEntered(e ->{Dragboard db = e.getDragboard();
			if ((field.getFill().equals(Color.CORNSILK))
					&&db.hasContent(buttonFormat) && draggingButton != null) {field.setFill(Color.ALICEBLUE);}});
		 
		 field.setOnDragExited(e -> {
		 	if (field.getFill().equals(Color.ALICEBLUE))field.setFill(Color.CORNSILK);});
		 
		 field.setOnDragOver(e -> {Dragboard db = e.getDragboard();
            if (db.hasContent(buttonFormat) && draggingButton != null) {e.acceptTransferModes(TransferMode.MOVE);}});

		 field.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
			int dropCol = (((GridPane) field.getParent()).getColumnIndex(field)); ///
			int dropRow = (((GridPane) field.getParent()).getRowIndex(field));////
            
            if (vertical==false) {
            	if ((nrOfFields+dropCol<=size)&&(db.hasContent(buttonFormat))){
            		if (checkFreeNeighbors(dropCol, dropRow, tableBoardPl)) {
		            	drop(field);
		            	e.setDropCompleted(true);
		            	dropHorizontal(field, dropCol, dropRow, tableBoardPl);
		            	fillFreeNeighbors(dropCol, dropRow, field, tableBoardPl);
		            	nbOfSetsShips++;
            		}
	            }
            }
            else {
	            if ((nrOfFields+dropRow<=size) &&(db.hasContent(buttonFormat))) {
	            	if (checkFreeNeighbors(dropCol, dropRow, tableBoardPl)) {
	            		drop(field);
		            	e.setDropCompleted(true);
		            	dropVertical(field, dropCol, dropRow, tableBoardPl);
		            	fillFreeNeighbors(dropCol, dropRow, field, tableBoardPl);
		            	nbOfSetsShips++;
	            	}
	            }
            }
            
            if (field.getFill().equals(Color.ALICEBLUE)) field.setFill(Color.CORNSILK);
    });}
	
	private void drop(Field field) {
		((Pane)draggingButton.getParent()).getChildren().remove(draggingButton);
	}
	
	private void dropVertical(Field field, int dropCol, int dropRow, char[][] tableBoard) {
		for (int i=0; i<nrOfFields; i++) {
        	((GridPane) field.getParent()).add(new Field(Color.SADDLEBROWN), dropCol, dropRow+i);
        	tableBoard[dropRow+i][dropCol]='X';
        }
        draggingButton = null;
	}
	
	private void dropHorizontal(Field field, int dropCol, int dropRow, char[][] tableBoard) {
		for (int i=0; i<nrOfFields; i++) {
        	((GridPane) field.getParent()).add(new Field(Color.SADDLEBROWN), dropCol+i, dropRow);
        	tableBoard[dropRow][dropCol+i]='X';
        }
        draggingButton = null;
	}
	
	private void fillFreeNeighbors(int dropCol, int dropRow, Field field, char[][] tableBoard) {
		boolean topRow=(dropRow!=0);
		boolean bottomRow=(vertical==true? dropRow!=size-nrOfFields : dropRow!=size-1);
		boolean leftCol=(dropCol!=0);
		boolean rightCol=(vertical==true? dropCol!=size-1 : dropCol!=size-nrOfFields);
			
		if (topRow) {
			if(vertical) {
				tableBoard[dropRow-1][dropCol]='*';
				((GridPane) field.getParent()).add(new Field(Color.ANTIQUEWHITE), dropCol, dropRow-1);
			}
			else for (int i=0; i<nrOfFields; i++) {
				tableBoard[dropRow-1][dropCol+i]='*';
				((GridPane) field.getParent()).add(new Field(Color.ANTIQUEWHITE), dropCol+i, dropRow-1);
			}
		}
		if (bottomRow) {
			if(vertical) {
				tableBoard[dropRow+nrOfFields][dropCol]='*';
				((GridPane) field.getParent()).add(new Field(Color.ANTIQUEWHITE), dropCol, dropRow+nrOfFields);
			}
			else for (int i=0; i<nrOfFields; i++) {
				tableBoard[dropRow+1][dropCol+i]='*';	
				((GridPane) field.getParent()).add(new Field(Color.ANTIQUEWHITE), dropCol+i, dropRow+1);
			}
		}
		if (leftCol) {
			if(vertical) for (int i=0; i<nrOfFields; i++) {
				tableBoard[dropRow+i][dropCol-1]='*';
				((GridPane) field.getParent()).add(new Field(Color.ANTIQUEWHITE), dropCol-1, dropRow+i);
			}
			else {
				tableBoard[dropRow][dropCol-1]='*';
				((GridPane) field.getParent()).add(new Field(Color.ANTIQUEWHITE), dropCol-1, dropRow);
			}
		}
		if (rightCol) {
			if(vertical) for (int i=0; i<nrOfFields; i++) {
				tableBoard[dropRow+i][dropCol+1]='*';
				((GridPane) field.getParent()).add(new Field(Color.ANTIQUEWHITE), dropCol+1, dropRow+i);
			}
			else {
				tableBoard[dropRow][dropCol+nrOfFields]='*';
				((GridPane) field.getParent()).add(new Field(Color.ANTIQUEWHITE), dropCol+nrOfFields, dropRow);
			}
		}
		if (topRow&&leftCol) {
			tableBoard[dropRow-1][dropCol-1]='*';
			((GridPane) field.getParent()).add(new Field(Color.ANTIQUEWHITE), dropCol-1, dropRow-1);
		}
		if (topRow&&rightCol) {
			if(vertical) {
				tableBoard[dropRow-1][dropCol+1]='*';	
				((GridPane) field.getParent()).add(new Field(Color.ANTIQUEWHITE), dropCol+1, dropRow-1);
			}
			else {
				tableBoard[dropRow-1][dropCol+nrOfFields]='*';
				((GridPane) field.getParent()).add(new Field(Color.ANTIQUEWHITE), dropCol+nrOfFields, dropRow-1);
			}
		}
		if (bottomRow&&leftCol) {
			if(vertical) {
				tableBoard[dropRow+nrOfFields][dropCol-1]='*';	
				((GridPane) field.getParent()).add(new Field(Color.ANTIQUEWHITE), dropCol-1, dropRow+nrOfFields);
			}
			else {
				tableBoard[dropRow+1][dropCol-1]='*';
				((GridPane) field.getParent()).add(new Field(Color.ANTIQUEWHITE), dropCol-1, dropRow+1);
			}
		}
		if (bottomRow&&rightCol) {
			if(vertical) {
				tableBoard[dropRow+nrOfFields][dropCol+1]='*';	
				((GridPane) field.getParent()).add(new Field(Color.ANTIQUEWHITE), dropCol+1, dropRow+nrOfFields);
			}
			else {
				tableBoard[dropRow+1][dropCol+nrOfFields]='*';	
				((GridPane) field.getParent()).add(new Field(Color.ANTIQUEWHITE), dropCol+nrOfFields, dropRow+1);
			}
		}
	}
	
	private boolean checkFreeNeighbors(int dropCol, int dropRow, char[][] tableBoard) {
		boolean b=true;
			
		if(vertical) {for(int i=0; i<nrOfFields; i++)if (tableBoard[dropRow+i][dropCol]!='0') b=false;	}
		else {for(int i=0; i<nrOfFields; i++) if (tableBoard[dropRow][dropCol+i]!='0') b=false;}
		
		return b;
	}
		
	private GridPane compBoard(MainPanel mainPanel) {
		GridPane compBoard = new GridPane();
		int nbOfSetShip=0; // number of sets ships
		int s1=mainPanel.nbOfS1, s2=mainPanel.nbOfS2, s3=mainPanel.nbOfS3, s4=mainPanel.nbOfS4;
		tableBoardPlComp= new char[size][size];
		
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				Field field = new Field(Color.CORNSILK);
				tableBoardPlComp[i][j]='0';
				compBoard.add(field, i, j);
			}
		}
		
		while (nbOfSetShip<mainPanel.nbOfS1+mainPanel.nbOfS2+mainPanel.nbOfS3+mainPanel.nbOfS4) { // finish when every ship is set
			int confirmShipSet = 0;  
			
			while (confirmShipSet==0) {  // increase when ship is successful set 
				int x = (int)(Math.random()*size);
				int y = (int)(Math.random()*size);
				int v = (int)(Math.random()*2);
				vertical=(v==0? true:false);
				int confirmShipLength=0;
				
				while (confirmShipLength==0) {
					nrOfFields = (int)(Math.random()*4)+1;
					switch (nrOfFields) {  // 
						case 1: if (s1>0) {confirmShipLength=1;}	break;
						case 2: if (s2>0) {confirmShipLength=1;}	break;
						case 3: if (s3>0) {confirmShipLength=1;}	break;
						default:if (s4>0) {confirmShipLength=1;}	break;
					}
				}
				
				if (vertical==false) {
			       	if (nrOfFields+x<=size){
			      		if (checkFreeNeighbors(x, y, tableBoardPlComp)) {
					       	dropHorizontal(getNodeByRowColumnIndex(x, y, compBoard), x, y, tableBoardPlComp);
				          	fillFreeNeighbors(x, y, getNodeByRowColumnIndex(x, y, compBoard), tableBoardPlComp);
					       	nbOfSetShip++;
					       	confirmShipSet=1;
					       	switch (nrOfFields) {  
								case 1:  {s1--; }	break;
								case 2:  {s2--; }	break;
								case 3:  {s3--; }	break;
								default: {s4--; }	break;
							}
			       		}
			       	}
			    }
			    else {
				    if (nrOfFields+y<=size)  {
				        if (checkFreeNeighbors(x, y, tableBoardPlComp)) {
					        dropVertical(getNodeByRowColumnIndex(x, y, compBoard), x, y, tableBoardPlComp);
					        fillFreeNeighbors(x, y, getNodeByRowColumnIndex(x, y, compBoard), tableBoardPlComp);
					        nbOfSetShip++;
					        confirmShipSet=1;
					        switch (nrOfFields) {  
								case 1:  {s1--; }	break;
								case 2:  {s2--; }	break;
								case 3:  {s3--; }	break;
								default: {s4--; }	break;
							}
				        }
				    }
			    }
			}
		}
		return compBoard;
	}
	
	@SuppressWarnings("static-access")
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
	
}