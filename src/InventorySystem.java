import java.util.*;

class ItemComparator implements Comparator<Item> {

    @Override
    public int compare(Item i1, Item i2) {
        return Long.compare(i2.getQuantity(), i1.getQuantity());
    }
}



public class InventorySystem {

    private long alertThreshold;

    Map<String, Item> itemMap = new HashMap<>();// Will be used for references and to check if element exists
    SortedSet<Item> topKList = new TreeSet<>(new ItemComparator()); // for top k ordered list by quantity
    Map<String, SortedSet<Item>> categoryList = new HashMap<>(); // holds ordered items by quantity in each category


    public InventorySystem(){
        alertThreshold = 0;
    }

    public void setAlertThreshold(long thresh){
        alertThreshold = thresh;
    }
    public long getAlertThreshold(){
        return alertThreshold;
    }

    private void quantityCheck(String id){ // O(1)
        if (itemMap.containsKey(id) && itemMap.get(id).getQuantity() < alertThreshold){
            System.out.println("Warning!!\n" + itemMap.get(id) + "\nStock is below threshold!");
        }
    }

    public static InventorySystem merge(InventorySystem inv1, InventorySystem inv2){ // O((n+m)*(log(n+m)))

        // let size of inv1.itemMap = n, inv2.itemMap = m

        InventorySystem newInv = new InventorySystem();

        Map<String, Item> newList = new HashMap<>(inv1.itemMap); // O(n)

        inv2.itemMap.forEach((k, v) -> { // O(m)
            if (newList.containsKey(k)){

                if (newList.get(k).getQuantity() < v.getQuantity()){
                    newList.put(k, v); // replace with inv2 entry as it has higher quantity
                }

            }else{
                newList.put(k, v);
            }
        });

        newList.forEach((k, v) -> { // O((n+m)*(log(n+m)))
            newInv.insertItem(k, v.getName(), v.getCategory(), v.getQuantity()); // O(log (n+m))
        });

        return newInv;
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

    public void deleteItem(String id){ // O(n)

        if (itemMap.containsKey(id)){

            // remove from itemMap
            Item i = itemMap.get(id);
            itemMap.remove(id);


            // remove from category Order
            SortedSet<Item> catOrder = categoryList.get(i.getCategory());

            Iterator<Item> it = catOrder.iterator();

            while (it.hasNext()) { // O(n)
                Item elem = it.next();
                if (elem.getId().equals(i.getId())) {
                    it.remove(); // O(n) for the removal worst case (items are not sorted by id so binary search won't work) (only happens once)
                    break;
                }
            }


            // remove from topKList
            it = topKList.iterator();

            while (it.hasNext()) { // O(n)
                Item elem = it.next();
                if (elem.getId().equals(i.getId())) {
                    it.remove(); // O(n) for the removal worst case (items are not sorted by id so binary search won't work) (only happens once)
                    break;
                }
            }

        }else{
            System.out.println("Item does not exist!!");
        }
    }

    public void updateItem(String id, long quantity){ //O(1)

        if (quantity < 0) {
            System.out.println("Invalid Quantity!!!!");
            return;
        }

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

    public void getKItems(long k){ // O(k)

        int c = 0;
        for (Item i: topKList){
            ++c;
            System.out.println(i);
            if (c == k){
                break;
            }
        }
    }

    public void getCategoryItems(String category){ // O(m)
        if (categoryList.containsKey(category)){

            for (Item i: categoryList.get(category)){
                System.out.println(i);
            }

        }else{
            System.out.println("Category does not exist!!");
        }
    }


}
