package com.dc.transimissionr.glObjects;

import com.dc.transimissionr.R;
import com.dc.transimissionr.glCore.TexFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XIeQian on 2017/4/3.
 */

public class WallsManager{
    private List<Wall> walls;
    private int[] textureID;

    public WallsManager(){
        walls=new ArrayList<>();
    }

    public void clearWalls(){
        walls.clear();
    }

    public void addWall(float width,float height){
        Wall wall=new Wall(width,height);
        wall.setTexId(textureID[0]);
        wall.setMd(new float[]{0,0,-3});
        wall.setNVec(new float[]{0,0,1},new float[]{0,1,0});
        walls.add(wall);
        wall=new Wall(width,height);
        wall.setTexId(textureID[0]);
        wall.setMd(new float[]{3,0,0});
        wall.setNVec(new float[]{-1,0,0},new float[]{0,1,0});
        walls.add(wall);
        wall=new Wall(width,height);
        wall.setTexId(textureID[0]);
        wall.setMd(new float[]{0,0,3});
        wall.setNVec(new float[]{0,0,-1},new float[]{0,1,0});
        walls.add(wall);
        wall=new Wall(width,height);
        wall.setTexId(textureID[0]);
        wall.setMd(new float[]{-3,0,0});
        wall.setNVec(new float[]{1,0,0},new float[]{0,1,0});
        walls.add(wall);
        wall=new Wall(6,6);
        wall.setTexId(textureID[2]);
        wall.setMd(new float[]{0,-3,0});
        wall.setNVec(new float[]{0,1,0},new float[]{0,0,-1});
        walls.add(wall);
        wall=new Wall(6,6);
        wall.setTexId(textureID[1]);
        wall.setMd(new float[]{0,3,0});
        wall.setNVec(new float[]{0,-1,0},new float[]{0,0,1});
        walls.add(wall);
//        Wall wall=new Wall(100,100);
//        wall.setMd(new float[]{0,0,0});
//        wall.setNVec(new float[]{0,0,1},new float[]{0,1,0});
//        wall.setTexId(textureID[2]);
//        walls.add(wall);
    }

    public void calcCollision(float[] pos, float[] vel){
        for(Wall w:walls){
            w.checkCollision(pos,vel);
        }
    }

    public void draw() {
        for(Wall w:walls){
            w.draw();
        }
    }

    public void initTexture(){
        textureID =new int[3];
        textureID[0]=TexFactory.getNewTexture(R.drawable.wall0,TexFactory.PMOD_REPEAT);
        textureID[1]=TexFactory.getNewTexture(R.drawable.floor0,TexFactory.PMOD_REPEAT);
        textureID[2]=TexFactory.getNewTexture(R.drawable.floor1,TexFactory.PMOD_REPEAT);
    }
}
