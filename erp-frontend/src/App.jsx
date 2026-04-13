import { BrowserRouter, Routes, Route, useLocation } from 'react-router-dom'
import Sidebar from './components/Sidebar'
import Dashboard   from './pages/Dashboard'
import Customers   from './pages/Customers'
import Employees   from './pages/Employees'
import Departments from './pages/Departments'
import Products    from './pages/Products'
import Suppliers   from './pages/Suppliers'
import SalesOrders from './pages/SalesOrders'
import Invoices    from './pages/Invoices'
import Payments    from './pages/Payments'

const titles = {
  '/':            'Dashboard',
  '/customers':   'Customers',
  '/employees':   'Employees',
  '/departments': 'Departments',
  '/products':    'Products',
  '/suppliers':   'Suppliers',
  '/sales-orders':'Sales Orders',
  '/invoices':    'Invoices',
  '/payments':    'Payments',
}

function Layout() {
  const loc = useLocation()
  const title = titles[loc.pathname] || 'ManuERP'

  return (
    <div className="app-shell">
      <Sidebar />
      <div className="main-content">
        <div className="topbar">
          <span className="topbar-title">{title}</span>
        </div>
        <div className="page-body">
          <Routes>
            <Route path="/"            element={<Dashboard />} />
            <Route path="/customers"   element={<Customers />} />
            <Route path="/employees"   element={<Employees />} />
            <Route path="/departments" element={<Departments />} />
            <Route path="/products"    element={<Products />} />
            <Route path="/suppliers"   element={<Suppliers />} />
            <Route path="/sales-orders" element={<SalesOrders />} />
            <Route path="/invoices"    element={<Invoices />} />
            <Route path="/payments"    element={<Payments />} />
          </Routes>
        </div>
      </div>
    </div>
  )
}

export default function App() {
  return (
    <BrowserRouter>
      <Layout />
    </BrowserRouter>
  )
}
