package properties;


public class POptions extends PropertyType
{
	public int selectedIndex;
	public String[] options;
	
	public POptions(String[] options)
	{
		this.options = options;
		selectedIndex = 0;
	}
	
	@Override
	public POptions copy() {
		String[] oc = options.clone();
		POptions opt = new POptions(oc);
		opt.selectedIndex = selectedIndex;
		return opt;
	}
	
	public String typeName() {
		// TODO Auto-generated method stub
		return "OptionsPropertyType";
	}


	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public POptions deserialize(String s) {
		return null;
		
	}
}