package ivan.kravtsov.vector;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.ArrayList;

public class PlusMinus {

    static ArrayList<Vector> vectors = new ArrayList<>();
    static ArrayList<String > operators = new ArrayList<>();
    static String[] operand;
    static Double x = 0.0;
    static Double y = 0.0;
    static Double amp = 0.0;
    static Double ph = 0.0;
    static String ans = "";
    static final String LOG = "Summa";

    static String Calculate(String exp){
        Log.d(LOG, "Выражение на входе ="+ exp);
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
        ph = 0.0;
        vectors.clear();
        operand = null;
        operand = exp.split("[-+]");
        for(int i=0; i < operand.length; i++){
                Vector vector = new Vector(operand[i]);
                vectors.add(i,vector);
        }
        vectors.get(0).setAmplitude(vectors.get(0).getAmplitude() * subzero);
        //блок расчетов
        for(int i=0; i<vectors.size(); i++){
            if(i==0){
                x += vectors.get(i).getX();
                y += vectors.get(i).getY();
            }

            if(i>0){
                Log.d(LOG, "оператор ="+ operators.get(i-1));
                if(operators.get(i-1).equals("+")){
                    x += vectors.get(i).getX();
                    y += vectors.get(i).getY();
                }
                if(operators.get(i-1).equals("-")){
                    x -= vectors.get(i).getX();
                    y -= vectors.get(i).getY();
                }
            }

        }

        amp = Math.sqrt((x*x)+(y*y));
        if(amp == 0.0){
            ph = 0.0;
        }else{
            if(y > 0){ph = Math.toDegrees(Math.acos(x/amp));}
            else{ph = 360 - Math.toDegrees(Math.acos(x/amp));}
        }
        @SuppressLint("DefaultLocale") String ans1 = String.format("%.4f",amp);
        @SuppressLint("DefaultLocale") String ans2 = String.format("%.4f",ph);
        ans = ans1 + "^" + ans2;

        vectors.add(new Vector(amp.toString()+"^"+ph.toString()));
        VectorView.vectorsView.clear();
        VectorView.vectorsView.addAll(vectors);
        Log.d(LOG, "Результат расчета ="+ ans);
        return ans;
    }
}
