package com.example.demo.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Order;
import com.example.demo.models.OrderItem;
import com.example.demo.models.Product;
import com.example.demo.repo.OrderItemRepo;
import com.example.demo.repo.OrderRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.UserRepo;
import com.stripe.model.Event;
import com.stripe.model.LineItem;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionListLineItemsParams;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
	private final OrderRepo orderRepo;
	private final OrderItemRepo orderItemRepo;
	private final UserRepo userRepo;
	private final ProductRepo productRepo;

	@Autowired
	public OrderService(OrderRepo orderRepo, OrderItemRepo orderItemRepo, UserRepo userRepo, ProductRepo productRepo) {
		this.orderRepo = orderRepo;
		this.orderItemRepo = orderItemRepo;
		this.userRepo = userRepo;
		this.productRepo = productRepo;
	}

	public void handleSuccessfulPayment(Event event) {
		Session session = (Session) event.getDataObjectDeserializer().getObject().get();
		String sessionId = session.getId();
		long userId = Long.valueOf(session.getClientReferenceId());

		try {
			// Expand each line item
			SessionListLineItemsParams listItemsParams = SessionListLineItemsParams.builder().addExpand("data.price.product")
					.build();

			// Get the data for each of them
			List<LineItem> lineItems = session.listLineItems(listItemsParams).getData();

			// Store a dictionary of product IDs to quantities
			Map<String, Long> orderedProducts = new HashMap<>();

			for (LineItem item : lineItems) {
				String productId = item.getPrice().getProductObject().getMetadata().get("product_id");
				if (productId == null) {
					System.out.println("Product ID not found in metadata");
					continue;
				}
				long quantity = item.getQuantity();
				orderedProducts.put(productId, quantity);
			}

			// Save user order information to the database
			// Step 1: Create a new Order object that records the user and the status
			Order order = new Order();
			order.setUser(userRepo.findById(userId).get());
			order.setStatus("processing");
			orderRepo.save(order);

			// Step 2: Create a new OrderItem object for each product in the order
			// Since the are mulitple products that the user has ordered, loop through the
			// orderedProducts map and create an OrderItem object for each product
			for (Map.Entry<String, Long> entry : orderedProducts.entrySet()) {
				String productId = entry.getKey();
				Long quantity = entry.getValue();
				Product product = productRepo.findById(Long.valueOf(productId)).get();
				// Create a new OrderItem object
				OrderItem orderItem = new OrderItem(order, product, quantity,
						product.getPrice());

				// Save the OrderItem object to the database
				orderItemRepo.save(orderItem);
			}
		} catch (Exception e) {
			System.out.println("Error handling stripe event: " + e.getMessage());
		}
	}

	@Transactional
	public void deleteOrder(Long orderId) {
		orderRepo.deleteById(orderId);
	}
}