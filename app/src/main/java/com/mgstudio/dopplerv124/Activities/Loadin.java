package com.mgstudio.dopplerv124.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mgstudio.dopplerv124.R;

/* 登录页代码 */
public class Loadin extends Activity {

    private final String TAG="LoadinActivity";
    private EditText edit_ip;
    private EditText edit_port;
    private Button btn_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadin);

        /* 初始化 */
        edit_ip = (EditText) findViewById(R.id.edit_ip);
        edit_port = (EditText) findViewById(R.id.edit_port);
        btn_connect = (Button) findViewById(R.id.btn_connect);

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toloadin = new Intent(Loadin.this, MainActivity.class);

                //将ip和port传递给下一个活动
                String linkip = edit_ip.getText().toString();
                toloadin.putExtra("linkiip", linkip);
                String linkport = edit_port.getText().toString();
                toloadin.putExtra("linkiport", linkport);

                startActivity(toloadin);
            }
        });

    }

}
