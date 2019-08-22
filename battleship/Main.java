package battleship;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Main extends Application {
	private DataFormat buttonFormat = new DataFormat("MyButton");
		
	public static void main(String[] args){launch(args);}

	public void start(Stage stage) throws Exception {
		
		MainPanel mainPanel = new MainPanel();
		mainPanel.setPrefSize(1000, 1400);
		mainPanel.menuNBR=0;
		mainPanel.stage=stage;
		
		final Menu menu1 = new Menu("GRA");
		final Menu menu2 = new Menu("WYGLAD");
		final Menu menu3 = new Menu("ROZMIESZCZONE STATKI");
		final Menu menu4 = new Menu("OBECNA GRA");
		
		MenuItem menu11 = new MenuItem("Rozpocznij nowa gre");
		ColorMenuItem whiteMI = new ColorMenuItem(Color.GHOSTWHITE, "Bia³y", mainPanel);
		ColorMenuItem blueMI = new ColorMenuItem(Color.ROYALBLUE, "Niebieski", mainPanel);
		ColorMenuItem greenMI = new ColorMenuItem(Color.LIMEGREEN, "Zielony", mainPanel);
		ColorMenuItem redMI = new ColorMenuItem(Color.TOMATO, "Czerwony", mainPanel);
		ColorMenuItem turqMI = new ColorMenuItem(Color.TURQUOISE, "Turkusowy", mainPanel);
		MenuItem menu31 = new MenuItem("Resetuj rozmieszcznie");
		MenuItem menu41 = new MenuItem("Zapisz");
		MenuItem menu12 = new MenuItem("Wczytaj");
		mainPanel.menu41=menu41;
		mainPanel.menu12=menu12;
		
		menu1.getItems().add(menu11);
		menu2.getItems().addAll(whiteMI.menu,  blueMI.menu, greenMI.menu, redMI.menu ,turqMI.menu);
		menu3.getItems().add(menu31);
		menu4.getItems().add(menu41);
		menu1.getItems().add(menu12);
				
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(menu1);
		menuBar.getMenus().add(menu2);
		menuBar.getMenus().add(menu3);
		menuBar.getMenus().add(menu4);
		
		HBox mainMenu = new HBox();
		mainMenu.getChildren().addAll(menuBar);
		
		VBox root = new VBox();
		root.getChildren().addAll(mainMenu, mainPanel);
		
		Scene scene = new Scene(root, 1100, 600);
		
		stage.setTitle("Gra w Statki // BattleShip Game");
		stage.setScene(scene);
		stage.show();
		
		// Menu 1 - opcja rozpocznij nowa gre
		menu11.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				mainPanel.getChildren().clear();
				if (mainPanel.set1pl==1) mainPanel.set1pl=0;
				ChoosePanel choosePanel = new ChoosePanel(mainPanel, buttonFormat);
				mainPanel.getChildren().add(choosePanel.options);
			}
		});
		
		// Menu 3 - zresetuj ustawienie statkow
		menu31.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				if (mainPanel.menuNBR==1) {
					mainPanel.getChildren().clear();
					SetPanel setPanel = new SetPanel(mainPanel, buttonFormat);
					mainPanel.getChildren().add(setPanel);
				}
				else {
					MyMessages mm=new MyMessages("opcja dostepna podczas rozmieszczania statkow", "Aby zresetowac rozmieszczenie statkow, musisz najpierw przejsc do panelu rozmieszczania statkow");
					mm.alert.showAndWait();
				}
			}
		});
		
		//zapisz
		mainPanel.menu41.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
					MyMessages mm=new MyMessages("opcja dostepna podczas gry", "Aby zapisac stan gry, musisz najpierw ja rozpoczac");
					mm.alert.showAndWait();
			}
		});
		
		// menu 4 - wczytaj
		menu12.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e){
				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
				
				fileChooser.getExtensionFilters().add(extFilter);
				fileChooser.setTitle("Wczytaj gre");
				File file = fileChooser.showOpenDialog(mainPanel.stage);
		            
				if(file != null){try {
					LoadFile(mainPanel, file);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}}
			}
		});
	}	

	protected void load(MainPanel mainPanel) throws IOException {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Wczytaj gre");
		File file = fileChooser.showOpenDialog(mainPanel.stage);
            
		if(file != null){LoadFile(mainPanel, file);}
	}	

	public void LoadFile(MainPanel mainPanel, File file) throws IOException {
		System.out.println(file.toString()+"\t1");
		try {
			System.out.println(file.toString()+"\t2");
			Scanner scaner = new Scanner(file);
				String input = scaner.next();
				String data[] = input.split("-");
				
				mainPanel.flagPl1=(Integer.parseInt(data[0])==0? false:true);
				mainPanel.flagPl2=(Integer.parseInt(data[1])==0? false:true);
				mainPanel.doTurn=(Integer.parseInt(data[2])==0? false:true);
				mainPanel.endTurn=(Integer.parseInt(data[3])==0? false:true);
				mainPanel.onePlayer=(Integer.parseInt(data[4])==0? false:true);
				mainPanel.goodPC=(Integer.parseInt(data[5])==0? false:true);
				mainPanel.nameOfPl1=data[6];
				mainPanel.nameOfPl2=data[7];
				mainPanel.nbOfS1=Integer.parseInt(data[8]);
				mainPanel.nbOfS2=Integer.parseInt(data[9]);
				mainPanel.nbOfS3=Integer.parseInt(data[10]);
				mainPanel.nbOfS4=Integer.parseInt(data[11]);
				mainPanel.shoots1=Integer.parseInt(data[12]);
				mainPanel.shoots2=Integer.parseInt(data[13]);
				mainPanel.shoots1accurate=Integer.parseInt(data[14]);
				mainPanel.shoots2accurate=Integer.parseInt(data[15]);
				mainPanel.size=Integer.parseInt(data[16]);
				mainPanel.xPC=Integer.parseInt(data[17]);
				mainPanel.yPC=Integer.parseInt(data[18]);
				
				mainPanel.tableBoardPl1 = new char[mainPanel.size][mainPanel.size];
				mainPanel.tableBoardPl2 = new char[mainPanel.size][mainPanel.size];
				
				char data1[] = data[19].toCharArray();
				char data2[] = data[20].toCharArray();
				
				for (int i=0; i<mainPanel.size; i++) {
					for (int j=0; j<mainPanel.size; j++) {
						mainPanel.tableBoardPl1[i][j]=data1[i*mainPanel.size+j];
						mainPanel.tableBoardPl2[i][j]=data2[i*mainPanel.size+j];
					}
				}
			
			scaner.close();
			mainPanel.load=1;
			GamePanel gamePanel = new GamePanel(mainPanel);
			mainPanel.getChildren().clear();
			mainPanel.getChildren().add(gamePanel);
			gamePanel.setVisible(true);
			mainPanel.load=0;
			
		} catch(Exception e){ e.getMessage(); } 
	}
	
}