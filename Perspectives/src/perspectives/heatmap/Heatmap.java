package perspectives.heatmap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import javax.xml.crypto.Data;
import perspectives.*;


public class Heatmap{

	/*FloatMatrix matrix;
	
	int size = 100;
	int dir = 1;
		
	public Heatmap(String name) {
		super(name);
	}
	
	public Heatmap(String name, FloatMatrix m)
	{
		super(name);
		
		try {
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		matrix = m;
	}
	
	public void render(Graphics2D g)
	{
		float max = Float.MIN_VALUE;
		float min = Float.MAX_VALUE;
		
		for (int i=0; i<matrix.width(); i++)
			for (int j=0; j<matrix.height(); j++)
			{
				if (matrix.get(i,j) < min) min = matrix.get(i,j);
				if (matrix.get(i,j) > max) max = matrix.get(i,j);
			}
		
		for (int i=0; i<matrix.width(); i++)
			for (int j=0; j<matrix.height(); j++)
			{
				float v = (matrix.get(i,j)-min)/(max-min);
				
				int vc = (int)(255*v);
				
				g.setColor(new Color(vc,vc,vc));
				g.fillRect(i*size, j*size, size, size);
			}
		
	}
	
	public <T> void propertyUpdated(Property p, T newvalue)
	{

	}
	
	public boolean mousepressed(int x, int y, int button)
	{		
		return false;
	};
	
	public boolean mousereleased(int x, int y, int button)
	{		
		return true;
	};
	
	public boolean mousedragged(int currentx, int currenty, int oldx, int oldy)
	{

		return false;
	};

	@Override
	public void simulate() {
		
		if (size <= 97)
			dir = -dir;
		else if (size >= 103)
			dir = -dir;
		
		size = size + dir;
		
	}
        */
}
