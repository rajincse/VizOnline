package d3;
import java.awt.Graphics2D;
import perspectives.Viewer2D;
import properties.PBoolean;
import properties.PDouble;
import properties.PFile;
import properties.PInteger;
import properties.PString;
import properties.Property;


public class D3Viewer extends Viewer2D{
    public D3Viewer(String name)            
    {
        super(name);
        try {

            Property<PFile> p33 = new Property<PFile>("Load Positions", new PFile());
            this.addProperty(p33);

            Property<PDouble> p = new Property<PDouble>("Simulation.SPRING_LENGTH", new PDouble(300.));
            //((BarnesHutGraphDrawer) drawer).setSpringLength(300.);
            this.addProperty(p);

           

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(Graphics2D g) {
        
    }

    @Override
    public void simulate() {
        
    }

	
}
