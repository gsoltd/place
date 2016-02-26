package myapp.myapps.co.benpro.objects;


public class SearchObject {

    private double lat;
    private double lon;
    private String name;
    private String address;
    private String icon;
    private double distance;
    private String photo;


    public SearchObject(double lat, double lon, String name, String address,String icon,double distance,String photo){
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.address = address;
        this.icon = icon;
        this.distance = distance;
        this.photo = photo;
    }

    public String toString(){
        return name + "(" + address + ")";
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getIcon() {
        return icon;
    }

    public double getDistance() {
        return distance;
    }

    public String getPhoto() {
        return photo;
    }
}

