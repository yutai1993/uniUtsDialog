package com.qishi.utsdialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import org.json.JSONObject;

public class UtsDialog {
    private static CustomDialog currentDialog = null; // 添加静态变量来持有对话框实例
    public static void init(Context context){
        //初始化
        DialogX.init(context);
    }
    public static void dialogShow(JSONObject options, DialogCallback callback){
        // 标题title
        String title = options.optString("title");
        // 内容content
        String htmlStr = options.optString("htmlString");
        // 确定confirmText 没传的话默认确定
        String confirmText = options.optString("confirmText", "确定");
        // 取消cancelText
        String cancelText = options.optString("cancelText");
        // MaskColor
        String maskColor = options.optString("maskColor", "#4D000000");
        // 是否显示取消按钮
        boolean showCancel = options.optBoolean("showCancel", false);
        boolean cancelable = options.optBoolean("cancelable", false);
        // 弹窗标识ID 用于回调回传
        String dialogId = options.optString("id");
        currentDialog = CustomDialog.show(new OnBindView<CustomDialog>(R.layout.layout_custom_dialog) {
                    @Override
                    public void onBind(final CustomDialog dialog, View v) {
                        // 设置title
                        TextView tvTitle;
                        tvTitle = v.findViewById(R.id.tv_title);
                        tvTitle.setText(title);
                        // showCancel为true并cancelText传入时显示取消按钮
                        if (showCancel && !cancelText.isEmpty()) {
                            TextView btnCancel;
                            btnCancel = v.findViewById(R.id.btn_cancel);
                            btnCancel.setVisibility(View.VISIBLE);
                            btnCancel.setText(cancelText);
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismiss();
                                    JSONObject data = new JSONObject();
                                    // dialogId存在则回传
                                    if (!dialogId.isEmpty()) {
                                        try {
                                            data.put("id", dialogId);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        callback.onCancel(data);
                                    }else {
                                        callback.onCancel(null);
                                    }
                                }
                            });
                        }else {
                            TextView btnCancel;
                            btnCancel = v.findViewById(R.id.btn_cancel);
                            // 隐藏取消按钮
                            btnCancel.setVisibility(View.GONE);
                        }
                        CustomWebView customWebView = (CustomWebView) v.findViewById(R.id.webview);
                        customWebView.setBackgroundColor(Color.TRANSPARENT);
                        customWebView.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                // 检查URL是否是你想要拦截的链接
                                if (url != null && url.startsWith("http")) {
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
                        TextView btnOk;
                        btnOk = v.findViewById(R.id.btn_ok);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismiss();
                                JSONObject data = new JSONObject();
                                // dialogId存在则回传
                                if (!dialogId.isEmpty()) {
                                    try {
                                        data.put("id", dialogId);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    callback.onConfirm(data);
                                }else {
                                    callback.onConfirm(null);
                                }
                            }
                        });
                    }
                })
                .setMaskColor(Color.parseColor(maskColor))
                .setCancelable(cancelable)
                .setAutoUnsafePlacePadding(false);

    }
    public static void dismiss() {
        if (currentDialog != null) {
            currentDialog.dismiss();
            currentDialog = null; // 清除引用，帮助垃圾收集器回收资源
        }
    }
}
