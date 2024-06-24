package com.qishi.utsdialog;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;

import java.util.List;
import java.util.Map;

public class Tool {
    public static void show(Context context, String title, List<Map<String, Object>> toolsList, toolCallback callback) {
        CustomDialog.build()
                .setCustomView(new OnBindView<CustomDialog>(R.layout.tool_custom) {
                    @Override
                    public void onBind(final CustomDialog dialog, View v) {
                        // 设置标题
                        TextView tvTitle = v.findViewById(R.id.tool_title);
                        tvTitle.setText(title);
                        /* 最外层 */
                        LinearLayout outerContainer = v.findViewById(R.id.outer_container);
                        outerContainer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 在这里处理点击事件
                                dialog.dismiss();
                            }
                        });
                        /* 内容区框 */
                        LinearLayout containerLayout = v.findViewById(R.id.container_layout);
                        containerLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 在这里处理点击事件
//                                PopTip.show("容器被点击");
                            }
                        });
                        /* 选项 */
                        ListView lvTools = (ListView) v.findViewById(R.id.lv_tools);
                        ToolsAdapter adapter =  new ToolsAdapter(context, toolsList);
                        lvTools.setAdapter(adapter);
                        lvTools.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                dialog.dismiss();
                                Map<String, Object> selectedItem = toolsList.get(position);
                                callback.onToolClick(selectedItem);
                            }
                        });
                    }
                })
                .setMaskColor(Color.parseColor("#4D000000"))
                .show();
    }

}
