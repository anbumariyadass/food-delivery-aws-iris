import { useEffect, useState } from 'react';
import Layout from './Layout';
import axios from 'axios';
import '../styles/RestaurantPage.css';
import { BASE_API } from '../config'; 

const DeliveryPersonalPage = () => {
  const user = JSON.parse(localStorage.getItem('user'));
  const [activeTab, setActiveTab] = useState('orders');
  const [info, setInfo] = useState(null);
  const [orders, setOrders] = useState([]);
  const [filterStatus, setFilterStatus] = useState('ALL');

  const tokenHeader = {
    headers: {
      Authorization: `Bearer ${user?.token}`
    }
  };

  const fetchInfo = () => {
    axios.get(`${BASE_API}/delivery/getDeliveryPersonal`, tokenHeader)
      .then(res => setInfo(res.data.data))
      .catch(() => setInfo(null));
  };

  const fetchOrders = () => {
    axios.get(`${BASE_API}/delivery/personal/orders`, tokenHeader)
      .then(res => setOrders(res.data.data))
      .catch(err => console.error(err));
  };

  const updateOrderStatus = (orderId, newStatus) => {
    axios.put(`${BASE_API}/delivery/${orderId}/updateOrderStatus?orderStatus=${newStatus}`, {}, tokenHeader)
      .then(() => fetchOrders())
      .catch(err => alert('Update Failed'));
  };

  useEffect(() => {
    if (activeTab === 'info') fetchInfo();
    if (activeTab === 'orders') fetchOrders();
  }, [activeTab]);

  const filteredOrders = filterStatus === 'ALL' ? orders : orders.filter(o => o.orderStatus === filterStatus);

  return (
    <Layout>
      <div className="restaurant-tabs">        
        <button onClick={() => setActiveTab('orders')} className={activeTab === 'orders' ? 'active' : ''}>My Orders</button>
        <button onClick={() => setActiveTab('info')} className={activeTab === 'info' ? 'active' : ''}>My Info</button>
      </div>

      <div className="restaurant-content">
        {activeTab === 'info' && (
          <div>
            {info ? (
              <div>
                <p><strong>Name:</strong> {info.deliveryPersonalName}</p>
                <p><strong>Address:</strong> {info.address}</p>
                <p><strong>Email:</strong> {info.email}</p>
                <p><strong>Phone:</strong> {info.phone}</p>
                <p><strong>Gender:</strong> {info.gender}</p>
              </div>
            ) : (
              <p>No delivery personal info found.</p>
            )}
          </div>
        )}

        {activeTab === 'orders' && (
          <div>
            <h3>My Delivery Orders</h3>
            <label>Status Filter: </label>
            <select onChange={(e) => setFilterStatus(e.target.value)} value={filterStatus}>
              <option value="ALL">ALL</option>              
              <option value="ORDERED">ORDERED</option>   
              <option value="ACKNOWLEDGED">ACKNOWLEDGED</option>
              <option value="READYTOPICK">READYTOPICK</option>           
              <option value="INTRANSIT">INTRANSIT</option>
              <option value="DELIVERED">DELIVERED</option>    
            </select>
            {filteredOrders.map(order => (
              <div key={order.orderId} className="order-box">
                <p><strong>Order ID:</strong> {order.orderId}</p>
                <p><strong>Restaurant:</strong> {order.restaurantName}</p>
                <p><strong>Customer:</strong> {order.customerContactName}</p>
                <p><strong>Address:</strong> {order.customerContactAddress}</p>
                <p><strong>Status:</strong> {order.orderStatus}</p>
                <p><strong>Total:</strong> ₹{order.totalPrice}</p>
                {['ORDERED', 'ACKNOWLEDGED', 'READYTOPICK'].includes(order.orderStatus) && (
                  <div style={{ marginTop: '10px' }}>
                    {order.orderStatus !== 'INTRANSIT' && (
                      <button className="edit-btn" onClick={() => updateOrderStatus(order.orderId, 'INTRANSIT')}>Mark In-Transit</button>
                    )}                 
                  </div>
                )}
                {order.orderStatus === 'INTRANSIT' && (
                  <div style={{ marginTop: '10px' }}>                   
                    <button className="add-btn" onClick={() => updateOrderStatus(order.orderId, 'DELIVERED')}>Mark Delivered</button>
                  </div>
                )}
              </div>
            ))}
          </div>
        )}
      </div>
    </Layout>
  );
};

export default DeliveryPersonalPage;
