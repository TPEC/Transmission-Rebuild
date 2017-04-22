package com.dc.transimissionr.glObjects;

import com.dc.transimissionr.glCore.MatrixState;
import com.dc.transimissionr.glCore.TexFactory;
import com.dc.transimissionr.glCore.VecFactory;

import static com.dc.transimissionr.gameData.Constant.PORTAL_SIZE;
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
    private float[] md2;

    private PortalInWall[] piw;

    public Wall(float width,float height){
        this.width=width;
        this.height=height;
        textureRect=new TextureRect(TexFactory.glSv,width/UNIT_SIZE,height/UNIT_SIZE);
        piw=new PortalInWall[2];
        piw[0]=new PortalInWall();
        piw[1]=new PortalInWall();
    }

    public void setWallPos(float[] md, float[] nVec,float[] nUp) {
        this.md = md;
        this.nVec = nVec;
        this.nUp=nUp;
        VecFactory.unitize3(this.nVec);
        VecFactory.unitize3(this.nUp);
        this.md2=VecFactory.getAdd3(md,VecFactory.getMultiply3(nVec,WALL_WIDTH));
    }

    public void setTexId(int texId) {
        this.texId = texId;
    }

    public float[] getnVec() {
        return nVec;
    }

    public float[] getnUp() {
        return nUp;
    }

    public float[] getMd2() {
        return md2;
    }

    public boolean checkCollision(float[] pos, float[] vel, boolean resize){//检测碰撞，输入位置向量、速度向量，返回是否碰撞，并修改速度向量，使得pos+vel刚好贴墙。墙的厚度为Constant.WALL_WIDTH
        int ab;
        if(!resize)
            ab=0;
        if(dotProduct3(nVec,vel)<0) {//正向相交
            float[] pv=VecFactory.getCrossPoint(nVec,md2,pos,vel);
            boolean f=VecFactory.dotProduct3(vel,VecFactory.getDel3(pv,pos))<0;
            f=f && VecFactory.dotProduct3(vel,VecFactory.getDel3(VecFactory.getCrossPoint(nVec,md,pos,vel),pos))>=0;
            if(VecFactory.getLength3(VecFactory.getDel3(pv,pos))<=VecFactory.getLength3(vel)||f) { //墙面厚度内
                pv=VecFactory.getDel3(pv,md2);
                float[] nx = VecFactory.crossProduct(nUp, nVec);
                float x = VecFactory.dotProduct3(pv, nx);
                float y = VecFactory.dotProduct3(pv, nUp);
                if (Math.abs(x) <= width / 2 && Math.abs(y) <= height / 2) {   //墙面范围内
                    if(resize) {
                        if(piw[0].valid){
                            if(Math.abs(x-piw[0].pos2[0])<=PORTAL_SIZE/2 && Math.abs(y-piw[0].pos2[1])<=PORTAL_SIZE/2){//穿过传送门0
                                if(piw[0].portal.getOtherSide().isValid()) {
                                    float[] vel_p = new float[3];
                                    vel_p[0] = VecFactory.dotProduct3(vel, nx);
                                    vel_p[1] = VecFactory.dotProduct3(vel, nUp);
                                    vel_p[2] = VecFactory.dotProduct3(vel, nVec);
                                    Wall w = piw[0].portal.getOtherSide().getInWall();
                                    nx = VecFactory.crossProduct(w.getnUp(), w.getnVec());
                                    vel[0] = vel_p[0] * nx[0] + vel_p[1] * w.getnUp()[0] + vel_p[2] * w.getnVec()[0];
                                    vel[1] = vel_p[0] * nx[1] + vel_p[1] * w.getnUp()[1] + vel_p[2] * w.getnVec()[1];
                                    vel[2] = vel_p[0] * nx[2] + vel_p[1] * w.getnUp()[2] + vel_p[2] * w.getnVec()[2];
                                    vel_p = piw[0].portal.getOtherSide().getPos();
                                    pos[0] = vel_p[0];
                                    pos[1] = vel_p[1];
                                    pos[2] = vel_p[2];
                                    return false;
                                }
                            }
                        }
                        if(piw[1].valid){
                            if(Math.abs(x-piw[1].pos2[0])<=PORTAL_SIZE/2 && Math.abs(y-piw[1].pos2[1])<=PORTAL_SIZE/2){//穿过传送门1
                                if(piw[1].portal.getOtherSide().isValid()) {
                                    float[] vel_p = new float[3];
                                    vel_p[0] = VecFactory.dotProduct3(vel, nx);
                                    vel_p[1] = VecFactory.dotProduct3(vel, nUp);
                                    vel_p[2] = VecFactory.dotProduct3(vel, nVec);
                                    Wall w = piw[1].portal.getOtherSide().getInWall();
                                    nx = VecFactory.crossProduct(w.getnUp(), w.getnVec());
                                    vel[0] = vel_p[0] * nx[0] + vel_p[1] * w.getnUp()[0] + vel_p[2] * w.getnVec()[0];
                                    vel[1] = vel_p[0] * nx[1] + vel_p[1] * w.getnUp()[1] + vel_p[2] * w.getnVec()[1];
                                    vel[2] = vel_p[0] * nx[2] + vel_p[1] * w.getnUp()[2] + vel_p[2] * w.getnVec()[2];
                                    vel_p = piw[1].portal.getOtherSide().getPos();
                                    pos[0] = vel_p[0];
                                    pos[1] = vel_p[1];
                                    pos[2] = vel_p[2];
                                    return false;
                                }
                            }
                        }
                        float[] vel_p = VecFactory.getProjection(nVec, md2, VecFactory.getAdd3(pos, vel));
                        vel_p = VecFactory.getDel3(vel_p, pos);
                        vel[0] = vel_p[0];
                        vel[1] = vel_p[1];
                        vel[2] = vel_p[2];
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean setPortal(float[] pos,Portal portal){
        float[] pv= VecFactory.getDel3(pos,md2);
        float[] nx = VecFactory.crossProduct(nUp, nVec);
        float x = VecFactory.dotProduct3(pv, nx);
        float y = VecFactory.dotProduct3(pv, nUp);
        if(Math.abs(x)<=width/2 && Math.abs(y)<=height/2){
            if(portal.isValid()){
                portal.getInWall().piw[portal.getIndex()].valid=false;
            }
            portal.setPos(VecFactory.getDel3(pos,VecFactory.getMultiply3(nVec,WALL_WIDTH*0.9f)));
            portal.setInWall(this);
            portal.setnUp(nUp);
            portal.setnVec(nVec);
            portal.setValid(true);
            piw[portal.getIndex()].portal=portal;
            piw[portal.getIndex()].valid=true;
            piw[portal.getIndex()].pos2[0]=x;
            piw[portal.getIndex()].pos2[1]=y;
            return true;
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

    class PortalInWall{
        boolean valid=false;
        float[] pos2;
        Portal portal;

        public PortalInWall(){
            pos2=new float[2];
        }
    }
}
