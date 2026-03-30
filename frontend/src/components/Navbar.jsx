import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'

export default function Navbar() {
  const { user, signOut } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  const handleLogout = () => {
    signOut()
    navigate('/login')
  }

  const isActive = (path) =>
    location.pathname.startsWith(path)
      ? 'text-indigo-600 font-semibold'
      : 'text-gray-600 hover:text-indigo-600'

  return (
    <nav className="fixed top-0 left-0 right-0 z-50 bg-white border-b border-gray-200 shadow-sm">
      <div className="max-w-6xl mx-auto px-4 h-16 flex items-center justify-between">
        <Link to="/products" className="text-xl font-bold text-indigo-600 tracking-tight">
          🛒 Mini Commerce
        </Link>

        <div className="flex items-center gap-6">
          <Link to="/products" className={`text-sm transition-colors ${isActive('/products')}`}>
            상품
          </Link>
          <Link to="/cart" className={`text-sm transition-colors ${isActive('/cart')}`}>
            장바구니
          </Link>
          <Link to="/orders" className={`text-sm transition-colors ${isActive('/orders')}`}>
            주문내역
          </Link>
        </div>

        <div className="flex items-center gap-3">
          <Link
            to="/profile"
            className="text-sm text-gray-600 hover:text-indigo-600 transition-colors"
          >
            {user?.name}님
          </Link>
          <button
            onClick={handleLogout}
            className="text-sm bg-gray-100 text-gray-700 px-3 py-1.5 rounded-lg hover:bg-gray-200 transition-colors"
          >
            로그아웃
          </button>
        </div>
      </div>
    </nav>
  )
}
