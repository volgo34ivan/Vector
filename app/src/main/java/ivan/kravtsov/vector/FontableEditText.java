package ivan.kravtsov.vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")

public class FontableEditText extends EditText {
    public FontableEditText(Context context) {
        super(context);
        if(!isInEditMode()) return;
    }

    public FontableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode())
            UiUtil.setCustomFont(this, context, attrs,
                    R.styleable.com_example_foo_view_FontableTextView,
                    R.styleable.com_example_foo_view_FontableTextView_myfont);
    }

    public FontableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(!isInEditMode())
        UiUtil.setCustomFont(this, context, attrs,
                R.styleable.com_example_foo_view_FontableTextView,
                R.styleable.com_example_foo_view_FontableTextView_myfont);
    }
}
