import javax.swing.*;
import java.awt.*;
public class Main {
    public static void main(String[] args) {
        loginFrame lF = new loginFrame();
        login(lF);
     }
     private static void login(loginFrame lF)
     {
         lF.setTitle("Sales Management");
        // lF.getContentPane().setBackground(Color.getHSBColor(600,300, 300));
         lF.setVisible(true);
         lF.setBounds(10,10,370,600);
         lF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         lF.setResizable(false);
     }
}
