import java.util.*;

public class Main {

    private static Random rng = new Random();
    private static Set<String> createdIds = new HashSet<>();
    private final static String[] categories = {
            "electronics",
            "furniture",
            "groceries",
            "home appliances",
            "Clothing",
            "Cleaning",
            "Toys",
            "Utensils",
            "Jewelery"
    };

    private static String generateRandomString(){
        StringBuilder st = new StringBuilder();

        for (int i = 0; i < 10; i++){
            st.append((char)rng.nextInt(65,90));
        }

        return st.toString();
    }

    private static String generateRandomId(){

        String st;

        while (true){
            st = generateRandomString();
            if (!createdIds.contains(st)){
                createdIds.add(st);
                break;
            }
        }
        return st;
    }

    private static ArrayList<Item> generateItemList(long n, long thresh){

        ArrayList<Item> newList = new ArrayList<Item>();
        String id;

        for (long i = 0; i < n; i++){

            id = generateRandomId();

            newList.add(new Item(id, "Item " + (char)rng.nextInt(65,90),
                    categories[rng.nextInt(categories.length)], rng.nextLong(thresh - thresh / 10, thresh * 10)));
        }

        return newList;
    }
    private static ArrayList<String> generateIdList(ArrayList<Item> itemList, long n) {
        ArrayList<String> newList = new ArrayList<>();

        while (n > 0){ // possibility for duplicates to be added exists
            newList.add(itemList.get(rng.nextInt(0, itemList.size())).getId()); // randomly select item id
            --n;
        }
        return newList;
    }
    private static ArrayList<Item> generateUpdateList(ArrayList<Item> itemList, long n){
        ArrayList<Item> newList = new ArrayList<>();
        Item it;
        while (n > 0){ // possibility for duplicates to be added exists
            it = itemList.get(rng.nextInt(0, itemList.size()));

            newList.add(
                    new Item(it.getId(), it.getName(), it.getCategory(), it.getQuantity() +
                            (rng.nextInt(-1,2))*(it.getQuantity()/2))
            );
            --n;
        }

        return newList;
    }


    public static void main(String[] args) {

        Scanner inp = new Scanner(System.in);

        // test code
        InventorySystem inv1 = new InventorySystem();
        InventorySystem inv2 = new InventorySystem();

        long n, m, common, threshold, startTime, endTime, k;
        long deleteCount1, deleteCount2, updateCount1, updateCount2;
        long searchCount1, searchCount2;
        String selectedCat;


        System.out.println("Please enter the following parameters for the automatic test to start");

        System.out.print("Enter the number of elements to be added in inventory 1: ");
        n = Long.parseLong(inp.nextLine());

        System.out.print("Enter the number of elements to be added in inventory 2: ");
        m = Long.parseLong(inp.nextLine());

        System.out.print("Enter the number of common elements to be added additionally between the two: ");
        common = Long.parseLong(inp.nextLine());

        System.out.print("Enter the alert threshold for the inventory systems: ");
        threshold = Long.parseLong(inp.nextLine());

        System.out.print("Enter the value of k for listing inventory items: ");
        k= Long.parseLong(inp.nextLine());

        System.out.print("Enter the number of items to be deleted later from inventory 1:");
        deleteCount1= Long.parseLong(inp.nextLine());

        System.out.print("Enter the number of items to be deleted later from inventory 2:");
        deleteCount2= Long.parseLong(inp.nextLine());

        System.out.print("Enter the number of items to be updated later from inventory 1:");
        updateCount1= Long.parseLong(inp.nextLine());

        System.out.print("Enter the number of items to be updated later from inventory 2:");
        updateCount2= Long.parseLong(inp.nextLine());

        System.out.print("Enter the number of items to be searched later from inventory 1:");
        searchCount1= Long.parseLong(inp.nextLine());

        System.out.print("Enter the number of items to be searched later from inventory 2:");
        searchCount2= Long.parseLong(inp.nextLine());

        System.out.println("\nAvailable categories are:");
        for (String s: categories){
            System.out.println(s);
        }

        System.out.print("Enter the desired category to view all inventory items: ");
        selectedCat = inp.nextLine();

        System.out.println("Setting up parameters....");

        inv1.setAlertThreshold(threshold);
        inv2.setAlertThreshold(threshold);

        // generate items randomly according to given count
        ArrayList<Item> itemList1 = generateItemList(n, threshold);
        ArrayList<Item> itemList2 = generateItemList(m, threshold);
        ArrayList<Item> commonList = generateItemList(common, threshold);

        // generate delete operation lists
        ArrayList<String> deleteList1 = generateIdList(itemList1, deleteCount1);
        ArrayList<String> deleteList2 = generateIdList(itemList2, deleteCount2);

        // generate update operation lists
        ArrayList<Item> updateList1 = generateUpdateList(itemList1, updateCount1);
        ArrayList<Item> updateList2 = generateUpdateList(itemList2, updateCount2);

        // generate search operation lists
        ArrayList<String> searchList1 = generateIdList(itemList1, searchCount1);
        ArrayList<String> searchList2 = generateIdList(itemList2, searchCount2);

        // final test result lists

        ArrayList<String> testResults = new ArrayList<>(); // holds output values of test to be shown at end

        /*
        // uncomment to print lists
        itemList1.forEach(k -> System.out.println(k));
        itemList2.forEach(k -> System.out.println(k));
        commonList.forEach(k -> System.out.println(k));
         */


        System.out.println("Starting tests");

        // performing operations on inventory 1

        // insertion
        System.out.println("Insertion test");
        startTime = System.nanoTime();
        itemList1.forEach( it -> {
            inv1.insertItem(it.getId(), it.getName(), it.getCategory(), it.getQuantity());
        });
        commonList.forEach( it -> {
            inv1.insertItem(it.getId(), it.getName(), it.getCategory(), it.getQuantity());
        });

        endTime = System.nanoTime();

        testResults.add(String.format("Insertion time for %s + %s elements into inventory 1: %s Milliseconds", n, common, (double)(endTime - startTime)/1000000));
        testResults.add(String.format("Number of items in inventory 1: %s\n", inv1.itemMap.size()));


        // deletion
        System.out.println("Deletion test");
        startTime = System.nanoTime();
        deleteList1.forEach(s -> {
            inv1.deleteItem(s);
        });

        endTime = System.nanoTime();
        testResults.add(String.format("deletion time for %s elements in inventory 1: %s Milliseconds", deleteCount1, (double)(endTime - startTime)/1000000));
        testResults.add(String.format("Number of items in inventory 1: %s\n", inv1.itemMap.size()));

        // add elements back to perform other operations (also showcases duplicate checking)
        System.out.println("Insertion test after deletion");
        startTime = System.nanoTime();
        itemList1.forEach( it -> {
            inv1.insertItem(it.getId(), it.getName(), it.getCategory(), it.getQuantity());
        });
        commonList.forEach( it -> {
            inv1.insertItem(it.getId(), it.getName(), it.getCategory(), it.getQuantity());
        });
        endTime = System.nanoTime();

        testResults.add(String.format("Insertion time after deletion for %s + %s elements into inventory 1: %s Milliseconds", n, common, (double)(endTime - startTime)/1000000));
        testResults.add(String.format("Number of items in inventory 1 after inserting again: %s\n", inv1.itemMap.size()));


        // updation
        System.out.println("Updation test");
        startTime = System.nanoTime();
        updateList1.forEach( it -> {
            inv1.updateItem(it.getId(), it.getQuantity());
        });
        endTime = System.nanoTime();
        testResults.add(String.format("Updation time for %s elements in inventory 1: %s Milliseconds\n", updateCount1, (double)(endTime - startTime)/1000000));

        // searching
        System.out.println("Searching test");
        startTime = System.nanoTime();
        searchList1.forEach( s -> {
            inv1.search(s);
        });
        endTime = System.nanoTime();
        testResults.add(String.format("Search time for %s elements in inventory 1: %s Milliseconds\n", searchCount1, (double)(endTime - startTime)/1000000));

        // get all category elements
        System.out.println("Category test");
        startTime = System.nanoTime();
        inv1.getCategoryItems(selectedCat);
        endTime = System.nanoTime();
        testResults.add(String.format("Category view time for %s in inventory 1: %s Milliseconds\n", selectedCat, (double)(endTime - startTime)/1000000));


        // get k items from all categories
        System.out.println("K items test");
        startTime = System.nanoTime();
        inv1.getKItems(k);
        endTime = System.nanoTime();
        testResults.add(String.format("K items view time for %s elements in inventory 1: %s Milliseconds\n", k, (double)(endTime - startTime)/1000000));


        // performing operations on inventory 2

        // insertion
        System.out.println("Insertion test");
        startTime = System.nanoTime();
        itemList2.forEach( it -> {
            inv2.insertItem(it.getId(), it.getName(), it.getCategory(), it.getQuantity());
        });
        commonList.forEach( it -> {
            inv2.insertItem(it.getId(), it.getName(), it.getCategory(), it.getQuantity());
        });

        endTime = System.nanoTime();

        testResults.add(String.format("Insertion time for %s + %s elements into inventory 2: %s Milliseconds", n, common, (double)(endTime - startTime)/1000000));
        testResults.add(String.format("Number of items in inventory 2: %s\n", inv2.itemMap.size()));


        // deletion
        System.out.println("Deletion test");
        startTime = System.nanoTime();
        deleteList2.forEach(s -> {
            inv2.deleteItem(s);
        });

        endTime = System.nanoTime();
        testResults.add(String.format("deletion time for %s elements in inventory 2: %s Milliseconds", deleteCount2, (double)(endTime - startTime)/1000000));
        testResults.add(String.format("Number of items in inventory 2: %s\n", inv2.itemMap.size()));

        // add elements back to perform other operations (also showcases duplicate checking)
        System.out.println("Insertion test after deletion");
        startTime = System.nanoTime();
        itemList2.forEach( it -> {
            inv2.insertItem(it.getId(), it.getName(), it.getCategory(), it.getQuantity());
        });
        commonList.forEach( it -> {
            inv2.insertItem(it.getId(), it.getName(), it.getCategory(), it.getQuantity());
        });
        endTime = System.nanoTime();

        testResults.add(String.format("Insertion time after deletion for %s + %s elements into inventory 2: %s Milliseconds", n, common, (double)(endTime - startTime)/1000000));
        testResults.add(String.format("Number of items in inventory 2 after inserting again: %s\n", inv2.itemMap.size()));


        // updation
        System.out.println("Updation test");
        startTime = System.nanoTime();
        updateList2.forEach( it -> {
            inv2.updateItem(it.getId(), it.getQuantity());
        });
        endTime = System.nanoTime();
        testResults.add(String.format("Updation time for %s elements in inventory 2: %s Milliseconds\n", updateCount2, (double)(endTime - startTime)/1000000));

        // searching
        System.out.println("Searching test");
        startTime = System.nanoTime();
        searchList2.forEach( s -> {
            inv2.search(s);
        });
        endTime = System.nanoTime();
        testResults.add(String.format("Search time for %s elements in inventory 2: %s Milliseconds\n", searchCount2, (double)(endTime - startTime)/1000000));

        // get all category elements
        System.out.println("Category test");
        startTime = System.nanoTime();
        inv2.getCategoryItems(selectedCat);
        endTime = System.nanoTime();
        testResults.add(String.format("Category view time for %s in inventory 2: %s Milliseconds\n", selectedCat, (double)(endTime - startTime)/1000000));


        // get k items from all categories
        System.out.println("K items test");
        startTime = System.nanoTime();
        inv2.getKItems(k);
        endTime = System.nanoTime();
        testResults.add(String.format("K items view time for %s elements in inventory 2: %s Milliseconds\n", k, (double)(endTime - startTime)/1000000));



        // merging

        System.out.println("Merging test");

        startTime = System.nanoTime();
        InventorySystem merged = InventorySystem.merge(inv1, inv2);
        endTime = System.nanoTime();

        testResults.add(String.format("Time to merge both inventory 1 and inventory 2 is: %s Milliseconds", (double)(endTime - startTime)/1000000));
        testResults.add(String.format("Length of merged inventory: %s\n", merged.itemMap.size()));


        // final result displaying
        System.out.println("\n\nFinal results=>");
        for (String s: testResults){
            System.out.println(s);
        }
    }


}