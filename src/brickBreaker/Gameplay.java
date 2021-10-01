package brickBreaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXDir = -1;
    private int ballYDir = -2;

    private MapGenerator brickMap;

    public Gameplay(){
        brickMap = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g){
//        Background
        g.setColor(Color.black);
        g.fillRect(1,1,692,592);

//        Bricks Map
        brickMap.draw((Graphics2D)g);

//        Borders
        g.setColor(Color.yellow);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(692,0,3,592);

//        Scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: " + score, 570, 30);

//        The Paddle
        g.setColor(Color.red);
        g.fillRect(playerX, 550, 100, 8);

//        The Ball
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        if(totalBricks <= 0) stopDisplay("You did It!", g);

        if(ballPosY > 570) stopDisplay("Game Over.", g);

    }

    public void stopDisplay(String message, Graphics g){
        play = false;
        ballXDir = 0;
        ballYDir = 0;
        g.setColor(Color.red);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString(message + " You scored " + score + " points.", 200, 300);

        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Play Again (Press Enter)", 200, 350);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if(play){
            if(new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))){
                ballYDir = -ballYDir;
            }

            A: for(int i = 0; i < brickMap.map.length; i++){
                for(int j = 0; j < brickMap.map[0].length; j++){
                    if(brickMap.map[i][j] > 0){
                        int brickX = j * brickMap.brickWidth + 80;
                        int brickY = i * brickMap.brickHeight + 50;
                        int brickWidth = brickMap.brickWidth;
                        int brickHeight = brickMap.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
//                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)){
                            brickMap.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if(ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width){
                                ballXDir = -ballXDir;
                            }
                            else{
                                ballYDir = -ballYDir;
                            }

                            break A;
                        }
                    }
                }
            }

            ballPosX += ballXDir;
            ballPosY += ballYDir;
            if(ballPosX < 0) ballXDir = -ballXDir;
            if(ballPosY < 0) ballYDir = -ballYDir;
            if(ballPosX > 670) ballXDir = -ballXDir;
        }

        repaint();
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX >= 590) playerX = 590;
            else moveRight();
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10) playerX = 10;
            else moveLeft();
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play) {
                play = true;
                ballPosX = 120;
                ballPosY = 350;
                ballXDir = -1;
                ballYDir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                brickMap = new MapGenerator(3, 7);

                repaint();
            }
        }
    }

    public void moveRight(){
        play = true;
        playerX += 20;
    }

    public void moveLeft(){
        play = true;
        playerX -= 20;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
