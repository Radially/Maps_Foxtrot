package radial_design.mapsfoxtrot;

/**
 * Created by Jonathan on 11/05/2016.
 */

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jonathan on 14/04/2016.
 */
@DynamoDBTable(tableName = "CourseSettings")
public class CourseSettings {
    private String userName;
    private List<String> ROloc;
    private Map<String, String> settingsMap/* = new HashMap<String, Object>()*/;


    @DynamoDBHashKey(attributeName = "User Name")
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @DynamoDBAttribute(attributeName = "RO Location")
    public List<String> getROloc() {
        return ROloc;
    }
    public void setROloc(List<String> roLoc) {
        this.ROloc = roLoc;
    }

    @DynamoDBAttribute(attributeName = "Settings Map")
    public Map<String, String> getSettingsMap() {
        return settingsMap;
    }
    public void setSettingsMap(Map<String, String> settingsMap) {
        this.settingsMap = settingsMap;
    }

    protected int getWindDir(){
        return Integer.parseInt(getSettingsMap().get("Wind Direction"));
    }
    protected int getCourseType(){
        return Integer.parseInt(getSettingsMap().get("Course Type"));
    }
    protected double getDist2m1(){
        return Double.parseDouble(getSettingsMap().get("Distance Mark 1"));
    }
    protected LatLng getROLatLng(){
        if(getROloc().size()>=2) return new LatLng( Double.parseDouble(getROloc().get(0)),Double.parseDouble(getROloc().get(1)));
        return null;
    }


}
