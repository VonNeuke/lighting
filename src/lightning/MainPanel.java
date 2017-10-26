package lightning;

import java.awt.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.MediaTracker;
import javax.swing.*;
import java.awt.event.*;
/*import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;*/
import java.util.EventListener;
import java.util.Vector;
import lightning.*;

public class MainPanel extends JPanel implements Runnable,MouseMotionListener,MouseListener{
  Game a;
  Vector BBv=new Vector();//子弹数组
  Vector EEv=new Vector();//敌机数组
  Vector BOMBv=new Vector();//爆炸数组
  Vector EBv=new Vector();//敌机子弹数组
  Vector DBv=new Vector();//大爆数组
  int E_num;
  int E_max;//敌机最大数
  int E_vel;//敌机速度
  int E_mov;//敌机横移机率
  int E_hit;//敌机开火机率
  int Sum;//击坠数
  int hero_hp;//hero生命
  Image back=null;
  Image hero=null;
  Image hero_l=null;
  Image hero_r=null;
  Image fire=null;
  Image fire_l=null;
  Image el_0=null;
  Image el_0_l=null;
  Image el_0_r=null;
  Image el_bb=null;
  Image el_bb_0=null;
  Image bomb_0=null;
  Image bomb_1=null;
  Image bomb_2=null;
  Image bomb_3=null;
  Image db_1=null;
  Image db_2=null;       //大爆的图片
  MediaTracker tracker=null;//媒体跟踪器,用来监测图像的装载
  Thread Game_star;
  int y;//背景滚动变量
  int hero_oldx,hero_oldy;//hero的旧x,y坐标
  int hero_x,hero_y;//hero的新x,y坐标
  int seq;//hero的动画变量
//  int dir;//hero的左右方向变量
  boolean isfire;//hero开火
  boolean isblast;//是否发爆
  int BBx,BBy;
  int DBx,DBy;          //大爆的横纵坐标
  int blastnum;          //大爆的数量
  boolean blastc;        //控制大爆
  Font f=new Font("黑体",Font.BOLD,30);
  Font ff=new Font("黑体",Font.BOLD,90);
  Font S=new Font("幼圆",Font.BOLD,15);
  Font SS=new Font("幼圆",Font.BOLD,25);

  MainPanel(Game a) {
    this.a=a;
    tracker=new MediaTracker(this);
    back=Toolkit.getDefaultToolkit().getImage(MainPanel.class.getResource("../img/back3.gif"));
    tracker.addImage(back,0);
    hero=Toolkit.getDefaultToolkit().getImage(MainPanel.class.getResource("../img/hero.gif"));
    tracker.addImage(hero,1);
    fire=Toolkit.getDefaultToolkit().getImage(MainPanel.class.getResource("../img/herozidan.gif"));
    tracker.addImage(fire,2);
    fire_l=Toolkit.getDefaultToolkit().getImage(MainPanel.class.getResource("../img/herozidan2.gif"));
    tracker.addImage(fire_l,3);
    el_0=Toolkit.getDefaultToolkit().getImage(MainPanel.class.getResource("../img/diji.gif"));
    tracker.addImage(el_0,4);
    el_0_l=Toolkit.getDefaultToolkit().getImage(MainPanel.class.getResource("../img/dijileft.gif"));
    tracker.addImage(el_0_l,5);
    el_0_r=Toolkit.getDefaultToolkit().getImage(MainPanel.class.getResource("../img/dijiright.gif"));
    tracker.addImage(el_0_r,6);
    el_bb=Toolkit.getDefaultToolkit().getImage(MainPanel.class.getResource("../img/dijizidan.gif"));
    tracker.addImage(el_bb,7);
    el_bb_0=Toolkit.getDefaultToolkit().getImage(MainPanel.class.getResource("../img/el_bb_0.gif"));
    tracker.addImage(el_bb_0,8);
    bomb_0=Toolkit.getDefaultToolkit().getImage(MainPanel.class.getResource("../img/bomb_0.gif"));
    tracker.addImage(bomb_0,9);
    bomb_1=Toolkit.getDefaultToolkit().getImage(MainPanel.class.getResource("../img/bomb_1.gif"));
    tracker.addImage(bomb_1,10);
    bomb_2=Toolkit.getDefaultToolkit().getImage(MainPanel.class.getResource("../img/bomb_2.gif"));
    tracker.addImage(bomb_2,11);
    bomb_3=Toolkit.getDefaultToolkit().getImage(MainPanel.class.getResource("../img/bomb_3.gif"));
    tracker.addImage(bomb_3,12);
	db_1=Toolkit.getDefaultToolkit().getImage(MainPanel.class.getResource("../img/db1.gif"));
    tracker.addImage(db_1,13);
    db_2=Toolkit.getDefaultToolkit().getImage(MainPanel.class.getResource("../img/db2.gif"));
    tracker.addImage(db_2,14);//将大爆的图片加到媒体跟踪器中
    
    this.addMouseMotionListener(this);
    this.addMouseListener(this);
    Game_star=new Thread(this);
    y=-(5*a.y);
    hero_x=a.x/2-50;
    hero_y=a.y-150;
    hero_oldx=hero_x;
    hero_oldy=hero_y;
    hero_hp=-1;
    seq=0;
 //   dir=0;
    Sum=0;
    E_num=0;
    E_max=9;
    E_vel=7;
    E_mov=90;
    E_hit=97;
    isfire=false;
    isblast=false;
    blastnum=0;
    blastc=false;
  }
  public void paint(Graphics g){
    g.drawImage(back,0,y,a.x,a.y*6,this);
    g.setColor(Color.RED);
    g.setFont(S);
    g.drawString("导弹:"+blastnum,a.x-400,25);//绘制大爆的数量
    g.drawString("生",a.x-23,40);
    g.drawString("命",a.x-23,55);
    g.fillRect(a.x-20,65,10,hero_hp);
    g.setColor(Color.BLACK);
    g.drawRect(a.x-20,64,10,300);
    if(seq==0){
      g.setColor(Color.RED);
    }
    if(seq==1){
      g.setColor(Color.BLACK);
    }
    g.setFont(S);
    g.drawString("积分:",a.x-80,25);
    g.drawString(String.valueOf(Sum),a.x-40,25);
    g.setFont(f);

    if(hero_hp==-1){
      g.setFont(f);
      g.setColor(Color.RED);
      g.drawString("你能超过1000分吗?",a.x/2-a.x/3,a.y/2-a.y/16);
      g.setFont(SS);
      g.setColor(Color.RED);
      g.drawString("请按下鼠标键开始",a.x/2-a.x/4,a.y/2+a.y/8);
      g.dispose();
      try{
        Thread.sleep(50);
      }catch(Exception e){
      }
      this.Game_star=null;
    }
    //如果游戏结束显示如下信息
    else if(hero_hp<0){
      g.setFont(ff);
      g.setColor(Color.RED);
      g.drawString("GAME", a.x / 2 - a.x / 4, a.y / 2 - a.y / 16);
      g.drawString("OVER", a.x / 2 - a.x / 4, a.y / 2 + a.y / 16);
      g.setFont(SS);
      g.setColor(Color.BLACK);
      g.drawString("请按下鼠标键重来", a.x / 2 - a.x / 4, a.y / 2 + a.y / 8);
      g.dispose();
      try {
        Thread.sleep(50);
      }
      catch (Exception e) {
      }
      this.Game_star=null;
    }
    //显示信息
    if(Math.abs(y)>(a.y)*5-50){
      g.drawString("本游戏即将开始",a.x/2-a.x/4,a.y/2-a.y/8);
    }
    if(Math.abs(y)>(a.y)*4 && Math.abs(y)<(a.y)*4+150){
      E_max=13;
      E_vel=7;
      E_mov=90;
      E_hit=95;
      g.drawString("第一关即将开始",a.x/2-a.x/4,a.y/2-a.y/8);
    }
    if(Math.abs(y)>(a.y)*3 && Math.abs(y)<(a.y)*3+150){
      E_max=16;
      E_vel=9;
      E_mov=90;
      E_hit=93;
      g.drawString("第二关即将开始",a.x/2-a.x/4,a.y/2-a.y/8);
    }
    if(Math.abs(y)>(a.y)*2 && Math.abs(y)<(a.y)*2+150){
      E_max=29;
      E_vel=11;
      E_mov=90;
      E_hit=91;
      g.drawString("第三关即将开始",a.x/2-a.x/4,a.y/2-a.y/8);
    }
    if(Math.abs(y)>(a.y) && Math.abs(y)<(a.y)+150){
      E_max=32;
      E_vel=13;
      E_mov=90;
      E_hit=89;
      g.drawString("最后了祝你好运",a.x/2-a.x/4,a.y/2-a.y/8);
    }
    if(y==0){
      E_max=40;
      E_vel=18;
      E_mov=90;
      E_hit=86;
      g.drawString("向上帝祈祷吧!",a.x/2-a.x/4,a.y/2-a.y/8);
    }
    //处理敌机子弹s
    for(int i=0;i<EBv.size();i++){
      EnemyBullet ebs;
      ebs=(EnemyBullet)EBv.elementAt(i);
      if(ebs.y>-50){
        if(ebs.cont<6){
          g.drawImage(el_bb,ebs.x,ebs.y,13,20,this);
        }
        if(ebs.cont>=6){
          g.drawImage(el_bb_0,ebs.x,ebs.y,13,15,this);
        }
      }
    }
    //处理子弹s
    for(int i=0;i<BBv.size();i++){
      PlayerBullet bs;
      bs=(PlayerBullet)BBv.elementAt(i);
      if(bs.y>-50){
        if(bs.cont<3){
          g.drawImage(fire_l,bs.x,bs.y,30,30,this);
        }
        if(bs.cont>=3){
          g.drawImage(fire,bs.x,bs.y,30,30,this);
        }
      }
    }
    //处理大爆
    for(int i=0;i<DBv.size();i++){
      Blast blast;
      blast=(Blast)DBv.elementAt(i);
      if(blast.islive){
        if(blast.cont<10){
        	//System.out.println(""+blast.x+"   "+blast.y);
          g.drawImage(db_1,blast.x,blast.y,200,200,this);
        }
        if(blast.cont>10 && blast.cont<20){
          g.drawImage(db_2,blast.x,blast.y,200,200,this);
        }
      }else{
        DBv.remove(i);
      }
    }
    //处理爆炸s
    for(int i=0;i<BOMBv.size();i++){
      Bomb bombs;
      bombs=(Bomb)BOMBv.elementAt(i);
      if(bombs.islive){
        if(bombs.cont<10){
          g.drawImage(bomb_0,bombs.x,bombs.y,50,50,this);
        }
        if(bombs.cont>10 && bombs.cont<20){
          g.drawImage(bomb_1,bombs.x,bombs.y,50,50,this);
        }
        if(bombs.cont>20 && bombs.cont<40){
          g.drawImage(bomb_2,bombs.x,bombs.y,50,50,this);
        }
        if(bombs.cont>40){
          g.drawImage(bomb_3,bombs.x,bombs.y,50,50,this);
        }
      }else{
        BOMBv.remove(i);
      }
    }
    //处理敌机s
    for(int i=0;i<EEv.size();i++){
      Enemy es;
      es=(Enemy)EEv.elementAt(i);
      if(es.y<(a.y-50)){
        if(es.islive && !es.toleft && !es.toright){
          g.drawImage(el_0,es.x,es.y,45,45,this);
        }else if(es.toleft){
          g.drawImage(el_0_l,es.x,es.y,50,50,this);
        }else if(es.toright){
          g.drawImage(el_0_r,es.x,es.y,50,50,this);
        }
      }else{
        es.islive=false;
      }
    }
    //处理hero
    g.clipRect(hero_x,hero_y,70,70);
    g.drawImage(hero,(hero_x-seq*1),hero_y,46,53,this);
  }

  public void run(){
    int cont=0;

    while(tracker.checkAll(true)==false){
      try{
        Thread.sleep(300);
      }
      catch(Exception e){
      }
    }

    while(Game_star!=null){
      //处理敌机s是否撞击hero
      if(EEv.size()==0){
        E_num=0;
      }//清空敌机数组
      if(cont!=0){
        //移动条件下增加难度
        if(E_num<=E_max){//满足条件则增加敌机
          Enemy es;
          es=new Enemy((int)(Math.random()*(a.x-50)),E_vel,E_mov,E_hit);
          EEv.addElement(es);
          E_num++;
        }
        for(int i=0;i<EEv.size();i++){
          Enemy es;
          es=(Enemy)EEv.elementAt(i);
          if(es.y<(a.y-50) && es.islive){
            es.hit(hero_x,hero_y);//监测碰撞
            if(!es.islive){
              Bomb bombs;
              bombs=new Bomb(es.x,es.y);
              BOMBv.addElement(bombs);
              //System.out.println("YOU LOST");
              hero_hp-=10;
            }
            if(es.islive){//移动未撞到的敌机
              es.move(hero_x);
              es.tofire();
              if(es.tofire() && i%4==0 && es.y<(a.y-100)){
                EnemyBullet ebs;
                ebs=new EnemyBullet(es.x,es.y,hero_x,hero_y);
                EBv.addElement(ebs);
              }
            }
          }
          else{
            EEv.remove(i);
          }//消除无效敌机
        }
      }

      //处理子弹s是否击中
      for(int i=0;i<BBv.size();i++){
        PlayerBullet bs;
        bs=(PlayerBullet)BBv.elementAt(i);
        if(bs.y>-50 && bs.islive){//子弹存活
          for(int j=0;j<EEv.size();j++){
            Enemy es;
            es=(Enemy)EEv.elementAt(j);
            if(es.islive && bs.islive){//敌机存活
              es.hit(bs.x,bs.y);
              bs.hit(es.x,es.y);//监测碰撞
              if(!es.islive){//添加爆炸
                Sum++;
                blastc=true;
                Bomb bombs;
                bombs=new Bomb(es.x,es.y);
                BOMBv.addElement(bombs);
              }
            }
          }
          if(bs.islive){
            bs.move();
          }//未击中的子弹继续移动
        }
        else{
          BBv.remove(i);
        }//清除击中的子弹
      }
		//处理大爆
	for(int i=0;i<DBv.size();i++){
        Blast blast;
        blast=(Blast)DBv.elementAt(i);
        if(blast.islive){//大爆存活
          for(int j=0;j<EEv.size();j++){
            Enemy es;
            es=(Enemy)EEv.elementAt(j);
            if(es.islive && blast.islive){//敌机存活
              es.hitblast(blast.x,blast.y);//监测碰撞
              if(!es.islive){//添加爆炸
                Sum++;
                blastc=true;
                Bomb bombs;
                bombs=new Bomb(es.x,es.y);
                BOMBv.addElement(bombs);
              }
            }
          }
        /*  for(int j=0;j<EEv.size();j++){
            EnemyBullet ebs;
            ebs=(EnemyBullet)EBv.elementAt(j);
            if(ebs.islive && blast.islive){//敌机存活
              ebs.hitblast(blast.x,blast.y);//监测碰撞
             /* if(!ebs.islive){
                EBv.remove(i);
              }*/
           // }
         // }
         blast.grow();
        }
        else{
          DBv.remove(i);
        }//清除击中的子弹
      }
      //处理爆炸s
      for(int i=0;i<BOMBv.size();i++){
        Bomb bombs;
        bombs=(Bomb)BOMBv.elementAt(i);
        if(bombs.islive){
          bombs.grow();
        }
        else{
          BOMBv.remove(i);
        }
      }
      //处理敌机子弹是否击中
      for(int i=0;i<EBv.size();i++){
        EnemyBullet ebs;
        ebs=(EnemyBullet)EBv.elementAt(i);
        ebs.hit(hero_x,hero_y);
        //消除无效子弹
        if(ebs.islive && ebs.y>-10 && ebs.y<a.y-10 && ebs.x>0 && ebs.x<a.x-10){
          ebs.move();
        }
        else if(!ebs.islive){
          //System.out.println("HIT YOU");
          EBv.remove(i);
          hero_hp-=10;
        }
        else {
          EBv.remove(i);
        }
      }
      //System.out.println(""+Sum);
      //blastnum=Sum%500;
      //System.out.println(""+blastnum);
	  if(Sum%500==0&&Sum>0&&blastc){             //符合条件增加大爆
	  	blastnum++;
	  	blastc=false;
	  	//System.out.println(""+blastnum);
	  }
      cont++;
      if(cont%3==0){
      	if(isblast){
      		DBmove(DBx,DBy);
      	}
        if(isfire){
          BBmove(BBx,BBy);
        }
        if(seq==0){
          seq=1;
        }
        else{
          seq=0;
        }
        cont=0;
      }
      repaint();
      try{
        Thread.sleep(20);
      }
      catch(Exception e){

      }
      if(y==0){
        y=0;
      }
      else{
        y++;
      }
    }
  }
  public void BBmove(int x,int y){
    PlayerBullet pb;
    pb=new PlayerBullet(x,y);
    //保证不出届
    if(BBx>=(a.x-45)){
      BBx=a.x-45;
    }
    if(BBy>=(a.y-150)){
      BBy=a.y-150;
    }
    BBv.addElement(pb);
  }
  //大爆的移动方法
  public void DBmove(int x,int y){
    Blast blast;
    blast=new Blast(x,y);
    //保证不出届
    /*if(BBx>=(a.x-75)){
      BBx=a.x-75;
    }
    if(BBx>=(a.y-150)){
      BBy=a.y-150;
    }*/
    DBv.addElement(blast);
  }
  public void  mouseDragged(MouseEvent e){
    isfire=true;
    BBx=hero_x+10;
    BBy=hero_y;
    hero_x=e.getX()-35;
    hero_y=e.getY()-35;
    //保证不出届
    if(BBx>=(a.x-45)){
      BBx=a.x-45;
    }
    if(BBy>=(a.y-150)){
      BBy=a.y-150;
    }
    if(BBx<5){
      BBx=5;
    }
    if(BBy<25){
      BBy=25;
    }
    //限速
    if((hero_x-hero_oldx)>15){
      hero_x=hero_oldx+15;
    }
    if((hero_x-hero_oldx)<-15){
      hero_x=hero_oldx-15;
    }
    if((hero_y-hero_oldy)>15){
      hero_y=hero_oldy+15;
    }
    if((hero_y-hero_oldy)<-15){
      hero_y=hero_oldy-15;
    }
    //保证不出届
    if(hero_x>=(a.x-55)){
      hero_x=a.x-55;
    }
    if(hero_y>=(a.y-110)){
      hero_y=a.y-110;
    }
    if(hero_x<0){
      hero_x=0;
    }
    if(hero_y<0){
      hero_y=0;
    }
    hero_oldx=hero_x;
    hero_oldy=hero_y;
  }
  public void mouseMoved(MouseEvent e){
    hero_x=e.getX()-35;
    hero_y=e.getY()-35;
    if(hero_x>=(a.x-55)){
      hero_x=a.x-55;
    }
    if(hero_y>=(a.y-110)){
      hero_y=a.y-110;
    }
    if(hero_x<0){
      hero_x=0;
    }
    if(hero_y<0){
      hero_y=0;
    }
    if((hero_x-hero_oldx)>15){
      hero_x=hero_oldx+15;
    }
    if((hero_x-hero_oldx)<-15){
      hero_x=hero_oldx-15;
    }
    if((hero_y-hero_oldy)>15){
      hero_y=hero_oldy+15;
    }
    if((hero_y-hero_oldy)<-15){
      hero_y=hero_oldy-15;
    }
    hero_oldx=hero_x;
    hero_oldy=hero_y;
  }
  public void mousePressed(MouseEvent e){
    //如果GAMEOVER可以重新开始
   if(e.getModifiers()==InputEvent.BUTTON1_MASK){
    if(hero_hp<0){
      if(this.Game_star!=null){
        this.Game_star.stop();
        this.Game_star=null;
        //this.Game_star.destroy();
      }
      this.Game_star=new Thread(this);
      Game_star.start();
      Game_star.setPriority(7);
      y=-(5*a.y);
      hero_x=a.x/2-35;
      hero_y=a.y-85;
      hero_oldx=hero_x;
      hero_oldy=hero_y;
      hero_hp=300;
      seq=0;
     // dir=0;
      Sum=0;
      E_num=0;
      E_max=9;
      E_vel=7;
      E_mov=90;
      E_hit=97;
      isfire=false;
      isblast=false;
      blastnum=1;
      blastc=false;
      EEv.removeAllElements();
      BBv.removeAllElements();
      EBv.removeAllElements();
      BOMBv.removeAllElements();
      DBv.removeAllElements();
    }
    else{
      isfire=true;
      BBx=hero_x+10;
      BBy=hero_y;
      //System.out.println(""+BBx+"  "+BBy);
      if(BBx>=(a.x-75)){
        BBx=a.y-75;
      }
      if(BBy>=(a.y-150)){
        BBy=a.y-150;
      }
      if(BBx<25){
        BBx=25;
      }
      if(BBy<25){
        BBy=25;
      }
    }
  }
  if(e.getModifiers()==InputEvent.BUTTON3_MASK){
  	if(blastnum>=1){
  	isblast=true;
  	DBx=hero_x-90;
  	DBy=hero_y-170;
  	EBv.removeAllElements();
  	blastnum--;
  	//System.out.println(""+blastnum);
    }
  }	
  }

  public void mouseClicked(MouseEvent e) {
  }

  public void mouseReleased(MouseEvent e) {
  	isfire=false;
  	isblast=false;
  }

  public void mouseEntered(MouseEvent e) {
  }

  public void mouseExited(MouseEvent e) {
  }
}