package myPackage;

import javax.swing.JFrame;

public class GameFrame extends JFrame{

	public GameFrame() {

		Board panel=new Board();
		add(panel);
        pack();
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        //addKeyListener does not work without it stack (286727)
        setFocusable(true);
	}
	
}
