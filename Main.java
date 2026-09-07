import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    private static ArrayList<Item> inventory = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final String[] VALID_CATEGORIES = {"Clothing", "Electronics", "Entertainment"};

    public static void main(String[] args) {
        boolean running = true; 

        while (running) {
            printMenu();
            int choice = getValidatedIntInput("Enter your choice (1-9): ", 1, 9);

            switch (choice) {
                case 1:
                    addItem();
                    break;
                case 2:
                    updateItem();
                    break;
                case 3:
                    removeItem();
                    break;
                case 4:
                    displayItemsByCategory();
                    break;
                case 5:
                    displayAllItems();
                    break;
                case 6:
                    searchItem();
                    break;
                case 7:
                    sortItems();
                    break;
                case 8:
                    displayLowStockItems();
                    break;
                case 9:
                    running = false;
                    System.out.println("Exiting program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice! Please enter a number between 1 and 9.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("=========================================");
        System.out.println("                  Menu        ");
        System.out.println("=========================================");
        System.out.println("1.  Add Item");
        System.out.println("2.  Update Item");
        System.out.println("3.  Remove Item");
        System.out.println("4.  Display Items by Category");
        System.out.println("5.  Display All Items");
        System.out.println("6.  Search Item");
        System.out.println("7.  Sort Items");
        System.out.println("8.  Display Low Stock Items");
        System.out.println("9.  Exit");
        System.out.println("=========================================");
    }

    private static boolean isValidCategory(String input) {
        for (String cat : VALID_CATEGORIES) {
            if (cat.equalsIgnoreCase(input)) {
                return true;
            }
        }
        return false;
    }

    private static String capitalizeCategory(String input) {
        for (String cat : VALID_CATEGORIES) {
            if (cat.equalsIgnoreCase(input)) {
                return cat;
            }
        }
        return input;
    }

    private static Item findItemById(String id) {
        for (Item item : inventory) {
            if (item.getId().equalsIgnoreCase(id.trim())) {
                return item;
            }
        }
        return null;
    }

    private static int getValidatedIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("Value must be between " + min + " and " + max + ". Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid integer.");
            }
        }
    }

    private static double getValidatedDoubleInput(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (Double.isNaN(value) || Double.isInfinite(value)) {
                    System.out.println("Invalid numeric value. Please try again.");
                    continue;
                }
                if (value < min) {
                    System.out.println("Price cannot be negative. Please try again.");
                    continue;
                }
                if (value > max) {
                    System.out.printf("Price cannot exceed ₱%,.2f. Please try again.\n", max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }
    
    private static String getNonEmptyInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    private static boolean isInventoryEmpty() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is currently empty!");
            return true;
        }
        return false;
    }

    private static void addItem() {
        String categoryInput;
        while (true) {
            categoryInput = getNonEmptyInput("Input Category (Clothing, Electronics, Entertainment): ");
            if (isValidCategory(categoryInput)) {
                break;
            }
            System.out.println("Category " + categoryInput + " does not exist! Please enter a valid category.");
        }

        String id;
        while (true) {
            id = getNonEmptyInput("Input ID: ");
            if (findItemById(id) == null) {
                break;
            }
            System.out.println("Error: An item with ID " + id + " already exists! Please use a unique ID.");
        }

        String name;
        while (true) {
            name = getNonEmptyInput("Input Name: ");
            if (name.length() <= Item.MAX_NAME_LENGTH) {
                break;
            }
            System.out.println("Name cannot exceed " + Item.MAX_NAME_LENGTH + " characters. Please try again.");
        }

        int quantity = getValidatedIntInput("Input Quantity (0-" + Item.MAX_QUANTITY + "): ", 0, Item.MAX_QUANTITY);
        double price = getValidatedDoubleInput("Input Price (0-" + Item.MAX_PRICE + "): ₱", 0.0, Item.MAX_PRICE);

        inventory.add(new InventoryItem(id, name, quantity, price, capitalizeCategory(categoryInput)));
        System.out.println("Item added successfully!");
    }

    private static void updateItem() {
        if (isInventoryEmpty()) return;

        String id = getNonEmptyInput("Input ID first: ");
        Item item = findItemById(id);

        if (item == null) {
            System.out.println("Item not found!");
            return;
        }

        String choice;
        while (true) {
            System.out.print("Do you want to update quantity or price?: ");
            choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("quantity") || choice.equals("price")) {
                break;
            }
            System.out.println("Invalid choice! Please type 'quantity' or 'price'.");
        }

        if (choice.equals("quantity")) {
            int oldVal = item.getQuantity();
            int newVal = getValidatedIntInput("Input new quantity (0-" + Item.MAX_QUANTITY + "): ", 0, Item.MAX_QUANTITY);
            item.setQuantity(newVal);
            System.out.println("Quantity of Item " + item.getName() + " updated from " + oldVal + " to " + newVal);
        } else {
            double oldVal = item.getPrice();
            double newVal = getValidatedDoubleInput("Input new price (0-" + Item.MAX_PRICE + "): ₱ ", 0.0, Item.MAX_PRICE);
            item.setPrice(newVal);
            System.out.printf("Price of Item %s updated from ₱%,.2f to ₱%,.2f\n", item.getName(), oldVal, newVal);
        }
    }

    private static void removeItem() {
        if (isInventoryEmpty()) return;

        String id = getNonEmptyInput("Input ID: ");
        Item item = findItemById(id);

        if (item != null) {
            inventory.remove(item);
            System.out.println("Item " + item.getName() + " has been removed from the inventory.");
        } else {
            System.out.println("Item not found!");
        }
    }

    private static void displayItemsByCategory() {
        if (isInventoryEmpty()) return;

        String categoryInput;
        while (true) {
            categoryInput = getNonEmptyInput("Input Category (Clothing, Electronics, Entertainment): ");
            if (isValidCategory(categoryInput)) {
                break;
            }
            System.out.println("Category " + categoryInput + " does not exist! Please enter a valid category.");
        }

        String targetCategory = capitalizeCategory(categoryInput);
        System.out.printf("%-10s | %-20s | %-10s | %-10s\n", "ID", "Name", "Quantity", "Price"); 
        System.out.println("--------------------------------------------------------");
        
        boolean found = false;
        for (Item item : inventory) {
            if (item.getCategory().equalsIgnoreCase(targetCategory)) {
                System.out.printf("%-10s | %-20s | %-10d | ₱%,-10.2f\n", 
                    item.getId(), item.getName(), item.getQuantity(), item.getPrice()); 
                found = true;
            }
        }
        if (!found) {
            System.out.println("No items found under this category.");
        }
    }

    private static void displayAllItems() {
        if (isInventoryEmpty()) return;

        System.out.printf("%-10s | %-20s | %-10s | %-10s | %-15s\n", "ID", "Name", "Quantity", "Price", "Category"); 
        System.out.println("-----------------------------------------------------------------------");
        for (Item item : inventory) {
            System.out.printf("%-10s | %-20s | %-10d | ₱%,-10.2f | %-15s\n", 
                item.getId(), item.getName(), item.getQuantity(), item.getPrice(), item.getCategory()); 
        }
    }

    private static void searchItem() {
        if (isInventoryEmpty()) return;

        String id = getNonEmptyInput("Input ID: ");
        Item item = findItemById(id);

        if (item != null) {
            System.out.println("Item details:");
            System.out.println("ID       : " + item.getId());
            System.out.println("Name     : " + item.getName());
            System.out.println("Quantity : " + item.getQuantity());
            System.out.printf("Price    : ₱%,.2f\n", item.getPrice());
            System.out.println("Category : " + item.getCategory());
        } else {
            System.out.println("Item not found!");
        }
    }

    private static void sortItems() {
        if (isInventoryEmpty()) return;

        String sortBy;
        while (true) {
            System.out.print("Input if sort by quantity or price: ");
            sortBy = scanner.nextLine().trim().toLowerCase();
            if (sortBy.equals("quantity") || sortBy.equals("price")) {
                break;
            }
            System.out.println("Invalid field! Please enter either 'quantity' or 'price'.");
        }

        String order;
        while (true) {
            System.out.print("Input if ascending or descending (asc/desc): ");
            order = scanner.nextLine().trim().toLowerCase();
            if (order.equals("asc") || order.equals("desc")) {
                break;
            }
            System.out.println("Invalid order! Please enter either 'asc' or 'desc'.");
        }

        Comparator<Item> comparator = null;
        if (sortBy.equals("quantity")) {
            comparator = Comparator.comparingInt(Item::getQuantity);
        } else {
            comparator = Comparator.comparingDouble(Item::getPrice);
        }

        if (order.equals("desc")) {
            comparator = comparator.reversed();
        }

        ArrayList<Item> sortedList = new ArrayList<>(inventory);
        Collections.sort(sortedList, comparator);

        System.out.printf("%-10s | %-20s | %-10s | %-10s | %-15s\n", "ID", "Name", "Quantity", "Price", "Category"); 
        System.out.println("-----------------------------------------------------------------------");
        for (Item item : sortedList) {
            System.out.printf("%-10s | %-20s | %-10d | ₱%,-10.2f | %-15s\n", 
                item.getId(), item.getName(), item.getQuantity(), item.getPrice(), item.getCategory()); 
        }
    }

    private static void displayLowStockItems() {
        if (isInventoryEmpty()) return;

        System.out.println("Display Low Stock Items (Quantity <= 5):");
        System.out.printf("%-10s | %-20s | %-10s | %-10s | %-15s\n", "ID", "Name", "Quantity", "Price", "Category"); 
        System.out.println("-----------------------------------------------------------------------");
        
        boolean found = false;
        for (Item item : inventory) {
            if (item.getQuantity() <= 5) { 
                System.out.printf("%-10s | %-20s | %-10d | ₱%,-10.2f | %-15s\n", 
                    item.getId(), item.getName(), item.getQuantity(), item.getPrice(), item.getCategory()); 
                found = true;
            }
        }
        if (!found) {
            System.out.println("No low stock items found.");
        }
    }
}