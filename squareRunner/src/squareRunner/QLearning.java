package squareRunner;


import repast.simphony.random.RandomHelper;


public class QLearning {

	Field[][] states;
	double learningRate;	
	double discountFactor;	
	
	public QLearning(double learningRate, double discountFactor, Field[][] states) {
		this.learningRate = learningRate;
		this.discountFactor = discountFactor;
		this.states = states;
	}	
	
	
	/**
	 * Update the QValue of field (x,y) for Action
	 * 
	 * @param action Action to be updated / Action that has been performed
	 * @param x new x location
	 * @param y new y location
	 * @param reward for the field we reached from field (x,y)
	 */
	public void update(
			Action action, int x_beforeMove, int y_beforeMove, int reward,
			int x_afterMove, int y_afterMove) {
		
		// Current Value of the Field (Before Update)
		double oldValue = getValue(action, x_beforeMove, y_beforeMove);
		
		// Value of the 
		double maxValue = maxValue(x_afterMove, y_afterMove);
				
		
		// Value of the Field (After Update) 
		double newValue = oldValue + learningRate * ((double)reward + discountFactor * maxValue - oldValue);	

		
		// Update q-value
		setValue(action, x_beforeMove, y_beforeMove, newValue);
		
	}
	
	/**
	 * Get the highest QValue
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private double maxValue(int x, int y) {
		return 
			Math.max(
				Math.max(getValue(Action.NORTH, x, y), getValue(Action.SOUTH, x, y)), 
				Math.max(getValue(Action.EAST, x, y), getValue(Action.WEST, x, y))
			);		
	}
	
	
	/**
	 * 
	 * @param action
	 * @param x
	 * @param y
	 * @return
	 */
	private double getValue(Action action, int x, int y) {
				
		if ( x < 0 || x > 9 || y < 0 || y > 4) return Integer.MIN_VALUE;
		
		Field state = states[x][y];
		
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
		
		Field state = states[x][y];
		
		double[] values = {state.getN(), state.getE(), state.getS(), state.getW()};
		Action[] actions = {Action.NORTH, Action.EAST, Action.SOUTH, Action.WEST};
		
		
		// If all values are equal (0 or any other value) then the random stays
		int maxIndex = RandomHelper.nextIntFromTo(0, values.length - 1); 
		double maxValue = values[maxIndex];
		
		// Get maximum q-value
		for (int i = 0; i < 4; i++) {
			if (maxValue < values[i]) {
				maxIndex = i;
				maxValue = values[i];				
			}
		}
		
		
		return actions[maxIndex];
		
	}
	
	
	private void setValue(Action action, int x, int y, double newValue) {
		switch(action) {
			case NORTH: 
				states[x][y].setN(newValue);
				break;
			case SOUTH: 
				states[x][y].setS(newValue);
				break;
			case WEST: 
				states[x][y].setW(newValue);
				break;
			case EAST: 
				states[x][y].setE(newValue);
				break;
		}

	}
	
	
}





