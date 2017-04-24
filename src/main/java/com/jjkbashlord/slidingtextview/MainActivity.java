package com.jjkbashlord.slidingtextview;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    CustomImageButton bLeft, bRight;
    TextView textView0, textView1, textViewHelloWorld;
    // The entire constraint layout for MainActivity
    ConstraintLayout constraintLayout;
    // Layout params for the buttons
    ConstraintLayout.LayoutParams rightLayoutParam, leftLayoutParam;
    // Semaphore that determines which textview is in use
    int textViewSem;
    // Current index with regards to the list of texts for the textView
    int currIndex;
    // Total count of texts that can be paged through
    int textViewCount;
    // Width of the textView relative to the button controllers
    int textViewWidth;

    float width, height, dpx;
    public static final ArrayList<String> titles = new ArrayList<String>(Arrays.asList(new String[]{"zero", "one", "two", "three", "four"}));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize and tag the constraint layout for future edits.
        constraintLayout = new ConstraintLayout(this);
        constraintLayout = (ConstraintLayout)  findViewById(R.id.activity_main_constraintlayout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels; //displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        bLeft = (CustomImageButton) findViewById(R.id.leftButton);
        bRight = (CustomImageButton)  findViewById(R.id.rightButton);
        textView0 = (TextView) findViewById(R.id.textView0);
        textViewHelloWorld = (TextView)findViewById(R.id.helloworld); 
        // Setting width to Screen width - 2*buttonWidth (which is 50dp)
        textViewWidth = (int) (width-(2*Util.convertDpToPixel(50, this)));
        textView0.getLayoutParams().width = textViewWidth;
        textView0.requestLayout();

        textView1 = (TextView)findViewById(R.id.textView1);
        // Save pixel value of 50dp to px
        dpx = Util.convertDpToPixel(50, this);

        bLeft.setOnClickListener(this);
        bRight.setOnClickListener(this);

        textView0.setText( titles.get(currIndex) );

        rightLayoutParam = (ConstraintLayout.LayoutParams) bRight.getLayoutParams();
        leftLayoutParam = (ConstraintLayout.LayoutParams) bLeft.getLayoutParams();

        // Starting off the base case at index 0 and using textView0
        currIndex = textViewSem = 0;
        textViewCount = titles.size();
    }

    @Override
    public void onClick(View view) {
        int temp = -1;
        switch (view.getId()){
            case R.id.leftButton:
                if(currIndex > 0){
                    currIndex--;
                    buttonsOff();
                    pageLabel(0);
                }
                break;
            case R.id.rightButton:
                if(currIndex < textViewCount-1){
                    currIndex++;
                    buttonsOff();
                    pageLabel(1);
                }
                break;
        }
    }

    /**
     *  Depending on the variables flag and textViewSem, initiates the constraint changes/animations
     *      on textView0 and textView1.  textViewSem determines which textView is currently in use,
     *      wherein the unused textView will be set on the appropriate side depending on the button (left/right)
     *      and animated in (In this case, the used textView will shrink in width whilst the unused textView
     *      grows in width, encapsulating the area entirely).
     *
     * @param flag      Simple flag determining between the reaction to the left or right button click.
     *                  if(flag == 0) Left Button clicked
     *                  else          Right button clicked
     */
    public void pageLabel(final int flag){
        // flag == 0 Left Button pressed: textViews will slide from left to right ->>>
        // flag == 1 Right Button pressed: textViews will slide from right to left <<<-

        if(textViewSem == 0){

            if(flag == 0) {//  page left ->>>>>
                unbindConstraint(0);
                constraintCase(0);
            }else {// page from rightside <<<----
                unbindConstraint(1);
                constraintCase(1);

            }
            ViewGroup.LayoutParams lp = textView0.getLayoutParams();
            ResizeAnimation a = new ResizeAnimation(textView0);
            a.setDuration(500);
            a.setParams(lp.width,0);
            textView0.startAnimation(a);
            textView0.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ConstraintSet constraintSet1 = new ConstraintSet();
                    constraintSet1.clone(constraintLayout);
                    constraintSet1.clear(R.id.textView0);
                    textView0.setText("");
                    if(flag == 0)
                        constraintSet1.connect(R.id.textView1, ConstraintSet.RIGHT, R.id.rightButton, ConstraintSet.LEFT, 0);
                    else
                        constraintSet1.connect(R.id.textView1, ConstraintSet.LEFT, R.id.leftButton, ConstraintSet.RIGHT, 0);
                    constraintSet1.applyTo(constraintLayout);
                    textView1.getLayoutParams().width = textViewWidth;
                    textView1.requestLayout();
                    buttonsOn();
                }
            }, 500);
            textView1.setText(titles.get(currIndex));
            textViewSem = 1;
        }else{
            if(flag == 0) {//  page left
                unbindConstraint(0);
                constraintCase(2);
            }else {// page from rightside
                unbindConstraint(1);
                constraintCase(3);
            }

            ViewGroup.LayoutParams lp = textView1.getLayoutParams();
            ResizeAnimation a = new ResizeAnimation(textView1);
            a.setDuration(500);
            a.setParams(lp.width,0);
            textView1.startAnimation(a);
            textView1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ConstraintSet constraintSet1 = new ConstraintSet();
                    constraintSet1.clone(constraintLayout);
                    constraintSet1.clear(R.id.textView1);
                    textView1.setText("");
                    if(flag == 0)
                        constraintSet1.connect(R.id.textView0, ConstraintSet.RIGHT, R.id.rightButton, ConstraintSet.LEFT, 0);
                    else
                        constraintSet1.connect(R.id.textView0, ConstraintSet.LEFT, R.id.leftButton, ConstraintSet.RIGHT, 0);
                    constraintSet1.applyTo(constraintLayout);
                    textView0.getLayoutParams().width = textViewWidth;
                    textView0.requestLayout();
                    buttonsOn();
                }
            }, 500);

            textView0.setText(titles.get(currIndex));
            textViewSem = 0;
        }
    }

    /**
     *  Depending on flag, which acts as a case flag, removes/resets constraints on textViews
     *      in order to make new constraints on them for future animations/layouts.
     *
     * @param flag0      Simple flag determining between the reaction to the left or right button click.
     *
     *                  if(flag0 == 0):
     *                      textView's rightToLeft constraint needs to be reset
     *                  if(flag0 == 1):
     *                      textView's rleftToRight constraint needs to be reset
     */
    public void unbindConstraint(int flag0){
        // unbind constraints on the textView coming in
        if(textViewSem == 1){
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) textView0.getLayoutParams();
            layoutParams.rightToLeft = ConstraintLayout.LayoutParams.UNSET;
            layoutParams.leftToRight = ConstraintLayout.LayoutParams.UNSET;
            textView0.setLayoutParams(layoutParams);

            ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) textView1.getLayoutParams();
            if( flag0 == 0) layoutParams1.leftToRight = ConstraintLayout.LayoutParams.UNSET;
            else layoutParams1.rightToLeft = ConstraintLayout.LayoutParams.UNSET;
            textView1.setLayoutParams(layoutParams1);
        }else{
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) textView1.getLayoutParams();
            layoutParams.rightToLeft = ConstraintLayout.LayoutParams.UNSET;
            layoutParams.leftToRight = ConstraintLayout.LayoutParams.UNSET;
            textView1.setLayoutParams(layoutParams);

            ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) textView0.getLayoutParams();
            if( flag0 == 0) layoutParams1.leftToRight = ConstraintLayout.LayoutParams.UNSET;
            else layoutParams1.rightToLeft = ConstraintLayout.LayoutParams.UNSET;
            textView0.setLayoutParams(layoutParams1);
        }
    }

    /**
     *  Depending on flag, which acts as a case flag, sets up constraints on textView0 and textView1
     *      for the coming animation.
     *
     * @param flag      Simple flag determining between the reaction to the left or right button click.
     *
     *                  if(flag == 0):
     *                      textView1 constraints set on the leftside to replace textView0
     *                  if(flag == 1):
     *                      textView1 constraints set on the rightside to replace textView0

     *                  if(flag == 2):
     *                      textView0 constraints set on the leftside to replace textView1
     *                  if(flag == 3):
     *                      textView0 constraints set on the leftside to replace textView1
     */
    public void constraintCase(int flag){
        if(flag == 0) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            // textView1's top constraint set below helloworld's bottom
            constraintSet.connect(R.id.textView1, ConstraintSet.TOP, R.id.helloworld, ConstraintSet.BOTTOM, 0);
            // textView1's left constraint set to the right side of leftButton
            constraintSet.connect(R.id.textView1, ConstraintSet.LEFT, R.id.leftButton, ConstraintSet.RIGHT, 0);
            // textView1's right constraint set to the left side of textView0
            constraintSet.connect(R.id.textView1, ConstraintSet.RIGHT, R.id.textView0, ConstraintSet.LEFT, 0);
            // textView0's right side set to the left side of rightButton
            constraintSet.connect(R.id.textView0, ConstraintSet.RIGHT, R.id.rightButton, ConstraintSet.LEFT, 0);
            constraintSet.applyTo(constraintLayout);
        }else if(flag == 1) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

            constraintSet.connect(R.id.textView1, ConstraintSet.TOP, R.id.helloworld, ConstraintSet.BOTTOM, 0);
            constraintSet.connect(R.id.textView1, ConstraintSet.RIGHT, R.id.rightButton, ConstraintSet.LEFT, 0);
            constraintSet.connect(R.id.textView1, ConstraintSet.LEFT, R.id.textView0, ConstraintSet.RIGHT, 0);
            constraintSet.connect(R.id.textView0, ConstraintSet.LEFT, R.id.leftButton, ConstraintSet.RIGHT, 0);
            constraintSet.applyTo(constraintLayout);
        }else if(flag == 2) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

            constraintSet.connect(R.id.textView0, ConstraintSet.TOP,R.id.helloworld, ConstraintSet.BOTTOM, 0);
            constraintSet.connect(R.id.textView0, ConstraintSet.LEFT, R.id.leftButton, ConstraintSet.RIGHT, 0);
            constraintSet.connect(R.id.textView0, ConstraintSet.RIGHT, R.id.textView1, ConstraintSet.LEFT, 0);
            constraintSet.connect(R.id.textView1, ConstraintSet.RIGHT, R.id.rightButton, ConstraintSet.LEFT, 0);
            constraintSet.applyTo(constraintLayout);
        }else if(flag == 3) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

            constraintSet.connect(R.id.textView0, ConstraintSet.TOP,R.id.helloworld, ConstraintSet.BOTTOM, 0);
            constraintSet.connect(R.id.textView0, ConstraintSet.RIGHT, R.id.rightButton, ConstraintSet.LEFT, 0);
            constraintSet.connect(R.id.textView0, ConstraintSet.LEFT, R.id.textView1, ConstraintSet.RIGHT, 0);
            constraintSet.connect(R.id.textView1, ConstraintSet.LEFT, R.id.leftButton, ConstraintSet.RIGHT, 0);
            constraintSet.applyTo(constraintLayout);
        }
    }

    public void buttonsOff(){
        bLeft.toggleOff();
        bRight.toggleOff();
    }

    public void buttonsOn(){
        bLeft.toggleOn();
        bRight.toggleOn();
    }
}
