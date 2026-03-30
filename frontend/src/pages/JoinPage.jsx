import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { join } from '../api/authApi'

export default function JoinPage() {
  const navigate = useNavigate()
  const [form, setForm] = useState({ loginId: '', password: '', name: '', email: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }))
    setError('')
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    const { loginId, password, name, email } = form
    if (!loginId || !password || !name || !email) {
      setError('모든 항목을 입력해주세요.')
      return
    }
    setLoading(true)
    try {
      await join(form)
      alert('회원가입이 완료되었습니다. 로그인해주세요.')
      navigate('/login')
    } catch (err) {
      setError(err.response?.data?.message ?? '회원가입에 실패했습니다.')
    } finally {
      setLoading(false)
    }
  }

  const fields = [
    { name: 'loginId', label: '아이디', type: 'text', placeholder: '아이디를 입력하세요' },
    { name: 'password', label: '비밀번호', type: 'password', placeholder: '비밀번호를 입력하세요' },
    { name: 'name', label: '이름', type: 'text', placeholder: '이름을 입력하세요' },
    { name: 'email', label: '이메일', type: 'email', placeholder: 'example@email.com' },
  ]

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-indigo-50 via-white to-purple-50 px-4">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-indigo-600 mb-1">🛒 Mini Commerce</h1>
          <p className="text-gray-500 text-sm">새 계정을 만들어보세요</p>
        </div>

        <div className="card p-8">
          <h2 className="text-xl font-bold text-gray-900 mb-6">회원가입</h2>

          <form onSubmit={handleSubmit} className="space-y-4">
            {fields.map((field) => (
              <div key={field.name}>
                <label className="block text-sm font-medium text-gray-700 mb-1.5">
                  {field.label}
                </label>
                <input
                  name={field.name}
                  type={field.type}
                  value={form[field.name]}
                  onChange={handleChange}
                  placeholder={field.placeholder}
                  className="input-field"
                />
              </div>
            ))}

            {error && (
              <div className="bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg px-4 py-3">
                {error}
              </div>
            )}

            <button type="submit" disabled={loading} className="btn-primary mt-2">
              {loading ? '처리 중...' : '회원가입'}
            </button>
          </form>

          <div className="mt-5 text-center">
            <p className="text-sm text-gray-500">
              이미 계정이 있으신가요?{' '}
              <Link to="/login" className="text-indigo-600 font-semibold hover:underline">
                로그인
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}
