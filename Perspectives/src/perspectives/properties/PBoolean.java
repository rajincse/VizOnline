package perspectives.properties;

import perspectives.base.PropertyType;

public class PBoolean extends PropertyType
{
	private boolean value;
	
	public boolean prefixSearchable = true;

	@Override
	public PBoolean copy() {
		// TODO Auto-generated method stub
		return new PBoolean(value);
	}
	
	public PBoolean(boolean b)
	{
		value = b;
	}
	
	public boolean boolValue()
	{
		return value;
	}
	
	public String typeName() {
		// TODO Auto-generated method stub
		return "PBoolean";
	}

	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		if (value)
			return "1";
		else
			return "0";
	}

	@Override
	public PBoolean deserialize(String s) {
		// TODO Auto-generated method stub
		if (s.equals("1"))
			return new PBoolean(true);
		else return new PBoolean(false);
	}
}
