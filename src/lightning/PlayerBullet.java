package lightning;

public class PlayerBullet {
  int x;
  int y;
  int v;
  int cont;
  boolean islive=true;
  PlayerBullet(int x,int y) {
    this.x=x;
    this.y=y;
    this.v=20;
    this.cont=0;
  }
  public void move(){
    if(this.cont>=7){
      this.cont=0;
    }
    this.y-=this.v;
    this.cont++;
  }
  public void hit(int a,int b){
    if(Math.abs((x+25)-(a+25))<26 && Math.abs((y+25)-(b+25))<26){
      this.islive=false;
    }
    else{
      this.islive=true;
    }
  }

}