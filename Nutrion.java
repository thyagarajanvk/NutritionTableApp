import java.awt.*;
import javax.swing.*;



public class Nutrion extends JFrame{
	
	static CardLayout cardsL;
	static Container c;
	
	public Main_menu  menuP; 
	public Entry entry;
	public Summary summary;
	public Solution solution;
	
	
	public static void main(String[] args){
		Nutrion a = new Nutrion();
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		a.setVisible(true);     
		a.setSize(1800, 1000);  
        a.setResizable(true);	
        a.setTitle("Nutrion");
	}
	
	public Nutrion(){
		// Card layout
		c = getContentPane();
		cardsL = new CardLayout();  
		c.setLayout(cardsL);
		menuP = new Main_menu();
		entry = new Entry();
		summary = new Summary(entry);
		solution = new Solution(entry);

		c.add("Menu", menuP);
		c.add("Entry", entry);
	}
	 
}




