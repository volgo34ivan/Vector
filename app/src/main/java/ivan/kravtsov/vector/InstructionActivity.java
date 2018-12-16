package ivan.kravtsov.vector;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class InstructionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.instructions_layout);
    }
}
