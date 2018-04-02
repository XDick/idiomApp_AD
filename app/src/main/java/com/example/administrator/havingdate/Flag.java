package com.example.administrator.havingdate;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/3/22.
 */

public class Flag extends DataSupport{

    String flag;
    int ID;

    Flag(int ID ,String flag){
        this.flag = flag;
        this.ID= ID;
    }



   String getFlag(){return flag;}

}
