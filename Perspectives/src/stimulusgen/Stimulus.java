package stimulusgen;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Stimulus {
	public String id;
	public String imagePath;
	
	private BufferedImage image;
	
	boolean aggregate;
	String fromUser;
	
	public Stimulus(String id)
	{
		this.id = id;
	}

	public Stimulus(String id, boolean aggregate)
	{
		this.aggregate = aggregate;
	}
	
	
	
	
	public BufferedImage getImage()
	{
		if (image != null)
			return image;
		
		try {
			System.out.println(imagePath);
			image = ImageIO.read(new File(imagePath));
			
			if (image.getWidth() > 1600)
			{
				double f = 1600./image.getWidth();
				int h = (int)(f * image.getHeight());
				BufferedImage bim = new BufferedImage(1600, h, BufferedImage.TYPE_INT_ARGB);
				bim.createGraphics().drawImage(bim, 0,0,null);
				image = bim;
			}
			
			else if (image.getHeight() > 900)
			{
				double f = 900./image.getHeight();
				int w = (int)(f * image.getWidth());
				BufferedImage bim = new BufferedImage(w, 900, BufferedImage.TYPE_INT_ARGB);
				bim.createGraphics().drawImage(bim, 0,0,null);
				image = bim;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return image;
	}
	
	public void releaseImage()
	{
		image = null;
	}
	
	public void fromFile(String filename)
	{
		
	}
}
