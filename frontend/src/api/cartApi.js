import api from './axios'

export const getCart = (memberId) =>
  api.get(`/carts/${memberId}`)

export const addToCart = (memberId, productId, quantity) =>
  api.post('/carts', { memberId, productId, quantity })

export const updateCartItem = (cartItemId, quantity) =>
  api.patch(`/carts/items/${cartItemId}`, { quantity })

export const removeCartItem = (cartItemId) =>
  api.delete(`/carts/items/${cartItemId}`)
