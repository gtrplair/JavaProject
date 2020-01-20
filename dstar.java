import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

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
SoundFile file;

PImage[] xwing = new PImage[3];
PImage enemy;
PImage playerlaser;
int score = 0;
PFont f;
int direction = 1;

PImage img;
int smallPoint, largePoint;


int pixelsize = 4;
int gridsize  = pixelsize * 7 + 5;

Player p;
//Star star;
//Enemy e;
ArrayList enemies = new ArrayList();
ArrayList lasers = new ArrayList();
ArrayList<Explosion> explosions = new ArrayList();
ArrayList<LaserEx> laserex = new ArrayList(); 
ArrayList stars = new ArrayList();

SpriteSheet spriteSheet;  
SpriteSheet2 spriteSheet2;  

boolean changeDir = false;

public void setup() {
  
  
  smallPoint = 4;
  largePoint = 40;
  imageMode(CENTER);
  noStroke();


  spriteSheet = new SpriteSheet();
  spriteSheet2 = new SpriteSheet2();
  enemy = loadImage("intercept3.png");
  xwing[0] = loadImage("xwnormal.png");
  xwing[1] = loadImage("xwleft.png");
  xwing[2] = loadImage("xwright.png");
  playerlaser = loadImage("player_singlelaser1.png");
  // Load a soundfile from the /data folder of the sketch and play it back
  file = new SoundFile(this, "xw_blaster.wav");
  // file.play();
  //laser.play();

  
  frameRate(FPS);
  ellipseMode(CENTER);

  createEnemies();
  f = createFont("Arial", 36, true);

  createStars();

  p = new Player(width >> 1, height - 100, DIAM, SPD);
  //e = new Enemy(width >> 1, height >> 1, DIAM, SPD);
  

}

public void createEnemies() {
  for (int i = 0; i < 4; i++) {
    for (int j = 0; j < 3; j++) {
      enemies.add(new Enemy(i*80+80, j*150 - 130, DIAM, SPD));
    }
  }
  for (int i = 0; i < 5; i++) {
    for (int j = 0; j < 4; j++) {
      enemies.add(new Enemy(i*80+40, j*150 - 200, DIAM, SPD));
    }
  }
}


public void createStars() {
  for (int i = 0; i < 500; i++) {
      stars.add(new Star());
  }
}



public void mousePressed() {
  createEnemies();
  //explosions.add( new Explosion( mouseX, mouseY ));
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
  
  for (int i = 0; i < stars.size(); i++) {
    Star star = (Star) stars.get(i);
    star.show();
  }

  for (int i = 0; i < enemies.size(); i++) {
    Enemy enemy = (Enemy) enemies.get(i);
    if (enemy.outside() == true) {
      direction *= (-1);
      changeDir = true;
      break;
    }
  }

  for (int i = 0; i < enemies.size(); i++) {
    Enemy enemy = (Enemy) enemies.get(i);
    if (!enemy.alive()) {
      enemies.remove(i);
      explosions.add( new Explosion( enemy.x, enemy.y ));
    } else {
      enemy.display();
    }
  }
  
   for (LaserEx laserex1 : laserex) {
    laserex1.display();
  }

  for (int i = laserex.size()-1; i >= 0; i--) {
    LaserEx laserex2 = laserex.get(i);
    if (!laserex2.exist) {
      laserex.remove(i);
    }
  }
  

  for (Explosion explosion1 : explosions) {
    explosion1.display();
  }

  for (int i = explosions.size()-1; i >= 0; i--) {
    Explosion explosion2 = explosions.get(i);
    if (!explosion2.exist) {
      explosions.remove(i);
    }
  }
 
  
  if (enemies.size() == 0){
    playAgain();
  }
  
  changeDir = false;
}


public void drawScore() {
  textFont(f);
  text("Score: " + String.valueOf(score), 300, 50);
}

public void playAgain() {
  textFont(f);
  text("Click Mouse to Play Again", width >> 2 - 70, height >> 2);
}



public void keyPressed() {
  p.setMove(keyCode, true);
}

public void keyReleased() {
  p.setMove(keyCode, false);
}
final class Enemy {

  int life = 3;


  int baseColor = color(255, 255, 255);
  int nextColor = baseColor;

  int x, y;
  final int d, v;

  Enemy(final int xx, final int yy, final int dd, final int vv) {
    x = xx;
    y = yy;
    d = dd;
    v = vv;
  }

  public void display() {
    imageMode(CENTER);
    image(enemy, x, y);
    updateObj();
  }

  public void updateObj() {
    if (frameCount%1 == 0) {
      x += direction *1;//* gridsize;
    }

    if (changeDir == true) {
      y += 50;// gridsize / 2;
    }
    collision();
  }

  public boolean alive() {
    for (int i = 0; i < lasers.size(); i++) {
      PlayerLaser playerlaser = (PlayerLaser) lasers.get(i);

      if (playerlaser.x > x && playerlaser.x < x + 50 && playerlaser.y > y && playerlaser.y < y + 30) {
        lasers.remove(i);
        laserex.add( new LaserEx( playerlaser.x, playerlaser.y ));

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
  
  public void collision(){
    if (p.x > x && p.x < x + 50 && p.y > y && p.y < y + 30) {
      System.exit(0);
    }
  }
  public void move() {

    x = x;
    y += 1;
    if (y > 800) {
      y = -400;
    }
  }
}
class Explosion {
 
  // Starting position of Explosion 
  float xpos, ypos;   
 
  // current frame is frameNumber
  int frameNumber = 0; 
  int frameNumberAdd=1; // add to go to next frame (mostly 1)
 
  // additional counter between two frames (to make it slower)
  int countBeforeNextFrame = 0;
  int speedOfAnimation = 2; // 1 is fast, 12 is slow e.g.
 
  // has the explosion started and 
  // does the explosion still exist? 
  boolean exist=true;
 
  // constructor 
  Explosion( float x_, float y_ ) {
    // start it 
    // set position 
    xpos=x_;
    ypos=y_;
  }
 
  public void display() {
    // Display
 
    // if explosion (still) runs
    if (exist) {
 
      // println(frameNumber); 
 
      // display current frame
      image(spriteSheet.frames[frameNumber], xpos, ypos);
 
      // to make movement slower: countBeforeNextFrame
      // by Chinchbug (<a href="http://www.openprocessing.org/sketch/26391" target="_blank" rel="nofollow">http://www.openprocessing.org/sketch/26391</a>)
      countBeforeNextFrame++; 
      if (countBeforeNextFrame > speedOfAnimation) {
        // reset countBeforeNextFrame
        countBeforeNextFrame = 0; 
        // go to next frame
        frameNumber+=frameNumberAdd;
        // if last frame, end explosion 
        if (frameNumber >= 15) {
          exist=false;
        }// if
      }// if
    }//if
  } // func
  //
}// class 
class LaserEx {
 
  // Starting position of Explosion 
  float xpos, ypos;   
 
  // current frame is frameNumber
  int frameNumber = 0; 
  int frameNumberAdd=1; // add to go to next frame (mostly 1)
 
  // additional counter between two frames (to make it slower)
  int countBeforeNextFrame = 0;
  int speedOfAnimation = 1; // 1 is fast, 12 is slow e.g.
 
  // has the explosion started and 
  // does the explosion still exist? 
  boolean exist=true;
 
  // constructor 
  LaserEx( float x_, float y_ ) {
    // start it 
    // set position 
    xpos=x_;
    ypos=y_;
  }
 
  public void display() {
    // Display
 
    // if explosion (still) runs
    if (exist) {
 
      // println(frameNumber); 
 
      // display current frame
      image(spriteSheet2.frames[frameNumber], xpos, ypos);
 
      // to make movement slower: countBeforeNextFrame
      // by Chinchbug (<a href="http://www.openprocessing.org/sketch/26391" target="_blank" rel="nofollow">http://www.openprocessing.org/sketch/26391</a>)
      countBeforeNextFrame++; 
      if (countBeforeNextFrame > speedOfAnimation) {
        // reset countBeforeNextFrame
        countBeforeNextFrame = 0; 
        // go to next frame
        frameNumber+=frameNumberAdd;
        // if last frame, end explosion 
        if (frameNumber >= 15) {
          exist=false;
        }// if
      }// if
    }//if
  } // func
  //
}// class 
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
            //file.play();
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
    for (int i = 0; i < lasers.size(); i++) {
      PlayerLaser playerlaser = (PlayerLaser) lasers.get(i);
      if (playerlaser.y < 0) {
        lasers.remove(i);
      }
    }
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
class SpriteSheet {
  // a list of images with the frames of the explosions
  PImage frames[];
 
  SpriteSheet() {

    PImage spriteSheet;
 
    spriteSheet = loadImage("tie_explosion.png");
 
    frames = new PImage[4*4];
 
    for (int y = 0; y < 4; y++) {
      for (int x = 0; x < 4; x++) {
        frames[y*4+x] = spriteSheet.get(x*128, y*128, 128, 128);
      } // for
    } // for
  } // constr
} // class
//
class SpriteSheet2 {
  // a list of images with the frames of the explosions
  PImage frames[];
 
  SpriteSheet2() {

    PImage spriteSheet2;
 
    spriteSheet2 = loadImage("laserex.png");
 
    frames = new PImage[1*16];
 
    for (int y = 0; y < 1; y++) {
      for (int x = 0; x < 16; x++) {
        frames[y+x] = spriteSheet2.get(x*32, y*32, 32, 32);
      } // for
    } // for
  } // constr
} // class
//
class Star {
  float x = random(0,600);
  float y = random(0,800);
  float w = random(1,3);
  float h = random(1,3);

  Star() {
  //  x = xx;
   // y = yy;
  }
  
  public void show() {
    fill (255);
    rectMode(CENTER);
    rect(x,y,w,h);
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
