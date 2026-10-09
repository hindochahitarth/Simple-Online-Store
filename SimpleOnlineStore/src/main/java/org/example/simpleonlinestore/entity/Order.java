package org.example.simpleonlinestore.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.example.simpleonlinestore.enums.OrderStatus;
import org.example.simpleonlinestore.service.impl.order.CancelledState;
import org.example.simpleonlinestore.service.impl.order.PaymentFailedState;
import org.example.simpleonlinestore.service.impl.order.PaymentPendingState;
import org.example.simpleonlinestore.service.impl.order.PlacedState;
import org.example.simpleonlinestore.service.interfaces.OrderState;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"orders", "addresses", "cart", "password", "authorities"})

    private User user;

    @Transient
    private OrderState state;
    @PostLoad
    public void initRuntimeState() {
        this.state = switch (this.status) {
            case PAYMENT_PENDING -> new PaymentPendingState();
            case PLACED -> new PlacedState();
            case PAYMENT_FAILED -> new PaymentFailedState();
            case PENDING -> null;
            case CONFIRMED -> null;
            case CANCELED -> new CancelledState();
        };
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    //precision - total number of digits
    //scale - after fraction (8+2)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;//used bigdecimal because in double precision error is there,rounding errors

        @Column(nullable = false, updatable = false)
    private LocalDateTime orderDate;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnoreProperties("order")

    private List<OrderItem> items = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
    }

    @Column(name = "razorpay_order_id")
    private String razorpayOrderId;

    @Column(name = "razorpay_payment_id")
    private String razorpayPaymentId;

}