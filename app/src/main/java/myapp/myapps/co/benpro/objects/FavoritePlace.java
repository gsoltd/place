package myapp.myapps.co.benpro.objects;

/**
 * Created by Arixa on 15/02/2016.
 */
public class FavoritePlace {

    private int id;
    private String name;
    private String address;
    private double distance;
    private String image;

    public FavoritePlace(){}

    public FavoritePlace(int id,String name,String address,double distance,String image){
        this.name = name;
        this.address = address;
        this.distance =distance;
        this.image = image;
    }

    public FavoritePlace(int id,String name,String address,double distance){
        this.name = name;
        this.address = address;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Place " + name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
