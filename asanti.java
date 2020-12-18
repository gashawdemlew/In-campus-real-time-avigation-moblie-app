package com.example.JU_NAVIGATOR.JU_NAVIGATOR;

/* Copyright 2014 ESRI
 *
 * All rights reserved under the copyright laws of the United States
 * and applicable international laws, treaties, and conventions.
 *
 * You may freely redistribute and use this sample code, with or
 * without modification, provided you include the original copyright
 * notice and use restrictions.
 *
 * See the sample code usage restrictions document for further information.
 *
 */



        import android.app.Activity;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.os.Environment;

        import com.esri.android.map.GraphicsLayer;
        import com.esri.android.map.MapView;
        import com.esri.android.map.TiledLayer;
        import com.esri.android.map.ags.ArcGISLocalTiledLayer;
        import com.esri.android.map.event.OnStatusChangedListener;
        import com.esri.core.geometry.GeometryEngine;
        import com.esri.core.geometry.Point;
        import com.esri.core.geometry.SpatialReference;
        import com.esri.core.map.Graphic;
        import com.esri.core.symbol.SimpleMarkerSymbol;

/**
 * This sample shows how to set the license level of your ArcGIS application to Basic. Setting the license level to
 * Basic prevents the watermark from appearing on the map. In order to set the license level to Basic you need to edit
 * this code and assign a valid client id string to the CLIENT_ID constant.<p>
 * When you release your app, you should ensure that the client id is encrypted and saved to the device in a
 * secure manner; this sample uses a hardcoded string instead for simplicity of example code.<p>
 * Follow these steps:
 * <ol>
 * <li>Browse to https://developers.arcgis.com.</li>
 * <li>Sign in with your ArcGIS developer account.</li>
 * <li>Create an application. This will give you access to a client id string.</li>
 * <li>Initialize the CLIENT_ID constant with the client id string and run the sample. If the license level has been
 * successfully set to Basic you won't see a watermark on the map.<p>
 * <b>NOTE:</b> When you release your app, you should ensure that the client id is encrypted and saved to the
 * device in a secure manner; the code here uses a hardcoded string instead for simplicity.</li>
 * </ol>
 */
public class asanti extends Activity {

    GraphicsLayer g1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.search_layout);
        final MapView mMapView = (MapView) findViewById(R.id.map);
        TiledLayer mTileLayer = new ArcGISLocalTiledLayer(Environment.getExternalStorageDirectory().getPath() +"/Download/ArcGIS/Samples/OfflineRouting/utm.tpk");
        mMapView.addLayer(mTileLayer);
        g1=new GraphicsLayer();
        mMapView.addLayer(g1);


      /*  LocationListener l1=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
          System.out.println("loc found 4");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                System.out.println("loc found 5");
            }

            @Override
            public void onProviderEnabled(String provider) {
                System.out.println("loc found 6");
            }

            @Override
            public void onProviderDisabled(String provider) {
                System.out.println("loc found 7");
            }
        };
        LocationDisplayManager l=mMapView.getLocationDisplayManager();
        l.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
        l.setLocationListener(l1);

        l.start();
        if(l.isStarted()){
            System.out.println("loc found 8");
        }
        else
            System.out.println("loc found 9");

*/
        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void onStatusChanged(Object source, STATUS status) {
                if (source == mMapView && status == STATUS.INITIALIZED) {
                   //System.out.println();
                    Point p1=new Point(36.851917,7.680895);
                    //System.out.println("our spital= "+mMapView.getSpatialReference().getID());
                    p1= (Point) GeometryEngine.project(p1,SpatialReference.create(4326),SpatialReference.create(3857));
                    //System.out.println("pppp X= "+p1.getX());
                    //mMapView.toMapPoint(p1.getX(), p1.getY());
                    //Point p1=new Point(133.76978,322.59644);
                    //p1=mMapView.toMapPoint((float)p1.getX(),(float)p1.getY());

                   // double search_layout=p1.getX();
                   // p1.setX(p1.getY());
                   // p1.setY(search_layout);
                   // p1.setY(p1.getY()*-1);
        System.out.println("search_layout X value"+p1.getX());
        System.out.println("search_layout Y value"+p1.getY());
                    SimpleMarkerSymbol resultSymbol = new SimpleMarkerSymbol(Color.RED, 25, SimpleMarkerSymbol.STYLE.CROSS);
                    // create graphic object for resulting location
                    Graphic resultLocGraphic = new Graphic(p1, resultSymbol);
                    // add graphic to location layer
                    g1.addGraphic(resultLocGraphic);
                    //Graphic graphic = new Graphic(p1, new SimpleMarkerSymbol(Color.BLUE, 10, SimpleMarkerSymbol.STYLE.DIAMOND));
                    //g1.addGraphic(graphic);

                    System.out.println("position x "+p1.getX());//longtuid
                    System.out.println("position y "+ p1.getY());//latitude
                }
            }
        });


        //GraphicsLayer gl=new GraphicsLayer(GraphicsLayer.RenderingMode.DYNAMIC);
        //Point p1=new Point(7.681486,36.851289);
        //Point mapPoint = (Point) GeometryEngine
          //      .project(p1,
            //            SpatialReference.create(4326),
              //          mMapView.getSpatialReference());
        //Graphic graphic = new Graphic(p1, new SimpleMarkerSymbol(Color.GREEN, 20, SimpleMarkerSymbol.STYLE.DIAMOND));
        //gl.addGraphic(graphic);
        //mMapView.addLayer(gl);
        //Unit mapUnit = mMapView.getSpatialReference()
         //       .getUnit();
        //double zoomWidth = Unit.convertUnits(
         //       5,
         //       Unit.create(LinearUnit.Code.MILE_US),
         //       mapUnit);
        //Envelope zoomExtent = new Envelope(p1,
          //      zoomWidth, zoomWidth);
       // mMapView.setExtent(zoomExtent);

    }
}
