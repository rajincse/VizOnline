package perspectives.properties;


import perspectives.base.PropertyType;

public class PPercent extends PropertyType
{
	private double val = 0.5;
	
	public PPercent(double v)
	{
		setRatio(v);
	}
	public PPercent(int p)
	{
		setPercent(p);
	}
	public void setPercent(int vv)
	{
		double v = vv/100.;
		setRatio(v);
	}
	public void setRatio(double v)
	{
		if (v < 0) val = 0;
		else if (v > 1) val = 1;
		else val = v;		
	}		
	public int getPercent()
	{
		return (int)(val*100);
	}
	public double getRatio()
	{
		return val;
	}
	@Override
	public PPercent copy() {
		// TODO Auto-generated method stub
		return new PPercent(val);
	}
	public String typeName() {
		// TODO Auto-generated method stub
		return "PPercent";
	}

	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return ""+val;
	}
	@Override
	public PPercent deserialize(String s) {
		return new PPercent(Double.parseDouble(s));
		
	}
}

