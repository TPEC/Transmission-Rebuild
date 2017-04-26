package com.dc.transimissionr.glObjects;

import com.dc.transimissionr.glCore.VecFactory;

import static com.dc.transimissionr.gameData.Constant.ACC_SCALE;
import static com.dc.transimissionr.gameData.Constant.MAX_HORIZON_VEL;
import static com.dc.transimissionr.gameData.Constant.MAX_VERTICAL_VEL;

/**
 * Created by XIeQian on 2017/4/3.
 */

public class Role{
    private float[] pos;
    private float[] vel;
    private float[] acc;
    private float[] cam,camh;
    private boolean onFloor;

    boolean alive=true;

    public Role(){
        pos=new float[3];
        vel=new float[3];
        acc=new float[3];
        cam=new float[3];
        camh=new float[3];
        pos[1]=20;
        cam[0]=0;
        cam[1]=0;
        cam[2]=-1;
        camh[1]=1;
    }

    public void setGravity(float gravity){
        acc[1]=-gravity;
    }

    public void setAcc2(float[] acc){
        this.acc[0]=acc[0]* ACC_SCALE;
        this.acc[2]=acc[1]* ACC_SCALE;
    }

    public boolean isOnFloor() {
        return onFloor;
    }

    public void setOnFloor(boolean onFloor) {
        this.onFloor = onFloor;
    }

    public void setPos(float[] pos) {
        this.pos = pos;
    }

    public void setVel(float[] vel) {
        this.vel = vel;
    }

    public void setCam(float[] cam) {
        this.cam = cam;
    }

    public void setCamh(float[] camh) {
        this.camh = camh;
    }

    public float[] getPos() {
        return pos;
    }

    public float[] getVel() {
        return vel;
    }

    public float[] getCam() {
        return cam;
    }

    public float[] getCamh(){
        return camh;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void calcVel(){
        vel[0]+=acc[0];
        vel[1]+=acc[1];
        vel[2]+=acc[2];
        if(vel[1]<-MAX_VERTICAL_VEL)
            vel[1]=-MAX_VERTICAL_VEL;
        float[] v2=new float[]{vel[0],vel[2]};
        if(VecFactory.getLength2(v2)>MAX_HORIZON_VEL){
            VecFactory.unitize2(v2);
            VecFactory.multiply2(v2,MAX_HORIZON_VEL);
            vel[0]=v2[0];
            vel[2]=v2[1];
        }
    }

    public void runLogic(){
        pos[0]+=vel[0];
        pos[1]+=vel[1];
        pos[2]+=vel[2];

        if(pos[1]<-30f){
            alive=false;
        }
    }
}
