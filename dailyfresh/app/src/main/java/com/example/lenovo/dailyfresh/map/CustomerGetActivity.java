package com.example.lenovo.dailyfresh.map;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.lenovo.dailyfresh.R;
import com.example.lenovo.dailyfresh.shop_db.Shop;
import com.example.lenovo.dailyfresh.shop_db.ShopAdapter;
import com.example.lenovo.dailyfresh.shop_db.ShopDao;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
//import org.crazyit.map.SearchShopActivity;
//import org.crazyit.map.Shop;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;

import java.util.ArrayList;
import java.util.List;

public class CustomerGetActivity extends AppCompatActivity implements OnGeocodeSearchListener, OnRouteSearchListener {
    private List<Shop> list;
    private ShopDao shopdao;
    private TextView eName;
    private TextView eBalance;
    private ListView listView;
    private ShopAdapter shopAdapter;
    private Shop f;

    private MapView mapView;
    private AMap aMap;
    private LocationManager locMgr;
    private Button navBtn;
    private double longti;
    private double lati;
    private boolean flag;
    GeocodeSearch search;
    RouteSearch routeSearch;
    Location loca;
    LatLng[] poses = new LatLng[3];


    public void clearlist(){
        list.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        initView();
        shopdao = new ShopDao(this);
        list = shopdao.getAll();
        shopAdapter = new ShopAdapter(this, R.layout.shop_item, list);
        listView.setAdapter(shopAdapter);
        clearlist();
        addd("北航合一", "东");
        addd("北航大运村", "南");
        addd("北航博彦", "西");


        // 建图
        mapView = (MapView) findViewById(R.id.map_navi);
        // 必须回调MapView的onCreate()方法
        mapView.onCreate(savedInstanceState);
        // 建图
        init();
        locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // check permission
        if (ActivityCompat.checkSelfPermission(CustomerGetActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CustomerGetActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomerGetActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            return;
        }
        loca = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // 创建GeocodeSearch对象
        LocationListener locLis = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // 使用GPS提供的定位信息来更新位置
                updatePosition(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

                updatePosition(loca);
            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        search = new GeocodeSearch(this);
        search.setOnGeocodeSearchListener(this);
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);

        poses[0] = new LatLng(39.990865, 116.355356);
        poses[1] = new LatLng(39.982638, 116.350424);
        poses[2] = new LatLng(39.987849, 116.351565);
        if (ActivityCompat.checkSelfPermission(CustomerGetActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CustomerGetActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomerGetActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            return;
        }
        locMgr.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 300, 1, locLis);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // {Some Code}
                    Toast.makeText(this, "成功获取location权限！", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // 初始化AMap对象
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            // 创建一个设置放大级别的CameraUpdate
            CameraUpdate cu = CameraUpdateFactory.zoomTo(16);
            // 设置地图的默认放大级别
            aMap.moveCamera(cu);
        }
    }

    private void updatePosition(Location location) {
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        // 创建一个设置经纬度的CameraUpdate
        CameraUpdate cu = CameraUpdateFactory.changeLatLng(pos);
        // 更新地图的显示区域
        aMap.moveCamera(cu);
        // 清除所有Marker等覆盖物
        aMap.clear();
        MarkerOptions markerOptions = new MarkerOptions()
                .position(pos)
                // 设置使用自定义图标
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bullet_red))
                .draggable(true);
        // 添加Marker
        aMap.addMarker(markerOptions);
        // target的latlag
        LatLng pos_tar = new LatLng(lati, longti);
        MarkerOptions markerOptions_target = new MarkerOptions()
                .position(pos_tar)
                // 设置使用自定义图标
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bullet_red))
                .draggable(true);
        if (flag == true) {
            aMap.addMarker(markerOptions_target);
        }
    }

    private void updatePosition(double lati, double longti) {
        LatLng pos = new LatLng(lati, longti);
        // 创建一个设置经纬度的CameraUpdate
        CameraUpdate cu = CameraUpdateFactory.changeLatLng(pos);
        // 更新地图的显示区域
        aMap.moveCamera(cu);
        // 清除所有Marker等覆盖物
        if (flag == false || flag) {
            aMap.clear();
            flag = true;
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .position(pos)
                // 设置使用自定义图标
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bullet_red))
                .draggable(true);
        // 添加Marker
        aMap.addMarker(markerOptions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 必须回调MapView的onResume()方法
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 必须回调MapView的onPause()方法
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 必须回调MapView的onSaveInstanceState()方法
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 必须回调MapView的onDestroy()方法
        mapView.onDestroy();
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        Log.e("showshopLHY", "123");
        GeocodeAddress addr = geocodeResult.getGeocodeAddressList().get(0);
        // 获取目标前的经纬度，其实可以不用geocode，这里只是把intent里面的东西拿了出来
        LatLonPoint latlng = new LatLonPoint(lati, longti);
        // 获取用户当前的位置
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(CustomerGetActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            return;
        }
        Location loc = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // 创建路线规划的起始点
        RouteSearch.FromAndTo ft = new RouteSearch.FromAndTo(
                new LatLonPoint(loc.getLatitude(), loc.getLongitude()), latlng);
        // 创建步行的查询条件
        RouteSearch.WalkRouteQuery walkRouteQuery = new RouteSearch.WalkRouteQuery(ft , RouteSearch.WALK_DEFAULT
        );
        routeSearch.calculateWalkRouteAsyn(walkRouteQuery);
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i){
        if(i == 1000 && walkRouteResult!=null){
            // 获取系统规划的第一条路径
            WalkPath walkPath = walkRouteResult.getPaths().get(0);
            // 获取规划路径中的多条路径
            List<WalkStep> steps = walkPath.getSteps();
            for(WalkStep step : steps){
                // 获取组成路线的多个点
                List<LatLonPoint> points = step.getPolyline();
                List<LatLng> latLngs = new ArrayList<>();
                for(LatLonPoint point : points){
                    latLngs.add(new LatLng(point.getLatitude(),point.getLongitude()));
                }
                PolylineOptions ployOptions = new PolylineOptions()
                        .addAll(latLngs)
                        .color(0xffff0000)
                        .width(8);
                aMap.addPolyline(ployOptions);
            }
        }
        else{
            Toast.makeText(this, "啊累累，目标好像丢失嘞(✿◡‿◡)"
                    , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {}

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i){}

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i){}


    private void initView() {
        listView = (ListView) findViewById(R.id.list1);
        eName = (TextView) findViewById(R.id.ed11);
        eBalance = (TextView) findViewById(R.id.ed21);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                // 创建一个设置经纬度的CameraUpdate
                CameraUpdate cu = CameraUpdateFactory.changeLatLng(poses[pos]);  // ②
                // 更新地图的显示区域
                aMap.moveCamera(cu);
                updatePosition(poses[pos].latitude,poses[pos].longitude);
                LatLonPoint lp = new LatLonPoint(poses[pos].latitude,poses[pos].longitude);
                RouteSearch.FromAndTo ft = new RouteSearch.FromAndTo(new LatLonPoint(loca.getLatitude(), loca.getLongitude()), lp);
                // 创建步行的查询条件
                RouteSearch.WalkRouteQuery walkRouteQuery = new RouteSearch.WalkRouteQuery(ft , RouteSearch.WALK_DEFAULT);
                routeSearch.calculateWalkRouteAsyn(walkRouteQuery);

            }
        });
    }
    public void add(View view){
        String name =eName.getText().toString();
        String balance = eBalance.getText().toString();
        f =new Shop(name,balance);
        shopdao.insert(f);
        list.add(f);
        shopAdapter.notifyDataSetChanged();
    }
    public void addd(String name,String balance){
        f =new Shop(name,balance);
        shopdao.insert(f);
        list.add(f);
        shopAdapter.notifyDataSetChanged();
    }
}



