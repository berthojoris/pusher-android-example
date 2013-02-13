package com.pusher.android.example;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.ChannelEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.pusher.client.util.HttpAuthorizer;

public class MainActivity extends Activity
	implements ConnectionEventListener, ChannelEventListener {
	
	private static final String PUBLIC_CHANNEL_NAME = "a_channel";
	
	private Pusher pusher;
	private Channel publicChannel;
	
	private Switch connectionSwitch;
	private TextView logTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO: login token?
		HttpAuthorizer authorizer = new HttpAuthorizer("http://www.leggetter.co.uk/pusher/pusher-examples/php/authentication/src/private_auth.php");
		PusherOptions options = new PusherOptions().setEncrypted(true).setAuthorizer(authorizer);
		pusher = new Pusher("8817c5eeccfb1ea2d1c6", options);
		
		// bind to all connection events
		pusher.getConnection().bind(ConnectionState.ALL, this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get view for logging
		logTextView = (TextView)this.findViewById(R.id.loggerText);
		
		bindToConnectionSwitch();
		
		log("Application running");
	}
	
	private void subscribeToPublicChannel() {
		publicChannel = pusher.subscribe(PUBLIC_CHANNEL_NAME, this, "some_event");
	}
	
	// Connect/disconnect depending on switch state
	private void bindToConnectionSwitch() {
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
	}
	
	//ConnectionEventListener implementation
	@Override
	public void onConnectionStateChange(ConnectionStateChange change) {
		String msg = String.format("Connection state changed from [%s] to [%s]",
				change.getPreviousState(), change.getCurrentState() );
		
		log( msg );
		
		if(change.getCurrentState() == ConnectionState.CONNECTED ) {
			// TODO: when "auto-reconnection" and "allow subscriptions prior to connections.subscription"
			// is implemented we should only need to subscribe once.
			subscribeToPublicChannel();
		}
	}

	@Override
	public void onError(String message, String code, Exception e) {
		String msg = String.format("Connection error: [%s] [%s] [%s]", message, code, e);
		log(msg);
	}

	// ChannelEventListener implementation
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
	
	// Logging helper method
	private void log(String msg) {
		LogTask task = new LogTask(logTextView, msg);
		task.execute();
	}

}

// Used for logging on the UI thread
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
