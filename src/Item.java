public class Item {


    private final String id;
    private final String name;
    private final String category;

    private long quantity;

    private boolean deleted = false; // to mark to delete

    public Item(String id, String name, String category, long quantity) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
    }

    public void delete(){
        deleted = true;
    }
    public boolean isDeleted(){
        return deleted;
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
        return id + "-" + name + " " + quantity + "nos";
    }

}
