/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brain;

import d3.D3Viewer;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import properties.PString;
import properties.Property;
import properties.PropertyType;
import util.Vector3D;

/**
 *
 * @author rajin
 */
public class BrainStatsD3Viewer extends D3Viewer{
    
    private final String PROPERTY_SELECTED_TUBES ="SelectedTubes";
    private final int TOTAL_CATEGORIES = 10;
    
    private BrainData brainData;
    private String lastTubeSelection;
    public BrainStatsD3Viewer(String name, BrainData d) {
        super(name);

        this.brainData = d;
        this.lastTubeSelection = "";
        try
        {
//            String val = "107,109,127,128,130,149,158";
            Property<PString> p = new Property<PString>(PROPERTY_SELECTED_TUBES,new PString(""));
            p.setPublic(true);
            p.setVisible(false);
            this.addProperty(p);
        }
        catch(Exception e)
        {
                e.printStackTrace();
        }
    }
   
    @Override
    public JSONObject updateData(boolean isInitialCall) {
        JSONObject data = new JSONObject();
        //InitialCall
        data.put("IsInitialCall", isInitialCall);
        // Property Data
        JSONObject propertyData= new JSONObject();
        
        String selectedTubes = this.getProperty(PROPERTY_SELECTED_TUBES).getValue().serialize();
        propertyData.put(PROPERTY_SELECTED_TUBES, selectedTubes);
        
        boolean dataUpdated = false;
        if(!selectedTubes.equals(this.lastTubeSelection))
        {
            dataUpdated = true;
            this.lastTubeSelection = selectedTubes;
        }
        
        data.put("PropertyData", propertyData);
        JSONArray dataArray =null;
        if(dataUpdated)
        {
            dataArray = this.getDataArray(selectedTubes);
            dataUpdated = false;
        }
        data.put("DataArray", dataArray);
        return data;
    }
    
    private JSONArray getDataArray(String selectedTubes)
    {
        JSONArray dataArray = new JSONArray();
     
        String[] tubeIds = selectedTubes.split(",");
        ArrayList<Double> tubeLengthList = new ArrayList<Double>();
        double minLength = Integer.MAX_VALUE;
        double maxLength = Integer.MIN_VALUE;
        
        for(String tubeIdString : tubeIds)
        {
            JSONObject obj = new JSONObject();
            int tubeId = Integer.parseInt(tubeIdString);
            
            double tubeLength = getTubeLength(tubeId);
            tubeLengthList.add(tubeLength);
            if(tubeLength < minLength)
            {
                minLength = tubeLength;
            }
            
            if(tubeLength > maxLength)
            {
                maxLength = tubeLength;
            }
        }
        
        double range = maxLength - minLength+.01;
        double categoryStep = range / TOTAL_CATEGORIES;
        int[] categorySegmentCount = new int[TOTAL_CATEGORIES];
       
        
        for(Double tubeLength : tubeLengthList)
        {
            int categoryIndex = (int) Math.floor((tubeLength-minLength)/categoryStep);
            categorySegmentCount[categoryIndex]++;
        }
        for(int i=0;i< TOTAL_CATEGORIES;i++)
        {
            double min = minLength+i*categoryStep;
            double max = minLength+(i+1)*categoryStep -0.01;
            String key = String.format("%.2f-%.2f", min, max);
            int value = categorySegmentCount[i];
            JSONObject obj = new JSONObject();
            obj.put("Key", key);
            obj.put("Value", value);
            dataArray.add(obj);
        }
        
        return dataArray;
    }
    private double getTubeLength(int tubeId)
    {
        Vector3D[] segments = this.brainData.segments[tubeId];
        double length =0;
        for(int i=1;i<segments.length;i++)
        {
            Vector3D v1 = segments[i-1];
            Vector3D v2 = segments[i];
            length += v1.minus(v2).magnitude();
        }
        return length;
    }
}
