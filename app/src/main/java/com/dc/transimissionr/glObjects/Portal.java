package com.dc.transimissionr.glObjects;

import com.dc.transimissionr.R;
import com.dc.transimissionr.glCore.MatrixState;
import com.dc.transimissionr.glCore.TexFactory;
import com.dc.transimissionr.glCore.VecFactory;

import static com.dc.transimissionr.gameData.Constant.PORTAL_SIZE;

/**
 * Created by XIeQian on 2017/4/22.
 */

public class Portal {
    private Portal otherSide;
    private Wall inWall;
    private float[] pos;
    private float[] nVec;
    private float[] nUp;

    private int index;

    private TextureRect textureRect;
    private int texId;

    private boolean valid=false;

    public Portal(int index){
        this.index=index;
        texId= TexFactory.getNewTexture(R.drawable.floor1,-1);
        textureRect=new TextureRect(TexFactory.glSv,1f,1f);
    }

    public void setnVec(float[] nVec) {
        this.nVec = nVec;
    }

    public void setnUp(float[] nUp) {
        this.nUp = nUp;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getIndex() {
        return index;
    }

    public float[] getPos() {
        return pos;
    }

    public void setPos(float[] pos) {
        this.pos = pos;
    }

    public Portal getOtherSide() {
        return otherSide;
    }

    public Wall getInWall() {
        return inWall;
    }

    public void setInWall(Wall inWall) {
        this.inWall = inWall;
    }

    public void setOtherSide(Portal otherSide) {
        this.otherSide = otherSide;
    }

    public void draw(){
        if(valid) {
            MatrixState.pushMatrix();
            MatrixState.translate(pos[0], pos[1], pos[2]);
            MatrixState.rotate(VecFactory.getIncludedAngle2(new float[]{0, 1}, new float[]{nVec[0], nVec[2]}), 0, 1, 0);
            MatrixState.scale(PORTAL_SIZE, 1f, 1f);
            MatrixState.rotate(-VecFactory.getIncludedAngle2(new float[]{1, 0}, new float[]{nUp[1], nUp[2]}), 1, 0, 0);
            MatrixState.scale(1f, PORTAL_SIZE, 1f);
            textureRect.drawSelf(texId);
            MatrixState.popMatrix();
        }
    }
}
