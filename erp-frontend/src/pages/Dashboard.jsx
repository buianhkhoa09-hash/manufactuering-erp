import { useEffect, useState } from 'react'
import { Users, Package, ShoppingCart, FileText, AlertTriangle, TrendingUp } from 'lucide-react'
import { customersApi, productsApi, salesOrdersApi, invoicesApi, employeesApi } from '../api'
import Badge from '../components/Badge'

export default function Dashboard() {
  const [stats, setStats] = useState(null)
  const [lowStock, setLowStock] = useState([])
  const [recentOrders, setRecentOrders] = useState([])
  const [outstandingInvoices, setOutstandingInvoices] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([
      customersApi.getAll(),
      productsApi.getAll(),
      salesOrdersApi.getAll(),
      invoicesApi.getAll(),
      employeesApi.getAll(),
      productsApi.getLowStock(),
    ]).then(([customers, products, orders, invoices, employees, lowStockRes]) => {
      setStats({
        customers: customers.data.length,
        products:  products.data.length,
        orders:    orders.data.length,
        employees: employees.data.length,
        revenue:   invoices.data
          .filter(i => i.status === 'PAID')
          .reduce((s, i) => s + i.totalAmount, 0),
        outstanding: invoices.data
          .filter(i => !['PAID','CANCELLED'].includes(i.status))
          .reduce((s, i) => s + i.balance, 0),
      })
      setLowStock(lowStockRes.data.slice(0, 6))
      setRecentOrders(orders.data.slice(-5).reverse())
      setOutstandingInvoices(invoices.data.filter(i => !['PAID','CANCELLED'].includes(i.status)).slice(0, 5))
    }).finally(() => setLoading(false))
  }, [])

  if (loading) return <div className="loading"><div className="spinner" /> Loading dashboard...</div>

  const fmt = n => new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD', maximumFractionDigits: 0 }).format(n)

  return (
    <div className="page-enter">
      <div className="stat-grid">
        <StatCard label="Total Customers" value={stats.customers} icon={<Users />} />
        <StatCard label="Products" value={stats.products} icon={<Package />} />
        <StatCard label="Sales Orders" value={stats.orders} icon={<ShoppingCart />} />
        <StatCard label="Employees" value={stats.employees} icon={<Users />} />
        <StatCard label="Revenue Collected" value={fmt(stats.revenue)} icon={<TrendingUp />} accent />
        <StatCard label="Outstanding Balance" value={fmt(stats.outstanding)} icon={<FileText />} warn={stats.outstanding > 0} />
      </div>

      <div className="dash-grid">
        {/* Recent Orders */}
        <div className="table-card">
          <div className="table-header">
            <span className="table-header-title">Recent Orders</span>
          </div>
          <div className="table-wrap">
            <table>
              <thead><tr>
                <th>#</th><th>Customer</th><th>Status</th><th>Total</th>
              </tr></thead>
              <tbody>
                {recentOrders.map(o => (
                  <tr key={o.id}>
                    <td className="td-mono">SO-{String(o.id).padStart(4,'0')}</td>
                    <td className="td-primary">{o.customerName}</td>
                    <td><Badge status={o.status} /></td>
                    <td className="td-mono">{fmt(o.totalAmount)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* Low Stock */}
        <div className="table-card">
          <div className="table-header">
            <span className="table-header-title">Low Stock Alerts</span>
            {lowStock.length > 0 && (
              <span className="low-stock-badge"><AlertTriangle size={11} /> {lowStock.length} items</span>
            )}
          </div>
          <div className="table-wrap">
            <table>
              <thead><tr>
                <th>Product</th><th>SKU</th><th>Stock</th><th>Min</th>
              </tr></thead>
              <tbody>
                {lowStock.length === 0
                  ? <tr><td colSpan={4} style={{textAlign:'center',color:'var(--text3)'}}>All stock levels OK</td></tr>
                  : lowStock.map(p => (
                    <tr key={p.id}>
                      <td className="td-primary">{p.name}</td>
                      <td className="td-mono">{p.sku}</td>
                      <td><span style={{color:'var(--red)',fontFamily:'var(--font-mono)'}}>{p.stockQty}</span></td>
                      <td className="td-mono">{p.reorderLevel}</td>
                    </tr>
                  ))
                }
              </tbody>
            </table>
          </div>
        </div>

        {/* Outstanding Invoices */}
        <div className="table-card" style={{gridColumn:'1/-1'}}>
          <div className="table-header">
            <span className="table-header-title">Outstanding Invoices</span>
          </div>
          <div className="table-wrap">
            <table>
              <thead><tr>
                <th>Invoice</th><th>Customer</th><th>Due</th><th>Total</th><th>Balance</th><th>Status</th>
              </tr></thead>
              <tbody>
                {outstandingInvoices.length === 0
                  ? <tr><td colSpan={6} style={{textAlign:'center',color:'var(--text3)'}}>No outstanding invoices</td></tr>
                  : outstandingInvoices.map(i => (
                    <tr key={i.id}>
                      <td className="td-mono td-primary">{i.invoiceNumber}</td>
                      <td>{i.customerName}</td>
                      <td className="td-mono">{i.dueDate}</td>
                      <td className="td-mono">{fmt(i.totalAmount)}</td>
                      <td className="td-mono" style={{color:'var(--red)'}}>{fmt(i.balance)}</td>
                      <td><Badge status={i.status} /></td>
                    </tr>
                  ))
                }
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  )
}

function StatCard({ label, value, icon, accent, warn }) {
  return (
    <div className="stat-card">
      <div className="stat-label">{label}</div>
      <div className="stat-value" style={accent ? {color:'var(--accent)'} : warn ? {color:'var(--red)'} : {}}>
        {value}
      </div>
      <div className="stat-icon">{icon}</div>
    </div>
  )
}
