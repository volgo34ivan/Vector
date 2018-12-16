package ivan.kravtsov.vector;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import static java.lang.Math.*;

public class Decomposition {

    public static ArrayList<Vector> vectorsDecomposition = new ArrayList<>(); //массив для рисования графика
    static String format = "%.3f";
    static String ans;
    static String TAG = "Decomposition";
    static DecimalFormat df = new DecimalFormat("#.##");

    public static String Calculate(String exp){
        Log.d(TAG, "Expression " + exp);
        ans = "";
        vectorsDecomposition.clear();
        String[] operands = exp.split(":");
        Vector vector = new Vector(operands[0]);
        vectorsDecomposition.add(vector);
        Double amplA = 1.0d;//амплитуда вектора A (искомое значение!)
        Double phaseA = Double.parseDouble(operands[1]);//фаза вектора A
        Double amplB = 1.0d;//амплитуда вектора B (искомое значение!)
        Double phaseB = Double.parseDouble(operands[2]);//фаза вектора B
        Double amplC = vector.getAmplitude(); //амплитуда вектора C
        Double phaseC = vector.getAngleDEG(); //фаза вектора C

        Log.d(TAG,"values input:  \n " +
                "sAmpC: " + amplC + "\n" +
                "sPhaseC: " + phaseC + "\n" +
                "sPhaseA: " + phaseA + "\n" +
                "sAmpA: " + amplA + "\n" +
                "sPhaseB: " + phaseB + "\n" +
                "sAmpB: " + amplB);

        double c = amplC;
        double a = amplA;
        double b = amplB;
        double xc = cos(toRadians(phaseC)) * c;
        double yc = sin(toRadians(phaseC)) * c;
        double xa = cos(toRadians(phaseA)) * a;
        double ya = sin(toRadians(phaseA)) * a;
        double xb = cos(toRadians(phaseB)) * b;
        double yb = sin(toRadians(phaseB)) * b;

        Log.d(TAG,"coords vectors:  \n " +
                "c: " + c + "\n" +
                "a: " + a + "\n" +
                "b: " + b + "\n" +
                "xc: " + xc + "\n" +
                "yc: " + yc + "\n" +
                "xa: " + xa + "\n" +
                "ya: " + ya + "\n" +
                "xb: " + xb + "\n" +
                "yb: " + yb + "\n");

        //Решение системы по формулам Крамера
        double d = xa * yb - xb * ya; // находи определитель матрицы 2х2
        Log.d(TAG, "D: " + d);
        if(d !=0){
            double da,db;
            da = xc * yb - xb * yc;
            db = xa * yc - xc * ya;
            double ka,kb;
            ka = da/d;
            kb = db/d;
            amplA = Double.parseDouble(String.format(Locale.US, format, ka));
            amplB = Double.parseDouble(String.format(Locale.US, format, kb));
        }else{
            ans = "Векторы коллинеарны!";
            VectorView.vectorsView.clear();
        }

        String vector_1 = amplA + "^" + phaseA;
        String vector_2 = amplB + "^" + phaseB;
        vectorsDecomposition.add(new Vector(vector_1));
        vectorsDecomposition.add(new Vector(vector_2));


if(!ans.equals("Векторы коллинеарны!")){
    VectorView.vectorsView.clear();
    VectorView.vectorsView.addAll(vectorsDecomposition);
    Log.d(TAG, vectorsDecomposition.get(1).getAmplitude().toString());
    ans = vectorsDecomposition.get(0).getVector()
            +" : "+ df.format(vectorsDecomposition.get(1).getAmplitude())
            +"^"+ vectorsDecomposition.get(1).getAngleDEG()
            +" : "+ df.format(vectorsDecomposition.get(2).getAmplitude())
            +"^"+ vectorsDecomposition.get(2).getAngleDEG();

}
        Log.d("Decomposition", ans);
        return ans;
    }
}
