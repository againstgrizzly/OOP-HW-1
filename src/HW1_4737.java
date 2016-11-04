
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;

public class HW1_4737 {

    private static String path = "./FGCU_logo.png";//Relative path on input image
    private static String title = "Eight Puzzle Game";//Title of frame

    //Main method creates new JEightPuzzleFrame object that takes in the title and path	
    public static void main(String[] args) {
        JEightPuzzleFrame game = new JEightPuzzleFrame(title, path);
    }
}

class JEightPuzzleFrame extends JFrame {

    private String title; //Title of window
    private int width; //width of window
    private int height; //height of window
    private JPanel mainPanel = new JPanel(); //panel that holds all buttons and the empty panel
    private JPanel emptyButton = new JPanel(); //panel for empty space
    private JButton[] buttons; //array to hold buttons of game
    private int board[][] = { //board used to represent locations on board grid
            {8, 0, 1},
            {4, 5, 2},
            {3, 6, 7}
    };

    /*
    The constructor takes in two strings. One representing the title and the other
    representing the path. The title is stored in private String title and the path is used
    to read the image the path is pointing and store it in BufferedImage. If the path is invalid, throws
    an error. If the image is successfully read, it's width and height are stored in global width and height 
    variables. mainPanel's layout is set to a 3,3 gridlayout and an 8 JButton object array used for the game are instantiated
    to buttons. A ButtonHandler handler which extends ActionListener object is created to handle button clicks by the
    user. The JButton objects stored in our buttons array is assigned partial image icons derived from our read in image
    by using a method call to iconAssigner and actionListeners are also added to the buttons. The buttons and emptyButton panel
    are then added to the mainPanel and then the mainPanel is added to the JFrame. The default close operation is set so that the 
    program exits, the title is set to the global String title. Dimension object is used to set the inner frame size to height
    and width of the frame. The window is made visible and not resizable.
    */
    public JEightPuzzleFrame(String title, String path) {
        this.title = title;
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (Exception e) {
            System.err.print("File not found");
            System.exit(1);
        }

        this.width = image.getWidth();
        this.height = image.getHeight();

        mainPanel.setLayout(new GridLayout(3, 3));
        buttons = new JButton[8];
        ButtonHandler handler = new ButtonHandler();

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(iconAssigner(image, i, width, height));
            buttons[i].addActionListener(handler);
        }

        mainPanel.add(emptyButton);
        mainPanel.add(buttons[0]);
        mainPanel.add(buttons[1]);
        mainPanel.add(buttons[4]);
        mainPanel.add(buttons[5]);
        mainPanel.add(buttons[2]);
        mainPanel.add(buttons[3]);
        mainPanel.add(buttons[6]);
        mainPanel.add(buttons[7]);
        add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(this.title);
        Dimension dimension = new Dimension();
        dimension.setSize(width, height);
        mainPanel.setPreferredSize(dimension);
        pack();
        setVisible(true);
        setResizable(false);
    }

    /*
        iconAssigner takes in an image, an int gridPosition representing what part of the image to be cropped
        and an int width and height representing the width and height of the orignal image. 
    */

    //This method returns a portion of the inputted image as an icon to be used to be assigned to a button
    public Icon iconAssigner(BufferedImage image, int gridPosition, int width, int height) {

        int iconWidth = width / 3;
        int iconHeight = height / 3;

        BufferedImage part = new BufferedImage(iconWidth, iconHeight, BufferedImage.TYPE_4BYTE_ABGR);

        switch (gridPosition) {
            case 0:
                for (int i = 0; i < iconWidth; i++) {
                    for (int j = 0; j < iconHeight; j++) {
                        part.setRGB(i, j, image.getRGB(i + 0, j + 0));
                    }
                }
                break;
            case 1:
                for (int i = 0; i < iconWidth; i++) {
                    for (int j = 0; j < iconHeight; j++) {
                        part.setRGB(i, j, image.getRGB(i + width / 3, j + 0));
                    }
                }
                break;
            case 2:
                for (int i = 0; i < iconWidth; i++) {
                    for (int j = 0; j < iconHeight; j++) {
                        part.setRGB(i, j, image.getRGB(i + (width * 2 / 3), j + 0));
                    }
                }
                break;
            case 3:
                for (int i = 0; i < iconWidth; i++) {
                    for (int j = 0; j < iconHeight; j++) {
                        part.setRGB(i, j, image.getRGB(i + 0, j + height / 3));
                    }
                }
                break;
            case 4:
                for (int i = 0; i < iconWidth; i++) {
                    for (int j = 0; j < iconHeight; j++) {
                        part.setRGB(i, j, image.getRGB(i + width / 3, j + height / 3));
                    }
                }
                break;
            case 5:
                for (int i = 0; i < iconWidth; i++) {
                    for (int j = 0; j < iconHeight; j++) {
                        part.setRGB(i, j, image.getRGB(i + width * 2 / 3, j + height / 3));
                    }
                }
                break;
            case 6:
                for (int i = 0; i < iconWidth; i++) {
                    for (int j = 0; j < iconHeight; j++) {
                        part.setRGB(i, j, image.getRGB(i + 0, j + height * 2 / 3));
                    }
                }
                break;
            case 7:
                for (int i = 0; i < iconWidth; i++) {
                    for (int j = 0; j < iconHeight; j++) {
                        part.setRGB(i, j, image.getRGB(i + width / 3, j + height * 2 / 3));
                    }
                }
                break;
        }

        return new ImageIcon(part);
    }

    //ActionListener for the buttons of the game
    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            int position = 0;

            //gets index of pressed button
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i] == e.getSource()) {
                    position = i;
                }
            }

            //Depending on location in the grid and if an emptyButton is next to it, switches
            //the position of the button with that of the neighbor emptyButton
            if((board[0][0] == position) && (board[1][0] == 8 || board[0][1] == 8)){

                if(board[1][0] == 8){//Correct

                    //Removes contents of mainPanel, adds updated buttons/emptyButton and redraws UI
                    mainPanel.removeAll();
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[1][0] = position;
                    board[0][0] = 8;
                }

                if(board[0][1] == 8){//Correct

                    mainPanel.removeAll();
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[0][1] = position;
                    board[0][0] = 8;

                }
            }

            else if((board[1][0] == position) && (board[0][0] == 8 || board[2][0] == 8 || board[1][1] == 8)){

                if(board[0][0] == 8){//Correct

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[0][0] = position;
                    board[1][0] = 8;


                }

                else if(board[2][0] == 8){//Correct

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[1][0] = 8;
                    board[2][0] = position;

                }

                else if(board[1][1] == 8){//Correct

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[1][0] = 8;
                    board[1][1] = position;
                }
            }

            else if((board[2][0] == position) && (board[1][0] == 8 || board[2][1] == 8)){

                if(board[1][0] == 8){//Correct

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[1][0] = position;
                    board[2][0] = 8;
                }

                else if(board[2][1] == 8){

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[2][1] = position;
                    board[2][0] = 8;
                }
            }


            else if((board[0][1] == position) && (board[1][1] == 8 || board[0][0] == 8 || board[0][2] == 8)){

                if(board[1][1] == 8){

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[1][1] = position;
                    board[0][1] = 8;
                }

                else if(board[0][0] == 8){

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[0][0] = position;
                    board[0][1] = 8;
                }

                else if(board[0][2] == 8){
                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[0][2] = position;
                    board[0][1] = 8;
                }
            }

           else  if((board[1][1] == position) && (board[1][0] == 8 || board[0][1] == 8 || board[1][2] == 8 || board[2][1] == 8)){

                if(board[1][0] == 8){

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[1][0] = position;
                    board[1][1] = 8;
                }

                else if(board[0][1] == 8){

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[1][1] = 8;
                    board[0][1] = position;

                }

                else if(board[1][2] == 8){

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[1][1] = 8;
                    board[1][2] = position;

                }

                else if(board[2][1] == 8){

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[1][1] = 8;
                    board[2][1] = position;

                }
            }

            else if((board[2][1] == position) && (board[2][0] == 8 || board[1][1] == 8 || board[2][2] == 8)){

                if(board[2][0] == 8){

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[2][0] = position;
                    board[2][1] = 8;
                }

                else if(board[1][1] == 8){

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[1][1] = position;
                    board[2][1] = 8;

                }

                else if(board[2][2] == 8){

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.updateUI();
                    board[2][2] = position;
                    board[2][1] = 8;
                }
            }

            else if((board[0][2] == position) && (board[0][1] == 8 || board[1][2] == 8)){

                if(board[0][1] == 8){//Correct

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[0][1] = position;
                    board[0][2] = 8;


                }

                else if(board[1][2] == 8){//correct

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[1][2] = position;
                    board[0][2] = 8;
                }
            }

            else if((board[1][2] == position) && (board[0][2] == 8 || board[1][1] == 8 || board[2][2] == 8)){

                if(board[0][2] == 8){

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[0][2] = position;
                    board[1][2] = 8;
                }

                else if(board[1][1] == 8){

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.updateUI();
                    board[1][1] = position;
                    board[1][2] = 8;
                }

                else if(board[2][2] == 8){

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(emptyButton);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.updateUI();
                    board[2][2] = position;
                    board[1][2] = 8;

                }
            }

            else if((board[2][2] == position) && (board[2][1] == 8 || board[1][2] == 8)){// 2 2

                if(board[1][2] == 8){

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][1]]);
                    mainPanel.add(emptyButton);
                    mainPanel.updateUI();
                    board[1][2] = position;
                    board[2][2] = 8;

                }

                else if(board[2][1] == 8){

                    mainPanel.removeAll();
                    mainPanel.add(buttons[board[0][0]]);
                    mainPanel.add(buttons[board[0][1]]);
                    mainPanel.add(buttons[board[0][2]]);
                    mainPanel.add(buttons[board[1][0]]);
                    mainPanel.add(buttons[board[1][1]]);
                    mainPanel.add(buttons[board[1][2]]);
                    mainPanel.add(buttons[board[2][0]]);
                    mainPanel.add(buttons[board[2][2]]);
                    mainPanel.add(emptyButton);
                    mainPanel.updateUI();
                    board[2][1] = position;
                    board[2][2] = 8;

                }

            }

            //Checks to see if the user has won and calls dialogWindowAndRandomizer
            if(
                            board[0][0] == 0 &&
                            board[0][1] == 1 &&
                            board[0][2] == 2 &&
                            board[1][0] == 3 &&
                            board[1][1] == 4 &&
                            board[1][2] == 5 &&
                            board[2][0] == 6 &&
                            board[2][1] == 7 &&
                            board[2][2] == 8
                    ){

                dialogWindowAndRandomizer();

            }
        }
    }

    //Displays dialog that the user has won
    //When the user presses "ok" the dialog window closes and the button game resets to a 
    //random configuration
    public void dialogWindowAndRandomizer(){

        JFrame frame = new JFrame();

        JOptionPane.showMessageDialog(frame, "You won!", "Congratulations", JOptionPane.PLAIN_MESSAGE);

        Collections.shuffle(Arrays.asList(board));

        mainPanel.removeAll();

        List<Integer> list = new ArrayList<>();

        for(int i = 0; i < 9; i++){
            list.add(i);
        }
        Collections.shuffle(list);
        int x = 0;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                board[i][j] = list.get(x);

                if(board[i][j] < 8){
                    mainPanel.add(buttons[board[i][j]]);
                }
                else{
                    mainPanel.add(emptyButton);
                }
                x++;
            }
        }
        mainPanel.updateUI();
    }

}


