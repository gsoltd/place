package myapp.myapps.co.benpro.logic;

import android.content.Context;

import myapp.myapps.co.benpro.database.DAL;

public abstract class BaseLogic {
    protected DAL dal;

    public BaseLogic(Context context){
        dal = new DAL(context);
    }

    public void open(){
        dal.open();
    }

    public void close(){
        dal.close();
    }

}

