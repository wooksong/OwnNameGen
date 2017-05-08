package wookdev.team.ownnamegen;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by wook on 2017-05-02.
 */

public class Letter {
    String ch_letter;
    String kr_letter;
    String mean;
    String proFiveElement;
    String radFiveElement;
    int stroke;
    boolean isLetter;
    int pos;

    public Letter() {
        this.ch_letter = "";
        this.mean = "";
        this.isLetter = false;
    }

    public Letter(String ch_letter) {
        this.ch_letter = ch_letter;
        this.isLetter = true;
    }

    public Letter(String kr_letter, String ch_letter, String mean) {
        this.kr_letter = kr_letter;
        this.ch_letter = ch_letter;
        this.mean = mean;
        this.isLetter = true;
    }

    public String getKrLetter(){
        return this.kr_letter;
    }
    public String getChLetter(){
        return this.ch_letter;
    }
    public String getMean(){
        return this.mean;
    }
    public String getProFiveElement() { return  this.proFiveElement; }
    public String getRadFiveElement() { return  this.radFiveElement; }
    public int getStroke()  {return this.stroke; }
    public int getPos()  {return this.pos; }
    public boolean isLetter() {return  this.isLetter; }

    public void setKrLetter(String letter) { this.kr_letter = letter;}
    public void setChLetter(String letter) { this.ch_letter = letter;}
    public void setMean(String letter) { this.mean = letter;}
    public void setProFiveElement(String element) { this.proFiveElement = element; }
    public void setRadFiveElement(String element) { this.radFiveElement = element; }
    public void setStroke(int cnt)  { this.stroke = cnt; }
    public void setLetter(boolean flag) { this.isLetter = flag; }
    public void setPos(int pos)  { this.pos = pos; }
}
