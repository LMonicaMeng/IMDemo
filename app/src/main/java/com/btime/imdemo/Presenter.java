package com.btime.imdemo;

import android.text.TextUtils;

public class Presenter implements IPresenter {
    private IView mView;

    public Presenter(IView view){
        this.mView = view;
    }
    @Override
    public void search() {
        //开启页面loading
        String inputString = mView.getInputString();
        if(TextUtils.isEmpty(inputString)){
            //为空直接返回
            return;
        }

        int hashCode = inputString.hashCode();

        IUserService service = new UserService();
        String serviceResult = service.search(hashCode);

        String Result = "Result:"+inputString+"-"+serviceResult;

        //关闭loading
        mView.setInputString(Result);

    }
}
