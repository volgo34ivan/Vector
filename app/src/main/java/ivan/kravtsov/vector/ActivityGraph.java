package ivan.kravtsov.vector;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.WindowManager;

public class ActivityGraph extends Activity {

    private static final double RAD = Math.PI / 180;
    public static Bitmap canvasBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.vector_canvas);
        canvasBackground = Assets.getBitmapFromAsset(this,"background/radar.jpg");
    }
}
