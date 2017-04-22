package com.dc.transimissionr.glObjects;

import com.dc.transimissionr.glCore.MatrixState;
import com.dc.transimissionr.glCore.TexFactory;
import com.dc.transimissionr.glCore.VecFactory;

import static com.dc.transimissionr.gameData.Constant.UNIT_SIZE;
import static com.dc.transimissionr.gameData.Constant.WALL_WIDTH;
import static com.dc.transimissionr.glCore.VecFactory.dotProduct3;

/**
 * Created by XIeQian on 2017/4/15.
 */

public class Wall {
    private TextureRect textureRect;
    private float width;
    private float height;
    private int texId;

    private float[] nVec;
    private float[] nUp;
    private float[] md;

    public Wall(float width,float height){
        this.width=width;
        this.height=height;
        textureRect=new TextureRect(TexFactory.glSv,width/UNIT_SIZE,height/UNIT_SIZE);
    }

    public void setNVec(float[] nVec,float[] nUp) {
        this.nVec = nVec;
        this.nUp=nUp;
        VecFactory.unitize3(this.nVec);
        VecFactory.unitize3(this.nUp);
    }

    public void setMd(float[] md) {
        this.md = md;
    }

    public void setTexId(int texId) {
        this.texId = texId;
    }

    public boolean checkCollision(float[] pos,float[] vel){//检测碰撞，输入位置向量、速度向量，返回是否碰撞，并修改速度向量，使得pos+vel刚好贴墙。墙的厚度为Constant.WALL_WIDTH
        if(dotProduct3(nVec,vel)<0) {
            float[] md2=VecFactory.getAdd3(md,VecFactory.getMultiply3(nVec,WALL_WIDTH));
            float[] pv=VecFactory.getCrossPoint(nVec,md2,pos,vel);
            boolean f=VecFactory.dotProduct3(vel,VecFactory.getDel3(pv,pos))<0;
            f=f&& VecFactory.dotProduct3(vel,VecFactory.getDel3(VecFactory.getCrossPoint(nVec,md,pos,vel),pos))>=0;
            if(VecFactory.getLength3(VecFactory.getDel3(pv,pos))<=VecFactory.getLength3(vel)||f) {
                pv=VecFactory.getDel3(pv,md2);
                float[] nx = VecFactory.crossProduct(nVec, nUp);
                float x = Math.abs(VecFactory.dotProduct3(pv, nx));
                float y = Math.abs(VecFactory.dotProduct3(pv, nUp));
                if (x >= -width / 2 && x < width / 2 && y >= -height / 2 && y < height / 2) {
                    float[] vel_p = VecFactory.getProjection(nVec, md2, VecFactory.getAdd3(pos, vel));
                    vel_p = VecFactory.getDel3(vel_p, pos);
                    vel[0] = vel_p[0];
                    vel[1] = vel_p[1];
                    vel[2] = vel_p[2];
                    return true;
                }
            }
        }
        return false;
    }

    public void draw(){
        MatrixState.pushMatrix();
        MatrixState.translate(md[0],md[1],md[2]);
        MatrixState.rotate(VecFactory.getIncludedAngle2(new float[]{0,1},new float[]{nVec[0],nVec[2]}),0,1,0);
        MatrixState.scale(width,1f,1f);
        MatrixState.rotate(-VecFactory.getIncludedAngle2(new float[]{1,0},new float[]{nUp[1],nUp[2]}),1,0,0);
        MatrixState.scale(1f,height,1f);
        textureRect.drawSelf(texId);
        MatrixState.popMatrix();
    }
}
