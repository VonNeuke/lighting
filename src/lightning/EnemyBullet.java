package lightning;

public class EnemyBullet {
  int x;
  int y;
  int vx;
  int cont;
  int vy;
  boolean islive=true;
  EnemyBullet(int x,int y,int a,int b) {
    this.x=x;
    this.y=y;
    this.vy=15;
    this.cont=0;
    if(this.x<a){
      this.vx=2;
    }
    else{
      this.vx=-2;
    }
    if(this.y<b){
      this.vy=7;
    }
    else{
      this.vy=-7;
    }
  }
  public void move(){
    if(this.cont>=20){
      this.cont=0;
    }
    this.cont++;
    this.y+=this.vy;
    this.x+=this.vx;
  }
  public void hit(int a,int b){
    if(Math.abs((x+10)-(a+25))<28 && Math.abs((y+10)-(b+5))<28){
      this.islive=false;
    }
    else{
      this.islive=true;
    }
  }
/*	public void hitblast(int c,int d){
    if(Math.abs((x+10)-(c+200))<1 && Math.abs((y+10)-(d+200))<1){
      this.islive=false;
    }
    else{
      this.islive=true;
    }
  }*/
}