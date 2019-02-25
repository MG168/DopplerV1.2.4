package com.mgstudio.dopplerv124.DefButton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgstudio.dopplerv124.R;

//自定义ImageButton，模拟ImageButton，并在其下方显示文字,并提供Button的部分接口
public class DefImagButton extends LinearLayout {
    public interface IMyClick {
        void onMyClick(int testid);
    }

    // ----------------private attribute-----------------------------
    private ImageButton mButtonImage = null;
    private TextView mButtonText = null;

    private int mtestid = -1;
    /*初始化接口变量*/
    IMyClick iMyClick = null;

    /*自定义事件*/
    public void setOnMyClickListener(IMyClick _iMyClick) {
        iMyClick = _iMyClick;
    }

    public DefImagButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.defimagebutton, this, true);
        mButtonImage = (ImageButton) findViewById(R.id.btn_imgbtn);//调用原始的自定义控件的原型
        mButtonImage.setOnClickListener(onClickListener);
        mButtonText = (TextView) findViewById(R.id.btn_texttitle);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ImgBtn);
        if (attributes != null) {

            //处理titleBar背景色
            String title = attributes.getString(R.styleable.ImgBtn_texttitle);
            setText(title);

            int icon = attributes.getResourceId(R.styleable.ImgBtn_icon, -1);
            setImageResource(icon);
            mtestid = attributes.getInt(R.styleable.ImgBtn_testid, -1);
        }

    }

    // ----------------public method-----------------------------
    /*
     * setImageResource方法
     */
    public void setImageResource(int resId) {
        mButtonImage.setImageResource(resId);
        mButtonImage.setScaleType(ImageView.ScaleType.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    /*
     * setText方法
     */
    public void setText(String title) {
        mButtonText.setText(title);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            iMyClick.onMyClick(mtestid);
        }

    };



    /*
     * setTextColor方法
     */
    public void setTextColor(int color) {
        mButtonText.setTextColor(color);
    }


}
