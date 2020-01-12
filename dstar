import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class dstar extends PApplet {

static final int DIAM = 48, SPD = 4, FPS = 60;
static final int BG = 0350;

PImage[] xwing = new PImage[3];
PImage enemy;
PImage playerlaser;
int score = 0;
PFont f;
int direction = 1;

int pixelsize = 4;
int gridsize  = pixelsize * 7 + 5;

Player p;
//Enemy e;
ArrayList enemies = new ArrayList();
ArrayList lasers = new ArrayList();

boolean incy = false;
 
public void setup() {
  
  
  enemy = loadImage("intercept3.png");
  xwing[0] = loadImage("xwnormal.png");
  xwing[1] = loadImage("xwleft.png");
  xwing[2] = loadImage("xwright.png");
  playerlaser = loadImage("player_singlelaser1.png");

  
  frameRate(FPS);
  ellipseMode(CENTER);

  createEnemies();
  f = createFont("Arial", 36, true);
 
  fill(Player.INK);
  //stroke(Player.OUTLINE);
  //strokeWeight(Player.BOLD);
 
  p = new Player(width >> 1, height >> 1, DIAM, SPD);
  //e = new Enemy(width >> 1, height >> 1, DIAM, SPD);
}
 
public void createEnemies() {
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j <= 3; j++) {
            enemies.add(new Enemy(i*100, j*100 + 70, DIAM, SPD));
        }
    }
}
 

 
public void draw() {
  background(0);
  p.move();
  p.display();
  //e.move();
  //e.display();
  drawScore();
  
      for (int i = 0; i < lasers.size(); i++) {
        PlayerLaser playerlaser = (PlayerLaser) lasers.get(i);
        playerlaser.display();
    }

    for (int i = 0; i < enemies.size(); i++) {
        Enemy enemy = (Enemy) enemies.get(i);
        if (enemy.outside() == true) {
            direction *= (-1);
            incy = true;
            break;
        }   
    }

    for (int i = 0; i < enemies.size(); i++) {
        Enemy enemy = (Enemy) enemies.get(i);
        if (!enemy.alive()) {
            enemies.remove(i);
        } else {
            enemy.display();
        }
    }

    incy = false;  
}
 
 
public void drawScore() {
    textFont(f);
    text("Score: " + String.valueOf(score), 300, 50);
}
 
 
public void keyPressed() {
  p.setMove(keyCode, true);
}
 
public void keyReleased() {
  p.setMove(keyCode, false);
}
 
final class Enemy {

  int life = 1;


  int baseColor = color(255, 255, 255);
  int nextColor = baseColor;

  int x, y;
  final int d, v;

  Enemy(final int xx, final int yy, final int dd, final int vv) {
    x = xx;
    y = 0;
    d = dd;
    v = vv;
  }

  public void display() {
    imageMode(CENTER);
    image(enemy, x, y);
    updateObj();
  }

  public void updateObj() {
    if (frameCount%30 == 0) {
      x += direction * gridsize;
    }

    if (incy == true) {
      y += gridsize / 2;
    }
  }

  public boolean alive() {
    for (int i = 0; i < lasers.size(); i++) {
      PlayerLaser playerlaser = (PlayerLaser) lasers.get(i);

      if (playerlaser.x > x && playerlaser.x < x + 7 * pixelsize + 5 && playerlaser.y > y && playerlaser.y < y + 5 * pixelsize) {
        lasers.remove(i);

        life--;
        nextColor = color(255, 0, 0);

        if (life == 0) {
          score += 50;
          return false;
        }

        break;
      }
    }

    return true;
  }
  public boolean outside() {
    return x + (direction*gridsize) < 0 || x + (direction*gridsize) > width - gridsize;
  }

  public void move() {

    x = x;
    y += 1;
    if (y > 800) {
      y = 0;
    }
  }
}

final class Player {
  static final int INK = 0xff008000, OUTLINE = 0;
  static final float BOLD = 2.0f;
 
  boolean isLeft, isRight, isUp, isDown;
  int x, y;
  final int d, v;
  float xacc;
  float yacc;
  float velocityX = 0;
  float velocityY = 0;
  float friction = -0.2f;
  
  boolean canShoot = true;
  int shootdelay = 0;
 
  Player(final int xx, final int yy,  final int dd, final int vv) {
    x = xx;
    y = yy;
    d = dd;
    v = vv;
  }
 
  public void display() {

    imageMode(CENTER);
    if (isLeft){
      image(xwing[1],x,y);
    }
    else if (isRight){
      image(xwing[2],x,y);
    }
    else {
      image(xwing[0],x,y);
    }
    
    shoot();
  }
 
  public void move() {
    xacc = 0;
    yacc = 0;
    
    if (isUp || isDown || isLeft || isRight){
      if (isLeft){
      
      }
      if (isRight){
        
      }
      if (isUp){
        
      }
      if (isDown){
        
      }
      
      
      x = (x + v*(PApplet.parseInt(isRight) - PApplet.parseInt(isLeft)));
      y = (y + v*(PApplet.parseInt(isDown)  - PApplet.parseInt(isUp)));
    }
  }
  
  public void shoot(){
    
    if (keyPressed && keyCode == CONTROL && canShoot) {
            lasers.add(new PlayerLaser(p.x -21, p.y-25, DIAM, SPD));
            lasers.add(new PlayerLaser(p.x +20, p.y-25, DIAM, SPD));
            canShoot = false;
            shootdelay = 0;
        }

        shootdelay++;
        
        if (shootdelay >= 20) {
            canShoot = true;
        }
   
  }
  
  
  public boolean setMove(final int k, final boolean b) {
    switch (k) {
    case +'W':
    case UP:
      return isUp = b;
 
    case +'S':
    case DOWN:
      return isDown = b;
 
    case +'A':
    case LEFT:
      return isLeft = b;
 
    case +'D':
    case RIGHT:
      return isRight = b;
 
    default:
      return b;
    }
  }
}
class PlayerLaser {

  int x, y;
  final int d, v;

  PlayerLaser(final int xx, final int yy, final int dd, final int vv) {
    x = xx;
    y = yy;
    d = dd;
    v = vv;
  }

  public void display() {
    imageMode(CENTER);
    image(playerlaser, x, y);
    
    rect(x, y, pixelsize, pixelsize);
    this.move();
  }
  
  public boolean alive() {
    for (int i = 0; i < enemies.size(); i++) {
      Enemy enemy = (Enemy) enemies.get(i);

      if (enemy.x > x && enemy.x < x + 7 * pixelsize + 5 && enemy.y > y && enemy.y < y + 5 * pixelsize) {
        enemies.remove(i);
{
          score += 50;

        }

        break;
      }
    }

    return true;
  }
  public boolean outside() {
    return x + (direction*gridsize) < 0 || x + (direction*gridsize) > width - gridsize;
  }

  public void move() {

    x = x;
    y -= 22;
    if (y < 0) {
      //   lasers.remove();
      
      
    }
  }
}

  public void settings() {  size(600, 800);  smooth(3); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "dstar" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
