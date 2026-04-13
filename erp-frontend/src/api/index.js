import axios from 'axios'

const api = axios.create({ baseURL: '/api' })

// ── Customers ────────────────────────────────────────
export const customersApi = {
  getAll:  ()         => api.get('/customers'),
  getById: (id)       => api.get(`/customers/${id}`),
  create:  (data)     => api.post('/customers', data),
  update:  (id, data) => api.put(`/customers/${id}`, data),
  delete:  (id)       => api.delete(`/customers/${id}`),
}

// ── Employees ────────────────────────────────────────
export const employeesApi = {
  getAll:        ()         => api.get('/employees'),
  getById:       (id)       => api.get(`/employees/${id}`),
  getByDept:     (deptId)   => api.get(`/employees/department/${deptId}`),
  create:        (data)     => api.post('/employees', data),
  update:        (id, data) => api.put(`/employees/${id}`, data),
  updateStatus:  (id, s)    => api.patch(`/employees/${id}/status`, { status: s }),
  delete:        (id)       => api.delete(`/employees/${id}`),
}

// ── Departments ──────────────────────────────────────
export const departmentsApi = {
  getAll:  ()         => api.get('/departments'),
  create:  (data)     => api.post('/departments', data),
  update:  (id, data) => api.put(`/departments/${id}`, data),
  delete:  (id)       => api.delete(`/departments/${id}`),
}

// ── Products ─────────────────────────────────────────
export const productsApi = {
  getAll:      ()         => api.get('/products'),
  getById:     (id)       => api.get(`/products/${id}`),
  getLowStock: ()         => api.get('/products/low-stock'),
  create:      (data)     => api.post('/products', data),
  update:      (id, data) => api.put(`/products/${id}`, data),
  adjustStock: (id, d)    => api.patch(`/products/${id}/stock`, { delta: d }),
  delete:      (id)       => api.delete(`/products/${id}`),
}

// ── Suppliers ────────────────────────────────────────
export const suppliersApi = {
  getAll:  ()         => api.get('/suppliers'),
  create:  (data)     => api.post('/suppliers', data),
  update:  (id, data) => api.put(`/suppliers/${id}`, data),
  delete:  (id)       => api.delete(`/suppliers/${id}`),
}

// ── Sales Orders ─────────────────────────────────────
export const salesOrdersApi = {
  getAll:        ()         => api.get('/sales-orders'),
  getById:       (id)       => api.get(`/sales-orders/${id}`),
  getByCustomer: (cid)      => api.get(`/sales-orders/customer/${cid}`),
  create:        (data)     => api.post('/sales-orders', data),
  update:        (id, data) => api.put(`/sales-orders/${id}`, data),
  updateStatus:  (id, s)    => api.patch(`/sales-orders/${id}/status`, { status: s }),
  delete:        (id)       => api.delete(`/sales-orders/${id}`),
}

// ── Invoices ─────────────────────────────────────────
export const invoicesApi = {
  getAll:       ()         => api.get('/invoices'),
  getById:      (id)       => api.get(`/invoices/${id}`),
  getByStatus:  (s)        => api.get(`/invoices/status/${s}`),
  create:       (data)     => api.post('/invoices', data),
  updateStatus: (id, s)    => api.patch(`/invoices/${id}/status`, { status: s }),
  delete:       (id)       => api.delete(`/invoices/${id}`),
}

// ── Payments ─────────────────────────────────────────
export const paymentsApi = {
  getAll:       ()     => api.get('/payments'),
  getByInvoice: (iid)  => api.get(`/payments/invoice/${iid}`),
  create:       (data) => api.post('/payments', data),
  delete:       (id)   => api.delete(`/payments/${id}`),
}

export default api
