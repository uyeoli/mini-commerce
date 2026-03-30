export const CATEGORY_LABELS = {
  CLOTHING: '의류',
  SHOES: '신발',
  BAGS: '가방',
  ACCESSORIES: '액세서리',
  SPORTSWEAR: '스포츠웨어',
  OUTER: '아우터',
}

export const CATEGORIES = Object.keys(CATEGORY_LABELS)

export const ORDER_STATUS_LABELS = {
  ORDER_COMPLETE: '주문완료',
  PAYMENT_COMPLETE: '결제완료',
  SHIPPING: '배송중',
  DELIVERY_COMPLETE: '배송완료',
}

export const ORDER_STATUS_COLORS = {
  ORDER_COMPLETE: 'bg-yellow-50 text-yellow-700',
  PAYMENT_COMPLETE: 'bg-blue-50 text-blue-700',
  SHIPPING: 'bg-indigo-50 text-indigo-700',
  DELIVERY_COMPLETE: 'bg-green-50 text-green-700',
}
