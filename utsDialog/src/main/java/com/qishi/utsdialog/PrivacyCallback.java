package com.qishi.utsdialog;

import android.webkit.WebView;

public interface PrivacyCallback {
    void onConfirm();
    void onCancel();
    void onShouldOverrideUrlLoading(WebView view, String url);
}

