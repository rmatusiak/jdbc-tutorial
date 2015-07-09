package com.acme.order;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.acme.order.pizza.PizzaOrder;
import com.acme.order.pizza.PizzaType;

@Slf4j
@Repository
@Primary
public class JdbcOrderRepository implements OrderRepository {

	private final String url = "jdbc:mysql://localhost:3306/pizza-tutorial";

	private final String user = "dbuser";

	private final String password = "dbpass";
	
	//private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	
	public JdbcOrderRepository() {
		BasicDataSource datasource = new BasicDataSource();
		datasource.setDriverClassName("com.mysql.jdbc.Driver");
		datasource.setUrl(url);
		datasource.setUsername(user);
		datasource.setPassword(password);
		datasource.setInitialSize(5);
		datasource.setMaxTotal(20);
		
		//this.dataSource = datasource;
		
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}
	

	@Override
	public String save(PizzaOrder order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}

	@Override
	public PizzaOrder get(String pizzaOrderId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*@Override
	public List<PizzaOrder> findAll() {
		
		List<PizzaOrder> orders = new ArrayList<>();
		
		try (Connection conn = DriverManager.getConnection(url, user, password)){
			Statement statement = conn.createStatement();
			
			final String SQL = "SELECT o.id as order_id,o.customer_id as customer_id, o.status, o.type, o.estimatedDeliveryTime," + 
			"o.finishTime,c.name,c.email,c.address from order_t o,customer_t c where o.customer_id = c.id";

			ResultSet rs = statement.executeQuery(SQL);
			
			while (rs.next()){
				orders.add(buildOrder(rs));
			}
			
			
		} catch (SQLException e) {
			log.info("Connection to database failed");
		}
		
		return orders;
	}*/
	
	/*@Override
	public List<PizzaOrder> findAll() {
		
		List<PizzaOrder> orders = new ArrayList<>();
		
		try (Connection connection = dataSource.getConnection()){
			
			Statement statement = connection.createStatement();
			
			final String SQL = "SELECT o.id as order_id,o.customer_id as customer_id, o.status, o.type, o.estimatedDeliveryTime," + 
					"o.finishTime,c.name,c.email,c.address from order_t o,customer_t c where o.customer_id = c.id";

			ResultSet rs = statement.executeQuery(SQL);
					
			while (rs.next()){
				orders.add(buildOrder(rs));
			}
			
			
		} catch (SQLException e) {
			log.info("Connection to database failed");
		}
		
		
		return orders;
	}*/
	
	@Override
	public List<PizzaOrder> findAll(){
		
		return this.jdbcTemplate.query( "SELECT o.id as order_id,o.customer_id as customer_id, o.status, o.type, o.estimatedDeliveryTime," + 
					"o.finishTime,c.name,c.email,c.address from order_t o,customer_t c where o.customer_id = c.id", new PizzaOrderMapper());
		
	}
	

	private static final class PizzaOrderMapper implements RowMapper<PizzaOrder> {

		public PizzaOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
			String type = rs.getString("type");
			String customerName = rs.getString("name");
			String customerId = rs.getString("customer_id");
			String customerEmail = rs.getString("email");
			String customerAddress = rs.getString("address");
			
			Customer customer = new Customer(customerId, customerName, customerEmail, customerAddress);
			PizzaOrder pizzaOrder = new PizzaOrder(customer, PizzaType.valueOf(type));
					
			return pizzaOrder;
		}
	}

	private PizzaOrder buildOrder(ResultSet rs) throws SQLException {
		String type = rs.getString("type");
		String customerName = rs.getString("name");
		String customerId = rs.getString("customer_id");
		String customerEmail = rs.getString("email");
		String customerAddress = rs.getString("address");
		
		Customer customer = new Customer(customerId, customerName, customerEmail, customerAddress);
		PizzaOrder pizzaOrder = new PizzaOrder(customer, PizzaType.valueOf(type));
				
		return pizzaOrder;
	}

	@Override
	public List<PizzaOrder> findByOrderStatus(OrderStatus orderStatus) {
		// TODO Auto-generated method stub
		return null;
	}

}
