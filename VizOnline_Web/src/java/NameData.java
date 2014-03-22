import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

import perspectives.base.DataSource;
import perspectives.base.Property;
import perspectives.properties.PFileInput;
import perspectives.properties.PInteger;


public class NameData extends DataSource{
	
	public String[] names;
	
	public NameData(String name) {
		super(name);	
		
		Property<PFileInput> loadData = new Property<PFileInput>("Load", new PFileInput())
				{
					@Override
					protected boolean updating(PFileInput newvalue) {
						
						try{						
							names = loadNamesFromFile(newvalue.path);							
							
							removeProperty("Load");
							Property<PInteger> info = new Property<PInteger>("Name#",new PInteger(names.length));
							info.setReadOnly(true);
							addProperty(info);
							
							loaded = true;
						}
						catch(Exception e){};
			        
						
						return true;
					}
				};
		
		addProperty(loadData);
	}
	
	private String[] loadNamesFromFile(String path)
	{
		try{
			
			BufferedReader br = new BufferedReader(
						new FileReader(path));
			
			int nrNames = Integer.parseInt(br.readLine());
			String[] names = new String[nrNames];
			
			for (int i=0; i<names.length; i++)
				names[i] = br.readLine();

			
			return names;
		}
		catch(Exception e){return null;}
	}
	

}
