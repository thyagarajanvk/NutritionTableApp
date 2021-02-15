import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileWriting extends FinalDaySummary{
    
	private String dateAndTime;
    private String totalValues = "";
    private DateTimeFormatter dtf;
    private LocalDateTime now;
    private PrintWriter myWriter;
    
    public FileWriting(DefaultTableModel d, double[] total, String[] headers){
    	
    	super(d);
    	
    	for (int i = 1; i < total.length; i++) {
    		totalValues += headers[i] + ": " + total[i] + "\n";
    	}
     
    	totalValues += "Note that cholesterol is in milligrams, calorie value is in kcal and the rest are in grams.";
    	
    	 dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
    	 now = LocalDateTime.now();
		
    	dateAndTime = dtf.format(now);
    	dateAndTime = dateAndTime.replace("/","-");
    	dateAndTime = dateAndTime.replace(":","-");
    	
    	      
 		try {
			myWriter = new PrintWriter(new File(System.getProperty("user.home") + "\\"+ dateAndTime+ ".txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  
 		myWriter.write(totalValues);
        myWriter.flush();  
		myWriter.close();
    	    	

    }
    
}


