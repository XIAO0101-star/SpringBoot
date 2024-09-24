package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.models.Order;
import com.example.demo.models.User;
import com.example.demo.repo.OrderItemRepo;
import com.example.demo.repo.OrderRepo;
import com.example.demo.repo.StatusRepo;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/orders")
public class OrdersController {
	private final OrderItemRepo orderItemRepo;
	private final OrderRepo orderRepo;
	private final StatusRepo statusRepo;

	@Autowired
	public OrdersController(OrderItemRepo orderItemRepo, OrderRepo orderRepo, StatusRepo statusRepo) {
		this.orderItemRepo = orderItemRepo;
		this.orderRepo = orderRepo;
		this.statusRepo = statusRepo;
	}

	@GetMapping("")
	public String viewOrders(Model model) {
		List<Order> orders = orderRepo.findAll();
		model.addAttribute("orders", orders);
		return "orders/index";
	}

	@GetMapping("/{id}")
	public String orderDetails(@PathVariable Long id, Model model) {
		// Find the order with matching id
		var order = orderRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Invalid product Id" + id));
		// Find all the corresponding order items
		var orderItem = orderItemRepo.findByOrder(order.getId());

		// Find the user information
		var user = order.getUser();

		model.addAttribute("order", order);
		model.addAttribute("order_item", orderItem);
		model.addAttribute("user", user);
		return "orders/details";
	}

	/*
	 * Two routes for editing orders
	 * 1. Show an edit form
	 * 2. Process the edit
	 */
	@GetMapping("/{id}/edit")
	public String showUpdateOrder(@PathVariable Long id, Model model) {
		var order = orderRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Invalid order Id" + id));
		model.addAttribute("order", order);
		model.addAttribute("allStatus", statusRepo.findAll());
		return "orders/edit";
	}

	@PostMapping("/{id}/edit")
	public String updateOrder(@PathVariable Long id, @Valid @ModelAttribute Order order, Model model,
			BindingResult bindingResult) {

		// Retrieve user from OrderRepo and update the status
		User user = orderRepo.findById(id).get().getUser();
		order.setUser(user);
		order.setId(id);
		order.setStatus(
				statusRepo.findById(
						Long.parseLong(order.getStatus())).get().getName());

		orderRepo.save(order);
		return "redirect:/orders";
	}

	/*
	 * Two routes for deleting
	 * 1. Show a delete form (asking the users if they really want to delete)
	 * 2. Process the delete
	 */
	@GetMapping("/{id}/delete")
	public String showDeleteOrderForm(@PathVariable Long id, Model model) {
		var order = orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
		model.addAttribute("order", order);
		return "orders/delete";
	}

	@PostMapping("/{id}/delete")
	public String deleteOrder(@PathVariable Long id) {
		orderItemRepo.deleteByOrderId(id);
		orderRepo.deleteById(id);
		return "redirect:/orders";
	}
}