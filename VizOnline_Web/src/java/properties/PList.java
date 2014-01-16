package properties;


public class PList extends PropertyType
{
	public int[] selectedIndeces;
	public String[] items;
	
	public boolean prefixSearchable = true;

	@Override
	public PList copy() {
		// TODO Auto-generated method stub
		PList l = new PList();
		l.selectedIndeces = selectedIndeces.clone();
		l.items = new String[items.length];
		for (int i=0; i<items.length; i++)
			l.items[i] = new String(items[i]);
		return null;
	}
	
	public String typeName() {
		// TODO Auto-generated method stub
		return "ListPropertyType";
	}


	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PList deserialize(String s) {
		// TODO Auto-generated method stub
		return null;
		
	}
	
	
}