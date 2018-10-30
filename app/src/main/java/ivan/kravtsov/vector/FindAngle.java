package ivan.kravtsov.vector;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static java.lang.Math.*;

public class FindAngle {
    static ArrayList<Vector> vectors = new ArrayList<>();
    static final String LOG = "AngleFind";
    static String multAnswer;
    static Double PI = 4 * atan(1);
    static DecimalFormat df = new DecimalFormat("#.##");


    static String Calculate(String exp){
        vectors.clear();
        Log.d(LOG, "Выражение на входе ="+ exp);
        String[] splitter;
        String[] subsplitter;
        splitter = exp.split("[)]");
        for (int i = 0; i<splitter.length; i++) {
            subsplitter = splitter[i].split("[(;)]");
            Double x = Double.parseDouble(subsplitter[1]);
            Double y = Double.parseDouble(subsplitter[2]);
            Double z = Double.parseDouble(subsplitter[3]);
            Vector vector = new Vector(x,y,z);
            vectors.add(vector);
            Log.d(LOG, "Вектор "+ i + " = " + splitter[i]);
            for(int b= 0; b< subsplitter.length; b++){
                Log.d(LOG, "Значение в ячейке "+ b + " = " + subsplitter[b]);
            }
        }

        Log.d(LOG,"Размер массива " + vectors.size());
        Log.d(LOG,"ID вектора 1 " + vectors.get(0).id);
        //Log.d(LOG,"ID вектора 2 " + vectors.get(1).idString);
        //Log.d(LOG,"ID вектора 3 " + vectors.get(2).idString);

        Double x1 = vectors.get(0).getX();
        Double y1 = vectors.get(0).getY();
        Double z1 = vectors.get(0).getZ();

        //Double x2 = vectors.get(1).getX();
        //Double y2 = vectors.get(1).getY();
        //Double z2 = vectors.get(1).getZ();

        //Double x3 = vectors.get(2).getX();
        //Double y3 = vectors.get(2).getY();
        //Double z3 = vectors.get(2).getZ();

        Log.d(LOG, "Переменные = " + x1 +";"+ y1 +";"+ z1);
        //Log.d(LOG, "Переменные = " + x2 +";"+ y2 +";"+ z2);
        //Log.d(LOG, "Переменные = " + x3 +";"+ y3 +";"+ z3);

        //Нормализация вектора
        Double LengthVector = sqrt(pow(x1,2)+pow(y1,2)+pow(z1,2));
        Double Nx1 = x1/LengthVector;
        Double Ny1 = y1/LengthVector;
        Double Nz1 = z1/LengthVector;
        Double angleOX,angleOY,angleOZ;
        if(Nx1<0){Nx1*=-1;}
        if(Ny1<0){Ny1*=-1;}
        if(Nz1<0){Nz1*=-1;}
        //находим угол OX
            angleOX = acos(Nx1)/(PI/180);
        //находим угол OY
            angleOY = acos(Ny1)/(PI/180);
        //находим угол OZ
            angleOZ = acos(Nz1)/(PI/180);

        multAnswer = "OX(" + df.format(angleOX) + ")" + "OY(" + df.format(angleOY) + ")" + "OZ(" + df.format(angleOZ) + ")" ;
        Log.d(LOG, "Ответ = " + multAnswer);

        return multAnswer;
    }
}
