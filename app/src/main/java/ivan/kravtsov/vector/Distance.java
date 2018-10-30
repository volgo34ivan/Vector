package ivan.kravtsov.vector;

import android.util.Log;

import java.util.ArrayList;

import static java.lang.Math.*;

public class Distance {
    static ArrayList<Vector> vectors = new ArrayList<>();
    static final String LOG = "Distance";
    static Double multAnswer;
    static String[] subsplitter;
    static String[] splitter;
    static int id;
    static Double x1,x2,y1,y2,z1,z2;

    static String Calculate(String exp){
        id = 0;
        vectors.clear();
        subsplitter = null;
        Log.d(LOG, "Выражение на входе ="+ exp);
        splitter = exp.split("dis");
        CheckExpression();
        if(id==0){
            for (int i = 0; i<splitter.length; i++) {
                subsplitter = splitter[i].split("[(;)]");
                Double x = Double.parseDouble(subsplitter[1]);
                Double y = Double.parseDouble(subsplitter[2]);
                Double z = Double.parseDouble(subsplitter[3]);
                Vector vector = new Vector(x,y,z);
                vectors.add(vector);
                Log.d(LOG, "Вектор COORD "+ i + " = " + splitter[i]);
                for(int b= 0; b< subsplitter.length; b++){
                    Log.d(LOG, "Значение в ячейке "+ b + " = " + subsplitter[b]);
                }
                subsplitter = null;
            }
            x1 = vectors.get(0).getX();
            y1 = vectors.get(0).getY();
            z1 = vectors.get(0).getZ();

            x2 = vectors.get(1).getX();
            y2 = vectors.get(1).getY();
            z2 = vectors.get(1).getZ();
        }else{
            for (int i = 0; i<splitter.length; i++) {
                Vector vector = new Vector(splitter[i]);
                vectors.add(vector);
                Log.d(LOG, "Вектор A/Ph "+ i + " = " + splitter[i]);
            }
            x1 = vectors.get(0).getX();
            y1 = vectors.get(0).getY();
            z1 = 0.0;

            x2 = vectors.get(1).getX();
            y2 = vectors.get(1).getY();
            z2 = 0.0;
        }


        Log.d(LOG,"Размер массива " + vectors.size());
        Log.d(LOG,"ID вектора 1 " + vectors.get(0).id);
        Log.d(LOG,"ID вектора 2 " + vectors.get(1).id);

        Log.d(LOG, "Переменные = " + x1 +";"+ y1 +";"+ z1);
        Log.d(LOG, "Переменные = " + x2 +";"+ y2 +";"+ z2);

        multAnswer = sqrt(pow(x2-x1,2)+pow(y2-y1,2)+pow(z2-z1,2));
        Log.d(LOG, "Ответ = " + multAnswer);

        return multAnswer.toString();
    }
    static public void CheckExpression(){
        char[] subopersplit;
                subopersplit = splitter[1].toCharArray();
                for (char aSubopersplit : subopersplit) {
                    if(Character.toString(aSubopersplit).equals("^")){
                        id = 1;
                    }else if(id != 1){
                        id = 0;
                    }
                }
    }
}
