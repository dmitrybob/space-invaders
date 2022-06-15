
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * Dima Bobrov
 */
public class Main extends JPanel {

    private Timer timer;  // fires an event to trigger updating the animation.

    private Sprite player;

    private boolean[] keys;

    private int frameCounter;

    ArrayList<Sprite> projectiles;

    ArrayList<Sprite> enemies;

    ArrayList<Sprite> enemyproj;

    boolean hitEdge;

    int life;

    int enemyMove;

    int wave;

    boolean gameover;

    int score;
    public Main(int w, int h){
        setSize(w, h);
        player = new Sprite(Resources.test, new Point(400, 575));
        projectiles = new ArrayList<>();
        keys = new boolean[256];
        timer = new Timer(800/60, e->update());
        timer.start();
        frameCounter = 30;
        enemies = new ArrayList<>();
        hitEdge = false;
        enemyMove = 3;
        enemyproj = new ArrayList<>();
        for(int i = 100; i <= 600; i+=150)
            for(int j = 50; j <= 300; j+=100){
                enemies.add(new Sprite(Resources.enemy, new Point(i,j)));
            }
        gameover = false;
        score = 0;
        life = 3;
        wave = 1;
        setupKeys();
    }


    // called every frame.  All game state changes should happen here.
    // Thus, all movement, adding/removing enemies or lasers, etc.
    public void update(){
        int k = 0;
        int m = 0;
        int x = 0;
        boolean intersects = false;
        boolean inter = false;
        if(keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]){ // move left
            if(player.getX() > 0)
                player.move(-6,0);
        }
        if(keys[KeyEvent.VK_D] ||  keys[KeyEvent.VK_RIGHT]){ // move right
            if(player.getX() < 650)
                player.move(6,0);
        }
        if(keys[KeyEvent.VK_E] && frameCounter >= 30){ // shoot
            projectiles.add(new Sprite(Resources.projectile, new Point(player.getX()+92,player.getY())));
            frameCounter = 0; // stops unlimited bullets
        }
        for(Sprite s: projectiles) // moves all projectiles
            s.move(0,-10);
        for(int i = 0 ; i < projectiles.size(); i++){
            if (projectiles.get(i).getY() <= -50)
                projectiles.remove(projectiles.get(i));
        }
        for(int i = 0 ; i < enemyproj.size(); i++){
            if (enemyproj.get(i).getY() >= 850)
                enemyproj.remove(enemyproj.get(i));
        }
        for(Sprite s: enemies){
            s.move(enemyMove,0);
            if(s.getX() >= getWidth() - 140 || s.getX() <= 0)
                hitEdge = true;
        }
        if(hitEdge) {
            enemyMove = enemyMove * -1;
            hitEdge = false;
            for(Sprite s : enemies) {
                s.move(0,10);
            }
        }
        for(int i = 0; i < enemies.size(); i++){
            for(int j = 0; j < projectiles.size(); j++){
                if(projectiles.get(j).intersects(enemies.get(i))){
                    k = i;
                    m = j;
                    intersects = true;
                }
            }
        }
        if(intersects) {
            enemies.remove(k);
            projectiles.remove(m);
            if(!gameover)
                score += 10;
        }
        if(Math.random() < .01 * wave + .01){
            Random rn = new Random();
            int randomNum = rn.nextInt(enemies.size());
            enemyproj.add(new Sprite(Resources.projectile, new Point(enemies.get(randomNum).getX(),enemies.get(randomNum).getY())));
        }
        for(Sprite s: enemyproj) // moves all enemy projectiles
            s.move(0,10);

        for(int i = 0; i < enemyproj.size(); i++) {
            if(enemyproj.get(i).intersects(player)){
                life--;
                inter = true;
                x = i;
                if(life <= 0){
                    gameover = true;
                }
            }
        }
        if(inter)
            enemyproj.remove(x);
        if(enemies.size() == 0){
            wave++;
            life++;
            for(int i = 100; i <= 600; i+=150)
                for(int j = 50; j <= 300; j+=100){
                    enemies.add(new Sprite(Resources.enemy, new Point(i,j)));
                }
        }
        frameCounter++;
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        /*
            ALL drawing happens here.
            Note that ONLY drawing should happen here - any game state
            changes should happen elsewhere.
         */
        if(!gameover) {
            player.draw(g2);
            for (Sprite sprite : enemyproj) {
                sprite.draw(g2);
            }

            for (Sprite enemy : enemies) {
                enemy.draw(g2);
            }

            for (Sprite projectile : projectiles) {
                projectile.draw(g2);
            }

            g2.setFont((new Font("TimesRoman", Font.PLAIN, 24)));
            g2.drawString("lives:" + life, 10, 60);
        }
       if(gameover){
           g2.setFont((new Font("TimesRoman", Font.PLAIN, 50)));
           g2.drawString("Game over", 300, 400);
       }
        g2.setFont((new Font("TimesRoman", Font.PLAIN, 24)));
        g2.drawString("Score: " + score, 10, 30);

        g2.setFont((new Font("TimesRoman", Font.PLAIN, 24)));
        g2.drawString("Wave: " + wave, 300, 30);

    }

    public void setupKeys(){
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                keys[e.getKeyCode()] = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys[e.getKeyCode()] = false;
            }

            @Override
            public void keyTyped(KeyEvent e) {

            }
        });
    }


    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int width = 800;
        int height = 800;
        window.setBounds(0, 0, width, height + 22); //(x, y, w, h) 22 due to title bar.

        JPanel panel = new Main(width, height);
        panel.setFocusable(true);
        panel.grabFocus();

        window.add(panel);
        window.setVisible(true);
        window.setResizable(true);
    }
}
