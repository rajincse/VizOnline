package perspectives.three_d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.Date;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.ARBPixelBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL44;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

import perspectives.base.Environment;
import perspectives.base.PEvent;
import perspectives.base.ViewerContainer;
import perspectives.three_d.ObjectController3D;

/**
 * This is a window class for Viewers. It will be able to accommodate any of the
 * three main types of Viewers: 2D, 3D, GUI. It will call their rendering,
 * simulation, and interactivity functions (if appropriate), it will implement
 * double buffering for them, and it can be added to the viewer area of the
 * Environment. ViewerContainers automatically implementing panning and zooming
 * for 2D viewers via 2dtransforms applied to the Graphics2D objects passed onto
 * the Viewer2D's render function.
 *
 * @author rdjianu
 *
 */
public class ViewerContainer3D extends ViewerContainer {

    //a pointer to the parent Environment class (needed for instance to delete the viewer from the Environment if the user activates the 'X')
    final Environment env;
    int dragPrevX;
    int dragPrevY;
    int renderCount = 0;
    ObjectController3D controller;
    int oldx = 0, oldy = 0;
    Pbuffer pbuffer;

    public ViewerContainer3D(Viewer3D v, Environment env, int width, int height) {
        super(v, env, width, height);

        System.out.println("creating viewer cont 3D");

        this.env = env;

        //GLProfile glp = GLProfile.getDefault();	 

        System.out.println("creating viewer cont 3D _ 1");

        try {

            pbuffer = new Pbuffer(width, height, new PixelFormat(8,8,1), null, null);

            System.out.println("creating viewer cont 3D _ 2");

            pbuffer.makeCurrent();

            System.out.println("creating viewer cont 3D _ 3");

            System.out.println("creating viewer cont 3D _ 31");

            System.out.println("creating viewer cont 3D _ 32");

            System.out.println("creating viewer cont 3D _ 33");

            // int pbo1 = ARBBufferObject.glGenBuffersARB();
            // int pbo2 = ARBBufferObject.glGenBuffersARB();


            //  ARBBufferObject.glBindBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB, pbo1);
            // ARBBufferObject.glBufferDataARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB, 1800*1200*4, ARBPixelBufferObject.GL_STREAM_READ_ARB);

            //  ARBBufferObject.glBindBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB, pbo2);
            // ARBBufferObject.glBufferDataARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB, 1800*1200*4, ARBPixelBufferObject.GL_STREAM_READ_ARB);

            //unbind?
            //  ARBBufferObject.glBindBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB, 0);

            controller = new ObjectController3D();

            System.out.println("creating viewer cont 3D _ 4");

            pbuffer.releaseContext();

            System.out.println("creating viewer cont 3D _ 5");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render() {
        if (renderCount >= 1) {
            return;
        }

        renderCount++;

        final ViewerContainer3D th = this;
        viewer.em.scheduleEvent(new PEvent() {
            public void process() {
                BufferedImage image = th.display();
                ((Viewer3D) viewer).render2DOverlay(image.createGraphics());
                renderDoneCallback(image);

            }
        });
    }

    public void renderDoneCallback(BufferedImage image) {
        renderCount--;
        setViewerImage(image);
        if (window != null) {
            this.window.redraw();
        }
    }
    Object o = new Object();

    public void resize(int w, int h) {
        synchronized (o) {
            super.resize(w, h);
            try {
                Pbuffer old = pbuffer;
                pbuffer = new Pbuffer(w, getHeight(), new PixelFormat(8,8,1), null, null);

                old.destroy();
            } catch (LWJGLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//            ((Viewer3D) viewer).setWidth(w);
        }
    }

    protected void mouseMoved(int x, int y) {
        lastMouseMove = new Date().getTime();
        ((Viewer3D) viewer).mousemoved(x, y);
    }

    protected void mouseDragged(int x, int y) {
        if (!((Viewer3D) viewer).mousedragged(x, y, oldx, oldy)) {
            controller.mouseDragged(x, y, pbuffer);
            viewer.requestRender();
        }
        oldx = x;
        oldy = y;
    }


	protected void mousePressed(int x, int y, int button)
	{
		if (!((Viewer3D)viewer).mousepressed(x, y, button))
		{
			controller.mousePressed(x, y,button, pbuffer);	
			viewer.requestRender();
		}
		oldx = x;
		oldy = y;

	}
	
	
	protected void mouseReleased(int x, int y, int button)
	{

		if (!((Viewer3D)viewer).mousereleased(x, y, button))
		{
			controller.mouseReleased(x, y, button, pbuffer);		
			viewer.requestRender();
		}
	}
	
	@Override
	protected void keyPressed(String keyText, String modifiersText)
	{
	
		controller.keyPressed(keyText, modifiersText, pbuffer);			
		viewer.requestRender();
	}
	
	@Override
	protected void keyReleased(String keyText, String modifiersText)
	{
		controller.keyReleased(keyText, modifiersText, pbuffer);			
		viewer.requestRender();
	}

    ByteBuffer pixelsRGB = null;

    public BufferedImage display() {
        synchronized (o) {
            try {
                this.pbuffer.makeCurrent();
                System.out.println(":::display ::: No Exception 1");
            } catch (LWJGLException e) {
                // TODO Auto-generated catch block
                System.out.println(":::display ::: Exception 1");
                e.printStackTrace();
            }


            // setup gl
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GLU.gluPerspective(60f, width / (float) height, 1f, 100.0f);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            System.out.println(":::display ::: No Exception 2");
            DoubleBuffer db1 = BufferUtils.createDoubleBuffer(16);
            db1.put(new double[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, -5, 1});
            db1.rewind();
            GL11.glLoadMatrix(db1);
            DoubleBuffer db2 = BufferUtils.createDoubleBuffer(16);
            db2.put(controller.mvmatrix);
            db2.rewind();
            GL11.glMultMatrix(db2);
            System.out.println(":::display ::: No Exception 2");

            GL11.glViewport(0, 0, width, height);
            GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            System.out.println(":::display ::: No Exception 3");
            ((Viewer3D) viewer).render();

            System.out.println(":::display ::: No Exception 4");

            //  controller.render();


            BufferedImage image = this.glToImage();
            System.out.println(":::display ::: No Exception 5");
            try {
                pbuffer.releaseContext();
                System.out.println(":::display ::: No Exception 6");
                return image;
            } catch (LWJGLException e) {
                // TODO Auto-generated catch block
                System.out.println(":::display ::: Exception 2");
                e.printStackTrace();
                BufferedImage image2 = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                return image2;
            }
        }


    }
    int pbo1, pbo2;

    private void glToImagePBO() {
        BufferedImage image = new BufferedImage(1800, 1200, BufferedImage.TYPE_INT_ARGB);

        int[] pixelInts = new int[1800 * 1200];

        // set the framebuffer to read
        GL11.glReadBuffer(GL11.GL_FRONT);


        //create a read to the "next" buffer, pbo 2
        ARBBufferObject.glBindBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_BINDING_ARB, pbo2);

        long ttt = new Date().getTime();
        GL11.glReadPixels(0, 0, 1800, 1200, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, 0);
        System.out.println("time: " + (new Date().getTime() - ttt));

        //process the data from the "current" buffer, pbo1
        ARBBufferObject.glBindBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_BINDING_ARB, pbo1);
        if (pixelsRGB == null) {
            pixelsRGB = ARBBufferObject.glMapBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB, ARBPixelBufferObject.GL_READ_ONLY_ARB, null);
        } else {
            pixelsRGB = ARBBufferObject.glMapBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB, ARBPixelBufferObject.GL_READ_ONLY_ARB, pixelsRGB);
        }


        if (pixelsRGB == null) {
            //switch the buffers and exit
            int aux = pbo1;
            pbo1 = pbo2;
            pbo2 = aux;
            return;
        }

        ARBBufferObject.glUnmapBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB);     // release pointer to the mapped buffer		    

        //switch the buffers to prepare for the next frame.
        int aux = pbo1;
        pbo1 = pbo2;
        pbo2 = aux;

        //put the pixels in the right format
        int p = width * height * 4; // Points to first byte (red) in each row.
        int q;                  // Index into ByteBuffer
        int i = 0;                  // Index into target int[]
        int w3 = width * 4;         // Number of bytes in each row

        for (int row = 0; row < height; row++) {
            p -= w3;
            q = p;
            for (int col = 0; col < width; col++) {

                int iB = pixelsRGB.get(q++);
                int iG = pixelsRGB.get(q++);
                int iR = pixelsRGB.get(q++);

                int iA = pixelsRGB.get(q++);

                pixelInts[i++] = 0xFF000000
                        | ((iR & 0x000000FF) << 16)
                        | ((iG & 0x000000FF) << 8)
                        | (iB & 0x000000FF);
            }

        }

        image.setRGB(0, 0, width, height, pixelInts, 0, width);
    }

    private BufferedImage glToImage() {

        try {


            //ByteBuffer pixelsRGB = BufferUtils.createByteBuffer(width * height * 4);
            //IntBuffer pixelsRGB = BufferUtils.createIntBuffer(width * height * 4);
            ByteBuffer pixelsRGB = ByteBuffer.allocateDirect(width * height * 4);

            long ttt1 = new Date().getTime();

            //GL11.glReadBuffer(GL11.GL_BACK);	        
            GL11.glReadBuffer(GL11.GL_FRONT);

            //  

//                ARBBufferObject.glBindBufferARB(ARBPixelBufferObject.GL_PIXEL_PACK_BUFFER_ARB, 0);

            //  GL11.glBindBuffer(GL11.GL_, vertexPointerTri);
            //GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 1);
            GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);

            System.out.println("::GLError():  " + GL11.glGetError() + " width-height: " + width + "-" + height);
            //   GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL11.GL_INT, pixelsRGB);
            // GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL11.GL_UNSIGNED_SHORT, pixelsRGB);
            //GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixelsRGB);
            GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixelsRGB);
       
            System.out.println("::GLError():  " + GL11.glGetError());

            System.out.println(":::glToImage ::: No Exception 1");
            long ttt2 = new Date().getTime();

            //put the pixels in the right format
            int p = width * height * 4; // Points to first byte (red) in each row.
            int q;                  // Index into ByteBuffer
            int i = 0;                  // Index into target int[]
            int w3 = width * 4;         // Number of bytes in each row

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            int[] pixelInts = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

            System.out.println(":::glToImage ::: No Exception 2");
            for (int row = 0; row < height; row++) {
                p -= w3;
                q = p;
                for (int col = 0; col < width; col++) {

                    int iB = pixelsRGB.get(q++);
                    int iG = pixelsRGB.get(q++);
                    int iR = pixelsRGB.get(q++);	                
                    //int iR = 250;

                    int iA = pixelsRGB.get(q++);


                    /*  int iR = pixelsRGB.get(q++);	             
	            
                     int iG = pixelsRGB.get(q++);
                     int iB = pixelsRGB.get(q++);
                        
                     int iA = pixelsRGB.get(q++); */

                    pixelInts[i++] = 0xFF000000
                            | ((iR & 0x000000FF) << 16)
                            | ((iG & 0x000000FF) << 8)
                            | (iB & 0x000000FF);
                }

            }




            long ttt3 = new Date().getTime();


            long ttt4 = new Date().getTime();
            System.out.println("write buffer to image: " + (ttt2 - ttt1) + " " + (ttt3 - ttt2) + " " + (ttt4 - ttt3));
            System.out.println(":::glToImage ::: No Exception 3");
            return image;

        } catch (Exception e) {
            System.out.println(":::glToImage :: Exception 1");
            e.printStackTrace();
            BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.createGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, 100, 100);
            return image;
        }

    }
}
