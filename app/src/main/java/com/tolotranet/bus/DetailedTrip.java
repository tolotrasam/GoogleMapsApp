package com.tolotranet.bus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;


import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.model.Direction;
import com.tolotranet.bus.model.Bus;
import com.tolotranet.bus.model.Bus_Stop;

import static com.tolotranet.bus.MapsActivity.getActiveBusStop;

public class DetailedTrip extends AppCompatActivity {

    private ArrayList<Bus> AllBusList;
    private String Bus_Stop_ID;
    private Bus_Stop bus_stop;
    private TextView distance;
    private TextView name;
    private TextView time;
    private String closest_time;
    private String closest_distance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_trip);

        Bus_Stop_ID = getIntent().getStringExtra("ID");

        distance = (TextView) findViewById(R.id.distance);
        name = (TextView) findViewById(R.id.name);
        time = (TextView) findViewById(R.id.time);
        bus_stop = getActiveBusStop();
        Bus_Stop_ID = getActiveBusStop().getId();
//        distance

        getAllBusWithPathHistory(Bus_Stop_ID);

        Log.d("hello bus stop", getActiveBusStop().getName());
        Log.d("hello bus stop id", getActiveBusStop().getId());
    }


    public static double getStraightDistanceBetweenTwoPoints(LatLng point2, LatLng point1) {

        final int R = 6371; // Radius of the earth

        double lat1 = point1.latitude;
        double lat2 = point2.latitude;
        double lon1 = point1.longitude;
        double lon2 = point2.longitude;
        double el1 = 0;
        double el2 = 0;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    private void getAllBusWithPathHistory(final String id) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Buses");
        //just do it once
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("Count ", "" + dataSnapshot.getChildrenCount());

                if (dataSnapshot.getChildrenCount() != 0) {

                    AllBusList = new ArrayList<Bus>();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Bus bus = new Bus();
                        bus.setId(postSnapshot.getKey());
                        bus.setPath(postSnapshot.child("path").getValue(String.class));

                        ArrayList<LatLng> coordinate = new ArrayList<LatLng>();
                        DataSnapshot path_histories = postSnapshot.child("path_history");

                        for (DataSnapshot point_history : path_histories.getChildren()) {
                            if (point_history.exists()) {

                                LatLng pointLatLong = new LatLng(
                                        point_history.child("lat").getValue(Double.class),
                                        point_history.child("lon").getValue(Double.class));
                                coordinate.add(pointLatLong);
                            }
                        }
                        bus.setPathHistory(coordinate);

                        AllBusList.add(bus);
                        Log.e("Get Bus Data", " " + bus.getId());
                    }
                } else {
                    Log.d("hello", "no bus found");
                }
                twoFilters(id, AllBusList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("hello", "The read failed: " + databaseError.getCode());
            }
        });


    }

    private void twoFilters(String id, List<Bus> allBusList) {

        //1) filter in if the bus has the bustop in his path
        ArrayList<Bus> BusPassingThroughBusStop = new ArrayList<>();
        for (Bus bus : allBusList) {

            String[] path_Array = bus.getPath().split(",");
            if (Arrays.asList(path_Array).contains(id)) {
                BusPassingThroughBusStop.add(bus);
            }
        }

        Log.d("hello ", " BusPassingThroughBusStop" + BusPassingThroughBusStop.size());

        ArrayList<Bus> BusPassingThroughBusStopAndHasNotYetLeft = new ArrayList<>();
        //2) filter in if the path_history has not yet the position of the bus stop +- margin

        for (Bus bus : BusPassingThroughBusStop) {

            Boolean hasPassedThroughBusStop = false;
            for (LatLng point : bus.getPathHistory()) {

                double distance = getStraightDistanceBetweenTwoPoints(point, bus_stop.getLocation());
                if (distance == 0 || distance < 50) {
                    hasPassedThroughBusStop = true;

                    break;
                }
            }

            if (!hasPassedThroughBusStop) {
                BusPassingThroughBusStopAndHasNotYetLeft.add(bus);
            }
        }


        Log.d("hello ", " BusPassingThroughBusStopAndHasNotYetLeft" + BusPassingThroughBusStopAndHasNotYetLeft.size());
        for (Bus bus : BusPassingThroughBusStopAndHasNotYetLeft) {
            double distance_From_user = 0;
            getDistanceBetweenTwoPoint(bus_stop.getLocation(), bus.getPosition(), null);
        }

    }


    private void getDistanceBetweenTwoPoint(LatLng Origin, LatLng destination, List<LatLng> waypoints) {

        Log.d("hello orgin", String.valueOf(Origin));
        Log.d("hello destination", String.valueOf(destination));

        GoogleDirection.withServerKey("AIzaSyCn8t4ByKTmT-Um5dI_wwqBgp1KQS1bePg")
                .from(Origin)
                .to(destination)
//                .waypoints(waypoints)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            // Do something
                            Log.d("hello rawbody", rawBody);
                            parseDirectionResponse(rawBody);

                        } else {
                            // Do something
                            Log.e("hello rawbody", rawBody);
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                    }
                });

    }

    private void parseDirectionResponse(String result) {
        JSONObject json = null;
        try {
            json = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray routeArray = null;
        try {
            routeArray = json.getJSONArray("routes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject routes = null;
        try {
            routes = routeArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray newTempARr = null;
        try {
            newTempARr = routes.getJSONArray("legs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject newDisTimeOb = null;
        try {
            newDisTimeOb = newTempARr.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject distOb = null;
        try {
            distOb = newDisTimeOb.getJSONObject("distance");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject timeOb = null;
        try {
            timeOb = newDisTimeOb.getJSONObject("duration");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            closest_distance = distOb.getString("text");
            Log.i("Distance :", distOb.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            closest_time = timeOb.getString("text");

            Log.i("Time :", timeOb.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        time.append("Time: "+closest_time+ " Distance "+closest_distance);

    }

}

