package ivan.kravtsov.vector;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static ivan.kravtsov.vector.Keyboard.*;

public class HistoryActivity extends Activity {

    private static ArrayList<HistoryItem> historyItemsArrayList;
    private static HistoryItemAdapter historyItemAdapter;
    public static String msg1,msg2,msg3,msg4,msg5,msg6,msg7,msg8;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        msg1 = getResources().getString(R.string.msg1);
        msg2 = getResources().getString(R.string.msg2);
        msg3 = getResources().getString(R.string.msg3);
        msg4 = getResources().getString(R.string.msg4);
        msg5 = getResources().getString(R.string.msg5);
        msg6 = getResources().getString(R.string.msg6);
        msg7 = getResources().getString(R.string.msg7);
        msg8 = getResources().getString(R.string.msg8);
        mContext = this;
        FragmentManager fm = getFragmentManager();
        HistoryListFragment historyListFragment = (HistoryListFragment) fm.findFragmentById(R.id.HistoryListFragment);
        //Массив с элементами истории вычислений
        historyItemsArrayList = new ArrayList<HistoryItem>();
        //Адаптер для привязки массива истории к ListView
        int resId = R.layout.history_item;
        historyItemAdapter = new HistoryItemAdapter(this,resId, historyItemsArrayList);
        historyListFragment.setListAdapter(historyItemAdapter);
        load();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.clear_history) {
            Keyboard.clearFile();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_start, menu);
        try{
            getActionBar().setTitle(getResources().getString(R.string.history_title));
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return true;
    }

    public void load(){

        String date = "";
        String expression = "";
        String answer = "";
        String id = "";
        Integer idInt;
        String filename;
        BufferedReader br = null;
            try {
                filename = this.mContext.getFilesDir() + "history_vectors.txt";
                br = new BufferedReader(new FileReader(filename));
                Log.d("ReadLine", "Файл найден.");
            }catch (IOException e1){
                Log.d("ReadLine", "Файл не найден.");
            }
        StringBuilder text = new StringBuilder();
        try {
            String line;

            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
                lineCount++;
                Log.d("ReadLine", String.valueOf(lineCount) + " Текущяя строка" + " " + line + " лог внутри основного цикла");
                switch (lineCount){
                    case 1:
                        date = line;
                        break;
                    case 2:
                        expression = line;
                        break;
                    case 3:
                        answer = line;
                        break;
                    case 4:
                        id = line;
                        idInt = Integer.parseInt(line);
                        onNewItemAdded(expression,answer,date,id, idInt);
                        lineCount = 0;
                        break;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            //обработка исключения в будущем
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    static public void onNewItemAdded(String newItemInput, String newItemAnswer){
        HistoryItem newHistoryItem = new HistoryItem(newItemInput, newItemAnswer);
        historyItemsArrayList.add(0, newHistoryItem);
        historyItemAdapter.notifyDataSetChanged();
    }
    static public void onNewItemAdded(String newItemInput, String newItemAnswer, String dateTimeCreated, String id, Integer idInteger){
        HistoryItem newHistoryItem = new HistoryItem(newItemInput, newItemAnswer,dateTimeCreated, id, idInteger);
        historyItemsArrayList.add(0, newHistoryItem);
        historyItemAdapter.notifyDataSetChanged();
    }
}
