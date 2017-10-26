package lightning;

public class Enemy {
  int x;
  int y;
  int r;
  int f;
  int vx;
  int vy;
  int cont;
  boolean islive=true;
  boolean toleft=false;
  boolean toright=false;
  public Enemy(int x,int vy,int r,int f) {
    this.x=x;
    this.y=-50;
    this.vx=0;
    this.vy=vy;
    this.r=r;
    this.f=f;
    this.cont=0;
  }
  public void move(int a){
    if(Math.random()*100>r){
      if(a-this.x>50 && cont==0){
        this.vx=3;
        this.toright=true;
        this.toleft=false;
      }
      else if(this.x-a>50 && cont==0){
        this.vx=-3;
        this.toright=false;
        this.toleft=true;
      }
      this.cont++;
    }
    if(this.cont>2){
      this.vx=0;
      this.toright=false;
      this.toleft=false;
      this.cont=0;
    }
    this.x+=this.vx;
    this.y+=this.vy;
  }
  public boolean tofire(){
    if(Math.random()*100>f){
      return true;
    }
    else{
      return false;
    }
  }
  public void hit(int a,int b){
    if(Math.abs((x+25)-(a+25))<28 && Math.abs((y+25)-(b+25))<28){
      this.islive=false;
    }
    else{
      this.islive=true;
    }
  }
	public void hitblast(int c,int d){
    if(Math.abs((x+25)-(c+25))<200 && Math.abs((y+25)-(d+25))<100){
      this.islive=false;
    }
    else{
      this.islive=true;
    }
  }
}