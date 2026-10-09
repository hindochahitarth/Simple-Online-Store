package org.example.simpleonlinestore.service.impl.order;
import org.example.simpleonlinestore.entity.Order;
import org.example.simpleonlinestore.entity.OrderItem;
import org.example.simpleonlinestore.entity.Product;
import org.example.simpleonlinestore.enums.OrderStatus;
import org.example.simpleonlinestore.repository.ProductRepository;
import org.example.simpleonlinestore.service.interfaces.OrderState;

public class PaymentPendingState implements OrderState {
    @Override
    public void paymentSuccess(Order order, String paymentId) {
        order.setStatus(OrderStatus.PLACED);
        order.setRazorpayPaymentId(paymentId);
        order.setState(new PlacedState());
    }
    @Override
    public void paymentFailed(Order order, ProductRepository productRepository) {
        order.setStatus(OrderStatus.PAYMENT_FAILED);
        restockInventory(order,productRepository);
        order.setState(new PaymentFailedState());
    }
    @Override
    public void cancelOrder(Order order, ProductRepository productRepository) {
        order.setStatus(OrderStatus.CANCELED);
        restockInventory(order,productRepository);
        order.setState(new CancelledState());
    }
    private void restockInventory(Order order, ProductRepository productRepository) {
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockCount(product.getStockCount() + item.getQuantity());
            productRepository.save(product);
        }
    }
}