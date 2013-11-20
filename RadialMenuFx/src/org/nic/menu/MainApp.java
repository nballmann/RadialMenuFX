package org.nic.menu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {

    private boolean pressed = false;

    private Stage primaryStage;
    
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
					initRadialMenu(event.getScreenX()-250, event.getScreenY()-250);
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

	Scene scene = new Scene(pane);
	scene.setFill(null);

	primaryStage.setTitle("Title");
	primaryStage.setScene(scene);
	primaryStage.setResizable(false);
	primaryStage.show();



	//	    pane.getChildren().addAll(RadialMenu.outerCircleSegment(
	//		    250, 250, 200, 100, 12, 
	//		    Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)), 
	//		    Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255))));
    }


    private void initRadialMenu(double posX, double posY)
    {
	final Stage stage = new Stage(StageStyle.TRANSPARENT);
	stage.setResizable(false);
	stage.initOwner(primaryStage);
	stage.setX(posX);
	stage.setY(posY);
	
	final RadialMenu menu = new RadialMenu(250, 250, 200, 100, 6, Color.RED, Color.BLACK);
	
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

	for(final Path p : menu.getSegments())
	{
	    p.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent e)
		{
		    p.setFill(Color.WHITE);
		}

	    });

	    p.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent e)
		{
		    p.setFill(Color.BLACK);
		}

	    });
	}


	Scene scene = new Scene(menu);
	scene.setFill(null);
	
	stage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

	    @Override
	    public void handle(MouseEvent e)
	    {
		if( e.getSource() == menu )
		{
		    stage.close();
		}
	    }

	});


	stage.setScene(scene);
	stage.show();
    }



    public static void main(String[] args) {
	launch(args);
    }
}
