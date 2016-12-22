package edu.nju.exam.Model;

import java.io.Serializable;

/**
 * Created by zhouxiaofan on 2016/12/11.
 */
public class Score  implements Serializable {
    private double score;
    private String ename;

    public Score(String ename,double score){
        this.ename=ename;
        this.score=score;
    }

    public double getScore() {
        return score;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public void setScore(double score) {
        this.score = score;
    }


}
