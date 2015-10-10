package com.yetsun.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by junye on 10/10/15.
 */
public class TIE extends Rectangle {


    float velocityX;
    float velocityY;
    int galaxyWidth;
    int galaxyHeight;
    int speed;


    void nextXY(){
        this.x = this.x + speed * velocityX * Gdx.graphics.getDeltaTime();
        this.y = this.y + speed * velocityY * Gdx.graphics.getDeltaTime();

        if(outOfTheGalaxy()){
            backToTheLine();
        }
    }

    void backToTheLine(){
        velocityX = (float)Math.random();
        velocityY = (float)Math.sqrt(1 - velocityX * velocityX);

        if(Math.random() > 0.5){
            velocityX *= -1;
        }

        if(Math.random() > 0.5){
            velocityY *= -1;
        }

        if(Math.random() > 0.5){
            x = (int)(Math.random() / 0.5) * galaxyWidth - width/2;
            y = (int)(Math.random() * galaxyHeight) - height/2;
        }else{
            x = (int)(Math.random() * galaxyWidth) - width/2;;
            y = (int)(Math.random() / 0.5) * galaxyHeight - height/2;
        }
    }

    public TIE(int width, int height, int speed, int galaxyWidth, int galaxyHeight){
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.galaxyHeight = galaxyHeight;
        this.galaxyWidth = galaxyWidth;
        this.backToTheLine();
    }

    public boolean outOfTheGalaxy(){
        if(x < (0 - width) || x > (galaxyWidth)
            || y < (0 - height) || y > (galaxyHeight)){
            return true;
        }else{
            return false;
        }
    }

}
