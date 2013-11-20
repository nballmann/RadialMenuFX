package org.nic.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class RadialMenu extends Region
{
    private double centerX, centerY, outerRadius, innerRadius;
    private int segmentCount;
    private Color strokeColor, fillColor;
    
    private ObservableList<Path> segments = FXCollections.observableArrayList();
    
    /**
     * @return the segments
     */
    public ObservableList<Path> getSegments()
    {
        return segments;
    }

    /**
     * @param segments the segments to set
     */
    public void setSegments(ObservableList<Path> segments)
    {
        this.segments = segments;
    }

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

    private void createOuterCircleSegments()
    {
	double segmentRadians = 360/segmentCount*Math.PI/180;

	for(int i = 2; i <= segmentCount+1; i++)
	{
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
	    
	    segments.add(p);
	}

    }
}
