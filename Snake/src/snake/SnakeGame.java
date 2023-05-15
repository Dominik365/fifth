/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake;

/**
 *
 * @author pytho
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600; // šířka, výška obrazovky
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25; // velikost jednoho pole
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; // počet herních polí
    static final int DELAY = 75; // zpoždění
    final int x[] = new int[GAME_UNITS]; // pole X souřadnic všech částí hada
    final int y[] = new int[GAME_UNITS]; // pole Y souřadnic všech částí hada
    int bodyParts = 6; // počet částí hada
    int applesEaten;
    int appleX; // souřadnice jídla
    int appleY;
    char direction = 'R'; // had začíná ve směru do prava
    boolean running = false;
    Timer timer;
    Random random;

    SnakeGame(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() { // na začátku hry vytvoří jablko a spustí timer
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) { // buildin metody pro vykreslování panelů, přepsáno pomocí override
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if(running) {
            for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) { // vykresli bíle tečky oddělující
                for(int j=0;j<SCREEN_WIDTH/UNIT_SIZE;j++) {
                    g.setColor(Color.white);
                    g.drawLine(j*UNIT_SIZE, i*UNIT_SIZE, j*UNIT_SIZE, i*UNIT_SIZE);
                }
            }

            g.setColor(Color.red); // vykresli červené kruhy jako jablka
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i< bodyParts;i++) { // vykresli hada jako zelené části
                if(i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        }
        else {
            gameOver(g);
        }
    }

    public void newApple(){ // vytvoř jablko na náhodné pozici
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move(){
        for(int i = bodyParts;i>0;i--) { // přesuň všechny části těla na část před nimi
            x[i] = x[(i-1)];
            y[i] = y[(i-1)];
        }

        switch(direction) { // pro hlavu (část s indexem 0) urči další pohyb podle směru kláves
            case 'U':
                 y[0] = y[0] - UNIT_SIZE;
        break;
        
    case 'D':
        y[0] = y[0] + UNIT_SIZE;
        break;

    case 'L':
        x[0] = x[0] - UNIT_SIZE;
        break;

    case 'R':
        x[0] = x[0] + UNIT_SIZE;
        break;
    }
}

public void checkApple() { // zkontroluj kolize s jablkem
    if((x[0] == appleX) && (y[0] == appleY)) {
        bodyParts++;
        applesEaten++;
        newApple();
    }
}

public void checkCollisions() { // kolize hada
    for(int i = bodyParts;i>0;i--) { // pokud narazil do svého těla
        if((x[0] == x[i])&& (y[0] == y[i])) {
            running = false;
        }
    }

    if(x[0] < 0) { // do levé zdi
        running = false;
    }

    if(x[0] > SCREEN_WIDTH) { // do pravé zdi
        running = false;
    }

    if(y[0] < 0) { // do horní
        running = false;
    }

    if(y[0] > SCREEN_HEIGHT) { // do spodní
        running = false;
    }

    if(!running) { // zastavení timeru
        timer.stop();
    }
}

public void gameOver(Graphics g) { // game over screen
    g.setColor(Color.red);
    g.setFont( new Font("Ink Free",Font.BOLD, 75));
    FontMetrics metrics = getFontMetrics(g.getFont());
    g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    System.exit(0);
}

@Override
public void actionPerformed(ActionEvent e) {
    if(running) {
        move();
        checkApple();
        checkCollisions();
    }
    repaint();
}

public class MyKeyAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) { // změna směru hada, ošetřeno proti otočení do protisměru
        switch(e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            if(direction != 'R') {
                direction = 'L';
            }
            break;

        case KeyEvent.VK_RIGHT:
            if(direction != 'L') {
                direction = 'R';
            }
            break;

        case KeyEvent.VK_UP:
            if(direction != 'D') {
                direction = 'U';
            }
            break;

        case KeyEvent.VK_DOWN:
            if(direction != 'U') {
                direction = 'D';
            }
            break;
        }
               
        }
    }
}
