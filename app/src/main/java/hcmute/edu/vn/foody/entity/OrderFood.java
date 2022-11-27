package hcmute.edu.vn.foody.entity;

public class OrderFood {
    private int id;
    private Food food;
    private int quantity;
    private int price_total;
    // 0: cart; 1: ongoing; 2:successful; 3: draft
    private int status;
    private Account account;

    public OrderFood(int id, Food food, int quantity, int price_total, int status, Account account) {
        this.id = id;
        this.food = food;
        this.quantity = quantity;
        this.price_total = price_total;
        this.status = status;
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice_total() {
        return price_total;
    }

    public void setPrice_total(int price_total) {
        this.price_total = price_total;
    }

}
