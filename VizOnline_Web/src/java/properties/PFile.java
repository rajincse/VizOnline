package properties;



public class PFile extends PropertyType
{
	public String path = "";
	
	public String[] extensions = new String[0];	
	public int currentExtension = -1;
	
	public boolean onlyFiles = true;
	public boolean onlyDirectories = false;
	public boolean filesAndDirectories = false;
	
	public boolean save = false;
	
	public String dialogTitle = "";

	@Override
	public PFile copy() {
		// TODO Auto-generated method stub
		PFile of = new PFile();			
		of.path = new String(path);
		of.currentExtension = currentExtension;
		of.onlyFiles = onlyFiles;
		of.onlyDirectories = onlyDirectories;
		of.filesAndDirectories = this.filesAndDirectories;
		of.dialogTitle = new String(this.dialogTitle);
		of.extensions = new String[extensions.length];
		of.save = this.save;
		for (int i=0; i<extensions.length; i++)
			of.extensions[i] = new String(extensions[i]);
		return of;
	}
	public String typeName() {
		// TODO Auto-generated method stub
		return "PFile";
	}

	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PFile deserialize(String s) {
		return null;
		
	}
}
