package squareRunner;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;

public class RunnerOverseer {

	Runner runnerOne;
	Runner runnerTwo;
	
	int iterations;
	
	public RunnerOverseer(Runner runnerOne, Runner runnerTwo) {
		this.runnerOne = runnerOne;
		this.runnerTwo = runnerTwo;
	}
	
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		
		// Reset agents, if the exit was reached
		if (runnerOne.hasReachedExit() && runnerTwo.hasReachedExit()) {
			
			if (runnerOne.prevSteps == runnerOne.steps && runnerOne.prevprevSteps == runnerOne.prevSteps &&
					runnerTwo.prevprevSteps == runnerTwo.prevSteps && runnerTwo.prevSteps == runnerTwo.steps) {
				System.out.printf("After %d Iterations \n", iterations);
				System.out.printf("Runner 1: %d Steps \n", runnerOne.steps);
				System.out.printf("Runner 2: %d Steps \n", runnerTwo.steps);
				RunEnvironment.getInstance().endRun();
			}
						
			runnerOne.prevprevSteps = runnerOne.prevSteps;
			runnerTwo.prevprevSteps = runnerTwo.prevSteps;
			
			runnerOne.prevSteps = runnerOne.steps;
			runnerTwo.prevSteps = runnerTwo.steps;
			
			runnerOne.steps = 0;
			runnerTwo.steps = 0;
			
			runnerOne.moveTo(0, 0);
			runnerTwo.moveTo(0, 4);
			
			runnerOne.setReachedExit(false);
			runnerTwo.setReachedExit(false);
			
			iterations ++;
			
		}	
	}
	
}
