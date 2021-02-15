import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Main_menu extends JPanel implements ActionListener{
	
	private JButton today, exit; 
	
	public Main_menu(){    
		
		today= new JButton("Today's entries");

		exit= new JButton("Exit");

		// Box layout
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Buttons for navigation
		this.add(Box.createRigidArea(new Dimension(0, 130)));
		
		
		today.setAlignmentX(Component.CENTER_ALIGNMENT);	    
		this.add(today);
		this.add(Box.createRigidArea(new Dimension(0, 45)));
    
        
			
		exit.setAlignmentX(Component.CENTER_ALIGNMENT);	 
		this.add(exit);
		this.add(Box.createRigidArea(new Dimension(0, 45)));
		
		// actions for buttons
        today.addActionListener(this);
        exit.addActionListener(this);


		this.setBackground(Color.CYAN);
	}

	public void actionPerformed(ActionEvent e) {

		// Utilisation of card layout
		if(e.getSource()==today){
			Nutrion.cardsL.next(Nutrion.c);
		} else if (e.getSource()==exit){
			System.exit(0);
		}   
	}
	
	// Drawing title
	public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.fillRect(0,0,3000,100);
        g.setColor(Color.YELLOW);        
        
		g.setFont(new Font("TimesRoman", Font.PLAIN, 30));  
		g.drawString("Nutrion",20, 50);  
    }   
}

