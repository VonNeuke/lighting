package lightning;
//package lightning;

class Blast {
  int x;
  int y;
  int cont;
  boolean islive=true;
  Blast(int x,int y) {
    this.x=x;
    this.y=y;
    this.cont=0;
  }
  public void grow(){
    if(cont>21){
      this.islive=false;
    }
    this.cont++;
    this.y++;
  }

}