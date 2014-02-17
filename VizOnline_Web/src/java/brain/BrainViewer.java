package brain;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Date;

//import javax.media.opengl.GL2;
//import javax.media.opengl.GLProfile;
//import javax.media.opengl.fixedfunc.GLLightingFunc;
//import javax.media.opengl.glu.GLU;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;




import perspectives.Task;
import perspectives.Viewer3D;
import properties.PColor;
import properties.PFile;
import properties.PInteger;
import properties.PPercent;
import properties.Property;
import properties.PropertyType;
import properties.PString;
import util.Vector3D;


public class BrainViewer extends Viewer3D{
	

	Tube[] tubes;

	
	int[] totalNumVerts;
	int[] vbo;

	int vertexStride;
	int colorPointer;
	int vertexPointer;
	int normalPointer;
	   
	Vector3D[][] projectedSegments;
	
	int oldWidth = 0;
	
	BrainData data;
	
	float[] model = new float[16];
	float[] proj = new float[16];
	int[] viewport = new int[4];
	
	boolean created = false;	
	
	String selectedTubes = "";
	
	public BrainViewer(String name, BrainData dat) {
		super(name);
		this.data = dat;	
		
		System.out.println("creating brainviewer");
		
		createGeometry(6,0.01, Color.LIGHT_GRAY);
		
		try {
			Property<PInteger> ptubewidth = new Property<PInteger>("Appearance.TubeWidth", new PInteger(10));		
			this.addProperty(ptubewidth);
			
			Property<PInteger> ptubefaces = new Property<PInteger>("Appearance.TubeFaces", new PInteger(6));		
			this.addProperty(ptubefaces);
		
			Property<PColor> ptubecolor = new Property<PColor>("Appearance.TubeColor", new PColor(Color.LIGHT_GRAY));
			this.addProperty(ptubecolor);
			
			Property<PString> pselection = new Property<PString>("SelectedTubes", new PString(""));
			pselection.setPublic(true);
			pselection.setVisible(false);
			this.addProperty(pselection);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(" done creating brainviewer");
	}	
	
	


	@Override
	protected <T extends PropertyType> void propertyUpdated(Property p,
			T newvalue) {
		// TODO Auto-generated method stub
		if (p.getName() == "Appearance.TubeWidth")
		{
			double width = ((PInteger)newvalue).intValue();
			if (width <= 0) return;
			width *= 0.001;
			
			int faces = ((PInteger)getProperty("Appearance.TubeFaces").getValue()).intValue();
			
			Color color = ((PColor)getProperty("Appearance.TubeColor").getValue()).colorValue();
			createGeometry(faces, width, color);
		}
		else if (p.getName() == "Appearance.TubeFaces")
		{
			double width = ((PInteger)getProperty("Appearance.TubeWidth").getValue()).intValue();
			width *= 0.01;
			
			int faces = ((PInteger)newvalue).intValue();
			Color color = ((PColor)getProperty("Appearance.TubeColor").getValue()).colorValue();
			createGeometry(faces, width, color);		
		}
		else if (p.getName() == "Appearance.TubeColor")
		{
			double width = ((PInteger)getProperty("Appearance.TubeWidth").getValue()).intValue();
			width *= 0.01;
			
			int faces = ((PInteger)getProperty("Appearance.TubeFaces").getValue()).intValue();
			
			Color color = ((PColor)newvalue).colorValue();
			createGeometry(faces, width, color);		
		}
		else
			super.propertyUpdated(p, newvalue);
	}
	
	public boolean creating = false;
	private void createGeometry(int faces, double width, Color color)
	{	
		if (creating) return;
		creating = true;
		
		System.out.println("setup creating geometry");
		final int facesf = faces;
		final double widthf = width;
		final Color colorf = color;
		Task t = new Task("Create Tubes")
		{
			@Override
			public void task() {
				created = false;
				System.out.println("creating geom");
				
				tubes = new Tube[data.segments.length];
				for (int i=0; i<data.segments.length; i++)
					tubes[i] = new Tube(data.segments[i], facesf, widthf, colorf);
				
				created = true;
				
				vbo = null;
								
				done();
				
				creating = false;
				System.out.println("done creating geom");
			}			
		};
		t.blocking = true;
		t.indeterminate = true;
		this.startTask(t);
	}




	@Override
	public void render() {
		
		if (!created) return;
		
		if (oldWidth != width)
		{
			vbo = null;
			oldWidth = width;
		}		
		
		long ttt = new Date().getTime();		
		
		for (int i=0; i<changeColor.size(); i++)
		{
			Color c = changeColor.get(i);
			this.changeTubeColor(changeColorTube.get(i), c);
			
		}
		changeColor.clear();
		changeColorTube.clear();
		
		
        GL11.glShadeModel(GL11.GL_SMOOTH);
     //   GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
     //   GL11.glClearDepth(1.0f);
        GL11.glEnable( GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc( GL11.GL_LEQUAL);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT,  GL11.GL_NICEST);
        

		GL11.glPushMatrix();
		
		float[] savedModel = model; 
		float[] savedProj = proj;
		int[] savedViewport = viewport;
		
		
		FloatBuffer pmb = BufferUtils.createFloatBuffer(proj.length);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, pmb);
		pmb.get(proj);
		
		FloatBuffer mvb = BufferUtils.createFloatBuffer(model.length);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, mvb);
		mvb.get(model);
		
		IntBuffer vpb = BufferUtils.createIntBuffer(16);
		GL11.glGetInteger(GL11.GL_VIEWPORT, vpb);
		vpb.get(viewport);	 
		
		 
		GL11.glMatrixMode (GL11.GL_MODELVIEW);
		

		GL11.glPushMatrix();
		
		GL11.glLoadIdentity();		
		GL11.glTranslated(0, 0, -10);
	
		 
        float SHINE_ALL_DIRECTIONS = 1;
        FloatBuffer lightPos = BufferUtils.createFloatBuffer(4);
        lightPos.put(new float[]{0, 0, -10, SHINE_ALL_DIRECTIONS}); lightPos.rewind();
        FloatBuffer lightColorAmbient = BufferUtils.createFloatBuffer(4);
        lightColorAmbient.put(new float[]{0.01f, 0.01f, 0.01f, 1f}); lightColorAmbient.rewind();
        FloatBuffer lightColorSpecular = BufferUtils.createFloatBuffer(4);
        lightColorSpecular.put(new float[]{0.99f, 0.99f, 0.99f, 1f});lightColorSpecular.rewind();
        

        // Set light parameters.
        
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, lightPos);
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, lightColorAmbient);
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, lightColorSpecular);
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, lightColorSpecular);

        // Enable lighting in GL.
        GL11.glEnable(GL11.GL_LIGHT1);
        GL11.glEnable(GL11.GL_LIGHTING);

        // Set material properties.
        FloatBuffer rgba = BufferUtils.createFloatBuffer(4);
        rgba.put(new float[]{.1f, .1f, .1f, 1f});rgba.rewind();
       // gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, rgba, 0);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, rgba);
        //gl.glMateria
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 1f);
        
        GL11.glPopMatrix();
        
        init();
        
        if (vbo == null) 
        	{
        	//GL11.glPopMatrix();
        	return;
        	}
 
        for (int i=0; i<tubes.length; i++)
        {	
        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo[i]);

        	GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        	GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        	GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
	      
        	GL11.glColorMaterial( GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE );
        	GL11.glEnable(GL11.GL_COLOR_MATERIAL);

        	GL11.glColorPointer(3,GL11.GL_FLOAT, vertexStride, colorPointer);
        	GL11.glVertexPointer(3,GL11.GL_FLOAT, vertexStride, vertexPointer);
        	GL11.glNormalPointer(GL11.GL_FLOAT, vertexStride, normalPointer);

        	GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, totalNumVerts[i]);

        	GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        	GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        	GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        	GL11.glDisable(GL11.GL_COLOR_MATERIAL);

        	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }
			
		
	      GL11.glPopMatrix();
	      
	      System.out.println("brain viewer render time: " + (new Date().getTime()-ttt));
		
	}


	@Override
	public String getViewerType() {
		return "Viewer3D";
	}

	
	
	public void project()
	{
		if (!created)
			return;
					
		long ttt = new Date().getTime();
		GLU glu = new GLU();
		
	   	 FloatBuffer modelbuf = BufferUtils.createFloatBuffer(16);
    	 modelbuf.put(model); modelbuf.rewind();
    	 FloatBuffer projbuf = BufferUtils.createFloatBuffer(16);
    	 projbuf.put(proj); projbuf.rewind();
    	 IntBuffer viewportbuf = BufferUtils.createIntBuffer(viewport.length);
    	 viewportbuf.put(viewport); viewportbuf.rewind();
		
		projectedSegments = new Vector3D[tubes.length][];
		for (int i=0; i<tubes.length; i++)
		{
			projectedSegments[i] = new Vector3D[tubes[i].segments.length];
			for (int j=0; j<projectedSegments[i].length; j++)
			{
				FloatBuffer result = BufferUtils.createFloatBuffer(3);
				
				glu.gluProject(tubes[i].segments[j].x, tubes[i].segments[j].y, tubes[i].segments[j].z,
						modelbuf,  projbuf,  viewportbuf, 
						result);
			
				Vector3D p = new Vector3D(result.get(0), viewport[3] - result.get(1) -1 , result.get(2));
				
			//	int realy = viewport[3] - (int) y - 1;
				
				projectedSegments[i][j] = p;
			}
		}
		
		System.out.println("projected in: " + (new Date().getTime()-ttt));
	}
	
	boolean drag = false;
	int startX, startY;
	int endX, endY;
	
	
	public boolean mousepressed(int x, int y, int button)
	{
		if (button == 1)
		{
			selectedTubes = "";
			getProperty("SelectedTubes").setValue(new PString(selectedTubes));
			Color tubeColor = ((PColor)getProperty("Appearance.TubeColor").getValue()).colorValue();
			for (int i=0; i<tubes.length; i++)
			{
			//	System.out.println(tubes[i].color.getRed());
				if (tubes[i].color.getRed() == 255)
				{
					changeColor.add(tubeColor);
					changeColorTube.add(i);
				}
			}
			
			this.requestRender();
			
			project();
			
			drag = true;
			startX = x;
			startY = y;
			endX = x;
			endY = y;
		}
		return false;
	};
	
	public boolean mousereleased(int x, int y, int button)
	{
		if (button == 1) drag = false;
		return false;
	};
	
	ArrayList<Integer> changeColorTube = new ArrayList<Integer>();
	ArrayList<Color> changeColor = new ArrayList<Color>();
	public boolean mousedragged(int currentx, int currenty, int oldx, int oldy)
	{
		if (!created)
			return false;
		
		if (drag)
		{
			endX = currentx;
			endY = currenty;
			
			for (int i=0; i<projectedSegments.length; i++)
				for (int j=0; j<projectedSegments[i].length-1; j++)
				{
					Line2D.Double l1 = new Line2D.Double(startX, startY, endX, endY);
					Line2D.Double l2 = new Line2D.Double(projectedSegments[i][j].x, projectedSegments[i][j].y, projectedSegments[i][j+1].x, projectedSegments[i][j+1].y);
					
					if (l1.intersectsLine(l2))
					{						
						changeColor.add(Color.red);
						changeColorTube.add(i);
						
						if (selectedTubes.length() == 0)
							selectedTubes += i;
						else
							selectedTubes += "," + i;
						
						this.getProperty("SelectedTubes").setValue(new PString(selectedTubes));
						
						break;
					}
				}
			
			this.requestRender();
			
			return true;
		}
		return false;
	};
	
	public void render2DOverlay(Graphics2D g)
	{
		if (drag)
		{
			g.setColor(Color.red);
			g.setStroke(new BasicStroke(10));
			g.drawLine(startX,  startY, endX, endY);
		}
	}
	

	
	public void changeTubeColor(int tube, Color color)
	{
		float[] c = new float[]{color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f};
		
		tubes[tube].color = color;
		
	//	System.out.println("change color tube: " + tube + " , " + c[0] + "," + c[1] + "," + c[2]);
		 
		GL15.glDeleteBuffers(vbo[tube]);
		 
		totalNumVerts[tube] = tubes[tube].indeces.length;

		// generate a VBO pointer / handle
		      IntBuffer buf = BufferUtils.createIntBuffer(1);
		      GL15.glGenBuffers(buf);
		      vbo[tube] = buf.get();

		      // interleave vertex / color data
		      FloatBuffer data = BufferUtils.createFloatBuffer(tubes[tube].indeces.length * 9);
		      
		      for (int i = 0; i < tubes[tube].indeces.length; i++) {		        	
		            data.put(c);
		            
		            Vector3D vertex = tubes[tube].vertices[tubes[tube].indeces[i]];
		            Vector3D normal = tubes[tube].normals[tubes[tube].indeces[i]];
		            
		            float[] vertexf = new float[]{vertex.x, vertex.y, vertex.z};
		            float[] normalf = new float[]{normal.x, normal.y, normal.z};
		            	            
		            data.put(vertexf);
		            data.put(normalf);
		       
		      }
		      data.rewind();

		      int bytesPerFloat = Float.SIZE / Byte.SIZE;

		      // transfer data to VBO
		      int numBytes = data.capacity() * bytesPerFloat;
		      GL15.glBindBuffer( GL15.GL_ARRAY_BUFFER, vbo[tube]);
		      GL15.glBufferData( GL15.GL_ARRAY_BUFFER, data,  GL15.GL_STATIC_DRAW);
		      GL15.glBindBuffer( GL15.GL_ARRAY_BUFFER, 0);

	}

	   
	   public void init()
	   {
		   if (vbo != null)
			   return;
		
		   
		   vbo = new int[tubes.length];
		   totalNumVerts = new int[tubes.length];
		   
		   for (int k=0; k<tubes.length; k++)
			   this.changeTubeColor(k, tubes[k].color);

		   int bytesPerFloat = Float.SIZE / Byte.SIZE;
		      vertexStride = 9 * bytesPerFloat;
		      
		      colorPointer = 0;
		      vertexPointer = 3 * bytesPerFloat;
		      normalPointer = 6* bytesPerFloat;
		   
		 
	   }

}
