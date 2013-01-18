package com.pusher.android.example;

import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.ChannelEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class MainActivity extends Activity implements ConnectionEventListener, ChannelEventListener {
	
	private Pusher pusher;
	private Channel channel = null;
	
	private Switch connectionSwitch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		pusher = new Pusher("8817c5eeccfb1ea2d1c6");
		pusher.getConnection().bind(ConnectionState.ALL, this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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

	@Override
	public void onEvent(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSubscriptionSucceeded(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionStateChange(ConnectionStateChange change) {
		System.out.println(
				String.format("Connection state changed from [%s] to [%s]",
						change.getPreviousState(), change.getCurrentState() ) );
		
	}

	@Override
	public void onError(String arg0, String arg1, Exception arg2) {
		// TODO Auto-generated method stub
	}

}
