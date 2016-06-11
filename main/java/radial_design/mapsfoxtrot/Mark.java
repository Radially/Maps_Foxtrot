package radial_design.mapsfoxtrot;

import com.mapbox.mapboxsdk.geometry.LatLng;

public interface Mark {
    LatLng loc =null;
    int drawableResource =0;
    String name = "default";

    Mark LocIt(int windD);
    LatLng getLoc();
    int getDrawableResource();
    String getName();
}
