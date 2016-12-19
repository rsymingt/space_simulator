void solarSystemInput(){
  
  recieve(new Ellipse(7.791 * pow(10, 10), 0, 328.5 * pow(10, 21), 2.44 * pow(10, 6), 0, 0, "Mercury"));
  recieve(new Ellipse(1.08 * pow(10,11), 0, 4.86 * pow(10,24), 6.05 * pow(10,6), 0, 0, "Venus")); //<>//
  recieve(new Ellipse(1.496 * pow(10,11), 0, 5.972 * pow(10,24), 6.371 * pow(10, 6), 0, 0, "Earth"));
  recieve(new Ellipse(2.279 * pow(10,11), 0, 6.39 * pow(10,23), 3.39 * pow(10,6), 0, 0, "Mars"));
  recieve(new Ellipse(7.785 * pow(10,11), 0, 1.89 * pow(10, 27), 6.99 * pow(10, 7), 0, 0, "Jupiter"));
  recieve(new Ellipse(1.43 * pow(10, 12), 0, 5.68 *pow(10,26), 5.82 * pow(10,7), 0, 0, "Saturn"));
  recieve(new Ellipse(2.87 * pow(10, 12), 0, 8.6 * pow(10,25), 2.53 * pow(10,7), 0, 0, "Uranus"));
  recieve(new Ellipse(4.5 * pow(10, 12), 0, 1.02 * pow(10,26), 2.53 * pow(10, 7), 0, 0, "Neptune"));
  recieve(new Ellipse(5.9 * pow(10, 12), 0 ,1.3 * pow(10, 22), 1.184 * pow(10, 6), 0, 0, "Pluto"));
  
  for(int i = 1; i < objects.size(); i ++){
    
    if(i%2 == 0){
    ((Ellipse)objects.get(i)).changeVel(centripCalc(objects.get(i), objects.get(0)));
    }else{
      ((Ellipse)objects.get(i)).changeVel(centripCalcO(objects.get(i), objects.get(0)));
    }
  }
  
}
