package perspectives.properties;

import perspectives.base.PropertyType;

public class PInteger extends PropertyType
{
	private int value;
	@Override
	public PropertyType copy() {
		// TODO Auto-generated method stub
		return new PInteger(value);
	}
	
	public PInteger(int value)
	{
		this.value = value;
	}
	
	public int intValue()
	{
		return value;
	}
	
	public String typeName() {
		// TODO Auto-generated method stub
		return "PInteger";
	}

	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return ""+value;
	}

	@Override
	public PInteger deserialize(String s) {
		return new PInteger(Integer.parseInt(s));
		
	}
	
}