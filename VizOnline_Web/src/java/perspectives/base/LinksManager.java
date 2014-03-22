package perspectives.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class LinksManager extends JDialog
{
	//pointer to the parent Environment
	private Environment env = null;
		
	public LinksManager(Environment envv)
	{
		this.env = envv;		
		
		this.requestFocus();
		this.setAlwaysOnTop(true);
		this.setModal(true);
		
		this.setTitle("Links");
		
		 Toolkit tool = Toolkit.getDefaultToolkit();		 
		 Image icon = tool.getImage("image/links.png");
		this.setIconImage(icon);
		
		this.setBounds(200, 200, 250, 300);
		 
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		Viewer[][] links = env.getLinks();
		Vector<Viewer> viewers = env.getViewers();
		
		this.getContentPane().add(Box.createVerticalStrut(10));
		for (int i=0; i<viewers.size()-1; i++)
		{
			for (int j=i+1; j<viewers.size(); j++)
			{
				final Viewer v1 = viewers.get(i);
				final Viewer v2 = viewers.get(j);
				boolean isLink = false;
				for (int k=0; k<links.length; k++)
					if ((links[k][0] == v1 && links[k][1] == v2) || (links[k][1] == v1 && links[k][0] == v2))
						isLink = true;
				
				
				JPanel oneLink = new JPanel();
				oneLink.setLayout(new BoxLayout(oneLink, BoxLayout.X_AXIS));
				oneLink.add(Box.createHorizontalGlue());
				oneLink.setMaximumSize(new Dimension(1000,50));
				
				String label = "Link";
				if (isLink) label = "Unlink";
				
				final JButton create = new JButton(label);
			    create.addActionListener(new ActionListener()
			    {
					public void actionPerformed(ActionEvent arg0) {				
						if (create.getText().equals("Link"))
						{
							env.linkViewers(v1,v2, true);
							create.setText("Unlink");
						}
						else
						{
							env.unlinkViewers(v1, v2);
							create.setText("Link");
						}
					}	    	
			    });    
				
				JLabel v1l = new JLabel(viewers.get(i).getName());
				JLabel v2l = new JLabel(viewers.get(j).getName());
				
				oneLink.add(v1l); oneLink.add(Box.createHorizontalStrut(20));
				oneLink.add(create); oneLink.add(Box.createHorizontalStrut(20));
				oneLink.add(v2l); oneLink.add(Box.createHorizontalGlue());
				
				
				this.getContentPane().add(oneLink);
				this.getContentPane().add(Box.createVerticalStrut(10));
			}
		}
		
		this.getContentPane().add(Box.createVerticalGlue());
		this.getContentPane().setBackground(Color.white);
		this.revalidate();
		
				
	
		
		
		
	
	
	 
	    
   
	 
		
	}

}

