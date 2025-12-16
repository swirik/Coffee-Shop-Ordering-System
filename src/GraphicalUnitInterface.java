import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class GraphicalUnitInterface extends JPanel {

    private final Stack<OrderItem> cart = new Stack<>();
    private final PriorityQueue<Order> orderQueue = new PriorityQueue<>();

    private final JPanel menuGrid;
    private final DefaultListModel<String> cartModel;
    private final DefaultListModel<String> queueModel;
    private final JLabel totalLabel;
    private final JCheckBox rushCheckBox;

    public GraphicalUnitInterface() {

        setLayout(new BorderLayout());
        setBackground(UIComponents.BG_COLOR);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        header.setBackground(UIComponents.BG_COLOR);
        JLabel title = new JLabel("Coffee Shop Ordering System");
        title.setFont(UIComponents.getHeaderFont());
        title.setForeground(UIComponents.TEXT_DARK);
        header.add(title);
        add(header, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(UIComponents.BG_COLOR);
        centerContainer.setBorder(new EmptyBorder(20, 40, 20, 20));

        menuGrid = new JPanel(new GridLayout(0, 3, 20, 20));
        menuGrid.setBackground(UIComponents.BG_COLOR);
        MenuItems();

        JScrollPane scroll = new JScrollPane(menuGrid);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(UIComponents.BG_COLOR);
        centerContainer.add(scroll, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        rightPanel.setPreferredSize(new Dimension(320, 0));
        rightPanel.setBackground(UIComponents.BG_COLOR);
        rightPanel.setBorder(new EmptyBorder(20, 10, 20, 30));

        cartModel = new DefaultListModel<>();
        queueModel = new DefaultListModel<>();
        totalLabel = new JLabel("Total: ₱0.00");
        rushCheckBox = new JCheckBox("Rush Order (Priority)");
        rushCheckBox.setBackground(Color.WHITE);
        rushCheckBox.setFont(UIComponents.getBodyFont());

        rightPanel.add(createCartPanel());
        rightPanel.add(createQueuePanel());

        add(rightPanel, BorderLayout.EAST);
    }

    private void MenuItems() {

        MenuItem[] items = {
                new MenuItem("Espresso", 79, "Coffee"),
                new MenuItem("Cafe Latte", 120, "Coffee"),
                new MenuItem("Matcha", 89, "Coffee"),
                new MenuItem("Cappuccino", 79, "Coffee"),
                new MenuItem("Plain Pretzel", 99, "Food"),
                new MenuItem("Croissant", 129, "Food")
        };

        for (MenuItem m : items) {
            menuGrid.add(createItemCard(m));
        }

    }

    private JPanel createItemCard(MenuItem item) {

        UIComponents.CardPanel card = new UIComponents.CardPanel();
        card.setLayout(new BorderLayout(0, 10));

        JPanel placeholder = new JPanel();
        placeholder.setBackground(new Color(240, 240, 240));
        placeholder.setPreferredSize(new Dimension(100, 120));
        card.add(placeholder, BorderLayout.CENTER);

        JPanel info = new JPanel(new GridLayout(3, 1));
        info.setBackground(Color.WHITE);

        JLabel name = new JLabel(item.getName(), SwingConstants.CENTER);
        name.setFont(UIComponents.getItemFont());
        name.setForeground(UIComponents.TEXT_DARK);

        JLabel price = new JLabel("₱" + item.getPrice(), SwingConstants.CENTER);
        price.setFont(UIComponents.getBodyFont());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        JButton addBtn = new UIComponents.ModernButton("+", UIComponents.TEXT_DARK);
        addBtn.setPreferredSize(new Dimension(30, 30));
        addBtn.addActionListener(e -> addItem(item));

        info.add(name);
        info.add(price);
        info.add(btnPanel);
        btnPanel.add(addBtn);

        card.add(info, BorderLayout.SOUTH);
        return card;

    }

    private JPanel createCartPanel() {

        UIComponents.CardPanel panel = new UIComponents.CardPanel();
        panel.setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Cart (Customer Side)");
        title.setFont(UIComponents.getItemFont());
        panel.add(title, BorderLayout.NORTH);

        JList<String> list = new JList<>(cartModel);
        list.setFont(UIComponents.getBodyFont());
        list.setBorder(null);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(3, 1, 5, 5));
        bottom.setBackground(Color.WHITE);

        bottom.add(rushCheckBox);
        bottom.add(totalLabel);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 5, 0));
        buttons.setBackground(Color.WHITE);

        JButton undo = new UIComponents.ModernButton("Undo", new Color(200, 100, 100));
        undo.addActionListener(e -> undo());
        JButton pay = new UIComponents.ModernButton("Complete Order", UIComponents.ACCENT_COLOR);
        pay.addActionListener(e -> checkout());

        buttons.add(undo);
        buttons.add(pay);
        bottom.add(buttons);

        panel.add(bottom, BorderLayout.SOUTH);
        return panel;

    }

    private JPanel createQueuePanel() {

        UIComponents.CardPanel panel = new UIComponents.CardPanel();

        panel.setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Queue (Staff Side - Optional)");
        title.setFont(UIComponents.getItemFont());
        panel.add(title, BorderLayout.NORTH);

        JList<String> list = new JList<>(queueModel);
        list.setFont(UIComponents.getBodyFont());
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JButton complete = new UIComponents.ModernButton("Pay", UIComponents.TEXT_DARK);
        complete.addActionListener(e -> completeOrder());
        panel.add(complete, BorderLayout.SOUTH);

        return panel;
    }

    private void addItem(MenuItem item) {

        String size = "Standard";

        if (item.getCategory().equals("Coffee")) {
            String[] sizes = {"Standard", "Medium", "Large"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Size?",
                    "Select",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    sizes,
                    sizes[0]
            );

            if (choice == -1) return;
            size = sizes[choice];
        }

        cart.push(new OrderItem(item, size));
        refreshCart();
    }


    private void undo() {
        if (!cart.isEmpty()) {
            cart.pop();
            refreshCart();
        }
    }


    private void checkout() {

        if (cart.isEmpty()) {
            return;
        }

        List<OrderItem> items = new ArrayList<>(cart);
        boolean isRush = rushCheckBox.isSelected();
        Order order = new Order("Guest", items, isRush);
        orderQueue.add(order);

        JOptionPane.showMessageDialog(this,
                "Order Placed Successfully!\nYour Order ID: " + order.getId(),
                "Order Confirmed",
                JOptionPane.INFORMATION_MESSAGE);

        cart.clear();
        rushCheckBox.setSelected(false);
        refreshCart();
        refreshQueue();
    }

    private void completeOrder() {

        if (orderQueue.isEmpty()) {
            return;
        }

        Order completed = orderQueue.poll();
        completed.setStatus("COMPLETED");
        refreshQueue();
        showCompletedDetails(completed);
    }

    private void showCompletedDetails(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("========== ORDER COMPLETED ==========\n");
        sb.append("Order ID: ").append(order.getId()).append("\n");
        sb.append("Time: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append("\n");
        sb.append("Status: ").append(order.isRush() ? "PRIORITY RUSH" : "Standard").append("\n");
        sb.append("-------------------------------------\n");

        double total = 0;
        for (OrderItem item : order.getItems()) {
            sb.append(String.format("%-25s ₱%.2f\n", item.getDetails(), item.getPrice()));
            total += item.getPrice();
        }

        sb.append("-------------------------------------\n");
        sb.append(String.format("TOTAL PAID:                  ₱%.2f\n", total));
        sb.append("=====================================\n");

        JTextArea area = new JTextArea(sb.toString());
        area.setFont(UIComponents.getMonoFont());
        area.setEditable(false);
        area.setOpaque(false);

        JOptionPane.showMessageDialog(this, area, "Order Details", JOptionPane.PLAIN_MESSAGE);
    }

    private void refreshQueue() {
        queueModel.clear();
        List<Order> displayList = new ArrayList<>(orderQueue);
        Collections.sort(displayList);

        for (Order o : displayList) {
            String prefix = o.isRush() ? "[RUSH] " : "";
            queueModel.addElement(prefix + "Order #" + o.getId() + " - " + o.getStatus());
        }
    }

    private void refreshCart() {
        cartModel.clear();
        double t = 0;
        for (OrderItem i : cart) {
            cartModel.addElement(i.toString());
            t += i.getPrice();
        }
        totalLabel.setText(String.format("Total: ₱%.2f", t));
    }
}