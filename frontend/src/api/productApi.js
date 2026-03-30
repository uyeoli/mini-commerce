import api from './axios'

export const searchProducts = (condition = {}) =>
  api.get('/products', { params: condition })

export const getProduct = (id) =>
  api.get(`/products/${id}`)

export const createProduct = (data) =>
  api.post('/products', data)

export const modifyProduct = (id, data) =>
  api.patch(`/products/${id}`, data)

export const deleteProduct = (id) =>
  api.delete(`/products/${id}`)
