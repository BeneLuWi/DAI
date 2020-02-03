package squareRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import repast.simphony.space.grid.GridPoint;

public class QLearning {

	List<State> states = new ArrayList<>();
	double learningRate;	
	double discountFactor;	
	
	public QLearning(double learningRate, double discountFactor) {
		this.learningRate = learningRate;
		this.discountFactor = discountFactor;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; i < 10; i++) {
				states.add(new State(i,j));
			}
		}

		setReward(0, 3, -3);
		setReward(3, 3, -3);
		setReward(0, 4, -3);
		setReward(5, 2, -3);
		setReward(6, 1, -3);
		setReward(7, 0, -3);
		
		
		setReward(1, 0, -10);
		setReward(2, 2, -10);
		setReward(8, 3, -10);
		setReward(9, 1, -10);
		
		setReward(3, 1, Integer.MIN_VALUE);
		setReward(3, 2, Integer.MIN_VALUE);
		setReward(6, 4, Integer.MIN_VALUE);
		setReward(7, 3, Integer.MIN_VALUE);
		setReward(7, 4, Integer.MIN_VALUE);
		
		setReward(8, 4, 10);
			
	}	
	
	
	/**
	 * 
	 * 
	 * @param action Action that took us here
	 * @param x new x location
	 * @param y new y location
	 */
	public void update(Action action, int x, int y, int reward) {
		
		// Current Value of the Field (Before Update)
		double oldValue = getValue(action, x, y);
		
		// Value of the previously visited field
		double maxValue;
		switch(action) {
			case NORTH: 
				maxValue = maxSurrounding(x, y + 1);
			case SOUTH: 
				maxValue = maxSurrounding(x, y - 1);		
			case WEST: 
				maxValue = maxSurrounding(x - 1, y);
			default: 
				maxValue = maxSurrounding(x + 1, y);
		}
		
		
		// Value of the Field (After Update) 
		double newValue = oldValue;	
		newValue += learningRate * ((double)reward + discountFactor * maxValue - oldValue);	
	 
	}
	
	
	private double maxSurrounding(int x, int y) {
		return 
			Math.max(
				Math.max(getValue(Action.NORTH, x, y), getValue(Action.SOUTH, x, y)), 
				Math.max(getValue(Action.EAST, x, y), getValue(Action.WEST, x, y))
			);		
	}
	
	
	private double getValue(Action action, int x, int y) {
		
		if ( x < 0 || x > 9 || y < 0 || y > 4) return Double.MIN_VALUE;
		
		State state = states
			.stream()
			.filter(s-> s.getPoint().getX() == x && s.getPoint().getY() == y)
			.findFirst()
			.orElse(null);
		
		if (state == null) return 0.0;
		
		switch(action) {
			case NORTH: 
				return state.n;
			case SOUTH: 
				return state.s;		
			case WEST: 
				return state.w;		
			default: 
				return state.e;		
		}
					
	}
	
	/**
	 * Get the best Action according to q-value
	 * 
	 * @param x current x
	 * @param y current y
	 * @return action with maximum q-value
	 */
	public Action getBestAction(int x, int y) {
		State state = states
				.stream()
				.filter(s-> s.getPoint().getX() == x && s.getPoint().getY() == y)
				.findFirst()
				.orElse(null);
		
		// Shouldnt happen
		if (state == null) return Action.NORTH;
		
		double[] values = {state.getN(), state.getE(), state.getS(), state.getW()};
		Action[] actions = {Action.NORTH, Action.EAST, Action.SOUTH, Action.WEST};
		
		
		// If all values are equal (0 or any other value) then the random stays
		Random r = new Random();
		int maxIndex = r.nextInt(3-0) + 0; 
		double maxValue = values[maxIndex];
		
		// Get maximum q-value
		for (int i = 0; i < 4; i++) {
			if (values[i] > maxValue) {
				maxIndex = i;
				maxValue = values[i];				
			}
		}
			
		return actions[maxIndex];
		
	}
	
	private void setValue(Action action, int x, int y, double newValue) {
		states.forEach(state -> {
			if (state.getPoint().getX() == x && state.getPoint().getY() == y) {
				switch(action) {
				case NORTH: 
					state.setN(newValue);
				case SOUTH: 
					state.setS(newValue);		
				case WEST: 
					state.setW(newValue);
				default: 
					state.setE(newValue);
				}
			}
		});
	}
	
	private void setReward(int x, int y, int reward) {
		states.forEach(state -> {
			if (state.getPoint().getX() == x && state.getPoint().getY() == y) {
				state.setReward(reward);
			}
		});
	}
	
}





