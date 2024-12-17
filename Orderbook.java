import java.util.*;

public class Orderbook {
    private String name;
    private List<Order> buyOrderList;
    private List<Order> sellOrderList;
    private List<String> matchList;

    public Orderbook(String name) {
        this.name = name;
        this.buyOrderList = new ArrayList<>();
        this.sellOrderList = new ArrayList<>();
        this.matchList = new ArrayList<>();
    }

    public void handleNewOrder(Order order) {
        if (order.getType().equals("BUY")) {
            addOrder(buyOrderList, order, Comparator.comparing(Order::getPrice).reversed());
        } else if (order.getType().equals("SELL")) {
            addOrder(sellOrderList, order, Comparator.comparing(Order::getPrice));
        }
        matchOrders();
    }

    private void addOrder(List<Order> orderList, Order order, Comparator<Order> comparator) {
        orderList.add(order);
        orderList.sort(comparator);
    }

    private void matchOrders() {
        while (!buyOrderList.isEmpty() && !sellOrderList.isEmpty()) {
            Order buyOrder = buyOrderList.get(0);
            Order sellOrder = sellOrderList.get(0);

            if (buyOrder.getPrice() >= sellOrder.getPrice()) {
                int matchedQuantity = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
                matchList.add("Match " + matchedQuantity + "@" + sellOrder.getPrice());

                buyOrder.reduceQuantity(matchedQuantity);
                sellOrder.reduceQuantity(matchedQuantity);

                if (buyOrder.getQuantity() == 0) buyOrderList.remove(0);
                if (sellOrder.getQuantity() == 0) sellOrderList.remove(0);
            } else {
                break;
            }
        }
    }

    public void print() {
        System.out.println("Orderbook name: " + name);
        System.out.println("Matches:");
        for (int i = 0; i < matchList.size(); i++) {
            System.out.println((i + 1) + ". " + matchList.get(i));
        }

        System.out.println("\nBuy orders:");
        for (int i = 0; i < buyOrderList.size(); i++) {
            System.out.println((i + 1) + ". Buy " + buyOrderList.get(i));
        }

        System.out.println("\nSell orders:");
        for (int i = 0; i < sellOrderList.size(); i++) {
            System.out.println((i + 1) + ". Sell " + sellOrderList.get(i));
        }
    }

    public static class Order {
        private String type;
        private int quantity;
        private int price;

        public Order(String type, int quantity, int price) {
            this.type = type;
            this.quantity = quantity;
            this.price = price;
        }

        public String getType() { return type; }
        public int getQuantity() { return quantity; }
        public int getPrice() { return price; }

        public void reduceQuantity(int amount) {
            this.quantity -= amount;
        }

        @Override
        public String toString() {
            return quantity + "@" + price;
        }
    }
}
