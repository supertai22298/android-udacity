package com.example.familyapp.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;

public class Direction {
    @SerializedName("routes")
    private ArrayList<Route> routes;
    @SerializedName("status")
    private String status;

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Route {
        @SerializedName("overview_polyline")
        private OverviewPolyLine overviewPolyLine;
        @SerializedName("legs")
        private ArrayList<Legs> legs;

        public OverviewPolyLine getOverviewPolyLine() {
            return overviewPolyLine;
        }

        public void setOverviewPolyLine(OverviewPolyLine overviewPolyLine) {
            this.overviewPolyLine = overviewPolyLine;
        }

        public ArrayList<Legs> getLegs() {
            return legs;
        }

        public void setLegs(ArrayList<Legs> legs) {
            this.legs = legs;
        }
    }

    public static class Legs {
        @SerializedName("distance")
        private Value distance;
        @SerializedName("duration")
        private Value duration;
        @SerializedName("steps")
        private ArrayList<Steps> steps;

        public Value getDistance() {
            return distance;
        }

        public void setDistance(Value distance) {
            this.distance = distance;
        }

        public Value getDuration() {
            return duration;
        }

        public void setDuration(Value duration) {
            this.duration = duration;
        }

        public ArrayList<Steps> getSteps() {
            return steps;
        }

        public void setSteps(ArrayList<Steps> steps) {
            this.steps = steps;
        }

        public ArrayList<LatLng> getDirectionSteps() {
            ArrayList<LatLng> routeList = new ArrayList<>();
            Direction.Location location;
            String polyline;
            for (int i = 0; i < steps.size(); i ++){
                location = steps.get(i).getStartLocation();
                routeList.add(new LatLng(location.getLat(), location.getLng()));
                polyline = steps.get(i).getPolyline().getPoints();
                routeList.addAll(PolyUtil.decode(polyline));
                location = steps.get(i).getEndLocation();
                routeList.add(new LatLng(location.getLat(), location.getLng()));
            }
            return routeList;
        }
    }

    public static class Steps {
        @SerializedName("start_location")
        private Location startLocation;
        @SerializedName("end_location")
        private Location endLocation;
        @SerializedName("polyline")
        private OverviewPolyLine polyline;

        public Location getStartLocation() {
            return startLocation;
        }

        public void setStartLocation(Location startLocation) {
            this.startLocation = startLocation;
        }

        public Location getEndLocation() {
            return endLocation;
        }

        public void setEndLocation(Location endLocation) {
            this.endLocation = endLocation;
        }

        public OverviewPolyLine getPolyline() {
            return polyline;
        }

        public void setPolyline(OverviewPolyLine polyline) {
            this.polyline = polyline;
        }
    }

    public static class OverviewPolyLine {
        @SerializedName("points")
        private String points;

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }
    }

    public static class Value {
        @SerializedName("text")
        private String text;
        @SerializedName("value")
        private String value;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class Location {
        @SerializedName("lat")
        private Double lat;
        @SerializedName("lng")
        private Double lng;

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }
    }
}

