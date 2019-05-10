package com.example.moodtracker.Controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodtracker.Manager.DatabaseManager;
import com.example.moodtracker.Manager.DateManager;
import com.example.moodtracker.Model.Mood;
import com.example.moodtracker.R;
import java.util.List;

public class StoryActivity extends AppCompatActivity {
    private LinearLayout storyLayout;
    private int widthDisplay;
    private int heightDisplay;
    private Activity activity=this;
    private DateManager dateManager = new DateManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        storyLayout=findViewById(R.id.storyLayout);
        Intent intent = getIntent();/** Recover width and height of main activity*/
        widthDisplay=intent.getIntExtra("width",0);
        heightDisplay= intent.getIntExtra("height",0);
        this.createStory();

        
    }/**Method create LinearLayout
     + using method createTextView
     + using method createImageButton
     */
    public void createLinearLayout(final int indexList){
        int indexMood=listSevenMood().get(indexList).getIndex();/**Recover the index of mood*/
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.width=(widthDisplay*(indexMood+1)/5);/**Config the width of layout in terms of the mood*/
        params.height=(heightDisplay/7);/**Config the height*/
            LinearLayout linearLayout = new LinearLayout(activity);
            int indexColor=listSevenMood().get(indexList).getIndex();
            linearLayout.setBackgroundColor(getResources().getColor(this.colorMood(indexColor)));
            linearLayout.setLayoutParams(params);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            storyLayout.addView(linearLayout);
            this.createTextView(indexList,linearLayout);/**Create TextView*/
            this.createButton(indexList,linearLayout);/**Create Button Comment*/

                }

    public void createTextView(int indexList,LinearLayout linearLayout){/**Method for create TextView*/
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10,10,10,0);/**Config of the text view*/
        TextView textView = new TextView(activity);
        String date = listSevenMood().get(indexList).getDateMood();/**Recover date of mood*/
        int dayMoreSeven=0;
        for (int indexDay = 1; indexDay <= 7; indexDay++) {
            if (date.equals(dateManager.testDateDayToDay(indexDay))) {/**Test date for the recover String of Story*/
                textView.setText(dateManager.listDayText(indexDay-1));
            }
            else{
                dayMoreSeven=dayMoreSeven+1;
            }
        }
        if(dayMoreSeven==7){/**Date superior 7 day*/
            textView.setText(dateManager.listDayText(7));
        }
        textView.setLayoutParams(params);
        linearLayout.addView(textView);/**Add text view of layout*/

    }
    @SuppressLint("ResourceAsColor")
    public void createButton(final int indexList, LinearLayout linearLayout){/**Method for create Button if comment exist*/
        String comment = listSevenMood().get(indexList).getCommentMood();
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity=Gravity.RIGHT;
        params.setMargins(10,0,30,10);
        if(!comment.equals("")){/**If comment exist*/
            ImageButton button = new ImageButton(this);
            button.setImageResource(R.drawable.ic_comment_black_48px);
            button.setBackgroundColor(android.R.color.transparent);
            button.setOnClickListener(new View.OnClickListener() {/**Set button clic for Toast Comment*/
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity,listSevenMood().get(indexList).getCommentMood(),Toast.LENGTH_LONG).show();
                }
            });
            button.setLayoutParams(params);
            linearLayout.addView(button);
        }


    }

    public List<Mood> listSevenMood() {/**List of seven last mood extract to sqlite data base*/
        DatabaseManager databaseManager=new DatabaseManager(activity);
        List<Mood> sevenLastMoodSave = databaseManager.readSevenLastMoodSave();
        databaseManager.close();
        return sevenLastMoodSave;
    }

    public void createStory() {/**Method for create Story
             +
             Using method createLinearLayout in terms of index mood */
                for (int i = listSevenMood().size()-1; i >= 0; i--) {
                    createLinearLayout(i);//Create layout

        }
    }
    public static int colorMood(int indexColor){
        int  colorsViewTab[]={R.color.faded_red,R.color.warm_grey,R.color.cornflower_blue_65,R.color.light_sage,R.color.banana_yellow};
        return colorsViewTab[indexColor];
    }
}

