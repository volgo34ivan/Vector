package ivan.kravtsov.vector;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.view.View.OnTouchListener;
import android.widget.Toast;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Keyboard extends AppCompatActivity implements OnTouchListener {

    private static Context context;
    private SoundPool mSoundPool;
    private AssetManager mAssetManager;
    private int mButton; //не делать локальной переменной!!!
    private int mStreamID = 0;
    static public boolean file = false;

    Button button1,button2,button3,button4,button5,button6; //строка 1
    Button button7,button8,button9,button0, buttonpoint,buttoncaret; //строка 2
    Button buttonplus,buttonminus,buttonx,buttonslash,buttoncircle,buttoncolon; //строка 3
    Button buttonarrows,buttonangle,buttongraph,buttonfaq,buttonscobeleft,buttonscoberight; //строка 4
    Button buttonsemicolon,shiftleft,shiftright,buttonclear, buttondel,buttonequals; //строка 5
    Button memory_plus,memory_minus,memory_read,memory_clean,history,next; //строка 6
    Integer cursor = 0;
    public static TextView answer;
    public static EditText input;
    LinearLayout linearLayout;
    Drawable paintButton;

    //идентификатор действия над выражением
    static public int id = 0;
    //0 - выражение отсутствует
    //1 - сложение /вычитание
    //2 - разложение вектора на 2 ближайших вектора
    //3 - скалярное умножение
    //4 - векторное умножение
    //5 - смешанное умножение
    //6 - деление на скаляр
    //7 - длина вектора
    //8 - угол между осями 3-х мерного вектора от 0 до 90 градусов

    //размеры экрана
    public static int width;
    public static int height;

    //Текстовые элементы ввод/вывод/сообщение
    public static String ans = "";
    public static String expression = "";
    public String hint = "";
    public String error = "";
    public String hintMemory = "";
    public static String memory = "";
    Integer sizeBtnPx,sizeBtn,widthDP,heightDP;

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.calculator);

        context = this;

        Resources r = getResources();
        float OneDPtoPX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics()); //размерность одного DP в пикселях

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        //рекламный блок занимает 1/4
        Integer inputHeightPX = Math.round((height-width)*2/5);
        Integer inputHeightDP = Math.round(inputHeightPX/OneDPtoPX);
        Integer answerHeightPX = Math.round((height-width)*2/5);
        Integer answerHeightDP = Math.round(answerHeightPX/OneDPtoPX);

        Integer aroundBtn = Math.round(width * 0.005f);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            //Integer sizeBtnPx = (width-(aroundBtn*7))/6;//с отступом
            sizeBtnPx = width/6; //без отступа
            //Integer sizeBtn = Math.round(sizeBtnPx/OneDPtoPX); //размер кнопок в DP
            sizeBtn = sizeBtnPx; //размер кнопок в пикселях
            widthDP = Math.round(width/OneDPtoPX); //ширина экрана в DP
            heightDP = Math.round(height/OneDPtoPX); //высота экрана в DP
        }else{
            //Integer sizeBtnPx = (width-(aroundBtn*7))/6;//с отступом
            sizeBtnPx = width/12; //без отступа
            //Integer sizeBtn = Math.round(sizeBtnPx/OneDPtoPX); //размер кнопок в DP
            sizeBtn = sizeBtnPx; //размер кнопок в пикселях
            widthDP = Math.round(width/OneDPtoPX); //ширина экрана в DP
            heightDP = Math.round(height/OneDPtoPX); //высота экрана в DP
        }
        Log.d("PIXELS","Разрешение экрана по X = " + Integer.toString(width) + " PX");
        Log.d("PIXELS","Разрешение экрана по Y = " + Integer.toString(height) + " PX");
        Log.d("PIXELS","Разрешение экрана по X = " + Integer.toString(widthDP) + " DP");
        Log.d("PIXELS","Разрешение экрана по Y = " + Integer.toString(heightDP) + " DP");
        Log.d("PIXELS","Отступ между кнопками = " + Integer.toString(aroundBtn) + " PX");
        Log.d("PIXELS","Размер сторон кнопок = " + Integer.toString(sizeBtn) + " PX");

        hint = getResources().getString(R.string.answer_window_faq);
        hintMemory = getResources().getString(R.string.answer_window_faq_memory);
        error = getResources().getString(R.string.answer_window_error);

        linearLayout = (LinearLayout)findViewById(R.id.background);
        linearLayout.setBackground(Assets.getDrawableFromAssets(this,"background/background.jpg"));

        button1 = (Button)findViewById(R.id.button1);
        button1.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_1.png"));
        button1.setOnTouchListener(this);
        button1.getLayoutParams().width = sizeBtn;
        button1.getLayoutParams().height = sizeBtn;
        button1.setPadding(aroundBtn,aroundBtn,0,0);

        button2 = (Button)findViewById(R.id.button2);
        button2.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_2.png"));
        button2.setOnTouchListener(this);
        button2.getLayoutParams().width = sizeBtn;
        button2.getLayoutParams().height = sizeBtn;
        button2.setPadding(aroundBtn,aroundBtn,0,0);

        button3 = (Button)findViewById(R.id.button3);
        button3.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_3.png"));
        button3.setOnTouchListener(this);
        button3.getLayoutParams().width = sizeBtn;
        button3.getLayoutParams().height = sizeBtn;
        button3.setPadding(aroundBtn,aroundBtn,0,0);

        button4 = (Button)findViewById(R.id.button4);
        button4.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_4.png"));
        button4.setOnTouchListener(this);
        button4.getLayoutParams().width = sizeBtn;
        button4.getLayoutParams().height = sizeBtn;
        button4.setPadding(aroundBtn,aroundBtn,0,0);

        button5 = (Button)findViewById(R.id.button5);
        button5.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_5.png"));
        button5.setOnTouchListener(this);
        button5.getLayoutParams().width = sizeBtn;
        button5.getLayoutParams().height = sizeBtn;
        button5.setPadding(aroundBtn,aroundBtn,0,0);

        button6 = (Button)findViewById(R.id.button6);
        button6.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_6.png"));
        button6.setOnTouchListener(this);
        button6.getLayoutParams().width = sizeBtn;
        button6.getLayoutParams().height = sizeBtn;
        button6.setPadding(aroundBtn,aroundBtn,aroundBtn,0);

        button7 = (Button)findViewById(R.id.button7);
        button7.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_7.png"));
        button7.setOnTouchListener(this);
        button7.getLayoutParams().width = sizeBtn;
        button7.getLayoutParams().height = sizeBtn;
        button7.setPadding(aroundBtn,aroundBtn,0,0);

        button8 = (Button)findViewById(R.id.button8);
        button8.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_8.png"));
        button8.setOnTouchListener(this);
        button8.getLayoutParams().width = sizeBtn;
        button8.getLayoutParams().height = sizeBtn;
        button8.setPadding(aroundBtn,aroundBtn,0,0);

        button9 = (Button)findViewById(R.id.button9);
        button9.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_9.png"));
        button9.setOnTouchListener(this);
        button9.getLayoutParams().width = sizeBtn;
        button9.getLayoutParams().height = sizeBtn;
        button9.setPadding(aroundBtn,aroundBtn,0,0);

        button0 = (Button)findViewById(R.id.button0);
        button0.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_0.png"));
        button0.setOnTouchListener(this);
        button0.getLayoutParams().width = sizeBtn;
        button0.getLayoutParams().height = sizeBtn;
        button0.setPadding(aroundBtn,aroundBtn,0,0);

        buttonpoint = (Button)findViewById(R.id.buttonpoint);
        buttonpoint.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_point.png"));
        buttonpoint.setOnTouchListener(this);
        buttonpoint.getLayoutParams().width = sizeBtn;
        buttonpoint.getLayoutParams().height = sizeBtn;
        buttonpoint.setPadding(aroundBtn,aroundBtn,0,0);

        buttoncaret = (Button)findViewById(R.id.caret);
        buttoncaret.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_caret.png"));
        buttoncaret.setOnTouchListener(this);
        buttoncaret.getLayoutParams().width = sizeBtn;
        buttoncaret.getLayoutParams().height = sizeBtn;
        buttoncaret.setPadding(aroundBtn,aroundBtn,aroundBtn,0);

        buttonplus = (Button)findViewById(R.id.buttonplus);
        buttonplus.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_plus.png"));
        buttonplus.setOnTouchListener(this);
        buttonplus.getLayoutParams().width = sizeBtn;
        buttonplus.getLayoutParams().height = sizeBtn;
        buttonplus.setPadding(aroundBtn,aroundBtn,0,0);

        buttonminus = (Button)findViewById(R.id.buttonminus);
        buttonminus.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_minus.png"));
        buttonminus.setOnTouchListener(this);
        buttonminus.getLayoutParams().width = sizeBtn;
        buttonminus.getLayoutParams().height = sizeBtn;
        buttonminus.setPadding(aroundBtn,aroundBtn,0,0);

        buttonx = (Button)findViewById(R.id.buttonx);
        buttonx.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_x.png"));
        buttonx.setOnTouchListener(this);
        buttonx.getLayoutParams().width = sizeBtn;
        buttonx.getLayoutParams().height = sizeBtn;
        buttonx.setPadding(aroundBtn,aroundBtn,0,0);

        buttonslash = (Button)findViewById(R.id.buttonslash);
        buttonslash.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_slash.png"));
        buttonslash.setOnTouchListener(this);
        buttonslash.getLayoutParams().width = sizeBtn;
        buttonslash.getLayoutParams().height = sizeBtn;
        buttonslash.setPadding(aroundBtn,aroundBtn,0,0);

        buttoncircle = (Button)findViewById(R.id.buttoncircle);
        buttoncircle.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_circle.png"));
        buttoncircle.setOnTouchListener(this);
        buttoncircle.getLayoutParams().width = sizeBtn;
        buttoncircle.getLayoutParams().height = sizeBtn;
        buttoncircle.setPadding(aroundBtn,aroundBtn,0,0);

        buttoncolon = (Button)findViewById(R.id.buttoncolon);
        buttoncolon.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_colon.png"));
        buttoncolon.setOnTouchListener(this);
        buttoncolon.getLayoutParams().width = sizeBtn;
        buttoncolon.getLayoutParams().height = sizeBtn;
        buttoncolon.setPadding(aroundBtn,aroundBtn,aroundBtn,0);

        buttonarrows = (Button)findViewById(R.id.buttonarrows);
        buttonarrows.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_arrows.png"));
        buttonarrows.setOnTouchListener(this);
        buttonarrows.getLayoutParams().width = sizeBtn;
        buttonarrows.getLayoutParams().height = sizeBtn;
        buttonarrows.setPadding(aroundBtn,aroundBtn,0,0);

        buttonangle = (Button)findViewById(R.id.buttonangle);
        buttonangle .setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_angle.png"));
        buttonangle .setOnTouchListener(this);
        buttonangle.getLayoutParams().width = sizeBtn;
        buttonangle.getLayoutParams().height = sizeBtn;
        buttonangle.setPadding(aroundBtn,aroundBtn,0,0);

        buttongraph = (Button)findViewById(R.id.buttongraph);
        buttongraph .setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_graph.png"));
        buttongraph .setOnTouchListener(this);
        buttongraph.getLayoutParams().width = sizeBtn;
        buttongraph.getLayoutParams().height = sizeBtn;
        buttongraph.setPadding(aroundBtn,aroundBtn,0,0);

        buttonfaq = (Button)findViewById(R.id.buttonfaq);
        buttonfaq.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_faq.png"));
        buttonfaq.setOnTouchListener(this);
        buttonfaq.getLayoutParams().width = sizeBtn;
        buttonfaq.getLayoutParams().height = sizeBtn;
        buttonfaq.setPadding(aroundBtn,aroundBtn,0,0);

        buttonscobeleft = (Button)findViewById(R.id.buttonscobeleft);
        buttonscobeleft.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_scobe_left.png"));
        buttonscobeleft.setOnTouchListener(this);
        buttonscobeleft.getLayoutParams().width = sizeBtn;
        buttonscobeleft.getLayoutParams().height = sizeBtn;
        buttonscobeleft.setPadding(aroundBtn,aroundBtn,0,0);

        buttonscoberight = (Button)findViewById(R.id.buttonscoberight);
        buttonscoberight.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_scobe_right.png"));
        buttonscoberight.setOnTouchListener(this);
        buttonscoberight.getLayoutParams().width = sizeBtn;
        buttonscoberight.getLayoutParams().height = sizeBtn;
        buttonscoberight.setPadding(aroundBtn,aroundBtn,aroundBtn,0);

        buttonsemicolon = (Button)findViewById(R.id.buttonsemicolon);
        buttonsemicolon.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_semicolon.png"));
        buttonsemicolon.setOnTouchListener(this);
        buttonsemicolon.getLayoutParams().width = sizeBtn;
        buttonsemicolon.getLayoutParams().height = sizeBtn;
        buttonsemicolon.setPadding(aroundBtn,aroundBtn,0,0);

        shiftleft = (Button)findViewById(R.id.shiftleft);
        shiftleft.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_arrow_left.png"));
        shiftleft.setOnTouchListener(this);
        shiftleft.getLayoutParams().width = sizeBtn;
        shiftleft.getLayoutParams().height = sizeBtn;
        shiftleft.setPadding(aroundBtn,aroundBtn,0,0);

        shiftright = (Button)findViewById(R.id.shiftright);
        shiftright.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_arrow_right.png"));
        shiftright.setOnTouchListener(this);
        shiftright.getLayoutParams().width = sizeBtn;
        shiftright.getLayoutParams().height = sizeBtn;
        shiftright.setPadding(aroundBtn,aroundBtn,0,0);

        buttonclear = (Button)findViewById(R.id.clear);
        buttonclear.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_c.png"));
        buttonclear.setOnTouchListener(this);
        buttonclear.getLayoutParams().width = sizeBtn;
        buttonclear.getLayoutParams().height = sizeBtn;
        buttonclear.setPadding(aroundBtn,aroundBtn,0,0);

        buttondel = (Button)findViewById(R.id.del);
        buttondel.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_d.png"));
        buttondel.setOnTouchListener(this);
        buttondel.getLayoutParams().width = sizeBtn;
        buttondel.getLayoutParams().height = sizeBtn;
        buttondel.setPadding(aroundBtn,aroundBtn,0,0);

        buttonequals = (Button)findViewById(R.id.buttonanswer);
        buttonequals.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_equals.png"));
        buttonequals.setOnTouchListener(this);
        buttonequals.getLayoutParams().width = sizeBtn;
        buttonequals.getLayoutParams().height = sizeBtn;
        buttonequals.setPadding(aroundBtn,aroundBtn,aroundBtn,0);

        memory_plus = (Button)findViewById(R.id.memory_plus);
        memory_plus.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_memory_plus.png"));
        memory_plus.setOnTouchListener(this);
        memory_plus.getLayoutParams().width = sizeBtn;
        memory_plus.getLayoutParams().height = sizeBtn;
        memory_plus.setPadding(aroundBtn,aroundBtn,0,0);

        memory_minus = (Button)findViewById(R.id.memory_minus);
        memory_minus.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_memory_minus.png"));
        memory_minus.setOnTouchListener(this);
        memory_minus.getLayoutParams().width = sizeBtn;
        memory_minus.getLayoutParams().height = sizeBtn;
        memory_minus.setPadding(aroundBtn,aroundBtn,0,0);

        memory_read = (Button)findViewById(R.id.memory_read);
        memory_read.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_memory_read.png"));
        memory_read.setOnTouchListener(this);
        memory_read.getLayoutParams().width = sizeBtn;
        memory_read.getLayoutParams().height = sizeBtn;
        memory_read.setPadding(aroundBtn,aroundBtn,0,0);

        memory_clean = (Button)findViewById(R.id.memory_clean);
        memory_clean.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_memory_clean.png"));
        memory_clean.setOnTouchListener(this);
        memory_clean.getLayoutParams().width = sizeBtn;
        memory_clean.getLayoutParams().height = sizeBtn;
        memory_clean.setPadding(aroundBtn,aroundBtn,0,0);

        history = (Button)findViewById(R.id.history);
        history.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_history.png"));
        history.setOnTouchListener(this);
        history.getLayoutParams().width = sizeBtn;
        history.getLayoutParams().height = sizeBtn;
        history.setPadding(aroundBtn,aroundBtn,0,0);

        next = (Button)findViewById(R.id.next);
        next.setBackground(Assets.getDrawableFromAssets(this,"buttons/blue/button_next.png"));
        next.setOnTouchListener(this);
        next.getLayoutParams().width = sizeBtn;
        next.getLayoutParams().height = sizeBtn;
        next.setPadding(aroundBtn,aroundBtn,0,0);

        input = (EditText) findViewById(R.id.expression);
        input.setBackground(Assets.getDrawableFromAssets(this,"background/InputWindow.jpg"));
        input.setScroller(new Scroller(this));
        input.setVerticalScrollBarEnabled(true);
        input.setMovementMethod(new ScrollingMovementMethod());
        input.setOnTouchListener(this);
        input.getLayoutParams().width = width;
        input.setPadding(aroundBtn,aroundBtn,aroundBtn,aroundBtn);

        answer = (TextView)findViewById(R.id.answer);
        answer.setBackground(Assets.getDrawableFromAssets(this,"background/AnswerWindow.jpg"));
        answer.setMovementMethod(new ScrollingMovementMethod());
        answer.setText(hint);
        answer.getLayoutParams().width = width;
        answer.setPadding(aroundBtn,aroundBtn,aroundBtn,aroundBtn);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            if(Settings.ENABLE_ADVERTISING.equals("OFF") & Settings.ENABLE_ADVERTISING_TEST.equals("OFF") ){
                input.getLayoutParams().height = Math.round(3*(height-sizeBtn*3)/5);
                answer.getLayoutParams().height = Math.round(2*(height-sizeBtn*3)/5);
            }else{
                input.getLayoutParams().height = Math.round(2*(height-sizeBtn*3)/5);
                answer.getLayoutParams().height = Math.round(2*(height-sizeBtn*3)/5);
            }
        }else{
            if(Settings.ENABLE_ADVERTISING.equals("OFF") & Settings.ENABLE_ADVERTISING_TEST.equals("OFF")){
                input.getLayoutParams().height = Math.round(inputHeightPX*1.5f);
                answer.getLayoutParams().height = answerHeightPX;
            }else{
                input.getLayoutParams().height = inputHeightPX;
                answer.getLayoutParams().height = answerHeightPX;
            }
        }
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
                case MotionEvent.ACTION_UP:
                    //mStreamID = playSound(mButton);
                    if (mStreamID > 0){ mSoundPool.stop(mStreamID);}
                    //playSound(mButton);
                    view.performClick();
                    Integer cursorStartLog = input.getSelectionStart();
                    Integer cursorEndLog = input.getSelectionEnd();
                    Log.d("MyCursor","GetSelectionStart = " + cursorStartLog.toString());
                    Log.d("MyCursor","GetSelectionEnd = " + cursorEndLog.toString());
                    Log.d("MyCursor","MyCursor = " + cursor.toString());
                    cursor = cursorStartLog;
                    break;
                default:
                    break;
                case MotionEvent.ACTION_DOWN:
                    Keyboard(view);
                    break;
            }
        switch (view.getId()){
            case R.id.expression:
                disableSoftInputFromAppearing(input);
                input.onTouchEvent(motionEvent);
                break;
        }
            return true;
        }

    public static void disableSoftInputFromAppearing(EditText editText) {
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setTextIsSelectable(true);
    }
    public void Delayed(final Integer go, final View view){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (go){
                    case 1:
                        Painter(view);
                        break;
                    case 2:
                        //делаем что-то, если на входе подали 2
                        break;
                        //...3,4,5 копируем
                }
            }
        }, 100);
    }
    public void Keyboard(View view){
        if(input.getSelectionStart()<0){
            cursor = 0;
        }else{
            cursor = input.getSelectionStart();
        }
        switch (view.getId()) {
            case R.id.button1:
                ReplaceButton(view);
                expression = input.getText().insert(cursor, "1").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.button2:
                ReplaceButton(view);
                expression = input.getText().insert(cursor, "2").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.button3:
                ReplaceButton(view);
                expression = input.getText().insert(cursor, "3").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.button4:
                ReplaceButton(view);
                expression = input.getText().insert(cursor, "4").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.button5:
                ReplaceButton(view);
                expression = input.getText().insert(cursor, "5").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.button6:
                ReplaceButton(view);
                expression = input.getText().insert(cursor, "6").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.button7:
                ReplaceButton(view);
                expression = input.getText().insert(cursor, "7").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.button8:
                ReplaceButton(view);
                expression = input.getText().insert(cursor, "8").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.button9:
                ReplaceButton(view);
                expression = input.getText().insert(cursor, "9").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.button0:
                ReplaceButton(view);
                expression = input.getText().insert(cursor, "0").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.buttonpoint:
                ReplaceButton(view);
                expression = input.getText().insert(cursor, ".").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.caret:
                ReplaceButton(view);
                expression = input.getText().insert(cursor, "^").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.buttonplus:
                ReplaceButton(view);
                if (id == 0 & expression.length() > 1 | cursor < expression.length()) {
                    if (id != 5) {
                        id = 1;
                    }
                }
                expression = input.getText().insert(cursor, "+").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.buttonminus:
                ReplaceButton(view);
                if (id == 0 & expression.length() > 1 | cursor < expression.length()) {
                    if (id != 5) {
                        id = 1;
                    }
                }
                expression = input.getText().insert(cursor, "-").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.buttonx:
                ReplaceButton(view);
                if (id == 0 & expression.length() > 1 | cursor < expression.length()) {
                    if (id != 5) {
                        id = 4;
                    }
                }
                expression = input.getText().insert(cursor, "X").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.buttonslash:
                ReplaceButton(view);
                if (id == 0 & expression.length() > 1 | cursor < expression.length()) {
                    if (id != 5) {
                        id = 6;
                    }
                }
                expression = input.getText().insert(cursor, "/").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.buttoncircle:
                ReplaceButton(view);
                if (id == 0 & expression.length() > 1 | cursor < expression.length()) {
                    if (id != 5) {
                        id = 3;
                    }
                }
                expression = input.getText().insert(cursor, "*").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.buttoncolon:
                ReplaceButton(view);
                if (id == 0 & expression.length() > 1 | cursor < expression.length()) {
                    if (id != 5) {
                        id = 2;
                    }
                }
                expression = input.getText().insert(cursor, ":").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.buttonarrows:
                ReplaceButton(view);
                if (id == 0 & expression.length() > 1 | cursor < expression.length()| id == 5) {
                    id = 7;
                }
                expression = input.getText().insert(cursor, "dis").toString();
                input.setText(expression);
                cursor = cursor + 3;
                input.setSelection(cursor);
                break;
            case R.id.buttonangle:
                ReplaceButton(view);
                if (id == 0 & expression.length() > 1 | cursor < expression.length()| id != 5) {
                    id = 8;
                }
                expression = input.getText().insert(cursor, "angle(").toString();
                input.setText(expression);
                cursor = cursor + 6;
                input.setSelection(cursor);
                break;
            case R.id.buttongraph:
                ReplaceButton(view);
                Intent vectorGraph = new Intent(Keyboard.this, ActivityGraph.class);
                Keyboard.this.startActivity(vectorGraph);

                //Intent ListActivity = new Intent(Keyboard.this, HistoryActivity.class);
                //Keyboard.this.startActivity(ListActivity);

                //Intent vectorGraph = new Intent(Keyboard.this, OpenGLES20Activity.class);
                //Keyboard.this.startActivity(vectorGraph);
                break;
            case R.id.buttonfaq:
                ReplaceButton(view);
                Intent instruction = new Intent(Keyboard.this, InstructionActivity.class);
                Keyboard.this.startActivity(instruction);
                break;
            case R.id.buttonscobeleft:
                ReplaceButton(view);
                expression = input.getText().insert(cursor, "(").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.buttonscoberight:
                ReplaceButton(view);
                expression = input.getText().insert(cursor, ")").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.buttonsemicolon:
                ReplaceButton(view);
                if (id == 0 & expression.length() > 1 | cursor < expression.length()) {
                    if (id !=7 & id!=8) {
                        id = 5;
                    }
                }
                expression = input.getText().insert(cursor, ";").toString();
                input.setText(expression);
                cursor++;
                input.setSelection(cursor);
                break;
            case R.id.shiftleft:
                ReplaceButton(view);
                if (cursor > 0) {
                    cursor = cursor - 1;
                }
                input.setSelection(cursor);
                break;
            case R.id.shiftright:
                ReplaceButton(view);
                if (input.getText().length() > cursor) {
                    cursor = cursor + 1;
                }
                input.setSelection(cursor);
                break;
            case R.id.clear:
                ReplaceButton(view);
                id = 0;
                input.setText(null);
                cursor = 0;
                answer.setText(null);
                ans = null;
                expression = "";
                answer.setText(hint);
                VectorView.maxValue = 0.0;
                PlusMinus.vectors.clear();
                Decomposition.vectorsDecomposition.clear();
                VectorView.vectorsView.clear();
                break;
            case R.id.del:
                ReplaceButton(view);
                if (cursor > 0) {
                    input.setSelection(cursor);
                    String text = input.getText().delete(cursor - 1, cursor).toString();
                    input.setText(text);
                    cursor--;
                    input.setSelection(cursor);
                    expression = text;
                }
                break;
            case R.id.memory_plus:
                ReplaceButton(view);
                try{
                    if(CheckExpressionMemory(answer.getText().toString()).equals("Vector")){
                        Memory(answer.getText().toString(),"M+","Vector");
                    }else{
                        Memory(answer.getText().toString(),"M+","Scalar");
                    }
                }catch (NumberFormatException e){
                    answer.setText(error);
                    //Toast.makeText(getApplicationContext(), "Проверьте содержимое памяти.",
                    //        Toast.LENGTH_SHORT).show();
                }
                catch (IndexOutOfBoundsException e){
                   answer.setText(error);
                   //Toast.makeText(getApplicationContext(), "Не могу записать в память.",
                   //        Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.memory_minus:
                ReplaceButton(view);
                try{
                    String CheckedExpression = CheckExpressionMemory(answer.getText().toString());
                    if(CheckedExpression.equals("Vector")){
                        Memory(answer.getText().toString(),"M-","Vector");
                    }else{
                        Memory(answer.getText().toString(),"M-","Scalar");
                    }
                }catch (NumberFormatException e){
                    answer.setText(error);
                    //Toast.makeText(getApplicationContext(), "Проверьте содержимое памяти.",
                     //       Toast.LENGTH_SHORT).show();
                }catch (IndexOutOfBoundsException e){
                    answer.setText(error);
                 //   Toast.makeText(getApplicationContext(), "Не могу записать в память.",
                 //            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.memory_read:
                ReplaceButton(view);
                if(memory.equals("")){
                    answer.setText(hintMemory);
                }else{
                    answer.setText(memory);
                }
                break;
            case R.id.memory_clean:
                ReplaceButton(view);
                memory = "";
                break;
            case R.id.history:
                ReplaceButton(view);
                Intent history = new Intent(Keyboard.this, HistoryActivity.class);
                Keyboard.this.startActivity(history);
                //Toast.makeText(getApplicationContext(), "Функция в разработке.",
                //        Toast.LENGTH_SHORT).show();
                break;
            case R.id.next:
                ReplaceButton(view);
                //Toast.makeText(getApplicationContext(), "Функция в разработке.",
                //       Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonanswer:
                ReplaceButton(view);
                try {
                    answer.setText(null);
                    if (id == 3 | id == 7 | id == 6) {
                        id = CheckExpression(expression);
                    }
                    //тут будут вызываться классы функций +,-,* и т.п.
                    //Log.d("ListFragment","В keyboard скопирован ID: " + id);
                    expression = input.getText().toString();
                    switch (id) {
                        case 0:
                            if (input.getText().length() > 0) {
                                answer.setText(input.getText());
                            } else {
                                //Toast.makeText(getApplicationContext(), "Данные отсутствуют.",
                                //        Toast.LENGTH_SHORT).show();
                                answer.setText(hint);
                            }
                            break;
                        case 1:
                            Log.d("ListFragment","В рассчет отправлено выражение:  " + expression);
                            ans = PlusMinus.Calculate(expression);
                            break;
                        case 2:
                            ans = Decomposition.Calculate(expression);
                            break;
                        case 3:
                            ans = Scalarnoe.Calculate(expression);
                            break;
                        case 4:
                            ans = Vectornoe.Calculate(expression);
                            break;
                        case 5:
                            ans = MixedMultiply.Calculate(expression);
                            break;
                        case 6:
                            ans = MultiDevideOnNumber.Calculate(expression);
                            break;
                        case 7:
                            ans = Distance.Calculate(expression);
                            break;
                        case 8:
                            ans = FindAngle.Calculate(expression);
                            break;
                    }
                    if (id != 0) {
                        answer.setText(ans);

                        Date date= new Date(java.lang.System.currentTimeMillis());
                        Date time = Calendar.getInstance().getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                        SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
                        String created = sdf.format(date) + " " + sdftime.format(time);

                        writeFile(created);
                        writeFile(expression);
                        writeFile(ans);
                        writeFile(Integer.toString(id));
                    }
                } catch (NumberFormatException e) {
                    answer.setText(error);
                    //Toast.makeText(getApplicationContext(), "Неверный числовой формат.",
                    //        Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    //Toast.makeText(getApplicationContext(), "Данные отсутствуют.",
                    //        Toast.LENGTH_SHORT).show();
                    answer.setText(error);
                } catch (ArrayIndexOutOfBoundsException e) {
                    //Toast.makeText(getApplicationContext(), "Недостаточно данных.",
                    //        Toast.LENGTH_SHORT).show();
                    answer.setText(error);
                }catch (RuntimeException e){
                    //Toast.makeText(getApplicationContext(), "Мало данных.",
                    //        Toast.LENGTH_SHORT).show();
                    answer.setText(error);
                }finally {
                    Log.d("OPA","ID сейчас установлен в " + id);
                }
                break;
        }
        Delayed(1, view);
    }
    public int CheckExpression(String exp){
        int n = id;
        int find = 0;
        String[] opersplit;
        char[] subopersplit;

        if(id ==3 | id == 6){
            opersplit = exp.split("[*/]");
            for(int i=0; i<opersplit.length; i++){
                subopersplit = opersplit[1].toCharArray();
                for (char aSubopersplit : subopersplit) {
                    if(Character.toString(aSubopersplit).equals("^")){
                        n = 3;
                        find = 1;
                    }else if(find == 0){ n = 6;}
                }
            }
        }
        if(id == 7){
            opersplit = exp.split("[)]");
            if(opersplit.length > 2){
                n = 5;
            }
        }
        return n;
    }
    public String CheckExpressionMemory(String exp){
        String n = "Scalar";

        char[] subopersplit = exp.toCharArray();
                for (char aSubopersplit : subopersplit) {
                    if(Character.toString(aSubopersplit).equals("^")){
                        n = "Vector";
                    }
            }
        return n;
    }
    //замена запятой на точку
    public String RefactorMemory(String exp){
        StringBuilder n = new StringBuilder();
        String g = ".";
        char c = g.charAt(0);
        char[] subopersplit = exp.toCharArray();
        for (int i = 0; i < subopersplit.length; i++) {
            if(Character.toString(subopersplit[i]).equals(",")){
                subopersplit[i] = c;
            }
            n.append(Character.toString(subopersplit[i]));
        }
        return n.toString();
    }
    public void Painter(View view){
        switch (view.getId()){
            case R.id.button1:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_1.png");
                button1.setBackground(paintButton);
                break;
            case R.id.button2:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_2.png");
                button2.setBackground(paintButton);
                break;
            case R.id.button3:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_3.png");
                button3.setBackground(paintButton);
                break;
            case R.id.button4:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_4.png");
                button4.setBackground(paintButton);
                break;
            case R.id.button5:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_5.png");
                button5.setBackground(paintButton);
                break;
            case R.id.button6:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_6.png");
                button6.setBackground(paintButton);
                break;
            case R.id.button7:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_7.png");
                button7.setBackground(paintButton);
                break;
            case R.id.button8:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_8.png");
                button8.setBackground(paintButton);
                break;
            case R.id.button9:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_9.png");
                button9.setBackground(paintButton);
                break;
            case R.id.button0:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_0.png");
                button0.setBackground(paintButton);
                break;
            case R.id.buttonpoint:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_point.png");
                buttonpoint.setBackground(paintButton);
                break;
            case R.id.caret:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_caret.png");
                buttoncaret.setBackground(paintButton);
                break;
            case R.id.buttonplus:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_plus.png");
                buttonplus.setBackground(paintButton);
                break;
            case R.id.buttonminus:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_minus.png");
                buttonminus.setBackground(paintButton);
                break;
            case R.id.buttonx:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_x.png");
                buttonx.setBackground(paintButton);
                break;
            case R.id.buttonslash:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_slash.png");
                buttonslash.setBackground(paintButton);
                break;
            case R.id.buttoncircle:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_circle.png");
                buttoncircle.setBackground(paintButton);
                break;
            case R.id.buttoncolon:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_colon.png");
                buttoncolon.setBackground(paintButton);
                break;
            case R.id.buttonarrows:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_arrows.png");
                buttonarrows.setBackground(paintButton);
                break;
            case R.id.buttonangle:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_angle.png");
                buttonangle.setBackground(paintButton);
                break;
            case R.id.buttongraph:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_graph.png");
                buttongraph.setBackground(paintButton);
                break;
            case R.id.buttonfaq:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_faq.png");
                buttonfaq.setBackground(paintButton);
                break;
            case R.id.buttonscobeleft:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_scobe_left.png");
                buttonscobeleft.setBackground(paintButton);
                break;
            case R.id.buttonscoberight:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_scobe_right.png");
                buttonscoberight.setBackground(paintButton);
                break;
            case R.id.buttonsemicolon:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_semicolon.png");
                buttonsemicolon.setBackground(paintButton);
                break;
            case R.id.shiftleft:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_arrow_left.png");
                shiftleft.setBackground(paintButton);
                break;
            case R.id.shiftright:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_arrow_right.png");
                shiftright.setBackground(paintButton);
                break;
            case R.id.clear:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_c.png");
                buttonclear.setBackground(paintButton);
                break;
            case R.id.del:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_d.png");
                buttondel.setBackground(paintButton);
                break;
            case R.id.buttonanswer:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_equals.png");
                buttonequals.setBackground(paintButton);
                break;
            case R.id.memory_plus:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_memory_plus.png");
                memory_plus.setBackground(paintButton);
                break;
            case R.id.memory_minus:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_memory_minus.png");
                memory_minus.setBackground(paintButton);
                break;
            case R.id.memory_read:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_memory_read.png");
                memory_read.setBackground(paintButton);
                break;
            case R.id.memory_clean:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_memory_clean.png");
                memory_clean.setBackground(paintButton);
                break;
            case R.id.history:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_history.png");
                history.setBackground(paintButton);
                break;
            case R.id.next:
                paintButton = Assets.getDrawableFromAssets(this,"buttons/blue/button_next.png");
                next.setBackground(paintButton);
                break;
        }
    }
    public void ReplaceButton(View view){
        switch (view.getId()){
            case R.id.button1:
                button1.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_1.png"));
                break;
            case R.id.button2:
                button2.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_2.png"));
                break;
            case R.id.button3:
                button3.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_3.png"));
                break;
            case R.id.button4:
                button4.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_4.png"));
                break;
            case R.id.button5:
                button5.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_5.png"));
                break;
            case R.id.button6:
                button6.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_6.png"));
                break;
            case R.id.button7:
                button7.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_7.png"));
                break;
            case R.id.button8:
                button8.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_8.png"));
                break;
            case R.id.button9:
                button9.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_9.png"));
                break;
            case R.id.button0:
                button0.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_0.png"));
                break;
            case R.id.buttonpoint:
                buttonpoint.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_point.png"));
                break;
            case R.id.caret:
                buttoncaret.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_caret.png"));
                break;
            case R.id.buttonplus:
                buttonplus.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_plus.png"));
                break;
            case R.id.buttonminus:
                buttonminus.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_minus.png"));
                break;
            case R.id.buttonx:
                buttonx.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_x.png"));
                break;
            case R.id.buttonslash:
                buttonslash.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_slash.png"));
                break;
            case R.id.buttoncircle:
                buttoncircle.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_circle.png"));
                break;
            case R.id.buttoncolon:
                buttoncolon.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_colon.png"));
                break;
            case R.id.buttonarrows:
                buttonarrows.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_arrows.png"));
                break;
            case R.id.buttonangle:
                buttonangle.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_angle.png"));
                break;
            case R.id.buttongraph:
                buttongraph.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_graph.png"));
                break;
            case R.id.buttonfaq:
                buttonfaq.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_faq.png"));
                break;
            case R.id.buttonscobeleft:
                buttonscobeleft.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_scobe_left.png"));
                break;
            case R.id.buttonscoberight:
                buttonscoberight.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_scobe_right.png"));
                break;
            case R.id.buttonsemicolon:
                buttonsemicolon.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_semicolon.png"));
                break;
            case R.id.shiftleft:
                shiftleft.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_arrow_left.png"));
                break;
            case R.id.shiftright:
                shiftright.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_arrow_right.png"));
                break;
            case R.id.clear:
                buttonclear.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_c.png"));
                break;
            case R.id.del:
                buttondel.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_d.png"));
                break;
            case R.id.buttonanswer:
                buttonequals.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_equals.png"));
                break;
            case R.id.memory_plus:
                memory_plus.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_memory_plus.png"));
                break;
            case R.id.memory_minus:
                memory_minus.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_memory_minus.png"));
                break;
            case R.id.memory_read:
                memory_read.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_memory_read.png"));
                break;
            case R.id.memory_clean:
                memory_clean.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_memory_clean.png"));
                break;
            case R.id.history:
                history.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_history.png"));
                break;
            case R.id.next:
                next.setBackground(Assets.getDrawableFromAssets(this,"buttons/green/button_next.png"));
                break;
        }
    }
    public String Memory(String input, String button, String type){
        switch(button){
            case "M+":
                if(memory.equals("")){
                    memory = input;
                }else{
                    if(type.equals("Scalar")){
                        Double _memory = Double.parseDouble(memory) + Double.parseDouble(input);
                        memory = _memory.toString();
                    }
                    if(type.equals("Vector")){
                        Log.d("CheckExpress", memory + "+" + input);
                        memory = PlusMinus.Calculate(memory + "+" + input);
                        memory = RefactorMemory(memory);
                    }
                }
                break;
            case "M-":
                if(memory.equals("")){
                    memory = input;
                }else{
                    if(type.equals("Scalar")){
                        Double _memory = Double.parseDouble(memory) - Double.parseDouble(input);
                        memory = _memory.toString();
                    }
                    if(type.equals("Vector")){
                        Log.d("CheckExpress", memory + "+" + input);
                        memory = PlusMinus.Calculate(memory + "-" + input);
                        memory = RefactorMemory(memory);
                    }
                }
                break;
            case "MR":
                Log.d("CheckExpress", memory + "+" + input);
                answer.setText(memory);
                break;
            case "MC":
                memory = "";
                break;
        }
        return memory;
    }
    public void generateNoteOnSD(Context context, String sFileName) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Vectors");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Создано на SD карте.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            generateNoteOnInternalStorage(this,"history_vectors.txt");
        }
    }
    public void generateNoteOnInternalStorage(Context context, String sFileName) {
        try {
            File root = new File(context.getFilesDir(),"history_vectors.txt");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Создано на внутренней памяти.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writieFileOnInternalStorage(Context context, String sBody){
        String yourFilePath = context.getFilesDir() + "history_vectors.txt";
        try{
            File yourFile = new File( yourFilePath );
            FileWriter writer = new FileWriter(yourFile);
            writer.append(sBody);
            writer.flush();
            writer.close();

        }catch (Exception e){
            e.printStackTrace();
            Log.d("ReadLine","Файл не найден при записи.");
        }
    }
    public void writeFile(String mValue) {
        try {
            String filename = context.getFilesDir() + "history_vectors.txt";
            Log.d("PathFile",filename);
            FileWriter fw = new FileWriter(filename, true);
            fw.write(mValue + "\n");
            fw.close();
        } catch (IOException ioe) {
            writieFileOnInternalStorage(this,mValue);
        }
    }
    public static void clearFile() {
        try {
            String filename= context.getFilesDir() + "history_vectors.txt";
            PrintWriter writer = new PrintWriter(filename);
            writer.write("");
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }
    @SuppressWarnings("deprecation")
    private void createOldSoundPool() {
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
    }
    private int playSound(int sound) {
        if (sound > 0) {
            mStreamID = mSoundPool.play(sound, 1, 1, 1, 0, 1);
        }
        return mStreamID;
    }
    private int loadSound(String fileName) {
        AssetFileDescriptor afd;
        try {
            afd = mAssetManager.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Не могу загрузить файл " + fileName,
                    Toast.LENGTH_SHORT).show();
            return -1;
        }
        return mSoundPool.load(afd, 1);
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // Для устройств до Android 5
            createOldSoundPool();
        } else {
            // Для новых устройств
            createNewSoundPool();
        }

        mAssetManager = getAssets();

        // получим идентификаторы
        mButton= loadSound("sounds/u_click.ogg");
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSoundPool.release();
        mSoundPool = null;
    }
}
