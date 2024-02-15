public class CustomerData {
    String customerName;
    Alist<Order> orders = new Alist<Order>();
     public CustomerData(String customerName, String date, String item){
        this.customerName = customerName;
        Order order = new Order(date, item);
        orders.add(order);
    }

    public String getCustomerName() {
        return customerName;
    }

    public Alist<Order> getOrders() {
        return orders;
    }

    public void printCustomerData(){
        System.out.println("customer name is: " +customerName);
        System.out.println("Orders:");
        for (int i = 1; i <= orders.getLength(); i++) {
            System.out.print(orders.getEntry(i).getDate() + "  ");
            System.out.println(orders.getEntry(i).getItem());
        }
    }

    public void addNewOrder(String date, String item) {//adds orders sorted
        Order newOrder = new Order(date, item);
        int len = orders.getLength();
    
        if (len == 0) {
            orders.add(newOrder);
        } else {
            for (int i = 1; i <= len; i++) {
                int comparisonResult = orders.getEntry(i).compareTo(newOrder);
                if (comparisonResult < 0) {
                    orders.add(i, newOrder);
                    return; // Break out of the loop after adding the new order
                }
            }
            orders.add( newOrder);
        }
    }

    private class Order implements Comparable<Order>{
        String date;
        String item;

        public Order(String date, String item){
            this.date = date;
            this.item = item;
        }

        public String getDate() {
            return date;
        }

        public String getItem() {
            return item;
        }
        
        @Override
        public int compareTo(Order other) { // if other date is newer, it gives a negative value
        String[] s1 = this.date.split("-");
        String[] s2 = other.date.split("-");

        for (int i = 0; i < 3; i++) {
            int comparison = Integer.compare(Integer.parseInt(s1[i]), Integer.parseInt(s2[i]));
            if (comparison != 0) {
                return comparison;
            }
        }
        return 0; // Dates are equal
        }
    }
}
