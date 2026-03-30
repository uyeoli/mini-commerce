import { useState, useEffect, useCallback } from 'react'
import { searchProducts } from '../api/productApi'
import ProductCard from '../components/ProductCard'
import { CATEGORY_LABELS, CATEGORIES } from '../constants'

export default function ProductListPage() {
  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(true)
  const [searchName, setSearchName] = useState('')
  const [selectedCategory, setSelectedCategory] = useState('')
  const [inputName, setInputName] = useState('')

  const fetchProducts = useCallback(async () => {
    setLoading(true)
    try {
      const condition = {}
      if (selectedCategory) condition.category = selectedCategory
      if (searchName) condition.name = searchName
      const res = await searchProducts(condition)
      setProducts(res.data ?? [])
    } catch {
      setProducts([])
    } finally {
      setLoading(false)
    }
  }, [selectedCategory, searchName])

  useEffect(() => {
    fetchProducts()
  }, [fetchProducts])

  const handleSearch = (e) => {
    e.preventDefault()
    setSearchName(inputName)
  }

  const handleCategoryClick = (cat) => {
    setSelectedCategory((prev) => (prev === cat ? '' : cat))
  }

  return (
    <div className="max-w-6xl mx-auto px-4 py-8">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-900 mb-1">상품 목록</h1>
        <p className="text-sm text-gray-500">원하는 상품을 찾아보세요</p>
      </div>

      {/* 검색 */}
      <form onSubmit={handleSearch} className="flex gap-2 mb-6">
        <input
          type="text"
          value={inputName}
          onChange={(e) => setInputName(e.target.value)}
          placeholder="상품명 검색..."
          className="input-field max-w-xs"
        />
        <button
          type="submit"
          className="bg-indigo-600 text-white px-5 py-2.5 rounded-lg text-sm font-semibold hover:bg-indigo-700 transition-colors"
        >
          검색
        </button>
        {(searchName || selectedCategory) && (
          <button
            type="button"
            onClick={() => { setSearchName(''); setInputName(''); setSelectedCategory('') }}
            className="bg-gray-200 text-gray-700 px-4 py-2.5 rounded-lg text-sm font-semibold hover:bg-gray-300 transition-colors"
          >
            초기화
          </button>
        )}
      </form>

      {/* 카테고리 필터 */}
      <div className="flex flex-wrap gap-2 mb-8">
        {CATEGORIES.map((cat) => (
          <button
            key={cat}
            onClick={() => handleCategoryClick(cat)}
            className={`px-4 py-1.5 rounded-full text-sm font-medium border transition-colors ${
              selectedCategory === cat
                ? 'bg-indigo-600 text-white border-indigo-600'
                : 'bg-white text-gray-600 border-gray-200 hover:border-indigo-400 hover:text-indigo-600'
            }`}
          >
            {CATEGORY_LABELS[cat]}
          </button>
        ))}
      </div>

      {/* 결과 */}
      {loading ? (
        <div className="flex items-center justify-center py-20">
          <div className="animate-spin w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full" />
        </div>
      ) : products.length === 0 ? (
        <div className="text-center py-20 text-gray-400">
          <p className="text-4xl mb-3">📦</p>
          <p className="font-medium">상품이 없습니다.</p>
        </div>
      ) : (
        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4">
          {products.map((product) => (
            <ProductCard key={product.id} product={product} />
          ))}
        </div>
      )}
    </div>
  )
}
