import { useEffect, useState } from 'react'
import { Plus, Search, Pencil, Trash2, Users } from 'lucide-react'
import { employeesApi, departmentsApi } from '../api'
import Modal from '../components/Modal'
import Badge from '../components/Badge'

const EMPTY = { firstName:'', lastName:'', email:'', phone:'', role:'', departmentId:'', hireDate:'', salary:'', status:'ACTIVE' }

export default function Employees() {
  const [rows, setRows]           = useState([])
  const [departments, setDepts]   = useState([])
  const [loading, setLoading]     = useState(true)
  const [search, setSearch]       = useState('')
  const [deptFilter, setDeptFilter] = useState('')
  const [modal, setModal]         = useState(null)
  const [form, setForm]           = useState(EMPTY)
  const [editing, setEditing]     = useState(null)
  const [error, setError]         = useState('')
  const [saving, setSaving]       = useState(false)

  const load = () => Promise.all([employeesApi.getAll(), departmentsApi.getAll()])
    .then(([e, d]) => { setRows(e.data); setDepts(d.data) })
    .finally(() => setLoading(false))

  useEffect(() => { load() }, [])

  const filtered = rows.filter(r => {
    const name = `${r.firstName} ${r.lastName}`.toLowerCase()
    const match = name.includes(search.toLowerCase()) || r.role.toLowerCase().includes(search.toLowerCase())
    const deptMatch = !deptFilter || String(r.departmentId) === deptFilter
    return match && deptMatch
  })

  const openAdd  = () => { setForm(EMPTY); setEditing(null); setError(''); setModal('form') }
  const openEdit = (row) => {
    setForm({...row, departmentId: String(row.departmentId)})
    setEditing(row.id); setError(''); setModal('form')
  }
  const closeModal = () => setModal(null)

  const save = async () => {
    setSaving(true); setError('')
    try {
      const payload = {...form, departmentId: Number(form.departmentId), salary: Number(form.salary)}
      if (editing) await employeesApi.update(editing, payload)
      else         await employeesApi.create(payload)
      await load(); closeModal()
    } catch(e) { setError(e.response?.data?.message || 'Error') }
    finally { setSaving(false) }
  }

  const del = async (id) => { if (!confirm('Delete employee?')) return; await employeesApi.delete(id); load() }
  const f = (k) => (e) => setForm(p => ({...p, [k]: e.target.value}))
  const fmt = n => `$${Number(n).toLocaleString()}`

  if (loading) return <div className="loading"><div className="spinner"/>Loading...</div>

  return (
    <div className="page-enter">
      <div className="table-card">
        <div className="table-header">
          <span className="table-header-title">Employees <span style={{color:'var(--text3)',fontWeight:400}}>({filtered.length})</span></span>
          <div className="search-wrap">
            <Search/><input className="search-input" placeholder="Name or role..." value={search} onChange={e=>setSearch(e.target.value)} type="text"/>
          </div>
          <select style={{width:150}} value={deptFilter} onChange={e=>setDeptFilter(e.target.value)}>
            <option value="">All Departments</option>
            {departments.map(d => <option key={d.id} value={d.id}>{d.name}</option>)}
          </select>
          <button className="btn btn-primary" onClick={openAdd}><Plus/>Add Employee</button>
        </div>
        <div className="table-wrap">
          <table>
            <thead><tr>
              <th>Name</th><th>Role</th><th>Department</th><th>Email</th><th>Salary</th><th>Hired</th><th>Status</th><th></th>
            </tr></thead>
            <tbody>
              {filtered.length === 0
                ? <tr><td colSpan={8}><div className="empty-state"><Users/><p>No employees found</p></div></td></tr>
                : filtered.map(r => (
                  <tr key={r.id}>
                    <td className="td-primary">{r.firstName} {r.lastName}</td>
                    <td>{r.role}</td>
                    <td>{r.departmentName}</td>
                    <td>{r.email}</td>
                    <td className="td-mono">{fmt(r.salary)}</td>
                    <td className="td-mono">{r.hireDate}</td>
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
        <Modal title={editing ? 'Edit Employee' : 'New Employee'} onClose={closeModal}
          footer={<>
            <button className="btn btn-ghost" onClick={closeModal}>Cancel</button>
            <button className="btn btn-primary" onClick={save} disabled={saving}>{saving?'Saving...':'Save'}</button>
          </>}>
          {error && <div className="alert alert-error">{error}</div>}
          <div className="form-grid">
            <div className="form-field">
              <label className="form-label">First Name *</label>
              <input type="text" value={form.firstName} onChange={f('firstName')} />
            </div>
            <div className="form-field">
              <label className="form-label">Last Name *</label>
              <input type="text" value={form.lastName} onChange={f('lastName')} />
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
              <label className="form-label">Role *</label>
              <input type="text" value={form.role} onChange={f('role')} />
            </div>
            <div className="form-field">
              <label className="form-label">Department *</label>
              <select value={form.departmentId} onChange={f('departmentId')}>
                <option value="">Select...</option>
                {departments.map(d => <option key={d.id} value={d.id}>{d.name}</option>)}
              </select>
            </div>
            <div className="form-field">
              <label className="form-label">Hire Date *</label>
              <input type="date" value={form.hireDate} onChange={f('hireDate')} />
            </div>
            <div className="form-field">
              <label className="form-label">Salary *</label>
              <input type="number" value={form.salary} onChange={f('salary')} />
            </div>
            <div className="form-field">
              <label className="form-label">Status</label>
              <select value={form.status} onChange={f('status')}>
                <option>ACTIVE</option><option>INACTIVE</option><option>ON_LEAVE</option>
              </select>
            </div>
          </div>
        </Modal>
      )}
    </div>
  )
}
