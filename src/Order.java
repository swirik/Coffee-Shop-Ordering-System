import java.util.ArrayList;
import java.util.List;

public class Order implements Comparable<Order> {
    private static int idCounter = 100;
    private final int id;
    private final String customer;
    private final List<OrderItem> items;
    private final boolean isRush;
    private String status;

    public Order(String customer, List<OrderItem> items, boolean isRush) {
        this.id = idCounter++;
        this.customer = customer;
        this.items = new ArrayList<>(items);
        this.isRush = isRush;
        this.status = isRush ? "PRIORITY" : "Pending";
    }

    public int getId() { return id; }
    public String getCustomer() { return customer; }
    public List<OrderItem> getItems() { return items; }
    public String getStatus() { return status; }
    public boolean isRush() { return isRush; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public int compareTo(Order other) {
        if (this.isRush && !other.isRush) return -1;
        if (!this.isRush && other.isRush) return 1;
        return Integer.compare(this.id, other.id);
    }
}