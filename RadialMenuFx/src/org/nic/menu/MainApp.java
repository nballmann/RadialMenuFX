package org.nic.menu;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class MainApp extends Application {

    private static final int SEGMENT_COUNT = 6;
    
    private boolean pressed = false;

    private Stage primaryStage;
    private Stage menuStage;

    private ScaleTransition scaleIn;

    @Override
    public void start(Stage primaryStage) {

	this.primaryStage = primaryStage;

	AnchorPane pane = new AnchorPane();
	pane.setPrefSize(700, 700);

	pane.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(final MouseEvent event)
	    {
		new Thread(new Runnable() {

		    @Override
		    public void run()
		    {
			pressed = true;
			
			while(pressed)
			{
			    try
			    {
				Thread.sleep(900);
			    } catch (InterruptedException e) {}

			    if(pressed)
			    {
				Platform.runLater(new Runnable() {

				    @Override
				    public void run()
				    {
					showMenu(event.getScreenX()-(menuStage.getWidth()/2), event.getScreenY()-(menuStage.getHeight()/2));

					System.out.println(menuStage.getWidth()/2 + " : " + menuStage.getHeight()/2);
					pressed = false;
				    }

				});
			    }
			}
		    }

		}).start();
	    }

	});

	pane.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(MouseEvent event)
	    {
		pressed = false;
	    }

	});

	initRadialMenu();

	Scene scene = new Scene(pane);
	scene.setFill(null);

	primaryStage.setTitle("Title");
	primaryStage.setScene(scene);
	primaryStage.setResizable(false);
	primaryStage.show();

	scaleIn = new ScaleTransition(Duration.millis(300));
	scaleIn.setFromX(0);
	scaleIn.setFromY(0);
	scaleIn.setToX(1.0);
	scaleIn.setToY(1.0);
    }

    private void initRadialMenu()
    {
	final Stage stage = new Stage(StageStyle.TRANSPARENT);
	stage.setResizable(false);
	stage.setWidth(300);
	stage.setHeight(300);
	//	stage.initModality(Modality.APPLICATION_MODAL);
	stage.initOwner(primaryStage);

	final RadialMenu menu = new RadialMenu(150, 150, 150, 80, SEGMENT_COUNT, Color.RED, Color.BLACK);

	menu.setRotate(360/SEGMENT_COUNT*(SEGMENT_COUNT*0.57));
	menu.focusTraversableProperty().set(true);
	menu.focusedProperty().addListener(new ChangeListener<Boolean>() {

	    @Override
	    public void changed(ObservableValue<? extends Boolean> obs,
		    Boolean oldValue, Boolean newValue)
	    {
		if(!newValue)
		{
		    Platform.runLater(new Runnable() {

			@Override
			public void run()
			{
			    stage.close();
			}

		    });
		}
	    }

	});

	for(final Group g : menu.getSegments())
	{
	    g.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent e)
		{
		    if(g.equals(menu.getSegments().get(0)))
		    {
			((Path) g.getChildren().get(0)).setFill(Color.BLUE);
		    }
		    else
			((Path) g.getChildren().get(0)).setFill(Color.WHITE);
		}

	    });

	    g.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent e)
		{
		    ((Path) g.getChildren().get(0)).setFill(Color.BLACK);
		}

	    });
	}

	final Scene scene = new Scene(menu);
	scene.setFill(null);

	stage.setOnShowing(new EventHandler<WindowEvent>() {

	    @Override
	    public void handle(WindowEvent e)
	    {
		scaleIn.setNode(menu);
		scaleIn.play();		
	    }

	});

	stage.setScene(scene);
	menuStage = stage;
    }

    private void showMenu(double posX, double posY)
    {
	menuStage.setX(posX);
	menuStage.setY(posY);
	menuStage.show();
    }

    public static void main(String[] args) {
	launch(args);
    }
}
