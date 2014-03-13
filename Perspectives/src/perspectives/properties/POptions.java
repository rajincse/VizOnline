package perspectives.properties;

import perspectives.base.PropertyType;

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
                optionString += "-" + selectedIndex;
          
                return optionString;
	}

	@Override
	public POptions deserialize(String s) {
		String[] sopt = s.split("-");
		
		String[] opt = new String[sopt.length-1];
		for (int i=0; i<opt.length; i++)
			opt[i] = sopt[i];
		
		POptions pOptions = new POptions(opt);   
		pOptions.selectedIndex = Integer.parseInt(sopt[sopt.length-1]);
		
		
             
           
            
            return pOptions;
		
	}
}