package radial_design.mapsfoxtrot;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.mapbox.mapboxsdk.annotations.Annotation;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.MyBearingTracking;
import com.mapbox.mapboxsdk.constants.MyLocationTracking;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MLActivity extends Activity implements MapboxMap.OnMyLocationChangeListener, markCyclerAdapter.OnMarkRecyclerItemClickListener {
    public static boolean LmenuOpened = false;
    public static boolean RmenuOpened = false;

    public DynamoDBMapper mapper;
    private User myRaceOfficerUser;
    private CourseSettings courseSettings;
    private Course course;
    private Point size;
    private IconFactory mIconFactory;
    private MapView mapView = null;

    private View navModeBar;

    private RecyclerView mRecyclerView;
    private markCyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView navModeHDG;
    private TextView navModeDst;

    private LatLng myLoc;
    private LatLng destination;
    private Context context = this;
    private LocationManager locationManager;
    private boolean setMyLocState = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ml);
        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        mIconFactory = IconFactory.getInstance(this);

        final View leftSlider = findViewById(R.id.left_slidedown);
        leftSlider.animate().translationY(-size.y * 7 / 8);
        final View rightSlider = findViewById(R.id.right_slidedown);
        rightSlider.animate().translationY(-size.y * 7 / 8);
        navModeBar = findViewById(R.id.nav_mode_bar);
        navModeBar.animate().translationY(size.y/*navModeBar.getHeight()*/);

        mapboxInit(savedInstanceState);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Location Manager to receive location updates
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } catch (Exception e) {

        }

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "eu-west-1:16195ef4-c4f3-41f6-8237-0bc0117f8260", // Identity Pool ID
                Regions.EU_WEST_1 // Region
        );
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        ddbClient.setRegion(Region.getRegion(Regions.EU_WEST_1));  //super important detail
        mapper = new DynamoDBMapper(ddbClient);

        myRaceOfficerUser = (User) getIntent().getSerializableExtra("My Race Officer");
        new loadCourseTask().execute(myRaceOfficerUser.getUserName());

        mRecyclerView = (RecyclerView) findViewById(R.id.marksRV);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        List<Mark> tempLocs = new ArrayList();
        mAdapter = new markCyclerAdapter(tempLocs, this); //(RCloc)     //what do you expect? null without a problem? wtf?
        mRecyclerView.setAdapter(mAdapter);

        navModeDst = (TextView) findViewById(R.id.nav_mode_distance_tv);
        navModeHDG = (TextView) findViewById(R.id.nav_mode_heading_tv);



        Button bl = (Button) findViewById(R.id.left_slidedown_B);
        bl.setText("updates");
        bl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (RmenuOpened) {
                    rightSlider.animate().translationY(-size.y * 7 / 8);
                    RmenuOpened = false;
                }
                if (LmenuOpened) {
                    leftSlider.animate().translationY(-size.y * 7 / 8);
                    LmenuOpened = false;
                } else {
                    leftSlider.animate().translationY(-size.y * 2 / 8);
                    LmenuOpened = true;
                }
            }
        });
        Button br = (Button) findViewById(R.id.right_slidedown_B);
        br.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (LmenuOpened) {
                    leftSlider.animate().translationY(-size.y * 7 / 8);
                    LmenuOpened = false;
                }
                if (RmenuOpened) {
                    rightSlider.animate().translationY(-size.y * 7 / 8);
                    RmenuOpened = false;
                } else {
                    rightSlider.animate().translationY(-size.y * 2 / 8);
                    RmenuOpened = true;
                }
            }
        });


        Button applyCourseB = (Button) findViewById(R.id.left_slidedown_applyCourse);
        applyCourseB.setText("Reload Race Course");
        applyCourseB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new loadCourseTask().execute(myRaceOfficerUser.getUserName());
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(MapboxMap mapboxMap) {
                        mapboxMap.removeAnnotations();
                    }
                });
                leftSlider.animate().translationY(-size.y * 7 / 8);
            }
        });

    }

    @Override
    public void onMarkRecyclerItemClick(final Mark mark) {
        destination = mark.getLoc();
        navModeUpdate();
        navModeBar.animate().translationY(size.y - navModeBar.getHeight());   //TO-BE checked
        Toast.makeText(MLActivity.this, mark.getName() + " is selected", Toast.LENGTH_SHORT).show();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                for (int i = 0; i < mapboxMap.getPolylines().size(); i++) {
                    mapboxMap.removePolyline(mapboxMap.getPolylines().get(0));
                }
                if (destination != null && myLoc != null) {
                    mapboxMap.addPolyline(new PolylineOptions()
                            .add(myLoc).add(destination)
                            .color(Color.parseColor("#3bb2d0"))
                            .width(3));
                }
            }
        });
    }


    private class loadCourseTask extends AsyncTask<String, Void, Boolean> {
        protected void onPreExecute() {
        }

        protected Boolean doInBackground(String... params) {
            try {
                courseSettings = mapper.load(CourseSettings.class, params[0]);
            } catch (Exception exception) {
                Log.e("cMark.@MLActivity", "EXCEPTION TIME!");
                Log.e("cMark.@MLActivity", exception.toString());
                return false;
            }
            if (courseSettings != null) {
                return true;
            } else {
                Log.e("cMark.@ROActivity", "Empty courseSettings");
                return false;
            }
        }

        protected void onProgressUpdate(Integer... progress) {
            Toast.makeText(MLActivity.this, "Connecting...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(MLActivity.this, "Course is ready!", Toast.LENGTH_LONG).show();
                course = new Course(courseSettings);
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(MapboxMap mapboxMap) {
                        mapboxMap.removeAnnotations();
                    }
                });
                course.setCourse();
                mAdapter.changeList(course.getCourseMarks());
                drawMarks();
            } else {
                Toast.makeText(MLActivity.this, "Wrong thing ! \n Try to be normal", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setWindIndicator() {
        ImageView windIndicator = (ImageView) findViewById(R.id.mark_layers_windIndicator);
        windIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                int windDir = Integer.parseInt(courseSettings.getSettingsMap().get("Wind Direction"));
                //mapView.setDirection(360 - windDir, true);
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(MapboxMap mapboxMap) {
                        mapboxMap.setCameraPosition(new CameraPosition.Builder().bearing(Integer.parseInt(courseSettings.getSettingsMap().get("Wind Direction"))).build());
                    }
                });
            }
        });
    }

    private void setMyLocButton() {
        final ImageView myLocB = (ImageView) findViewById(R.id.set_my_loc_center);
        myLocB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                int windDir = Integer.parseInt(courseSettings.getSettingsMap().get("Wind Direction"));
                //mapView.setDirection(360 - windDir, true);
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(MapboxMap mapboxMap) {
                        if (myLoc != null) {
                            mapboxMap.setCameraPosition(new CameraPosition.Builder().target(myLoc).build());
                        }
                        if (setMyLocState) {
                            setMyLocState = false;
                            mapboxMap.getTrackingSettings().setMyBearingTrackingMode(MyBearingTracking.COMPASS);
                            mapboxMap.getTrackingSettings().setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
                        } else {
                            setMyLocState = true;
                            mapboxMap.getTrackingSettings().setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
                        }
                    }
                });
            }
        });
    }

    private void navModeUpdate() {
        if (destination != null && myLoc!=null){
            if(myLoc.distanceTo(destination) > 15){
                if(hdg2dest(myLoc, destination)/100!=0) navModeHDG.setText((int) hdg2dest(myLoc, destination) + "");
                else  navModeHDG.setText("0"+((int) hdg2dest(myLoc, destination))+"");
                if (myLoc.distanceTo(destination) > 1000) {
                    navModeDst.setText(((int)(myLoc.distanceTo(destination)*0.539956803/100))/10.0 + "nm");
                } else {
                    navModeDst.setText((int) myLoc.distanceTo(destination) + "m");
                }
            }
            else {
                destination=null;  //nav mode dismissed, TO-ADD nav_mode_layout animated down
                navModeBar.animate().translationY(size.y);   //TO-BE checked
            }
        }
    }

    private void drawMarks() {
        //mapView.addMarker(new MarkerOptions().position(RCloc).title("RC").icon(iconF.fromDrawable(ContextCompat.getDrawable(this, R.drawable.rc_boat_mark))));
        course.setCourse();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                for (int t = 0; t < course.markNumber(); t++) {
                    mapboxMap.addMarker(new MarkerOptions().position(course.getMarkLoc(t)).title(course.getMarkName(t)).icon(mIconFactory.fromDrawable(ContextCompat.getDrawable(context, course.getMarkDrawable(t)))));
                }
            }
        });

    }

    private void mapboxInit(Bundle savedInstanceState) {
        mapView = (MapView) findViewById(R.id.mark_layers_mapboxMapView);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);
        //mapView.setCenterCoordinate(new LatLng(32.85, 34.99));
        // mapView.setZoomLevel(12);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                mapboxMap.setMyLocationEnabled(true);
                mapboxMap.setOnMyLocationChangeListener((MapboxMap.OnMyLocationChangeListener) context);
                mapboxMap.getTrackingSettings().setMyBearingTrackingMode(MyBearingTracking.COMPASS);
                mapboxMap.getTrackingSettings().setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);

                mapboxMap.getUiSettings().setCompassEnabled(true);
                mapboxMap.getUiSettings().setCompassMargins(0, 200, 35, 0);
                mapboxMap.getUiSettings().setLogoEnabled(false);
                mapboxMap.getUiSettings().setAttributionEnabled(false);

                mapboxMap.setOnMyLocationTrackingModeChangeListener(new MapboxMap.OnMyLocationTrackingModeChangeListener() {
                    @Override
                    public void onMyLocationTrackingModeChange(int myLocationTrackingMode) {

                    }
                });
                mapboxMap.setOnMyBearingTrackingModeChangeListener(new MapboxMap.OnMyBearingTrackingModeChangeListener() {
                    @Override
                    public void onMyBearingTrackingModeChange(int myBearingTrackingMode) {

                    }
                });
            }
        });
        //mapView.setCompassMargins(0, 200, 35, 0);
        //mapView.setCompassEnabled(true);
        //mapView.setLogoVisibility(1);
        setWindIndicator();
        setMyLocButton();
    }


    @Override
    public void onMyLocationChange(final Location location) {
        myLoc = loc2lng(location);
        if (destination != null) navModeUpdate();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                for (int i = 0; i < mapboxMap.getPolylines().size(); i++) {
                    mapboxMap.removePolyline(mapboxMap.getPolylines().get(0));
                }
                if (destination != null && myLoc != null) {
                    mapboxMap.addPolyline(new PolylineOptions()
                            .add(myLoc).add(destination)
                            .color(Color.parseColor("#3bb2d0"))
                            .width(3));
                }
            }
        });
    }


    public LatLng loc2lng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public double hdg2dest(LatLng p1, LatLng p2) {
        double p1lat = Math.toRadians(p1.getLatitude());
        double p1Lng = Math.toRadians(p1.getLongitude());
        double p2lat = Math.toRadians(p2.getLatitude());
        double p2Lng = Math.toRadians(p2.getLongitude());
        double y = Math.sin(p2Lng - p1Lng) * Math.cos(p2lat);
        double x = Math.cos(p1lat) * Math.sin(p2lat) - Math.sin(p1lat) * Math.cos(p2lat) * Math.cos(p2Lng - p1Lng);
        double result = Math.toDegrees(Math.atan2(y, x));
        if (result >= 0) return result;
        else return 360 + result;
    }

}




