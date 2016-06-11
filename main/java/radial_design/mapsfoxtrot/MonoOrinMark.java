package radial_design.mapsfoxtrot;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class MonoOrinMark implements  Mark{
    public LatLng loc;
    private Mark dpndMloc;
    private int dpndBrng;
    private double distance;
    public int drawableResource;
    public String name;

    public MonoOrinMark(String name, Mark dpndM, int dpndBrng,double distance ,  int drawableResource){
        this.name=name;
        this.dpndMloc=dpndM;
        this.dpndBrng=dpndBrng;
        this.distance=distance;
        this.drawableResource=drawableResource;
        this.loc=null;
    }

    public MonoOrinMark(String name, LatLng dpndLoc, int dpndBrng,double distance ,  int drawableResource){
        this.name=name;
        this.dpndMloc=new LocOrinMark("invisible", dpndLoc);
        this.dpndBrng=dpndBrng;
        this.distance=distance;
        this.drawableResource=drawableResource;
        this.loc=null;
    }

    public String getName(){return name;}
    public int getDrawableResource(){return drawableResource;}
    public LatLng getLoc(){return this.loc;}

    public Mark LocIt(int windD){
        this.loc=headingDist(dpndMloc.getLoc(), windD-dpndBrng, distance);
        return this;
    }

    private LatLng headingDist(LatLng initial, int dir, double mark1dis){         //dir is from destination toward the initial point
        LatLng destination = new LatLng();
        double tc=Math.toRadians(-dir);
        double l2r = mark1dis/(3440.06479482);

        double lat1 = Math.toRadians(initial.getLatitude()), lng1 = Math.toRadians(initial.getLongitude());

        double lat2 =Math.asin(Math.sin(lat1)*Math.cos(l2r)+Math.cos(lat1)*Math.sin(l2r)*Math.cos(tc));
        double dlon=Math.atan2(Math.sin(tc)*Math.sin(l2r)*Math.cos(lat1),Math.cos(l2r)-Math.sin(lat1)*Math.sin(lat2));
        double lng2=(lng1-dlon +Math.PI)%(2*Math.PI)-Math.PI;

        destination.setLatitude(Math.toDegrees(lat2));
        destination.setLongitude(Math.toDegrees(lng2));
        return new LatLng(Math.toDegrees(lat2), Math.toDegrees(lng2));

    }

}
