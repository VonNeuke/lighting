package lightning;

class Bomb {
  int x;
  int y;
  int cont;
  boolean islive=true;
  Bomb(int x,int y) {
    this.x=x;
    this.y=y;
    this.cont=0;
  }
  public void grow(){
    if(cont>61){
      this.islive=false;
    }
    this.cont++;
    this.y++;
  }

}