package perspectives.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;


public class ViewerWindow extends JInternalFrame  {

	JPanel drawArea = null;	
	
	ViewerContainer viewerContainer;
	
	private ViewerGUI vg;
	
	boolean dragging = false;
	
	public void redraw()
	{
		drawArea.repaint();
	}
	
	public ViewerWindow(ViewerContainer vc)
	{
		super(vc.viewer.getName(), true,true,true,true);
		
		viewerContainer = vc;	
		vc.window = this;
		
		if (viewerContainer.viewer.getViewerType() == "ViewerGUI")
		{
			drawArea = new JPanel();			
			vg = (ViewerGUI)viewerContainer.viewer;			
			vg.setContentPanel(drawArea);
			vg.init();	
		}
		
		if (vc.viewer.getViewerType() == "Viewer2D" || vc.viewer.getViewerType() == "Viewer3D")
		{	
			drawArea = new JPanel()
			{
				//our drawArea for 2d viewers will always paint 'finalImage'; this finalImage gets updated by MySwingWorker
				 public void paintComponent(Graphics g) {
				        super.paintComponent(g);				        
				        Graphics2D g2 = (Graphics2D)g; 					        
				        g2.drawImage(viewerContainer.getImage(), null, 0, 0);				 
				    }
			};			
			
			//for 2D viewers we add automatic zooming and panning; this can happen using the arrow keys and +,- keys; the key strokes are also sent to the viewer as interaction events
			drawArea.setFocusable(true);
			drawArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
			
			drawArea.addKeyListener(new KeyListener()
			{
				public void keyPressed(KeyEvent e)
				{
					viewerContainer.scheduleKeyPress(e.getKeyText(e.getKeyCode()), e.getModifiersExText(e.getModifiersEx()));
				}

				public void keyReleased(KeyEvent e)
				{
					viewerContainer.scheduleKeyRelease(e.getKeyText(e.getKeyCode()), e.getModifiersExText(e.getModifiersEx()));	
				}
				public void keyTyped(KeyEvent e) {
					viewerContainer.keyTyped(e);
				}				
			});
			
			//we add listeners both to send those events to the 2D viewer but also to implement zooming an panning; there's a 25pixel offset on the y-axes because of the title bar of ViewerContainers; for zooming and panning we use
			//the raw coordinates; when passing the coordinates to the viewer they need to be transformed so they are in the Viewer's space, hence the operations based on translate and zoom.
			drawArea.addMouseListener(new MouseListener()
			{
				public void mouseClicked(MouseEvent e) {
				}
				public void mouseEntered(MouseEvent e) {
					//viewerContainer.mouseEntered(e.getX(), e.getY());
				}
				public void mouseExited(MouseEvent e) {
					//viewerContainer.mouseExited(e.getX(), e.getY());
				}
				public void mousePressed(MouseEvent e) {					
					viewerContainer.scheduleMousePress(e.getX(), e.getY(), e.getButton());
					drawArea.requestFocus();
				}
				public void mouseReleased(MouseEvent e)
				{
					viewerContainer.scheduleMouseRelease(e.getX(), e.getY(), e.getButton());
				}			
			});
			
			drawArea.addFocusListener(new FocusListener()
			{
				@Override
				public void focusGained(FocusEvent arg0) {
					drawArea.setBorder(BorderFactory.createEtchedBorder(Color.darkGray,Color.gray));
				}

				@Override
				public void focusLost(FocusEvent arg0) {
					drawArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));	
				}
				
			});
			
			drawArea.addMouseMotionListener(new MouseMotionListener()
			{
				public void mouseDragged(MouseEvent e) {					
					viewerContainer.scheduleMouseDrag(e.getX(), e.getY());				
				}
				public void mouseMoved(MouseEvent e){
					viewerContainer.scheduleMouseMove(e.getX(), e.getY());
				}			
			});			
			
		}
		
		//property list		
		this.getContentPane().setLayout(new BorderLayout());
		
		final JPanel sidePanel = new JPanel();
		sidePanel.setLayout(new BorderLayout());
		
	    final JPanel pp = new JPanel();
	    pp.setLayout(new BoxLayout(pp,BoxLayout.Y_AXIS));	   
	    pp.setBorder(BorderFactory.createEmptyBorder());
	    
	    pp.add(Box.createVerticalStrut(10));
	    
	    final JButton hideButton = new JButton();
		hideButton.setSelected(true);
		hideButton.setFocusable(false);
	    hideButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("hide.png")));
	    hideButton.setMaximumSize(new Dimension(12,45));
	    hideButton.setPreferredSize(new Dimension(12,50));
	    hideButton.setForeground(Color.YELLOW);      
	    
	    
	    final Viewer vf = viewerContainer.viewer;
	    
	   final JScrollPane listScrollPane1 = new JScrollPane(pp);   
	    
	   final JInternalFrame ff = this;
	   
	   final JToggleButton showButton = new JToggleButton();
	   showButton.setSelected(true);
	   showButton.setFocusable(false);
	   showButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("show.png")));
	   showButton.setMaximumSize(new Dimension(12,10000));
	   showButton.setPreferredSize(new Dimension(12,50));
	   showButton.setBackground(Color.YELLOW);
	  
	 
	    
		hideButton.addActionListener(new ActionListener() {	    	   
	    	   public void actionPerformed(ActionEvent e) {
	    		 ff.remove(sidePanel);
	    		 ff.add(showButton,BorderLayout.WEST);
	    		 hideButton.setSelected(true);
	    		 ff.revalidate();
			      }			      
	       }); 
		
		showButton.addActionListener(new ActionListener() {	    	   
	    	   public void actionPerformed(ActionEvent e) {
	    		 ff.remove(showButton);
	    		 showButton.setSelected(true);
	    		 ff.add(sidePanel,BorderLayout.WEST);
	    		 ff.revalidate();
			      }			      
	       });      
	    
	 
		PropertyManagerViewer pmviewer = new PropertyManagerViewer(viewerContainer.viewer);
	    
	    pp.add(pmviewer);
	    pp.add(Box.createVerticalGlue());
	      
	    
	    listScrollPane1.setBorder(BorderFactory.createEmptyBorder());	       
	    listScrollPane1.setPreferredSize(new Dimension(225,200));
	    
	    pp.setBackground(new Color(240,240,255));
	    
	    
	    sidePanel.add(listScrollPane1, BorderLayout.WEST);
	    sidePanel.setBackground(Color.CYAN);
	    sidePanel.add(hideButton, BorderLayout.EAST);	    
	    
		this.add(sidePanel,BorderLayout.WEST);			
		this.add(drawArea, BorderLayout.CENTER);
		
		this.setVisible(true);		
		setBorder(BorderFactory.createLineBorder(new Color(180,180,250)));
				
		
		//if a user clicks inside a viewer this becomes a focused viewer via Environment; if deleted it gets deleted from the Environment.
		this.addInternalFrameListener(new InternalFrameListener()
		{
			public void internalFrameActivated(InternalFrameEvent arg0) {
				viewerContainer.getEnvironment().setFocusedViewer(viewerContainer.viewer);
			}
			public void internalFrameClosed(InternalFrameEvent arg0) {
				viewerContainer.getEnvironment().deleteViewer(viewerContainer.viewer);
			}
			public void internalFrameClosing(InternalFrameEvent arg0) {
			}
			public void internalFrameDeactivated(InternalFrameEvent arg0) {
			}
			public void internalFrameDeiconified(InternalFrameEvent arg0) {
			}
			public void internalFrameIconified(InternalFrameEvent arg0) {
			}
			public void internalFrameOpened(InternalFrameEvent arg0) {
			}
              
        } );  
		
		this.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				viewerContainer.resize(ff.getWidth(), ff.getHeight());				
				
			}
		});
		
	}
	
}
