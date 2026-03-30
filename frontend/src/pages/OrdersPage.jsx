import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { getMemberOrders } from '../api/orderApi'
import { useAuth } from '../contexts/AuthContext'
import { ORDER_STATUS_LABELS, ORDER_STATUS_COLORS } from '../constants'

export default function OrdersPage() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    getMemberOrders(user.memberId)
      .then((res) => setOrders(res.data ?? []))
      .catch(() => setOrders([]))
      .finally(() => setLoading(false))
  }, [user.memberId])

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <div className="animate-spin w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full" />
      </div>
    )
  }

  return (
    <div className="max-w-3xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-900 mb-6">주문 내역</h1>

      {orders.length === 0 ? (
        <div className="card p-12 text-center text-gray-400">
          <p className="text-5xl mb-4">📦</p>
          <p className="font-medium text-base mb-4">주문 내역이 없습니다.</p>
          <button
            onClick={() => navigate('/products')}
            className="text-indigo-600 font-semibold hover:underline text-sm"
          >
            쇼핑 시작하기
          </button>
        </div>
      ) : (
        <div className="space-y-3">
          {orders.map((order) => (
            <div
              key={order.orderId}
              className="card p-5 cursor-pointer hover:shadow-md transition-shadow"
              onClick={() => navigate(`/orders/${order.orderId}`)}
            >
              <div className="flex items-center justify-between">
                <div>
                  <div className="flex items-center gap-2 mb-1.5">
                    <span className="text-sm font-semibold text-gray-900">
                      주문번호 #{order.orderId}
                    </span>
                    <span className={`badge ${ORDER_STATUS_COLORS[order.status] ?? 'bg-gray-100 text-gray-700'}`}>
                      {ORDER_STATUS_LABELS[order.status] ?? order.status}
                    </span>
                  </div>
                  <p className="text-xs text-gray-400">
                    {order.createdAt ? new Date(order.createdAt).toLocaleDateString('ko-KR', {
                      year: 'numeric',
                      month: 'long',
                      day: 'numeric',
                      hour: '2-digit',
                      minute: '2-digit',
                    }) : ''}
                  </p>
                </div>
                <div className="text-right">
                  <p className="font-bold text-indigo-600 text-lg">{order.totalPrice?.toLocaleString()}원</p>
                  <p className="text-xs text-gray-400 mt-0.5">자세히 보기 →</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
