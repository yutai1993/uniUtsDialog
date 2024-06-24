package com.qishi.utsdialog;

import android.webkit.WebView;
import org.json.JSONObject;

public interface DialogCallback {
    void onConfirm(JSONObject data);
    void onCancel(JSONObject data);
    void onShouldOverrideUrlLoading(WebView view, String url);
}

