package application;

import javafx.geometry.Point2D;

public class MapNode {
	public double h;//arbitrary n to end
	public double g;//start to arbitrary n
	public double f;//h + g
	public boolean isWall, visited, checkPoint;
	public MapNode prev;
	public final Point2D coord;
	public final int r;
	public final int c;
	MapNode(double h, double g, double f, boolean isWall, Point2D p){
		this.h = h;
		this.f = f;
		this.g = g;
		this.r = (int)p.getY()/20;//row
		this.c = (int)p.getX()/20;//column
		this.isWall = isWall;
		this.checkPoint = false;
		prev = null;
		visited = false;
		coord = p;
	}
}