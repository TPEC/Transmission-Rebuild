package com.dc.transimissionr.glObjects;

import com.dc.transimissionr.R;
import com.dc.transimissionr.glCore.TexFactory;
import com.dc.transimissionr.glCore.VecFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIeQian on 2017/4/3.
 */

public class WallsManager{
    private List<Wall> walls;
    private int[] textureID;

    private int startFloorId;

    private Exit exit;

    public WallsManager(){
        walls=new ArrayList<>();
        exit=new Exit();
    }

    public void clearWalls(){
        walls.clear();
    }

    public void initRolePos(Role role){
        role.setPos(VecFactory.getCopy3(walls.get(startFloorId).getMd2()));
    }

    public void addWall(int level){
        Wall wall=null;
        switch (level) {
            case 0:
                wall = new Wall(6, 6);
                wall.setTexId(textureID[0]);
                wall.setWallPos(new float[]{0, 0, -3}, new float[]{0, 0, 1}, new float[]{0, 1, 0});
                walls.add(wall);

                wall = new Wall(6, 6);
                wall.setTexId(textureID[0]);
                wall.setWallPos(new float[]{3, 0, 0}, new float[]{-1, 0, 0}, new float[]{0, 1, 0});
                walls.add(wall);

                wall = new Wall(6, 6);
                wall.setTexId(textureID[0]);
                wall.setWallPos(new float[]{0, 0, 3}, new float[]{0, 0, -1}, new float[]{0, 1, 0});
                walls.add(wall);

                wall = new Wall(6, 6);
                wall.setTexId(textureID[0]);
                wall.setWallPos(new float[]{-3, 0, 0}, new float[]{1, 0, 0}, new float[]{0, 1, 0});
                walls.add(wall);

                wall = new Wall(6, 6);
                wall.setTexId(textureID[2]);
                wall.setWallPos(new float[]{0, -3, 0}, new float[]{0, 1, 0}, new float[]{0, 0, -1});
                startFloorId = walls.size();
                walls.add(wall);

                wall = new Wall(6, 6);
                wall.setTexId(textureID[1]);
                wall.setWallPos(new float[]{0, 3, 0}, new float[]{0, -1, 0}, new float[]{0, 0, 1});
                walls.add(wall);
                break;
            case 1:
                wall=new Wall(6,6);
                wall.setTexId(textureID[0]);//墙
                wall.setWallPos(new float[]{0,0,-9},new float[]{0,0,1},new float[]{0,1,0});
                walls.add(wall);

                wall=new Wall(6,6);
                wall.setTexId(textureID[0]);
                wall.setWallPos(new float[]{0,0,9},new float[]{0,0,-1},new float[]{0,1,0});
                walls.add(wall);

                wall=new Wall(18,6);
                wall.setTexId(textureID[0]);
                wall.setWallPos(new float[]{3,0,0},new float[]{-1,0,0},new float[]{0,1,0});//右边面朝左的
                walls.add(wall);

                wall=new Wall(6,6);
                wall.setTexId(textureID[0]);
                wall.setWallPos(new float[]{3,-6,0},new float[]{-1,0,0},new float[]{0,1,0});//右边面朝左的
                walls.add(wall);

                wall=new Wall(18,6);
                wall.setTexId(textureID[0]);
                wall.setWallPos(new float[]{-3,0,0},new float[]{1,0,0},new float[]{0,1,0});//左边面朝右
                walls.add(wall);

                wall=new Wall(6,6);
                wall.setTexId(textureID[0]);
                wall.setWallPos(new float[]{-3,-6,0},new float[]{1,0,0},new float[]{0,1,0});//左边面朝右
                walls.add(wall);

                wall=new Wall(6,6);
                wall.setTexId(textureID[2]);//[2]应该是地？
                wall.setWallPos(new float[]{0,-3,6},new float[]{0,1,0},new float[]{0,0,-1});// ground 1
//                startFloorId=walls.size();
                walls.add(wall);

                wall=new Wall(6,6);
                wall.setTexId(textureID[2]);//[2]应该是地？
                wall.setWallPos(new float[]{0,-3,-6},new float[]{0,1,0},new float[]{0,0,-1});
                startFloorId=walls.size();
                walls.add(wall);

                wall=new Wall(6,6);
                wall.setTexId(textureID[2]);//[2]应该是地？
                wall.setWallPos(new float[]{0,-9,0},new float[]{0,1,0},new float[]{0,0,-1});//第二层
//                startFloorId=walls.size();
                walls.add(wall);

                wall=new Wall(6,18);
                wall.setTexId(textureID[1]);   //ceiling
                wall.setWallPos(new float[]{0,3,0},new float[]{0,-1,0},new float[]{0,0,1});
                walls.add(wall);

                wall=new Wall(6,6);
                wall.setTexId(textureID[0]);
                wall.setWallPos(new float[]{0,-6,3},new float[]{0,0,-1},new float[]{0,1,0});
                walls.add(wall);

                wall=new Wall(6,6);
                wall.setTexId(textureID[0]);
                wall.setWallPos(new float[]{0,-6,-3},new float[]{0,0,1},new float[]{0,1,0});
                walls.add(wall);

                exit.setPos(new float[]{0f,-2.9f,8f});

                break;
        }
    }

    public boolean calcExit(Role role){
        return exit.getToExit(role.getPos());
    }

    public boolean calcCollision(Role role){ //返回onFloor
        boolean f=false;
        for(int i=0;i<walls.size();i++){
            int j=walls.get(i).checkCollision(role,true);
            if(j==1){
                if(!f && VecFactory.dotProduct3(walls.get(i).getnVec(),new float[]{0,1,0})>=.7f)
                    f=true;
            }else if(j==2){
                i=-1;
            }
        }
        return f;
    }

    public int calcPortal(float[] pos, float[] vel, Portal portal){
        Wall nw=null;
        float nl=10000f;
        for(Wall w:walls){
            if(w.checkCollision(pos,vel)){
                float[] cp=VecFactory.getCrossPoint(w.getnVec(),w.getMd2(),pos,vel);
                float cd=VecFactory.getDistance3(pos,cp);
                if(cd<nl && VecFactory.dotProduct3(vel,VecFactory.getDel3(cp,pos))>=0){
                    nl=cd;
                    nw=w;
                }
            }
        }
        if(nw!=null){
            if(nw.setPortal(VecFactory.getCrossPoint(nw.getnVec(),nw.getMd2(),pos,vel),portal))
                return 1;
            else
                return 2;
        }
        return 0;
    }

    public void draw() {
        for(Wall w:walls){
            w.draw();
        }
        exit.draw();
    }

    public void initTexture(){
        textureID =new int[3];
        textureID[0]=TexFactory.getNewTexture(R.drawable.wall0,TexFactory.PMOD_REPEAT);
        textureID[1]=TexFactory.getNewTexture(R.drawable.floor0,TexFactory.PMOD_REPEAT);
        textureID[2]=TexFactory.getNewTexture(R.drawable.floor1,TexFactory.PMOD_REPEAT);
    }
}
