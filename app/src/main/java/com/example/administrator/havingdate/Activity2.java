package com.example.administrator.havingdate;

import android.app.Fragment;
import android.os.Bundle;
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

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/3/22.
 */

public class Activity2 extends android.support.v4.app.Fragment {
    private View rootView;
    private SwipeRefreshLayout swipeRefresh;
    private FamousPeopleAdapter adapter;
    List<FamousPeople> famousPeopleList=new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_layout1, container, false);

        FamousPeople famousPeople;


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

        init();

        return rootView;
    }


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
                        init();
                        Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void init() {
        famousPeopleList.clear();
        Log.d(TAG ,"我的收藏初始化");
        List<FamousPeople> famousPeopleData = DataSupport
                .where("love like ?", "%" + "true" + "%")
                .find(FamousPeople.class);

        for (FamousPeople famous : famousPeopleData) {
            famousPeopleList.add(famous);
            Log.d(TAG ,"收藏列表"+famous.getTitle());
        }


    }

}