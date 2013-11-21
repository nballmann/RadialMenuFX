package org.nic.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

/**
 * Extension PopUp-Menu for JavaFX 2.x
 * 
 * 
 * @author N.Ballmann
 *
 */
public class RadialMenu extends Region
{
    private double centerX, centerY, outerRadius, innerRadius;
    private int segmentCount;
    private Color strokeColor, fillColor;
    
    private ObservableList<Group> segments = FXCollections.observableArrayList();
    
    /**
     * getter for the ObservableList representing this RadialMenus ring-segment-nodes
     * @return the segments
     */
    public ObservableList<Group> getSegments()
    {
        return segments;
    }

    /**
     * setter for the ObservableList representing this RadialMenus ring-segment-nodes
     * @param segments the ObservableList to set
     */
    public void setSegments(ObservableList<Group> segments)
    {
        this.segments = segments;
    }

    /**
     * Creates a new RadialMenu Node<br>
     * Listeners can be registered onto the Group-Nodes inside of the ObservableList<br>
     * retrievable by its getter {@link getSegments}
     * @param centerX the horizontal center of the node inside its {@link Scene} 
     * @param centerY the vertical center of the node inside its {@link Scene} 
     * @param outerRadius the outer radius of the {@linkRadialMenu}
     * @param innerRadius the inner radius of the {@linkRadialMenu}
     * @param segmentCount number of segments/click-able parts inside the RadialMenu
     * @param strokeColor the outside stroke color of the RadialMenu
     * @param fillColor the fill color of the RadialMenu
     */
    public RadialMenu(double centerX, double centerY, double outerRadius, double innerRadius, int segmentCount, Color strokeColor, Color fillColor)
    {
	this.centerX = centerX;
	this.centerY = centerY;
	this.outerRadius = outerRadius;
	this.innerRadius = innerRadius;
	this.segmentCount = segmentCount;
	this.strokeColor = strokeColor;
	this.fillColor = fillColor;
	
	this.setPrefSize(outerRadius*2.3, outerRadius*2.3);
	
	createOuterCircleSegments();
	
	this.getChildren().addAll(segments);
    }

    /**
     * computes the circular ring segments specified by segmentCount 
     * through translation of vector coordinates into local coordinate-space at
     * application runtime <br>
     * Each segment and its associated symbol will be added to a 
     * group-node which will be put into an ObservableList of type Group: segments <br>
     * @see {@link segments}, {@link getSegments}, {@link setSegments}
     */
    private void createOuterCircleSegments()
    {
	double segmentRadians = 360/segmentCount*Math.PI/180;

	for(int i = 2; i <= segmentCount+1; i++)
	{
	    Group g = new Group();
	    
	    Path p = new Path();
	    p.setFill(fillColor);
	    p.setStroke(strokeColor);
	    p.setFillRule(FillRule.EVEN_ODD);
	    
	    MoveTo firstPoint = new MoveTo();
	    firstPoint.setX(centerX + innerRadius*Math.cos(segmentRadians*(i-1)));
	    firstPoint.setY(centerY + innerRadius*Math.sin(segmentRadians*(i-1)));
	    
	    p.getElements().add(firstPoint);
	    
	    LineTo nextLine = new LineTo();
	    nextLine.setX(centerX + outerRadius*Math.cos(segmentRadians*(i-1)));
	    nextLine.setY(centerY + outerRadius*Math.sin(segmentRadians*(i-1)));
	    
	    ArcTo outerArc = new ArcTo();
	    outerArc.setSweepFlag(true);
	    outerArc.setAbsolute(true);
	    outerArc.setX(centerX + outerRadius*Math.cos(2*Math.PI + segmentRadians*i));
	    outerArc.setY(centerY + outerRadius*Math.sin(2*Math.PI + segmentRadians*i));
	    outerArc.setRadiusX(outerRadius);
	    outerArc.setRadiusY(outerRadius);

	    LineTo line2 = new LineTo();
	    line2.setX(centerX + innerRadius*Math.cos(segmentRadians*(i)));
	    line2.setY(centerY + innerRadius*Math.sin(segmentRadians*(i)));
	    
	    ArcTo innerArc = new ArcTo();
	    innerArc.setSweepFlag(false);
	    innerArc.setAbsolute(true);
	    innerArc.setX(centerX + innerRadius*Math.cos(2*Math.PI + segmentRadians*(i-1)));
	    innerArc.setY(centerY + innerRadius*Math.sin(2*Math.PI + segmentRadians*(i-1)));
	    innerArc.setRadiusX(innerRadius);
	    innerArc.setRadiusY(innerRadius);
	    
	    MoveTo end = new MoveTo();
	    end.setX(centerX + innerRadius*Math.cos(segmentRadians*(i)));
	    end.setY(centerY + innerRadius*Math.sin(segmentRadians*(i)));
	    
	    p.getElements().add(nextLine);
	    p.getElements().add(outerArc);
	    p.getElements().add(line2);
	    p.getElements().add(innerArc);
	    p.getElements().add(end);
	    
	    g.getChildren().add(p);
	    
	    
	    // temporary: for button layout injection
	    Rectangle rect = new Rectangle();
	    rect.setX(centerX-((outerRadius-innerRadius)*0.35) + (innerRadius + (outerRadius - innerRadius)/2)*Math.cos(segmentRadians*(i-.5)));
	    rect.setY(centerY-((outerRadius-innerRadius)*0.35) + (innerRadius + (outerRadius - innerRadius)/2)*Math.sin(segmentRadians*(i-.5)));
	    rect.setWidth(((outerRadius-innerRadius)*0.7));
	    rect.setHeight(((outerRadius-innerRadius)*0.7));
	    rect.setRotate(Math.toDegrees(segmentRadians*(i-.5)));
	    rect.setFill(Color.RED);
	    
	    g.getChildren().add(rect);
	    
	    segments.add(g);
	}

    }
}
