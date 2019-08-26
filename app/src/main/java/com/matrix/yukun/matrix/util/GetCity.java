package com.matrix.yukun.matrix.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.matrix.yukun.matrix.MyApp;
import com.matrix.yukun.matrix.tool_module.weather.bean.OnEventpos;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/3/5.
 */
public class GetCity {
    protected static LocationManager locationManager;
    protected static Context context;
    private static String city = null;
    private locListener listener;
    private static GetCity getCitys;

    public static GetCity getInstance(Context contexts) {
        context = contexts;
        if(getCitys==null){
            getCitys=new GetCity();
        }
        return getCitys;
    }

    public String getCity() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            MyApp.showToast("请打开GPS定位");
            return null;
        }
        listener = new locListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        return city;
    }

    public class locListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            city = toCity(location.getLatitude(), location.getLongitude());
            EventBus.getDefault().post(new OnEventpos(city));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.i("----city", "onStatusChanged");

        }

        @Override
        public void onProviderEnabled(String s) {
            Log.i("----city", "onProviderEnabled");

        }

        @Override
        public void onProviderDisabled(String s) {
            Log.i("----city", "onProviderDisabled");

        };

    }

    public static String toCity(double latitude, double longitude) {
        Geocoder gc = new Geocoder(context, Locale.getDefault());
        try {
            // 取得地址相关的一些信息\经度、纬度
            List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                sb.append(address.getLocality()).append("\n");
                return sb.toString();
            }
        } catch (IOException e) {

        }
        return null;
    }

    public void endGPS() {
        if (locationManager != null&&listener!=null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                MyApp.showToast("请打开GPS定位");
                return;
            }
            locationManager.removeUpdates(listener);
        }
        if(listener!=null){
            listener=null;
        }
    }
}
