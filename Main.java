package app.ue;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Main extends Activity {
	
	
	
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.layout.menu, menu);

		return true;

		}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        
        Object o = this;
        final Intent service = new Intent(this, Updater.class);
        final Intent config = new Intent(this, Config.class);
        
        final Button ok_button = (Button) findViewById(R.id.ok);
        ok_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//Pass variable?
            	//service.putExtra("test", 666);
            	
                // Connect to server
            	
            	startService(service);
            	
            }
        });
        
        final Button cancel_button = (Button) findViewById(R.id.cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Stop service
            	 stopService(service);
            	// Quit
            	 finish();
            }
        });
        
        final Button config_button = (Button) findViewById(R.id.config);
        config_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Connect to server
            	
            	
            	
            	startActivity(config);
            	
            	
            	
            }
        });
        
        final Button refresh_button = (Button) findViewById(R.id.stop);
        refresh_button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		stopService(service);
        		//Bundle b = service.getExtras();
                //b.getString("status");
        		
                //t.setText();
        	}        	
        });
					
        
        
    }
    
    public void service() {
    	//startService(service);
    	//Updater u = new Updater();
    }
}