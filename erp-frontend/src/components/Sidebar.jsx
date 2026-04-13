import { NavLink, useLocation } from 'react-router-dom'
import {
  LayoutDashboard, Users, UserCircle, Package,
  Truck, ShoppingCart, FileText, CreditCard, Building2
} from 'lucide-react'

const sections = [
  {
    label: 'Overview',
    items: [
      { to: '/',          icon: LayoutDashboard, label: 'Dashboard' },
    ]
  },
  {
    label: 'Operations',
    items: [
      { to: '/sales-orders', icon: ShoppingCart, label: 'Sales Orders' },
      { to: '/customers',    icon: UserCircle,   label: 'Customers' },
      { to: '/invoices',     icon: FileText,     label: 'Invoices' },
      { to: '/payments',     icon: CreditCard,   label: 'Payments' },
    ]
  },
  {
    label: 'Inventory',
    items: [
      { to: '/products',  icon: Package, label: 'Products' },
      { to: '/suppliers', icon: Truck,   label: 'Suppliers' },
    ]
  },
  {
    label: 'HR',
    items: [
      { to: '/employees',   icon: Users,     label: 'Employees' },
      { to: '/departments', icon: Building2, label: 'Departments' },
    ]
  },
]

export default function Sidebar() {
  return (
    <aside className="sidebar">
      <div className="sidebar-logo">
        <div className="brand">Manu<span>ERP</span></div>
        <div className="tagline">Manufacturing Suite</div>
      </div>

      {sections.map(sec => (
        <div className="sidebar-section" key={sec.label}>
          <div className="sidebar-section-label">{sec.label}</div>
          {sec.items.map(item => (
            <NavLink
              key={item.to}
              to={item.to}
              end={item.to === '/'}
              className={({ isActive }) => `nav-item${isActive ? ' active' : ''}`}
            >
              <item.icon />
              {item.label}
            </NavLink>
          ))}
        </div>
      ))}

      <div className="sidebar-footer">
        <span className="status-dot" />
        <span className="status-label">API connected · :8080</span>
      </div>
    </aside>
  )
}
