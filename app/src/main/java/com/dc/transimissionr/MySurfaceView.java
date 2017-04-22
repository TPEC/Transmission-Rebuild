package com.dc.transimissionr;

import android.content.Context;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.dc.transimissionr.Controller.MoveController;
import com.dc.transimissionr.Controller.PortalController;
import com.dc.transimissionr.Controller.ViewController;
import com.dc.transimissionr.glCore.MatrixState;
import com.dc.transimissionr.glCore.TexFactory;
import com.dc.transimissionr.glCore.VecFactory;
import com.dc.transimissionr.glObjects.CrossLine;
import com.dc.transimissionr.glObjects.Role;
import com.dc.transimissionr.glObjects.WallsManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.dc.transimissionr.gameData.Constant.CAM_SCALE;
import static com.dc.transimissionr.gameData.Constant.ratio;
import static com.dc.transimissionr.gameData.Constant.ratio_height;
import static com.dc.transimissionr.gameData.Constant.ratio_width;

public class MySurfaceView extends GLSurfaceView
{
    private static final float ProjectFrustum_RATIO=0.05f;
    private SceneRenderer mRenderer;

    private MoveController moveController;
    private PortalController portalController;
    private ViewController viewController;

	public MySurfaceView(Context context) {
        super(context);
        TexFactory.glSv=this;

        this.setEGLContextClientVersion(2);
        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        this.setKeepScreenOn(true);

        RectF rectMC=new RectF(80,420,320,660);
        moveController=new MoveController(rectMC);
        RectF rectPC=new RectF(960,540,1080,660);
        portalController=new PortalController(rectPC);
        viewController=new ViewController(new RectF(0,0,1280,720),new RectF[]{rectMC,rectPC});
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        moveController.onTouchEvent(event);
        portalController.onTouchEvent(event);
        viewController.onTouchEvent(event);
        if(moveController.getClickDown()) {
            mRenderer.setRoleVel(moveController.getMoveVector());
        }
        if(portalController.getClicked()){

        }
        if(viewController.getClickDown()){
            mRenderer.setRoleCam(viewController.getMoveVector());
        }
        return true;
    }

	private class SceneRenderer implements GLSurfaceView.Renderer
    {
        private WallsManager wallsManager;
        private Role role;
        private CrossLine crossLine;
    	
        public void onDrawFrame(GL10 gl)
        {
            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

            gameLogic();
            drawGameScene();
            drawHUD();
        }  

        public void onSurfaceChanged(GL10 gl, int width, int height) {
        	GLES20.glViewport(0, 0, width, height);
            ratio = (float) width / height;
            MatrixState.setProjectFrustum(-ratio*ProjectFrustum_RATIO, ratio*ProjectFrustum_RATIO, -ProjectFrustum_RATIO, ProjectFrustum_RATIO, 0.1f, 100);
			MatrixState.setCamera(1,2,3,0f,0f,0f,0f,1.0f,0.0f);
            MatrixState.setInitStack();
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0f,0f,0f, 1.0f);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            GLES20.glEnable(GLES20.GL_CULL_FACE);

            wallsManager=new WallsManager();
            wallsManager.initTexture();
            wallsManager.addWall(6,6);
            moveController.initTexture();
            portalController.initTexture();
            crossLine=new CrossLine(50f,5f);
            crossLine.initTexture();
            role=new Role();
            role.setGravity(0.1f);
        }

        public void drawGameScene(){
            MatrixState.setProjectFrustum(-ratio*ProjectFrustum_RATIO, ratio*ProjectFrustum_RATIO, -ProjectFrustum_RATIO, ProjectFrustum_RATIO, 0.1f, 100);
            wallsManager.draw();
        }

        public void drawHUD(){
            MatrixState.pushMatrix();
            MatrixState.pushCamera();
            MatrixState.setProjectOrtho(-6.4f/ratio_width,6.4f/ratio_width,-3.6f/ratio_height,3.6f/ratio_height,0.1f,100);
            MatrixState.scale(0.01f/ratio_width,0.01f/ratio_height,1f);
            MatrixState.setCamera(0,0,1,0,0,0,0,1,0);
            moveController.draw();
            portalController.draw();
            crossLine.draw();
            MatrixState.popCamera();
            MatrixState.popMatrix();
        }

        public void gameLogic(){

            if(moveController.getClickDown())
                setRoleVel(moveController.getMoveVector());
            else {
                float[] v=new float[]{role.getVel()[0],role.getVel()[2]};
                VecFactory.multiply2(v,-10f);
                role.setAcc2(v);
            }
            role.calcVel();
            wallsManager.calcCollision(role.getPos(),role.getVel());
            role.runLogic();
            MatrixState.setCamera(role.getPos(),role.getCam(),role.getCamh());
        }

        public void setRoleVel(float[] vVel){
            float[] vec3= VecFactory.crossProduct(role.getCam(),role.getCamh());
            float[] vec2=new float[2];
            if(vec3[0]!=0){
                vec2[1]=1;
                vec2[0]=-vec3[2]/vec3[0];
            }else{
                vec2[0]=1;
                vec2[1]=-vec3[0]/vec3[2];
            }
            float k=vec2[0]*role.getCam()[0]+vec2[1]*role.getCam()[2];
            if(k>0) {
                VecFactory.multiply2(vec2, -1f);
            }else if(k==0){
                if(vec2[0]*role.getCamh()[0]+vec2[1]*role.getCamh()[2]<0)
                    VecFactory.multiply2(vec2, -1f);
            }
            VecFactory.rotate2(vec2,new float[]{vVel[1],-vVel[0]});
            VecFactory.unitize2(vec2);

            role.setAcc2(vec2);
        }
        public void setRoleCam(float[] vCam){
            VecFactory.rotate3y(role.getCam(),vCam[0]*CAM_SCALE);
            VecFactory.rotate3y(role.getCamh(),vCam[0]*CAM_SCALE);
            float[] vec3=VecFactory.crossProduct(role.getCamh(),role.getCam());
            VecFactory.rotate3(role.getCam(),vCam[1]*CAM_SCALE,vec3);
            VecFactory.rotate3(role.getCamh(),vCam[1]*CAM_SCALE,vec3);
            float a=Math.abs(VecFactory.getIncludedAngle2(new float[]{0,1,0},role.getCam()));
            if(a<90){
                a+=Math.abs(VecFactory.getIncludedAngle2(new float[]{0,1,0},role.getCamh()));
                if(a>90){
                    role.setCam(new float[]{0,1,0});
                    vec3=role.getCamh();
                    vec3[1]=0;
                    VecFactory.unitize3(vec3);
                    role.setCamh(vec3);
                }
            }else{
                a=180-a+Math.abs(VecFactory.getIncludedAngle2(new float[]{0,1,0},role.getCamh()));
                if(a>90){
                    role.setCam(new float[]{0,-1,0});
                    vec3=role.getCamh();
                    vec3[1]=0;
                    VecFactory.unitize3(vec3);
                    role.setCamh(vec3);
                }
            }
        }
        public void addPortal(int index){

        }
    }
}