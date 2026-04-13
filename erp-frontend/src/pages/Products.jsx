import { useEffect, useState } from 'react'
import { Plus, Search, Pencil, Trash2, Package, AlertTriangle } from 'lucide-react'
import { productsApi, suppliersApi } from '../api'
import Modal from '../components/Modal'
import Badge from '../components/Badge'

const EMPTY = { name:'', sku:'', category:'', description:'', unitPrice:'', costPrice:'', stockQty:0, reorderLevel:10, unit:'pcs', supplierId:'', status:'ACTIVE' }

export default function Products() {
  const [rows, setRows]         = useState([])
  const [suppliers, setSuppliers] = useState([])
  const [loading, setLoading]   = useState(true)
  const [search, setSearch]     = useState('')
  const [filterLow, setFilterLow] = useState(false)
  const [modal, setModal]       = useState(null)
  const [form, setForm]         = useState(EMPTY)
  const [editing, setEditing]   = useState(null)
  const [error, setError]       = useState('')
  const [saving, setSaving]     = useState(false)

  const load = () => Promise.all([productsApi.getAll(), suppliersApi.getAll()])
    .then(([p, s]) => { setRows(p.data); setSuppliers(s.data) })
    .finally(() => setLoading(false))

  useEffect(() => { load() }, [])

  const filtered = rows.filter(r => {
    const match = r.name.toLowerCase().includes(search.toLowerCase()) || r.sku.toLowerCase().includes(search.toLowerCase())
    return match && (!filterLow || r.lowStock)
  })

  const openAdd  = () => { setForm(EMPTY); setEditing(null); setError(''); setModal('form') }
  const openEdit = (row) => {
    setForm({...row, supplierId: row.supplierId || '', unitPrice: row.unitPrice, costPrice: row.costPrice})
    setEditing(row.id); setError(''); setModal('form')
  }
  const closeModal = () => setModal(null)

  const save = async () => {
    setSaving(true); setError('')
    try {
      const payload = {...form, supplierId: form.supplierId || null}
      if (editing) await productsApi.update(editing, payload)
      else         await productsApi.create(payload)
      await load(); closeModal()
    } catch(e) { setError(e.response?.data?.message || 'Error') }
    finally { setSaving(false) }
  }

  const del = async (id) => { if (!confirm('Delete product?')) return; await productsApi.delete(id); load() }
  const f = (k) => (e) => setForm(p => ({...p, [k]: e.target.value}))
  const fmt = n => `$${Number(n).toLocaleString('en-US', {minimumFractionDigits:2})}`
  const lowCount = rows.filter(r => r.lowStock).length

  if (loading) return <div className="loading"><div className="spinner"/>Loading...</div>

  return (
    <div className="page-enter">
      <div className="table-card">
        <div className="table-header">
          <span className="table-header-title">Products <span style={{color:'var(--text3)',fontWeight:400}}>({filtered.length})</span></span>
          <div className="search-wrap">
            <Search/><input className="search-input" placeholder="Name or SKU..." value={search} onChange={e=>setSearch(e.target.value)} type="text"/>
          </div>
          {lowCount > 0 && (
            <button className={`btn btn-sm ${filterLow ? 'btn-primary' : 'btn-ghost'}`} onClick={()=>setFilterLow(p=>!p)}>
              <AlertTriangle size={13}/>{lowCount} Low Stock
            </button>
          )}
          <button className="btn btn-primary" onClick={openAdd}><Plus/>Add Product</button>
        </div>
        <div className="table-wrap">
          <table>
            <thead><tr>
              <th>Name</th><th>SKU</th><th>Category</th><th>Unit Price</th><th>Cost</th><th>Stock</th><th>Status</th><th></th>
            </tr></thead>
            <tbody>
              {filtered.length === 0
                ? <tr><td colSpan={8}><div className="empty-state"><Package/><p>No products found</p></div></td></tr>
                : filtered.map(r => (
                  <tr key={r.id}>
                    <td className="td-primary">
                      {r.name}
                      {r.lowStock && <span className="low-stock-badge" style={{marginLeft:8}}><AlertTriangle size={10}/> Low</span>}
                    </td>
                    <td className="td-mono">{r.sku}</td>
                    <td>{r.category}</td>
                    <td className="td-mono">{fmt(r.unitPrice)}</td>
                    <td className="td-mono" style={{color:'var(--text3)'}}>{fmt(r.costPrice)}</td>
                    <td className="td-mono" style={{color: r.lowStock ? 'var(--red)' : 'var(--green)'}}>
                      {r.stockQty} {r.unit}
                    </td>
                    <td><Badge status={r.status}/></td>
                    <td>
                      <div className="flex gap-8">
                        <button className="btn btn-ghost btn-sm" onClick={()=>openEdit(r)}><Pencil/></button>
                        <button className="btn btn-danger btn-sm" onClick={()=>del(r.id)}><Trash2/></button>
                      </div>
                    </td>
                  </tr>
                ))
              }
            </tbody>
          </table>
        </div>
      </div>

      {modal === 'form' && (
        <Modal title={editing ? 'Edit Product' : 'New Product'} onClose={closeModal}
          footer={<>
            <button className="btn btn-ghost" onClick={closeModal}>Cancel</button>
            <button className="btn btn-primary" onClick={save} disabled={saving}>{saving?'Saving...':'Save'}</button>
          </>}>
          {error && <div className="alert alert-error">{error}</div>}
          <div className="form-grid">
            <div className="form-field span2">
              <label className="form-label">Name *</label>
              <input type="text" value={form.name} onChange={f('name')} />
            </div>
            <div className="form-field">
              <label className="form-label">SKU *</label>
              <input type="text" value={form.sku} onChange={f('sku')} />
            </div>
            <div className="form-field">
              <label className="form-label">Category</label>
              <input type="text" value={form.category||''} onChange={f('category')} />
            </div>
            <div className="form-field">
              <label className="form-label">Unit Price *</label>
              <input type="number" step="0.01" value={form.unitPrice} onChange={f('unitPrice')} />
            </div>
            <div className="form-field">
              <label className="form-label">Cost Price *</label>
              <input type="number" step="0.01" value={form.costPrice} onChange={f('costPrice')} />
            </div>
            <div className="form-field">
              <label className="form-label">Stock Qty</label>
              <input type="number" value={form.stockQty} onChange={f('stockQty')} />
            </div>
            <div className="form-field">
              <label className="form-label">Reorder Level</label>
              <input type="number" value={form.reorderLevel} onChange={f('reorderLevel')} />
            </div>
            <div className="form-field">
              <label className="form-label">Unit</label>
              <input type="text" value={form.unit} onChange={f('unit')} />
            </div>
            <div className="form-field">
              <label className="form-label">Status</label>
              <select value={form.status} onChange={f('status')}>
                <option>ACTIVE</option><option>DISCONTINUED</option>
              </select>
            </div>
            <div className="form-field span2">
              <label className="form-label">Supplier</label>
              <select value={form.supplierId||''} onChange={f('supplierId')}>
                <option value="">— None —</option>
                {suppliers.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
              </select>
            </div>
          </div>
        </Modal>
      )}
    </div>
  )
}
