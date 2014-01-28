package d3;
import java.awt.Graphics2D;
import org.json.JSONObject;
import perspectives.Viewer2D;


 public abstract class D3Viewer extends Viewer2D{
    
     public D3Viewer(String name)
     {
         super(name);
     }
     abstract  public JSONObject updateData();
     @Override
    public void render(Graphics2D g) {
        
        
    }

    @Override
    public void simulate() {
        
    }
}
