package perspectives.properties;

import perspectives.base.PropertyType;

public class PText extends PropertyType{
	private java.lang.String value;
	@Override
	public PropertyType copy() {
		// TODO Auto-generated method stub
		return new PText(new String(value));
	}
	
	public PText(String value)
	{
		this.value = value;
	}
	public void setValue(String value)
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
		return "PText";
	}


	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public PText deserialize(String s) {
		return new PText(s);
		
	}
}
