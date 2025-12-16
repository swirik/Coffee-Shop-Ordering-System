public class OrderItem {
    private final MenuItem menuItem;
    private final String size;

    public OrderItem(MenuItem menuItem, String size) {
        this.menuItem = menuItem;
        this.size = size;
    }

    public double getPrice() {
        double p = menuItem.getPrice();
        if ("Medium".equals(size)) p += 0.5;
        if ("Large".equals(size)) p += 1.0;
        return p;
    }

    public String getDetails() {
        return String.format("%s (%s)", menuItem.getName(), size);
    }

    @Override
    public String toString() {
        return String.format("%s ₱%.2f", getDetails(), getPrice());
    }
}