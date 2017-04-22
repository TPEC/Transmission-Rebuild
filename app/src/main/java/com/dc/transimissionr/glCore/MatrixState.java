package com.dc.transimissionr.glCore;
import android.opengl.Matrix;

import static com.dc.transimissionr.gameData.Constant.CAM_DISTANCE;
import static com.dc.transimissionr.gameData.Constant.EYE_HEIGHT;

public class MatrixState {
	private static float[] mProjMatrix = new float[16];
	private static float[] mVMatrix = new float[16];
	private static float[] currMatrix;

	static float[][] mStack=new float[8][16];
	static int stackTop=-1;

	public static void setInitStack() {
		currMatrix=new float[16];
		Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);
	}

	public static void pushMatrix(){
		stackTop++;
		for(int i=0;i<16;i++) {
			mStack[stackTop][i]=currMatrix[i];
		}
	}

	public static void popMatrix() {
		for(int i=0;i<16;i++) {
			currMatrix[i]=mStack[stackTop][i];
		}
		stackTop--;
	}

	public static void translate(float x,float y,float z) {
		Matrix.translateM(currMatrix, 0, x, y, z);
	}

	public static void rotate(float angle,float x,float y,float z) {
		Matrix.rotateM(currMatrix,0,angle,x,y,z);
	}

	public static void scale(float x,float y,float z) {
		Matrix.scaleM(currMatrix,0, x, y, z);
	}

	public static void pushCamera(){
		camStackTop++;
		for(int i=0;i<9;i++) {
			camStack[stackTop][i]=camStack[0][i];
		}
	}

	public static void popCamera() {
		setCamera(camStack[0][0],camStack[0][1],camStack[0][2],camStack[0][3],camStack[0][4],camStack[0][5],camStack[0][6],camStack[0][7],camStack[0][8]);
		camStackTop--;
	}

	static float[][] camStack=new float[4][9];
	static int camStackTop=0;

	public static void setCamera(float cx, float cy, float cz, float tx, float ty, float tz, float upx, float upy, float upz) {
		Matrix.setLookAtM(mVMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
		camStack[0][0]=cx;
		camStack[0][1]=cy;
		camStack[0][2]=cz;
		camStack[0][3]=tx;
		camStack[0][4]=ty;
		camStack[0][5]=tz;
		camStack[0][6]=upx;
		camStack[0][7]=upy;
		camStack[0][8]=upz;
	}

	public static void setProjectFrustum(float left, float right, float bottom, float top, float near, float far) {
		Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
	}

	public static void setProjectOrtho(float left, float right, float bottom, float top, float near, float far) {
		Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
	}

	static float[] mMVPMatrix=new float[16];
	public static float[] getFinalMatrix() {
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, currMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
		return mMVPMatrix;
	}

	public static float[] getMMatrix() {
		return currMatrix;
	}

	public static void setCamera(float[] pos,float[] head,float[]headh){
		setCamera(pos[0],pos[1]+EYE_HEIGHT,pos[2],pos[0]+head[0]*CAM_DISTANCE,pos[1]+head[1]*CAM_DISTANCE,pos[2]+head[2]*CAM_DISTANCE,headh[0],headh[1],headh[2]);
	}
}
