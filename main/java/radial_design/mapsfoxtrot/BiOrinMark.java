package radial_design.mapsfoxtrot;

import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class BiOrinMark implements Mark {
    public LatLng loc;
    private Mark dpnd1;
    private Mark dpnd2;
    private int brng1;
    private int brng2;
    public int drawableResource;
    public String name;


    public BiOrinMark(String name, Mark dpnd1, Mark dpnd2, int brng1, int brng2, int drawableResource){
        this.name=name;
        this.brng1=brng1;
        this.brng2=brng2;
        this.dpnd1=dpnd1;
        this.dpnd2=dpnd2;
        this.drawableResource=drawableResource;
        this.loc=null;
    }

    public BiOrinMark(String name, LatLng dpnd1Loc, Mark dpnd2, int brng1, int brng2, int drawableResource){
        this.name=name;
        this.brng1=brng1;
        this.brng2=brng2;
        this.dpnd1=new LocOrinMark("invisible", dpnd1Loc);
        this.dpnd2=dpnd2;
        this.drawableResource=drawableResource;
        this.loc=null;
    }

    public String getName(){return name;}
    public int getDrawableResource(){return drawableResource;}
    public LatLng getLoc(){return this.loc;}

    public Mark LocIt(int windD){
        this.loc=biHeading(dpnd1.getLoc(), dpnd2.getLoc(), windD - brng1, windD - brng2);
        if(this.loc==null) Log.e("Null LatLng Created","Mark name "+name+" by method setBiOrientedLoc");
        else Log.i("valid LatLng Created", "Mark name " + name + " by method setBiOrientedLoc " + this.loc.toString());
        return this;
    }

    public LatLng biHeading(LatLng p1, LatLng p2, double brng1, double brng2){
        double lat1 = Math.toRadians(p1.getLatitude()), lon1 = Math.toRadians(p1.getLongitude());
        double lat2 = Math.toRadians(p2.getLatitude()), lon2 = Math.toRadians(p2.getLongitude());
        double brng13 = Math.toRadians(brng1), brng23 = Math.toRadians(brng2);
        double dLat = lat2 - lat1, dLon = lon2 - lon1;

        double dist12 = 2 * Math.asin(Math.sqrt(Math.sin(dLat / 2)
                * Math.sin(dLat / 2) + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2)));
        if (dist12 == 0) return null;
        Double brngA = Math.acos((Math.sin(lat2) - Math.sin(lat1) * Math.cos(dist12)) / (Math.sin(dist12) * Math.cos(lat1)));
        if (brngA.isNaN()) brngA = 0.0;
        Double brngB = Math.acos((Math.sin(lat1) - Math.sin(lat2) * Math.cos(dist12)) / (Math.sin(dist12) * Math.cos(lat2)));
        double brng12, brng21;
        if (Math.sin(lon2 - lon1) > 0) {
            brng12 = brngA;
            brng21 = 2 * Math.PI - brngB;
        } else {
            brng12 = 2 * Math.PI - brngA;
            brng21 = brngB;
        }
        double alpha1 = (brng13 - brng12 + Math.PI) % (2 * Math.PI) - Math.PI; // angle
        double alpha2 = (brng21 - brng23 + Math.PI) % (2 * Math.PI) - Math.PI; // angle

        double alpha3 = Math.acos(-Math.cos(alpha1) * Math.cos(alpha2)
                + Math.sin(alpha1) * Math.sin(alpha2) * Math.cos(dist12));
        double dist13 = Math.atan2(
                Math.sin(dist12) * Math.sin(alpha1) * Math.sin(alpha2),
                Math.cos(alpha2) + Math.cos(alpha1) * Math.cos(alpha3));
        double lat3 = Math.asin(Math.sin(lat1) * Math.cos(dist13)
                + Math.cos(lat1) * Math.sin(dist13) * Math.cos(brng13));
        double dLon13 = Math.atan2(
                Math.sin(brng13) * Math.sin(dist13) * Math.cos(lat1),
                Math.cos(dist13) - Math.sin(lat1) * Math.sin(lat3));
        double lon3 = lon1 + dLon13;
        lon3 = (lon3 + Math.PI) % (2 * Math.PI) - Math.PI;

        return new LatLng((float) Math.toDegrees(lat3), (float) Math.toDegrees(lon3));
    }
}
