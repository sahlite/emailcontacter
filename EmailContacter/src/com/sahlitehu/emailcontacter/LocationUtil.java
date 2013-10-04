package com.sahlitehu.emailcontacter;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationUtil {
	public interface LocationUpdate{
		void update(boolean isnewdata);
	};
	
	private Location current_location = null;
	private LocationUpdate callback = null;
	
	// Define a listener that responds to location updates
	LocationListener locationListener = new LocationListener() {
	    public void onLocationChanged(Location location) {
	      // Called when a new location is found by the network location provider.
//	      makeUseOfNewLocation(location);
	    	if (null == current_location){
	    		current_location = location;
	    	}else{
	    		if (isBetterLocation(location, current_location)){
	    			current_location = location;
	    		}
	    	}
	    	callback.update(true);
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {}

	    public void onProviderEnabled(String provider) {}

	    public void onProviderDisabled(String provider) {}
	  };
	  
	  public void setCallback(LocationUpdate cb){
		  callback = cb;
	  }
	  
	  public void start(Context context){
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		
		String locationProvider = LocationManager.NETWORK_PROVIDER;
		// Or use LocationManager.GPS_PROVIDER

		current_location = locationManager.getLastKnownLocation(locationProvider);
	  }
	  
	  public void stop(Context context){
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			
		// Remove the listener you previously added
		locationManager.removeUpdates(locationListener);
	  }
	  
	  private static final int TWO_MINUTES = 1000 * 60 * 2;

	  /** Determines whether one Location reading is better than the current Location fix
	    * @param location  The new Location that you want to evaluate
	    * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	    */
	  protected boolean isBetterLocation(Location location, Location currentBestLocation) {
	      if (currentBestLocation == null) {
	          // A new location is always better than no location
	          return true;
	      }

	      // Check whether the new location fix is newer or older
	      long timeDelta = location.getTime() - currentBestLocation.getTime();
	      boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	      boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	      boolean isNewer = timeDelta > 0;

	      // If it's been more than two minutes since the current location, use the new location
	      // because the user has likely moved
	      if (isSignificantlyNewer) {
	          return true;
	      // If the new location is more than two minutes older, it must be worse
	      } else if (isSignificantlyOlder) {
	          return false;
	      }

	      // Check whether the new location fix is more or less accurate
	      int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	      boolean isLessAccurate = accuracyDelta > 0;
	      boolean isMoreAccurate = accuracyDelta < 0;
	      boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	      // Check if the old and new location are from the same provider
	      boolean isFromSameProvider = isSameProvider(location.getProvider(),
	              currentBestLocation.getProvider());

	      // Determine location quality using a combination of timeliness and accuracy
	      if (isMoreAccurate) {
	          return true;
	      } else if (isNewer && !isLessAccurate) {
	          return true;
	      } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	          return true;
	      }
	      return false;
	  }

	  /** Checks whether two providers are the same */
	  private boolean isSameProvider(String provider1, String provider2) {
	      if (provider1 == null) {
	        return provider2 == null;
	      }
	      return provider1.equals(provider2);
	  }
	  
	  public Location getLocation(){
		  return current_location;
	  }
}
