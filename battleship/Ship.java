package battleship;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseButton;

public class Ship extends Button {
	protected boolean vertical;
    private Integer nrOfFields;
	
    protected Integer getNrOfFields() {return nrOfFields;}

	protected void setNrOfFields(Integer nrOfFields) {this.nrOfFields = nrOfFields;}

	protected Ship(int x) {
		super();
		nrOfFields = x;
		setPrefHeight(17);
			setPrefWidth(x*23);
		setBackground(new Background(new BackgroundFill(Color.SADDLEBROWN, new CornerRadii(10), new Insets(5))));
		vertical=false;
		
		setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){  // przy podwojnym kliknieciu LPM
		            if((mouseEvent.getClickCount() == 2)){
		                rotate(x);
		            }
		        }
		        if(mouseEvent.getButton().equals(MouseButton.SECONDARY)){ // przy kliknieciu RPM
		        	rotate(x);
		        }
		    }
		});
	}
	
	// obrot statku
	private void rotate(int x) {
		if (vertical==false) {
            setPrefHeight(x*23);
			setPrefWidth(23);
			vertical=true;
		}
		else {
			setPrefHeight(17);
			setPrefWidth(x*23);
			vertical=false;
		}
	}
		
}