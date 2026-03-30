import api from './axios'

export const createOrder = (memberId, deliveryAddress) =>
  api.post('/orders', { memberId, deliveryAddress })

export const getMemberOrders = (memberId) =>
  api.get(`/orders/member/${memberId}`)

export const getOrderDetail = (orderId) =>
  api.get(`/orders/${orderId}`)

export const updateOrderStatus = (orderId, status) =>
  api.patch(`/orders/${orderId}/status`, { status })
