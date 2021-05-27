package com.codility.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;

public class NoGifEditText extends AppCompatEditText {

    public NoGifEditText(Context context) {
        super(context);
    }

    public NoGifEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoGifEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        final InputConnection ic = super.onCreateInputConnection(editorInfo);
        EditorInfoCompat.setContentMimeTypes(editorInfo, new String[]{"image/*", "image/png", "image/gif", "image/jpeg"});

        return InputConnectionCompat.createWrapper(ic, editorInfo,
                (inputContentInfo, flags, opts) -> {
                    Toast.makeText(getContext(), "No gif support", Toast.LENGTH_SHORT).show();
                    return true;
                });
    }
}
