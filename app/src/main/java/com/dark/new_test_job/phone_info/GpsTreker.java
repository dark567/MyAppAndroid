package com.dark.new_test_job.phone_info;

/**
 * Created by dark on 14.04.2016.
 */


        import java.io.UnsupportedEncodingException;
        import java.net.URLEncoder;
        import java.util.Date;
        import java.util.concurrent.ExecutionException;

        import android.app.Activity;
        import android.content.Intent;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.os.Bundle;
        import android.view.Gravity;
        import android.view.View;
        import android.widget.TextView;
        import android.widget.Toast;
        import com.dark.new_test_job.R;

        import ru.yandex.yandexmapkit.MapController;
        import ru.yandex.yandexmapkit.MapView;
        import ru.yandex.yandexmapkit.OverlayManager;
        import ru.yandex.yandexmapkit.utils.GeoPoint;

public class GpsTreker extends Activity {


    TextView tvEnabledGPS;
    TextView tvStatusGPS;
    TextView tvLocationGPS;
    TextView tvEnabledNet;
    TextView tvStatusNet;
    TextView tvLocationNet;

    private LocationManager locationManager;
    StringBuilder sbGPS = new StringBuilder();
    StringBuilder sbNet = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.gps);

//        tvEnabledGPS = (TextView) findViewById(R.id.tvEnabledGPS);
//        tvStatusGPS = (TextView) findViewById(R.id.tvStatusGPS);
//        tvLocationGPS = (TextView) findViewById(R.id.tvLocationGPS);
        tvEnabledNet = (TextView) findViewById(R.id.tvEnabledNet);
        tvStatusNet = (TextView) findViewById(R.id.tvStatusNet);
        tvLocationNet = (TextView) findViewById(R.id.tvLocationNet);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                locationListener);
        checkEnabled();
        }
        catch (Exception e) {
            System.out.println("Необязательный блок, но раз уже написан, ...");
        }

        final MapView mMapView = (MapView) findViewById(R.id.map);
// Получаем MapController
        MapController mMapController = mMapView.getMapController();

// Перемещаем карту на заданные координаты
        mMapController.setPositionAnimationTo(new GeoPoint(49.9914631, 36.231666));

        mMapController.setZoomCurrent(15);

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    public void onClick_gps3(View view/*, Location location*/) {

        final MapView mMapView = (MapView) findViewById(R.id.map);
// Получаем MapController
        MapController mMapController = mMapView.getMapController();
        OverlayManager mOverlayManager = mMapController.getOverlayManager();


        String point = mOverlayManager.getMyLocation().getMyLocationItem().getGeoPoint().toString();
        Integer rpoint = mOverlayManager.getMyLocation().getMyLocationItem().getRectBounds().height();
        Integer rqpoint = mOverlayManager.getMyLocation().getMyLocationItem().getRectBounds().width();
        Integer rqqpoint = mOverlayManager.getMyLocation().getMyLocationItem().getOffsetX();

                Toast toast = Toast.makeText(getApplicationContext(),
                "Координаты! "+point+" / "+rpoint+" / "+rqpoint+" / "+rqqpoint,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();


    }



    public void onClick_gps(/*String provider, Location location*/) {

//        Toast toast = Toast.makeText(getApplicationContext(),
//                "Пароль НЕ верный! "/*+etLName+"/"+pass*/,
//                Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        LinearLayout toastContainer = (LinearLayout) toast.getView();
//        ImageView catImageView = new ImageView(getApplicationContext());
//        toastContainer.addView(catImageView, 0);
//        toast.show();


//        if (location == null)
//            return;
//        else {
//          final MapView mMapView = (MapView) findViewById(R.id.map);
//// Получаем MapController
//            MapController mMapController = mMapView.getMapController();
////
//            double lat = location.getLatitude();
//            double lng = location.getLongitude();
//            GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
//
//
//            if (point != null) {
//                mMapController.setPositionAnimationTo(point);
//                mMapController.setZoomCurrent(15);
//            }
//        }
    }
    public LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
//            if (provider.equals(LocationManager.GPS_PROVIDER)) {
//                tvStatusGPS.setText("Статус: " + String.valueOf(status));
//            } else
            if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                tvStatusNet.setText("Статус: " + String.valueOf(status));
            }
        }
    };

    public void showLocation(Location location) {
        if (location == null)
            return;
//        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
//            tvLocationGPS.setText(formatLocation(location));
//        } else
        if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {
            tvLocationNet.setText(formatLocation(location));
        }
    }

    public String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Координаты: широта = %1$.10f, долгота = %2$.10f, время = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));

    }





//

    private void checkEnabled() {
//        tvEnabledGPS.setText("Статус gps GPS_PROVIDER: "
//                + locationManager
//                .isProviderEnabled(LocationManager.GPS_PROVIDER));
        tvEnabledNet.setText("Статус gps NETWORK_PROVIDER: "
                + locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    public void onClickLocationSettings(View view) {
        startActivity(new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    };

}