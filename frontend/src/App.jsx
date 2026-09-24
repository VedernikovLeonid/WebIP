import { useEffect, useMemo, useState } from 'react';

const API_URL = '/api/v1/appointments';

const STATUS_LABELS = {
  NEW: 'Новая',
  CONFIRMED: 'Подтверждена',
  IN_PROGRESS: 'В работе',
  COMPLETED: 'Завершена',
  CANCELLED: 'Отменена'
};

const EMPTY_FORM = {
  clientName: '',
  phone: '',
  carBrand: '',
  carModel: '',
  licensePlate: '',
  serviceType: 'Замена масла и фильтров',
  appointmentDate: '',
  appointmentTime: '10:00',
  masterName: 'Илья Кузнецов',
  status: 'NEW',
  comment: ''
};

function formatDate(value) {
  if (!value) return 'Дата не указана';
  return new Intl.DateTimeFormat('ru-RU', { day: '2-digit', month: 'short' })
    .format(new Date(`${value}T00:00:00`));
}

function App() {
  const [appointments, setAppointments] = useState([]);
  const [form, setForm] = useState(EMPTY_FORM);
  const [filter, setFilter] = useState('ALL');
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [notice, setNotice] = useState('');

  const loadAppointments = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await fetch(API_URL);
      if (!response.ok) throw new Error('Сервер вернул ошибку при загрузке записей');
      setAppointments(await response.json());
    } catch (requestError) {
      setError(`${requestError.message}. Запустите Spring Boot на порту 8080.`);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAppointments();
  }, []);

  const visibleAppointments = useMemo(() => {
    const sorted = [...appointments].sort((a, b) => {
      const first = `${a.appointmentDate} ${a.appointmentTime}`;
      const second = `${b.appointmentDate} ${b.appointmentTime}`;
      return first.localeCompare(second);
    });
    return filter === 'ALL' ? sorted : sorted.filter((item) => item.status === filter);
  }, [appointments, filter]);

  const stats = useMemo(() => ({
    total: appointments.length,
    today: appointments.filter((item) => item.appointmentDate === new Date().toISOString().slice(0, 10)).length,
    active: appointments.filter((item) => ['NEW', 'CONFIRMED', 'IN_PROGRESS'].includes(item.status)).length,
    done: appointments.filter((item) => item.status === 'COMPLETED').length
  }), [appointments]);

  const updateField = (event) => {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
  };

  const resetForm = () => {
    setForm(EMPTY_FORM);
    setEditingId(null);
  };

  const submitForm = async (event) => {
    event.preventDefault();
    setSaving(true);
    setError('');
    setNotice('');
    try {
      const response = await fetch(editingId ? `${API_URL}/${editingId}` : API_URL, {
        method: editingId ? 'PUT' : 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      });
      if (!response.ok) {
        const payload = await response.json().catch(() => ({}));
        throw new Error(payload.message || 'Не удалось сохранить запись');
      }
      await loadAppointments();
      setNotice(editingId ? 'Запись обновлена.' : 'Новая запись добавлена.');
      resetForm();
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSaving(false);
    }
  };

  const editAppointment = (appointment) => {
    setEditingId(appointment.id);
    setForm({
      clientName: appointment.clientName,
      phone: appointment.phone,
      carBrand: appointment.carBrand,
      carModel: appointment.carModel,
      licensePlate: appointment.licensePlate,
      serviceType: appointment.serviceType,
      appointmentDate: appointment.appointmentDate,
      appointmentTime: appointment.appointmentTime,
      masterName: appointment.masterName,
      status: appointment.status,
      comment: appointment.comment || ''
    });
    window.scrollTo({ top: document.body.scrollHeight, behavior: 'smooth' });
  };

  const deleteAppointment = async (id) => {
    if (!window.confirm('Удалить эту запись?')) return;
    setError('');
    try {
      const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
      if (!response.ok) throw new Error('Не удалось удалить запись');
      setAppointments((current) => current.filter((item) => item.id !== id));
      setNotice('Запись удалена.');
      if (editingId === id) resetForm();
    } catch (requestError) {
      setError(requestError.message);
    }
  };

  return (
    <div className="app-shell">
      <header className="topbar">
        <a className="brand" href="/" aria-label="GARAGE 17">GARAGE<span>17</span></a>
        <div className="topbar-meta">
          <span className="online-dot" /> API online workspace
          <a href="http://localhost:8080/swagger-ui/index.html" target="_blank" rel="noreferrer">Swagger UI ↗</a>
        </div>
      </header>

      <main>
        <section className="hero">
          <div>
            <p className="kicker">Панель администратора / Автосервис</p>
            <h1>Записи без<br /><em>лишнего шума.</em></h1>
            <p className="hero-copy">Единое рабочее пространство для приёма клиентов, распределения работ и контроля загрузки мастерской.</p>
          </div>
          <div className="hero-stamp">
            <span>GARAGE</span>
            <strong>17</strong>
            <small>service desk</small>
          </div>
        </section>

        <section className="stats" aria-label="Сводка по записям">
          <div><span>Всего записей</span><strong>{stats.total}</strong></div>
          <div><span>На сегодня</span><strong>{stats.today}</strong></div>
          <div><span>В работе</span><strong>{stats.active}</strong></div>
          <div><span>Завершено</span><strong>{stats.done}</strong></div>
        </section>

        {error && <div className="alert alert-error">{error}</div>}
        {notice && <div className="alert alert-success">{notice}</div>}

        <section className="workspace">
          <div className="section-head">
            <div>
              <p className="kicker">01 / Schedule</p>
              <h2>Ближайшие визиты</h2>
            </div>
            <label className="filter-label">
              <span>Показать</span>
              <select value={filter} onChange={(event) => setFilter(event.target.value)}>
                <option value="ALL">Все статусы</option>
                {Object.entries(STATUS_LABELS).map(([value, label]) => <option key={value} value={value}>{label}</option>)}
              </select>
            </label>
          </div>

          {loading ? <div className="empty-state">Подключаемся к REST API...</div> : (
            <div className="appointments-list">
              {visibleAppointments.length === 0 && <div className="empty-state">Записей с таким статусом пока нет.</div>}
              {visibleAppointments.map((appointment) => (
                <article className="appointment-card" key={appointment.id}>
                  <div className="appointment-date"><strong>{formatDate(appointment.appointmentDate)}</strong><span>{appointment.appointmentTime}</span></div>
                  <div className="appointment-main">
                    <div className="appointment-heading"><h3>{appointment.clientName}</h3><span className={`status status-${appointment.status.toLowerCase()}`}>{STATUS_LABELS[appointment.status]}</span></div>
                    <p>{appointment.carBrand} {appointment.carModel} <span className="plate">{appointment.licensePlate}</span></p>
                    <small>{appointment.serviceType} · мастер {appointment.masterName}</small>
                    {appointment.comment && <div className="comment">“{appointment.comment}”</div>}
                  </div>
                  <div className="appointment-actions">
                    <button type="button" onClick={() => editAppointment(appointment)}>Изменить</button>
                    <button type="button" className="danger" onClick={() => deleteAppointment(appointment.id)}>Удалить</button>
                  </div>
                </article>
              ))}
            </div>
          )}
        </section>

        <section className="form-section">
          <div className="section-head">
            <div>
              <p className="kicker">02 / Appointment</p>
              <h2>{editingId ? `Изменить запись #${editingId}` : 'Новая запись'}</h2>
            </div>
            {editingId && <button type="button" className="text-button" onClick={resetForm}>Сбросить изменения</button>}
          </div>
          <form className="appointment-form" onSubmit={submitForm}>
            <div className="form-grid">
              <label>Имя клиента<input name="clientName" value={form.clientName} onChange={updateField} placeholder="Например, Анна Петрова" required /></label>
              <label>Телефон<input name="phone" value={form.phone} onChange={updateField} placeholder="+7 900 000-00-00" required /></label>
              <label>Марка<input name="carBrand" value={form.carBrand} onChange={updateField} placeholder="Toyota" required /></label>
              <label>Модель<input name="carModel" value={form.carModel} onChange={updateField} placeholder="Camry" required /></label>
              <label>Госномер<input name="licensePlate" value={form.licensePlate} onChange={updateField} placeholder="А000АА 716" required /></label>
              <label>Вид работ<select name="serviceType" value={form.serviceType} onChange={updateField}><option>Замена масла и фильтров</option><option>Компьютерная диагностика</option><option>Шиномонтаж</option><option>Замена тормозных колодок</option><option>Техническое обслуживание ТО-2</option></select></label>
              <label>Дата<input type="date" name="appointmentDate" value={form.appointmentDate} onChange={updateField} required /></label>
              <label>Время<input type="time" name="appointmentTime" value={form.appointmentTime} onChange={updateField} required /></label>
              <label>Мастер<select name="masterName" value={form.masterName} onChange={updateField}><option>Илья Кузнецов</option><option>Дмитрий Орлов</option><option>Николай Беляев</option></select></label>
              <label>Статус<select name="status" value={form.status} onChange={updateField}>{Object.entries(STATUS_LABELS).map(([value, label]) => <option key={value} value={value}>{label}</option>)}</select></label>
            </div>
            <label className="wide-field">Комментарий<textarea name="comment" value={form.comment} onChange={updateField} placeholder="Что важно знать мастеру?" rows="3" /></label>
            <div className="form-footer"><span>Все изменения отправляются в Spring Boot REST API.</span><button className="primary-button" type="submit" disabled={saving}>{saving ? 'Сохраняем...' : editingId ? 'Сохранить запись' : 'Создать запись'}</button></div>
          </form>
        </section>
      </main>

      <footer className="footer"><span>Лабораторная работа №1 / REST API</span><span>Java · Spring Boot · React</span></footer>
    </div>
  );
}

export default App;
