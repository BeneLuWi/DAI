package deliveryEmperor;

public class Trust {

	private Warehouse company;
	private double trustValue;
	
	private int successes;
	private int failures;
	
	public Trust(Warehouse company) {
		this.company = company;
		this.trustValue = 1.0;
	}
	
	public void addSuccess() {
		successes++;
		updateTrustValue();
	}
	
	public void addFailure() {
		failures++;
		updateTrustValue();
	}
	
	private void updateTrustValue() {
		trustValue = ((double)(successes - failures)) / ((double) (successes + failures));
	}
	
	
	public Warehouse getCompany() {
		return company;
	}
	public void setCompany(Warehouse company) {
		this.company = company;
	}
	public double getTrustValue() {
		return trustValue;
	}
	public void setTrustValue(double trustValue) {
		this.trustValue = trustValue;
	}
	
	
	
	
}
