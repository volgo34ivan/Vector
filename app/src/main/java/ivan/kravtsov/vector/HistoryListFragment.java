package ivan.kravtsov.vector;

import android.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryListFragment extends ListFragment {

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        TextView date = (TextView) getViewByPosition(position,l).findViewById(R.id.date);
        TextView input = (TextView) getViewByPosition(position,l).findViewById(R.id.input);
        TextView answer = (TextView) getViewByPosition(position,l).findViewById(R.id.answer);
        TextView idString = (TextView) getViewByPosition(position,l).findViewById(R.id.idString);
        TextView idInteger = (TextView) getViewByPosition(position,l).findViewById(R.id.idNumber);

        String _date = date.getText().toString();
        String _input = input.getText().toString();
        String _answer = answer.getText().toString();
        String _idString = idString.getText().toString();
        String _idInteger = idInteger.getText().toString();


        Keyboard.input.setText(_input);
        Keyboard.answer.setText(_answer);
        Keyboard.id = Integer.parseInt(_idInteger);

        Toast.makeText(getActivity(), "Данные скопированы в окно ввода.", Toast.LENGTH_SHORT).show();

        Log.d("ListFragment","Нажат элемент ID: " + position + " Дата элемента: " + _date);
        Log.d("ListFragment","Нажат элемент ID: " + position + " Вводеные данные: " + _input);
        Log.d("ListFragment","Нажат элемент ID: " + position + " Ответ: " + _answer);
        Log.d("ListFragment","Нажат элемент ID: " + position + " Операция: " + _idString);
        Log.d("ListFragment","Нажат элемент ID: " + position + " ID операции: " + _idInteger);
        Log.d("ListFragment","Нажат элемент ID: " + position + " скопирован ID операции: " + Integer.parseInt(_idInteger));

    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

}
