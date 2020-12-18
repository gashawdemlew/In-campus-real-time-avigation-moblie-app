package com.example.JU_NAVIGATOR.JU_NAVIGATOR;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.TiledLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.tasks.geocode.Locator;
import com.esri.core.tasks.geocode.LocatorFindParameters;
import com.esri.core.tasks.geocode.LocatorReverseGeocodeResult;
import com.esri.core.tasks.na.NAFeaturesAsFeature;
import com.esri.core.tasks.na.RouteDirection;
import com.esri.core.tasks.na.RouteTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends ActionBarActivity implements TextToSpeech.OnInitListener {

    ImageButton help;
    List<Point> paths = new ArrayList<>();
    List<RouteDirection> directions;
    LocationDisplayManager lDisplayManager;
    MapView mMapView;
    TextToSpeech tts;
    final String tpkPath1 = "/ArcGIS/dan.tpk";
    final String extern = Environment.getExternalStorageDirectory().getPath();
    final String tpkPath = "/ArcGIS/sister.tpk";

    TiledLayer mTileLayer;
    GraphicsLayer mGraphicsLayer = new GraphicsLayer();
    String sex="";
    RouteTask mRouteTask = null;
    NAFeaturesAsFeature mStops;
    Locator mLocator = null;
    View mCallout = null;
    Spinner dSpinner;
    LocationListener l1;
    Button clear;//= (Button) findViewById(R.id.button3);
    Button route;//= (Button) findViewById(R.id.button2);
    Button b1;//= (Button) findViewById(R.id.button);
    TouchListener t1;
    int main_counter;
    List<Point> near_bys;



   /* private void copyAssets() {

        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("dave");
            for(int i=0;i<files.length;i++)
                System.out.println("files are "+files[i]);
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for(String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(getExternalFilesDir(null), filename);
                System.out.println("the file path is "+outFile.getPath());
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }*/
    public void initializeRoutingAndGeocoding() {

        // We will spin off the initialization in a new thread
        new Thread(new Runnable() {

          @Override
          public void run() {
         try {
             mLocator = Locator.createLocalLocator(Environment.getExternalStorageDirectory().getPath() + "/Arcgis/Geocoding/JimmaLocator.loc");
             // mRouteTask = RouteTask.createLocalRouteTask(Environment.getExternalStorageDirectory().getPath() + "/Download/ss/almaz.geodatabase", "street_ND");
             mRouteTask = RouteTask.createLocalRouteTask(Environment.getExternalStorageDirectory().getPath() + "/ArcGIS/Routing/univ.geodatabase", "jimma_ND");
         }catch (Exception e){popToast("Locator or navigation data not found",true); }


    }
     }).start();
    }







    public String gps_location(){


    //  Polygon po=mMapView.getExtent();
  //      Polygon poo=new Polygon();
    //    poo.
        //    System.out.println("polygon type is"+po.getPointCount());
//System.out.println("the scale is " + mMapView.getScale());
//for(int i=0;i<10;i++)
  //  mMapView.zoomin();
//for(int i=0;i<po.getPointCount();i++){
 //   Graphic graphic = new Graphic(po.getPoint(i), new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.DIAMOND));
  //  mGraphicsLayer.addGraphic(graphic);
//}
       Point pp = lDisplayManager.getPoint();
        if(pp!=null){
      //..  Point pp=new Point(263238.38,849620.51);
       //Point pp = new Point(4102255.421241852, 857676.2648124732);


       // Point p2=new Point(4102385.220408512,857248.2199544971);
       // System.out.println("distance b/n the two is "+GeometryEngine.distance(pp,p2,SpatialReference.create(4326)));

        //Point p1=new Point(66.9969,6.65428); //i took this coordinate from my gps device for demonstration
      //   pp=(Point) GeometryEngine
      //          .project(pp,
      //                  SpatialReference.create(4326),
      //                  mMapView.getSpatialReference());
        /*Point p3=(Point) GeometryEngine
                .project(p1,
                        SpatialReference.create(4326),
                        SpatialReference.create(32637));
        System.out.println("X is= "+p2.getX()+" and y is ="+p2.getY());
*/
        Graphic graphic = new Graphic(pp, new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.DIAMOND));
        mGraphicsLayer.addGraphic(graphic);
        mStops.addFeature(graphic);
        String stopAddress = "";


        try {
            //
            // Attempt to reverse geocode the point.
            // Our input and output spatial reference will
            // be the same as the map.

            SpatialReference mapRef = mMapView.getSpatialReference();

            LocatorReverseGeocodeResult result=null;
            try{
                result = mLocator.reverseGeocode(pp, 50, mapRef, mapRef);}
            catch (Exception e){System.out.println("mlocator error ="+e);}


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
            tts.speak("u have reached "+stopAddress,TextToSpeech.QUEUE_FLUSH,null);
            showCallout(stopAddress, pp);


        } catch (Exception e) {
            Log.v("Reverse Geocode", e.getMessage());
        }
        return stopAddress;}
    else {
            tts.speak("ur gps is off or ur not inside main campus ",TextToSpeech.QUEUE_FLUSH,null);
            popToast("ur gps is off or ur not inside main campus ",true);
        return null;}
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        String r1=data.getStringExtra("r1");
        String r2=data.getStringExtra("r2");
        System.out.println("the intent value is "+data.getStringExtra("destination")+"value of r2 "+r2);
        System.out.println("the intent value is "+data.getStringExtra("source"));
        System.out.println("the intent value of sex is"+data.getStringExtra("sex"));
        sex=data.getStringExtra("sex");
        System.out.println("the intent value is "+data.getStringExtra("source")+"source is "+data.getStringExtra("source"));
        if((r1.compareTo("GPS")==0)&&(data.getStringExtra("source"))!=null)
        executeLocatorTask(data.getStringExtra("source"),5);
        if(r2.compareTo("FROM TEXT")==0)
        executeLocatorTask(data.getStringExtra("destination"),5);
        if(mStops==null){
            System.out.println("mstops are null");
        }
       // TouchListener t1=new TouchListener(MainActivity.this,mMapView,this);
       // try{System.out.println("url is"+mStops.toJson());}
       // catch (Exception e){System.out.println("url is not "+e);}
       // System.out.println("the size of feature is "+mStops.getFeatures().size());
       //t1.route();


    }

    MapView mapview;
    ArcGISLocalTiledLayer mTileLayer1;

    public void onInit(int status) {
        // TODO Auto-generated method stub

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.ENGLISH);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                result = tts.setLanguage(Locale.UK);


                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    result = tts.setLanguage(Locale.US);



                }





                Toast.makeText(this, "Language not supported", Toast.LENGTH_LONG).show();
                Log.e("TTS", "Language is not supported");
            } else {
            //    btnSpeak.setEnabled(true);

            }

        } else {
            Log.e("TTS", "Initilization Failed");
        }

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routing_and_geocoding);

        new eulasample(this).show();
        help=(ImageButton)findViewById(R.id.help);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewhelp();
            }
        });

    // LicenseResult licenseResult = ArcGISRuntime.setClientId("MbYgrEmjOW84tEy0");

//        LicenseLevel licenseLevel = ArcGISRuntime.License.getLicenseLevel();
        //mapview= (MapView) findViewById(R.id.map1);
        tts=new TextToSpeech(this,this);
//        tts.setLanguage(Locale.US);
        //copyAssets();
        File file=new File(Environment.getExternalStorageDirectory().getPath()+"/ArcGIS");
        if(file.exists()==false) {
            copyAssetFolder(getAssets(), "ArcGIS",
                    Environment.getExternalStorageDirectory().getPath() + "/ArcGIS");
        }
            near_bys=new ArrayList<Point>();
        mMapView = (MapView) findViewById(R.id.map);
        mTileLayer= new ArcGISLocalTiledLayer(extern + tpkPath);
        mTileLayer1= new ArcGISLocalTiledLayer(extern + tpkPath1);
        mMapView.addLayer(mTileLayer,0);
        mMapView.addLayer(mTileLayer1,1);
        mMapView.addLayer(mGraphicsLayer,2);
       // ListView lv= (ListView) findViewById(R.id.listView);
        //lv.bringToFront();

        mMapView.getLayer(1).setVisible(false);
        //mapview.addLayer(mGraphicsLayer);
        mMapView.setMapBackground(Color.DKGRAY, Color.BLACK, 0, 0);
        mMapView.setMinScale(19255.725615194548);
   // System.out.println("spital reference value is "+mMapView);
        //mMapView.setVisibility(View.INVISIBLE);
        //mapview.setVisibility(View.VISIBLE);
        //mapview.bringToFront();
        /*ArrayList<Point> male_barrier=new ArrayList<Point>();
        ArrayList<Point> female_barrier=new ArrayList<Point>();
        male_barrier.add(new Point(263238.38,849620.51));
        male_barrier.add(new Point(263200.92,849564.34));
        male_barrier.add(new Point(263287.63,849482.84));
        male_barrier.add(new Point(263281.2,849540));
        male_barrier.add(new Point(263169.56,849565.83));
        int count=0;

        Graphic graphic = new Graphic(pp, new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.DIAMOND));
                //   mGraphicsLayer.addGraphic(graphic);










        System.out.println("the size of male is "+male_barrier.size());
        while(count<male_barrier.size()){
            //Graphic barrier = new Graphic(male_barrier.get(count++), null);

            Graphic barrier = new Graphic(male_barrier.get(count++), new SimpleMarkerSymbol(Color.GREEN, 20, SimpleMarkerSymbol.STYLE.DIAMOND));
            mGraphicsLayer.addGraphic(barrier);


        }
*/
        //mMapView.setFitsSystemWindows(true);
      ///  Point poo=new Point(7.680350,36.852222);
       // poo=GeometryEngine.project(7.680350, 36.852222, SpatialReference.create(4326));
       // poo= (Point) GeometryEngine.project(poo, SpatialReference.create(4326), mMapView.getSpatialReference());
       // Graphic graphic = new Graphic(poo, new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.DIAMOND));
       ///    mGraphicsLayer.addGraphic(graphic);
        dSpinner = (Spinner) findViewById(R.id.directionsSpinner);
        dSpinner.setEnabled(false);
        dSpinner.setBackgroundColor(Color.BLACK);
        t1=new TouchListener(MainActivity.this,mMapView,this);
       //clear= (Button) findViewById(R.id.imageButton2);
        //clear= (Button) findViewById(R.id.button3);
        //route= (Button) findViewById(R.id.button2);
        //b1= (Button) findViewById(R.id.button);
        //dSpinner.setAlpha(1);
        //System.out.println("color is"+dSpinner.getSolidColor());
        second s=new second(this);
        s.start();






/*        l1=new LocationListener() {


//               boolean locationChanged = false;
            //Point pp;
  //          Point search_layout=null;            // Zooms to the current when first GPS fix arrives.
//int count=1;
            @Override
            public void onLocationChanged(Location loc) {

                //count++;
                popToast("gps started "+lDisplayManager.isStarted(),true);

                 //   setContentView(R.layout.activity_gps_trying);
                //TextView t1 = (TextView) findViewById(R.id.textView4);
                //TextView t2 = (TextView) findViewById(R.id.textView5);
                //t1.setText("LONG = " + loc.getLongitude());
                //t2.setText("LAT = " + loc.getLatitude());
                //System.out.println("found 9");
                //count++;
                //if (count%5==0) {
                // if (locationChanged == false) {
                //     locationChanged = true;
                 //lDisplayManager.setShowLocation(true);
                //double locy = loc.getLatitude();
                //double locx = loc.getLongitude();
                //      TextView t1= (TextView) findViewById(R.id.textView4);
                //      TextView t2= (TextView) findViewById(R.id.textView5);
                //      t1.setText("Lat= "+locy);
                //      t2.setText("Lon= "+locy);
                ////System.out.println("position1o latitiude"+locy);
                ////System.out.println("position10 longtiude"+locx);
                ///System.out.println(mMapView.);
                //Double search_layout=locy;
                //locy=locx;
                //locx=search_layout;
                ////    Point wgspoint = new Point(locx, locy);
              //   Point pp = new Point(4102255.421241852, 857676.2648124732);
                //Point pp = (Point) GeometryEngine.project(p1, SpatialReference.create(3857), SpatialReference.create(4326));






                //X= 4102255.3348612054
                //Y= 857676.0949267921
                // System.out.println("our spital= "+mMapView.getSpatialReference().getID());
                //Point pp=mMapView.toMapPoint(p1);
                // if (mCallout == null) {
                //// if (mCallout == null) {
                ////     LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ////     mCallout = inflater.inflate(R.layout.callout, null);
                //// }
                //(ImageView)mCallout.findViewById(R.id.image);
                //      ((TextView) mCallout.findViewById(R.id.calloutText)).setText("here");
                ////   mMapView.getCallout().show(p1, mCallout);
                ////  mMapView.getCallout().setMaxWidth(700);

                //System.out.println("position x "+p1.getX());
                //System.out.println("position y "+ p1.getY());
                //Point mapPoint = (Point) GeometryEngine
                //        .project(p1,
                //                SpatialReference.create(32637),
                //                SpatialReference.create(4326));

                // System.out.println("reference is"+mm);
                //System.out.println("temp1o Y value"+mapPoint.getY());


                //pp=(Point) GeometryEngine
                //        .project(pp,
                //                SpatialReference.create(4326),
                //                SpatialReference.create(32637));

                //   pp.setX(pp.getX()-19.24);
                //   pp.setY(pp.getY()-10.53);
                //   Graphic graphic = new Graphic(pp, new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.DIAMOND));
                //   mGraphicsLayer.addGraphic(graphic);

                //System.out.println("PP value X= " + pp.getX());
                //System.out.println("PP value Y= " + pp.getY());
                // System.out.println("display X1=  " + mapPoint.getX());
                // System.out.println("display Y1=  " + mapPoint.getY());
                // System.out.println("display X2=  " + p2.getX());
                // System.out.println("display Y2=  " + p2.getY());

                //
                ///
                //
                //  mGraphicsLayer.addGraphic(graphic);
                //lDisplayManager.
                // Unit mapUnit = mMapView.getSpatialReference()
                //         .getUnit();
                // double zoomWidth = Unit.convertUnits(
                //        SEARCH_RADIUS,
                //        Unit.create(LinearUnit.Code.MILE_US),
                //        mapUnit);
                // Envelope zoomExtent = new Envelope(mapPoint,
                //         zoomWidth, zoomWidth);
                // mMapView.setExtent(zoomExtent);

                }



            @Override
            public void onProviderDisabled(String arg0) {
                System.out.println("found 4");
                popToast("GPS provider disabled", true);
            }

            @Override
            public void onProviderEnabled(String arg0) {
                popToast("GPS provider enabled", true);
                System.out.println("found 5");
            }

            @Override
            public void onStatusChanged(String arg0, int arg1,
                                        Bundle arg2) {
                System.out.println("found 6");
            }
        };












*/


        //Find the directions spinner



        // Retrieve the map and initial extent from XML layout

        //  AssetManager a1=getAssets();
        //a1.
        //  try {
        //      InputStream f1=a1.open("arc");
        //      //f1.
        //  } catch (IOException e) {
        //      e.printStackTrace();
        //  }

        // Set the tiled map service layer and add a graphics layer

        //SimpleLineSymbol polygonOutline = new SimpleLineSymbol(Color.RED, 2, SimpleLineSymbol.STYLE.SOLID);

// create the polygon symbol
// if an outline is not needed put "null" instead of "polygonOutline"
/*        SimpleFillSymbol fillSymbol = new SimpleFillSymbol(Color.GREEN, SimpleFillSymbol.STYLE.SOLID);
        fillSymbol.setOutline(polygonOutline);
        Polygon po=new Polygon();
        Point p1=new Point(4102363.9071817156,857772.619643);
        Point p2=new Point(4102416.5557467686,857799.2401230272);
        Point p3=new Point(4102436.1589281,857744.31787604);
        po.startPath(p1);
        po.lineTo(p2);
        po.lineTo(p3);
        Graphic ba_gr=new Graphic(po,fillSymbol);
        mGraphicsLayer.addGraphic(ba_gr);
*/
        //mMapView.getGrid().setType(Grid.GridType.MGRS);
        //mMapView.setExtent(mMapView.getMaxExtent());
        //mMapView.setResolution(mMapView.getMinScale());
        //System.out.println("grid is "+mMapView.getGrid().getType());
        //mMapView.setEsriLogoVisible(false);

        // Initialize the RouteTask and Locator with the local data
        //  initializeRoutingAndGeocoding();



        //System.out.println("found 1");





















        //List<RouteDirection> mDirections=t1.d1.mDirections;

        //System.out.println("the direction size is "+mDirections.size());

    }






    class DirectionsItemListener implements OnItemSelectedListener {

        private List<RouteDirection> mDirections;

        public DirectionsItemListener(List<RouteDirection> directions) {
            mDirections = directions;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            // We have to account for the added summary String
            if (mDirections != null && pos > 0 && pos <= mDirections.size())
                mMapView.setExtent(mDirections.get(pos - 1).getGeometry());

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
    ImageView ii1;


    public void showCallout(String text, Point location) {

        // If the callout has never been cruteated, inflate it
        if (mCallout == null) {
            LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mCallout = inflater.inflate(R.layout.callout, null);
        }
        System.out.println("search_layout==pp1 at callo");
        // Show the callout with the given text at the given location
        ((TextView) mCallout.findViewById(R.id.calloutText)).setText(text);
        ii1= (ImageView) mCallout.findViewById(R.id.imageView9);
        System.out.println("true true true "+text);
        if(text.compareTo("social scince office")==0){

            System.out.println("true tue true");
            ii1.setImageResource(R.drawable.social_science);
        ii1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ii1.setImageDrawable(null);
            }
        });
        }


        else if(text.compareTo("conference hall")==0) {

            System.out.println("true tue true");
          ii1.setImageResource(R.drawable.hall);}


        else if(text.compareTo("social library")==0) {

            System.out.println("true tue true");
            ii1.setImageResource(R.drawable.social_library);}


        else if(text.compareTo("green tower")==0) {

            System.out.println("true tue true");
            ii1.setImageResource(R.drawable.green_tower);}


        else {ii1.setImageDrawable(null);}
     //   else {
     //
     //       ii1.invalidate();
     //       ii1.destroyDrawingCache();
     //   }
        mMapView.getCallout().show(location, mCallout);
        mMapView.getCallout().setMaxWidth(700);

    }

    public void popToast(final String message, final boolean show) {
        // Simple helper method for showing toast on the main thread
        if (!show)
            return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    Point mLocationLayerPoint;
    String mLocationLayerPointString;
    AutoCompleteTextView mSearchEditText;
    AutoCompleteTextView autoComplete;
    ImageButton i1,i2,i3,i4;
    ToggleButton tog1,tog2,tog3,tog4;
    View searchRef,actionsetting,traveling_mod;
    String status="yse";
    //AutoCompleteTextView mSearchEditText = (AutoCompleteTextView) findViewById(R.id.autocomplete_country);
    public void for_clear(View view){
        System.out.println("tom off");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;
        searchRef = menu.findItem(R.id.action_search).getActionView();
        actionsetting=menu.findItem(R.id.action_clear).getActionView();
        traveling_mod=menu.findItem(R.id.action_settings).getActionView();
        tog1= (ToggleButton) traveling_mod.findViewById(R.id.toggleButton);
        tog2= (ToggleButton) traveling_mod.findViewById(R.id.toggleButton2);
        tog3= (ToggleButton) traveling_mod.findViewById(R.id.toggleButton4);
        tog4= (ToggleButton) traveling_mod.findViewById(R.id.toggleButton3);
        mSearchEditText = (AutoCompleteTextView)actionsetting.findViewById(R.id.autoComplete);

        //mSearchEditText = (EditText) searchRef.findViewById(R.id.searchText);


        tog1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tog1.isChecked()==false)
                    tog1.setBackgroundColor(Color.BLACK);
                    //tog1.setChecked(false);
                else {tog2.setChecked(false);
                tog2.setBackgroundColor(Color.BLACK);
          //      tog3.setChecked(false);
          //      tog3.setBackgroundColor(Color.BLACK);
                tog4.setChecked(false);
                tog4.setBackgroundColor(Color.BLACK);

                tog1.setBackgroundColor(Color.RED);
            }
           }
        });
        tog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tog2.isChecked()==false)
                    tog2.setBackgroundColor(Color.BLACK);

                else {
                    tog1.setChecked(false);
                    tog1.setBackgroundColor(Color.BLACK);
                    // tog3.setChecked(false);
                    // tog3.setBackgroundColor(Color.BLACK);
                    tog4.setChecked(false);
                    tog4.setBackgroundColor(Color.BLACK);
                    tog2.setBackgroundColor(Color.RED);
                }
            }
        });
        tog3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // tog1.setChecked(false);
                // tog1.setBackgroundColor(Color.BLACK);
                // tog2.setChecked(false);
                // tog2.setBackgroundColor(Color.BLACK);
                if(tog3.isChecked()==false)
                    tog3.setBackgroundColor(Color.BLACK);
               else {
                    tog4.setChecked(false);
                    tog4.setBackgroundColor(Color.BLACK);
                    tog3.setBackgroundColor(Color.RED);
                }
            }
        });

        tog4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tog4.isChecked()==false)
                    tog4.setBackgroundColor(Color.BLACK);


                else{
                    tog1.setChecked(false);
                    tog1.setBackgroundColor(Color.BLACK);
                    tog3.setChecked(false);
                    tog3.setBackgroundColor(Color.BLACK);
                    tog2.setChecked(false);
                    tog2.setBackgroundColor(Color.BLACK);
                    tog4.setBackgroundColor(Color.RED);
                }
            }
        });
        i1= (ImageButton)searchRef.findViewById(R.id.imageButton4);
        i2= (ImageButton)searchRef.findViewById(R.id.imageButton3);
        i3= (ImageButton)searchRef.findViewById(R.id.imageButton5);
        i4= (ImageButton)searchRef.findViewById(R.id.imageButton2);

        i1.setBackgroundColor(Color.BLUE);
        i1.setBackgroundColor(Color.BLUE);
        i1.setBackgroundColor(Color.WHITE);
        i1.setBackgroundColor(Color.BLUE);

        i4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mMapView.removeLayer(0);
               //robel gps1=new robel(MainActivity.this);
               // gps1.startActivity(new Intent(MainActivity.this,gps_trying.class));
                //Intent intint1=new Intent(MainActivity.this,gps_trying.class);
          //intint1.putExtra("name",gps1);
                if(mMapView.getLayer(0).isVisible()==true){
                    mMapView.getLayer(0).setVisible(false);
                mMapView.getLayer(1).setVisible(true);
                mMapView.setMapBackground(Color.WHITE, Color.BLACK, 0, 0);}
                else if(mMapView.getLayer(0).isVisible()==false){
                    mMapView.getLayer(0).setVisible(true);
                    mMapView.getLayer(1).setVisible(false);
                    mMapView.setMapBackground(Color.DKGRAY, Color.BLACK, 0, 0);}
            //    startActivity(intint1);
           // System.out.println("visibility1 is "+mapview.getVisibility());
             //   System.out.println("visibility1 is "+mMapView.getVisibility());
          /*      if(mapview.getVisibility()==View.INVISIBLE){
                    mMapView.setVisibility(View.INVISIBLE);
                    mapview.setVisibility(View.VISIBLE);

                }
                else if(mapview.getVisibility()==View.VISIBLE){
                    mapview.setVisibility(View.INVISIBLE);
                    mMapView.setVisibility(View.VISIBLE);

                }

             */   //mGraphicsLayer= (GraphicsLayer) mMapView.getLayer(1);
                //mMapView = (MapView) findViewById(R.id.map);
                //setContentView(R.layout.activity_gps_trying);


                //mapview.setVisibility(View.VISIBLE);
                //mMapView = (MapView) findViewById(R.id.map);

            }
        });
        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.route();
            }
        });
                i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps_location();
            }
        });
        i1.setClickable(true);
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dSpinner.setAdapter();
                sex=" ";
                mStops.clearFeatures();
                mGraphicsLayer.removeAll();
                mMapView.getCallout().hide();
                paths.clear();
                main_counter=1;
            }
        });

        System.out.println("m1.i1.i3 is not null");
        String[] colors = getResources().getStringArray(R.array.place_name);

        //{"conference_hall","Medical_class","Dirty_store","techno_gate","football_stadium","medical_lab",
        //"civil_lab","techno_space","biology_lab","health_registrar","whitehouse_dorm"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,colors);
        //autoComplete = (AutoCompleteTextView) searchRef.findViewById(R.id.autoComplete);
        mSearchEditText.setAdapter(adapter);
        mSearchEditText.setThreshold(1);

        mSearchEditText.setOnKeyListener(new View.OnKeyListener() {
            //@Override
           // public boolean onKey(View v, int keyCode, KeyEvent event) {
           //     return false;
           // }




            public void onSearchButtonClicked(View view) {
                near_bys.clear();
                System.out.println("printing from inside");
                // mStops.clearFeatures();
                // mGraphicsLayer.removeAll();
                // mMapView.getCallout().hide();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                String address = mSearchEditText.getText().toString();
                if (address.compareTo("dormitary") == 0) {
                    String[] colors = getResources().getStringArray(R.array.place_name);
                    for (int i = 0; i < colors.length; i++)
                        if (colors[i].contains("dormitary"))
                            executeLocatorTask(colors[i],50);
                } /*else if (address.compareTo("toilet") == 0) {
                    int amount = 0;
                    String[] colors = getResources().getStringArray(R.array.place_name);
                    for (int i = 0; i < colors.length; i++) {
                        if (colors[i].contains("toilet")) {
                            amount += 1;
                        }
                    }
                }*/

                    else if (address.compareTo("toilet") == 0) {
                        int amount=0;
                        String[] colors = getResources().getStringArray(R.array.place_name);
                        for(int i=0;i<colors.length;i++){
                            if(colors[i].contains("toilet")) {
                                amount+=1;
                            }}

                        if(lDisplayManager.getPoint()==null){
                        popToast("no gps or outside university",true);

                        for(int i=0;i<colors.length;i++){
                            if(colors[i].contains("toilet")) {
                                executeLocatorTask(colors[i],50);}}
                    }








                    else for(int i=0;i<colors.length;i++){
                        if(colors[i].contains("toilet")) {
                            System.out.println("the value is "+amount);
                                executeLocatorTask(colors[i],amount);}}}
                    //executeLocatorTask(colors[0],1);
                        //System.out.println("we have "+near_bys.size()+" in our loop");}




                else if (address.compareTo("gate") == 0) {
                    int amount=0;
                    String[] colors = getResources().getStringArray(R.array.place_name);
                    for(int i=0;i<colors.length;i++){
                        if(colors[i].contains("gate")) {
                            amount+=1;
                        }}

                    if(lDisplayManager.getPoint()==null){
                        popToast("no gps or outside university",true);

                        for(int i=0;i<colors.length;i++){
                            if(colors[i].contains("gate")) {
                                executeLocatorTask(colors[i],50);}}
                    }








                    else for(int i=0;i<colors.length;i++){
                        if(colors[i].contains("gate")) {
                            System.out.println("the value is "+amount);
                            executeLocatorTask(colors[i],amount);}}}










                   // int[] amount=mGraphicsLayer.getGraphicIDs();
                    //Graphic graphic=mGraphicsLayer.getGraphic(amount[0]);
                    //graphic.getGeometry()
                    //if(amount!=null){System.out.println("we have "+amount.length+" near bys");}
                            /*System.out.println("teamer executing distance"+Distance);
                            if(temp==0.0){
                                temp=Distance;
                            }
                            else if(Distance<temp){
                                temp=Distance;
                                temppo=temp_point1;
                              //  index=i;
                            }
                        }}
                mStops.clearFeatures();


                    Graphic graphic = new Graphic(temppo, new SimpleMarkerSymbol(Color.RED, 10, SimpleMarkerSymbol.STYLE.DIAMOND));
                    mGraphicsLayer.addGraphic(graphic);
                    StopGraphic stop = new StopGraphic(graphic);
                    mStops.addFeature(stop);
                    t1.route();
   //                 mStops.addFeature(temppo);
                }
                else*/
                    //executeLocatorTask(address);

                //}

                else{
                    executeLocatorTask(address,5);
                    System.out.println("reached fire");
                }

            }

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                System.out.println("inside");
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    System.out.println("clicking");
                    onSearchButtonClicked(mSearchEditText);
                    return true;
                }
                return false;
            }
        });
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        //System.out.println("printing "+id);
        if (id == R.id.action_search) {
            System.out.println("printing "+id);
          /*  i1= (ImageButton)findViewById(R.id.search_button);
            i1.setClickable(true);
            i1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("printout working ");
                }
            });*/

        }
        //else System.out.println("printing not "+id);
        return super.onOptionsItemSelected(item);
    }











Double Distance;
Point temp_point1,temp_point2;

    private void executeLocatorTask(String address,int which) {

        // Create Locator parameters from single line address string
        System.out.println("clicking " + address);
        LocatorFindParameters findParams = new LocatorFindParameters(address);
        System.out.println("clicking then"+findParams.getText());
        // Use the centre of the current map extent as the find location point
        findParams.setLocation(mMapView.getCenter(), mMapView.getSpatialReference());

        // Calculate distance for find operation
        Envelope mapExtent = new Envelope();
        mMapView.getExtent().queryEnvelope(mapExtent);
        if(mapExtent==null){
            System.out.println("clone is null");
        }
        else{
            System.out.println("clone is not null");
        }
        // assume map is in metres, other units wont work, double current envelope
        double distance = (mapExtent != null && mapExtent.getWidth() > 0) ? mapExtent.getWidth() * 2 : 10000;
        findParams.setDistance(distance);
        findParams.setMaxLocations(2);

        // Set address spatial reference to match map
        findParams.setOutSR(mMapView.getSpatialReference());

        // Execute async task to find the address
        LocatorAsyncTask l1=new LocatorAsyncTask(MainActivity.this,this);

        //System.out.println("relax near bys are "+MainActivity.this.near_bys.size()+"  numbers");
//if(which==1){
//    LocatorFindParameters findParams1 = new LocatorFindParameters("please");
//    l1.search(findParams1);
//}else{
        l1.search(findParams,which);//}
    ///    StopGraphic stop = new StopGraphic(l1.search(findParams));
    ///    stop.setName(address);
    ///    mStops.addFeature(stop);
      //  mStops
        //mLocationLayerPointString = address;

       /* AsyncTask a1=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }
        };*/

    }
    private static boolean copyAssetFolder(AssetManager assetManager,
                                           String fromAssetPath, String toPath) {
        try {
            String[] files = assetManager.list(fromAssetPath);
            new File(toPath).mkdirs();
            boolean res = true;
            for (String file : files)
                if ((file.contains(".")||(file.contains("jimma_ND")))&&(file.compareTo("univ.tn")!=0)){
                    res &= copyAsset(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
                    System.out.println("robel the file path is "+file.toString());}
                else
                    res &= copyAssetFolder(assetManager,
                            fromAssetPath + "/" + file,
                            toPath + "/" + file);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean copyAsset(AssetManager assetManager,
                                     String fromAssetPath, String toPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    private void viewhelp(){
        final AlertDialog.Builder alertadd = new AlertDialog.Builder(
                MainActivity.this);
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        final View view = factory.inflate(R.layout.viewhelp, null);
        alertadd.setTitle("JU Navigator help");
        //alertadd.setMessage("");
        alertadd.setView(view);
        alertadd.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });
        alertadd.show();
    }

}
































