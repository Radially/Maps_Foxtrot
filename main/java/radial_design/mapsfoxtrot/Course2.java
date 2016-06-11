package radial_design.mapsfoxtrot;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonathan on 25/05/2016.
 */
public class Course2 {
    private List<Mark> courseMarks = new ArrayList<Mark>();
    private CourseSettings courseSettings;
    private Mark referencePoint;
    private int courseType;

    public Course2(CourseSettings courseSettings) {
        this.setCourseSettings(courseSettings);
        courseType=courseSettings.getCourseType();
    }
    public void setCourse() {
        courseMarks.clear();
        switch (courseType % 10) {
            case 1:  //Trapezoid
                courseMarks.add(new MonoOrinMark("Pin End", courseSettings.getROLatLng(), 90, 0.16, R.drawable.mark_cylinder_orange_24));
                courseMarks.get(0).LocIt(courseSettings.getWindDir());
                referencePoint = new MonoOrinMark("referencePoint", midPoint(courseSettings.getROLatLng().getLatitude(), courseSettings.getROLatLng().getLongitude(), courseMarks.get(0).getLoc().getLatitude(), courseMarks.get(0).getLoc().getLongitude()), 0, 0.05, R.drawable.mark_cylinder_orange_24);
                courseMarks.add(new MonoOrinMark("1", referencePoint, 0, courseSettings.getDist2m1(), R.drawable.mark_cylinder_orange_24));
                switch (courseType / 10 % 10) {  //60,120 or 70,110
                    case 0:  //60,120
                        switch (courseType / 100 % 10) { //shorted or 1/2 or 2/3
                            case 0:  //shorted OUT, reach=1/2 beat
                                courseMarks.add(new BiOrinMark("2", referencePoint, courseMarks.get(1), 30, 120, R.drawable.mark_cylinder_orange_24));
                                courseMarks.add(new BiOrinMark("3", referencePoint, courseMarks.get(2), 60, 0, R.drawable.mark_cylinder_orange_24));
                                break;
                            case 1:   // equal beats, reach=1/2 beat
                                courseMarks.add(new BiOrinMark("2", referencePoint, courseMarks.get(1), 30, 120, R.drawable.mark_cylinder_orange_24));
                                courseMarks.add(new BiOrinMark("3", referencePoint, courseMarks.get(2), 120, 0, R.drawable.mark_cylinder_orange_24));
                                courseMarks.add(new MonoOrinMark("Finish Line", courseMarks.get(3), 240, 0.1, R.drawable.mark_cylinder_orange_24));
                                break;
                            case 2:  // equal beats, reach=2/3 beat
                                courseMarks.add(new BiOrinMark("2", referencePoint, courseMarks.get(1), 41, 120, R.drawable.mark_cylinder_orange_24));
                                courseMarks.add(new BiOrinMark("3", referencePoint, courseMarks.get(2), 120, 0, R.drawable.mark_cylinder_orange_24));
                                courseMarks.add(new MonoOrinMark("Finish Line", courseMarks.get(3), 240, 0.1, R.drawable.mark_cylinder_orange_24));
                                break;
                        }
                        break;
                    case 1:  //70,110
                        switch (courseType / 100 % 10) { //shorted or 1/2 or 2/3
                            case 0:  //shorted OUT, reach=1/2 beat
                                courseMarks.add(new BiOrinMark("2", referencePoint, courseMarks.get(1), 30, 110, R.drawable.mark_cylinder_orange_24));
                                courseMarks.add(new BiOrinMark("3", referencePoint, courseMarks.get(2), 70, 0, R.drawable.mark_cylinder_orange_24));
                                break;
                            case 1:   // equal beats, reach=1/2 beat
                                courseMarks.add(new BiOrinMark("2", referencePoint, courseMarks.get(1), 30, 110, R.drawable.mark_cylinder_orange_24));
                                courseMarks.add(new BiOrinMark("3", referencePoint, courseMarks.get(2), 110, 0, R.drawable.mark_cylinder_orange_24));
                                courseMarks.add(new MonoOrinMark("Finish Line", courseMarks.get(3), 250, 0.1, R.drawable.mark_cylinder_orange_24));
                                break;
                            case 2:  // equal beats, reach=2/3 beat
                                courseMarks.add(new BiOrinMark("2", referencePoint, courseMarks.get(1), 39, 120, R.drawable.mark_cylinder_orange_24));
                                courseMarks.add(new BiOrinMark("3", referencePoint, courseMarks.get(2), 110, 0, R.drawable.mark_cylinder_orange_24));
                                courseMarks.add(new MonoOrinMark("Finish Line", courseMarks.get(3), 250, 0.1, R.drawable.mark_cylinder_orange_24));
                                switch (courseType / 1000 % 10) {
                                    case 1:
                                        courseMarks.add(new BiOrinMark("5", referencePoint, courseMarks.get(3), 180, 250, R.drawable.mark_cylinder_orange_24));
                                        break;
                                    case 2:
                                        courseMarks.add(new BiOrinMark("5", referencePoint, courseMarks.get(3), 180, 250, R.drawable.mark_cylinder_orange_24));
                                        Mark finishLineReference = new MonoOrinMark("Finish Line Reference", referencePoint, 180, 0.15, R.drawable.mark_cylinder_orange_24);
                                        courseMarks.add(new MonoOrinMark("Starboard Finish", finishLineReference, 270, 0.03, R.drawable.mark_cylinder_orange_24));
                                        courseMarks.add(new MonoOrinMark("Port Finish", finishLineReference, 90, 0.03, R.drawable.mark_cylinder_orange_24));
                                        break;
                                }
                                break;
                        }
                        break;
                }

                //mark 1a
                //mark 2a

                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
        courseMarks.add(new LocOrinMark("Signal boat", courseSettings.getROLatLng()));
    }


    public LatLng midPoint(double lat1, double lon1, double lat2, double lon2) {
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);
        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);
        return new LatLng(Math.toDegrees(lat3), Math.toDegrees(lon3));
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


