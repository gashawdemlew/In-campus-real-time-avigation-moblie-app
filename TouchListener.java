package com.example.JU_NAVIGATOR.JU_NAVIGATOR;

import android.content.Context;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.tasks.geocode.LocatorReverseGeocodeResult;
import com.esri.core.tasks.na.NAFeaturesAsFeature;
import com.esri.core.tasks.na.Route;
import com.esri.core.tasks.na.RouteDirection;
import com.esri.core.tasks.na.RouteParameters;
import com.esri.core.tasks.na.RouteResult;
import com.esri.core.tasks.na.StopGraphic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by asanti on 4/17/2015.
 */


class DirectionsItemListener implements AdapterView.OnItemSelectedListener {

    List<RouteDirection> mDirections;
    MapView m1;

    public DirectionsItemListener(List<RouteDirection> directions, MapView m2) {
        mDirections = directions;
        m1 = m2;


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // We have to account for the added summary String
        if (mDirections != null && pos > 0 && pos <= mDirections.size()) {
            Graphic gr;
            GraphicsLayer g1 = (GraphicsLayer) m1.getLayer(2);

            m1.setExtent(mDirections.get(pos - 1).getGeometry());
            System.out.println("the geometry is "+mDirections.get(pos-1).getGeometry().getType().name());
            if(mDirections.get(pos-1).getGeometry().getType().name()=="POINT"){
                SimpleMarkerSymbol symbol=new SimpleMarkerSymbol(Color.BLUE,20, SimpleMarkerSymbol.STYLE.CROSS);
                gr=new Graphic(mDirections.get(pos-1).getGeometry(),symbol);
            }
            else{
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.RED, 3, SimpleLineSymbol.STYLE.DASH);
            gr= new Graphic(mDirections.get(pos - 1).getGeometry(), lineSymbol);
                Polyline p = (Polyline) mDirections.get(pos - 1).getGeometry();
                System.out.println("path count =" + p.getPathCount());
                int index = p.getPathStart(0);
                Graphic graphic = new Graphic(p.getPoint(0), new SimpleMarkerSymbol(Color.BLUE, 20, SimpleMarkerSymbol.STYLE.DIAMOND));
                g1.addGraphic(graphic);

                }
            g1.addGraphic(gr);

            System.out.println("the geometry is " + mDirections.get(pos - 1).getGeometry().getType().name());

            //m1.addLayer(g1);


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
}

class TouchListener extends MapOnTouchListener {

    MainActivity m1;//=new MainActivity();
    Context c1;
    private int routeHandle = -1;
    DirectionsItemListener d1;

    @Override
    public void onLongPress(MotionEvent point) {
        // Our long press will clear the screen
        m1.mStops.clearFeatures();
        m1.mGraphicsLayer.removeAll();
        m1.mMapView.getCallout().hide();
    }

    @Override
    public boolean onSingleTap(MotionEvent point) {

        if (m1.mLocator == null) {
            m1.popToast("Locator uninitialized", true);
            return super.onSingleTap(point);
        }
        // Add a graphic to the screen for the touch event

        System.out.println("point X= "+point.getX());
        System.out.println("point Y= "+point.getY());
        Point mapPoint = m1.mMapView.toMapPoint(point.getX(), point.getY());
        //Point point1= (Point) GeometryEngine.project(mapPoint,m1.mapview.getSpatialReference(),SpatialReference.create(4326));

        System.out.println("possesion X"+mapPoint.getX());
        System.out.println("possesion Y"+mapPoint.getY());
        Graphic graphic = new Graphic(mapPoint, new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.DIAMOND));
        m1.mGraphicsLayer.addGraphic(graphic);

        String stopAddress = "";
        try {
            //
            // Attempt to reverse geocode the point.
            // Our input and output spatial reference will
            // be the same as the map.
            SpatialReference mapRef = m1.mMapView.getSpatialReference();
            System.out.println("when touch spital = "+mapRef.getText());
            System.out.println("when touch pointX = "+mapPoint.getX());
            System.out.println("when touch pointy = "+mapPoint.getY());
            LocatorReverseGeocodeResult result = m1.mLocator.reverseGeocode(mapPoint, 50, mapRef, mapRef);

            mapPoint.getX();

            // Construct a nicely formatted address from the results
            StringBuilder address = new StringBuilder();

            if (result != null && result.getAddressFields() != null) {
                Map<String, String> addressFields = result.getAddressFields();
                for (Map.Entry<String, String> entry : addressFields.entrySet())
                    address.append(entry.getValue());
                //!= null ? entry.getValue() + " " : ""
            }

              /*if (result != null && result.getAddressFields() != null) {
                    Map<String, String> addressFields = result.getAddressFields();
                    address.append(String.format("%s\n%s, %s %s", addressFields.get("Street"), addressFields.get("City"),
                            addressFields.get("State"), addressFields.get("ZIP")));
                }*/
            // Show the results of the reverse geocoding in
            // the map's callout.
            stopAddress = address.toString();
            m1.tts.speak(stopAddress, TextToSpeech.QUEUE_FLUSH,null);
            m1.showCallout(stopAddress, mapPoint);
            System.out.println("gggeeyy X="+mapPoint.getX());
            System.out.println("gggeeyy Y="+mapPoint.getY());

        } catch (Exception e) {
            Log.v("Reverse Geocode", e.getMessage());
        }

        // Add the touch event as a stop
        StopGraphic stop = new StopGraphic(graphic);
        stop.setName(stopAddress);
        m1.mStops.addFeature(stop);

        return true;
    }

    List<Point> points;


    @Override
    public boolean onDoubleTap(MotionEvent point) {

        // Return default behavior if we did not initialize properly.
        route();
        return true;
    }

    public TouchListener(Context context, MapView view, MainActivity m) {
        super(context, view);
        m1 = m;
        c1 = context;
    }

    public void route(){
        System.out.println("at line rone");
        if (m1.mRouteTask == null) {
            m1.popToast("RouteTask uninitialized.", true);
            //return null;//super.onDoubleTap(point);
        }
        if(m1.mStops.getFeatures().size()==1)
            m1.gps_location();
        try {
            m1.main_counter=1;
            // Set the correct input spatial reference on the stops and the
            // desired output spatial reference on the RouteParameters object.

            SpatialReference mapRef = m1.mMapView.getSpatialReference();
            System.out.println("ref is "+mapRef.getLatestID()+"and old is "+mapRef.getID());
            RouteParameters params = m1.mRouteTask.retrieveDefaultRouteTaskParameters();
            params.setOutSpatialReference(mapRef);
            //Point barrier=new Point(4102404.7484810953,857766.6769565246);







            System.out.println("sex value is "+m1.sex);
            if(m1.sex.compareTo("FEMALE")==0)
                System.out.println("the intent value of sex isFEMALE is true");
                Polygon po = new Polygon();

                Point p1 = new Point(4102363.9071817156, 857772.619643);
                Point p2 = new Point(4102416.5557467686, 857799.2401230272);
                Point p3 = new Point(4102436.1589281, 857744.31787604);
                po.startPath(p1);
                po.lineTo(p2);
                po.lineTo(p3);
                Graphic ba_gr = new Graphic(po, null);

                //m1.mGraphicsLayer.addGraphic(ba_gr);
                NAFeaturesAsFeature na = new NAFeaturesAsFeature();
           //     na.addFeature(ba_gr);
                //na.getSpatialReference()
                if (mapRef == na.getSpatialReference()) {
                    System.out.println("they have the same spital");
                } else {
                    System.out.println("they have the same spital not");
                }

               /*Point mLocation = new Point(-263007.321477015,849599.0226428931);
                Point mEnd = new Point(-263086.43474752136,849673.5044312371);

                StopGraphic point1 = new StopGraphic(mLocation);
                StopGraphic point2 = new StopGraphic(mEnd);

                m1.mStops.setFeatures(new Graphic[] { point1, point2 });
                m1.mStops.setCompressedRequest(true);
                 */
                na.setSpatialReference(mapRef);
                ArrayList<Point> male_barrier=new ArrayList<Point>();
                ArrayList<Point> female_barrier=new ArrayList<Point>();
            ArrayList<Point> disable_barrier=new ArrayList<Point>();
            ArrayList<Point> car_barrier=new ArrayList<Point>();

            male_barrier.add(new Point(4102521.6316308966,857608.553785146));
                male_barrier.add(new Point(4102464.9584236764,857577.7106948268));
                male_barrier.add(new Point(4102571.320820012,857525.0819628091));
                male_barrier.add(new Point(4102591.775140738,857484.733288503));
                //male_barrier.add(new Point(263281.2,849540));
                //male_barrier.add(new Point(263169.56,849565.83));


                female_barrier.add(new Point(4102599.3724522106,857533.8534121922));
                female_barrier.add(new Point(4102604.6321321316,857445.5541264937));
                female_barrier.add(new Point(4102722.791218484,857557.2439795999));
                female_barrier.add(new Point(4102853.357897327,857668.5440667121));
                female_barrier.add(new Point(4102907.7078934154,857759.434481879));
                female_barrier.add(new Point(4102608.490536885,857388.9035752079));
                female_barrier.add(new Point(4102662.8405508287,857467.846655375));
                female_barrier.add(new Point(4102545.9588035843,857461.4142305858));
                female_barrier.add(new Point(4102586.2830103585,857419.8960439828));
                female_barrier.add(new Point(4102670.6933286153,857583.7427993077));


 disable_barrier.add(new Point(4102273.9856826128,857436.8746583996));
 disable_barrier.add(new Point(4102240.5861404017,857482.9144446275));
 disable_barrier.add(new Point(4102224.9224800956,857439.7623407061));
 disable_barrier.add(new Point(4102275.2699521002,857564.9440329649));
 disable_barrier.add(new Point(4102370.337666102,857544.5488398983));
 disable_barrier.add(new Point(4102336.1624123384,857516.8663564954));
 disable_barrier.add(new Point(4102326.5341895167,857486.5377629542));
 disable_barrier.add(new Point(4102337.722507648,857435.785438907));
 disable_barrier.add(new Point(4102519.982148629,857489.6985113045));
            disable_barrier.add(new Point(4102478.2386262007,857423.1666685869));
            disable_barrier.add(new Point(4102479.9154330716,857541.903363588));
            disable_barrier.add(new Point(4102544.0128673348,857464.9171486378));
            disable_barrier.add(new Point(4102640.4304294493,857516.8755000867));

            disable_barrier.add(new Point(4102599.342807278,857530.2298342648));
            disable_barrier.add(new Point(4102572.0008622594,857531.7973156364));
            disable_barrier.add(new Point(4102498.192560771,857599.950432844));
            disable_barrier.add(new Point(4102897.277089166,857789.2077352887));

            disable_barrier.add(new Point(4102511.8581631305,857674.7017460159));
            disable_barrier.add(new Point(4102487.8032246903,857741.9768539396));
            disable_barrier.add(new Point(4102419.6937944917,857653.5836948563));
            disable_barrier.add(new Point(4102441.806405364,857718.4667976637));

            disable_barrier.add(new Point(4102495.185954275,857893.6442669701));
            disable_barrier.add(new Point(4102296.2822652324,857813.8212081147));
            disable_barrier.add(new Point(4102360.5283587114,857748.7886161183));
            disable_barrier.add(new Point(4102265.0562827378,857756.5793240456));









            car_barrier.add(new Point(4102226.1101901405,857436.6201324537));//1

            car_barrier.add(new Point(4102250.579713551,857439.0088490053 ));//2

            car_barrier.add(new Point(4102247.2972128936,857485.4395879613       ));//3

            car_barrier.add(new Point(4102276.9888910525,857354.5078927029));//4

            car_barrier.add(new Point(4102417.666931767,857368.5416241025));//5

            car_barrier.add(new Point(4102471.8792314045,857412.569876271      ));//6

            car_barrier.add(new Point(4102271.9555686773,857534.2105102282       ));//7

            car_barrier.add(new Point(4102270.165118749,857555.4573195184));//8


            car_barrier.add(new Point(4102290.904531299,857551.8742401315));//9

            car_barrier.add(new Point(4102334.3230082113,857588.4511345997  ));//10

            car_barrier.add(new Point(4102282.8416337497,857619.3551931728));//11

            car_barrier.add(new Point(4102388.62754564,857549.608799117));//12

            car_barrier.add(new Point(4102394.74492763544,857492.2918035872       ));//13

            car_barrier.add(new Point(4102520.737373607,857492.3543441801    ));//14

            car_barrier.add(new Point(4102480.1537774694,857542.378540358));//15

            car_barrier.add(new Point(4102457.474705131,857555.5164950711));//16

            car_barrier.add(new Point(4102420.0372073585,857649.7216380131       ));//17

            car_barrier.add(new Point(4102507.0979754045,857652.1574013532      ));
            car_barrier.add(new Point(4102554.0634623547,857623.9400664035));//12

            car_barrier.add(new Point(4102569.224602233,857544.6306078339 ));//13

            car_barrier.add(new Point(4102675.180175291,857511.523475562));//14

            car_barrier.add(new Point(4102742.565297518,857576.6160950646));//15

            car_barrier.add(new Point(4102742.266893759,857726.6882549857  ));//16

            car_barrier.add(new Point(4102605.446454873,857696.8292388215 ));//17

            car_barrier.add(new Point(4102906.5409256066,857758.4704670851));

            car_barrier.add(new Point(4102441.867527075,857723.1463066064));//17

            car_barrier.add(new Point(4102330.2626324035,857750.1687087634));

            car_barrier.add(new Point(4102250.988666394,857776.200574943));//17

            car_barrier.add(new Point(4102224.579487753,857804.268028621));

            car_barrier.add(new Point(4102248.9610834173,857830.9171889713));//17

            car_barrier.add(new Point(4102222.1042934344,857831.365068195));

            car_barrier.add(new Point(4102384.7385636903,857900.1357771603));//17

            car_barrier.add(new Point(4102469.6358702644,857916.259652641));

            car_barrier.add(new Point(4102447.553620268,857875.7529424058));//17

            car_barrier.add(new Point(4102533.215243371,857806.7310269403));

            car_barrier.add(new Point(4102366.424125079,857393.6438798325));



            if((m1.tog3.isChecked()==true)&&(m1.tog1.isChecked()==true)){

                int count=0;
                System.out.println("the size of male is "+male_barrier.size());
                while(count<male_barrier.size()){
                    //Graphic barrier = new Graphic(male_barrier.get(count++), null);

                    Graphic barrier = new Graphic(male_barrier.get(count++), new SimpleMarkerSymbol(Color.GREEN, 20, SimpleMarkerSymbol.STYLE.DIAMOND));
                    //m1.mGraphicsLayer.addGraphic(barrier);

                    na.addFeature(barrier);
                }
                   int des=0;
                while(des<disable_barrier.size()){
                    //Graphic barrier = new Graphic(male_barrier.get(count++), null);

                    Graphic barrier = new Graphic(disable_barrier.get(des++), new SimpleMarkerSymbol(Color.GREEN, 20, SimpleMarkerSymbol.STYLE.DIAMOND));
                    //m1.mGraphicsLayer.addGraphic(barrier);

                    na.addFeature(barrier);
                }









            }



           else if((m1.tog3.isChecked()==true)&&(m1.tog2.isChecked()==true)){

                int count=0;
                System.out.println("the size of male is "+male_barrier.size());
                while(count<female_barrier.size()){
                    //Graphic barrier = new Graphic(male_barrier.get(count++), null);

                    Graphic barrier = new Graphic(female_barrier.get(count++), new SimpleMarkerSymbol(Color.GREEN, 20, SimpleMarkerSymbol.STYLE.DIAMOND));
                    //m1.mGraphicsLayer.addGraphic(barrier);

                    na.addFeature(barrier);
                }
                int des=0;
                while(des<disable_barrier.size()){
                    //Graphic barrier = new Graphic(male_barrier.get(count++), null);

                    Graphic barrier = new Graphic(disable_barrier.get(des++), new SimpleMarkerSymbol(Color.GREEN, 20, SimpleMarkerSymbol.STYLE.DIAMOND));
                    //m1.mGraphicsLayer.addGraphic(barrier);

                    na.addFeature(barrier);
                }









            }











            else if(m1.tog1.isChecked()==true){

                    int count=0;
                    System.out.println("the size of male is "+male_barrier.size());
                    while(count<male_barrier.size()){
                        //Graphic barrier = new Graphic(male_barrier.get(count++), null);

                        Graphic barrier = new Graphic(male_barrier.get(count++), new SimpleMarkerSymbol(Color.GREEN, 20, SimpleMarkerSymbol.STYLE.DIAMOND));
                        //m1.mGraphicsLayer.addGraphic(barrier);

                        na.addFeature(barrier);
                    }
                }else if(m1.tog2.isChecked()==true){
                    int count=0;
                    System.out.println("the size of female is "+male_barrier.size());
                    while(count<female_barrier.size()){
                        Graphic barrier = new Graphic(female_barrier.get(count++), null);
                        na.addFeature(barrier);
                }}

            else if(m1.tog4.isChecked()==true){
                int count=0;
                System.out.println("the size of female is "+male_barrier.size());
                while(count<car_barrier.size()){
                    Graphic barrier = new Graphic(car_barrier.get(count++), null);
                    na.addFeature(barrier);
                }}






                params.setPointBarriers(na);
                    try{System.out.println("the barriers are "+params.getPointBarriers().toJson().toString());}
                    catch (Exception ee){System.out.println("the barriers are "+ee);}
                //params.setReturnPolygonBarriers(true);
                // System.out.println("barriers are "+params.getPolygonBarriers().toJson().toString());
                // System.out.println("barriers are "+params.isReturnPolygonBarriers());

                m1.mStops.setSpatialReference(mapRef);

            System.out.println("spatial reference   :-" + mapRef.getText());
            if (m1.mStops == null) {
                System.out.println("they are empty");
            } else {
                System.out.println("url is"+m1.mStops.getURL());
            }
            // Set the stops and since we want driving directions,
            // returnDirections==true
           if(m1.mStops==null){
               System.out.println("mstops are null");
           }
            params.setStops(m1.mStops);
            params.setReturnDirections(true);

            // Perform the solve
            System.out.println("dave is 9");

            //System.out.println("dok " + params.getStops().toJson());
            System.out.println("dok " + params.getDirectionsLanguage());
            System.out.println("dok " + params.getImpedanceAttributeName());
            //System.out.println("dokkk " + m1.mRouteTask.getNetworkDescription().getName());
            System.out.println("nev is1");
            RouteResult results = null;
            try {
                results = m1.mRouteTask.solve(params);
            } catch (Exception ed) {
                System.out.println("diffrent" + ed.getMessage());
                ed.printStackTrace();
            }

            System.out.println("dave is 2");
            // Grab the results; for offline routing, there will only be one
            // result returned on the output.

            Route result = null;
            System.out.println("the possibility are "+results.getRoutes().size());
            if (results != null) {
                result = results.getRoutes().get(0);
            } else {
                System.out.println("no route return");
            }
            // List<Route> routes = results.getRoutes();
            // Route result = routes.get(0);
            // Remove any previous route Graphics
            if (routeHandle != -1)
                m1.mGraphicsLayer.removeGraphic(routeHandle);
            System.out.println("nev is3");
            // Add the route shape to the graphics layer
            System.out.println("nesserary info name= "+result.getRouteName());
            int distance= (int) (result.getTotalKilometers()*1000);

            //m1.tts.speak("distance of route in meters is = "+distance,TextToSpeech.QUEUE_FLUSH,null);
            System.out.println("nesserary info minutes= "+result.getTotalMinutes());
            Geometry geom = result.getRouteGraphic().getGeometry();
            routeHandle = m1.mGraphicsLayer.addGraphic(new Graphic(geom, new SimpleLineSymbol(Color.GREEN, 5)));
            m1.mMapView.getCallout().hide();

            // Get the list of directions from the result
            List<RouteDirection> directions = result.getRoutingDirections();
            System.out.println("nev is4");
            // enable spinner to receive directions
            m1.dSpinner.setEnabled(true);

            // Iterate through all of the individual directions items and
            // create a nicely formatted string for each.
            System.out.println("dave is 33");
            List<String> formattedDirections = new ArrayList<String>();

            for (int i = 0; i < directions.size(); i++) {
                RouteDirection direction = directions.get(i);
                formattedDirections.add(String.format("%s\nGo %.2f %s For %.2f Minutes", direction.getText(),
                        direction.getLength(), params.getDirectionsLengthUnit().name(), direction.getMinutes()));
            }

            System.out.println("dave is 44");
            // Add a summary String
            Double dd=result.getTotalMinutes();
            int ii=dd.intValue();
            Double hh= Double.valueOf(distance);
            Double gg=hh/1000;
            formattedDirections.add(0, "time ="+ii+" minutes distance ="+ii+" kilometers");

            // Create a simple array adapter to visualize the directions in
            // the Spinner
            System.out.println("dave is 55");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(c1.getApplicationContext(),
                    android.R.layout.simple_spinner_item, formattedDirections);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            m1.dSpinner.setAdapter(adapter);


            // Add a custom OnItemSelectedListener to the spinner to allow
            // panning to each directions item.
            d1=new DirectionsItemListener(directions,m1.mMapView);

            //  m1.dSpinner.getRootView().setAlpha(0);

            for(int i=0;i<directions.size();i++){
                if(directions.get(i).getGeometry().getType().name()=="POLYLINE"){
                Polyline poly= (Polyline) directions.get(i).getGeometry();
                Point point=poly.getPoint(0);
                System.out.println("points are "+point.getX());
                   m1.paths.add(point);}
                else{m1.paths.add((Point) directions.get(i).getGeometry());}
            }

            //m1.paths=points;
            m1.directions=directions;
            m1.dSpinner.setOnItemSelectedListener(d1);
            System.out.println("dave is 66");
         }catch (Exception e) {
            m1.popToast("Solve Failed: " + e.getMessage(), true);
            System.out.println("error1 is "+e);
        }
    }





}

