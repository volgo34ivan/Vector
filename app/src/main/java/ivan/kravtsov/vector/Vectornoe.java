package ivan.kravtsov.vector;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.ArrayList;

public class Vectornoe {

    static ArrayList<Vector> vectors = new ArrayList<>();
    static ArrayList<String > operators = new ArrayList<>();
    static String[] operand;
    static Double x = 0.0;
    static Double y = 0.0;
    static Double amp = 0.0;
    static Double phRAD = 0.0;
    static String ans = "";
    static final String LOG = "Vectornoe";
    static Double PI = 4 * Math.atan(1);
    static Double ansDouble;


    @SuppressLint("DefaultLocale")
    static String Calculate(String exp){
        Log.d(LOG, "Выражение на входе ="+ exp);
        ansDouble = 0.0;
        String[] splitter;
        operators.clear();
        splitter = exp.split("");
        Integer subzero = 1;
        Log.d(LOG, "1й оператор в сплите = "+ splitter[1]);
        if(splitter[1].equals("-")){
            subzero = -1;
            splitter[1] = "";
            StringBuilder expBuilder = new StringBuilder();
            for (String aSplitter : splitter) {
                if (!aSplitter.equals("")) {
                    expBuilder.append(aSplitter);
                }
            }
            exp = expBuilder.toString();
            Log.d(LOG, "Измененное выражение = "+ exp);
        }

        Log.d(LOG, "Длинна выражения ="+ splitter.length);
        for(int i=0; i<splitter.length; i++){
            int spring = 0;
            if(splitter[1].equals("-")){spring = 2;}
                if(splitter[i+spring].equals("+")){operators.add(splitter[i+spring]);}
                if(splitter[i+spring].equals("-")){operators.add(splitter[i+spring]);}
        }

        Log.d(LOG, "Количество операторов = "+ operators.size());
        x = 0.0;
        y = 0.0;
        amp = 0.0;
        phRAD = 0.0;
        vectors.clear();
        operand = null;
        operand = exp.split("[X]");
        for(int i=0; i < operand.length; i++){
            Vector vector = new Vector(operand[i]);
            vectors.add(i,vector);
        }
        vectors.get(0).setAmplitude(vectors.get(0).getAmplitude() * subzero);

            phRAD = vectors.get(0).getAngleRAD() - vectors.get(1).getAngleRAD();
        Double phDEG = vectors.get(0).getAngleDEG() - vectors.get(1).getAngleDEG();
        Log.d(LOG, "Разность фаз в радианах = "+ phRAD);
        Log.d(LOG, "Разность фаз в градусах = "+ phDEG);
            if(phRAD <0){
                Log.d(LOG, "Число ПИ = "+ PI);
                phRAD = phRAD + 2 * PI;}
            if(phDEG == 90){ ans = "0";} else {
                ansDouble = vectors.get(0).getAmplitude()
                        * vectors.get(1).getAmplitude()
                        * Math.sin(phRAD);
                Log.d(LOG, "Ответ в формате Double до format = "+ ansDouble);
            }

        VectorView.vectorsView.clear();
        VectorView.vectorsView.addAll(vectors);
        ans = String.format("%.4f",ansDouble);
        Log.d(LOG, "Результат расчета ="+ ans);
        return ans;
    }
}
