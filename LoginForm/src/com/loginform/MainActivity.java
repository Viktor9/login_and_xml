package com.loginform;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private static final String TAG = "UZENET" ;
	private static final String akcio = "bejelentkezes"; //ez kellett meg
	
	Button login , kijelent;
	EditText email , jelszo, status;
	
	//TextView status;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		setup();
	}

	private void setup() {
		email = (EditText) findViewById(R.id.email);
		jelszo = (EditText) findViewById(R.id.jelszo);
		login = (Button) findViewById(R.id.login);
		kijelent = (Button) findViewById(R.id.kijelentkezes);
		status = (EditText) findViewById(R.id.tvstatus);
		login.setOnClickListener(this);
		kijelent.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.login:
		
			login();
			
			break;
		//beiras kijelentkezesre
		case R.id.kijelentkezes:
		
			kijelentkezes();
			
			break;
		}
		
	}

	private void login() {
		// TODO Auto-generated method stub
		try{
			
		CookieStore cookieStore = new BasicCookieStore();	
			
		httpclient = new DefaultHttpClient();
		HttpContext ctx = new BasicHttpContext();
		ctx.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		
		httppost = new HttpPost("http://www.aktivferfi.hu/index.php?bejelentkezes");
		
		nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("akcio", akcio )); 
		nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString().trim() ));
		nameValuePairs.add(new BasicNameValuePair("jelszo", jelszo.getText().toString().trim() ));
				
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		//httppost.setHeader("accept", "application/xml");
		
		response = httpclient.execute(httppost,ctx); // ezt kellene valahol latni
		
		List<Cookie> cookies = cookieStore.getCookies();
		if( !cookies.isEmpty() ){
		    for (Cookie cookie : cookies){
		        String cookieString = cookie.getName() + " : " + cookie.getValue();
		    }
		 }
		        
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		final String response = httpclient.execute(httppost, responseHandler);
		
		status.setText("" + cookies);
		if (response.equalsIgnoreCase("User Found")){
			startActivity(new Intent(MainActivity.this, UserPage.class));
		}
			
		}catch (Exception e){
			//Log.d(TAG, "VmilyenException" ,e);
			e.printStackTrace();
		}  
		
		
	}
	
	private void kijelentkezes(){
		
		try{
			String URL = "http://www.aktivferfi.hu/index.php?akcio=kijelentkezes"; 
			
			//?kijelentkezes a parameter kell elkuldeni
			String SetServerString = "";
			
			HttpClient Client = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(URL);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        SetServerString = Client.execute(httpget, responseHandler);
	        
	        //httppost
	        httpget.setHeader("accept", "application/xml");
	        // cookie jon vissza es a kezolap xml-je
	        
	        status.setText(SetServerString);
	        /*
	        if (Client.equalsIgnoreCase("User Found")){
				startActivity(new Intent(MainActivity.this, UserPage.class));
			}
	        */
		}
		catch(Exception Ex){
			Log.i(TAG, "Ez van");
		}   
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
