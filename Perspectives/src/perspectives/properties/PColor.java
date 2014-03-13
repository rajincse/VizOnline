package perspectives.properties;

import java.awt.Color;
import perspectives.base.PropertyType;


public class PColor extends PropertyType
{
	private Color value;
	
	public boolean prefixSearchable = true;

	@Override
	public PColor copy() {
		// TODO Auto-generated method stub
		return new PColor(new Color(value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha()));
	}
	
	public PColor(Color c)
	{
		value = c;
	}
	
	public Color colorValue()
	{
		return value;
	}
	
	public String typeName() {
		// TODO Auto-generated method stub
		return "PColor";
	}


	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return ""+value.getRed() + "-" + value.getGreen() + "-" + value.getBlue() + "-" + value.getAlpha();
	}

	@Override
	public PColor deserialize(String s) {
		String[] split = s.split("-");
		if (split.length <= 3)
			return new PColor(new Color(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])));
		if (split.length > 3)
			return new PColor(new Color(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])));
		return null;
		
	}
}
