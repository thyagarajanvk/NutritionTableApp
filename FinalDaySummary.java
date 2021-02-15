import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class FinalDaySummary extends JPanel implements ActionListener{
    
      
	private JTable finalDaySummary;
    private JScrollPane sumTableScroll;
    private JButton exitProgram;

    public FinalDaySummary(DefaultTableModel t){

        finalDaySummary = new JTable(t){
        	
			public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        
        

        finalDaySummary.getColumnModel().getColumn(0).setPreferredWidth(400);
        finalDaySummary.getColumnModel().getColumn(7).setPreferredWidth(200);

        
        sumTableScroll = new JScrollPane(finalDaySummary);    
        this.add(sumTableScroll);          
        this.setVisible(true);
        
        
        sumTableScroll.setBorder(BorderFactory.createTitledBorder ("Final Day's Summary Table"));

        
        // Adding a solutions button
        exitProgram = new JButton("Exit");
        exitProgram.addActionListener(this);  
        this.add(exitProgram);

        
    }

    public void actionPerformed(ActionEvent e){

        if(e.getSource()==exitProgram){
            System.exit(0);
        } 
        

    }


}


