package ivan.kravtsov.vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class VectorView extends View {
    public static ArrayList<Vector> vectorsView = new ArrayList<>();
    //Константа для перевода в градусов в радианы
    private static final double RAD = Math.PI / 180;
    private Paint paint = new Paint();
    private static Bitmap bitmap;
    private int d = 0;
    private float dimension = 0f;
    private static  int v = 0;
    private static Double maxVector;
    private static float k;
    private static float vol = 0.8f;
    private static int sm= 10;
    private static float ts = 20.0f;

    //данные для рисования вектора

    static public float amplitude = 0f;
    static public float angle = 0f;
    static public String vectorInfo;
    static public Double ampInfo;
    static public Double angleInfo;
    static public String ampText;
    static public String angleText;
    static public Double maxValue;

    DecimalFormat df = new DecimalFormat("#.##");

    public VectorView(Context context){
        super(context);
    }

    public VectorView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public VectorView(Context context, AttributeSet ats, int defaultStyle){
        super(context,ats,defaultStyle);
    }

    private String convertToFormat(double value){

        return df.format(value);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        //Расчеты с размером экрана телефона
        //производятся сдесь

        //Получаем инфрмацию о размере дисплея телефона
        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);

        //получаем минимальную из сторон дисплея телефона
        d = Math.min(measuredWidth,measuredHeight);

        //устанавливаем размеры холста Canvas
        setMeasuredDimension(d, d);
    }

    @Override
    public void onDraw(Canvas canvas){
        maxValue = 0.0;
        //фоновый цвет представления
        bitmap = ActivityGraph.canvasBackground;
        int widthCanvas = canvas.getWidth();
        dimension = (float) d;

        @SuppressLint("DrawAllocation")
        Rect rectangle = new Rect(0,0,(int)dimension,(int)dimension);
        canvas.drawBitmap(bitmap, null, rectangle, null);

        paint.setStrokeWidth(5);
        paint.setTextSize(ts);

        //поиск в массиве вектора с максимальной амплитудой
        for(int i = 0; i< vectorsView.size(); i++){
            Double value = vectorsView.get(i).getAmplitude();
            if(value > maxValue){
                maxValue = value;
            }
            maxVector = maxValue;
            k = (float)((widthCanvas/maxVector) * vol);
        }

        for(int i = 0; i< vectorsView.size(); i++){
            ampInfo = vectorsView.get(i).getAmplitude();
            angleInfo = vectorsView.get(i).getAngleDEG();
            switch (Keyboard.id){
                case 1:
                    if(PlusMinus.amp.equals(ampInfo)){
                        paint.setColor(Color.GREEN);
                    }else{paint.setColor(Color.WHITE);}
                    break;
                case 2:
                    if(Decomposition.vectorsDecomposition.get(0).getAmplitude().equals(ampInfo)){
                        paint.setColor(Color.GREEN);
                    }else{paint.setColor(Color.WHITE);}
                    break;
                case 3:
                        paint.setColor(Color.WHITE);
                    break;
                case 4:
                    paint.setColor(Color.WHITE);
                    break;
            }

            ampText = convertToFormat(ampInfo);
            angleText = convertToFormat(angleInfo);

            vectorInfo = ampText + "^" + angleText;

            angle = Float.parseFloat(vectorsView.get(i).getAngleDEG().toString());
            amplitude = Float.parseFloat(vectorsView.get(i).getAmplitude().toString())*k;
            //Здесь тестируется алгоритм поворота вектора вокруг мнимого центра
            //на любой угол angle
            //можем задавать длину вектора в пикселях amplitude.
            //Исправляем вектор, если пользователь ввел фазу больше 360 градусов
            if (angle > 360) {
                int a = (int) angle / 360 ;
                int b = a * 360;
                if (a == 0){
                    angle -= 360;
                } else {
                    angle -= 360 * b;
                }
            }
            //Свитчер выбора квадранта.
            //где cos и sin принимают знак " - " был применен abs
            //Зависит от вводимой фазы (угла вектора angle)
            //Условыне операторы выбора квадранта построения вектора v.
            if (angle >= 0 && angle <= 90){ v = 1;}
            if (angle > 90 && angle <= 180){ v = 2;}
            if (angle > 180 && angle <= 270) {v = 3;}
            if (angle > 270 && angle <= 360) {v = 4;}

            //Свитчер выбора квадранта.
            //где cos и sin принимают знак " - " был применен abs
            //Зависит от вводимой фазы (угла вектора angle)
            switch (v){
                //Квадрант 1. От 0 градусов до 90 градусов
                case 1:
                    float sin1 = (float) Math.sin(angle * RAD);
                    float cos1 = (float) Math.cos(angle * RAD);
                    float ox1 = (amplitude *cos1/2) + (dimension/2);
                    float oy1 = -(amplitude * sin1/2)+(dimension/2);
                    if(ox1 != 0 && oy1 != 0){
                        canvas.drawLine(dimension/2,dimension/2,ox1,oy1,paint);
                        canvas.drawCircle(ox1,oy1,dimension*0.01f,paint);
                        canvas.drawText(vectorInfo,ox1+sm,oy1-sm,paint);
                    }
                    break;
                //Квадрант 2. От 90 градусов до 180 градусов
                case 2:
                    float sin2 = (float) Math.sin(angle * RAD);
                    float cos2 = (float) Math.abs(Math.cos(angle * RAD));
                    float ox2 = -(amplitude *cos2/2) + (dimension/2);
                    float oy2 = -(amplitude * sin2/2)+(dimension/2);
                    if(ox2 != 0 && oy2 != 0){
                        canvas.drawLine(dimension/2,dimension/2,ox2,oy2,paint);
                        canvas.drawCircle(ox2,oy2,dimension*0.01f,paint);
                        canvas.drawText(vectorInfo,ox2-sm,oy2-sm,paint);
                    }
                    break;
                //Квадрант 3. От 180 градусов до 270 градусов
                case 3:
                    float sin3 = (float) Math.abs(Math.sin(angle * RAD));
                    float cos3 = (float) Math.abs(Math.cos(angle * RAD));
                    float ox3 = -(amplitude *cos3/2) + (dimension/2);
                    float oy3 = (amplitude * sin3/2) + (dimension/2);
                    if(ox3 != 0 && oy3 != 0){
                        canvas.drawLine(dimension/2,dimension/2,ox3,oy3,paint);
                        canvas.drawCircle(ox3,oy3,dimension*0.01f,paint);
                        canvas.drawText(vectorInfo,ox3-sm*2,oy3+sm*2,paint);
                    }
                    break;
                //Квадрант 4. От 270 градусов до 360 градусов
                case 4:
                    float sin4 = (float) Math.abs(Math.sin(angle * RAD));
                    float cos4 = (float) Math.cos(angle * RAD);
                    float ox4 = (amplitude *cos4/2) + (dimension/2);
                    float oy4 = (amplitude * sin4/2) + (dimension/2);
                    if(ox4 != 0 && oy4 != 0){
                        canvas.drawLine(dimension/2,dimension/2,ox4,oy4,paint);
                        canvas.drawCircle(ox4,oy4,dimension*0.01f,paint);
                        canvas.drawText(vectorInfo,ox4+sm,oy4+sm,paint);
                    }
                    break;

            }
        }
    }

    private int measure(int measureSpec){
        int result = 0;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode == MeasureSpec.UNSPECIFIED){
            result = 200;
        } else {
            result = specSize;
        }
        return result;
    }
}
