package myPackage;

import java.util.Scanner;

public class Start {

	public static void main(String[] args) {
		
		Scanner reader = new Scanner( System.in );
		int choice;

		System.out.println("Welcome in the Snake Game!");
		
		System.out.println("=================================");
		System.out.println("Press 1 for A* Algorithm");
		System.out.println("Press 2 for Q Learning Algorithm");
		System.out.println("=================================");
		
		 boolean bError = true;
		 while (bError) {
			 System.out.print("Please insert your choice: ");
			 if (reader.hasNextInt()){
				 choice = reader.nextInt();
				 if(choice != 1 && choice != 2){
					 System.out.println("Invalid Choice.. Please try again");
					 continue;
				 }
				 if(choice == 2)
					 Parameters.ALGO_CHOICE = true;
			 }	 
			 else {
				 reader.next();
				 System.out.println("Invalid Choice.. Please try again");
				 continue;
			 }
			 bError = false;
		 }
		
		GameFrame window=new GameFrame();
		window.setVisible(true);

	}

}
