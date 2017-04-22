package com.dc.transimissionr.glCore;

/**
 * Created by XIeQian on 2017/4/10.
 */

public class VecFactory {
    public static float[] crossProduct(float[] va,float[] vb){//叉乘
        float[] vc=new float[3];
        vc[0]=va[1]*vb[2]-va[2]*vb[1];
        vc[1]=va[2]*vb[0]-va[0]*vb[2];
        vc[2]=va[0]*vb[1]-va[1]*vb[0];
        return vc;
    }

    public static float dotProduct3(float[] va,float[] vb){//点乘
        return va[0]*vb[0]+va[1]*vb[1]+va[2]*vb[2];
    }

    public static void unitize3(float[] vec){//单位化
        float s=getLength3(vec);
        if(s>0){
            s=1/s;
            multiply3(vec,s);
        }
    }

    public static void unitize2(float[] vec){
        float s= getLength2(vec);
        if(s>0){
            s=1/s;
            multiply2(vec,s);
        }
    }

    public static float getLength2(float[] vec){
        return (float) Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]);
    }
    public static float getLength3(float[] vec){
        return (float) Math.sqrt(vec[0]*vec[0]+vec[1]*vec[1]+vec[2]*vec[2]);
    }

    public static float getDistance3(float[] va, float[] vb){
        return getLength3(getDel3(va,vb));
    }

    public static void multiply3(float[] vec, float k){//向量数乘
        vec[0]*=k;
        vec[1]*=k;
        vec[2]*=k;
    }

    public static float[] getMultiply3(float[] vec, float k){
        float[] f=new float[3];
        f[0]=vec[0]*k;
        f[1]=vec[1]*k;
        f[2]=vec[2]*k;
        return f;
    }

    public static void multiply2(float[] vec, float k){
        vec[0]*=k;
        vec[1]*=k;
    }

    public static void rotate2(float[] vec,float[] ur){
        float v0=ur[0]*vec[0]-ur[1]*vec[1];
        float v1=ur[1]*vec[0]+ur[0]*vec[1];
        vec[0]=v0;
        vec[1]=v1;
    }

    public static void rotate3y(float[] vec,float a){
        float c=(float) Math.cos(a);
        float s=(float) Math.sin(a);
        float v0=c*vec[0]-s*vec[2];
        float v2=c*vec[2]+s*vec[0];
        vec[0]=v0;
        vec[2]=v2;
    }

    public static void rotate3(float[] vec,float a,float[] base){
        float c=(float) Math.cos(a);
        float s=(float) Math.sin(a);
        float v0=(c+base[0]*base[0]*(1-c))*vec[0]+(base[0]*base[1]*(1-c)-base[2]*s)*vec[1]+(base[0]*base[2]*(1-c)+base[1]*s)*vec[2];
        float v1=(base[0]*base[1]*(1-c)+base[2]*s)*vec[0]+(c+base[1]*base[1]*(1-c))*vec[1]+(base[1]*base[2]*(1-c)-base[0]*s)*vec[2];
        float v2=(base[0]*base[2]*(1-c)-base[1]*s)*vec[0]+(base[1]*base[2]*(1-c)+base[0]*s)*vec[1]+(c+base[2]*base[2]*(1-c))*vec[2];
        vec[0]=v0;
        vec[1]=v1;
        vec[2]=v2;
    }

    public static float getIncludedAngle2(float[] vBase,float[] vec){//获取vBase和vec夹角（角度制）
        if(vec[0]==0 && vec[1]==0)
            return 0;
        float a= (float) Math.acos((vBase[0]*vec[0]+vBase[1]*vec[1])/getLength2(vBase)/getLength2(vec));
        if(vBase[0]*vec[1]-vBase[1]*vec[0]>0)
            a=-a;
        return (float) (a*180/Math.PI);
    }

    public static float[] getAdd3(float[] va,float[] vb){
        float[] vec=new float[3];
        vec[0]=va[0]+vb[0];
        vec[1]=va[1]+vb[1];
        vec[2]=va[2]+vb[2];
        return vec;
    }

    public static float[] getDel3(float[] va,float[] vb){
        float[] vec=new float[3];
        vec[0]=va[0]-vb[0];
        vec[1]=va[1]-vb[1];
        vec[2]=va[2]-vb[2];
        return vec;
    }

    public static float[] getProjection(float[] n, float[] m, float[] p) {//得出p在平面的投影
        float[] f = new float[3];
        float t=(n[0]*m[0]+n[1]*m[1]+n[2]*m[2]-n[0]*p[0]-n[1]*p[1]-n[2]*p[2])/(n[0]*n[0]+n[1]*n[1]+n[2]*n[2]);
        f[0]=p[0]+n[0]*t;
        f[1]=p[1]+n[1]*t;
        f[2]=p[2]+n[2]*t;
        return f;
    }

    public static float[] getCrossPoint(float[] n, float[] m, float[] p, float[] v) {//得出射线与平面交点
        float[] f = new float[3];
        float t=(n[0]*m[0]+n[1]*m[1]+n[2]*m[2]-n[0]*p[0]-n[1]*p[1]-n[2]*p[2])/(n[0]*v[0]+n[1]*v[1]+n[2]*v[2]);
        f[0]=p[0]+v[0]*t;
        f[1]=p[1]+v[1]*t;
        f[2]=p[2]+v[2]*t;
        return f;
    }

}
