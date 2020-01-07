package bdiJZombies;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.ContextUtils;

public class Overseer {

	private void endSimulation() {
		RunEnvironment.getInstance().endRun();
	}
	
	
	@ScheduledMethod(start = 1, interval = 1)
	public void checkState() {
		
		Context<Object> context = ContextUtils.getContext(this);
		
		List<Human> humans = new ArrayList<>();
		List<Zombie> zombies= new ArrayList<>();
	
		for (Object obj: context) {
			if (obj instanceof Human) {
				humans.add((Human) obj);
			} else if (obj instanceof Zombie) {
				zombies.add((Zombie) obj);
			}
		}
		
		if (humans.size()==0) {
			System.out.println("All humans eaten..");
			endSimulation();
		} else if(zombies.stream().noneMatch(zombie -> !zombie.isTrapped())) {
			System.out.println("All zombies caught..");
			endSimulation();
		}
		
	}
	
}
