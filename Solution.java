import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class Solution extends JPanel implements ActionListener{

	private int numberOfFoodItems = 14164; 
    private int numberOfParameters = 8;    
    private static Summary sum;
    private static Entry entry;
    
    private JTable databaseTable, sourceTable;
    private JButton chooseToAdd, buttonToGoBack;
    private JScrollPane solutionScroll;
    
    
    private BufferedReader br;
    private InputStream in;
    
    private int[] originalPosition = new int[numberOfFoodItems];
    private String[][] sortingData, sortedDataTable, dataCopy;
    private int nutrientSolution;
    private boolean selected = true;
    private String lineOfFile;
    private String[] values;
    
    

    public Solution(Summary s){
        sum = s;
    }

    public Solution(Entry e){
        entry = e;
    }

    
    
    public Solution(){

        // Construct the days nutrition table        

        sortingData = getDataCopy();

        // Initializing the position array that keeps track of original position for faster updating of the entry table in Entry class
        for (int i = 0; i < sortingData.length;i++){
            originalPosition[i] = i;
        }
            
        sortedDataTable = sort(sum.getSpecificNutrient() + 1, numberOfFoodItems, sortingData);

        databaseTable = new JTable(sortedDataTable,entry.getHeaders()){
      			public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        databaseTable.getColumnModel().getColumn(0).setPreferredWidth(400);
        databaseTable.getColumnModel().getColumn(7).setPreferredWidth(200);

        // Formatting
        databaseTable.setBounds(30,40,200,300);
        solutionScroll = new JScrollPane(databaseTable);    
        this.add(solutionScroll);          
        solutionScroll.setBorder(BorderFactory.createTitledBorder ("Solutions Table"));

        // Adding a solutions button
        chooseToAdd = new JButton("Add this item to daily entries");
        chooseToAdd.setAlignmentX(Component.CENTER_ALIGNMENT);	  
        chooseToAdd.addActionListener(this);  
        this.add(chooseToAdd);

        // Adding a button that goes back
        buttonToGoBack = new JButton("Go Back");
        buttonToGoBack.setAlignmentX(Component.CENTER_ALIGNMENT);	  
        buttonToGoBack.addActionListener(this);  
        this.add(buttonToGoBack);


         // Add a mouse listener so that the row of the cell selected by the mouse is stored in a value
        databaseTable.addMouseListener( new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                            
                sourceTable = (JTable) e.getSource();
                nutrientSolution = sourceTable.rowAtPoint( e.getPoint() );
                selected = true;
            }
        });

   
    }

    public String[][] getDataCopy(){

        dataCopy = new String[numberOfFoodItems][numberOfParameters];

        try{
        	
        	in = getClass().getResourceAsStream("table.csv"); 
        	br = new BufferedReader(new InputStreamReader(in));
            
    
            for (int rows = 0; rows < numberOfFoodItems; rows++){
                lineOfFile = br.readLine();
                values = lineOfFile.split(",");
                for (int columns = 0; columns < numberOfParameters; columns++){
                    dataCopy[rows][columns] = values[columns];
                }
            }
    
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return dataCopy;
    
    }

    public void actionPerformed(ActionEvent e){

        if (e.getSource()==chooseToAdd && selected){
            sum.setUpdatedEntryTable(originalPosition[nutrientSolution]);

            //Game.cardsL.previous(Game.c);
            backToTheSummary(sum.getBackCounter());

        } else if (e.getSource() == buttonToGoBack){
            //Game.cardsL.previous(Game.c);    
            backToTheSummary(sum.getBackCounter());
        } 
        
    }

    public String[][] sort(int specificNutrientColumnNumber, int recursionOrdinal, String[][] s){
        for (int i = recursionOrdinal - 1; i > 0; i--){
            if (toDouble(s[i][specificNutrientColumnNumber]) > toDouble(s[i-1][specificNutrientColumnNumber])){
                String temp[] = s[i];
                s[i] = s[i-1];
                s[i-1]= temp;


                //Swapping the position array elements
                int positionTemp = originalPosition[i];
                originalPosition[i] = originalPosition[i-1];
                originalPosition[i-1] = positionTemp;

            }
        }



        if (recursionOrdinal > numberOfFoodItems-1000){
            sort(specificNutrientColumnNumber, recursionOrdinal-1, s);
        } 
        
        return s;
        
    }

    

    public double toDouble(String toBeConverted){
    	try {
    		return Double.parseDouble(toBeConverted);
    	} catch (NumberFormatException e){
    		return 0;
    	}
    }

    public void backToTheSummary(int n){
        if (n == 0){
            return;
        }

        Nutrion.cardsL.previous(Nutrion.c);
        backToTheSummary(n-1);    
    }

    



}


