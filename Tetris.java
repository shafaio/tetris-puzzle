import java.util.Random;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Tetris extends JFrame implements KeyListener {

    public static final int SCREEN_WIDTH = Interface.SQUARE_SIZE * (TetrisBoard.NUMBER_OF_COLUMS + 6);
    public static final int SCREEN_LENGTH = Interface.SQUARE_SIZE * (TetrisBoard.NUMBER_OF_ROWS + 1);

    public Tetris() {
        super("Tetris Puzzle!");
        newBoard = new TetrisBoard(0);
        setSize(SCREEN_WIDTH, SCREEN_LENGTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newInterface = new Interface();
        add(newInterface);
        setVisible(true);
        addKeyListener(this);

        Random GenRandom = new Random();
        int type = GenRandom.nextInt() % 7;
        if (type < 0)
            type = -type;
        type++;
        nextPiece = new Piece(type);
        newBoard.setNextPiece(nextPiece);
    }

    public static void main(String[] args) throws InterruptedException {

        Tetris newTetris = new Tetris();
        Random GenRandom = new Random();

       
        long tStart = System.currentTimeMillis();
        long tStartLR = System.currentTimeMillis();

        
        int type = GenRandom.nextInt() % 7;
        if (type < 0)
            type = -type;
        type++;

        
        currentPiece = new Piece(type);
        newBoard.setPosition(currentPiece);

        Thread.sleep(1000L);

        
        while (!gameOver) {
          
            try {
                Thread.sleep(60L);
            } catch (InterruptedException ex) {
            }

            
            long tEnd = System.currentTimeMillis();
            long tEndLR = System.currentTimeMillis();

         
            if (speedLeftRight != 0) {
               
                newBoard.removePosition(currentPiece);
                if (tEndLR - tStartLR >= speedLeftRight) {
                    tStartLR = System.currentTimeMillis();
                    Piece newPiece = currentPiece.clonePiece();
                    if (speedLeftRight > 0)
                        newPiece.moveRight();
                    if (speedLeftRight < 0)
                        newPiece.moveLeft();
                    if (newPiece.checkLegalPosition()) {
                        currentPiece = newPiece;
                    }
                }
                newBoard.setPosition(currentPiece);
                newInterface.repaint();
            }

           
            newBoard.removePosition(currentPiece);
            if (tEnd - tStart >= newBoard.getMode()) {
                tStart = System.currentTimeMillis();
                Piece newPiece = currentPiece.clonePiece();
                newPiece.moveDown();
                if (newPiece.checkLegalPosition()) {
                    currentPiece = newPiece;
                } else {
                    if (!newPiece.checkLegalPosition()) {
                        System.out.println("New Piece Generated!");
                        newBoard.setPosition(currentPiece);
                        newBoard.update(currentPiece.getCurrentX());
                        type = GenRandom.nextInt() % 7;
                        if (type < 0)
                            type = -type;
                        type++;
                        currentPiece = nextPiece.clonePiece();
                        nextPiece = new Piece(type);
                        newBoard.setNextPiece(nextPiece);
                        if (!currentPiece.checkLegalPosition()) {
                            gameOver = true;
                            break;
                        }
                    } else {
                        currentPiece = newPiece;
                    }
                }
            }

            if (currentPiece.checkLegalPosition())
                newBoard.setPosition(currentPiece);
            newInterface.repaint();
        }
    }

    public void keyPressed(KeyEvent event) {
        if (event.getKeyChar() == 'a') {
            System.out.println("Move left!");
            speedLeftRight = -TetrisBoard.SUPER_SPEED;
        }
        if (event.getKeyChar() == 'd') {
            System.out.println("Move right!");
            speedLeftRight = TetrisBoard.SUPER_SPEED;
        }

        if (event.getKeyChar() == 'w' || event.getKeyChar() == ' ') {
            newBoard.removePosition(currentPiece);
            Piece newPiece = currentPiece.clonePiece();
            newPiece.rotate();
            System.out.println("Rotate!");
            if (newPiece.checkLegalPosition()) {
                currentPiece = newPiece;
            } else {
                newPiece.rotate();
                if (newPiece.checkLegalPosition())
                    currentPiece = newPiece;
                else {
                    newPiece.rotate();
                    if (newPiece.checkLegalPosition())
                        currentPiece = newPiece;
                }
            }
            if (currentPiece.checkLegalPosition())
                newBoard.setPosition(currentPiece);
            newInterface.repaint();
        }
        if (event.getKeyChar() == 's') {
            newBoard.changeMode(TetrisBoard.SUPER_SPEED);
            System.out.println("Turbo!!!");
        }
    }

    public void keyTyped(KeyEvent event) {
    }

    public void keyReleased(KeyEvent event) {
        if (event.getKeyChar() == 's') {
            newBoard.previousSpeed();
            System.out.println("Turbo!!!");
        }
        if (event.getKeyChar() == 'a') {
            speedLeftRight = 0;
        }
        if (event.getKeyChar() == 'd') {
            speedLeftRight = 0;
        }
    }

  

    static private Piece currentPiece;
    static private Piece nextPiece;
    static private TetrisBoard newBoard;
    static private Interface newInterface;
    static private int speedLeftRight = 0;
    static private boolean gameOver = false;
}
