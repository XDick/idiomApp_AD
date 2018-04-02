package com.example.administrator.havingdate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.litepal.tablemanager.Connector;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/3/19.
 */

public class DownloadActivity extends Activity {

int size;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_dialog_layout);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setFinishOnTouchOutside(false);
        Connector.getDatabase();

        Button button = findViewById(R.id.download_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHtmlFromJsoup();

                 Flag flag;
                 flag =new Flag(2,"ready");
                 flag.save();
                Toast.makeText(DownloadActivity.this, "下拉列表以刷新", Toast.LENGTH_SHORT).show();
                 finish();
            }
        });


    }

    private void getHtmlFromJsoup() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    for (int j = 1; j <= 999; j++) {
                        Document document = Jsoup.
                                connect("http://www.gs5000.cn/gs/lishirenwu/list_116_" + j + ".html")
                                .timeout(50000).get();

                        Elements titleElements = document.getElementsByClass("title");
                        Elements bodyElements = document.getElementsByClass("intro");
                        Elements imgElements = document.getElementsByClass("preview");
                        Elements sizeElements = document.getElementsByClass("preview");
                        Elements contentElements = document.getElementsByClass("preview");


                        Log.d(TAG, "title:" + titleElements
                                .select("a").text());
                        Log.d(TAG, bodyElements.text());
                        Log.d(TAG, "pic:" + "http://www.gs5000.cn" + imgElements.select("img").attr("src"));
                        Log.d(TAG, "content:" + "http://www.gs5000.cn" + contentElements.select("a").attr("href"));


                        for (int i = 0; i < (sizeElements.size()); i++) {
                            Document document2 = Jsoup.
                                    connect("http://www.gs5000.cn"
                                            + contentElements.get(i).attr("href"))
                                    .timeout(0).get();

                            Elements contentElements2 = document2.getElementsByClass("content");

                                Log.d(TAG, "imgUrl:"+imgElements.get(i)
                                        .select("img")
                                        .attr("src"));

                                 String str = contentElements2.select("table").text()
                                         .replace(" ","\n");
                                 String content =str.substring(0,str.indexOf("相关文章推荐："));

                                   FamousPeople famousPeople = new FamousPeople(titleElements.get(i + 1)
                                           .select("a").text(), bodyElements.get(i).text()
                                           , "http://www.gs5000.cn" + imgElements.get(i)
                                           .select("img")
                                           .attr("src")
                                           , content.replace("www.gs5000.cn","")
                                   ,"false");

                                   Log.d(TAG , "图片的地址:"+famousPeople.getImageUrl());

                                   famousPeople.save();//数据库的储存




                            System.out.print(sizeElements.size());
                            if (sizeElements.size() < 10) {
                                break;
                            }
                        }

                    }
                    //  initIdioms();

                }
                catch(Exception e){
                    e.printStackTrace();
                    Log.d(TAG, "访问网络失败了！");

                }

            }
        }).start();


    }

    public boolean onKeyDown(int keyCode,KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            Intent intent = new Intent(DownloadActivity.this,DownloadActivity.class);
            startActivity(intent);

            return true;
        }
        return false;
    }

    }