import axios from 'axios'

const instance = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
})

instance.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

let isRefreshing = false
let pendingQueue = []

const processPendingQueue = (error, token = null) => {
  pendingQueue.forEach(({ resolve, reject }) => {
    if (error) reject(error)
    else resolve(token)
  })
  pendingQueue = []
}

instance.interceptors.response.use(
  (res) => res,
  async (error) => {
    const originalRequest = error.config

    if (error.response?.status === 401 && !originalRequest._retry) {
      const refreshToken = localStorage.getItem('refreshToken')

      if (!refreshToken) {
        localStorage.clear()
        window.location.href = '/login'
        return Promise.reject(error)
      }

      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          pendingQueue.push({ resolve, reject })
        }).then((token) => {
          originalRequest.headers.Authorization = `Bearer ${token}`
          return instance(originalRequest)
        })
      }

      originalRequest._retry = true
      isRefreshing = true

      try {
        const { data } = await axios.post('/api/auth/refresh', { refreshToken })
        const newAccessToken = data.accessToken

        localStorage.setItem('accessToken', newAccessToken)
        instance.defaults.headers.common.Authorization = `Bearer ${newAccessToken}`
        processPendingQueue(null, newAccessToken)

        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`
        return instance(originalRequest)
      } catch (refreshError) {
        processPendingQueue(refreshError, null)
        localStorage.clear()
        window.location.href = '/login'
        return Promise.reject(refreshError)
      } finally {
        isRefreshing = false
      }
    }

    return Promise.reject(error)
  },
)

export default instance
