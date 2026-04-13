import { useEffect, useState } from 'react'
import { Plus, Search, Pencil, Trash2, UserCircle } from 'lucide-react'
import { customersApi } from '../api'
import Modal from '../components/Modal'
import Badge from '../components/Badge'

const EMPTY = { name:'', email:'', phone:'', address:'', city:'', country:'', status:'ACTIVE' }

export default function Customers() {
  const [rows, setRows]       = useState([])
  const [loading, setLoading] = useState(true)
  const [search, setSearch]   = useState('')
  const [modal, setModal]     = useState(null) // null | 'add' | 'edit'
  const [form, setForm]       = useState(EMPTY)
  const [editing, setEditing] = useState(null)
  const [error, setError]     = useState('')
  const [saving, setSaving]   = useState(false)

  const load = () => customersApi.getAll().then(r => setRows(r.data)).finally(() => setLoading(false))
  useEffect(() => { load() }, [])

  const filtered = rows.filter(r =>
    r.name.toLowerCase().includes(search.toLowerCase()) ||
    r.email.toLowerCase().includes(search.toLowerCase()) ||
    (r.city || '').toLowerCase().includes(search.toLowerCase())
  )

  const openAdd  = () => { setForm(EMPTY); setEditing(null); setError(''); setModal('form') }
  const openEdit = (row) => { setForm({...row}); setEditing(row.id); setError(''); setModal('form') }
  const closeModal = () => setModal(null)

  const save = async () => {
    setSaving(true); setError('')
    try {
      if (editing) await customersApi.update(editing, form)
      else         await customersApi.create(form)
      await load(); closeModal()
    } catch(e) {
      setError(e.response?.data?.message || 'An error occurred')
    } finally { setSaving(false) }
  }

  const del = async (id) => {
    if (!confirm('Delete this customer?')) return
    await customersApi.delete(id); load()
  }

  const f = (k) => (e) => setForm(p => ({...p, [k]: e.target.value}))

  if (loading) return <div className="loading"><div className="spinner"/>Loading...</div>

  return (
    <div className="page-enter">
      <div className="table-card">
        <div className="table-header">
          <span className="table-header-title">Customers <span style={{color:'var(--text3)',fontWeight:400}}>({filtered.length})</span></span>
          <div className="search-wrap">
            <Search /><input className="search-input" placeholder="Search..." value={search} onChange={e=>setSearch(e.target.value)} type="text"/>
          </div>
          <button className="btn btn-primary" onClick={openAdd}><Plus/>Add Customer</button>
        </div>
        <div className="table-wrap">
          <table>
            <thead><tr>
              <th>Name</th><th>Email</th><th>Phone</th><th>City</th><th>Country</th><th>Status</th><th></th>
            </tr></thead>
            <tbody>
              {filtered.length === 0
                ? <tr><td colSpan={7}><div className="empty-state"><UserCircle/><p>No customers found</p></div></td></tr>
                : filtered.map(r => (
                  <tr key={r.id}>
                    <td className="td-primary">{r.name}</td>
                    <td>{r.email}</td>
                    <td className="td-mono">{r.phone}</td>
                    <td>{r.city}</td>
                    <td>{r.country}</td>
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
        <Modal title={editing ? 'Edit Customer' : 'New Customer'} onClose={closeModal}
          footer={<>
            <button className="btn btn-ghost" onClick={closeModal}>Cancel</button>
            <button className="btn btn-primary" onClick={save} disabled={saving}>{saving ? 'Saving...' : 'Save'}</button>
          </>}>
          {error && <div className="alert alert-error">{error}</div>}
          <div className="form-grid">
            <div className="form-field span2">
              <label className="form-label">Name *</label>
              <input type="text" value={form.name} onChange={f('name')} />
            </div>
            <div className="form-field span2">
              <label className="form-label">Email *</label>
              <input type="email" value={form.email} onChange={f('email')} />
            </div>
            <div className="form-field">
              <label className="form-label">Phone</label>
              <input type="text" value={form.phone||''} onChange={f('phone')} />
            </div>
            <div className="form-field">
              <label className="form-label">Status</label>
              <select value={form.status} onChange={f('status')}>
                <option>ACTIVE</option><option>INACTIVE</option>
              </select>
            </div>
            <div className="form-field">
              <label className="form-label">City</label>
              <input type="text" value={form.city||''} onChange={f('city')} />
            </div>
            <div className="form-field">
              <label className="form-label">Country</label>
              <input type="text" value={form.country||''} onChange={f('country')} />
            </div>
            <div className="form-field span2">
              <label className="form-label">Address</label>
              <input type="text" value={form.address||''} onChange={f('address')} />
            </div>
          </div>
        </Modal>
      )}
    </div>
  )
}
