package perspectives.properties;

import perspectives.base.PropertyType;

public class PFileOutput extends PropertyType{
	
	public String path = "";
	
	public String[] extensions = new String[0];	
	public int currentExtension = -1;
	
	public boolean onlyFiles = true;
	public boolean onlyDirectories = false;
	public boolean filesAndDirectories = false;

	public String dialogTitle = "";


	public PFileOutput copy() {
		PFileOutput of = new PFileOutput();
		// TODO Auto-generated method stub	
		of.path = new String(path);
		of.currentExtension = currentExtension;
		of.onlyFiles = onlyFiles;
		of.onlyDirectories = onlyDirectories;
		of.filesAndDirectories = this.filesAndDirectories;
		of.dialogTitle = new String(this.dialogTitle);
		of.extensions = new String[extensions.length];		
		for (int i=0; i<extensions.length; i++)
			of.extensions[i] = new String(extensions[i]);
		return of;
	}
	
	public String typeName() {
		// TODO Auto-generated method stub
		return "PFileOutput";
	}

	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return "true";
            //return null;
	}
	@Override
	public PFileOutput deserialize(String s) {
	    //we will be returning a PFile with the selected filepath
            PFileOutput pf = copy();
            pf.path = s;
            
            return pf;
	}

}
