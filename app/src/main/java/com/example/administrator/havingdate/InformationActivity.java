package com.example.administrator.havingdate;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 */

public class InformationActivity extends AppCompatActivity{
    public static final String INFORMATION_NAME="information_name";

    public static final String INFORMATION_IMAGE_ID="information_image_id";
    FloatingActionButton fab;
    FamousPeople famousPeople;
@Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_information);


    Intent intent =getIntent();
    final String informationName = intent.getStringExtra(INFORMATION_NAME);
    final String articleImageUrl = intent.getStringExtra("image_url");
    final String articlePre = intent.getStringExtra("idiom_pre");

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)
            findViewById(R.id.collapsing_toolbar);
    ImageView informationImageView = (ImageView)findViewById(R.id.information_image_view);
    TextView informationContentText = (TextView) findViewById(R.id.information_content_text);
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    if(actionBar !=null){
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    collapsingToolbar.setTitle(informationName);
    if(intent.getStringExtra("image_url")
            .equals("http://www.gs5000.cn/images/defaultpic.gif")){
    Glide.with(this).load(R.drawable.android).into(informationImageView);}
    else {Glide.with(this).load(articleImageUrl).into(informationImageView);}
    final String informationContent = generateInformationContent(informationName);
    informationContentText.setText(informationContent);

    fab = (FloatingActionButton) findViewById(R.id.fab);

    List<FamousPeople> famousPeopleList = DataSupport
            .where("title like ?" ,"%"+informationName+"%").find(FamousPeople.class);

    for (FamousPeople famous : famousPeopleList){
        famousPeople =famous;
    }
    if (famousPeople.getLove().equals("true")){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fab.setImageResource(R.drawable.check);
            }
        });

    }



       fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (famousPeople.getLove().equals("true")) {

                    FamousPeople famous =new FamousPeople();
                    famousPeople.setLove("false");
                    famousPeople.updateAll("title = ?" ,informationName);
                    Snackbar.make(v, "取消收藏", Snackbar.LENGTH_SHORT).show();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fab.setImageResource(R.drawable.blank);
                        }
                    });

                }
               else{
                    FamousPeople famous =new FamousPeople();
                    famousPeople.setLove("true");
                    famousPeople.updateAll("title = ?" ,informationName);
                    Snackbar.make(v,"收藏成功",Snackbar.LENGTH_SHORT).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fab.setImageResource(R.drawable.check);
                        }
                    });
                }


            }
        });

    }

    private String generateInformationContent(String informationName){
        StringBuilder informationContent = new StringBuilder();
            switch (informationName){

                default:
                    informationContent.append(getIntent().getStringExtra("idiom_content"));

                    break;
            }

            return informationContent.toString();
    }


@Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

