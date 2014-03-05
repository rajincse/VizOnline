/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brain;

import d3.D3Viewer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import perspectives.PropertyManager;
import properties.PString;
import properties.Property;
import properties.PropertyType;
import util.Vector3D;

/**
 *
 * @author rajin
 */
public class BrainStatsD3Viewer extends D3Viewer{
    
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
            Property<PString> p = new Property<PString>(BrainViewer.PROPERTY_SELECTED_TUBES,new PString(""));
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
   public <T extends PropertyType> boolean propertyBroadcast(Property p,
			T newvalue, PropertyManager origin) 
   {
        if (p.getName() == BrainViewer.PROPERTY_SELECTED_TUBES)
        {
                Property<PString> propertySelectedTube = this.getProperty(BrainViewer.PROPERTY_SELECTED_TUBES);
                propertySelectedTube.setValue((PString)newvalue);
                this.requestRender();
                String value = ((PString)newvalue).stringValue();
                System.out.println("Brain D3 viewer "+BrainViewer.PROPERTY_SELECTED_TUBES+": "+value);
        }
        return super.propertyBroadcast(p, newvalue, origin);
   }
    @Override
    public JSONObject updateData(boolean isInitialCall) {
        JSONObject data = new JSONObject();
        //InitialCall
        data.put("IsInitialCall", isInitialCall);
        // Property Data
        JSONObject propertyData= new JSONObject();
        
        Property<PString> propertySelectedTube = this.getProperty(BrainViewer.PROPERTY_SELECTED_TUBES);
        String selectedTubes = propertySelectedTube.getValue().stringValue();
        propertyData.put(BrainViewer.PROPERTY_SELECTED_TUBES, selectedTubes);
        
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
        HashMap<String, Double> tubeLengthList = new HashMap<String, Double>();
        double minLength = Integer.MAX_VALUE;
        double maxLength = Integer.MIN_VALUE;
        
        for(String tubeIdString : tubeIds)
        {
            int tubeId = Integer.parseInt(tubeIdString);
            
            double tubeLength = getTubeLength(tubeId);
            tubeLengthList.put(tubeIdString, tubeLength);
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
        
        String[] cateoryTubeIndex = new String[TOTAL_CATEGORIES];
        
        int[] categorySegmentCount = new int[TOTAL_CATEGORIES];
       
        for(Entry<String,Double> e : tubeLengthList.entrySet())
        {
            String tubeId = e.getKey();
            Double tubeLength = e.getValue();
            int categoryIndex = (int) Math.floor((tubeLength-minLength)/categoryStep);
            categorySegmentCount[categoryIndex]++;
            
            if(cateoryTubeIndex[categoryIndex]== null || cateoryTubeIndex[categoryIndex].isEmpty())
            {
               cateoryTubeIndex[categoryIndex] = tubeId;
            }
            else
            {
                String categoryTubeId = cateoryTubeIndex[categoryIndex];
                categoryTubeId+= ","+tubeId;
                cateoryTubeIndex[categoryIndex] = categoryTubeId;
            }
        }
        
        
        for(int i=0;i< TOTAL_CATEGORIES;i++)
        {
            double min = minLength+i*categoryStep;
            double max = minLength+(i+1)*categoryStep -0.01;
            String key = String.format("%.2f-%.2f", min, max);
            int value = categorySegmentCount[i];
            JSONObject obj = new JSONObject();
            obj.put("Index",i);
            obj.put("Key", key);
            obj.put("Value", value);
            obj.put("TubeIds", cateoryTubeIndex[i]);
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
