package com.example.moodtracker.Controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.moodtracker.Manager.DatabaseManager;
import com.example.moodtracker.Manager.DateManager;
import com.example.moodtracker.Manager.SwipeDetector;
import com.example.moodtracker.Model.Mood;
import com.example.moodtracker.R;



public class MainActivity extends AppCompatActivity {
    /**
     * Variable declaration
     */
    private ImageButton buttonStory, buttonComment,buttonShare;
    private ImageView imageSmiley;
    private SwipeDetector swipeDetector;
    private View layoutMain;
    private Mood currentMood, lastMood;
    private EditText inputComment;
    private DatabaseManager databaseManager = new DatabaseManager(this);
    private DateManager dateManager=new DateManager();
    private String today = dateManager.getCurrentDate();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.init();/**init method invoked*/
        this.alertDialog();/**alertDialog method invoked*/
        this.startMood();
        this.startStory();
        this.startShare();

    }
    /**
     * Link graphic object - Java object*
     * + Create SwipeDetector + DataBasemanager
     */
    private void init() {
        swipeDetector = new SwipeDetector(this);
        buttonStory = (ImageButton) findViewById(R.id.buttonStory);
        buttonComment = (ImageButton) findViewById(R.id.buttonComment);
        imageSmiley = (ImageView) findViewById(R.id.imageSmiley);
        layoutMain = (View) findViewById(R.id.layoutMain);
        layoutMain.setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
        buttonShare=(ImageButton)findViewById(R.id.buttonShare);

    }


    /**
     * Delegation Android detector when swipe event
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (swipeDetector.onTouchEvent(event)) { /**If swipe event = listener is swipeDetector*/
            return false;
        } else { /**Else detector is onTouchEvent*/
            return true;
        }
    }

    /**
     * onSwipe method for change graphic view and save current mood
     */
    public void onSwipe(SwipeDetector.SwipeDirection direction) {
        switch (direction) {
            case BOTTOM_TO_TOP:
                if (currentMood.getIndex() < 4) {
                    currentMood.setIndex(currentMood.getIndex() + 1);/** increase index mood if index inferior 4*/

                }
                break;
            case TOP_TO_BOTTOM:
                if (currentMood.getIndex() > 0) {
                    currentMood.setIndex(currentMood.getIndex() - 1);/** decrease index mood if index superior a 0*/
                }
                break;
        }
        databaseManager.updateIndexMood(currentMood.getIndex()); /**save mood in sqlite data base*/
        databaseManager.close();
        this.setImageSmileyAndBackground(layoutMain, imageSmiley, this, currentMood);/** change graphic view in terms of index mood*/

    }

    /**
     * alertDialog method for the comment day
     */
    public void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Commentaire");
        inputComment = new EditText(this);
        builder.setView(inputComment);
        /**Set positive Button*/
        builder.setPositiveButton("VALIDER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentMood.setCommentMood(inputComment.getText().toString());
                databaseManager.updateCommentMood(inputComment.getText().toString());
                databaseManager.close();

            }
        });
        /**Set negative Button*/
        builder.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        /**Create dialog*/
        final AlertDialog alertDialog = builder.create();
        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputComment.setText(currentMood.getCommentMood());
                alertDialog.show();
            }
        });
            }

    /**
     * Set Method for lauching mood
     */
    public void startMood() {
        lastMood=databaseManager.readLastMood();
        if (lastMood == null) {/** If is the first launch of application*/
            currentMood = new Mood(3, "", today);
            databaseManager.insertMood(currentMood.getIndex(),currentMood.getCommentMood(),currentMood.getDateMood());
            databaseManager.close();
        }
        else if(lastMood.getDateMood().equals(today)){/**If is not the first day launch*/
            currentMood=lastMood;
        }
        else{
            currentMood=new Mood(3,"",today);/**If is the first day launch*/
            databaseManager.insertMood(currentMood.getIndex(),currentMood.getCommentMood(),currentMood.getDateMood());
            databaseManager.close();
        }
        this.setImageSmileyAndBackground(layoutMain, imageSmiley, this, currentMood);

    }
    public void startStory(){/**Start Story Activity*/
        buttonStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent storyActivity= new Intent(MainActivity.this,StoryActivity.class);
                storyActivity.putExtra("width",layoutMain.getMeasuredWidth());/**Recover the appk display width for the story activity*/
                storyActivity.putExtra("height",layoutMain.getMeasuredHeight());/**Recover the appk display height for the story activity*/
                startActivity(storyActivity);
            }
        });
    }
    public void startShare(){/**Method for share mood by sms*/
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent= new Intent(Intent.ACTION_VIEW);
                shareIntent.setData(Uri.parse("sms:"));
                shareIntent.putExtra("sms_body",MainActivity.this.smsMood());
                startActivity(shareIntent);
            }
        });
    }
    public String smsMood(){/** Method for initialize the message shared*/
        int indexCurrentMood=currentMood.getIndex();
        String smsBody;
        switch(indexCurrentMood){
            case 0:
                smsBody="Salut, mon humeur du jour est triste.";
                break;
            case 1:
                smsBody="Salut, mon humeur du jour est maussade.";
                break;
            case 2:
                smsBody="Salut, mon humeur du jour est neutre.";
                break;
            case 3:
                smsBody="Salut, mon humeur du jour est joyeuse.";
                break;
            case 4:
                smsBody="Salut, mon humeur du jour est euphorique.";
                break;
            default:
                smsBody="Erreur";
        }
        return smsBody;
    }
    public static void setImageSmileyAndBackground(View view, ImageView imageMood, Context context, Mood mood){
        Resources res = context.getResources();
        int imageSmileyTab[]={R.drawable.smileysad,R.drawable.smileydisappointed,R.drawable.smileynormal,R.drawable.smileyhappy, R.drawable.smileysuperhappy};
        int  colorsViewTab[]={R.color.faded_red,R.color.warm_grey,R.color.cornflower_blue_65,R.color.light_sage,R.color.banana_yellow};
        int index = mood.getIndex();
        imageMood.setImageResource(imageSmileyTab[index]);
        view.setBackgroundColor(res.getColor(colorsViewTab[index]));
    }
}