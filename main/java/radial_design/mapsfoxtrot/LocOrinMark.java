package radial_design.mapsfoxtrot;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class LocOrinMark implements  Mark{
    public LatLng loc;
    public int drawableResource;
    public String name;

    public LocOrinMark(String name, LatLng loc ,int drawableResource){
        this.name=name;
        this.loc=loc;
        this.drawableResource=drawableResource;
    }
    public LocOrinMark(String name, LatLng loc){
        this.name=name;
        this.loc=loc;
        this.drawableResource=R.drawable.rc_boat_mark;
    }
    public Mark LocIt(int windD) {
        //nothing to do. Loc is already done!
        return this;
    }
    public String getName(){return name;}
    public int getDrawableResource(){return drawableResource;}
    public LatLng getLoc(){return this.loc;}
}
