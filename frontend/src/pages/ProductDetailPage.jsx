import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getProduct } from '../api/productApi'
import { addToCart } from '../api/cartApi'
import { useAuth } from '../contexts/AuthContext'
import { CATEGORY_LABELS } from '../constants'

export default function ProductDetailPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const { user } = useAuth()
  const [product, setProduct] = useState(null)
  const [quantity, setQuantity] = useState(1)
  const [loading, setLoading] = useState(true)
  const [adding, setAdding] = useState(false)
  const [message, setMessage] = useState(null)

  useEffect(() => {
    getProduct(id)
      .then((res) => setProduct(res.data))
      .catch(() => navigate('/products'))
      .finally(() => setLoading(false))
  }, [id, navigate])

  const handleAddToCart = async () => {
    setAdding(true)
    setMessage(null)
    try {
      await addToCart(user.memberId, product.id, quantity)
      setMessage({ type: 'success', text: '장바구니에 추가되었습니다.' })
    } catch (err) {
      setMessage({ type: 'error', text: err.response?.data?.message ?? '장바구니 추가 실패' })
    } finally {
      setAdding(false)
    }
  }

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <div className="animate-spin w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full" />
      </div>
    )
  }

  if (!product) return null

  return (
    <div className="max-w-3xl mx-auto px-4 py-8">
      <button
        onClick={() => navigate(-1)}
        className="flex items-center gap-1.5 text-sm text-gray-500 hover:text-indigo-600 mb-6 transition-colors"
      >
        ← 뒤로가기
      </button>

      <div className="card p-6 md:p-8">
        <div className="grid md:grid-cols-2 gap-8">
          {/* 이미지 영역 */}
          <div className="aspect-square bg-gradient-to-br from-indigo-50 to-purple-50 rounded-xl flex items-center justify-center text-8xl">
            {getCategoryEmoji(product.category)}
          </div>

          {/* 상품 정보 */}
          <div className="flex flex-col justify-between">
            <div className="space-y-3">
              <span className="badge bg-indigo-50 text-indigo-700 text-sm">
                {CATEGORY_LABELS[product.category] ?? product.category}
              </span>

              <h1 className="text-2xl font-bold text-gray-900">{product.productName}</h1>

              <p className="text-gray-500 text-sm leading-relaxed">{product.productInformation}</p>

              <div className="pt-2">
                <p className="text-3xl font-bold text-indigo-600">
                  {product.price?.toLocaleString()}원
                </p>
                <p className="text-sm text-gray-400 mt-1">
                  재고{' '}
                  <span className={product.stockShortage ? 'text-red-500 font-semibold' : 'text-gray-600'}>
                    {product.stock}개
                  </span>
                  {product.stockShortage && (
                    <span className="ml-2 badge bg-red-50 text-red-600">품절임박</span>
                  )}
                </p>
              </div>
            </div>

            <div className="mt-6 space-y-4">
              {/* 수량 선택 */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">수량</label>
                <div className="flex items-center gap-3">
                  <button
                    onClick={() => setQuantity((q) => Math.max(1, q - 1))}
                    className="w-9 h-9 rounded-lg border border-gray-300 flex items-center justify-center hover:bg-gray-50 font-semibold text-lg"
                  >
                    -
                  </button>
                  <span className="w-10 text-center font-semibold text-gray-900">{quantity}</span>
                  <button
                    onClick={() => setQuantity((q) => Math.min(product.stock, q + 1))}
                    className="w-9 h-9 rounded-lg border border-gray-300 flex items-center justify-center hover:bg-gray-50 font-semibold text-lg"
                  >
                    +
                  </button>
                </div>
              </div>

              <p className="text-sm text-gray-500">
                합계:{' '}
                <span className="font-bold text-gray-900">
                  {(product.price * quantity).toLocaleString()}원
                </span>
              </p>

              {message && (
                <div
                  className={`text-sm rounded-lg px-4 py-3 ${
                    message.type === 'success'
                      ? 'bg-green-50 text-green-700 border border-green-200'
                      : 'bg-red-50 text-red-700 border border-red-200'
                  }`}
                >
                  {message.text}
                </div>
              )}

              <button
                onClick={handleAddToCart}
                disabled={adding || product.stock === 0}
                className="btn-primary"
              >
                {adding ? '추가 중...' : product.stock === 0 ? '품절' : '장바구니 담기'}
              </button>

              <button
                onClick={() => navigate('/cart')}
                className="btn-secondary"
              >
                장바구니 보기
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

function getCategoryEmoji(category) {
  const map = {
    CLOTHING: '👕',
    SHOES: '👟',
    BAGS: '👜',
    ACCESSORIES: '💍',
    SPORTSWEAR: '🏃',
    OUTER: '🧥',
  }
  return map[category] ?? '📦'
}
