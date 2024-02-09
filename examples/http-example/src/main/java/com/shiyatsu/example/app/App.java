package com.shiyatsu.example.app;

import com.shiyatsu.example.app.http.AppHttp;
import com.shiyatsu.example.app.logger.LoggerApp;

public class App {

	public static void main(String[] args) {
		AppHttp.exec();
		LoggerApp.exec();
	}

}
