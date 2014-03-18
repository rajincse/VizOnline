package perspectives.properties;
import perspectives.base.PropertyType;

public class PProgress extends PropertyType {
	
	
	double progress;
	
	boolean cancelable;
	
	public boolean indeterminable;
	
	public PProgress()
	{
		progress = 0;
		cancelable = true;
	}
	
	public PProgress(double d)
	{
		progress = d;
		cancelable = true;
	}
	
	public double getValue()
	{
		return progress;
	}
	
	public void setValue(double progress)
	{
		this.progress = progress;
	}

	@Override
	public <T extends PropertyType> T copy() {
		
		PProgress newp = new PProgress();
		newp.setValue(this.getValue());
		newp.cancelable = this.cancelable;
		return (T) newp;
	}

	@Override
	public String typeName() {
		return "PProgress";
	}

	@Override
	public String serialize() {
		return ""+progress;
	}

	@Override
	public PropertyType deserialize(String s) {
		PProgress p = new PProgress();
		p.setValue(Double.parseDouble(s));
		return p;
	}
}
