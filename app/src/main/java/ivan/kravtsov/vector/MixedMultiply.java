package ivan.kravtsov.vector;

import android.util.Log;

import java.util.ArrayList;

public class MixedMultiply{

    static ArrayList<Vector> vectors = new ArrayList<>();
    static final String LOG = "Multiple";
    static Double multAnswer;


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
            subsplitter = null;
        }

            Log.d(LOG,"Размер массива " + vectors.size());
            Log.d(LOG,"ID вектора 1 " + vectors.get(0).id);
            Log.d(LOG,"ID вектора 2 " + vectors.get(1).id);
            Log.d(LOG,"ID вектора 3 " + vectors.get(2).id);

            Double x1 = vectors.get(0).getX();
            Double y1 = vectors.get(0).getY();
            Double z1 = vectors.get(0).getZ();

            Double x2 = vectors.get(1).getX();
            Double y2 = vectors.get(1).getY();
            Double z2 = vectors.get(1).getZ();

            Double x3 = vectors.get(2).getX();
            Double y3 = vectors.get(2).getY();
            Double z3 = vectors.get(2).getZ();

            Log.d(LOG, "Переменные = " + x1 +";"+ y1 +";"+ z1);
            Log.d(LOG, "Переменные = " + x2 +";"+ y2 +";"+ z2);
            Log.d(LOG, "Переменные = " + x3 +";"+ y3 +";"+ z3);

            multAnswer = x1*y2*z3+y1*z2*x3+z1*x2*y3-x3*y2*z1-x1*y3*z2-x2*y1*z3;
        Log.d(LOG, "Ответ = " + multAnswer);

        return multAnswer.toString();
    }
}
