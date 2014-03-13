/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package perspectives.parallel_coordinates;

import java.awt.Color;
import java.awt.Graphics2D;

import perspectives.base.Property;
import perspectives.base.PropertyType;
import perspectives.properties.PColor;
import perspectives.properties.PInteger;
import perspectives.properties.PPercent;
import perspectives.util.TableData;

/**
 *
 * @author mershack
 */
public class ParallelCoordViewer extends ParallelCoordDrawer {

   

    public ParallelCoordViewer(String name, TableData tb) {

        super(name,tb);
        
        try{
                      
		Property<PColor> p1 = new Property<PColor>("Appearance.Vertical Lines Color",new PColor(new Color(200,150,150)));			
		this.addProperty(p1);
			
		Property<PColor> p2 = new Property<PColor>("Appearance.Data edge Color",new PColor(new Color(200,150,150)));			
		this.addProperty(p2);
			
                Property<PPercent> p6 = new Property<PPercent>("Appearance.Header Angle", new PPercent(0));
                 this.addProperty(p6);
         			
		Property<PPercent> p4 = new Property<PPercent>("Appearance.Edge Alpha", new PPercent(0));		
		this.addProperty(p4);
                        
                        
		Property<PInteger> p = new Property<PInteger>("Appearance.Width", new PInteger(100));
		this.addProperty(p);		
			
                        
         Property<PInteger> p5 = new Property<PInteger>("Appearance.Height", new PInteger(300));
		this.addProperty(p5);		
			
                                    
        }catch(Exception e){
            e.printStackTrace();
        }
                
        
        
        
    }
    
    public <T extends PropertyType> void propertyUpdated(Property p, T newvalue)
	{
		if (p.getName() == "Appearance.Vertical Lines Color"){
                    this.setVerticalLinesColor(((PColor)newvalue).colorValue());
                }
			
		if (p.getName() == "Appearance.Data edge Color")
                    this.setDataLinesColor(((PColor)newvalue).colorValue());                
                
               if (p.getName() == "Appearance.Edge Alpha")
		{
			int alpha = (int)(255.*((PPercent)newvalue).getRatio());
			this.setDataLinesAlpha(alpha);
			
		}  
                
		if(p.getName() == "Appearance.Width"){
                    this.setVerticalLinesSeparation(((PInteger) newvalue).intValue());
                    
                }
                
                if(p.getName() == "Appearance.Height")
                {
                    this.setVerticalLinesHeight(((PInteger) newvalue).intValue());
                }

                if(p.getName().equals("Appearance.Header Angle")){
                	double angle = ((PPercent)newvalue).getRatio() * 90;
                    this.setHeaderAngle((int)angle);
                }
                
	}
    
    
    

    @Override
    public void simulate() {
        /*
         if (size <= 97)
         dir = -dir;
         else if (size >= 103)
         dir = -dir;
		
         size = size + dir; */
    }
}
