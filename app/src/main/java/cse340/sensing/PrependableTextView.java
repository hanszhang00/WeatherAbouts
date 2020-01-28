package cse340.sensing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class PrependableTextView extends AppCompatTextView {
    public PrependableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Sets the text of this TextView to the specified resource followed by its current text after
     * an empty line.
     *
     * @param id    String resource to prepend to this.
     */
    @SuppressLint("SetTextI18n")
    public void prependText(@StringRes int id) {
        setText(getResources().getText(id) + "\n\n" + getText());
    }
}
