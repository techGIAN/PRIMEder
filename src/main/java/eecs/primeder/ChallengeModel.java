package eecs.primeder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by user on 4/5/17.
 */
public class ChallengeModel
{
    private int leftPoint, rightPoint;
    private int rawScore, items;
    private double percentage;
    private long timeElapsed, timeStart;
    private List<Integer> numList;
    private List<String> wrongAnswers;
    private boolean primeLastAnswer = true;

    public ChallengeModel (int l, int r) {
        this.leftPoint = l;
        this.rightPoint = r;
        this.rawScore = 0;
        this.percentage = 0;
        this.timeStart = (new Date()).getTime();
        this.timeElapsed = 0;
        this.items = 0;
        this.numList = new ArrayList<Integer>();
        this.wrongAnswers = new ArrayList<String>();
    }

    public int getLeftPoint() {
        return this.leftPoint;
    }

    public int getRightPoint() {
        return this.rightPoint;
    }

    public void rawScoreChange(int n) {
        this.rawScore = n;
    }
    public int getRawScore() {
        return this.rawScore;
    }

    public double getPercentage() {
        this.percentage = getRawScore() / (20.0);
        return this.percentage * 100;
    }

    public List<String> getMistakes() {
        return this.wrongAnswers;
    }

    public int randomGenerator() {
        Random rng = new Random();
        int randomInt = rng.nextInt(getRightPoint() - getLeftPoint()) + getLeftPoint();
        while (randomInt % 2 == 0 || numList.contains(randomInt)) {
            randomInt = rng.nextInt(getRightPoint() - getLeftPoint()) + getLeftPoint();
        }
        numList.add(randomInt);
        return randomInt;
    }

    public void resetTimer() {
        this.timeStart = (new Date()).getTime();
    }

    public long getStart() {
        return this.timeStart;
    }

    public long getElapsedTime() {
        long current = (new Date()).getTime();
        this.timeElapsed = current - this.timeStart;
        return this.timeElapsed;
    }

    public boolean isPrime(int n) {
        if (n % 2 == 0) return false;
        for(int i = 3; i*i <= n; i +=2) {
            if(n % i == 0) return false;
        }
        return true;
    }

    public void lastAnsToggle(boolean ans) {
        if (ans) this.primeLastAnswer = true;
        else this.primeLastAnswer = false;
    }

    public boolean getLastAns() {
        return this.primeLastAnswer;
    }

    public void record(boolean success) {
        this.items++;
        if (success)
            this.rawScore++;
        else {
            String additionalComponent = "";
            if (numList.get(numList.size()-1) < 10) additionalComponent = "\t";
            String s = "" + numList.get(numList.size()-1) + "\t\t\t" + additionalComponent;
            if (getLastAns()) s += "\t Prime \t\t\t\t Not Prime";
            else s += "\t Not Prime \t\t Prime";
            this.wrongAnswers.add(s);
        }
    }
}
