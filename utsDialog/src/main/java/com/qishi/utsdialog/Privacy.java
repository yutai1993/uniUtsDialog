package com.qishi.utsdialog;

import android.graphics.Color;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import org.json.JSONObject;

public class Privacy {
    public static CustomDialog show(JSONObject options, PrivacyCallback callback) {
        // 标题title
        String title = options.optString("title");
        // 内容content
        String htmlStr = options.optString("htmlString");
        // MaskColor
        String maskColor = options.optString("maskColor", "#4D000000");

        return CustomDialog.show(new OnBindView<CustomDialog>(R.layout.layout_privacy_dialog) {
                    @Override
                    public void onBind(final CustomDialog dialog, View v) {
                        // 设置title
                        TextView tvTitle;
                        tvTitle = v.findViewById(R.id.tv_title);
                        tvTitle.setText(title);

                        // 设置确定按钮
                        TextView btnOk;
                        btnOk = v.findViewById(R.id.btn_ok);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                callback.onConfirm();
                            }
                        });

                        // 退出按钮
                        TextView btnCancel;
                        btnCancel = v.findViewById(R.id.btn_cancel);
                        btnCancel.setVisibility(View.VISIBLE);
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                dialog.dismiss();
                                    callback.onCancel();
                            }
                        });

                        CustomWebView customWebView = (CustomWebView) v.findViewById(R.id.webview);
                        customWebView.setBackgroundColor(Color.TRANSPARENT);
                        customWebView.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                // 检查URL是否是你想要拦截的链接
                                if (url != null) {
                                    // 回调
                                    callback.onShouldOverrideUrlLoading(view, url);
                                    // 返回true表示你已经处理了这个链接
                                    return true;
                                }
                                // 对于其他链接，返回false让WebView正常加载
                                return false;
                            }
                        });
                        customWebView.loadData(htmlStr, "text/html", "UTF-8");
                    }
                })
                .setMaskColor(Color.parseColor(maskColor))
                .setCancelable(false)
                .setAutoUnsafePlacePadding(false);

    }
}
