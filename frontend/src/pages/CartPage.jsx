import { useState, useEffect, useCallback } from 'react'
import { useNavigate } from 'react-router-dom'
import { getCart, updateCartItem, removeCartItem } from '../api/cartApi'
import { createOrder } from '../api/orderApi'
import { useAuth } from '../contexts/AuthContext'

export default function CartPage() {
  const { user } = useAuth()
  const navigate = useNavigate()
  const [cart, setCart] = useState(null)
  const [loading, setLoading] = useState(true)
  const [deliveryAddress, setDeliveryAddress] = useState('')
  const [showCheckout, setShowCheckout] = useState(false)
  const [ordering, setOrdering] = useState(false)

  const fetchCart = useCallback(async () => {
    setLoading(true)
    try {
      const res = await getCart(user.memberId)
      setCart(res.data)
    } catch {
      setCart(null)
    } finally {
      setLoading(false)
    }
  }, [user.memberId])

  useEffect(() => {
    fetchCart()
  }, [fetchCart])

  const handleQuantityChange = async (cartItemId, newQty) => {
    if (newQty < 1) return
    try {
      await updateCartItem(cartItemId, newQty)
      await fetchCart()
    } catch (err) {
      alert(err.response?.data?.message ?? '수량 변경 실패')
    }
  }

  const handleRemove = async (cartItemId) => {
    if (!confirm('장바구니에서 삭제하시겠습니까?')) return
    try {
      await removeCartItem(cartItemId)
      await fetchCart()
    } catch {
      alert('삭제 실패')
    }
  }

  const handleOrder = async () => {
    if (!deliveryAddress.trim()) {
      alert('배송지를 입력해주세요.')
      return
    }
    setOrdering(true)
    try {
      const res = await createOrder(user.memberId, deliveryAddress)
      const orderId = res.data
      alert('주문이 완료되었습니다!')
      navigate(`/orders/${orderId}`)
    } catch (err) {
      alert(err.response?.data?.message ?? '주문 실패')
    } finally {
      setOrdering(false)
    }
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <div className="animate-spin w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full" />
      </div>
    )
  }

  const items = cart?.cartItemList ?? []
  const totalPrice = cart?.totalPrice ?? 0

  return (
    <div className="max-w-3xl mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-900 mb-6">장바구니</h1>

      {items.length === 0 ? (
        <div className="card p-12 text-center text-gray-400">
          <p className="text-5xl mb-4">🛒</p>
          <p className="font-medium text-base mb-4">장바구니가 비어있습니다.</p>
          <button
            onClick={() => navigate('/products')}
            className="text-indigo-600 font-semibold hover:underline text-sm"
          >
            쇼핑 계속하기
          </button>
        </div>
      ) : (
        <div className="space-y-4">
          {/* 장바구니 아이템 */}
          <div className="card divide-y divide-gray-100">
            {items.map((item) => (
              <div key={item.cartItemId} className="p-4 flex items-center gap-4">
                <div className="w-14 h-14 bg-gradient-to-br from-indigo-50 to-purple-50 rounded-lg flex items-center justify-center text-2xl flex-shrink-0">
                  📦
                </div>

                <div className="flex-1 min-w-0">
                  <p className="font-semibold text-gray-900 text-sm truncate">{item.productName}</p>
                  <p className="text-indigo-600 font-bold text-sm">{item.price?.toLocaleString()}원</p>
                </div>

                <div className="flex items-center gap-2">
                  <button
                    onClick={() => handleQuantityChange(item.cartItemId, item.quantity - 1)}
                    className="w-8 h-8 rounded-lg border border-gray-300 flex items-center justify-center hover:bg-gray-50 font-semibold"
                  >
                    -
                  </button>
                  <span className="w-8 text-center font-semibold text-sm">{item.quantity}</span>
                  <button
                    onClick={() => handleQuantityChange(item.cartItemId, item.quantity + 1)}
                    className="w-8 h-8 rounded-lg border border-gray-300 flex items-center justify-center hover:bg-gray-50 font-semibold"
                  >
                    +
                  </button>
                </div>

                <div className="text-right min-w-[80px]">
                  <p className="font-bold text-gray-900 text-sm">{item.subtotal?.toLocaleString()}원</p>
                </div>

                <button
                  onClick={() => handleRemove(item.cartItemId)}
                  className="text-gray-300 hover:text-red-400 transition-colors text-lg ml-1"
                >
                  ✕
                </button>
              </div>
            ))}
          </div>

          {/* 총 합계 */}
          <div className="card p-5">
            <div className="flex justify-between items-center">
              <span className="font-semibold text-gray-700">총 결제금액</span>
              <span className="text-2xl font-bold text-indigo-600">{totalPrice.toLocaleString()}원</span>
            </div>
          </div>

          {/* 주문하기 */}
          {showCheckout ? (
            <div className="card p-5 space-y-4">
              <h3 className="font-bold text-gray-900">배송 정보</h3>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1.5">배송지 주소</label>
                <input
                  type="text"
                  value={deliveryAddress}
                  onChange={(e) => setDeliveryAddress(e.target.value)}
                  placeholder="배송받을 주소를 입력하세요"
                  className="input-field"
                />
              </div>
              <div className="flex gap-3">
                <button onClick={handleOrder} disabled={ordering} className="btn-primary">
                  {ordering ? '주문 처리 중...' : '주문 완료'}
                </button>
                <button
                  onClick={() => setShowCheckout(false)}
                  className="btn-secondary"
                >
                  취소
                </button>
              </div>
            </div>
          ) : (
            <button onClick={() => setShowCheckout(true)} className="btn-primary">
              주문하기
            </button>
          )}
        </div>
      )}
    </div>
  )
}
