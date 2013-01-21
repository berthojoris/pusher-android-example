package com.pusher.android.example;

import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.ChannelEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends Activity implements ConnectionEventListener, ChannelEventListener {
	
	private Pusher pusher;
	private Channel channel = null;
	
	private Switch connectionSwitch;
	private TextView logTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		pusher = new Pusher("8817c5eeccfb1ea2d1c6");
		pusher.getConnection().bind(ConnectionState.ALL, this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		logTextView = (TextView)this.findViewById(R.id.loggerText);
		
		// Connect/disconnect depending on switch state
		connectionSwitch = (Switch)this.findViewById(R.id.connectSwitch);
		connectionSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton button, boolean checked) {
				if(checked) {
					pusher.connect();
				}
				else {
					pusher.disconnect();
				}				
			}
		});
		
		log("Application running");
	}

	@Override
	public void onEvent(String channelName, String eventName, String data) {
		String msg = String.format("Event received: [%s] [%s] [%s]", channelName, eventName, data);
		log( msg );
	}

	@Override
	public void onSubscriptionSucceeded(String channelName) {
		String msg = String.format("Subscription succeeded for [%s]", channelName);
		log( msg );
	}

	@Override
	public void onConnectionStateChange(ConnectionStateChange change) {
		String msg = String.format("Connection state changed from [%s] to [%s]",
				change.getPreviousState(), change.getCurrentState() );
		
		log( msg );
	}

	@Override
	public void onError(String message, String code, Exception e) {
		String msg = String.format("Connection error: [%s] [%s] [%s]", message, code, e);
		log(msg);
	}
	
	private void log(String msg) {
		LogTask task = new LogTask(logTextView, msg);
		task.execute();
	}

}

class LogTask extends AsyncTask<Void, Void, Void> {
	
	TextView view;
	String msg;
	
	public LogTask(TextView view, String msg) {
		this.view = view;
		this.msg = msg;
	}

	@Override
	protected Void doInBackground(Void... args) {
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		System.out.println(msg);
		
		String currentLog = view.getText().toString();
		String newLog = msg + "\n" + currentLog;
		view.setText(newLog);
		
		super.onPostExecute(result);
	}
	
}
