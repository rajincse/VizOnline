package perspectives.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import perspectives.base.DataSource;
import perspectives.base.Property;
import perspectives.base.PropertyType;
import perspectives.properties.PBoolean;
import perspectives.properties.PFileInput;
import perspectives.properties.PInteger;
import perspectives.properties.POptions;


/**
 *
 * @author mershack
 */
public class TableData extends DataSource {

    protected TableDistances table;
    boolean valid;
    
    

    public TableData(String name) {
        super(name);

        valid = false;
        

        table = new TableDistances();
        
        createProperties();
        
    }
    
    public DistanceMatrix2 toDistanceMatrix2()
    {
    	DistanceMatrix2 m = new DistanceMatrix2(table.getCount());
    	if (table.getCount() != table.getColumnCount())
    		return null;
    	
    	for (int i=0; i<table.getCount()-1; i++)
    	{
    		for (int j=i+1; j<table.getCount(); j++)
    		{
    			double v = (Double)table.getValueAt(i, j);
    			m.setDistance(i, j, (float)v);
    		}
    	}
    	return m;
    	
    }
    
    public void setTable(TableDistances t)
    {
    	table = t;
    }
    
    protected void createProperties()
    {

        try {
            Property<PBoolean> p0 = new Property<PBoolean>("JSON File", new PBoolean(false));          
            addProperty(p0);


            Property<PBoolean> p2 = new Property<PBoolean>("Col Headers", new PBoolean(true));
            addProperty(p2);
            
            Property<PBoolean> p21 = new Property<PBoolean>("Row Headers",new PBoolean(true));           
            addProperty(p21);

            Property<POptions> p3 = new Property<POptions>("Delimiter", new POptions(new String[]{"TAB","SPACE","COMMA"}));
            addProperty(p3);

            PFileInput f = new PFileInput();
            f.dialogTitle = "Open Data File";
            f.extensions = new String[]{"*", "txt", "xml"};
            Property<PFileInput> p1 = new Property<PFileInput>("Tabular File",f);
            addProperty(p1);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public <T extends PropertyType> void propertyUpdated(Property p, T newvalue) {
        boolean js = false;
                
        if(p.getName() == "JSON File") {
            js = ((PBoolean) getProperty("JSON File").getValue()).boolValue();
        }
        
        
        if (js) {
            //se;
            this.removeProperty("Col Headers");
            this.removeProperty("Row Headers");
            this.removeProperty("Delimiter");

        }

        if (p.getName() == "Tabular File") {

           if(!js){
                boolean ch = ((PBoolean) getProperty("Col Headers").getValue()).boolValue();
                boolean rh = ((PBoolean) getProperty("Row Headers").getValue()).boolValue();

              //hide the others if it is a json file

             int delim = ((POptions) (getProperty("Delimiter").getValue())).selectedIndex;
             PFileInput f = ((PFileInput) newvalue);
             String d = "\t";   //default selection
             if (delim == 1) {
                d = " ";
              } else if (delim == 2) {
                d = ",";
              }

              table.fromFile(f.path, d, ch, rh);  //Function to Get the Data from File
           }
           else{   //get the data from a JSON format
               
           } 
            //Determine the properties of the file such as number of rows and columns and print them on the screen
            if (table.getColumnCount() != 0) {
                this.setLoaded(true);

                this.removeProperty("Tabular File");
                this.removeProperty("Delimiter");
                this.removeProperty("Col Headers");
                this.removeProperty("Row Headers");
                this.removeProperty("JSON File");

                try {
                    Property<PInteger> p1 = new Property<PInteger>("# Columns", new PInteger(table.getColumnCount()));                   
                    p1.setReadOnly(true);
                    this.addProperty(p1);

                    Property<PInteger> p2 = new Property<PInteger>("# Rows",new PInteger(table.getRowCount()));                  
                    p2.setReadOnly(true);
                    this.addProperty(p2);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /* Method to get the table */
    public TableDistances getTable() {
        return table;
    }

    /* method to set that the table is valid */
    public boolean isValid() {
        return valid;
    }
    
  
    
    

}
