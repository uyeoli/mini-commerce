import api from './axios'

export const login = (loginId, password) =>
  api.post('/users/login', { loginId, password })

export const join = (data) =>
  api.post('/users', data)

export const updateMember = (id, data) =>
  api.patch(`/users/${id}`, data)
