package com.example.moodtracker.Manager;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.moodtracker.Controller.MainActivity;

public class SwipeDetector extends GestureDetector {

    public SwipeDetector(final MainActivity context){/** Swipe detector method extends GestureDetector class*/
        super(context,new GestureDetector.SimpleOnGestureListener() {

            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.i("DEBUG", e1 + " - " + e2);
                float deltaX = e2.getX() - e1.getX();
                float deltaY = e2.getY() - e1.getY();
                if (Math.abs(deltaX) > Math.abs(deltaY)) {/**If X>Y = movement is mainly horizontal*/
                    if (deltaX > 0) {
                        context.onSwipe(SwipeDirection.LEFT_TO_RIGHT);/**If X>0 = movement is Left to Right*/
                    } else
                        context.onSwipe(SwipeDirection.RIGHT_TO_LEFT);/**If X<0 = movement is Right to Left*/

                }
                else {
                    if (deltaY > 0) {/**If Y>X = movement is mainly vertical*/
                        context.onSwipe(SwipeDirection.TOP_TO_BOTTOM);/**If Y>0 = movement is Top to Bottom*/
                    } else
                        context.onSwipe(SwipeDirection.BOTTOM_TO_TOP);/**If Y<0 = movement is Bottom to Top*/
                }
                return false;
            }



        });
    }



    public static enum SwipeDirection{
        LEFT_TO_RIGHT, RIGHT_TO_LEFT, TOP_TO_BOTTOM, BOTTOM_TO_TOP
    }
}
