package deliveryEmperor;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Warehouse {
	private String companyName;
	private int success;
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	public Warehouse(ContinuousSpace<Object> space, Grid<Object> grid, String companyName, int success) {
		this.space = space;
		this.grid = grid;
		this.companyName = companyName;
		this.success = success;
	}

	
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}



	public int getSuccess() {
		return success;
	}



	public void setSuccess(int success) {
		this.success = success;
	}


	
	
	
	
	
}
