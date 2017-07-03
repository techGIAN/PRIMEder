package eecs.primeder;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity
{

    private String scoreString, percentageString, timeString, mistakeString;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle b = getIntent().getExtras();
        scoreString = b.getString("scoreHere");
        percentageString = b.getString("percentage");
        timeString = b.getString("timeVal");
        mistakeString = b.getString("mistakesMade");
        showResults();
        ChallengeActivity.ca.finish();
    }

    public void showResults() {
        ((TextView) findViewById(R.id.resultView)).setText("Your score is: " + this.scoreString + "/20" + "\n");
        ((TextView) findViewById(R.id.resultView)).append("In percent, this is: " + this.percentageString + "\n");
        ((TextView) findViewById(R.id.resultView)).append("Your time is: " + this.timeString + "\n");
        if (percentageString.equals("100.00%"))
            ((TextView) findViewById(R.id.resultView)).append("Congratulations on your perfect score!");
        else
            ((TextView) findViewById(R.id.resultView)).append("Your mistakes are: " + "\n" + this.mistakeString);

    }

    public void doneClick(View v) {
        this.finish();
        setContentView(R.layout.activity_home);
    }
}
