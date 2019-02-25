package com.mgstudio.dopplerv124.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mgstudio.dopplerv124.Activities.Buttonactivities.BackActivity;
import com.mgstudio.dopplerv124.Activities.Buttonactivities.ClearActivity;
import com.mgstudio.dopplerv124.Activities.Buttonactivities.EditActivity;
import com.mgstudio.dopplerv124.Activities.Buttonactivities.FocusActivity;
import com.mgstudio.dopplerv124.Activities.Buttonactivities.LineActivity;
import com.mgstudio.dopplerv124.Activities.Buttonactivities.MeasureActivity;
import com.mgstudio.dopplerv124.Activities.Buttonactivities.Mode1Activity;
import com.mgstudio.dopplerv124.Activities.Buttonactivities.Mode2Activity;
import com.mgstudio.dopplerv124.Activities.Buttonactivities.NoteActivity;
import com.mgstudio.dopplerv124.Activities.Buttonactivities.PatientActivity;
import com.mgstudio.dopplerv124.Activities.Buttonactivities.RangeActivity;
import com.mgstudio.dopplerv124.Activities.Buttonactivities.SaveImageActivity;
import com.mgstudio.dopplerv124.Activities.Buttonactivities.SaveVideoActivity;
import com.mgstudio.dopplerv124.Activities.Buttonactivities.SoundActivity;
import com.mgstudio.dopplerv124.Activities.Buttonactivities.StopActivity;
import com.mgstudio.dopplerv124.Activities.Buttonactivities.WaveActivity;
import com.mgstudio.dopplerv124.DefButton.DefImagButton;
import com.mgstudio.dopplerv124.DefButton.RulerView;
import com.mgstudio.dopplerv124.FunctionDefine;
import com.mgstudio.dopplerv124.R;
import com.mgstudio.dopplerv124.TaskCenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;


/* 客户端主界面代码 */
public class MainActivity extends AppCompatActivity implements DefImagButton.IMyClick,RulerView.OnValueChangeListener{


    private final String TAG="ClientActivity";
    private ImageView image_show;
    public Bitmap bmp = null;
    private Handler mMainHandler;// 主线程Handler
    private String line;

    public boolean stop_flag = false;//运行状态

    private EditText hastext;
    private Button button;
    private boolean isButton = true;//自定义带删除功能的文本编辑框

    private TextView mTextView;//自定义刻度尺
    private TextView textView1;

    private TextView mRun;//运行、冻结
    private TextView mPing;//帧率
    public int Ping_count = 0;

    private TextView mTime;
    private static final int msgKey1 = 1;//自定义时间显示

    /* 帧动画初始化 */
    private SimpleDraweeView mImgvOne;
    private AnimationDrawable animationDrawable;
//    // video output dimension 视频输出维度,<-这些包含了视频编码解码->
//    static final int OUTPUT_WIDTH = 640;
//    static final int OUTPUT_HEIGHT = 480;
//
//    VideoEncoder mEncoder;
//    VideoDecoder mDecoder;
//
//    SurfaceView mEncoderSurfaceView;
//    android.view.SurfaceView mDecoderSurfaceView;

    //    初始化控件
    DefImagButton defimgbtn_wifi;
    DefImagButton defimgbtn_plus, defimgbtn_focus, defimgbtn_range, defimgbtn_measure, defimgbtn_note, defimgbtn_clear;
    DefImagButton defimgbtn_minus, defimgbtn_wave, defimgbtn_sound, defimgbtn_back, defimgbtn_saveimage, defimgbtn_savevideo;
    DefImagButton defimgbtn_mode1, defimgbtn_mode2, defimgbtn_stop, defimgbtn_line, defimgbtn_patient, defimgbtn_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* 初始化 */
        image_show = (ImageView) findViewById(R.id.image_show);


//        <-注释内容为视频编码解码内容->
//        mEncoderSurfaceView = (SurfaceView) findViewById(R.id.encoder_surface);
//        mEncoderSurfaceView.getHolder().addCallback(mEncoderCallback);
//
//        mDecoderSurfaceView = (android.view.SurfaceView) findViewById(R.id.decoder_surface);
//        mDecoderSurfaceView.getHolder().addCallback(mDecoderCallback);
//
//        mEncoder = new MyEncoder();
//        mDecoder = new VideoDecoder();


        //自定义活动控件显示，实例化
        defimgbtn_plus = (DefImagButton) findViewById(R.id.defimgbtn_plus);
        defimgbtn_focus = (DefImagButton) findViewById(R.id.defimgbtn_focus);
        defimgbtn_range = (DefImagButton) findViewById(R.id.defimgbtn_range);
        defimgbtn_measure = (DefImagButton) findViewById(R.id.defimgbtn_measure);
        defimgbtn_note = (DefImagButton) findViewById(R.id.defimgbtn_note);
        defimgbtn_clear = (DefImagButton) findViewById(R.id.defimgbtn_clear);
        defimgbtn_minus = (DefImagButton) findViewById(R.id.defimgbtn_minus);
        defimgbtn_wave = (DefImagButton) findViewById(R.id.defimgbtn_wave);
        defimgbtn_sound = (DefImagButton) findViewById(R.id.defimgbtn_sound);
        defimgbtn_back = findViewById(R.id.defimgbtn_back);
        defimgbtn_saveimage = findViewById(R.id.defimgbtn_saveimage);
        defimgbtn_savevideo = (DefImagButton) findViewById(R.id.defimgbtn_savevideo);
        defimgbtn_mode1 = (DefImagButton) findViewById(R.id.defimgbtn_mode1);
        defimgbtn_mode2 = (DefImagButton) findViewById(R.id.defimgbtn_mode2);
        defimgbtn_stop = (DefImagButton) findViewById(R.id.defimgbtn_stop);
        defimgbtn_line = (DefImagButton) findViewById(R.id.defimgbtn_line);
        defimgbtn_patient = (DefImagButton) findViewById(R.id.defimgbtn_patient);
        defimgbtn_edit = (DefImagButton) findViewById(R.id.defimgbtn_edit);

        //刻度显示

        ((RulerView)findViewById(R.id.height_ruler)).setOnValueChangeListener(this);
        mTextView = (TextView)findViewById(R.id.text);
        //时间显示
        mTime = (TextView) findViewById(R.id.mytime);
        new TimeThread().start();

        //显示运行状态
        mRun = (TextView)findViewById(R.id.text_run);

        //显示帧率
        mPing = (TextView)findViewById(R.id.text_ping);

        //增益显示
        textView1 =(TextView)findViewById(R.id.textView1);

        //设置监听事件
        defimgbtn_plus.setOnMyClickListener(this);
        defimgbtn_focus.setOnMyClickListener(this);
        defimgbtn_range.setOnMyClickListener(this);
        defimgbtn_measure.setOnMyClickListener(this);
        defimgbtn_note.setOnMyClickListener(this);
        defimgbtn_clear.setOnMyClickListener(this);
        defimgbtn_minus.setOnMyClickListener(this);
        defimgbtn_wave.setOnMyClickListener(this);
        defimgbtn_sound.setOnMyClickListener(this);
        defimgbtn_back.setOnMyClickListener(this);
        defimgbtn_saveimage.setOnMyClickListener(this);
        defimgbtn_savevideo.setOnMyClickListener(this);
        defimgbtn_mode1.setOnMyClickListener(this);
        defimgbtn_mode2.setOnMyClickListener(this);
        defimgbtn_stop.setOnMyClickListener(this);
        defimgbtn_line.setOnMyClickListener(this);
        defimgbtn_patient.setOnMyClickListener(this);
        defimgbtn_edit.setOnMyClickListener(this);

        connect(); //建立连接

        //实例化主线程,用于更新接收过来的消息
        mMainHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 0:
                        try {
                            image_show.setImageBitmap(bmp); //在图片控件上显示图片
                            update_ping();//更新显示的帧数
                        }catch (Exception e)
                        {
                            Log.i("Bmp", "显示失败");
                        }break;
                    case 1:
                    {
//                        edit_recv.setText(line);//显示接收内容
                    }
                    case 2:
                    {
//                        edit_recv.setText(edit_recv.getText().toString() + "断开连接" + "\n");
                    }break;
                    case 3: {
//                        edit_recv.setText(edit_recv.getText().toString() + "连接成功" + "\n");
                    }break;
                    default:break;
                }
            }
        };

        //重写断开连接回调函数
        TaskCenter.sharedCenter().setDisconnectedCallback(new TaskCenter.OnServerDisconnectedCallbackBlock() {
            @Override
            public void callback(IOException e) {
                Message msg = Message.obtain();
                msg.what = 2;
                mMainHandler.sendMessage(msg);
            }
        });

        //重写连接成功回调函数
        TaskCenter.sharedCenter().setConnectedCallback(new TaskCenter.OnServerConnectedCallbackBlock() {
            @Override
            public void callback() {
                Message msg = Message.obtain();
                msg.what = 3;
                mMainHandler.sendMessage(msg);
            }
        });

        //重写接收回调函数
        TaskCenter.sharedCenter().setReceivedCallback(new TaskCenter.OnReceiveCallbackBlock() {
            @Override
            public void callback(byte[] receivedData) {
                int length = receivedData.length;
                if(length > 100) {
                    //处理接收的图片信息
                    try {
                        //解码图片数据
                        bmp = BitmapFactory.decodeByteArray(receivedData, 0, receivedData.length);
                        //通知主线程,将接收的消息显示到界面
                        Message msg = Message.obtain();
                        msg.what = 0;
                        mMainHandler.sendMessage(msg);
                        Log.i("Bmp", "接收图片成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("Bmp", "接收图片失败");
                    }
                }
                else {
                    //处理接收的文本信息
                    try {
                        byte[] bs = new byte[receivedData.length];
                        System.arraycopy(receivedData, 0, bs, 0, receivedData.length); //复制到新的字节数组
                        String str = new String(bs, "UTF-8"); /* UTF-8编码 */
                        //通知主线程,将接收的消息显示到界面
                        Message msg = Message.obtain();
                        msg.what = 1;
                        line = str + "\n";
                        msg.obj = line;
                        mMainHandler.sendMessage(msg);
                        Log.i("Msg", "接收成功");
                    }catch (IOException e) {
                        Log.i("Msg", "接收失败");
                    }
                }
            }
        });
    }


    /* 连接按钮处理函数：建立Socket连接 */
    @SuppressLint("HandlerLeak")
    public void connect() {
        /* 接收上一活动的数据 */
        Intent getlinkaddr = getIntent();
        String getlinkip = getlinkaddr.getStringExtra("linkiip");
        String getlinkport = getlinkaddr.getStringExtra("linkiport");
        String IPAdr = getlinkip;
        int PORT = Integer.parseInt(getlinkport);
        //连接
        TaskCenter.sharedCenter().connect(IPAdr,PORT);
    }

    //断开连接
    public void disconnect() {
        TaskCenter.sharedCenter().disconnect();
    }

    //清除（接收内容）
    public void clear() {
        image_show.setImageBitmap(null);
        disconnect();
    }

    //时间显示
    public class TimeThread extends Thread{
        @Override
        public void run () {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(true);
        }
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    mTime.setText(getTime());
                    break;
                default:
                    break;
            }
        }
    };
    //获得当前年月日时分秒星期
    public String getTime(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));//时
        String mMinute = String.valueOf(c.get(Calendar.MINUTE));//分
        String mSecond = String.valueOf(c.get(Calendar.SECOND));//秒

        if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        return mYear + "-" + mMonth + "-" + mDay+" "+"  "+mHour+":"+mMinute+":"+mSecond+"  "+"星期"+mWay;
    }

    //主页自定义按钮，点击跳转到各功能模块或调节各功能显示
    @Override
    public void onMyClick(int testid) {
        Intent intent = null;

        switch (testid) {
            case FunctionDefine.test_plus:
                String addNum = new String();
                addNum=textView1.getText().toString();
                int addnum = Integer.parseInt(addNum);
                if(addnum>=100) {//暂时设置增益最大值为100
                    return;
                }else{
                    addnum=addnum+1;
                    textView1.setText(String.valueOf(addnum));
                    String msg_GN_plus = "GN" + addnum;
                    TaskCenter.sharedCenter().send(msg_GN_plus.getBytes());
                }
//                intent = new Intent(MainActivity.this, MainActivity.class);
////                显示intent
//                startActivity(intent);
                break;
            case FunctionDefine.test_focus:
                String msg_FC = "focus";
                TaskCenter.sharedCenter().send(msg_FC.getBytes());
//                intent = new Intent(MainActivity.this, FocusActivity.class);
//                startActivity(intent);
                break;
            case FunctionDefine.test_range:
                String msg_DR = "dynamicrange";
                TaskCenter.sharedCenter().send(msg_DR.getBytes());
//                intent = new Intent(MainActivity.this, RangeActivity.class);
//                startActivity(intent);
                break;
            case FunctionDefine.test_measure:
                intent = new Intent(MainActivity.this, MeasureActivity.class);
                startActivity(intent);
                break;
            case FunctionDefine.test_note:
                if(isButton){
                    hastext.setVisibility(View.VISIBLE);
                    isButton = false;
                }else {
                    hastext.setVisibility(View.GONE);
                    isButton = true;
                }
//                intent = new Intent(MainActivity.this, NoteActivity.class);
//                startActivity(intent);
                break;
            case FunctionDefine.test_clear:
                hastext.setText("");
                hastext.setVisibility(View.GONE);//清空文本框内容同时文本框不可见

//                intent = new Intent(MainActivity.this, ClearActivity.class);
//                startActivity(intent);
                break;
            case FunctionDefine.test_minus:
                String Num = new String();
                Num=textView1.getText().toString();
                int cutnum = Integer.parseInt(Num);
                if(cutnum<=0){//设置增益最小值为0
                    return;
                }else{
                    cutnum=cutnum-1;
                    textView1.setText(String.valueOf(cutnum));
                    String msg_GN_minus = "GN" + cutnum;
                    TaskCenter.sharedCenter().send(msg_GN_minus.getBytes());
                }
//                intent = new Intent(MainActivity.this, MinusActivity.class);
//                startActivity(intent);
                break;
            case FunctionDefine.test_wave:
                String msg_WV = "wave";
                TaskCenter.sharedCenter().send(msg_WV.getBytes());
//                intent = new Intent(MainActivity.this, WaveActivity.class);
//                startActivity(intent);
                break;
            case FunctionDefine.test_sound:
                String msg_DN = "denoise";
                TaskCenter.sharedCenter().send(msg_DN.getBytes());
//                intent = new Intent(MainActivity.this, SoundActivity.class);
//                startActivity(intent);
                break;
            case FunctionDefine.test_back:
//                animationDrawable = new AnimationDrawable();
                int i = 0;
                /* 重播帧动画，编译出问题，暂时先注释掉 */
                while (i<100) {
                    i++;
//                    String filename = Environment.getExternalStorageDirectory().getPath() + i + ".png";
//                    Bitmap image_i = BitmapFactory.decodeStream(getClass().getResourceAsStream(filename));
//                    Drawable drawable_i = new BitmapDrawable(image_i);
//                    animationDrawable.addFrame(drawable_i,100);
                }
//                animationDrawable.setOneShot(false);
//                mImgvOne.setBackground(animationDrawable);
//                animationDrawable.start();
//                intent = new Intent(MainActivity.this, BackActivity.class);
//                startActivity(intent);
                break;
            case FunctionDefine.test_saveimage:
                requestAllPower();
//                String path =  Environment.getExternalStorageDirectory().getPath()
//                        + "/mingpian.png";
                String path = "/storage/emulated/0/saveimage.png";
                saveBitmap(image_show, path);

//                intent = new Intent(MainActivity.this, SaveImageActivity.class);
//                startActivity(intent);
                break;
            case FunctionDefine.test_savevideo:
                requestAllPower();
                int j = 0;
                while (j < 0) {
                    j++;
                    String vedio_path =  Environment.getExternalStorageDirectory().getPath()
                            + "/j.png";
//                    String vedio_path = "/storage/emulated/0/j++.png";
                    saveBitmap(image_show, vedio_path);
                }

//                intent = new Intent(MainActivity.this, SaveVideoActivity.class);
//                startActivity(intent);
                break;
            case FunctionDefine.test_mode1:
                String msg_M1 = "mode1";
                TaskCenter.sharedCenter().send(msg_M1.getBytes());
//                intent = new Intent(MainActivity.this, Mode1Activity.class);
//                startActivity(intent);
                break;
            case FunctionDefine.test_mode2:
                String msg_M2 = "mode2";
                TaskCenter.sharedCenter().send(msg_M2.getBytes());
//                intent = new Intent(MainActivity.this, Mode2Activity.class);
//                startActivity(intent);
                break;
            /**
             * 冻结功能实现
             */
            case FunctionDefine.test_stop:
                set_run_stop();//运行和冻结状态切换

//                intent = new Intent(MainActivity.this, StopActivity.class);
//                startActivity(intent);
                break;
            case FunctionDefine.test_line:
                String msg_LN = "line";
                TaskCenter.sharedCenter().send(msg_LN.getBytes());
//                intent = new Intent(MainActivity.this, LineActivity.class);
//                startActivity(intent);
                break;
            case FunctionDefine.test_patient:
                intent = new Intent(MainActivity.this, PatientActivity.class);
                startActivity(intent);
                break;
            case FunctionDefine.test_edit:
                intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    /* 申请权限，保存图片功能需要使用之前申请 */
    private void requestAllPower() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET}, 1);
            }
        }
    }

    /* 运行/冻结 切换 */
    private void set_run_stop() {
        if (stop_flag == false) {
            stop_flag = true;
            mRun.setText("冻结");
        }
        else if (stop_flag == true) {
            stop_flag = false;
            mRun.setText("运行");

            Ping_count = 0; //清零
            String str = String.valueOf(Ping_count);
            mPing.setText(str);//显示帧数
        }
    }

    /* 更新帧数以及保存图片 */
    private void update_ping() {
        Ping_count++; //成功接收一帧就计数
        if(Ping_count > 100)
            Ping_count = 100;

        String str = String.valueOf(Ping_count);
        mPing.setText(str); //显示总的计数

        /* 直接保存图片较慢，会刷新不正常 */
        requestAllPower();//申请权限
        String img_name = str +".png";
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/data/" + img_name;//设置保存的路径（绝对路径）

        saveBitmap(bmp, path);//自动保存图片到本地

    }

    private void saveBitmap(Bitmap bitmap, String filepath) {
        File file = new File(filepath);
        if (file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 10, out)) {
                out.flush();
                out.close();
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    private void saveBitmap(View view, String filePath) {

//        String path =  Environment.getExternalStorageDirectory().getPath()
//                + "/saveimage.png";

        // 创建对应大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);


    /* 图片保存部分 */
        //存储
        FileOutputStream outStream = null;
        File file = new File(filePath);
        if (file.isDirectory()) {//如果是目录不允许保存
            Toast.makeText(MainActivity.this, "该路径为目录路径", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            Toast.makeText(MainActivity.this, "图片保存成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage() + "#");
            Toast.makeText(MainActivity.this, "图片保存失败", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                bitmap.recycle();
                if (outStream != null) {
                    outStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    //自定义刻度尺显示
    @Override
    public void onChange(RulerView view, float value) {
        switch (view.getId()){
            case R.id.height_ruler:
                mTextView.setText( "D:" +" " +value+" mm");
                break;
        }
    }



}
