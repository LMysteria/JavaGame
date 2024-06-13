import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.*;

public class Javagame extends JPanel implements ActionListener, KeyListener{
    private class Tile{
        int x;
        int y;

        Tile (int x, int y){
            this.x=x;
            this.y=y;
        }
    }

    private class Player{
        Tile playerTile;
        int playerVelocityX;
        int playerVelocityY;
        int playerSpeed=1;
        int playerSize=1;
        Player (Tile playerTile, int playerVelocityX, int playerVelocityY){
            this.playerTile=playerTile;
            this.playerVelocityX=playerVelocityX;
            this.playerVelocityY=playerVelocityY;
        }
    }

    private class Enemy{
        Tile enemyTile;
        int enemySize=1;
        Enemy (Tile enemyTile){
            this.enemyTile= enemyTile;
        }
    }

    private class Bullet{
        Tile bulletTile;
        int bulletVelocityX;
        int bulletVelocityY;
        int playerSize;
        Bullet(Tile bulletTile, int bulletVelocityX, int bulletVelocityY){
            this.bulletTile=bulletTile;
            this.bulletVelocityX=bulletVelocityX;
            this.bulletVelocityY=bulletVelocityY;
        }
        public void bulletmove(){
            this.bulletTile.x+=this.bulletVelocityX;
            this.bulletTile.y+=this.bulletVelocityY;
        }
    }
    
    int playerSpeed=1;
    int bulletSpeed=3;
    int boardWidth;
    int boardHeight;
    int tileSize=10;
    ArrayList<Bullet> Bullets;

    Player player;
    Enemy enemy;

    Tile bulletTile;


    Random random;

    //gameloop
    Timer gameLoop;


    Javagame(int boardWidth, int boardHeight){
        this.boardWidth=boardWidth;
        this.boardHeight=boardHeight;
        setPreferredSize(new Dimension(this.boardWidth,this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        player = new Player(new Tile(boardWidth/(tileSize*2)-2,boardHeight/(tileSize*2)-1), 0, 0);
        Bullets = new ArrayList<Bullet>();

        enemy = new Enemy(new Tile(16, 16));
        random = new Random();
        EnemySpawn();

        gameLoop = new Timer(25, this);
        gameLoop.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        
        //grid
        for (int i=0; i<boardWidth/tileSize; i++){
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            if(i<boardHeight/tileSize)
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        }

        //enemy
        g.setColor(Color.red);
        g.fillRect((enemy.enemyTile.x-enemy.enemySize)*tileSize, (enemy.enemyTile.y-enemy.enemySize)*tileSize, tileSize*(2*enemy.enemySize+1), tileSize*(2*enemy.enemySize+1));

        //player
        g.setColor(Color.cyan);
        g.fillRect((player.playerTile.x-player.playerSize)*tileSize, (player.playerTile.y-player.playerSize)*tileSize, tileSize*(2*player.playerSize+1), tileSize*(2*player.playerSize+1));

        //bullet
        for (Bullet bullet : Bullets) {
            g.setColor(Color.GREEN);
            g.fillRect(bullet.bulletTile.x*tileSize, bullet.bulletTile.y*tileSize, tileSize, tileSize);
        }

    }

    public void EnemySpawn(){
        enemy.enemyTile.x = random.nextInt(boardWidth/tileSize);
        enemy.enemyTile.y = random.nextInt(boardHeight/tileSize);
    }

    public void move(){
        //player
        player.playerTile.x += player.playerVelocityX;
        player.playerTile.y += player.playerVelocityY;
        
        //bullet
        Iterator<Bullet> i = Bullets.iterator();
        while (i.hasNext()){
            Bullet bullet = i.next();
            bullet.bulletTile.x += bullet.bulletVelocityX;
            bullet.bulletTile.y += bullet.bulletVelocityY;
            //hit
            if (collision(bullet.bulletTile, enemy.enemyTile, enemy.enemySize)){
                i.remove();
                continue;
            }
                
            //out of bound
            if(bullet.bulletTile.x>boardWidth||bullet.bulletTile.x<0||bullet.bulletTile.y>boardHeight||bullet.bulletTile.y<0){
                i.remove();
            }
        }
    }

    public boolean collision(Tile bullet, Tile entity, int entitySize){
        if(bullet.x<entity.x-entitySize||bullet.x>entity.x+entitySize)
        return false;
        if(bullet.y<entity.y-entitySize||bullet.y>entity.y+entitySize)
        return false;
        return true;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Player movement
        //go left
        if (e.getKeyCode() == KeyEvent.VK_A){
            player.playerVelocityX = -player.playerSpeed;
            player.playerVelocityY = 0;
            return;
        }
        //go right
        if (e.getKeyCode() == KeyEvent.VK_D){
            player.playerVelocityX = player.playerSpeed;
            player.playerVelocityY = 0;
            return;
        }
        //go up
        if (e.getKeyCode() == KeyEvent.VK_W){
            if (e.getKeyCode() == KeyEvent.VK_D)
            player.playerVelocityX = 0;
            player.playerVelocityY = -player.playerSpeed;
            return;
        }
        //go down
        if (e.getKeyCode() == KeyEvent.VK_S){
            player.playerVelocityX = 0;
            player.playerVelocityY = player.playerSpeed;
            return;
        } 

        //bullet movement
        //shoot left
        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            Bullets.add(new Bullet(new Tile(player.playerTile.x, player.playerTile.y), -bulletSpeed, 0));
            return;
        } 
        //shoot right
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            Bullets.add(new Bullet(new Tile(player.playerTile.x, player.playerTile.y), bulletSpeed, 0));
            return;
        } 
        //shoot up
        if (e.getKeyCode() == KeyEvent.VK_UP){
            Bullets.add(new Bullet(new Tile(player.playerTile.x, player.playerTile.y), 0, -bulletSpeed));
            return;
        } 
        //shoot down
        if (e.getKeyCode() == KeyEvent.VK_DOWN){
            Bullets.add(new Bullet(new Tile(player.playerTile.x, player.playerTile.y), 0, bulletSpeed));
            return;
        } 
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A||e.getKeyCode() == KeyEvent.VK_D||e.getKeyCode() == KeyEvent.VK_S||e.getKeyCode() == KeyEvent.VK_W){
            player.playerVelocityX = 0;
            player.playerVelocityY = 0;
            return;
        }
    }


//No Need
    @Override
    public void keyTyped(KeyEvent e) {
    }



}
