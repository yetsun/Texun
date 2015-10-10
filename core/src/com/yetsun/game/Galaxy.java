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
    private Array<Rectangle> TIEs;

    private long lastDropTime;

    private int galaxyX = 800;
    private int galaxyY = 480;
    private int XWingX = 64;
    private int XWingY = 73;
    private int TIEX = 10;
    private int TIEY = 10;

    @Override
    public void create() {

        // load the images for the droplet and the XWing, 64x64 pixels each
        TIEImage = new Texture(Gdx.files.internal("TIE.png"));
        XWingImage = new Texture(Gdx.files.internal("X-Wing.png"));

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, galaxyX, galaxyY);

        XWing = new Rectangle();
        XWing.setX(galaxyX / 2 - XWingX / 2);
        XWing.setY(galaxyY / 2 - XWingY / 2);
        XWing.setWidth(XWingX);
        XWing.setHeight(XWingY);

        TIEs = new Array<Rectangle>();
        spawnRaindrop();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(XWingImage, (float) XWing.getX(), (float) XWing.getY());
        for (Rectangle TIE : TIEs) {
            batch.draw(TIEImage, TIE.x, TIE.y);
        }
        batch.end();

        //move the XWing
        if (Gdx.input.isTouched()) {
            if (touchPos == null) {
                touchPos = new Vector3();
            }
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            XWing.setX(touchPos.x - XWingX / 2);
            XWing.setY(touchPos.y - XWingY / 2);
        }

        if (XWing.getX() < 0) XWing.setX(0);
        if (XWing.getX() > (galaxyX - XWingX)) XWing.setX(galaxyX - XWingX);

        if (XWing.y < 0) XWing.y = 0;
        if (XWing.y > (galaxyY - XWingY)) XWing.setX(galaxyY - XWingY);

        //move the drops
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

        Iterator<Rectangle> iter = TIEs.iterator();
        while (iter.hasNext()) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            if (raindrop.y < 0) iter.remove();

            //handle collision
            if (raindrop.overlaps(XWing)) {
                iter.remove();
            }
        }

    }

    @Override
    public void dispose() {
        TIEImage.dispose();
        XWingImage.dispose();
        batch.dispose();
    }

    private void spawnRaindrop() {
        Rectangle TIE = new Rectangle();
        TIE.x = MathUtils.random(0, galaxyX - XWingX);
        TIE.y = galaxyY;
        TIE.width = TIEX;
        TIE.height = TIEY;
        TIEs.add(TIE);
        lastDropTime = TimeUtils.nanoTime();
    }
}
