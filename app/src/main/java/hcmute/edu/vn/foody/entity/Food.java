package hcmute.edu.vn.foody.entity;

public class Food {
    private int id;
    private Restaurant restaurant;
    private String name;
    private int price;
    private int image;

    public Food() {
    }

    public Food(int id, Restaurant restaurant, String name, int price, int image) {
        this.id = id;
        this.restaurant = restaurant;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
