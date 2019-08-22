package battleship;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseEvent;

public class ChoosePanel extends VBox {
	protected ComboBox<Integer> cbBoard = new ComboBox<Integer>();
	protected ComboBox<Integer> cbSh4 = new ComboBox<Integer>();
	protected ComboBox<Integer> cbSh3 = new ComboBox<Integer>();
	protected ComboBox<Integer> cbSh2 = new ComboBox<Integer>();
	protected ComboBox<Integer> cbSh1 = new ComboBox<Integer>();
	protected ComboBox<String> cbNbOfPlayers = new ComboBox<String>();
	protected ObservableList<Integer> cbSh4Items = cbSh4.getItems(); 
	protected ObservableList<Integer> cbSh3Items = cbSh3.getItems(); 
	protected ObservableList<Integer> cbSh2Items = cbSh2.getItems(); 
	protected ObservableList<Integer> cbSh1Items = cbSh1.getItems();
	protected GridPane options = new GridPane();
	protected TextField tf2PlName;
	
	protected ChoosePanel(MainPanel mainPanel, DataFormat buttonFormat){
		mainPanel.onePlayer=false;
		TextField[] tfOfChoose = {new TextField("Rodzaj gry: "), new TextField("Imie pierwszego gracza: ") , new TextField("Imie pierwszego gracza: "), new TextField("Rozmiar Planszy: "), new TextField("Liczba 4masztowców: "), new TextField("Liczba 3masztowców: "), 
				new TextField("Liczba 2masztowców: "), new TextField("Liczba 1masztowców: ")};
		
		for (Integer i=2; i<21;i++) {cbBoard.getItems().add(i);}
		
		// utworzenie ComboBoxow
		cbNbOfPlayers.getItems().addAll("player vs player","player vs cumputer");
		TextField tf1PlName= new TextField();
		tf2PlName= new TextField();
		cbSh4.getItems().addAll(0);
		cbSh3.getItems().addAll(0);
		cbSh2.getItems().addAll(0,1);
		cbSh1.getItems().addAll(0,1);
		
		ArrayList<ComboBox<Integer>> alOfChoose = new ArrayList<ComboBox<Integer>>();
		//
		
		// dodanie CBow do listy
		alOfChoose.add(cbBoard);
		alOfChoose.add(cbSh4);
		alOfChoose.add(cbSh3);
		alOfChoose.add(cbSh2);
		alOfChoose.add(cbSh1);
		//
		
		Button next = new Button("Rozpocznij gre");
		
		// dynamiczne dodanie do textFieldow nieedytowalnych opisow
		for (int i=0; i<tfOfChoose.length; i++) {
			options.add(tfOfChoose[i], 0,i);
			tfOfChoose[i].setEditable(false);
		}
		
		options.add(cbNbOfPlayers, 1,0);
		options.add(tf1PlName, 1,1);
		options.add(tf2PlName, 1,2);
	
		for (ComboBox<Integer> i : alOfChoose) {options.add(i, 1,alOfChoose.indexOf(i)+3);}
		
		options.add(next, 2,6,1,6);
		
		// wstepny wybor pozycji z CB
		cbNbOfPlayers.getSelectionModel().select(0);
		cbBoard.getSelectionModel().select(0);
		cbSh4.getSelectionModel().select(0);
		cbSh3.getSelectionModel().select(0);
		cbSh2.getSelectionModel().select(0);
		cbSh1.getSelectionModel().select(0);
		
		cbBoard.setOnMouseClicked(new EventHandler<MouseEvent>() {public void handle(MouseEvent me) {setSizeOfBoard();}});
		cbNbOfPlayers.valueProperty().addListener( e -> {setNbOfPlayers(mainPanel);} );
		cbSh4.setOnMouseClicked(new EventHandler<MouseEvent>() {public void handle(MouseEvent me) {choosecbSh4();}});
		cbSh3.setOnMouseClicked(new EventHandler<MouseEvent>() {public void handle(MouseEvent me) {choosecbSh3();}});
		cbSh2.setOnMouseClicked(new EventHandler<MouseEvent>() {public void handle(MouseEvent me) {choosecbSh2();}});
		cbSh1.setOnMouseClicked(new EventHandler<MouseEvent>() {public void handle(MouseEvent me) {choosecbSh1();}});
		cbSh4.setOnMouseReleased(new EventHandler<MouseEvent>() {public void handle(MouseEvent me) {choosecbSh4();}});
		cbSh3.setOnMouseReleased(new EventHandler<MouseEvent>() {public void handle(MouseEvent me) {choosecbSh3();}});
		cbSh2.setOnMouseReleased(new EventHandler<MouseEvent>() {public void handle(MouseEvent me) {choosecbSh2();}});
		cbSh1.setOnMouseReleased(new EventHandler<MouseEvent>() {public void handle(MouseEvent me) {choosecbSh1();}});
		
		// wczytanie kolejnego panelu - setPanel
		next.setOnMouseClicked(new EventHandler<MouseEvent>() {public void handle(MouseEvent me) {
			mainPanel.size=cbBoard.getValue(); 
			mainPanel.menuNBR=0;
			mainPanel.nbOfS1=cbSh1.getValue(); mainPanel.nbOfS2=cbSh2.getValue(); mainPanel.nbOfS3=cbSh3.getValue(); mainPanel.nbOfS4=cbSh4.getValue();
				
			int name1ok = validationOfName(tf1PlName, mainPanel.nameOfPl1, 1, mainPanel);
			int name2ok = validationOfName(tf2PlName, mainPanel.nameOfPl2, 2, mainPanel);
					
			if(name1ok+name2ok==2) {
				if(mainPanel.nbOfS1!=0||mainPanel.nbOfS2!=0||mainPanel.nbOfS3!=0||mainPanel.nbOfS4!=0) {
					mainPanel.getChildren().clear();
					SetPanel setPanel = new SetPanel(mainPanel, buttonFormat);
					mainPanel.getChildren().add(setPanel);
				}
				else {
					MyMessages mm=new MyMessages("Bledna liczba statkow", "Aby rozpoczac gre, musisz wybrac przynajmniej 1 statek");
					mm.alert.showAndWait();
			}}
		}});
				
		options.setAlignment(Pos.TOP_CENTER);
		options.setPadding(new Insets(100,0,0,0));
		options.setHgap(20);
		options.setVgap(20);
	}
	
	/// opcja wyboru rodzaju gry
	private void setNbOfPlayers(MainPanel mainPanel) {
		switch (cbNbOfPlayers.getValue()){
		case "player vs player": 
			mainPanel.onePlayer=false; 
			tf2PlName.setEditable(true);
			break;
		default: 
			mainPanel.onePlayer=true; 
			tf2PlName.setText("Computer");
			tf2PlName.setEditable(false);
			break;
		}
	}
	
	// wstepne ustalenie zawartosci comboboxow
	private void setSizeOfBoard() {
		switch (cbBoard.getValue()){
		case 2:
			cbSh4.getItems().clear(); cbSh4.getItems().addAll(0);
			cbSh3.getItems().clear(); cbSh3.getItems().addAll(0);
			cbSh2.getItems().clear(); cbSh2.getItems().addAll(0,1);
			cbSh1.getItems().clear(); cbSh1.getItems().addAll(0,1);
			break;
		case 3:
			cbSh4.getItems().clear(); cbSh4.getItems().addAll(0);
			cbSh3.getItems().clear(); cbSh3.getItems().addAll(0);
			cbSh2.getItems().clear(); cbSh2.getItems().addAll(0,1);
			cbSh1.getItems().clear(); cbSh1.getItems().addAll(0,1,2);
			break;
		case 4:
			cbSh4.getItems().clear(); cbSh4.getItems().addAll(0,1,2);
			cbSh3.getItems().clear(); cbSh3.getItems().addAll(0,1,2);
			cbSh2.getItems().clear(); cbSh2.getItems().addAll(0,1,2);
			cbSh1.getItems().clear(); cbSh1.getItems().addAll(0,1,2,3,4);
			break;
		case 5:
			cbSh4.getItems().clear(); cbSh4.getItems().addAll(0,1,2);
			cbSh3.getItems().clear(); cbSh3.getItems().addAll(0,1,2);
			cbSh2.getItems().clear(); cbSh2.getItems().addAll(0,1,2);
			cbSh1.getItems().clear(); cbSh1.getItems().addAll(0,1,2,3,4);
			break;
		default:
			cbSh4.getItems().clear(); 
			for (int i=0; i<=(int)(cbBoard.getValue()*cbBoard.getValue()*0.07); i++) {cbSh4.getItems().add(i);	}
			cbSh3.getItems().clear(); 
			for (int i=0; i<=(int)(cbBoard.getValue()*cbBoard.getValue()*0.09); i++) {cbSh3.getItems().add(i);	}
			cbSh2.getItems().clear(); 
			for (int i=0; i<=(int)(cbBoard.getValue()*cbBoard.getValue()*0.12); i++) {cbSh2.getItems().add(i);	}
			cbSh1.getItems().clear(); 
			for (int i=0; i<=(int)(cbBoard.getValue()*cbBoard.getValue()*0.16); i++) {cbSh1.getItems().add(i);	}
			break;
		}
		cbBoard.getSelectionModel().select(0);
		cbSh4.getSelectionModel().select(0);
		cbSh3.getSelectionModel().select(0);
		cbSh2.getSelectionModel().select(0);
		cbSh1.getSelectionModel().select(0);
	}
	////////////////////////
	
	///  	ustalanie wybranej wartosci z comboboxa. jesli uprzednio wybrana wartosc nadal znajduje sie na liscie, to ja zostaw, 
	// 		jezeli lista zmalala ponizej tej wartosci, to ustaw 0
	private void set4(Integer pcbSh4) {
		if (cbSh4Items.indexOf(pcbSh4)==-1) cbSh4.getSelectionModel().select(0);
		else cbSh4.getSelectionModel().select(cbSh4Items.indexOf(pcbSh4));
	}
	
	private void set3(Integer pcbSh3) {
		if (cbSh3Items.indexOf(pcbSh3)==-1) cbSh3.getSelectionModel().select(0);
		else cbSh3.getSelectionModel().select(cbSh3Items.indexOf(pcbSh3));
	}
	
	private void set2(Integer pcbSh2) {
		if (cbSh2Items.indexOf(pcbSh2)==-1) cbSh2.getSelectionModel().select(0);
		else cbSh2.getSelectionModel().select(cbSh2Items.indexOf(pcbSh2));
	}
	
	private void set1(Integer pcbSh1) {
		if (cbSh1Items.indexOf(pcbSh1)==-1) cbSh1.getSelectionModel().select(0);
		else cbSh1.getSelectionModel().select(cbSh1Items.indexOf(pcbSh1));
	}
	///
	
	// algorytm wyliczajacy maxymalna liczbe statkow przy konkretnych wyborach poszczegolnych rodzajow statkow
	// rownolegla aktualizacja wartosci w comboboxach
	// oddzielnie - podanie konkretnych przypadkow dla malych plansz od 2x2 do 5x5 wlacznie
	// dla plansz >5 algorytm powinien podawac max liczbe statkow przy najmniej ekonomicznym rozmieszczeniu (2 pola przerwy)
	private void switch4(int pcbSh4, int pcbSh3, int pcbSh2, int pcbSh1) {
		switch (cbBoard.getValue()){
		case 2:
		case 3:
			cbSh4.getItems().clear(); cbSh4.getItems().addAll(0);
			break;
		case 4:
			if (pcbSh3==0 && pcbSh2==0 && pcbSh1==0) {cbSh4.getItems().clear(); cbSh4.getItems().addAll(0,1,2);}
			else if ((pcbSh3==1 && pcbSh2==0 && pcbSh1==0)||(pcbSh3==0 && pcbSh2==1 && pcbSh1==0)||(pcbSh3==0 && pcbSh2==0 && pcbSh1<3))
					{cbSh4.getItems().clear(); cbSh4.getItems().addAll(0,1);}
				else {cbSh4.getItems().clear(); cbSh4.getItems().addAll(0);}
			break;
		case 5:
			if (pcbSh3==0 && pcbSh2==0 && pcbSh1==0) {cbSh4.getItems().clear(); cbSh4.getItems().addAll(0,1,2);}
			else if ((pcbSh3==1 && pcbSh2==0 && pcbSh1==0)||(pcbSh3==0 && pcbSh2<2 && pcbSh1<2)||(pcbSh3==0 && pcbSh2==0 && pcbSh1<3))
					{cbSh4.getItems().clear(); cbSh4.getItems().addAll(0,1);}
				else {cbSh4.getItems().clear(); cbSh4.getItems().addAll(0);}
			break;
		default:
			cbSh4.getItems().clear(); 
			for (int i=0; i<=(int)((cbBoard.getValue()*cbBoard.getValue()-(pcbSh3*3*100/27+pcbSh2*2*100/24+pcbSh1*100/16))/(4*92/28)); i++) {
				cbSh4.getItems().add(i);
			}
			break;
		}
		set4(pcbSh4);
	}
	
	private void switch3(int pcbSh4, int pcbSh3, int pcbSh2, int pcbSh1) {
		switch (cbBoard.getValue()){
		case 2:
			cbSh3.getItems().clear(); cbSh3.getItems().addAll(0);
			break;
		case 3:
			if (pcbSh4==0 && pcbSh2==0 && pcbSh1==0) {cbSh3.getItems().clear(); cbSh3.getItems().addAll(0,1);}
			else {cbSh3.getItems().clear(); cbSh3.getItems().addAll(0);}
			break;
		case 4:
			if (pcbSh4==0 && pcbSh2==0 && pcbSh1==0) {cbSh3.getItems().clear(); cbSh3.getItems().addAll(0,1,2);}
			else if ((pcbSh4==1 && pcbSh2==0 && pcbSh1==0)||(pcbSh4==0 && pcbSh2==1 && pcbSh1==0)||(pcbSh4==0 && pcbSh2==0 && pcbSh1<3))
					{cbSh3.getItems().clear(); cbSh3.getItems().addAll(0,1);}
				else {cbSh3.getItems().clear(); cbSh3.getItems().addAll(0);}
			break;
		case 5:
			if (pcbSh4==0 && pcbSh2==0 && pcbSh1==0) {cbSh3.getItems().clear(); cbSh3.getItems().addAll(0,1,2);}
			else if ((pcbSh4==1 && pcbSh2==0 && pcbSh1==0)||(pcbSh4==0 && pcbSh2<2 && pcbSh1<2)||(pcbSh4==0 && pcbSh2==0 && pcbSh1<3))
					{cbSh3.getItems().clear(); cbSh3.getItems().addAll(0,1);}
				else {cbSh3.getItems().clear(); cbSh3.getItems().addAll(0);}
			break;
		default:
			cbSh3.getItems().clear(); 
			for (int i=0; i<=(int)((cbBoard.getValue()*cbBoard.getValue()-(pcbSh4*4*92/28+pcbSh2*2*100/24+pcbSh1*100/16))/(3*100/27)); i++) {
				cbSh3.getItems().add(i);	
			}
			break;
		}
		set3(pcbSh3);
	}
	
	private void switch2(int pcbSh4, int pcbSh3, int pcbSh2, int pcbSh1) {
		switch (cbBoard.getValue()){
		case 2:
		case 3:	
			if (pcbSh4==0 && pcbSh3==0 && pcbSh1==0) {cbSh2.getItems().clear(); cbSh2.getItems().addAll(0,1);}
			else {cbSh2.getItems().clear(); cbSh2.getItems().addAll(0);}
			break;
		case 4:
			if (pcbSh4==0 && pcbSh3==0 && pcbSh1==0) {cbSh2.getItems().clear(); cbSh2.getItems().addAll(0,1,2);}
			else if ((pcbSh4==1 && pcbSh3==0 && pcbSh1==0)||(pcbSh4==0 && pcbSh3==1 && pcbSh1==0)||(pcbSh4==0 && pcbSh3==0 && pcbSh1<3))
					{cbSh2.getItems().clear(); cbSh2.getItems().addAll(0,1);}
				else {cbSh2.getItems().clear(); cbSh2.getItems().addAll(0);}
			break;
		case 5:
			if (pcbSh4==0 && pcbSh3==0 && pcbSh1<2) {cbSh2.getItems().clear(); cbSh2.getItems().addAll(0,1,2,3);}
				else if (pcbSh4==0 && pcbSh3==0 && pcbSh1<3){cbSh2.getItems().clear(); cbSh2.getItems().addAll(0,1,2);}
					else if ((pcbSh4<2 && pcbSh3==0 && pcbSh1<2)||(pcbSh4==0 && pcbSh3<2 && pcbSh1<2)||(pcbSh4==0 && pcbSh3==0 && pcbSh1<4))
							{cbSh2.getItems().clear(); cbSh2.getItems().addAll(0,1);}
						else {cbSh2.getItems().clear(); cbSh2.getItems().addAll(0);}
			break;
		default:
			cbSh2.getItems().clear(); 
			for (int i=0; i<(int)((cbBoard.getValue()*cbBoard.getValue()-(pcbSh4*4*92/28+pcbSh3*3*100/27+pcbSh1*100/16))/(2*100/24)); i++) {
				cbSh2.getItems().add(i);
			}
			break;
		}
		set2(pcbSh2);
	}
	
	private void switch1(int pcbSh4, int pcbSh3, int pcbSh2, int pcbSh1) {
		switch (cbBoard.getValue()){
		case 2:
			if (pcbSh4==0 && pcbSh3==0 && pcbSh2==0) {cbSh1.getItems().clear(); cbSh1.getItems().addAll(0,1);}
			else {cbSh1.getItems().clear(); cbSh1.getItems().addAll(0);}
			break;
		case 3:	
			if (pcbSh4==0 && pcbSh3==0 && pcbSh2==0) {cbSh1.getItems().clear(); cbSh1.getItems().addAll(0,1,2);}
			else {cbSh1.getItems().clear(); cbSh1.getItems().addAll(0);}
			break;
		case 4:
			if (pcbSh4==0 && pcbSh3==0 && pcbSh2==0) {cbSh1.getItems().clear(); cbSh1.getItems().addAll(0,1,2,3,4);}
			else if ((pcbSh4==1 && pcbSh3==0 && pcbSh2==0)||(pcbSh4==0 && pcbSh3==1 && pcbSh2==0)||(pcbSh4==0 && pcbSh3==0 && pcbSh2==1))
					{cbSh1.getItems().clear(); cbSh1.getItems().addAll(0,1,2);}
				else {cbSh1.getItems().clear(); cbSh1.getItems().addAll(0);}
			break;
		case 5:
			if (pcbSh4==0 && pcbSh3==0 && pcbSh2==0) {cbSh1.getItems().clear(); cbSh1.getItems().addAll(0,1,2,3,4);}
			else if (pcbSh4==0 && pcbSh3==0 && pcbSh2==1) {cbSh1.getItems().clear(); cbSh1.getItems().addAll(0,1,2,3);}
				else if ((pcbSh4==1 && pcbSh3==0 && pcbSh2==0)||(pcbSh4==0 && pcbSh3==1 && pcbSh2==0)||(pcbSh4==0 && pcbSh3==0 && pcbSh2<3))
						{cbSh1.getItems().clear(); cbSh1.getItems().addAll(0,1,2);}
					else if ((pcbSh4==1 && pcbSh3==0 && pcbSh2<2)||(pcbSh4==0 && pcbSh3==1 && pcbSh2<2)||(pcbSh4==0 && pcbSh3==0 && pcbSh2<4))
							{cbSh1.getItems().clear(); cbSh1.getItems().addAll(0,1);}
						else {cbSh1.getItems().clear(); cbSh1.getItems().addAll(0);}
			break;
		default:
			cbSh1.getItems().clear(); 
			for (int i=0; i<(int)((cbBoard.getValue()*cbBoard.getValue()-(pcbSh4*4*92/28+pcbSh3*3*100/27+pcbSh2*2*100/24))/(100/16)); i++) {
				cbSh1.getItems().add(i);
			}
			break;
		}
		set1(pcbSh1);
	}
	////////////////////
	
	// ustalenie kolejnosci aktualizacji CB w zaleznosci od zmiany wyboru liczby konkretnych statkow
	private void choosecbSh4() {
		Integer pcbSh4 = cbSh4.getValue();
		Integer pcbSh3 = cbSh3.getValue();
		Integer pcbSh2 = cbSh2.getValue();
		Integer pcbSh1 = cbSh1.getValue();
			
		switch4(pcbSh4, pcbSh3, pcbSh2, pcbSh1);
		switch3(pcbSh4, pcbSh3, pcbSh2, pcbSh1);
		switch2(pcbSh4, pcbSh3, pcbSh2, pcbSh1); 
		switch1(pcbSh4, pcbSh3, pcbSh2, pcbSh1); 
	}
	
	private void choosecbSh3() {
		Integer pcbSh4 = cbSh4.getValue();
		Integer pcbSh3 = cbSh3.getValue();
		Integer pcbSh2 = cbSh2.getValue();
		Integer pcbSh1 = cbSh1.getValue();
			
		switch3(pcbSh4, pcbSh3, pcbSh2, pcbSh1); 
		switch2(pcbSh4, pcbSh3, pcbSh2, pcbSh1); 
		switch1(pcbSh4, pcbSh3, pcbSh2, pcbSh1); 
		switch4(pcbSh4, pcbSh3, pcbSh2, pcbSh1);
	}
	
	private void choosecbSh2() {
		Integer pcbSh4 = cbSh4.getValue();
		Integer pcbSh3 = cbSh3.getValue();
		Integer pcbSh2 = cbSh2.getValue();
		Integer pcbSh1 = cbSh1.getValue();
		
		switch2(pcbSh4, pcbSh3, pcbSh2, pcbSh1); 
		switch1(pcbSh4, pcbSh3, pcbSh2, pcbSh1); 
		switch4(pcbSh4, pcbSh3, pcbSh2, pcbSh1); 
		switch3(pcbSh4, pcbSh3, pcbSh2, pcbSh1); 
	}
	
	private void choosecbSh1() {
		Integer pcbSh4 = cbSh4.getValue();
		Integer pcbSh3 = cbSh3.getValue();
		Integer pcbSh2 = cbSh2.getValue();
		Integer pcbSh1 = cbSh1.getValue();
		
		switch1(pcbSh4, pcbSh3, pcbSh2, pcbSh1); 
		switch4(pcbSh4, pcbSh3, pcbSh2, pcbSh1); 
		switch3(pcbSh4, pcbSh3, pcbSh2, pcbSh1); 
		switch2(pcbSh4, pcbSh3, pcbSh2, pcbSh1);
	}
	/////
	
	private boolean ifLetters(TextField name) {
		char[] tab = name.getText().toCharArray();
		ArrayList<Integer> al = new ArrayList<Integer>();
		int[] tabForAL= {134, 136, 141, 143, 151, 152, 157, 162, 164, 165, 168, 169, 171, 189, 190, 224, 227, 228};
		for (int i=65; i<91; i++) al.add(i);
		for (int i=97; i<123; i++) al.add(i);
		for (int i=0; i<tabForAL.length; i++) al.add(tabForAL[i]);
		boolean b=true;
		for(char c: tab) if(!al.contains((int)c)) b=false; 
		return b;
	}
	
	private int validationOfName(TextField name, String mPanelName, int player, MainPanel mainPanel) {
		if (name.getText().equals("")) {
			if (player==1) mainPanel.nameOfPl1="1 Player";
			else mainPanel.nameOfPl2="2 Player";
			 return 1;
			}
		else if (name.getText().toCharArray().length>25){	
			MyMessages mm=new MyMessages("Bledne liczba znakow", "Pole imienia moze zawierac maksymalnie 25 znakow");
			mm.alert.showAndWait(); return 0;}
		else if (ifLetters(name)) {
			if (player==1) mainPanel.nameOfPl1=name.getText();
			else mainPanel.nameOfPl2=name.getText(); 
			return 1;}
		else{	MyMessages mm=new MyMessages("Bledne znaki", "Pole imienia moze zostac wypelnione tylko literami");
				mm.alert.showAndWait(); return 0;}
	}
	
}