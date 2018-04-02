package com.example.administrator.havingdate;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import km.lmy.searchview.SearchView;

import static android.content.ContentValues.TAG;


public class Activity1 extends Fragment {

    private SwipeRefreshLayout swipeRefresh;

    private Document document2;
    private Document document;
    private List<FamousPeople> famousPeopleList = new ArrayList<>();
    private FamousPeopleAdapter adapter;
    private Elements sizeElements;
    private int ListSize;
    private View rootView;//缓存Fragment view
    boolean notFinish = true;


    /*--------------------------------------------------------*/
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_layout1, container, false);

        Log.d(TAG, "看看碎片1有没有运行");


/*------------------------------------数据库储存-----------------------*/

        Connector.getDatabase();

/*-----------------------------列表--------------------------------------*/
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FamousPeopleAdapter(famousPeopleList);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "列表1生成的代码");

        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshInformations();
            }
        });
        initIdioms();

        return rootView;
    }
    /*--------------------------实现刷新功能---------------------------*/

    private void refreshInformations() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ((AppCompatActivity) getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initIdioms();
                        Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
    /*------------------------------------------------------------------------*/


    private void initIdioms() {
        famousPeopleList.clear();
        List<FamousPeople> famousPeopleData;
        famousPeopleData = DataSupport.limit(3000).offset(0)
                .find(FamousPeople.class);
        Collections.shuffle(famousPeopleData);//使列表乱序
        for (FamousPeople idiom : famousPeopleData) {
            famousPeopleList.add(idiom);
            Log.d(TAG, idiom.getTitle());
        }

        ListSize = famousPeopleData.size();
        Log.d(TAG, "列表大小" + ListSize);
    }



}

