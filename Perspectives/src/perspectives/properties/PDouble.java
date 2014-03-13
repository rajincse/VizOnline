package perspectives.properties;

import perspectives.base.PropertyType;

public class PDouble extends PropertyType
{
	private double value;
	@Override
	public PropertyType copy() {
		// TODO Auto-generated method stub
		return new PDouble(value);
	}
	
	public PDouble(double value)
	{
		this.value = value;
	}
	
	public double doubleValue()
	{
		return value;
	}
	
	@Override
	public String typeName() {
		// TODO Auto-generated method stub
		return "PDouble";
	}


	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return ""+value;
	}

	@Override
	public PDouble deserialize(String s) {
		return new PDouble(Double.parseDouble(s));
		
	}
	
}
