package com.example.administrator.havingdate;

import android.content.Intent;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import org.litepal.crud.DataSupport;


import java.util.ArrayList;
import java.util.List;



import km.lmy.searchview.SearchView;

public class FirstActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    SearchView searchView;
    FamousPeople searchFamous;
    private Button downLoadButton ;
    private static boolean isExit=false;
    List<FamousPeople> searchList = new ArrayList<FamousPeople>();
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Flag flag =new Flag(1,"");
        flag.save();

        /*---------------------------搜索栏----------------------*/
         setSearch();

       /*--------------------------------------------------------*/
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        View headview = navView.inflateHeaderView(R.layout.nav_header);
        /*------------------------------------------------*/

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.history:
                        replaceFragment(new Activity1());
                        break;
                    case R.id.my_like:
                        replaceFragment(new Activity2());
                        break;
                    case R.id.nav_author:
                        Intent intent2 = new Intent(FirstActivity.this, AuthorActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_about:
                        Intent intent3 = new Intent(FirstActivity.this, WebActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.count:
                        List<FamousPeople> famousPeopleData ;
                        famousPeopleData = DataSupport.limit(3000).offset(0)
                                .find(FamousPeople.class);
                        Toast.makeText(FirstActivity.this,
                                "一共有"+famousPeopleData.size()+"条数据",Toast.LENGTH_SHORT).show();
                        break;

                }

                mDrawerLayout.closeDrawers();

                return true;
            }
        });


        List<Flag> flags =DataSupport.where("flag like ?"
                ,"%"+"ready"+"%").find(Flag.class);

        if (flags.isEmpty()){
         gotoDialog();}

        replaceFragment(new Activity1());




    /*---------------------------------------------------------------------------*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }

    }

    /*--------------------------------------------------------------------------------------*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
        case android.R.id.home:
        mDrawerLayout.openDrawer(GravityCompat.START);
        break;
            case  R.id.search:
                searchView.open();
          break;



    }
        return true;

    }

    /*--------------------------------------------------------------------------------------*/
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);

        return true;
    }
/*--------------------------------监听返回键--------------------*/
public boolean onKeyDown(int keyCode,KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
             if(searchView.isOpen()){
                 searchView.close();
             }
             else{
                 exit();

             }
        return true;
    }
    return false;
}
/*-------------------------------------显示碎片-------------------------------------------------*/

private void replaceFragment (Fragment fragment){
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(R.id.fragment,fragment);
    transaction.commit();

}
    /*------------------------------------得到碎片----------------------------------------------*/
    /*public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = FirstActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }
*/

private void setSearch(){

    searchView =findViewById(R.id.searchView);
    searchView.defaultState(searchView.CLOSE);

    searchView.setOnSearchActionListener(new SearchView.OnSearchActionListener() {
        @Override
        public void onSearchAction(String s) {




                    searchList = DataSupport
                            .where("title like ?"
                                    ,"%"+searchView.getEditTextView().getText().toString()+"%")
                            .find(FamousPeople.class);

                    for(FamousPeople famousPeople :searchList){
                        int i =0;
                        Log.d("","搜索栏"+famousPeople.getTitle());
                        searchFamous = famousPeople;
                        searchView.addOneHistory(searchFamous.getTitle());

                    }

                searchView.setHistoryItemClickListener(new SearchView.OnHistoryItemClickListener() {
                    @Override
                    public void onClick(String s, int i) {
                        FamousPeople famousIntent =new FamousPeople();
                        List<FamousPeople> famousPeopleList = DataSupport.where("title like ?","%"+s +"%").find(FamousPeople.class);
                       for (FamousPeople famousPeople : famousPeopleList){
                           famousIntent =famousPeople;
                       }
                      Intent intent = new Intent(FirstActivity.this, InformationActivity.class);
                        intent.putExtra(InformationActivity.INFORMATION_NAME, famousIntent.getTitle());
                        intent.putExtra("image_url",  famousIntent.getImageUrl());
                        intent.putExtra("idiom_content",  famousIntent.getContent());
                        FirstActivity.this.startActivity(intent);

                    }
                })  ;

            if(!searchList.isEmpty()) {

            }
            else{
                Toast.makeText(FirstActivity.this,
                        "对不起TAT,没有这条数据",Toast.LENGTH_SHORT).show();
            }
        }
    });





}

private void gotoDialog(){
    Intent intent = new Intent(FirstActivity.this,DownloadActivity.class);
    startActivity(intent);

}

boolean  IfGoToDialog(){

    List<FamousPeople> boolList = DataSupport.findAll(FamousPeople.class);
    if(boolList.isEmpty()){
        return  true;
    }
    return false;
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
         replaceFragment(new Activity1());
            Toast.makeText(getApplicationContext(), "再按一次退出",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
        }
    }

}
