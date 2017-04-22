package com.dc.transimissionr.glCore;

/**
 * Created by XIeQian on 2017/4/10.
 */

public class VecFactory {
    public static float[] crossProduct(float[] va,float[] vb){
        float[] vc=new float[3];
        vc[0]=va[1]*vb[2]-va[2]*vb[1];
        vc[1]=va[2]*vb[0]-va[0]*vb[2];
        vc[2]=va[0]*vb[1]-va[1]*vb[0];
        return vc;
    }

    public static float dotProduct3(float[] va,float[] vb){
        return va[0]*vb[0]+va[1]*vb[1]+va[2]*vb[2];
    }

    public static void unitize3(float[] vec){
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

    public static void multiply3(float[] vec, float k){
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

    public static float getIncludedAngle2(float[] vBase,float[] vec){
        if(vec[0]==0 && vec[1]==0)
            return 0;
        float a= (float) Math.acos((vBase[0]*vec[0]+vBase[1]*vec[1])/getLength2(vBase)/getLength2(vec));
        if(vBase[0]*vec[1]-vBase[1]*vec[0]>0)
            a=-a;
        return (float) (a*180/Math.PI);
    }

    public static float[] getProjection(float[] n, float[] m, float[] p) {//得出p在平面的投影
        float[] f = new float[3];
        if(n[0]==0){
            f[0]=p[0];
            f[1]=n[1]*n[1]*m[1]+n[2]*n[2]*p[1]-n[1]*n[2]*(p[2]-m[2]);
            f[2]=n[2]*n[2]*m[2]+n[1]*n[1]*p[2]-n[2]*n[1]*(p[1]-m[1]);
        }else{
            float a0 = n[1] * p[0] - n[0] * p[1];
            float a1 = n[2] * p[0] - n[0] * p[2];
            float a2 = n[0] * m[0] + n[1] * m[1] + n[2] * m[2];
            float d1 = a2 * n[0] * n[0] + a0 * n[0] * n[1] + a1 * n[0] * n[2];
            float d2 = n[1] * a1 * n[2] - a0 * n[0] * n[0] + n[1] * n[0] * a2 - a0 * n[2] * n[2];
            float d3 = n[2] * n[1] * a0 - n[0] * n[0] * a1 - n[1] * n[1] * a1 + n[0] * n[2] * a2;
            float d = n[0] * n[0] * n[0] + n[0] * n[1] * n[1] + n[0] * n[2] * n[2];
            f[0] = d1 / d;
            f[1] = d2 / d;
            f[2] = d3 / d;
        }
        return f;
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

    public static float[] getCrossPoint(float[] n, float[] m, float[] p, float[] v) {//得出射线与平面交点
        float[] f = new float[3];
        if(n[0]==0){
            f[1]=(n[2]*v[2]*p[1]+n[1]*v[1]*m[1]-n[2]*v[1]*(p[2]-m[2]))/(n[2]*v[2]+n[1]*v[1]);
            f[2]=(n[1]*v[1]*p[2]+n[2]*v[2]*m[2]-n[1]*v[2]*(p[1]-m[1]))/(n[1]*v[1]+n[2]*v[2]);
            if(v[1]==0)
                f[0]=v[0]*(f[2]-p[2])/v[2]+p[0];
            else
                f[0]=v[0]*(f[1]-p[1])/v[1]+p[0];
        }else{
            float a0 = v[1] * p[0] - v[0] * p[1];
            float a1 = v[2] * p[0] - v[0] * p[2];
            float a2 = n[0] * m[0] + n[1] * m[1] + n[2] * m[2];
            float d1 = a2 * v[0] * v[0] + a0 * v[0] * n[1] + a1 * v[0] * n[2];
            float d2 = v[1] * a1 * v[2] - a0 * v[0] * n[0] + v[1] * v[0] * a2 - a0 * v[2] * n[2];
            float d3 = v[2] * n[1] * a0 - n[0] * v[0] * a1 - v[1] * n[1] * a1 + v[0] * v[2] * a2;
            float d = n[0] * v[0] * v[0] + v[0] * v[1] * n[1] + v[0] * v[2] * n[2];
            f[0] = d1 / d;
            f[1] = d2 / d;
            f[2] = d3 / d;
        }
        return f;
    }

}
