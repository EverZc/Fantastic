package com.zc.guessmusic.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.zc.guessmusic.R;
import com.zc.guessmusic.adapter.RouteLineAdapter;
import com.zc.guessmusic.overlayutil.OverlayManager;
import com.zc.guessmusic.overlayutil.TransitRouteOverlay;
import com.zc.guessmusic.overlayutil.WalkingRouteOverlay;

import java.util.ArrayList;
import java.util.List;

public class MapMainActivityTEXT extends AppCompatActivity implements BaiduMap.OnMapClickListener,
        OnGetRoutePlanResultListener
{
    // 搜索相关
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    MapView mMapView = null;
    BaiduMap mBaiduMap;
    RouteLine route = null;
    // 浏览路线节点相关
    Button mBtnPre = null; // 上一个节点
    Button mBtnNext = null; // 下一个节点
    int nodeIndex = -1; // 节点索引,供浏览节点时使用
    int nowSearchType = -1 ; // 当前进行的检索，供判断浏览节点时结果使用。
    private TextView positionText;
    //当前位置的坐标
    LatLng ll;
    private TextView popupText = null; // 泡泡view
    WalkingRouteResult nowResultwalk = null;
    TransitRouteResult nowResultransit = null;
    DrivingRouteResult nowResultdrive  = null;
    MassTransitRouteResult nowResultmass = null;

    OverlayManager routeOverlay = null;

    StringBuilder currentPosition ;

    MassTransitRouteLine massroute = null;

    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;
    private Marker mMarkerE;
    private Marker mMarkerF;

    private Marker mMarkerA1;
    private Marker mMarkerB1;
    private Marker mMarkerC1;
    private Marker mMarkerD1;
    private Marker mMarkerE1;
    private Marker mMarkerF1;

    BitmapDescriptor bdA;
    BitmapDescriptor bdB;
    BitmapDescriptor bdC ;
    BitmapDescriptor bdD ;
    BitmapDescriptor bdE ;
    BitmapDescriptor bdF ;
    BitmapDescriptor bdA1;
    BitmapDescriptor bdB1;
    BitmapDescriptor bdC1 ;
    BitmapDescriptor bdD1 ;
    BitmapDescriptor bdE1 ;
    BitmapDescriptor bdF1 ;

    private boolean isFirstLocate = true;
    public MyLocationListenner myListener = new MyLocationListenner();
    // 定位相关
    LocationClient mLocationClient;

    private MyLocationConfiguration.LocationMode mCurrentMode;
    boolean isFirstLoc = true; // 是否首次定位
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.mapactivity_main);
        // 初始化全局 bitmap 信息，不用时及时 recycle
        bdA = BitmapDescriptorFactory
                .fromResource(R.drawable.zuobiao_fengjing);
        bdB = BitmapDescriptorFactory
                .fromResource(R.drawable.zuobiao_haoche);
        bdC = BitmapDescriptorFactory
                .fromResource(R.drawable.zuobiao_lanqiu);
        bdD = BitmapDescriptorFactory
                .fromResource(R.drawable.zuobiao_meinv);
        bdE = BitmapDescriptorFactory
                .fromResource(R.drawable.zuobiao_weixin);
        bdF = BitmapDescriptorFactory
                .fromResource(R.drawable.zuobiao_zhangjie);
        bdA1 = BitmapDescriptorFactory
                .fromResource(R.drawable.zuobiao_fengjing);
        bdB1 = BitmapDescriptorFactory
                .fromResource(R.drawable.zuobiao_haoche);
        bdC1 = BitmapDescriptorFactory
                .fromResource(R.drawable.zuobiao_lanqiu);
        bdD1 = BitmapDescriptorFactory
                .fromResource(R.drawable.zuobiao_meinv);
        bdE1 = BitmapDescriptorFactory
                .fromResource(R.drawable.zuobiao_weixin);
        bdF1 = BitmapDescriptorFactory
                .fromResource(R.drawable.zuobiao_zhangjie);
        //获取地图控件引用
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.baidumap_mapView);
        mBaiduMap = mMapView.getMap();
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        initOverlay();
        mBtnPre = (Button) findViewById(R.id.pre);
        mBtnNext = (Button) findViewById(R.id.next);
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);

        //定位初始化
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(myListener);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MapMainActivityTEXT.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        Log.e("-----","权限1");
        if (ContextCompat.checkSelfPermission(MapMainActivityTEXT.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        Log.e("-----","权限2");
        if (ContextCompat.checkSelfPermission(MapMainActivityTEXT.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        Log.e("-----","权限3");
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MapMainActivityTEXT.this, permissions, 1);
        } else {
            requestLocation();
        }
    }
    private void requestLocation() {

        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    //button的点击事件
    public void searchButtonProcess(View view) {
        route = null;
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        mBaiduMap.clear();
        // 处理搜索按钮响应
        // 设置起终点信息，对于tranist search 来说，城市名无意义
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("成都", "天府三街");
        PlanNode sss=PlanNode.withLocation(ll);
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("成都", "和美西路");
        // 实际使用中请对起点终点城市进行正确的设定
        if (view.getId() == R.id.transit) {
            mSearch.transitSearch((new TransitRoutePlanOption())
                    .from(sss).city("成都").to(enNode));
            nowSearchType = 2;
        } else if (view.getId() == R.id.walk) {
            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(sss).to(enNode));
            nowSearchType = 3;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MapMainActivityTEXT.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);

            if (result.getRouteLines().size() > 1 ) {
                nowResultwalk = result;

                MyTransitDlg myTransitDlg = new MyTransitDlg(MapMainActivityTEXT.this,
                        result.getRouteLines(),
                        RouteLineAdapter.Type.WALKING_ROUTE);
                myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                    public void onItemClick(int position) {
                        route = nowResultwalk.getRouteLines().get(position);
                        WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
                        mBaiduMap.setOnMarkerClickListener(overlay);
                        routeOverlay = overlay;
                        overlay.setData(nowResultwalk.getRouteLines().get(position));
                        overlay.addToMap();
                        overlay.zoomToSpan();
                    }

                });
                myTransitDlg.show();

            } else if ( result.getRouteLines().size() == 1 ) {
                // 直接显示
                route = result.getRouteLines().get(0);
                WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            } else {
                Log.d("route result", "结果数<0" );
                return;
            }
        }
    }

    //添加覆盖物
    public void initOverlay() {
        // add marker overlay
        LatLng llA = new LatLng(30.63683627, 104.1602306);
        LatLng llB = new LatLng(30.63805870, 104.1598713);
        LatLng llC = new LatLng(30.63509126, 104.15773335);
        LatLng llD = new LatLng(30.63792664, 104.15651165);
        LatLng llE=new LatLng(30.5544924951933,104.07427178834307);

        LatLng llA1 = new LatLng(30.5545780, 104.07288839);
        LatLng llB1 = new LatLng(30.55402600510793, 104.07225060094562);
        LatLng llC1 = new LatLng(30.5543991973574, 104.07480178859396);
        LatLng llD1 = new LatLng(30.553209642009374, 104.07358009310039);
        LatLng llE1 = new LatLng(30.552175572128736,104.07179246513553);
        LatLng llF1 = new LatLng(30.5534195646114,104.07258297398431);

        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bdA)
                .zIndex(9).draggable(true);
        // 生长动画
        ooA.animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
        MarkerOptions ooB = new MarkerOptions().position(llB).icon(bdB)
                .zIndex(5);
        ooB.animateType(MarkerOptions.MarkerAnimateType.grow);

        mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));
        MarkerOptions ooC = new MarkerOptions().position(llC).icon(bdC)
                .perspective(false).zIndex(7);
        ooC.animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarkerC = (Marker) (mBaiduMap.addOverlay(ooC));
        MarkerOptions ooD = new MarkerOptions().position(llD).icon(bdD)
                .zIndex(0);
        ooD.animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));

        MarkerOptions ooE = new MarkerOptions().position(llE).icon(bdE)
                .zIndex(22).draggable(true);
        ooE.animateType(MarkerOptions.MarkerAnimateType.grow);
        //-----------------------------------------------
        MarkerOptions ooA1 = new MarkerOptions().position(llA1).icon(bdA1)
                .zIndex(20).draggable(true);
        // 生长动画
        ooA1.animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarkerA1 = (Marker) (mBaiduMap.addOverlay(ooA1));
        MarkerOptions ooB1 = new MarkerOptions().position(llB1).icon(bdB1)
                .zIndex(21);
        ooB1.animateType(MarkerOptions.MarkerAnimateType.grow);

        mMarkerB1 = (Marker) (mBaiduMap.addOverlay(ooB1));
        MarkerOptions ooC1 = new MarkerOptions().position(llC1).icon(bdC1)
                .perspective(false).zIndex(25);
        ooC1.animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarkerC1 = (Marker) (mBaiduMap.addOverlay(ooC1));
        MarkerOptions ooD1 = new MarkerOptions().position(llD1).icon(bdD1)
                .zIndex(23);
        ooD1.animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarkerD1 = (Marker) (mBaiduMap.addOverlay(ooD1));

        MarkerOptions ooE1 = new MarkerOptions().position(llE1).icon(bdE1)
                .zIndex(24).draggable(true);
        ooE1.animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarkerE1 = (Marker) (mBaiduMap.addOverlay(ooE1));

        MarkerOptions ooF1 = new MarkerOptions().position(llF1).icon(bdF1)
                .zIndex(26).draggable(true);
        ooF1.animateType(MarkerOptions.MarkerAnimateType.grow);
        mMarkerF1 = (Marker) (mBaiduMap.addOverlay(ooF1));

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker==mMarkerA1){
                    Toast.makeText(MapMainActivityTEXT.this,"请获取服务器的数据 Test--1",Toast.LENGTH_SHORT).show();
                }else if(marker==mMarkerB1){
                    Toast.makeText(MapMainActivityTEXT.this,"请获取服务器的数据 Test--2",Toast.LENGTH_SHORT).show();
                }else if(marker==mMarkerC1){
                    Toast.makeText(MapMainActivityTEXT.this,"请获取服务器的数据 Test--3",Toast.LENGTH_SHORT).show();
                }else if(marker==mMarkerD1){
                    Toast.makeText(MapMainActivityTEXT.this,"请获取服务器的数据 Test--4",Toast.LENGTH_SHORT).show();
                }else if(marker==mMarkerE1){
                    Toast.makeText(MapMainActivityTEXT.this,"请获取服务器的数据 Test--5",Toast.LENGTH_SHORT).show();
                }else if(marker==mMarkerF1){
                    Toast.makeText(MapMainActivityTEXT.this,"请获取服务器的数据 Test--6",Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(
                        MapMainActivityTEXT.this,
                        "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
                                + marker.getPosition().longitude,
                        Toast.LENGTH_LONG).show();
                Log.e("-------------",marker.getPosition().latitude + ", "
                        + marker.getPosition().longitude);
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    private static Bitmap big(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(1.5f,1.5f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }


    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MapMainActivityTEXT.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            result.getSuggestAddrInfo();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            mBtnPre.setVisibility(View.VISIBLE);
            mBtnNext.setVisibility(View.VISIBLE);

            if (result.getRouteLines().size() > 1 ) {
                nowResultransit = result;

                MyTransitDlg myTransitDlg = new MyTransitDlg(MapMainActivityTEXT.this,
                        result.getRouteLines(),
                        RouteLineAdapter.Type.TRANSIT_ROUTE);
                myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                    public void onItemClick(int position) {
                        route = nowResultransit.getRouteLines().get(position);
                        TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
                        mBaiduMap.setOnMarkerClickListener(overlay);
                        routeOverlay = overlay;
                        overlay.setData(nowResultransit.getRouteLines().get(position));
                        overlay.addToMap();
                        overlay.zoomToSpan();
                    }
                });
                myTransitDlg.show();

            } else if ( result.getRouteLines().size() == 1 ) {
                // 直接显示
                route = result.getRouteLines().get(0);
                TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            } else {
                Log.d("route result", "结果数<0" );
                return;
            }
        }
    }
    /**
     * 节点浏览示例
     * @param v
     */
    public void nodeClick(View v) {
        LatLng nodeLocation = null;
        String nodeTitle = null;
        Object step = null;
        if ( nowSearchType != 0  && nowSearchType != -1) {
            // 非跨城综合交通
            if (route == null || route.getAllStep() == null) {
                return;
            }
            if (nodeIndex == -1 && v.getId() == R.id.pre) {
                return;
            }
            // 设置节点索引
            if (v.getId() == R.id.next) {
                if (nodeIndex < route.getAllStep().size() - 1) {
                    nodeIndex++;
                } else {
                    return;
                }
            } else if (v.getId() == R.id.pre) {
                if (nodeIndex > 0) {
                    nodeIndex--;
                } else {
                    return;
                }
            }
            // 获取节结果信息
            step = route.getAllStep().get(nodeIndex);
            if (step instanceof DrivingRouteLine.DrivingStep) {
                nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrance().getLocation();
                nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
            } else if (step instanceof WalkingRouteLine.WalkingStep) {
                nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrance().getLocation();
                nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
            } else if (step instanceof TransitRouteLine.TransitStep) {
                nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrance().getLocation();
                nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
            } else if (step instanceof BikingRouteLine.BikingStep) {
                nodeLocation = ((BikingRouteLine.BikingStep) step).getEntrance().getLocation();
                nodeTitle = ((BikingRouteLine.BikingStep) step).getInstructions();
            }
        } else if ( nowSearchType == 0) {
            // 跨城综合交通  综合跨城公交的结果判断方式不一样
            if (massroute == null || massroute.getNewSteps() == null) {
                return;
            }
            if (nodeIndex == -1 && v.getId() == R.id.pre) {
                return;
            }
            boolean isSamecity = nowResultmass.getOrigin().getCityId() == nowResultmass.getDestination().getCityId();
            int size = 0;
            if ( isSamecity ) {
                size = massroute.getNewSteps().size();
            } else {
                for ( int i = 0; i < massroute.getNewSteps().size(); i++ ) {
                    size += massroute.getNewSteps().get(i).size();
                }
            }

            // 设置节点索引
            if (v.getId() == R.id.next) {
                if (nodeIndex < size - 1) {
                    nodeIndex++;
                } else {
                    return;
                }
            } else if (v.getId() == R.id.pre) {
                if (nodeIndex > 0) {
                    nodeIndex--;
                } else {
                    return;
                }
            }
            if ( isSamecity ) {
                // 同城
                step = massroute.getNewSteps().get(nodeIndex).get(0);
            } else {
                // 跨城
                int num = 0;
                for (int j = 0; j < massroute.getNewSteps().size(); j++ ) {
                    num += massroute.getNewSteps().get(j).size();
                    if ( nodeIndex - num < 0) {
                        int k = massroute.getNewSteps().get(j).size() + nodeIndex - num;
                        step = massroute.getNewSteps().get(j).get(k);
                        break;
                    }
                }
            }

            nodeLocation = ((MassTransitRouteLine.TransitStep) step).getStartLocation();
            nodeTitle = ((MassTransitRouteLine.TransitStep) step).getInstructions();
        }

        if (nodeLocation == null || nodeTitle == null) {
            return;
        }

        // 移动节点至中心
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
        // show popup
       popupText = new TextView(MapMainActivityTEXT.this);
        popupText.setBackgroundResource(R.drawable.tishi_blue);
        popupText.setTextColor(0xFF000000);
        popupText.setText(nodeTitle);
        mBaiduMap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {


            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("街道：").append(location.getStreet()).append("\n");
           Log.e("------------------",currentPosition+"");

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            if (isFirstLoc) {
                isFirstLoc = false;
                //当前的经纬度
                ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                //自设定经纬度
                /*ll = new LatLng(30.554041554813924,
                        104.0728434825822);*/
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);

                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

       /* private void navigateTo(BDLocation location) {
            if (isFirstLocate) {

                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(update);
                update = MapStatusUpdateFactory.zoomTo(16f);
                mBaiduMap.animateMapStatus(update);
                isFirstLocate = false;
            }
            MyLocationData.Builder locationBuilder = new MyLocationData.
                    Builder();
            locationBuilder.latitude(location.getLatitude());
            locationBuilder.longitude(location.getLongitude());
            MyLocationData locationData = locationBuilder.build();
            mBaiduMap.setMyLocationData(locationData);
        }*/



        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    @Override
    protected void onDestroy() {
        if (mSearch != null) {
            mSearch.destroy();
        }
        mMapView.onDestroy();
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        bdA.recycle();
        bdB.recycle();
        bdC.recycle();
        bdD.recycle();
        bdE.recycle();
        bdF.recycle();
        bdA1.recycle();
        bdB1.recycle();
        bdC1.recycle();
        bdD1.recycle();
        bdE1.recycle();
        bdF1.recycle();

    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }



    // 响应DLg中的List item 点击
    interface OnItemInDlgClickListener {
        public void onItemClick(int position);
    }
    // 供路线选择的Dialog
    class MyTransitDlg extends Dialog {

        private List<? extends RouteLine> mtransitRouteLines;
        private ListView transitRouteList;
        private RouteLineAdapter mTransitAdapter;

        OnItemInDlgClickListener onItemInDlgClickListener;

        public MyTransitDlg(Context context, int theme) {
            super(context, theme);
        }

        public MyTransitDlg(Context context, List< ? extends RouteLine> transitRouteLines, RouteLineAdapter.Type
                type) {
            this( context, 0);
            mtransitRouteLines = transitRouteLines;
            mTransitAdapter = new  RouteLineAdapter( context, mtransitRouteLines , type);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_transit_dialog);

            transitRouteList = (ListView) findViewById(R.id.transitList);
            transitRouteList.setAdapter(mTransitAdapter);

            transitRouteList.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemInDlgClickListener.onItemClick( position);
                    mBtnPre.setVisibility(View.VISIBLE);
                    mBtnNext.setVisibility(View.VISIBLE);
                    dismiss();
                }
            });
        }

        public void setOnItemInDlgClickLinster( OnItemInDlgClickListener itemListener) {
            onItemInDlgClickListener = itemListener;
        }
    }

}
