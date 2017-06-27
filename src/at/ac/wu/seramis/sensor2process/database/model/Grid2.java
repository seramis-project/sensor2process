package at.ac.wu.seramis.sensor2process.database.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Grid2 extends Region
{
	private int lengthX = 0, lengthY = 0;
	private int borderX = 5, borderY = 5; // also the minimum distance of the grid to the border of the canvas
//	private double startX = 0, startY = 0;
	private double distX = 0, distY = 0;
	
	public Grid2(int width, int height, int lengthX, int lengthY, boolean showBorder, ShopMap showMap)
	{
		this.setWidth(width);
		this.setHeight(height);
		this.setMaxWidth(width);
		this.setMaxHeight(height);
		
		this.lengthX = lengthX;
		this.lengthY = lengthY;
				
		this.computeGrid(this.getWidth(), this.getHeight());
		this.displayMap(this.getWidth(), this.getHeight(), showMap);
		this.displayGrid(this.getWidth(), this.getHeight());
		if(showBorder) this.displayBorder(this.getWidth(), this.getHeight());
	}
	
	private void computeGrid(double width, double height)
	{
		while((width - 2 * borderX) % (lengthX + 1) != 0)
		{
			borderX++;
		}
		
		while((height - 2 * borderY) % (lengthY + 1) != 0) 
		{
			borderY++;
		}
		
		this.distX = (width - 2 * borderX) / (lengthX + 1);
		this.distY = (height - 2 * borderY) / (lengthY + 1);
	}
		
	private void displayMap(double width, double height, ShopMap showMap)
	{
		if(showMap == ShopMap.ITALY)
		{
			Image plan = new Image(getClass().getResourceAsStream("/rfidanalysis/visualization/img/plan.png"));

			ImageView planView = new ImageView(plan);
			planView.setX(-97);
			planView.setY(-45);
			planView.setFitWidth(1012);
			planView.setFitHeight(948);
			planView.setOpacity(0.5);
			planView.setClip(new Rectangle(0,0,width,height));
			
			this.getChildren().add(planView);
			
//			Polygon fitting1 = new Polygon(371, 116, 410, 116, 410, 240, 371, 240);
//			Polygon fitting2 = new Polygon(608, 116, 646, 116, 646, 240, 608, 240);
//			Polygon fitting3 = new Polygon(490, 383, 529, 383, 529, 503, 490, 503);
//			Polygon fitting4 = new Polygon(371, 655, 410, 655, 410, 780, 371, 780);
//			Polygon fitting5 = new Polygon(608, 655, 646, 655, 646, 780, 608, 780);
//			
//			fitting1.setFill(Color.rgb(255, 0, 0, 0.4));
//			fitting2.setFill(Color.rgb(255, 0, 0, 0.4));
//			fitting3.setFill(Color.rgb(255, 0, 0, 0.4));
//			fitting4.setFill(Color.rgb(255, 0, 0, 0.4));
//			fitting5.setFill(Color.rgb(255, 0, 0, 0.4));
//			
//			this.getChildren().addAll(fitting1, fitting2, fitting3, fitting4, fitting5);
		}
		else if(showMap == ShopMap.VOESENDORF)
		{
//			WebView plan = new WebView();
//			plan.getEngine().load(getClass().getResource("/rfidanalysis/visualization/img/voesendorf.svg").toExternalForm());
//			
//			this.getChildren().add(plan);
			
			Image plan = new Image(getClass().getResourceAsStream("/rfidanalysis/visualization/img/voesendorf.png"));

			ImageView planView = new ImageView(plan);
//			planView.setX(-300);
//			planView.setY(-220);
			planView.setFitWidth(this.getWidth());
			planView.setFitHeight(this.getHeight());
			planView.setOpacity(0.5);
			planView.setClip(new Rectangle(0,0,width,height));
			
			this.getChildren().add(planView);
		}
	}
	
	private void displayBorder(double width, double height)
	{		
		Line lTop = new Line(this.borderX + 0, 
				this.borderY + 0, 
				this.borderX + (this.lengthX + 1) * this.distX, 
				this.borderY + 0);
		lTop.setStroke(Color.BLACK);
		lTop.setStrokeWidth(4);
		
		Line lRight = new Line(this.borderX + (this.lengthX + 1) * this.distX, 
				this.borderY + 0, 
				this.borderX + (this.lengthX + 1) * this.distX, 
				this.borderY + (this.lengthY + 1) * this.distY);
		lRight.setStroke(Color.BLACK);
		lRight.setStrokeWidth(4);
		
		Line lLeft1 = new Line(this.borderX + 0, 
				this.borderY + 0, 
				this.borderX + 0, 
				this.borderY + (this.lengthY * 0.4 + 1) * this.distY);
		lLeft1.setStroke(Color.BLACK);
		lLeft1.setStrokeWidth(4);
		
		Line lMiddle = new Line(this.borderX + 0, 
				this.borderY + (this.lengthY * 0.4 + 1) * this.distY, 
				this.borderX + (this.lengthX / 4 + 1) * this.distX, 
				this.borderY + (this.lengthY * 0.4 + 1) * this.distY);
		lMiddle.setStroke(Color.BLACK);
		lMiddle.setStrokeWidth(4);
		
		Line lLeft2 = new Line(this.borderX + (this.lengthX / 4 + 1) * this.distX, 
				this.borderY + (this.lengthY * 0.4 + 1) * this.distY, 
				this.borderX + (this.lengthX / 4 + 1) * this.distX, 
				this.borderY + (this.lengthY + 1) * this.distY);
		lLeft2.setStroke(Color.BLACK);
		lLeft2.setStrokeWidth(4);
		
		Line lBottom = new Line(this.borderX + (this.lengthX / 4 + 1) * this.distX, 
				this.borderY + (this.lengthY + 1) * this.distY, 
				this.borderX + (this.lengthX + 1) * this.distX, 
				this.borderY + (this.lengthY + 1) * this.distY);
		lBottom.setStroke(Color.BLACK);
		lBottom.setStrokeWidth(4);
		
		this.getChildren().add(lTop);
		this.getChildren().add(lRight);
		this.getChildren().add(lLeft1);
		this.getChildren().add(lMiddle);
		this.getChildren().add(lLeft2);
		this.getChildren().add(lBottom);
	}
	
	private void displayGrid(double width, double height)
	{
		this.setClip(new Rectangle(0,0,width,height));

		Rectangle border = new Rectangle(this.borderX, this.borderY, width - 2 * this.borderX, height - 2 * borderY);
		border.setFill(Color.TRANSPARENT);
		border.setStroke(Color.BLACK);
		
		this.getChildren().add(border);
		
		for(int x = 1; x <= this.lengthX; x++)
		{
			for(int y = 1; y <= this.lengthY; y++)
			{
				Rectangle dot = new Rectangle(this.borderX + x * this.distX - 1, this.borderY + y * this.distY - 1, 2, 2);
				dot.setFill(Color.SILVER);
				dot.setStroke(Color.TRANSPARENT);
				
				this.getChildren().add(dot);
			}
		}
	}
	
	public double computeX(double x)
	{
		return this.borderX + x * this.distX;
	}
	
	public double computeY(double y)
	{
		return this.borderY + y * this.distY;
	}
	
	public double computeDistX(double x)
	{
		return this.distX * x;
	}
	
	public double computeDistY(double y)
	{
		return this.distY * y;
	}
	
	public enum ShopMap
	{
		NONE,
		ITALY,
		VOESENDORF
	}
}
