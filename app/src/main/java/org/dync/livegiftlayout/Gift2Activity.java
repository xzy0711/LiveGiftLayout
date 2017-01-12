package org.dync.livegiftlayout;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.dync.giflibrary.adapter.FaceGVAdapter;
import org.dync.giflibrary.adapter.FaceVPAdapter;
import org.dync.giflibrary.util.ExpressionUtil;
import org.dync.giflibrary.widget.GiftModel;
import org.dync.giflibrary.widget.LeftGiftControl;
import org.dync.giflibrary.widget.LeftGiftsItemLayout;
import org.dync.giflibrary.widget.Viewpager;

import java.util.ArrayList;
import java.util.List;


public class Gift2Activity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private LinearLayout giftLl;

    private LeftGiftsItemLayout gift1;
    private LeftGiftsItemLayout gift2;


    private LinearLayout giftLayout;
    private LinearLayout ll_portrait;
    private LinearLayout ll_landscape;
    private ImageView btnGift;
    private Viewpager mViewPager;
    private LinearLayout mDotsLayout;
    private LayoutInflater inflater;

    private LeftGiftControl leftGiftControl;
    private int columns = 6;
    private int rows = 4;
    //每页显示的表情view
    private List<View> views = new ArrayList<>();
    private String giftstr;
    private RecyclerView rvGift;
    private ExpressionUtil expressionUtil;
    private List<String> staticFacesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift2);
        giftLl = (LinearLayout) findViewById(R.id.giftLl);
        gift1 = (LeftGiftsItemLayout) findViewById(R.id.gift1);
        gift2 = (LeftGiftsItemLayout) findViewById(R.id.gift2);


        initGiftLayout();

        giftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        leftGiftControl = new LeftGiftControl(this);
        leftGiftControl.setGiftsLayout(gift1, gift2);

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftGiftControl.loadGift(GiftModel.create("125", "安卓机器人", 3, "http://www.baidu.com", "123", "Lee125", "http://www.baidu.com"));
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftGiftControl.loadGift(GiftModel.create("123", "安卓机器人", 1, "http://www.baidu.com", "123", "Lee123", "http://www.baidu.com"));
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftGiftControl.loadGift(GiftModel.create("124", "安卓机器人", 1314, "http://www.baidu.com", "123", "Lee124", "http://www.baidu.com"));

            }
        });
    }

    private void initGiftLayout() {
        ll_portrait = (LinearLayout) findViewById(R.id.ll_portrait);
        ll_landscape = (LinearLayout) findViewById(R.id.ll_landscape);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        giftLayout = (LinearLayout) findViewById(R.id.giftLayout);
        btnGift = (ImageView) findViewById(R.id.toolbox_iv_face);

        rvGift = (RecyclerView) findViewById(R.id.rv_gift);
        mViewPager = (Viewpager) findViewById(R.id.toolbox_pagers_face);
        mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);

        btnGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(giftstr)) {
                    Toast.makeText(getApplication(), "你还没选择礼物呢", Toast.LENGTH_SHORT).show();
                } else {
                    leftGiftControl.loadGift(GiftModel.create(giftstr, "安卓机器人", 1, "http://www.baidu.com", "123", "Lee125", "http://www.baidu.com"));
                }
            }
        });
        findViewById(R.id.action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (giftLayout.getVisibility() == View.VISIBLE) {
                    giftLayout.setVisibility(View.GONE);
                } else {
                    giftLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        mViewPager.addOnPageChangeListener(new Gift2Activity.PageChange());
        initViewPager();

        initGiftView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (giftLl != null) {
            leftGiftControl.cleanAll();
        }
    }

    /**************************
     * 礼物面板
     **************************/

    private void initGiftView() {
        if (expressionUtil == null) {
            expressionUtil = new ExpressionUtil();
        }
        if (staticFacesList == null) {
            staticFacesList = expressionUtil.initStaticFaces(this);
        }

        expressionUtil.giftView(this, rvGift, staticFacesList);
    }

    /**
     * 初始化礼物面板
     */
    private void initViewPager() {
        expressionUtil = new ExpressionUtil();
        staticFacesList = expressionUtil.initStaticFaces(this);
        int pagesize = expressionUtil.getPagerCount(staticFacesList.size(), columns, rows);
        // 获取页数
        for (int i = 0; i < pagesize; i++) {
            views.add(expressionUtil.viewPagerItem(this, i, staticFacesList, columns, rows, null));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
            params.setMargins(10, 0, 10, 0);
            mDotsLayout.addView(dotsItem(i), params);
        }
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
        mViewPager.setAdapter(mVpAdapter);
        mViewPager.setCurrentItem(0);
        mDotsLayout.getChildAt(0).setSelected(true);

        expressionUtil.setGiftClickListener(new ExpressionUtil.GiftClickListener() {
            @Override
            public void onClick(int position, String pngStr) {
                giftstr = pngStr;
            }
        });
    }

    /**
     * 表情页切换时，底部小圆点
     *
     * @param position
     * @return
     */
    private ImageView dotsItem(int position) {
        View layout = inflater.inflate(R.layout.dot_image, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
        iv.setId(position);
        return iv;
    }

    private boolean isScrolling = false;
    private boolean left = false;//从右向左，positionOffset值逐渐增大
    private boolean right = false;//从左向右，positionOffset值逐渐减小
    private int lastValue = -1;

    /**
     * 表情页改变时，dots效果也要跟着改变
     */
    class PageChange implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //这里的position是当前屏幕可见页面的第一个页面
            if (isScrolling) {
                if (lastValue > positionOffsetPixels) {
                    //递减，向右滑动
                    right = true;
                    left = false;
                } else if (lastValue < positionOffsetPixels) {
                    //递增，向左滑动
                    right = false;
                    left = true;
                } else if (lastValue == positionOffsetPixels) {
                    right = left = false;
                }
            }
//            Log.i("CustormViewPager", "onPageScrolled: positionOffset =>" + positionOffset + "; positionOffsetPixels =>" + positionOffsetPixels);
//            Log.i("CustormViewPager", "onPageScrolled: right =>" + right + "; left =>" + left);
            lastValue = positionOffsetPixels;
        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
                mDotsLayout.getChildAt(i).setSelected(false);
            }
            mDotsLayout.getChildAt(position).setSelected(true);
            for (int i = 0; i < views.size(); i++) {//清除选中，当礼物页面切换到另一个礼物页面
                RecyclerView view = (RecyclerView) views.get(i);
                FaceGVAdapter adapter = (FaceGVAdapter) view.getAdapter();
                adapter.clearSelection();
                giftstr = "";
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //CustormViewPager.SCROLL_STATE_IDLE 空闲状态 0；CustormViewPager.SCROLL_STATE_DRAGGING 正在滑动 1
            //CustormViewPager.SCROLL_STATE_SETTLING 滑动完毕 2；页面开始滑动时，状态变化（1,2,0）
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                isScrolling = true;
            } else {
                isScrolling = false;
            }
            if (state == ViewPager.SCROLL_STATE_SETTLING) {
//                Log.i("CustormViewPager", "----------------right =>" + right + "; left =>" + left + "----------------------------");
                right = left = false;
//                lastValue = -1;
            }
        }
    }
}