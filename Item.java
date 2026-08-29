public abstract class Item {
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
    
    public void setId(String id) { 
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be empty.");
        }
        this.id = id.trim(); 
    }

    public String getName() { 
        return name; 
    }
    
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name.trim();
    }

    public int getQuantity() { 
        return quantity; 
    }
    
    public void setQuantity(int quantity) { 
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.quantity = quantity; 
    }

    public double getPrice() { 
        return price; 
    }
    
    public void setPrice(double price) { 
        if (price < 0.0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price; 
    }

    public String getCategory() { 
        return category; 
    }
    
    public void setCategory(String category) { 
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }
        this.category = category.trim();
    }
}