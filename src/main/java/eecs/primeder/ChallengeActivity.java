package eecs.primeder;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ChallengeActivity extends AppCompatActivity
{

    private long myTime = 10;
    //private long val = 0;
    private Timer t;
    private ChallengeModel cm;
    private int rInt, counter, scoreHolder;
    public static Activity ca;
    private String timeStamp = "00:00:00";
    private boolean wrongAnswer = false;
    private String dType;
    private boolean timerStarted = false;
    private int intervalLeft = 0;
    private int intervalRight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle b = getIntent().getExtras();
        dType = b.getString("difficulty");
        //new AlertDialog.Builder(this).setMessage(dType).show();
        ca = this;
        counter = 1;
        scoreHolder = 0;
        switch (dType) {
            case "Easy":
                intervalLeft = 2;
                intervalRight = 100;
                break;
            case "Average":
                intervalLeft = 80;
                intervalRight = 200;
                break;
            case "Difficult":
                intervalLeft = 185;
                intervalRight = 500;
                break;
            case "Extreme":
                intervalLeft = 425;
                intervalRight = 1000;
                break;
            default:    break;
        }
        cm = new ChallengeModel(intervalLeft,intervalRight);
        MediaPlayer mpRace = MediaPlayer.create(this,R.raw.race);
        mpRace.start();
        CountDownTimer t = new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
                long v = millisUntilFinished/1000-1;
                ((TextView) findViewById(R.id.numberView)).setText(Long.toString(v));
                if (v == 0) {
                    ((TextView) findViewById(R.id.numberView)).setText("G0!");
                    ((TextView) findViewById(R.id.readyTextView)).setText("");
                }
            }

            public void onFinish() {
                ((TextView) findViewById(R.id.numberView)).setText("G0!");
                CountDownTimer ct = new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        startup();
                    }
                }.start();

            }
        }.start();
    }

    public void startup() {
        gameOn();
        t = new Timer();        // A thread of execution is instantiated
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //val++;
                        timerStarted = true;
                        TextView tv = (TextView) findViewById(R.id.timerView);
                        String timeSet = tv.getText().toString();
                        String[] timeSplit = timeSet.split(":");
                        //System.out.println("TEST: " + timeSplit[0]);
                        long hr = Long.parseLong(timeSplit[0]);
                        long min = Long.parseLong(timeSplit[1]);
                        long sec = Long.parseLong(timeSplit[2]);

                        sec++;
                        if (wrongAnswer) {
                            wrongAnswer = false;
                            sec = sec+9;
                        }

                        if (sec >= 60) {
                            min++;
                            sec = 0;
                        }

                        if (min >= 60) {
                            hr++;
                            min = 0;
                        }

                        String x = String.format("%02d",hr);
                        String y = String.format("%02d",min);
                        String z = String.format("%02d",sec);

                        timeStamp = x + ":" + y + ":" + z;
                        ((TextView) findViewById(R.id.timerView)).setText(timeStamp);
                        if (min > myTime) t.cancel();
                    }
                });
            }
        },0,1000);
    }

    public void gameOn() {
        ((TextView) findViewById(R.id.itemNumber)).setText("#" + counter);
        this.rInt = cm.randomGenerator();
        ((TextView) findViewById(R.id.numberView)).setText(Integer.toString(rInt));
    }

    public void primeActivate(View v) {
        cm.lastAnsToggle(true);
        MediaPlayer mpCorrect = MediaPlayer.create(this, R.raw.correct);
        MediaPlayer mpWrong = MediaPlayer.create(this, R.raw.wrong);
        System.out.println("TEST: PRIME");
        boolean guessPrime = true;
        boolean isPrime = cm.isPrime(this.rInt);
        cm.record(guessPrime == isPrime);
        if (guessPrime == isPrime) {
            scoreHolder++;
            mpCorrect.start();
            //if (mpCorrect.isPlaying()) mpCorrect.stop();
            ((TextView) findViewById(R.id.scoreView)).setText("Score: " + scoreHolder + "/20");
        } else {
            wrongAnswer = true;
            mpWrong.start();
            //if (mpWrong.isPlaying()) mpWrong.stop();
        }
        counter++;
        if (counter < 21) gameOn();
        else endGame();
    }

    public void compositeActivate(View v) {
        cm.lastAnsToggle(false);
        MediaPlayer mpCorrect = MediaPlayer.create(this, R.raw.correct);
        MediaPlayer mpWrong = MediaPlayer.create(this, R.raw.wrong);
        System.out.println("TEST: COMPOSITE");
        boolean guessPrime = false;
        boolean isPrime = cm.isPrime(this.rInt);
        cm.record(guessPrime == isPrime);
        if (guessPrime == isPrime) {
            scoreHolder++;
            mpCorrect.start();
            //if (mpCorrect.isPlaying()) mpCorrect.stop();
            ((TextView) findViewById(R.id.scoreView)).setText("Score: " + scoreHolder + "/20");
        } else {
            wrongAnswer = true;
            mpWrong.start();
            //if (mpWrong.isPlaying()) mpWrong.stop();
        }
        counter++;
        if (counter < 21) gameOn();
        else endGame();
    }

    public void endGame() {
        cm.rawScoreChange(scoreHolder);
        t.cancel();
        //this.finish();
        Intent intent = new Intent(this, EndActivity.class);

        //Bundle bundle = new Bundle();
        intent.putExtra("scoreHere", Integer.toString(this.scoreHolder));
        intent.putExtra("percentage", String.format("%.2f",cm.getPercentage()) + "%");
        intent.putExtra("timeVal", timeStamp);

        String mistakes = "Mistake \t Your Answer \t Correct Answer \n";
        for (String g : cm.getMistakes()) {
            mistakes = mistakes + g + "\n";
        }
        intent.putExtra("mistakesMade", mistakes);
        System.out.println("My score: " + cm.getPercentage());
        //intent.putExtras(bundle);
        this.startActivity(intent);
    }

    public void quit(View v) {
        if (this.timerStarted) t.cancel();
        this.finish();
        setContentView(R.layout.activity_home);
    }
}
