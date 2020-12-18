package com.example.JU_NAVIGATOR.JU_NAVIGATOR;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;

import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.tasks.na.NAFeaturesAsFeature;

/**
 * Created by asanti on 5/12/2015.
 */
public class second {
    
    MainActivity m1;
    public second(MainActivity m){
        m1=m;
    }
    
    
    
    public void start(){
        m1.mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {

            private static final long serialVersionUID = 1L;
            int count = 1;

            @Override
            public void onStatusChanged(Object source, STATUS status) {

                if (source == m1.mMapView && status == STATUS.INITIALIZED) {
                    System.out.println("relax mapview loadded");
                    m1.lDisplayManager = m1.mMapView.getLocationDisplayManager();
                    m1.lDisplayManager.setShowLocation(true);
                    m1.lDisplayManager.setAccuracyCircleOn(true);
                    m1.lDisplayManager.setAllowNetworkLocation(true);
                    m1.lDisplayManager.setLocationListener(new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            // if((count==1)&&(m1.lDisplayManager.isStarted()==true)){
                            //Point pp=new Point(location.getLatitude(),location.getLongitude());
                            //Location loc=m1.lDisplayManager.getLocation();
                            //Point po=m1.lDisplayManager.getPoint();
                            Point pp = m1.lDisplayManager.getPoint();
                            if (m1.paths != null) {
                                System.out.println("the path size is " + m1.paths.size());
                                int temp_counter = 0;
                                for (int i = 1; i < m1.paths.size(); i++) {

                                    System.out.println("the point is " + m1.paths.get(i).getX() + "  and  " + m1.paths.get(i).getY());
                                    Double far_distance = GeometryEngine.distance(m1.paths.get(i), pp, SpatialReference.create(4326));
                                    if (i == m1.paths.size()) {
                                        if (far_distance < 8)
                                            m1.popToast("u have reached ur destination", true);
                                        if ((far_distance < 8) && (m1.main_counter == 1)) {
                                            m1.tts.speak("u have reached ur destination", TextToSpeech.QUEUE_FLUSH, null);
                                        }
                                        m1.main_counter++;
                                    } else if (far_distance <= 8) {//&&(temp_counter==0)){
                                        m1.popToast(m1.directions.get(i).getText(), true);
                                    }
                                }
                            }
                        }
                                    //temp_counter++;}}

                            //Point pp=new Point(loc.getLatitude(),loc.getLongitude());
                            //Point pp = new Point(4102255.421241852, 857676.2648124732);
                            //   Point p2=(Point) GeometryEngine
                            //          .project(po,
                            //                SpatialReference.create(3857),
                            //               SpatialReference.create(4326));
                            //Point pd=p2;
                            // Double search_layout=p2.getX();
                            // p2.setX(p2.getY());
                            // p2.setY(search_layout);
                            // Point p3=(Point) GeometryEngine
                            //         .project(p2,
                            //                 SpatialReference.create(4326),
                            //                 SpatialReference.create(32637));
                            // SimpleMarkerSymbol symbol=new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.DIAMOND);

                            //System.out.println("X iss= "+p3.getX()+" and y iss ="+p3.getY()+" "+m1.mMapView.getLocationDisplayManager().isShowLocation());

                            //  Graphic graphic = new Graphic(p2, new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.DIAMOND));

                            //6.65428                         //m1.mGraphicsLayer.addGraphic(graphic);
                            //                    count=0;}

                        //}

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
                    m1.lDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);

                    System.out.println("found 3");

                    m1.lDisplayManager.start();

                }

            }
        });


        m1.mMapView.setOnTouchListener(m1.t1);
        m1.initializeRoutingAndGeocoding();


        /*m1.clear.setBackgroundColor(Color.GREEN);
        m1.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m1.sex=" ";
                m1.mStops.clearFeatures();
                m1.mGraphicsLayer.removeAll();
                m1.mMapView.getCallout().hide();
            }
        });
*/
        if(m1.i1!=null){
       m1.i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //m1.mStops.clearFeatures();
               // m1.mGraphicsLayer.removeAll();
               // m1.mMapView.getCallout().hide();
            }
        });}
        else System.out.println("m1.i1.i2 is not null");
      /*  m1.route.setBackgroundColor(Color.YELLOW);
        m1.route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setContentView(R.layout.route);
                Intent intent=new Intent(m1,gps.class);
                intent.putExtra("source",m1.gps_location());
                m1.startActivityForResult(intent,1);


            }
        });

        m1.b1.setBackgroundColor(Color.RED);
        m1.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                m1.gps_location();





















            }
        });*/
        //      if (licenseResult == LicenseResult.VALID && licenseLevel == LicenseLevel.BASIC) {
        //         System.out.println("roba sucess");
        //        // MessageDialogFragment.showMessage(getString(R.string.basic_license_succeeded), getFragmentManager());
        //   } else {
        //      System.out.println("roba failed");
        //MessageDialogFragment.showMessage(getString(R.string.valid_client_id_required), getFragmentManager());
        // }

//        System.out.println("File3 exists");




        //   LocationManager locationManager = (LocationManager)
        //         this.getSystemService(Context.LOCATION_SERVICE);


        ///    Uri path = Uri.parse("file:///android_asset/roba.txt");


        m1.mStops= new NAFeaturesAsFeature();

    }
    
        
}
