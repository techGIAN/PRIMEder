package eecs.primeder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

public class HomeActivity extends AppCompatActivity
{

    private CharSequence[] difficulties = {"Easy", "Average", "Difficult", "Extreme"};
    private int inputSelection = -1;
    private Dialog levelDialog;
    private Context c = this;
    private Activity thisActivity = this;
    //private ToneGenerator tg;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //tg = new ToneGenerator(AudioManager.STREAM_MUSIC,ToneGenerator.MAX_VOLUME);
    }

    public void challengeClick(View v) {
        //tg.startTone(ToneGenerator.TONE_CDMA_ANSWER, 300);
        //tg.startTone(ToneGenerator.TONE_DTMF_P, 300);        wrong answer
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select difficulty");

        //int inputSelection = -1;
        //Dialog levelDialog;
        builder.setSingleChoiceItems(difficulties,inputSelection,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        inputSelection = item;
                        levelDialog.dismiss();
                        //thisActivity.finish();
                        Intent intent = new Intent(c, ChallengeActivity.class);
                        String difficultyType = difficulties[inputSelection].toString();
                        intent.putExtra("difficulty",difficultyType);
                        startActivity(intent);
                    }
                });
        levelDialog = builder.create();
        levelDialog.show();
    }

    public void getTips(View v) {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Tips on Primes");
        String help = "*Primes are numbers whose only positive factors are 1 and itself." + "\n";
        help = help + "*An even number is never prime, except for the number 2." + "\n";
        help = help + "*If a number ends in 5, it is never prime, except for the number 5 itself." + "\n";
        help = help + "*If the sum of the digits of a number is divisible by 3, then it is not prime, except for the number 3." + "\n";
        help = help + "*Not a guarantee, but a number is prime if it can be written in the form 2^p - 1, where p is another prime." + "\n";
        help = help + "*Be careful with pseudo-primes! They're like catfishes.";
        build.setMessage(help);
        build.show();
    }
}
