public class Item {


    private final String id;
    private final String name;
    private final String category;

    private long quantity;

    public Item(String id, String name, String category, long quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() { // to easily print to console
        return id + " | " + name + " | " + category + " | " + quantity + " nos";
    }

}
