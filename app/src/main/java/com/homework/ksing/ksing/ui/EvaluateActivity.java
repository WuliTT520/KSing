package com.homework.ksing.ksing.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.homework.ksing.ksing.R;
import com.homework.ksing.ksing.activity.KgLoginActivity;
import com.homework.ksing.ksing.adapter.CommentExpandAdapter;
import com.homework.ksing.ksing.adapter.MyImageView;
import com.homework.ksing.ksing.bean.CommentBean;
import com.homework.ksing.ksing.bean.CommentDetailBean;
import com.homework.ksing.ksing.bean.ReplyDetailBean;
import com.homework.ksing.ksing.controller.MyURL;
import com.homework.ksing.ksing.ui.main.MyAdapter;
import com.homework.ksing.ksing.view.CommentExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * by moos on 2018/04/20
 */
public class EvaluateActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EvaluateActivity";
    private android.support.v7.widget.Toolbar toolbar;
    private TextView bt_comment;
    private CommentExpandableListView expandableListView;
    private CommentExpandAdapter adapter;
    private CommentBean commentBean;
    private List<CommentDetailBean> commentsList;
    private BottomSheetDialog dialog;
    private MyImageView songbg;
    private String state_code;
    private String song_picture;
    private SharedPreferences sp;
    private MyURL myURL=new MyURL();
    private String testJson = "{\n" +
            "\t\"code\": 1000,\n" +
            "\t\"message\": \"查看评论成功\",\n" +
            "\t\"data\": {\n" +
            "\t\t\"total\": 3,\n" +
            "\t\t\"list\": [{\n" ;

    private TextView myName;
    private TextView myText;
    private TextView songName;
    private CircleImageView myDp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_evaluate);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        System.out.println(testJson);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        state_code=intent.getExtras().getString("state_code");
        song_picture=intent.getExtras().getString("song_dp");
        getInChildThread(myURL.getURL()+"/getDynamicStateInfo");

        myName=findViewById(R.id.detail_page_userName);
        myText=findViewById(R.id.detail_page_story);
        songName=findViewById(R.id.detail_page_title);
        myDp=findViewById(R.id.detail_page_userLogo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        songbg=findViewById(R.id.detail_page_image);
        expandableListView = (CommentExpandableListView) findViewById(R.id.detail_page_lv_comment);
        bt_comment = (TextView) findViewById(R.id.detail_page_do_comment);
        bt_comment.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("详情");
        commentsList = generateTestData();
        initExpandableListView(commentsList);
        songbg.setImageURL(myURL.getURL()+song_picture);
        songName.setText(intent.getExtras().getString("songName"));


    }

    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(final List<CommentDetailBean> commentList){
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new CommentExpandAdapter(this, commentList);
        expandableListView.setAdapter(adapter);
        for(int i = 0; i<commentList.size(); i++){
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                Log.e(TAG, "onGroupClick: 当前的评论id>>>"+commentList.get(groupPosition).getId());
//                if(isExpanded){
//                    expandableListView.collapseGroup(groupPosition);
//                }else {
//                    expandableListView.expandGroup(groupPosition, true);
//                }
                showReplyDialog(groupPosition);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(EvaluateActivity.this,"点击了回复", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //toast("展开第"+groupPosition+"个分组");

            }
        });

    }

    /**
     * by moos on 2018/04/20
     * func:生成测试数据
     * @return 评论数据
     */
    private List<CommentDetailBean> generateTestData(){
        Gson gson = new Gson();
        commentBean = gson.fromJson(testJson, CommentBean.class);
        List<CommentDetailBean> commentList = commentBean.getData().getList();
        return commentList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();



        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.detail_page_do_comment){

            showCommentDialog();
        }
    }

    /**
     * by moos on 2018/04/20
     * func:弹出评论框
     */
    private void showCommentDialog(){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){

                    //commentOnWork(commentContent);
                    dialog.dismiss();
                    CommentDetailBean detailBean = new CommentDetailBean(myName.getText().toString(), commentContent,"刚刚");
                    detailBean.setUserLogo(myURL.getURL()+myDp);
                    adapter.addTheCommentData(detailBean);

                    addComment(myURL.getURL()+"/addComment",commentContent);

                    Toast.makeText(EvaluateActivity.this,"评论成功", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(EvaluateActivity.this,"评论内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * by moos on 2018/04/20
     * func:弹出回复框
     */
    private void showReplyDialog(final int position){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(position).getNickName() + " 的评论:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){

                    dialog.dismiss();
                    ReplyDetailBean detailBean = new ReplyDetailBean(myName.getText().toString(),replyContent);
                    adapter.addTheReplyData(detailBean, position);
                    expandableListView.expandGroup(position);
                    Toast.makeText(EvaluateActivity.this,"回复成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(EvaluateActivity.this,"回复内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }



    private void getInChildThread(String url) {

        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        //post请求来获得数据
        //创建一个RequestBody，存放重要数据的键值对
        RequestBody body = new FormBody.Builder()
                .add("state_code",state_code).build();
        //创建一个请求对象，传入URL地址和相关数据的键值对的对象
        final Request request = new Request.Builder().addHeader("cookie",sp.getString("sessionID",""))
                .url(url)
                .post(body).build();

        //创建一个能处理请求数据的操作类
        Call call = client.newCall(request);

        //使用异步任务的模式请求数据
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG","错误信息：" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                JSONObject data = null;
                try {
                    data = new JSONObject(result);
                    myName.setText(data.getString("name"));
                    myText.setText(data.getString("user_text"));

                    JSONArray dateArray=data.getJSONArray("comments");
                    for(int i=0;i<dateArray.length();i++)
                    {
                        JSONObject jsonObject=dateArray.getJSONObject(i);
                        String user_dp=myURL.getURL()+jsonObject.getString("friend_dp");
                        System.out.println(user_dp);
                        if(i==dateArray.length()-1){
                            testJson+="\t\t\t\t\"id\": 40,\n" +
                                    "\t\t\t\t\"nickName\": \""+jsonObject.getString("friend_name")+"\",\n" +
                                    "\t\t\t\t\"userLogo\": \""+user_dp+"\",\n" +
                                    "\t\t\t\t\"content\": \""+jsonObject.getString("text")+"\",\n" +
                                    "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
                                    "\t\t\t\t\"replyTotal\": 0,\n" +
                                    "\t\t\t\t\"createDate\": \"三天前\"\n" +

                                    "\t\t\t}\n" +
                                    "\t\t]\n" +
                                    "\t}\n" +
                                    "}";
                            break;
                        }
                        testJson+="\t\t\t\t\"id\": 42,\n" +
                                "\t\t\t\t\"nickName\": \""+jsonObject.getString("friend_name")+"\",\n" +
                                "\t\t\t\t\"userLogo\": \""+user_dp+"\",\n" +
                                "\t\t\t\t\"content\": \""+jsonObject.getString("text")+"\",\n" +
                                "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
                                "\t\t\t\t\"replyTotal\": 1,\n" +
                                "\t\t\t\t\"createDate\": \"三分钟前\"\n" +"\t\t\t},\n" +
                            "\t\t\t{\n" ;





                    }

                    System.out.println(testJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(data);




            }
        });





    }


    private void addComment(String url,String commentContent) {

        sp=this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        //post请求来获得数据
        //创建一个RequestBody，存放重要数据的键值对
        RequestBody body = new FormBody.Builder()
                .add("state_code",state_code)
                .add("text",commentContent).build();
        //创建一个请求对象，传入URL地址和相关数据的键值对的对象
        final Request request = new Request.Builder().addHeader("cookie",sp.getString("sessionID",""))
                .url(url)
                .post(body).build();

        //创建一个能处理请求数据的操作类
        Call call = client.newCall(request);

        //使用异步任务的模式请求数据
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG","错误信息：" + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {






            }
        });





    }

}
