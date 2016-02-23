package com.huilong.zhang.mobilesafe117.View;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huilong.zhang.mobilesafe117.R;

public class DragViewActivity extends Activity {
    private TextView textViewtop;
    private TextView textViewbottom;
    private ImageView imageView;
    private int sStartX;
    private int sEndY;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        textViewtop = (TextView) findViewById(R.id.textviewtop);
        textViewbottom = (TextView) findViewById(R.id.textviewbottom);
        imageView = (ImageView) findViewById(R.id.iv_drag);




        int lastX = sharedPreferences.getInt("top", 0);
        int lastY = sharedPreferences.getInt("left", 0);

        // onMeasure(测量view), onLayout(安放位置), onDraw(绘制)
        // ivDrag.layout(lastX, lastY, lastX + ivDrag.getWidth(),
        // lastY + ivDrag.getHeight());//不能用这个方法,因为还没有测量完成,就不能安放位置

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView
                .getLayoutParams();// 获取布局对象
        layoutParams.leftMargin = lastY;// 设置左边距
        layoutParams.topMargin = lastX;// 设置top边距

        imageView.setLayoutParams(layoutParams);// 重新设置位置

        final int winWidth = getWindowManager().getDefaultDisplay().getWidth();
        final int winHeight = getWindowManager().getDefaultDisplay()
                .getHeight();

        if (lastY > winHeight / 2) {// 上边显示,下边隐藏
            textViewtop.setVisibility(View.VISIBLE);
            textViewbottom.setVisibility(View.INVISIBLE);
        } else {
            textViewtop.setVisibility(View.INVISIBLE);
            textViewbottom.setVisibility(View.VISIBLE);
        }


        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE: //移动状态

                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();

                        int dx = endX - sStartX;          //计算移动偏移量
                        int dy = endY -sEndY;

                        int l = imageView.getLeft() + dx;
                        int r = imageView.getRight() + dx;

                        int t = imageView.getTop() + dy;
                        int b = imageView.getBottom() + dy;



                        // 判断是否超出屏幕边界, 注意状态栏的高度
                        if (l < 0 || r > winWidth || t < 0 || b > winHeight - 20) {
                            break;
                        }

                        // 根据图片位置,决定提示框显示和隐藏
                        if (t > winHeight / 2) {// 上边显示,下边隐藏
                            textViewtop.setVisibility(View.VISIBLE);
                            textViewbottom.setVisibility(View.INVISIBLE);
                        } else {
                            textViewtop.setVisibility(View.INVISIBLE);
                            textViewbottom.setVisibility(View.VISIBLE);
                        }

                        imageView.layout(l,t,r,b);

                        sStartX = (int) event.getRawX();
                        sEndY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_DOWN: //鼠标按下
                        sStartX = (int) event.getRawX();
                        sEndY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:  //鼠标抬起

                        sharedPreferences.edit().putInt("top",imageView.getTop()).commit();
                        sharedPreferences.edit().putInt("left",imageView.getLeft()).commit();


                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drag_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
