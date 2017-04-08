package teamlld.nik.uniobuda.hu.walletapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by GERGO on 2017.03.31..
 */

public class GraphView extends View {

    public final static int Max_Values=50;
    protected List<Float> values;
    protected Path path;
    protected Paint paint;

    public GraphView(Context context) {
        super(context);
        Init();
    }

    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public GraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Init();
    }

    protected void Init()  // konstruktor helyett
    {
        values=new ArrayList<>(Max_Values);
        Random rnd=new Random();
        for (int i=0;i<20;i++)
        {
            addValue(rnd.nextFloat());
        }
        path=new Path();
        paint=new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8f);

    }

    public void addValue(float value)
    {
        //TODO grfikonon megjelenő értékek számának tartása, vagy valamilyen felbontás szerinti megjelenítése pl:hónap
        //this.values.remove(0);
        this.values.add(value);
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width=getWidth();
        int height=getHeight();

        float widthStep=width/values.size();
        float heightStep=widthStep;
        float heightOffset=height>>1;

        path.reset();
        path.moveTo(0,-values.get(0) * heightStep + heightOffset);

        for (int i=1;i<values.size();i++)
        {
            path.lineTo(i* widthStep, -values.get(i)*heightStep+heightOffset);
        }

        canvas.drawPath(path, paint);
    }
}
