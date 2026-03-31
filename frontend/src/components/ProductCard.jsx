import { useNavigate } from 'react-router-dom'
import { CATEGORY_LABELS } from '../constants'

export default function ProductCard({ product }) {
  const navigate = useNavigate()

  return (
    <div
      className="card p-4 cursor-pointer hover:shadow-md transition-shadow duration-200"
      onClick={() => navigate(`/products/${product.id}`)}
    >
      <div className="aspect-square bg-gradient-to-br from-indigo-50 to-purple-50 rounded-lg mb-3 overflow-hidden flex items-center justify-center text-4xl">
        {product.imageUrl ? (
          <img
            src={`/api${product.imageUrl}`}
            alt={product.productName}
            className="w-full h-full object-cover"
          />
        ) : (
          getCategoryEmoji(product.category)
        )}
      </div>

      <div className="space-y-1.5">
        <div className="flex items-center justify-between">
          <span className="badge bg-indigo-50 text-indigo-700">
            {CATEGORY_LABELS[product.category] ?? product.category}
          </span>
          {product.stockShortage && (
            <span className="badge bg-red-50 text-red-600">품절임박</span>
          )}
        </div>
        <h3 className="font-semibold text-gray-900 text-sm leading-snug line-clamp-2">
          {product.productName}
        </h3>
        <p className="text-indigo-600 font-bold text-base">
          {product.price?.toLocaleString()}원
        </p>
        <p className="text-xs text-gray-400">재고 {product.stock}개</p>
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
