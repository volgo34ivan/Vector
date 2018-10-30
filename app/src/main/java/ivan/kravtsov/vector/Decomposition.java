package ivan.kravtsov.vector;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Decomposition {

    static ArrayList<Vector> vectorsDecomposition = new ArrayList<>(); //массив для рисования графика
    static DecimalFormat df = new DecimalFormat("#.####");
    static String ans;

    static String Calculate(String exp){
        ans = "";
        vectorsDecomposition.clear();
        String[] operands = exp.split(":");
        Vector vector = new Vector(operands[0]);
        vectorsDecomposition.add(vector);
        Double InputAmpl = vector.getAmplitude();
        Double InputPhase = vector.getAngleDEG();
        String NVphaseEdit1 = operands[1];
        String NVphaseEdit2 = operands[2];
        String NVampEditAnswer1 = "0";
        String NVampEditAnswer2 = "0";
        //Инициализация координат векторов
        String sc = InputAmpl.toString();
        String sa = "1";
        String sb = "1";
        String scx = InputPhase.toString();
        String scy = InputPhase.toString();
        String sx1 = NVphaseEdit1;
        String sx2 = NVphaseEdit2;
        String sy1 = NVphaseEdit1;
        String sy2 = NVphaseEdit2;
        double c;
        if(sc.equals("")){
            c = 0;}else{
            c = Double.parseDouble(sc);}
        double a;
        if(sa.equals("")){
            a = 0;}else{
            a = Double.parseDouble(sa);}
        double b;
        if(sb.equals("")){
            b = 0;}else{
            b = Double.parseDouble(sb);}
        double cx;
        if(scx.equals("")){
            cx = 0;}else{
            cx = Math.rint((Math.cos(Math.toRadians(Double.parseDouble(scx))) * c)*1000)/1000;}
        double cy;
        if(scy.equals("")){
            cy = 0;}else{
            cy = Math.rint((Math.sin(Math.toRadians(Double.parseDouble(scy))) * c)*1000)/1000;}
        double x1;
        if(sx1.equals("")){
            x1 = 0;}else{
            x1 = Math.rint((Math.cos(Math.toRadians(Double.parseDouble(sx1))) * a)*1000)/1000;}
        double x2;
        if(sx2.equals("")){
            x2 = 0;}else{
            x2 = Math.rint((Math.cos(Math.toRadians(Double.parseDouble(sx2))) * b)*1000)/1000;}
        double y1;
        if(sy1.equals("")){
            y1 = 0;}else{
            y1 = Math.rint((Math.sin(Math.toRadians(Double.parseDouble(sy1))) * a)*1000)/1000;}
        double y2;
        if(sy2.equals("")){
            y2 = 0;}else{
            y2 = Math.rint((Math.sin(Math.toRadians(Double.parseDouble(sy2))) * b)*1000)/1000;}

        double d = x1 * y2 - x2 * y1;

        if(d !=0){
            if(y1 == 0){
                cx = cx / x1;
                x2 = x2 / x1;
                x1 = x1 / x1;
                cy = cx / y2;
                y2 = y2 / y2;
                y2 = cy;
                x1 = cx - x2 * y2;
                a = a * x1;
                b = b * y2;
                NVampEditAnswer1 = String.format(Locale.US, "%.3f", a);
                NVampEditAnswer2 = String.format(Locale.US, "%.3f", b);
            }else{
                double p;
                if(x1 ==0 || y2 ==0){
                    if(x1 ==0){
                        cy = cy / y1;
                        y2 = y2 / y1;
                        y1 =1;
                        x1 =0;
                        cx = cx / x2;
                        x2 =1;
                        x2 = cx;
                        y1 = cy - y2 * x2;
                        a = a * y1;
                        b = b * x2;
                        NVampEditAnswer1 = String.format(Locale.US, "%.3f", a);
                        NVampEditAnswer2 = String.format(Locale.US, "%.3f", b);
                    }else{
                        p =(x1 / y1)*(-1);
                        y1 = y1 * p;
                        y2 = y2 * p;
                        cy = cy * p;
                        x1 = x1 + y1;
                        x2 = x2 + y2;
                        cx = cx + cy;
                        x1 =0;
                        cy = cy / y1;
                        y2 = y2 / y1;
                        y1 =1;
                        cx = cx / x2;
                        x2 =1;
                        x2 = cx;
                        y1 = cy - y2 * x2;
                        a = a * y1;
                        b = b * x2;
                        NVampEditAnswer1 = String.format(Locale.US, "%.3f", a);
                        NVampEditAnswer2 = String.format(Locale.US, "%.3f", b);
                    }
                }else{
                    p =(y1 / x1)*(-1);
                    x1 = x1 * p;
                    x2 = x2 * p;
                    cx = cx * p;
                    y1 = y1 + x1;
                    y2 = y2 + x2;
                    cy = cy + cx;
                    cx = cx / x1;
                    x2 = x2 / x1;
                    cy = cy / y2;
                    y2 = cy;
                    x1 = cx - x2 * y2;
                    a = a * x1;
                    b = b * y2;
                    NVampEditAnswer1 = String.format(Locale.US, "%.3f", a);
                    NVampEditAnswer2 = String.format(Locale.US, "%.3f", b);
                }
            }
        }else{
            Log.d("DECOMP","Вычислить невозможно!");
            ans = "Векторы коллинеарны!";
            VectorView.vectorsView.clear();
        }

        String vector_1 = a + "^" + NVphaseEdit1;
        String vector_2 = b + "^" + NVphaseEdit2;
        vectorsDecomposition.add(new Vector(vector_1));
        vectorsDecomposition.add(new Vector(vector_2));

        sc = InputAmpl.toString();
        sa = NVampEditAnswer1;
        sb = NVampEditAnswer2;
        scx = InputPhase.toString();
        scy = InputPhase.toString();
        sx1 = NVphaseEdit1;
        sx2 = NVphaseEdit2;
        sy1 = NVphaseEdit1;
        sy2 = NVphaseEdit2;
        if(sc.equals("")){
            c = 0.0;}else{
            c = Double.parseDouble(sc);}
        if(sa.equals("")){
            a = 0.0;}else{
            a = Double.parseDouble(sa);}
        if(sb.equals("")){
            b = 0.0;}else{
            b = Double.parseDouble(sb);}
        if(scx.equals("")){
            cx = 0.0;}else{
            cx = Math.rint((Math.cos(Math.toRadians(Double.parseDouble(scx))) * c)*1000)/1000;}
        if(scy.equals("")){
            cy = 0.0;}else{
            cy = Math.rint((Math.sin(Math.toRadians(Double.parseDouble(scy))) * c)*1000)/1000;}
        if(sx1.equals("")){
            x1 = 0.0;}else{
            x1 = Math.rint((Math.cos(Math.toRadians(Double.parseDouble(sx1))) * a)*1000)/1000;}
        if(sx2.equals("")){
            x2 = 0.0;}else{
            x2 = Math.rint((Math.cos(Math.toRadians(Double.parseDouble(sx2))) * b)*1000)/1000;}
        if(sy1.equals("")){
            y1 = 0.0;}else{
            y1 = Math.rint((Math.sin(Math.toRadians(Double.parseDouble(sy1))) * a)*1000)/1000;}
        if(sy2.equals("")){
            y2 = 0.0;}else{
            y2 = Math.rint((Math.sin(Math.toRadians(Double.parseDouble(sy2))) * b)*1000)/1000;}
        //Проверка
        double testX = ((x1 + x2) / cx) / 100;
        double testY = ((y1 + y2) / cy) / 100;
        if(testX <0.05 && testY <0.05){
            Log.d("DECOMP", "Проверка прошла успешно!");
            Log.d("DECOMP", "Исходный вектор" + vector.getAmplAngle());
            Log.d("DECOMP", "Амплитуда вектора на(" + NVphaseEdit1 + ") = " + NVampEditAnswer1);
            Log.d("DECOMP", "Амплитуда вектора на(" + NVphaseEdit2 + ") = " + NVampEditAnswer2);
        }else{
            Log.d("DECOMP", "Проверка прошла не успешно!");
        }
if(!ans.equals("Векторы коллинеарны!")){
    VectorView.vectorsView.clear();
    VectorView.vectorsView.addAll(vectorsDecomposition);
    ans = vectorsDecomposition.get(0).getAmplAngle()
            +" : "+ df.format(vectorsDecomposition.get(1).getAmplitude())
            +"^"+ vectorsDecomposition.get(1).getAngleDEG()
            +" : "+ df.format(vectorsDecomposition.get(2).getAmplitude())
            +"^"+ vectorsDecomposition.get(2).getAngleDEG();
    Log.d("DECOMP", ans);
}
        return ans;
    }
}
