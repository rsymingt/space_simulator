class Button {
  PVector pos;
  color textColor, hoverColor;
  float size, tWidth;
  String text;

  Button(String text, PVector pos, float size, color textColor, color hoverColor) {
    this.pos = pos;
    this.textColor = textColor;
    this.hoverColor = hoverColor;
    this.size = size;
    this.text = text;
    textSize(size);
    tWidth = textWidth(text);
  }
  
  void draw(boolean on) {
    textSize(size);
    if (containsMouse()) fill(hoverColor);
    else fill(textColor);
    text(text, pos.x, pos.y + size);
    if (on)
      rect(pos.x, pos.y, tWidth, size);
  }
  
  boolean containsMouse() {
    if (mouseX > pos.x && mouseX < pos.x + tWidth && mouseY > pos.y && mouseY < pos.y + size )
      return true;
    else return false;
  }
  
  float getWidth(){
    return tWidth;
  }
}
