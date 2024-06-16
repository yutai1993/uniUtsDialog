package com.qishi.utsdialog;

import android.content.Context;
import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.MessageDialog;

public class UtsDialog {
    public static void init(Context context){
        //初始化
        DialogX.init(context);
    }
    public static void dialogShow(){
        MessageDialog.show("标题", "正文内容", "确定");
    }
}
