package radial_design.mapsfoxtrot;

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
@DynamoDBTable(tableName = "Course")
public class OldCourse {
    private int courseType;
    private List<Mark> courseMarks = new ArrayList<Mark>();
    private LatLng RCloc = new LatLng(32.85,34.99);
    private LatLng ReferencePoint = new LatLng(32.85,34.99);
    private float dis2m1;
    private int windD;
    private CourseSettings courseSettings;

    public Map<String, String> getSettingsMap() {
        return courseSettings.getSettingsMap();
    }
    public void setSettingsMap(Map<String, String> settingsMap) {
        this.courseSettings.setSettingsMap(settingsMap);
        courseType=Integer.parseInt(settingsMap.get("Course Type"));  //temp to test "unmarshall""
        dis2m1=Float.parseFloat(settingsMap.get("Distance Mark 1"));
        windD=Integer.parseInt(settingsMap.get("Wind Direction"));
        // RCloc=new LatLng(Double.parseDouble(settingsMap.get("RCloc").toString()),Double.parseDouble(settingsMap.get("RCloc").toString()));
    }

    public OldCourse(CourseSettings courseSettings) {
        this.setCourseSettings(courseSettings);
        courseType=Integer.parseInt(courseSettings.getSettingsMap().get("Course Type"));  //temp to test "unmarshall""
        dis2m1=Float.parseFloat(courseSettings.getSettingsMap().get("Distance Mark 1"));
        windD=Integer.parseInt(courseSettings.getSettingsMap().get("Wind Direction"));
    }



    public void setCourse() {
        courseMarks.clear();
        courseMarks.add(new LocOrinMark("RC boat", RCloc));
        switch (courseType) {
            case 1:
                courseMarks.add(new MonoOrinMark("1",  ReferencePoint,  0 , dis2m1, R.drawable.mark_cylinder_orange_24));
                courseMarks.add(new BiOrinMark("2",  ReferencePoint, courseMarks.get(1), 40, 110, R.drawable.mark_cylinder_orange_24));
                courseMarks.add(new BiOrinMark("3",  ReferencePoint, courseMarks.get(1), 70, 140, R.drawable.mark_cylinder_orange_24));
                courseMarks.add(new MonoOrinMark("4", courseMarks.get(0), 30, 0.15, R.drawable.mark_cylinder_orange_24));
                courseMarks.add(new MonoOrinMark("Pin End",  courseMarks.get(0),  90 , 0.08, R.drawable.mark_cylinder_orange_24));
                courseMarks.add(new MonoOrinMark("Finish Line",  courseMarks.get(3), 240 , 0.1 , R.drawable.mark_cylinder_orange_24));

                break;
            case 2:
                courseMarks.add(new MonoOrinMark("Windward",  ReferencePoint,  0 , dis2m1, R.drawable.mark_cylinder_orange_24));
                courseMarks.add(new MonoOrinMark("Leeward",  ReferencePoint,  180 , dis2m1, R.drawable.mark_cylinder_orange_24));
                courseMarks.add(new MonoOrinMark("Pin End",  courseMarks.get(0),  90 , 0.08, R.drawable.mark_cylinder_orange_24));
                break;
            case 3:
            default:

                break;
        }

        for(int i=0; i<courseMarks.size();i++){
            courseMarks.get(i).LocIt(windD);
        }
    }
    public OldCourse(int courseNum, LatLng RCloc, float dis2m1, int windD) {
        this.courseType = courseNum;
        this.dis2m1 = dis2m1;
        this.windD = windD;
        this.RCloc = RCloc;
        courseMarks.add(new LocOrinMark("RC boat", RCloc));
        switch (courseNum) {
            case 1:
                courseMarks.add(new MonoOrinMark("1",  courseMarks.get(0),  0 , dis2m1, R.drawable.mark_cylinder_orange_24));
                courseMarks.add(new BiOrinMark("2",  courseMarks.get(0), courseMarks.get(1), 40, 110, R.drawable.mark_cylinder_orange_24));
                courseMarks.add(new BiOrinMark("3",  courseMarks.get(0), courseMarks.get(1), 70, 140, R.drawable.mark_cylinder_orange_24));
                courseMarks.add(new MonoOrinMark("4", courseMarks.get(0), 30, 0.15, R.drawable.mark_cylinder_orange_24));
                courseMarks.add(new MonoOrinMark("Pin End",  courseMarks.get(0),  90 , 0.08, R.drawable.mark_cylinder_orange_24));
                break;
            case 2:
                courseMarks.add(new MonoOrinMark("Windward",  courseMarks.get(0),  0 , dis2m1, R.drawable.mark_cylinder_orange_24));
                courseMarks.add(new MonoOrinMark("Leeward",  courseMarks.get(0),  180 , dis2m1, R.drawable.mark_cylinder_orange_24));
                courseMarks.add(new MonoOrinMark("Pin End",  courseMarks.get(0),  90 , 0.08, R.drawable.mark_cylinder_orange_24));
                break;
            case 3:
            default:

                break;
        }

        for(int i=0; i<courseMarks.size();i++){
            courseMarks.get(i).LocIt(windD);
        }
    }





    public void setCourseSettings(CourseSettings cs){this.courseSettings=cs;}
    public CourseSettings getCourseSettings() {return this.courseSettings;}
    public int markNumber(){
        return courseMarks.size();
    }
    public Mark getMark(int index){
        return courseMarks.get(index);
    }
    public LatLng getMarkLoc(int index){
        return courseMarks.get(index).getLoc();
    }
    public String getMarkName(int index){
        return courseMarks.get(index).getName();
    }
    public int getMarkDrawable(int index){
        return courseMarks.get(index).getDrawableResource();
    }
    public List<Mark> getCourseMarks() {
        return courseMarks;
    }
}
