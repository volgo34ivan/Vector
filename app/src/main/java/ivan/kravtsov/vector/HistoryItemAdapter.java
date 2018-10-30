package ivan.kravtsov.vector;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class HistoryItemAdapter extends ArrayAdapter<HistoryItem> {

    int resource;

    public HistoryItemAdapter(Context context,
                              int resource,
                              List<HistoryItem> items){
        super(context, resource, items);
        this.resource = resource;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent){

        LinearLayout itemView;

        HistoryItem item = getItem(position);

        String inputString = item.getInput();
        String answerString = item.getAnswer();
        String createdDateTime = item.getItemCreated();
        String idString = item.getIdString();
        Integer idNumber = item.getIdInteger();
        Log.d("NUMBER_ID", idNumber.toString());
        //Date createdDate = item.getDate();
        //Date createdTime = item.getTime();
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        //SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
        //String dateString = sdf.format(createdDate) + " " + sdftime.format(createdTime);

        if(convertView == null){
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource,itemView,true);
        }else{
            itemView = (LinearLayout) convertView;
        }

        TextView dateView = (TextView) itemView.findViewById(R.id.date);
        TextView answerView = (TextView) itemView.findViewById(R.id.answer);
        TextView inputView = (TextView) itemView.findViewById(R.id.input);
        TextView idView = (TextView) itemView.findViewById(R.id.idString);
        TextView idIntegerView = (TextView) itemView.findViewById(R.id.idNumber);

        dateView.setText(createdDateTime);
        inputView.setText(inputString);
        answerView.setText(answerString);
        idView.setText(idString);
        idIntegerView.setText(idNumber.toString());
       //idIntegerView.setText(idString);

        return itemView;
    }

}
