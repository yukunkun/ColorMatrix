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
import com.matrix.yukun.matrix.main_module.utils.ToastUtils;
import com.matrix.yukun.matrix.tool_module.weather.bean.OnEventpos;
import com.matrix.yukun.matrix.util.log.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/3/5.
 */
public class CityPosition {
    protected static LocationManager locationManager;
    protected static Context context;
    private static String city = null;
    private static CityPosition getCitys;
    private static LocationListener mLocationListener;
    public static CityPosition getInstance(Context contexts) {
        context = contexts;
        if(getCitys==null){
            getCitys=new CityPosition();
        }
        return getCitys;
    }

    public String getPosition(LocationListener locListener) {
        releaseGPS();
        mLocationListener=locListener;
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
            ToastUtils.showToast("请打开GPS定位");
            return null;
        }
        String provider = getProvider(locationManager);
        LogUtil.i("----city","provider:"+provider+"");
        locationManager.requestLocationUpdates(provider, 10000, 10000, locListener);
        return city;
    }

    /**
     *  gps 提供器
     * @param locationManager
     * @return
     */
    public static String getProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        // gps定位
        if(prodiverlist.contains(LocationManager.GPS_PROVIDER)){
            return LocationManager.GPS_PROVIDER;
        }else if(prodiverlist.contains(LocationManager.PASSIVE_PROVIDER)){
            return LocationManager.PASSIVE_PROVIDER;
        } else if(prodiverlist.contains(LocationManager.NETWORK_PROVIDER)){
            return LocationManager.NETWORK_PROVIDER;
        }
        return null;
    }

    public static String changeToCity(double latitude, double longitude) {
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
            LogUtil.e("----city", e.toString());
            e.toString();
        }
        return null;
    }

    public static void releaseGPS() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ToastUtils.showToast("请打开GPS定位");
                return;
            }
            if(mLocationListener!=null){
                locationManager.removeUpdates(mLocationListener);
            }
        }
        if(mLocationListener!=null){
            mLocationListener=null;
        }
    }
}
