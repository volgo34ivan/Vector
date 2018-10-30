package ivan.kravtsov.vector;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HistoryItem {
    String input;
    String answer;
    Date created;
    String itemCreated;
    Date time;
    String idString;
    Integer idNumber;
    String msg1,msg2,msg3,msg4,msg5,msg6,msg7,msg8;

    public String getInput(){
        return input;
    }
    public String getAnswer(){
        return answer;
    }
    public Date getDate(){
        return created;
    }
    public Date getTime(){
        return time;
    }
    public String getItemCreated(){
        return itemCreated;
    }

    public HistoryItem(String _input, String _answer){
        this(_input,_answer, new Date(java.lang.System.currentTimeMillis()), Calendar.getInstance().getTime());
    }
    public HistoryItem(String _input,String _answer, Date _created, Date _time){
        input = _input;
        answer = _answer;
        created = _created;
        time = _time;
    }
    public HistoryItem(String _input,String _answer, String _created, String _id, Integer idInt){
        input = _input;
        answer = _answer;
        itemCreated = _created;
        idString = _id;
        idNumber = idInt;
        msg1 = HistoryActivity.msg1;
        msg2 = HistoryActivity.msg2;
        msg3 = HistoryActivity.msg3;
        msg4 = HistoryActivity.msg4;
        msg5 = HistoryActivity.msg5;
        msg6 = HistoryActivity.msg6;
        msg7 = HistoryActivity.msg7;
        msg8 = HistoryActivity.msg8;
    }

    @Override
    public String toString(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String dateString = sdf.format(created);
        return "(" + dateString + " " + time + ")" + input + " " + answer + " " + idString + " " + idNumber;
    }

    public String getIdString() {
        int _id = Integer.parseInt(idString);
        String msg = idString;
        if(_id !=0){
            switch (_id){
                case 1:
                    msg = msg1; // "Сложение/вычитание.";
                    idNumber = 1;
                    break;
                case 2:
                    msg = msg2; //"Разложение вектора.";
                    idNumber = 2;
                    break;
                case 3:
                    msg = msg3; //"Скалярное умножение.";
                    idNumber = 3;
                    break;
                case 4:
                    msg = msg4; //"Векторное умножение.";
                    idNumber = 4;
                    break;
                case 5:
                    msg = msg5; //"Смешанное умножение.";
                    idNumber = 5;
                    break;
                case 6:
                    msg = msg6; //"Умножение/деление на скаляр.";
                    idNumber = 6;
                    break;
                case 7:
                    msg = msg7; //"Расстояние между векторами.";
                    idNumber = 7;
                    break;
                case 8:
                    msg = msg8; //"Угол между вектором и осями x,y,z.";
                    idNumber = 8;
                    break;
                case 9:

                    break;
                case 10:

                    break;
            }
        }
        return msg;
    }
    public Integer getIdInteger(){
        return idNumber;
    }
}
