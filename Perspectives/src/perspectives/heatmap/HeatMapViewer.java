package perspectives.heatmap;

import java.awt.Color;
import java.awt.Graphics2D;

import java.util.ArrayList;

import perspectives.base.Property;
import perspectives.base.PropertyType;
import perspectives.properties.PColor;
import perspectives.properties.PInteger;
import perspectives.util.TableData;


//import FloatMatrix;

/**
 *
 * @author mershack
 */
public class HeatMapViewer extends HeatMapDrawer {

    public HeatMapViewer(String name, TableData tb) {

        super(name, tb);

        try {
            Property<PColor> p1 = new Property<PColor>("Appearance.Highest Value Color",new PColor(new Color(0, 255, 0)));
          
            this.addProperty(p1);
            
            Property<PInteger> p2 = new Property<PInteger>("Appearance.Column Width", new PInteger(getBoxWidth()));
            this.addProperty(p2);

            Property<PInteger> p3 = new Property<PInteger>("Appearance.Row Height", new PInteger(getBoxHeight()));
            this.addProperty(p3);

            Property<PInteger> p7 = new Property<PInteger>("Appearance.Header Angle", new PInteger(20));
            this.addProperty(p7);

            Property<PColor> p8 = new Property<PColor>("Appearance.Selection Color", new PColor(new Color(255, 0, 0)));
            this.addProperty(p8);
                        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

      public <T extends PropertyType> void propertyUpdated(Property p, T newvalue){
        
              if (p.getName().equals("Appearance.Highest Value Color")) {
            this.setHighColor(((PColor)newvalue).colorValue());
        }

        if (p.getName().equals("Appearance.Lowest Value Color")) {
            this.setLowColor(((PColor)newvalue).colorValue());
        }

        if (p.getName().equals("Appearance.Column Width")) {
            this.setBoxWidth(((PInteger) newvalue).intValue());

        }

        if (p.getName().equals("Appearance.Row Height")) {
            this.setBoxHeight(((PInteger) newvalue).intValue());
        }
        /**
         * The cluster LinkTypes *
         */
      /*  if (p.getName().equals("ClusterLinkedType.Single-Linkage")) {
            boolean b = (Boolean) newvalue;
            this.setSingleLinkType(b);
            //set the other linkages to false
        }

        if (p.getName().equals("ClusterLinkedType.Complete-Linkage")) {
            boolean b = (Boolean) newvalue;
            this.setCompLinkType(b);
        }

        if (p.getName().equals("ClusterLinkedType.Average-Linkage")) {
            boolean b = (Boolean) newvalue;
            this.setAvgLinkType(b);
        }*/

       
        if (p.getName().equals("Appearance.Header Angle")) {             
            int val = ((PInteger) newvalue).intValue();   
            this.setHeaderAngle(val);
        }


        if (p.getName().equals("Appearance.Selection Color")) {
            this.setSelectedColor(((PColor)newvalue).colorValue());
        }

        /*if (p.getName().equals("Appearance.Scale Colors")) {
            ArrayList<Color> ac = (ArrayList<Color>) newvalue;

            // System.out.println(ac.size()+ "*****");

            this.setScaleColors((ArrayList<Color>) newvalue);
        }*/

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
