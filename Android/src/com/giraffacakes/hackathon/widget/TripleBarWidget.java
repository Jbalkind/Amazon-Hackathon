package com.giraffacakes.hackathon.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TripleBarWidget extends View {
	private int v1Colour = 0xFFFF4D41;
	private int v2Colour = 0xFFFFC741;
	private int v3Colour = 0xFF52D73A;
	
	private Bitmap render;
	private boolean valid;
	private int v1, v2, v3;
	
	public TripleBarWidget(Context context) {
		super(context);
		init();
	}
	
	public TripleBarWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public TripleBarWidget(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public TripleBarWidget(Context context, int v1, int v2, int v3) {
		super(context);
		init();
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}
	
	private void init() {
		valid = false;
		render = null;
	}
	
	public int getV1() {
		return v1;
	}

	public void setV1(int v1) {
		this.v1 = v1;
		valid = false;
	}

	public int getV2() {
		return v2;
	}

	public void setV2(int v2) {
		this.v2 = v2;
		valid = false;
	}

	public int getV3() {
		return v3;
	}

	public void setV3(int v3) {
		this.v3 = v3;
		valid = false;
	}
	
	public void setValues(int v1, int v2, int v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		valid = false;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (render == null || !valid) {
			createRender();
		}
		
		canvas.drawBitmap(render, 0, 0, null);
	}
	
	private void createRender() {
		int w = getWidth();
		int h = getHeight();
		int sum = v1 + v2 + v3;
		float v1Ratio = (v1 / (float) sum) * w;
		float v2Ratio = ((v2 + v1) / (float) sum) * w;
		
		Log.i("GiraffaCakes", String.format("%.2f, %.2f", v1Ratio, v2Ratio));
		
		Bitmap output = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		
		Paint paint = new Paint();
		
		/* Draw first part of bar */
		paint.setColor(v1Colour);
		RectF r = new RectF(0, 0, v1Ratio, h);
		canvas.drawRect(r, paint);
		
		/* Draw middle part of bar */
		paint.setColor(v2Colour);
		r = new RectF(v1Ratio, 0, v2Ratio, h);
		canvas.drawRect(r, paint);
		
		/* Draw last part of bar */
		paint.setColor(v3Colour);
		r = new RectF(v2Ratio, 0, w, h);
		canvas.drawRect(r, paint);
		
		canvas.save();
		render = output;
	}
}
