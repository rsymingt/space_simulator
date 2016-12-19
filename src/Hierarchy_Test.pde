GameEngine game;

double metersC;
ArrayList<Object> objects;
import javax.swing.JOptionPane; 

float timeSpeed;
int timePassed;
double zoomVelConstant;
float years;
float planetMult;
ArrayList<Button> menuButtons;

float xC;
float yC;

boolean lockScreen = true;

void setup() {
  size(displayWidth, displayHeight);
  frameRate(60);
  if(frame != null)
  frame.setResizable(true);
  objects = new ArrayList<Object>();
  timeSpeed = 525949/60;
  zoomVelConstant = 1000;
  years = 0;
  planetMult = 50;
  timePassed = 0;
  menuButtons = new ArrayList();
  
  xC = 0;
  yC = 0;
  
  menuButtons.add(new Button("None", new PVector(xC, yC), 32, color(0), color(255, 0, 0)));
  xC += menuButtons.get(menuButtons.size()-1).getWidth()+25;

  game = new GameEngine();
  metersC = 8.333333 * pow(10, -7);
}

void draw() {
  game.runGame();
}

void recieve(Object o) {
  objects.add(o);
  menuButtons.add(new Button(((Ellipse)o).nameGet(), new PVector(xC, yC), 32, color(0), color(255, 0, 0)));
  xC += menuButtons.get(menuButtons.size()-1).getWidth()+25;
  if(xC >= width-menuButtons.get(menuButtons.size()-1).getWidth()){
    xC = 0;
    yC += 40;
  }
}
