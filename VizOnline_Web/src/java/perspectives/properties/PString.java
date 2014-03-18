package perspectives.properties;

import perspectives.base.PropertyType;

public class PString extends PropertyType
{
	private java.lang.String value;
	@Override
	public PropertyType copy() {
		// TODO Auto-generated method stub
		return new PString(new String(value));
	}
	
	public PString(String value)
	{
		this.value = value;
	}
	
	public String stringValue()
	{
		return value;
	}

	@Override
	public String typeName() {
		// TODO Auto-generated method stub
		return "PString";
	}


	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public PString deserialize(String s) {
		return new PString(s);
		
	}
	
}
