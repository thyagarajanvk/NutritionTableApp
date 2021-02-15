import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Arrays;
import javax.swing.table.*;

public class Summary extends JPanel implements ActionListener{
    
    
	private String[] entries;
    private int numberOfParameters = 8;    
    private boolean selected = false;
    private int backCounter = 0;
    
    private String[][] daysNutritionTable;
    private String[][] displayArray = new String[1][numberOfParameters-1];
    private String [][] oldData;

    private double[] deficiencies;
    private String[] fraction;
    private double[] previousFraction;
    private int[] recommendedAmounts = new int[]{2000, 44, 50, 275, 31, 25, 300};
    private double[] total = new double[8];
    private String[] headersForDeficiencyTable = new String[]{"Calories","Fat", "Protein", "Carbohydrate", "Sugar", "Fiber", "Cholesterol"};
    private int row;
    private int nutrientSolution;
    
    
    private DefaultTableModel dtm;
    
    
    private JTable nutrition, sourceTable, deficiencyTable;
    private JButton chooseSolution, infoAboutAction, doneForTheDay;
    private JTextField textField;
    private JTextArea addedText;
    private static Entry ent; // Change variable name. this one is static because you don't want it to change when you call (from the button click in the entry class) the constructor again (the one with no parameters)
    private JScrollPane summaryScroll, deficiencyScroll, additionalItemScroll;
    
    private FileWriting summaryAndSave;
    private Solution updated;
    

    public Summary(Entry n){
        ent = n;
    }


    

    public Summary(){

        // Get entries from the Entries class and turn into a 1D Array
        entries = ent.getEntries().split(",");
        
        initializeWeightArrays();

        // Formatting
        this.setBackground(Color.LIGHT_GRAY);
        //this.add(Box.createRigidArea(new Dimension(0, 130)));
        
        
        // Populate the days nutrition table by only copying the row, row by row from the large dataset in the Entry class 
        daysNutritionTable = new String[entries.length][numberOfParameters];
        for (int i = 0; i < entries.length; i++){
            daysNutritionTable[i] = ent.getData(Integer.parseInt(entries[i])); // Bullet proof in case     
        }


        
        dtm = new DefaultTableModel(daysNutritionTable, ent.getHeaders());


        
        // Construct the days nutrition table

        nutrition = new JTable(dtm){    

			public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        

        
        nutrition.getColumnModel().getColumn(0).setPreferredWidth(400);
        nutrition.getColumnModel().getColumn(7).setPreferredWidth(200);
        
        summaryScroll = new JScrollPane(nutrition);    
        this.add(summaryScroll);          
        summaryScroll.setBorder(BorderFactory.createTitledBorder ("Summary table"));

        // Add a mouse listener so that the row of the cell selected by the mouse is stored in a value
        nutrition.addMouseListener( new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                sourceTable = (JTable) e.getSource();
                row = sourceTable.rowAtPoint( e.getPoint() );
                selected = true;
            }
        });
        
        
        // Add a text field for the user to type things in
        textField = new JTextField(10);
        this.add(textField);
        textField.addActionListener(this);
        textField.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Un-clickable button to inform user of instructions
        infoAboutAction = new JButton("Please select a food item and specify the amount eaten in grams (assumed 100g if nothing is entered)");
        infoAboutAction.setEnabled(false); 
        this.add(infoAboutAction);


        // Add up all the nutrients and assign them to a 1D total array
        for (int i = 1; i < numberOfParameters; i++){
            double sum = 0;
            for (int j = 0; j < entries.length; j++){
                sum += Double.parseDouble(daysNutritionTable[j][i]);
            }

            total[i] += sum;

        }

        // Assigning the deficiency values to the display array
        deficiencies = new double[numberOfParameters];

        for (int i = 1; i < numberOfParameters; i++){
            deficiencies[i] = total[i] - recommendedAmounts[i-1]; 
        }


        for (int i = 1; i < numberOfParameters; i++){
            displayArray[0][i-1] = Double.toString(deficiencies[i]);

        }

        // Construct the deficiency table
        deficiencyTable = new JTable(displayArray, headersForDeficiencyTable){
			public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        // Formatting
        //deficiencyTable.setBounds(30,40,200,300);
        deficiencyScroll = new JScrollPane(deficiencyTable);    
        this.add(deficiencyScroll);
        deficiencyScroll.setBorder(BorderFactory.createTitledBorder ("Decificiency Table"));




        addedText = new JTextArea(20, 50);
        this.add(addedText);
        addedText.setAlignmentX(Component.LEFT_ALIGNMENT);  
        addedText.setEditable(false);
        addedText.setText("Added food items: ");

        additionalItemScroll = new JScrollPane(addedText);    
        this.add(additionalItemScroll);          

        // Adding a solutions button
        chooseSolution = new JButton("Solutions for this specific nutrient");
        chooseSolution.setAlignmentX(Component.CENTER_ALIGNMENT);	  
        chooseSolution.addActionListener(this);  
        this.add(chooseSolution);

        


         // Add a mouse listener so that the row of the cell selected by the mouse is stored in a value
         deficiencyTable.addMouseListener( new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                JTable source = (JTable) e.getSource();
                nutrientSolution = source.columnAtPoint( e.getPoint() );
                selected = true;

                

            }
        });


        // Adding a solutions button
        doneForTheDay = new JButton("Click if no more items are to be added and totals saved as a textfile and saved onto home directory.");
        doneForTheDay.setAlignmentX(Component.CENTER_ALIGNMENT);	  
        doneForTheDay.addActionListener(this);  
        this.add(doneForTheDay);


    }


    // Getter methods
    public int getSpecificNutrient(){
        return nutrientSolution;
    }

    public String[][] getEntryTable(){
        return daysNutritionTable;
    }

    public int getBackCounter(){
        return backCounter;
    }


    public void initializeWeightArrays(){
        fraction = new String[entries.length];
        previousFraction = new double[entries.length];
        Arrays.fill(previousFraction, 100);
    }
    

    // Setter methods
    public void setUpdatedEntryTable(int update){
        
        ent.setChosenEntries(update);    
        
        
        

        entries = ent.getEntries().split(","); // Don't know if this is needed

        initializeWeightArrays();
      

        // Populate the days nutrition table by only copying the row, row by row from the large dataset in the Entry class 
        oldData = daysNutritionTable;        
        daysNutritionTable = new String[entries.length][numberOfParameters];
        for (int i = 0; i < entries.length - 1; i++){
            daysNutritionTable[i] = oldData[i];      
            
        }
        
        try {
        daysNutritionTable[entries.length - 1] = ent.getData(Integer.parseInt(entries[entries.length - 1]));
        } catch (NumberFormatException e){/*ignore*/}
        addedText.append("\n" + daysNutritionTable[entries.length - 1][0] +".");
        
        dtm.addRow(daysNutritionTable[entries.length - 1]);


        

        //nutrition.repaint();
        
        updateDeficiency();

        
    }



    public void actionPerformed(ActionEvent e2){
    	try {
	        if(e2.getSource()==chooseSolution){
	            
	        	updated = new Solution();
	            Nutrion.c.add(updated);
	            backCounter++;
	            Nutrion.cardsL.last(Nutrion.c);
	
	        } else if (e2.getSource() == doneForTheDay){
	            summaryAndSave = new FileWriting(dtm, total, ent.getHeaders());
	            Nutrion.c.add(summaryAndSave);
	            Nutrion.cardsL.last(Nutrion.c);
	        } else {
	    		fraction[row] = textField.getText();
	    		textField.setText("");
	            
	        	if (Double.parseDouble(fraction[row]) <= 0){
	        		throw new ArithmeticException();
	        	}
	    		
	            if (selected){
	                update(Double.parseDouble(fraction[row]), row);
	            }
	        }

    		
    	}catch (Exception e1) {
    		textField.setText("");
    	} 

    }
    

    // Updating the daysNutritionTable
    public void update(double divisor, int r){
        for (int j = 1; j < numberOfParameters; j++){
        	try {
            daysNutritionTable[r][j] = Double.toString((Double.parseDouble(daysNutritionTable[r][j])*(100/previousFraction[r])/(100/ divisor)));
        	} catch (NumberFormatException e) {}
            
            
            
            dtm.setValueAt(daysNutritionTable[r][j], r, j);

            
        } 


        
        updateDeficiency(); 
        previousFraction[r] = divisor;

    }

    

    // Updating the deficiencyTable
    public void updateDeficiency(){

        
        total = new double[numberOfParameters]; 

        for (int i = 1; i < numberOfParameters; i++){
            double sum = 0;
            for (int j = 0; j < entries.length; j++){
            	try {
                sum += Double.parseDouble(daysNutritionTable[j][i]);
            	} catch (NumberFormatException e) {}
            }
            
            total[i] += sum;
        }
        



        for (int i = 1; i < numberOfParameters; i++){
            deficiencies[i] = total[i] - recommendedAmounts[i-1]; 
        }


        for (int i = 1; i < numberOfParameters; i++){
            displayArray[0][i-1] = Double.toString(deficiencies[i]);

        }

        deficiencyTable.repaint();
        
    }

    
     

    


}

