package com.gc.newsclient.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * @author gc
 */
public class MyApplication extends Application{
	private static Context context;//全局的上下文
	private static Handler mainHandler;//全局的主线程handler
	/**
	 * app的入口函数
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		//初始化COntext
		context = this;
		//初始化mainHandler,在主线程创建的handler就是主线程的handler
		mainHandler = new Handler();
	}
	
	/**
	 * 获取全局的上下文
	 * @return
	 */
	public static Context getContext(){
		return context;
	}
	
	public static Handler getMainHandler(){
		return mainHandler;
	}

}
