package com.zhanghang.wheeldroid;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

public class WheelDroidView extends View {
	private final int MESSAGE_SCROLLER = 1;
	
	private final int TEXT_SIZE = 36;
	private int scrollerOffset = 0;
	
	private ArrayList<IOnWheelViewChangedListener> mListener;
	
	private Handler scrollerHander = new Handler(){//scroller����ʱ��Ϣ������
		@Override
		public void handleMessage(Message msg){
			if(msg.what==MESSAGE_SCROLLER){
				dealComputeScroll();
				if(!scroller.isFinished()){
					this.sendEmptyMessage(MESSAGE_SCROLLER);
				}else{
					invalidate();//������ͼ
				}
			}
		}
	};
	private int lastScrollerY=0;
	private Scroller scroller;//������
    private SimpleOnGestureListener simple = new SimpleOnGestureListener(){
//    	float startY = 0;
//    	float preCurItem=curItemIndex;
    	@Override
    	public boolean onDown(MotionEvent e){
    		if(!scroller.isFinished()){
    			scroller.forceFinished(true);//ǿ��ֹͣ����
    		}
//    		startY = valueLayout.getLineTop(0);
    		return true;
    	}
    	
    	@Override
    	public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
    		scrollerOffset+=(-distanceY);
    		int count =(int) -scrollerOffset/getItemHeight();//���ӵ�item����
    		int pos = curItemIndex+count;
    		if(pos<=0) pos=0;
    		else if(pos>=adapter.getCount()) pos=adapter.getCount()-1;
    		
    		if(pos!=curItemIndex){
    			int cur = curItemIndex;
    			setCurItem(pos);
    			if(mListener.size()>0){//��Ӧֵ�ı��¼�
    				for(IOnWheelViewChangedListener l:mListener){
    					boolean result = l.onChanged(cur, pos);
    					if(result) break;
    				}
    			}
    		}
    		return true;
    	}
    	@Override
    	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
    		lastScrollerY = curItemIndex*getItemHeight();
//    		startY+=(curItemIndex-preCurItem)*getItemHeight();
    		int maxY = adapter.getCount()*getItemHeight();
    		scroller.fling(0, (int)lastScrollerY, 0, (int)velocityY, 0, 0, 0, maxY);//��    ����
    		scrollerHander.removeMessages(MESSAGE_SCROLLER);
    		scrollerHander.sendEmptyMessage(MESSAGE_SCROLLER);
    		return true;
    	}
    };
	private GestureDetector gestureDetector;
    
	private StaticLayout topTextLayout;//�ϲ��ֲ���
	private StaticLayout valueLayout;//��ѡ�񲼾�
	private Drawable centerDrawable;//��ѡ������
	private StaticLayout bottomTextLayout;//�²��ֲ���
	
	private TextPaint noMidPaint;//��ѡ�񻭱�
	private TextPaint midPaint;//��ѡ�񻭱�
	
	
	private int curItemIndex=0;//��ǰ��ʾ����
	private int visibleItem = 5;//Ĭ�Ͽɼ���Ϊ5
	
	private Context context;
	
	private WheelDroidAdapter adapter;

	public WheelDroidView(Context c,AttributeSet attrs) {
		super(c,attrs);
		context = c;
		initView();
	}
	
	public void setAdapter(WheelDroidAdapter a){
		this.adapter = a;
	}
	
	public WheelDroidAdapter getAdapter(){
		return this.adapter;
	}
	
	private int getItemHeight(){
		return topTextLayout.getLineTop(1)-topTextLayout.getLineTop(0);
	}
	
	private void initView(){
		
		//���ʳ�ʼ��
		noMidPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		noMidPaint.setTextSize(TEXT_SIZE);
		
		midPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
                | Paint.FAKE_BOLD_TEXT_FLAG | Paint.DITHER_FLAG);
		midPaint.setTextSize(TEXT_SIZE);
		midPaint.setColor(Color.RED);
        midPaint.setShadowLayer(0.1f, 0, 0.1f, 0xFFC0C0C0);
        
        if(centerDrawable==null){
        	centerDrawable = this.getResources().getDrawable(R.drawable.wheel_val);
        }
        gestureDetector = new GestureDetector(context,this.simple);//��ʼ�����ƴ���
        gestureDetector.setIsLongpressEnabled(false);//���ó�������
        scroller = new Scroller(context);//��ʼ��������
        
        //�������б�
        mListener = new ArrayList<IOnWheelViewChangedListener>();
	}
	
	public void setOnWheelViewChangedListener(IOnWheelViewChangedListener l){
		this.mListener.add(l);
	}
	
	//��ղ���
	private void cleanLayout(){
		topTextLayout=null;
		valueLayout=null;
		bottomTextLayout = null;
		scrollerOffset = 0;
	}
	
	//���ز��������text
	private String[] bulidText(int cur){
		String[] result = {"","",""};
		result[1]=this.adapter.getItem(cur).toString();//��ѡ��
		//��ѡ��,�ϲ��ֵ���
		int up_limit = cur-visibleItem/2;
		if(cur>0){
			for(int i = cur-1;i>=up_limit;i--){
				String str = "";
				if(i>=0) str=this.adapter.getItem(i).toString();
				result[0]=str+"\n"+result[0];
			}
		}else if(cur==0){
			result[0]="";
		}
		//��ѡ��²��ֵ���
		int down = cur+visibleItem/2;
		if(cur<this.adapter.getCount()-1){
			for(int i = cur+1;i<this.adapter.getCount()&&i<=down;i++){
				result[2]+=this.adapter.getItem(i).toString()+"\n";
			}
		}else if(cur==this.adapter.getCount()-1){
			result[2]="";
		}
		return result;
	}
	
	//��ʼ������
	private void initLayout(){
		String[] texts = this.bulidText(curItemIndex);//���ݵ�ǰ��ʾ����Ŀ���ɶ�Ӧ���ַ�����
		topTextLayout=new StaticLayout(texts[0], noMidPaint, this.getWidth(), Layout.Alignment.ALIGN_CENTER, 1F, 30, false);
		valueLayout=new StaticLayout(texts[1], midPaint, this.getWidth(), Layout.Alignment.ALIGN_CENTER, 1F, 30, false);
		bottomTextLayout = new StaticLayout(texts[2], noMidPaint, this.getWidth(), Layout.Alignment.ALIGN_CENTER, 1F, 30, false);
		
	}
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		 int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//	     int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	     int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	     int heightSize = MeasureSpec.getSize(heightMeasureSpec);
	     this.setMeasuredDimension(widthSize, heightSize);
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		cleanLayout();//�������
		initLayout();//��ʼ������
		drawCenterRect(canvas);//������ѡ������
		drawTopArea(canvas);//�ϲ�������
		drawValueArea(canvas);//��ѡ������
		drawButtomArea(canvas);//�²�������
		Paint paint = new Paint();
	        // ���߿���Ϊ��ɫ.
	    paint.setColor(Color.BLACK);
	    paint.setTextSize(16);
	    int center = this.getHeight() / 2;
	    int offset = this.getItemHeight() / 2;
	    canvas.drawLine(0, center - offset, this.getWidth(), center - offset,
	                paint);
	    canvas.drawLine(0, center + offset, this.getWidth(), center + offset,
	                paint);
	}
	
	 /**
     * Draws rect for current value
     * 
     * @param canvas
     *            the canvas for drawing
     */
    private void drawCenterRect(Canvas canvas) {
        int center = this.getHeight() / 2;
        int offset = this.getItemHeight()/2;
        centerDrawable.setBounds(0, center - offset, getWidth(), center
                + offset);
        centerDrawable.draw(canvas);
    }
    //������ѡ�������ϲ�������
    private void drawTopArea(Canvas canvas){
    	canvas.save();
    	Rect bound = new Rect();
    	int center = this.getHeight() / 2;
        int offset = this.getItemHeight()/2;
        int line_spcaing = topTextLayout.getLineTop(1)-topTextLayout.getLineTop(0);
        int top_height = center - offset-2*line_spcaing;
    	canvas.translate(0, top_height+scrollerOffset);
    	noMidPaint.drawableState = this.getDrawableState();
    	topTextLayout.draw(canvas);
    	canvas.restore();
    }
    
    //������ѡ������
    private void drawValueArea(Canvas canvas){
    	 canvas.save();
    	 int center = this.getHeight() / 2;
         int offset = this.getItemHeight()/2;
         canvas.translate(0, center-offset+scrollerOffset);
         midPaint.drawableState = this.getDrawableState();
         valueLayout.draw(canvas);
         canvas.restore();
    }
    
  //������ѡ���²�������
    private void drawButtomArea(Canvas canvas){
    	 canvas.save();
    	 int center = this.getHeight() / 2;
         int offset = this.getItemHeight()/2;
         canvas.translate(0, center+offset+scrollerOffset);
         bottomTextLayout.draw(canvas);
         canvas.restore();
    }
    
    //���õ�ǰѡ��
    public void setCurItem(int cur){
    	this.curItemIndex = cur;
    	this.invalidate();//���²���
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!gestureDetector.onTouchEvent(event)
                && event.getAction() == MotionEvent.ACTION_UP) {
        }
        return true;
    }
    
    //ͨ����Ϣ���ƴ��������̣�ʵʱ���£�
    public void dealComputeScroll(){
    	    scroller.computeScrollOffset();
    		int curY = scroller.getCurrY();
    		int delate = lastScrollerY - curY;
    		lastScrollerY=curY;
    		if(delate!=0){
    			scrollerOffset+=delate;
        		int pos = (scrollerOffset)/this.getItemHeight();//�Զ�����ʱ��ʵʱ���¹����ĵ�ǰ��
    			pos+=curItemIndex;
        		//����Ƿ񳬳��������
        		if(pos<=0) pos=0;
        		else if(pos>=adapter.getCount()) pos=adapter.getCount()-1;
        		if(pos!=curItemIndex){
        			this.setCurItem(pos);
        		}
    		}
    		
    		if(Math.abs(curY-scroller.getFinalY())<1){
    			curY = scroller.getFinalY();
    			scroller.forceFinished(true);//��������
    		}
    	
    }
    //��ȡѡ��ֵ
    public String getCurItemValue(){
    	return this.adapter.getItem(curItemIndex).toString();
    }
}
