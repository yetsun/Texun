package com.yetsun.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class Galaxy extends ApplicationAdapter {
    //Texture img;
    private Texture TIEImage;
    private Texture XWingImage;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Vector3 touchPos;

    private Rectangle XWing;
    private Array<TIE> TIEPlatoon;

    private long lastDropTime;

    private int galaxyWidth = 800;
    private int galaxyHeight = 480;
    private int XWingWidth = 64;
    private int XWingHeight = 73;
    private int TIEWidth = 10;
    private int TIEHeight = 10;

    private int TIEPlatoonSize = 50;
    private int TIESpeed = 80;

    private boolean gameover = false;
    @Override
    public void create() {

        // load the images for the droplet and the XWing, 64x64 pixels each
        TIEImage = new Texture(Gdx.files.internal("TIE.png"));
        XWingImage = new Texture(Gdx.files.internal("X-Wing.png"));

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, galaxyWidth, galaxyHeight);

        XWing = new Rectangle();
        XWing.setX(galaxyWidth / 2 - XWingWidth / 2);
        XWing.setY(galaxyHeight / 2 - XWingHeight / 2);
        XWing.setWidth(XWingWidth);
        XWing.setHeight(XWingHeight);

        TIEPlatoon = new Array<TIE>(TIEPlatoonSize);

        for(int i = 0; i < TIEPlatoonSize; i++){
            TIEPlatoon.add(new TIE(TIEWidth, TIEHeight, TIESpeed, galaxyWidth, galaxyHeight));
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(XWingImage, (float) XWing.getX(), (float) XWing.getY());
        for (Rectangle TIE : TIEPlatoon) {
            batch.draw(TIEImage, TIE.x, TIE.y);
        }
        batch.end();

        if(!gameover) {
            //move the XWing
            if (Gdx.input.isTouched()) {
                if (touchPos == null) {
                    touchPos = new Vector3();
                }
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);

                XWing.setX(touchPos.x - XWingWidth / 2);
                XWing.setY(touchPos.y - XWingHeight / 2);
            }

            if (XWing.getX() < 0) XWing.setX(0);
            if (XWing.getX() > (galaxyWidth - XWingWidth)) XWing.setX(galaxyWidth - XWingWidth);

            if (XWing.y < 0) XWing.y = 0;
            if (XWing.y > (galaxyHeight - XWingHeight)) XWing.setX(galaxyHeight - XWingHeight);

            //move the TIE platoon
            Iterator<TIE> iter = TIEPlatoon.iterator();
            while (iter.hasNext()) {
                TIE t = iter.next();

                t.nextXY();

                //handle collision
                if (t.overlaps(XWing)) {
                    gameover = true;
                }
            }
        }

    }

    @Override
    public void dispose() {
        TIEImage.dispose();
        XWingImage.dispose();
        batch.dispose();
    }

}
