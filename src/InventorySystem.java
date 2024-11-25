import java.util.*;

class ItemComparator implements Comparator<Item> {

    @Override
    public int compare(Item i1, Item i2) {
        return Long.compare(i2.getQuantity(), i1.getQuantity());
    }
}



public class InventorySystem {

    private final long alertThreshold;

    Map<String, Item> itemMap = new HashMap<>();// Will be used for references and to check if element exists
    SortedSet<Item> topKList = new TreeSet<>(new ItemComparator()); // for top k ordered list by quantity
    Map<String, SortedSet<Item>> categoryList = new HashMap<>(); // holds ordered items by quantity in each category

    public InventorySystem(long alertThreshold){
        this.alertThreshold = alertThreshold;
    }

    private void quantityCheck(String id){
        if (itemMap.containsKey(id) && itemMap.get(id).getQuantity() < alertThreshold){
            System.out.println("Warning!!\n" + itemMap.get(id) + "\nStock is below threshold!");
        }
    }

    public void insertItem(String id, String name, String category, long quantity){ // O(log n)

        if (itemMap.containsKey(id)){
            System.out.println("Invalid item ID !! ID already exists");
            return;
        }

        Item i = new Item(id, name, category, quantity);

        itemMap.put(id, i); // O(1)

        topKList.add(i); // O(log n)

        if (categoryList.containsKey(category)){ // O(1) * O(log n)

            categoryList.get(category).add(i);

        }else{

            categoryList.put(category, new TreeSet<Item>(new ItemComparator()));
            categoryList.get(category).add(i);

        }
        quantityCheck(id);
    }

    /*
    * This function deletes items from the itemMap, which is a O(1) operation but does not delete from the other
    * data structures. Instead, the item is flagged as deleted. So during printing or other processing this flag will be
    * detected, and it will be removed.
    * */
    public void deleteItem(String id){ //O(1)

        if (itemMap.containsKey(id)){
            Item i = itemMap.get(id);
            i.delete();
            itemMap.remove(id);
        }else{
            System.out.println("Item does not exist!!");
        }
    }

    public void updateItem(String id, long quantity){ //O(1)
        if (itemMap.containsKey(id)){
            itemMap.get(id).setQuantity(quantity);
            quantityCheck(id);
        }else{
            System.out.println("Item does not exist!!");
        }
    }

    public void search(String id){ // O(1)
        if (itemMap.containsKey(id)){
            System.out.println(itemMap.get(id));
            quantityCheck(id);
        }else{
            System.out.println("Item does not exist!!");
        }
    }




}
