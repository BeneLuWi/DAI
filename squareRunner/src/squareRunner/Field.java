package squareRunner;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class Field {

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	GridPoint point;
	
	private int effort;
	FieldType type;
	
	double n = 0.0;
	double w = 0.0;
	double s = 0.0;
	double e = 0.0;
	
	public Field(ContinuousSpace<Object> space, Grid<Object> grid, int x, int y) {
		this.space = space;
		this.grid = grid;
		this.type = FieldType.DEFAULT;
		this.effort = -1;
		this.point = new GridPoint(x,y);
		
		if (x == 0) this.w = -1000;
		if (x == 9) this.e = -1000;
		if (y == 0) this.s = -1000;
		if (y == 4) this.n = -1000;
		
	}

	
	/*
	 * Getters and Setters
	 */
	
	public void setType(FieldType type) {
		this.type = type;
		switch(type) {
			case IMPASSABLE:
				this.effort = Integer.MIN_VALUE;
				break;
			case SLIPPERY:
				this.effort = -3;
				break;
			case EXIT:
				this.effort = 10;
				break;
			case TRAP:
				this.effort = -10;
				break;
		}
	}
	
	public int getEffort() {
		return effort;
	}

	public void setEffort(int effort) {
		this.effort = effort;
	}

	public int getX() {
		return this.point.getX();		
	}

	public int getY() {
		return this.point.getY();		
	}
	
	public FieldType getType() {
		return type;
	}
	
	public double getN() {
		return n;
	}
	public void setN(double n) {
		this.n = n;
	}
	public double getW() {
		return w;
	}
	public void setW(double w) {
		this.w = w;
	}
	public double getS() {
		return s;
	}
	public void setS(double s) {
		this.s = s;
	}
	public double getE() {
		return e;
	}
	public void setE(double e) {
		this.e = e;
	}
	
}
