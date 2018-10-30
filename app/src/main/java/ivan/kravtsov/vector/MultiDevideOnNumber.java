package ivan.kravtsov.vector;

import android.util.Log;

import java.util.ArrayList;

public class MultiDevideOnNumber {

    static ArrayList<Vector> vectors = new ArrayList<>();
    static final String LOG = "MultiDevide";
    static Double multAnswer;
    static char[] allExp;

    static String Calculate(String exp){
        vectors.clear();
        allExp = null;
        Log.d(LOG, "Выражение на входе ="+ exp);
        String[] splitter;
        splitter = exp.split("[/*]");
        allExp = exp.toCharArray();

            Vector vector = new Vector(splitter[0]);
            vectors.add(vector);
            Log.d(LOG, "Вектор "+ 1 + " = " + splitter[0]);


        Log.d(LOG,"Размер массива " + vectors.size());
        Log.d(LOG,"ID вектора 1 " + vectors.get(0).id);
        Double A1 = vectors.get(0).getAmplitude();

        for(int i=0; i<allExp.length; i++){
            if(Character.toString(allExp[i]).equals("*")){
                multAnswer = A1*Double.parseDouble(splitter[1]);
                vectors.get(0).setAmplitude(multAnswer);
            }
            if(Character.toString(allExp[i]).equals("/")){
                multAnswer = A1/Double.parseDouble(splitter[1]);
                vectors.get(0).setAmplitude(multAnswer);
            }
        }

        Log.d(LOG, "Ответ = " + vectors.get(0).getAmplitude() + "^" + vectors.get(0).getAngleDEG());

        return vectors.get(0).getAmplitude() + "^" + vectors.get(0).getAngleDEG();
    }
}
