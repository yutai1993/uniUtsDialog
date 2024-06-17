package com.qishi.utsdialog;

import org.json.JSONObject;
import android.webkit.WebView;

public interface DialogCallback {
    void onConfirm(JSONObject data);
    void onCancel(JSONObject data);
    void onShouldOverrideUrlLoading(WebView view, String url);
}