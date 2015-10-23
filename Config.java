package app.ue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Config extends Activity {
	
	TextView txt_server;
	TextView txt_port;
	TextView txt_freq;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        
        try {
        FileInputStream in = openFileInput(".uesec");    		
		Properties conf = new Properties();
		
		conf.load(in);
		in.close();
		
        
        txt_server = (TextView)findViewById(R.id.server);
        txt_port = (TextView)findViewById(R.id.port);
        txt_freq = (TextView)findViewById(R.id.freq);
        
        txt_server.setText(conf.getProperty("server"));
        txt_port.setText(conf.getProperty("port"));
        txt_freq.setText(conf.getProperty("interval"));
        
        }
        catch (Exception e)
        {
        	//Config not found?
        	e.printStackTrace();
        }
        
        final Button save_button = (Button) findViewById(R.id.save);
        save_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Connect to server
            	
            	saveConfig(false);
            	
            	
            }
        });
        
        final Button restart_button = (Button) findViewById(R.id.restart);
        restart_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Connect to server
            	
            	saveConfig(true);
            	
            	
            }
        });
        
    }
    
   

    
    public void saveConfig(boolean restart)
    {
    	try {
    		File f = new File(".uesec");
    		if ( !f.exists() )
    		{
    			OutputStream o = openFileOutput(".uesec", Context.MODE_PRIVATE);
        		o.write(' ');
        		o.close();
        		
        		FileInputStream in = openFileInput(".uesec");    		
        		Properties conf = new Properties();
        		
        		conf.load(in);
        		in.close();
        		
        		conf.setProperty("interval", "60000");
        		conf.setProperty("server", "76.249.179.148");
        		conf.setProperty("port", "19876");
        		
        		OutputStream out = openFileOutput(".uesec", Context.MODE_PRIVATE);
        		conf.store(out, "");
        		out.close();
    		}
    		
    		    		
    		FileInputStream in = openFileInput(".uesec");    		
    		Properties conf = new Properties();
    		
    		conf.load(in);
    		in.close();
    		
    		conf.setProperty("interval", ""+txt_freq.getText());
    		conf.setProperty("server", ""+txt_server.getText());
    		conf.setProperty("port", ""+txt_port.getText());
    		
    		OutputStream out = openFileOutput(".uesec", Context.MODE_PRIVATE);
    		conf.store(out, "");
    		out.close();
    		
    		if ( restart )
    		{
	    		final Intent service = new Intent(this, Updater.class);    	    	
	    		stopService(service);
	    		startService(service);
    		}
    		
    		Toast.makeText(this, "Config Saved", Toast.LENGTH_LONG).show();
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    }
      
}