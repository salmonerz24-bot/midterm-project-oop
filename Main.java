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
            int choice = getValidatedIntInput("Enter your choice (1-9): ");

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
            if (item.getId().equalsIgnoreCase(id)) {
                return item;
            }
        }
        return null;
    }

    private static int getValidatedIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < 0) {
                    System.out.println("Value cannot be negative. Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid integer.");
            }
        }
    }

    private static double getValidatedDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value < 0.0) {
                    System.out.println("Price cannot be negative. Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    private static void addItem() {
        System.out.print("Input Category (Clothing, Electronics, Entertainment): ");
        String categoryInput = scanner.nextLine().trim();
        
        if (!isValidCategory(categoryInput)) {
            System.out.println("Category " + categoryInput + " does not exist!");
            return;
        }

        System.out.print("Input ID: ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("ID cannot be empty.");
            return;
        }
        if (findItemById(id) != null) {
            System.out.println("Error: An item with the ID " + id + " already exists!");
            return;
        }

        System.out.print("Input Name: ");
        String name = scanner.nextLine().trim();

        int quantity = getValidatedIntInput("Input Quantity: ");
        double price = getValidatedDoubleInput("Input Price: ");

        inventory.add(new InventoryItem(id, name, quantity, price, capitalizeCategory(categoryInput)));
        System.out.println("Item added successfully!");
    }

    private static void updateItem() {
        System.out.print("Input ID first: ");
        String id = scanner.nextLine().trim();
        Item item = findItemById(id);

        if (item == null) {
            System.out.println("Item not found!!");
            return;
        }

        System.out.print("Do you want to update quantity or price?: ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("quantity")) {
            int oldVal = item.getQuantity();
            int newVal = getValidatedIntInput("Input new quantity: ");
            item.setQuantity(newVal);
            System.out.println("Quantity of Item " + item.getName() + " is updated from " + oldVal + " to " + newVal + "");
        } else if (choice.equals("price")) {
            double oldVal = item.getPrice();
            double newVal = getValidatedDoubleInput("Input new price: ");
            item.setPrice(newVal);
            System.out.println("Price of Item " + item.getName() + " is updated from " + oldVal + " to " + newVal + "");
        } else {
            System.out.println("Invalid update target selection! Operation cancelled.");
        }
    }

    private static void removeItem() {
        System.out.print("Input ID: ");
        String id = scanner.nextLine().trim();
        Item item = findItemById(id);

        if (item != null) {
            inventory.remove(item);
            System.out.println("Item " + item.getName() + " has been removed from the inventory");
        } else {
            System.out.println("Item not found!");
        }
    }

    private static void displayItemsByCategory() {
        System.out.print("Input Category: ");
        String categoryInput = scanner.nextLine().trim();

        if (!isValidCategory(categoryInput)) {
            System.out.println("Category " + categoryInput + " does not exist!");
            return;
        }

        String targetCategory = capitalizeCategory(categoryInput);
        System.out.printf("%-10s | %-20s | %-10s | %-10s\n", "ID", "Name", "Quantity", "Price"); //
        System.out.println("--------------------------------------------------------");
        
        boolean found = false;
        for (Item item : inventory) {
            if (item.getCategory().equalsIgnoreCase(targetCategory)) {
                System.out.printf("%-10s | %-20s | %-10d | ₱%,-10.2f\n", 
                    item.getId(), item.getName(), item.getQuantity(), item.getPrice()); //
                found = true;
            }
        }
        if (!found) {
            System.out.println("No items found under this category.");
        }
    }

    private static void displayAllItems() {
        System.out.printf("%-10s | %-20s | %-10s | %-10s | %-15s\n", "ID", "Name", "Quantity", "Price", "Category"); //
        System.out.println("-----------------------------------------------------------------------");
        for (Item item : inventory) {
            System.out.printf("%-10s | %-20s | %-10d | %-10.2f | %-15s\n", 
                item.getId(), item.getName(), item.getQuantity(), item.getPrice(), item.getCategory()); //
        }
    }

    private static void searchItem() {
        System.out.print("Input ID: ");
        String id = scanner.nextLine().trim();
        Item item = findItemById(id);

        if (item != null) {
            System.out.println("Item details:");
            System.out.println("ID       : " + item.getId());
            System.out.println("Name     : " + item.getName());
            System.out.println("Quantity : " + item.getQuantity());
            System.out.println("Price    : " + item.getPrice());
            System.out.println("Category : " + item.getCategory());
        } else {
            System.out.println("Item not found!");
        }
    }

    private static void sortItems() {
        System.out.print("Input if sort by quantity or price: ");
        String sortBy = scanner.nextLine().trim().toLowerCase();
        System.out.print("Input if ascending or descending: ");
        String order = scanner.nextLine().trim().toLowerCase();

        if ((!sortBy.equals("quantity") && !sortBy.equals("price")) || 
            (!order.equals("asc") && !order.equals("desc"))) {
            System.out.println("Invalid sorting parameters provided!");
            return;
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

        System.out.printf("%-10s | %-20s | %-10s | %-10s | %-15s\n", "ID", "Name", "Quantity", "Price", "Category"); //
        System.out.println("-----------------------------------------------------------------------");
        for (Item item : sortedList) {
            System.out.printf("%-10s | %-20s | %-10d | %-10.2f | %-15s\n", 
                item.getId(), item.getName(), item.getQuantity(), item.getPrice(), item.getCategory()); //
        }
    }

    private static void displayLowStockItems() {
        System.out.println("Display Low Stock Items (Quantity <= 5):");
        System.out.printf("%-10s | %-20s | %-10s | %-10s | %-15s\n", "ID", "Name", "Quantity", "Price", "Category"); //
        System.out.println("-----------------------------------------------------------------------");
        
        boolean found = false;
        for (Item item : inventory) {
            if (item.getQuantity() <= 5) { //
                System.out.printf("%-10s | %-20s | %-10d | %-10.2f | %-15s\n", 
                    item.getId(), item.getName(), item.getQuantity(), item.getPrice(), item.getCategory()); //
                found = true;
            }
        }
        if (!found) {
            System.out.println("No low stock items found.");
        }
    }
}