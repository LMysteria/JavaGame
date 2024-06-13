import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 1280;
        int boardHeight = 720;

        JFrame frame = new JFrame("Javagame");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Javagame Javagame = new Javagame(boardWidth, boardHeight);
        frame.add(Javagame);
        frame.pack();
        Javagame.requestFocus();
    }
}
