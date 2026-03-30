import { useState } from 'react'
import { useAuth } from '../contexts/AuthContext'
import { updateMember } from '../api/authApi'

export default function ProfilePage() {
  const { user, signIn } = useAuth()
  const [form, setForm] = useState({
    name: user?.name ?? '',
    email: user?.email ?? '',
    password: '',
  })
  const [loading, setLoading] = useState(false)
  const [message, setMessage] = useState(null)

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }))
    setMessage(null)
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!form.name || !form.email) {
      setMessage({ type: 'error', text: '이름과 이메일을 입력해주세요.' })
      return
    }
    setLoading(true)
    try {
      const payload = { name: form.name, email: form.email }
      if (form.password) payload.password = form.password

      await updateMember(user.memberId, payload)

      // 로컬 사용자 정보 업데이트 (토큰은 유지)
      const token = localStorage.getItem('accessToken')
      const refreshToken = localStorage.getItem('refreshToken')
      signIn({
        ...user,
        name: form.name,
        email: form.email,
        accessToken: token,
        refreshToken,
      })

      setMessage({ type: 'success', text: '프로필이 업데이트되었습니다.' })
      setForm((prev) => ({ ...prev, password: '' }))
    } catch (err) {
      setMessage({ type: 'error', text: err.response?.data?.message ?? '업데이트 실패' })
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="max-w-lg mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold text-gray-900 mb-6">내 프로필</h1>

      <div className="card p-6">
        {/* 아이디 (수정 불가) */}
        <div className="mb-6 pb-6 border-b border-gray-100">
          <p className="text-sm text-gray-500 mb-1">아이디</p>
          <p className="font-semibold text-gray-900">{user?.loginId}</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">이름</label>
            <input
              name="name"
              type="text"
              value={form.name}
              onChange={handleChange}
              className="input-field"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">이메일</label>
            <input
              name="email"
              type="email"
              value={form.email}
              onChange={handleChange}
              className="input-field"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">
              새 비밀번호{' '}
              <span className="text-gray-400 font-normal">(변경 시에만 입력)</span>
            </label>
            <input
              name="password"
              type="password"
              value={form.password}
              onChange={handleChange}
              placeholder="변경할 비밀번호"
              className="input-field"
            />
          </div>

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

          <button type="submit" disabled={loading} className="btn-primary mt-2">
            {loading ? '저장 중...' : '저장하기'}
          </button>
        </form>
      </div>
    </div>
  )
}
