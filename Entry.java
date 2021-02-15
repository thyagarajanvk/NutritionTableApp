import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class Entry extends JPanel implements ActionListener{
    
	private int numberOfParameters = 8;    
    private int numberOfFoodItems = 14164; 
    
    private boolean[] alreadyAdded;
    private boolean selected = false;
    private int row;
    private String chosenEntries = "";
    private String lineOfFile;
    private String[][] data;
    private String[] headers, values;
    private BufferedReader br;
    private InputStream in;
    
    
    private JScrollPane entryScroll;
    private JButton backButton, chooseEntry, finishChoosing;
    private JTable entryTable, sourceTable;
    
    
    private Summary sum;
    private Solution solution;
    private Solution problemFixer;

    
    
    public Entry(){


        this.setBackground(Color.LIGHT_GRAY);
        this.add(Box.createRigidArea(new Dimension(0, 130)));
    
        // Initializing the already added array
        alreadyAdded = new boolean[numberOfFoodItems];

        // Initializing the data array
        headers = new String[]{"Name","Calories","Fat", "Protein", "Carbohydrate", "Sugar", "Fiber", "Cholesterol"};      
        data = new String[numberOfFoodItems][numberOfParameters];

        try{
        	
        	
        	in = getClass().getResourceAsStream("table.csv"); 
        	br = new BufferedReader(new InputStreamReader(in));
            

            for (int rows = 0; rows < numberOfFoodItems; rows++){
                lineOfFile = br.readLine();
                values = lineOfFile.split(",");
                for (int columns = 0; columns < numberOfParameters; columns++){
                    data[rows][columns] = values[columns];
                }
            }
            
            br.close();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        
        // Table constructing
        entryTable = new JTable(data, headers){
            

			public boolean isCellEditable(int row, int column){
                return false;
            }
        };    
        
        
        entryTable.getColumnModel().getColumn(0).setPreferredWidth(400);
        entryTable.getColumnModel().getColumn(7).setPreferredWidth(200);
        
        // Scroll pane constructing
        entryScroll = new JScrollPane(entryTable);    
        this.add(entryScroll);
        entryScroll.setBorder(BorderFactory.createTitledBorder ("Entry Table"));

         
    

    
        

        // Button constructing
        backButton = new JButton("Back to main menu");
        backButton.addActionListener(this);  
        this.add(backButton);
            
		
		


        chooseEntry = new JButton("Add to the daily entries");
        chooseEntry.addActionListener(this);  
        this.add(chooseEntry);

        finishChoosing = new JButton("Finished daily entries?");
        finishChoosing.addActionListener(this);  
        this.add(finishChoosing);


        // Listening to mouse click
        entryTable.addMouseListener( new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                sourceTable = (JTable) e.getSource();
                row = sourceTable.rowAtPoint( e.getPoint() );
                selected = true;
            }
        });

        

    
    }

   

    public void actionPerformed(ActionEvent e) {

		// Utilisation of card layout
		if(e.getSource()==backButton){
			Nutrion.cardsL.previous(Nutrion.c);
        }   
        if(e.getSource()==chooseEntry){
            if (alreadyAdded[row] == false && selected){
                chosenEntries += row + ",";
                alreadyAdded[row] = true;        
            } 
            
        }
        if (e.getSource() == finishChoosing && !chosenEntries.equals("")){
            sum = new Summary();
            Nutrion.c.add(sum);
            solution = new Solution(sum);
            
            Nutrion.cardsL.next(Nutrion.c);
        } 
    }







    // Getter methods
    public String getEntries(){
        return chosenEntries;
    }    

    public String[] getData(int r){
        return data[r];
    }

    public String[] getHeaders(){
        return headers;
    }

    public String[][] getDataTable(){
        return data;
    }

    // Setter methods
    public void setChosenEntries(int update){
        chosenEntries += update + ",";
    }
    
    


    

}



