package radial_design.mapsfoxtrot;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonathan on 25/05/2016.
 */
public class Course {
    private List<Mark> courseMarks = new ArrayList<Mark>();
    private CourseSettings courseSettings;
    private Mark referencePoint;
    private int courseType = 0;

    public Course(CourseSettings courseSettings) {
        this.setCourseSettings(courseSettings);
        courseType = courseSettings.getCourseType();
    }

    public Course(int courseType, LatLng loc, double dist2m1, int windDir, String userName) {
        courseSettings = new CourseSettings();
        courseSettings.setUserName(userName);

        List<String> scl = new ArrayList<String>();
        scl.add("" + loc.getLatitude());
        scl.add("" + loc.getLongitude());
        courseSettings.setROloc(scl);

        Map<String, String> scm = new HashMap<String, String>();
        scm.put("Course Type", "" + courseType);
        scm.put("Distance Mark 1", "" + dist2m1);
        scm.put("Wind Direction", "" + windDir);
        courseSettings.setSettingsMap(scm);
    }

    public void setCourse() {
        courseMarks.clear();
        courseType = courseSettings.getCourseType();
        switch (courseType % 10) {
            case 1:  //Trapezoid
                courseMarks.add(new MonoOrinMark("Pin End", courseSettings.getROLatLng(), 90, 0.16, R.drawable.mark_cylinder_orange_24));
                courseMarks.get(0).LocIt(courseSettings.getWindDir());
                referencePoint = new MonoOrinMark("referencePoint", midPoint(courseSettings.getROLatLng().getLatitude(), courseSettings.getROLatLng().getLongitude(), courseMarks.get(0).getLoc().getLatitude(), courseMarks.get(0).getLoc().getLongitude()), 0, 0.05, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir());
                courseMarks.add(new MonoOrinMark("1", referencePoint, 0, courseSettings.getDist2m1(), R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                switch (courseType / 10 % 10) {  //60,120 or 70,110
                    case 0:  //60,120
                        switch (courseType / 100 % 10) { //shorted or 1/2 or 2/3
                            case 0:  //shorted OUT, reach=1/2 beat
                                courseMarks.add(new BiOrinMark("2", referencePoint, courseMarks.get(1), 30, 120, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                courseMarks.add(new BiOrinMark("3", referencePoint, courseMarks.get(2), 60, 180, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                break;
                            case 1:   // equal beats, reach=1/2 beat
                                courseMarks.add(new BiOrinMark("2", referencePoint, courseMarks.get(1), 30, 120, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                courseMarks.add(new BiOrinMark("3", referencePoint, courseMarks.get(2), 120, 180, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                courseMarks.add(new MonoOrinMark("Finish Line", courseMarks.get(3), 240, 0.1, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                break;
                            case 2:  // equal beats, reach=2/3 beat
                                courseMarks.add(new BiOrinMark("2", referencePoint, courseMarks.get(1), 41, 120, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                courseMarks.add(new BiOrinMark("3", referencePoint, courseMarks.get(2), 120, 180, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                courseMarks.add(new MonoOrinMark("Finish Line", courseMarks.get(3), 240, 0.1, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                break;
                        }
                        break;
                    case 1:  //70,110
                        switch (courseType / 100 % 10) { //shorted or 1/2 or 2/3
                            case 0:  //shorted OUT, reach=1/2 beat
                                courseMarks.add(new BiOrinMark("2", referencePoint, courseMarks.get(1), 30, 110, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                courseMarks.add(new BiOrinMark("3", referencePoint, courseMarks.get(2), 70, 180, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                break;
                            case 1:   // equal beats, reach=1/2 beat
                                courseMarks.add(new BiOrinMark("2", referencePoint, courseMarks.get(1), 30, 110, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                courseMarks.add(new BiOrinMark("3", referencePoint, courseMarks.get(2), 110, 180, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                courseMarks.add(new MonoOrinMark("Finish Line", courseMarks.get(3), 250, 0.1, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                break;
                            case 2:  // equal beats, reach=2/3 beat
                                courseMarks.add(new BiOrinMark("2", referencePoint, courseMarks.get(1), 39, 120, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                courseMarks.add(new BiOrinMark("3", referencePoint, courseMarks.get(2), 110, 180, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                switch (courseType / 1000 % 10) {
                                    case 0:
                                        courseMarks.add(new MonoOrinMark("Finish Line", courseMarks.get(3), 250, 0.1, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                        break;
                                    case 1:
                                        courseMarks.add(new BiOrinMark("5", referencePoint, courseMarks.get(3), 180, 250, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                        break;
                                    case 2:
                                        courseMarks.add(new BiOrinMark("5", referencePoint, courseMarks.get(3), 180, 250, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                        Mark finishLineReference = new MonoOrinMark("Finish Line Reference", referencePoint, 180, 0.15, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir());
                                        courseMarks.add(new MonoOrinMark("Starboard Finish", finishLineReference, 270, 0.04, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                        courseMarks.add(new MonoOrinMark("Port Finish", finishLineReference, 90, 0.04, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                                        break;
                                }
                                break;
                        }
                        break;
                }
                switch (courseType / 1000 % 10) {
                    case 1:
                        courseMarks.add(new MonoOrinMark("1a", courseMarks.get(1), 120, 0.033, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                        break;
                    case 2:
                        courseMarks.add(new MonoOrinMark("2a", courseMarks.get(2), 225, 0.05, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                        break;
                    case 3:
                        courseMarks.add(new MonoOrinMark("1a", courseMarks.get(1), 120, 0.033, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                        courseMarks.add(new MonoOrinMark("2a", courseMarks.get(2), 225, 0.05, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                        break;
                }
                addGate(referencePoint, 90, 0.02, "4S", "4P", courseMarks);
                break;
            case 2:  // windward/leeward
                courseMarks.add(new MonoOrinMark("Pin End", courseSettings.getROLatLng(), 90, 0.16, R.drawable.flag_red_mark));
                courseMarks.get(0).LocIt(courseSettings.getWindDir());
                referencePoint = new MonoOrinMark("referencePoint", midPoint(courseSettings.getROLatLng().getLatitude(), courseSettings.getROLatLng().getLongitude(), courseMarks.get(0).getLoc().getLatitude(), courseMarks.get(0).getLoc().getLongitude()), 0, 0.05, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir());
                switch (courseType / 10 % 10) {
                    case 0: //mark 1 only
                        courseMarks.add(new MonoOrinMark("1", referencePoint, 0, courseSettings.getDist2m1(), R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                        break;
                    case 1:  //mark 1a and 1
                        courseMarks.add(new MonoOrinMark("1a", referencePoint, 0, courseSettings.getDist2m1(), R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                        courseMarks.add(new MonoOrinMark("1", courseMarks.get(1), 260, 0.02, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
                        break;
                    case 2:  // 1 is a gate
                        Mark referencePoint1 = new MonoOrinMark("1reference", referencePoint, 0, courseSettings.getDist2m1(), R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir());
                        addGate(referencePoint1, 90, 0.02, "1S", "1P", courseMarks);
                        break;
                }
                switch (courseType / 100 % 10) {
                    case 0:  // L, no finish line
                        break;
                    case 1:  //W, upwind finish line
                        Mark referencePointFinish = new MonoOrinMark("referencePointFinish", referencePoint, 0, courseSettings.getDist2m1() + 0.05, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir());
                        addGate(referencePointFinish, 90, 0.02, "Finish S", "Finish P", courseMarks);
                        break;
                    case 2:  // LR
                        break;
                    case 3:  //LG
                        break;
                    case 4:  //WR
                        break;
                    case 5:  //WG
                        break;
                }
                addGate(referencePoint, 90, 0.02, "4S", "4P", courseMarks);
                break;
            case 3:  //triangular
                break;
            case 4:  //optimist
                break;
            case 5:
                break;
        }
        courseMarks.add(new LocOrinMark("Signal boat", courseSettings.getROLatLng()));

        /*for (int p=0; p< courseMarks.size(); p++){
            courseMarks.get(p).LocIt(getCourseSettings().getWindDir());
        }*/
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
    private void addGate(Mark reference,int direction, double distance,String nameStarboard, String namePort ,List<Mark> markList){
        markList.add(new MonoOrinMark(nameStarboard, reference, direction+180, distance/2, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
        markList.add(new MonoOrinMark(namePort, reference, direction, distance/2, R.drawable.mark_cylinder_orange_24).LocIt(getCourseSettings().getWindDir()));
    }

    public void setCourseSettings(CourseSettings cs) {
        this.courseSettings = cs;
    }

    public CourseSettings getCourseSettings() {
        return this.courseSettings;
    }

    public int markNumber() {
        return courseMarks.size();
    }

    public Mark getMark(int index) {
        return courseMarks.get(index);
    }

    public LatLng getMarkLoc(int index) {
        return courseMarks.get(index).getLoc();
    }

    public String getMarkName(int index) {
        return courseMarks.get(index).getName();
    }

    public int getMarkDrawable(int index) {
        return courseMarks.get(index).getDrawableResource();
    }

    public List<Mark> getCourseMarks() {
        return courseMarks;
    }
}


