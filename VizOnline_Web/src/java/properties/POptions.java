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
		return "POptions";
	}


	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		//It should return the options;
            	String optionString="";
                for(int i=0; i<options.length; i++){
                    if(i==0){
                     optionString = options[i];
                    }else{
                        optionString +="-" + options[i];
                    }
                }
                return optionString;
	}

	@Override
	public POptions deserialize(String s) {
	    //it should return a POptions object with the selected index property
            POptions pOptions = new POptions(options);
            for(int i=0; i<options.length; i++){
                if(options[i].equalsIgnoreCase(s)){
                    pOptions.selectedIndex = i;
                }
            }
                return pOptions;
		
	}
}