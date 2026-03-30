import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getOrderDetail } from '../api/orderApi'
import { ORDER_STATUS_LABELS, ORDER_STATUS_COLORS } from '../constants'

export default function OrderDetailPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [order, setOrder] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    getOrderDetail(id)
      .then((res) => setOrder(res.data))
      .catch(() => navigate('/orders'))
      .finally(() => setLoading(false))
  }, [id, navigate])

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <div className="animate-spin w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full" />
      </div>
    )
  }

  if (!order) return null

  return (
    <div className="max-w-2xl mx-auto px-4 py-8">
      <button
        onClick={() => navigate('/orders')}
        className="flex items-center gap-1.5 text-sm text-gray-500 hover:text-indigo-600 mb-6 transition-colors"
      >
        ← 주문 목록으로
      </button>

      <h1 className="text-2xl font-bold text-gray-900 mb-6">주문 상세</h1>

      <div className="space-y-4">
        {/* 주문 정보 */}
        <div className="card p-6">
          <h2 className="font-bold text-gray-900 mb-4">주문 정보</h2>
          <div className="space-y-3 text-sm">
            <InfoRow label="주문번호" value={`#${order.orderId}`} />
            <InfoRow
              label="주문 상태"
              value={
                <span className={`badge ${ORDER_STATUS_COLORS[order.status] ?? 'bg-gray-100 text-gray-700'}`}>
                  {ORDER_STATUS_LABELS[order.status] ?? order.status}
                </span>
              }
            />
            <InfoRow label="주문자" value={order.memberName} />
            <InfoRow label="배송지" value={order.deliveryAddress} />
            <InfoRow
              label="주문일시"
              value={
                order.createdAt
                  ? new Date(order.createdAt).toLocaleDateString('ko-KR', {
                      year: 'numeric',
                      month: 'long',
                      day: 'numeric',
                      hour: '2-digit',
                      minute: '2-digit',
                    })
                  : ''
              }
            />
          </div>
        </div>

        {/* 주문 상품 */}
        <div className="card p-6">
          <h2 className="font-bold text-gray-900 mb-4">주문 상품</h2>
          <div className="divide-y divide-gray-100">
            {(order.items ?? []).map((item) => (
              <div key={item.productId} className="py-3 flex items-center justify-between gap-4">
                <div>
                  <p className="font-medium text-gray-900 text-sm">{item.productName}</p>
                  <p className="text-xs text-gray-400 mt-0.5">
                    {item.price?.toLocaleString()}원 × {item.quantity}개
                  </p>
                </div>
                <p className="font-bold text-gray-900 text-sm flex-shrink-0">
                  {item.subtotal?.toLocaleString()}원
                </p>
              </div>
            ))}
          </div>
        </div>

        {/* 결제 금액 */}
        <div className="card p-5">
          <div className="flex justify-between items-center">
            <span className="font-semibold text-gray-700">총 결제금액</span>
            <span className="text-2xl font-bold text-indigo-600">
              {order.totalPrice?.toLocaleString()}원
            </span>
          </div>
        </div>
      </div>
    </div>
  )
}

function InfoRow({ label, value }) {
  return (
    <div className="flex items-start gap-4">
      <span className="text-gray-500 w-20 flex-shrink-0">{label}</span>
      <span className="font-medium text-gray-900">{value}</span>
    </div>
  )
}
