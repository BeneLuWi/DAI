package squareRunner;

import repast.simphony.space.grid.GridPoint;

public class State {

	GridPoint point;
	double n;
	double w;
	double s;
	double e;
	int reward;

	public State(int x, int y) {
		this.point = new GridPoint(x,y);	
	}
	
	
	
	public int getReward() {
		return reward;
	}



	public void setReward(int reward) {
		this.reward = reward;
	}



	public GridPoint getPoint() {
		return point;
	}
	public void setPoint(GridPoint point) {
		this.point = point;
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
