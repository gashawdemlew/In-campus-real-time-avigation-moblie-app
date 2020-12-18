package com.example.JU_NAVIGATOR.JU_NAVIGATOR;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.geocode.Locator;
import com.esri.core.tasks.geocode.LocatorFindParameters;
import com.esri.core.tasks.geocode.LocatorGeocodeResult;
import com.esri.core.tasks.na.StopGraphic;

import java.util.List;

/**
 * Created by asanti on 4/17/2015.
 */
class LocatorAsyncTask extends AsyncTask<LocatorFindParameters, Void, List<LocatorGeocodeResult>> {

    private Exception mException;
    MainActivity m1;
    Context c1;
    Graphic return_result;
    Graphic resultLocGraphic;
    int amount;
    public LocatorAsyncTask(Context c,MainActivity m){
        m1=m;
        c1=c;
    }

    public void search(LocatorFindParameters address,int am){
        amount=am;
        execute(address);
        //if(resultLocGraphic==null)
        //    System.out.println("nothing returned");
        //return resultLocGraphic;

    }
    public void near_by(){
        //int[] amount=m1.mGraphicsLayer.getGraphicIDs();
        //System.out.println("why there are "+m1.near_bys.size()+" near bys");
execute();
    }


    @Override
    protected List<LocatorGeocodeResult> doInBackground(LocatorFindParameters... params) {
        System.out.println("cloneingg");
        mException = null;
        List<LocatorGeocodeResult> results = null;
        System.out.println("it is null by ");
        Locator locator;
        locator= Locator.createLocalLocator(Environment.getExternalStorageDirectory().getPath()+"/Arcgis/Geocoding/JimmaLocator.loc");
        if(locator==null){
            System.out.println("it is null");
        }
        else {
            System.out.println("it is null not "+locator.getUrl());
        }
        try {
            System.out.println("excepi"+locator.getInfo().getServiceDescription());
if(results.get(0).getAddress().compareTo("please")==0){
    List<LocatorGeocodeResult> geocodeResult=null;
    return geocodeResult;
}



        } catch (Exception e) {
            mException = e;
            System.out.println("excepi");
        }
        try {
            results = locator.find(params[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }


    protected void onPostExecute(List<LocatorGeocodeResult> result) {

        System.out.println("ere please");
        if (mException != null) {
            System.out.println("abeba error "+mException.getMessage());
            Log.w("PlaceSearch", "LocatorSyncTask failed with:");
            mException.printStackTrace();
           // Toast.makeText(c1, getString(R.string.addressSearchFailed), Toast.LENGTH_LONG).show();
            /// return;
        }

        if(result==null){
            System.out.println("there are "+m1.near_bys.size()+"  nearbys");
        }
        else if (result.size() == 0) {

            Toast.makeText(c1, "place not found", Toast.LENGTH_LONG).show();
        } else {
            System.out.println("abeba error in third");
            // Use first result in the list
            LocatorGeocodeResult geocodeResult = result.get(0);

            // get return geometry from geocode result

            Point resultPoint = geocodeResult.getLocation();
            //m1.temp_point1=resultPoint;
            //System.out.println("while executing result is tempting");
            //System.out.println("while executing result" + resultPoint.getX());
            //System.out.println("while executing result"+resultPoint.getY());
            //System.out.println("while executing gps" + m1.lDisplayManager.getPoint().getX());
            //System.out.println("while executing gps"+m1.lDisplayManager.getPoint().getY());
/*Point pp= (Point) GeometryEngine.project(
        m1.lDisplayManager.getPoint(),
        SpatialReference.create(4326),
        m1.mMapView.getSpatialReference());
            m1.Distance= GeometryEngine.distance(
                        pp,
            resultPoint,m1.mMapView.getSpatialReference());
  */
            //System.out.println("executing value is "+m1.Distance);
            //  return resultPoint;
            // create marker symbol to represent location
            //System.out.println("gggeeyy X="+resultPoint.getX());
            //System.out.println("gggeeyy Y="+resultPoint.getY());
            //NAFeatures na;

            SimpleMarkerSymbol resultSymbol = new SimpleMarkerSymbol(Color.BLUE, 20, SimpleMarkerSymbol.STYLE.CROSS);
            // create graphic object for resulting location
            resultLocGraphic = new Graphic(resultPoint, resultSymbol);
            m1.near_bys.add(resultPoint);
            System.out.println("we have "+m1.near_bys.size()+"  in our locator class");




            if((m1.near_bys.size()==amount)&&(amount!=0)){
                System.out.println("we have fullfile our locator");
                //Double distance=
                int index=0;
                        Point temp1= m1.near_bys.get(0);//(Point) GeometryEngine.project(m1.near_bys.get(0),m1.mMapView.getSpatialReference(),SpatialReference.create(4326));
                Double distance=GeometryEngine.distance(m1.lDisplayManager.getPoint(),temp1,SpatialReference.create(4326));
                Point temp = m1.near_bys.get(0);
                SpatialReference sp=m1.mMapView.getSpatialReference();
                        for(int i=1;i<m1.near_bys.size();i++){
                            temp= m1.near_bys.get(i);//(Point) GeometryEngine.project(m1.near_bys.get(i),m1.mMapView.getSpatialReference(),SpatialReference.create(4326));
                            Double real_distance=GeometryEngine.distance(m1.lDisplayManager.getPoint(),temp,SpatialReference.create(4326));
                            if(real_distance<distance){
                                System.out.println("switching index "+index+" value is "+i);
                                index=i;
                                distance=real_distance;}
                            System.out.println("asanti distance is "+real_distance);


                        }
                Graphic graphic = new Graphic(m1.near_bys.get(index), new SimpleMarkerSymbol(Color.RED, 30, SimpleMarkerSymbol.STYLE.CIRCLE));
                   m1.mGraphicsLayer.addGraphic(graphic);









            }
            // add graphic to location layer
            m1.mGraphicsLayer.addGraphic(resultLocGraphic);
            //return_result =resultLocGraphic;
            // create text symbol for return address   849335.1833042512
            String address = geocodeResult.getAddress();
            TextSymbol resultAddress = new TextSymbol(25, address, Color.GREEN);
            // create offset for text
            resultAddress.setOffsetX(-4 * address.length());
            resultAddress.setOffsetY(10);
            // create a graphic object for address text
            Graphic resultText = new Graphic(resultPoint, resultAddress);
            // add address text graphic to location graphics layer
            m1.mGraphicsLayer.addGraphic(resultText);

            m1.mLocationLayerPoint = resultPoint;

            // Zoom map to geocode result location
            m1.mMapView.zoomToResolution(geocodeResult.getLocation(), 2);
            System.out.println("chapi "+resultLocGraphic.getGeometry().getType().name());
            StopGraphic stop = new StopGraphic(resultLocGraphic);

            stop.setName(address);
            m1.mStops.addFeature(stop);
            //System.out.println("the size of feature is "+m1.mStops.getFeatures().size());
            //for()
            //m1.mStops.addFeature(stop);
        System.out.println("address is "+address);
        }
    }


}