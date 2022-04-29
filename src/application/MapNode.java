package application;

import javafx.geometry.Point2D;

public class MapNode {
	public double h;
	public double g;
	public double f;
	public boolean isWall, visited;
	public MapNode prev;
	public final Point2D coord;
	public final int r;
	public final int c;
	MapNode(double h, double g, double f, boolean isWall, Point2D p){
		this.h = h;
		this.f = f;
		this.g = g;
		this.r = (int)p.getY()/20;
		this.c = (int)p.getX()/20;
		this.isWall = isWall;
		prev = null;
		visited = false;
		coord = p;
	}
}
