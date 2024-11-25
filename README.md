# Inventory Management System


## Inventory Class functions and their time complexities

### insertItem - O(log n)
Inserts a new item into the system. Checks for duplicates during insertion and alerts the user.

### deleteItem - O(n) (best case is o(log n))
Deletes an item from the system based on its ID.

### updateItem - O(1)
Updates the quantity of an existing item

### search - O(1)
Searches for an item by its ID and prints its details

### getKItems - O(k)
Retrieves and prints the top K items ordered by quantity (irrespective of category)

### getCategoryItems - O(m)
Retrieves and prints all the elements in a category ordered by quantity

### merge - O((n+m)*(log(n+m)))
Merges two inventories and returns a new inventory
(n is the number of elements in inventory 1, m is the number of elements in inventory 2)
