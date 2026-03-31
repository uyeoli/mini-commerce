import { useState, useEffect, useRef } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getProduct, modifyProduct } from '../api/productApi'
import { uploadImage } from '../api/fileApi'
import { CATEGORY_LABELS, CATEGORIES } from '../constants'

export default function ProductEditPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [form, setForm] = useState(null)
  const [imageFile, setImageFile] = useState(null)
  const [imagePreview, setImagePreview] = useState(null)
  const [uploading, setUploading] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const fileInputRef = useRef(null)

  useEffect(() => {
    getProduct(id)
      .then((res) => {
        const p = res.data
        setForm({
          productName: p.productName ?? '',
          productInformation: p.productInformation ?? '',
          price: p.price ?? '',
          stock: p.stock ?? '',
          category: p.category ?? '',
        })
        if (p.imageUrl) setImagePreview(p.imageUrl)
      })
      .catch(() => navigate('/products'))
  }, [id, navigate])

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }))
    setError('')
  }

  const handleImageChange = (e) => {
    const file = e.target.files[0]
    if (!file) return
    if (!file.type.startsWith('image/')) {
      setError('이미지 파일만 업로드 가능합니다.')
      return
    }
    if (file.size > 5 * 1024 * 1024) {
      setError('파일 크기는 5MB 이하여야 합니다.')
      return
    }
    setImageFile(file)
    setImagePreview(URL.createObjectURL(file))
    setError('')
  }

  const handleRemoveImage = () => {
    setImageFile(null)
    setImagePreview(null)
    if (fileInputRef.current) fileInputRef.current.value = ''
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    const { productName, price, stock, category } = form
    if (!productName || !price || !stock || !category) {
      setError('상품명, 가격, 재고, 카테고리는 필수 항목입니다.')
      return
    }
    setLoading(true)
    try {
      let imageUrl = imagePreview // 기존 URL 유지 (새 파일 없을 경우)

      if (imageFile) {
        setUploading(true)
        const uploadRes = await uploadImage(imageFile)
        imageUrl = uploadRes.data.imageUrl
        setUploading(false)
      }

      await modifyProduct(id, {
        ...form,
        price: Number(form.price),
        stock: Number(form.stock),
        imageUrl: imagePreview === null ? null : imageUrl, // 이미지 삭제 시 null
      })
      alert('상품이 수정되었습니다.')
      navigate(`/products/${id}`)
    } catch (err) {
      setUploading(false)
      setError(err.response?.data?.message ?? '상품 수정 실패')
    } finally {
      setLoading(false)
    }
  }

  if (!form) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <div className="animate-spin w-8 h-8 border-4 border-indigo-600 border-t-transparent rounded-full" />
      </div>
    )
  }

  return (
    <div className="max-w-xl mx-auto px-4 py-8">
      <button
        onClick={() => navigate(-1)}
        className="flex items-center gap-1.5 text-sm text-gray-500 hover:text-indigo-600 mb-6 transition-colors"
      >
        ← 뒤로가기
      </button>

      <h1 className="text-2xl font-bold text-gray-900 mb-6">상품 수정</h1>

      <div className="card p-6">
        <form onSubmit={handleSubmit} className="space-y-5">

          {/* 이미지 */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">
              상품 이미지 <span className="text-gray-400 font-normal">(jpg, png, gif · 최대 5MB)</span>
            </label>

            {imagePreview ? (
              <div className="relative w-full aspect-video rounded-xl overflow-hidden bg-gray-100 border border-gray-200">
                <img
                  src={imagePreview.startsWith('blob:') ? imagePreview : `/api${imagePreview}`}
                  alt="미리보기"
                  className="w-full h-full object-contain"
                />
                <button
                  type="button"
                  onClick={handleRemoveImage}
                  className="absolute top-2 right-2 w-7 h-7 bg-black/50 hover:bg-black/70 text-white rounded-full flex items-center justify-center text-sm transition-colors"
                >
                  ✕
                </button>
              </div>
            ) : (
              <div
                onClick={() => fileInputRef.current?.click()}
                className="w-full aspect-video rounded-xl border-2 border-dashed border-gray-300 hover:border-indigo-400 flex flex-col items-center justify-center cursor-pointer transition-colors bg-gray-50 hover:bg-indigo-50"
              >
                <p className="text-3xl mb-2">🖼️</p>
                <p className="text-sm font-medium text-gray-500">클릭하여 이미지 업로드</p>
                <p className="text-xs text-gray-400 mt-1">이미지 파일만 가능</p>
              </div>
            )}
            <input
              ref={fileInputRef}
              type="file"
              accept="image/*"
              onChange={handleImageChange}
              className="hidden"
            />
            {imagePreview && (
              <button
                type="button"
                onClick={() => fileInputRef.current?.click()}
                className="mt-2 text-xs text-indigo-600 hover:underline"
              >
                이미지 변경
              </button>
            )}
          </div>

          {/* 카테고리 */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">
              카테고리 <span className="text-red-500">*</span>
            </label>
            <select name="category" value={form.category} onChange={handleChange} className="input-field">
              <option value="">카테고리 선택</option>
              {CATEGORIES.map((cat) => (
                <option key={cat} value={cat}>{CATEGORY_LABELS[cat]}</option>
              ))}
            </select>
          </div>

          {/* 상품명 */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">
              상품명 <span className="text-red-500">*</span>
            </label>
            <input
              name="productName"
              type="text"
              value={form.productName}
              onChange={handleChange}
              className="input-field"
            />
          </div>

          {/* 상품 설명 */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">상품 설명</label>
            <textarea
              name="productInformation"
              value={form.productInformation}
              onChange={handleChange}
              rows={4}
              className="input-field resize-none"
            />
          </div>

          {/* 가격 / 재고 */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1.5">
                가격 (원) <span className="text-red-500">*</span>
              </label>
              <input
                name="price"
                type="number"
                min="0"
                value={form.price}
                onChange={handleChange}
                className="input-field"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1.5">
                재고 (개) <span className="text-red-500">*</span>
              </label>
              <input
                name="stock"
                type="number"
                min="0"
                value={form.stock}
                onChange={handleChange}
                className="input-field"
              />
            </div>
          </div>

          {error && (
            <div className="bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg px-4 py-3">
              {error}
            </div>
          )}

          <div className="flex gap-3 pt-2">
            <button type="submit" disabled={loading || uploading} className="btn-primary">
              {uploading ? '이미지 업로드 중...' : loading ? '저장 중...' : '수정 완료'}
            </button>
            <button type="button" onClick={() => navigate(-1)} className="btn-secondary">
              취소
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
