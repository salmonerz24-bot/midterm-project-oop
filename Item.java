public abstract class Item {

    public static final int MAX_QUANTITY = 1000000;
    public static final double MAX_PRICE = 1000000.00;
    public static final int MAX_NAME_LENGTH = 20;

    private String id;
    private String name;
    private int quantity;
    private double price;
    private String category;

    public Item(String id, String name, int quantity, double price, String category) {
        setId(id);
        setName(name);
        setQuantity(quantity);
        setPrice(price);
        setCategory(category);
    }

    public String getId() { 
        return id; 
    }
    
    public final void setId(String id) { 
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be empty.");
        }
        this.id = id.trim(); 
    }

    public String getName() { 
        return name; 
    }
    
    public final void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        String trimmed = name.trim();
        if (trimmed.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Name cannot exceed " + MAX_NAME_LENGTH + " characters.");
        }
        this.name = trimmed;
    }

    public int getQuantity() { 
        return quantity; 
    }
    
    public final void setQuantity(int quantity) { 
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        if (quantity > MAX_QUANTITY) {
            throw new IllegalArgumentException("Quantity cannot exceed " + MAX_QUANTITY + ".");
        }
        this.quantity = quantity; 
    }

    public double getPrice() { 
        return price; 
    }
    
    public final void setPrice(double price) { 
        if (Double.isNaN(price) || Double.isInfinite(price)) {
            throw new IllegalArgumentException("Invalid price value.");
        }
        if (price < 0.0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (price > MAX_PRICE) {
            throw new IllegalArgumentException("Price cannot exceed ₱" + MAX_PRICE + ".");
        }
        this.price = price; 
    }

    public String getCategory() { 
        return category; 
    }
    
    public final void setCategory(String category) { 
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }
        this.category = category.trim();
    }
}