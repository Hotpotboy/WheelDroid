package com.yundongse.network.interfaces;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import com.yundongse.bean.BaseNetBean;

public interface ICallBack {
	//��HttpResponse����ת��ΪBaseNetBean����,������bean
    public abstract void callBack(HttpResponse response) throws JSONException;
}
