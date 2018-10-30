package ivan.kravtsov.vector;

import static java.lang.Math.*;

public class Vector {
    public Integer id = 0;
    private String AmplAngle;
    private Double AngleRAD;
    private Double AngleDEG;
    private Double Amplitude;
    private Double multipler;
    private Double x = 0.0;
    private Double y = 0.0;
    private Double z = 0.0;
    private String parser = "\\^";
    private String[] DataVector;
    private static Double PI = 4*atan(1);
    public Vector(String inputData){
        AmplAngle = inputData;
        DataVector = AmplAngle.split(parser);
        AngleDEG = Double.parseDouble(DataVector[1]);
        Amplitude = Double.parseDouble(DataVector[0]);
        AngleRAD = Double.parseDouble(DataVector[1])*(PI/180);
    }
    public Vector (Integer i,String d){
        multipler = Double.parseDouble(d);
    }
    public Vector (Double xDouble, Double yDouble, Double zDouble){
        id = 1;
        x = xDouble;
        y = yDouble;
        z = zDouble;
    }
    public Double getMultipler(){
        return multipler;
    }
    public void setAmplitude(Double d){
        Amplitude = d;
    }
    public String getAmplAngle(){return AmplAngle;}
    public Double getAngleRAD(){return AngleRAD;}
    public Double getAngleDEG(){return AngleDEG;}
    public Double getAmplitude(){return Amplitude;}
    public Double getX(){
        if(id == 1){
            return x;
        }else{
            x = cos(AngleRAD)*Amplitude;
            return x;
        }
        }
    public Double getY(){
        if(id == 1){
            return y;
        }else{
            y = sin(AngleRAD)*Amplitude;
            return y;
        }
        }
    public Double getZ(){
        if(id == 1){
            return z;
        }else{
            z = sin(AngleRAD)*Amplitude;
            return z;
        }
        }
}
