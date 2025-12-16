const BASE_URL = 'http://localhost:8080/api/v1/tareas';
const tasksContainer = document.getElementById('tasks-container');

// --- 1. Leer todas las tareas (GET) ---
async function fetchTasks() {
    try {
        const response = await fetch(BASE_URL);
        const tasks = await response.json();
        
        displayTasks(tasks);
    } catch (error) {
        console.error('Error al obtener tareas:', error);
        tasksContainer.innerHTML = '<p>Error al cargar las tareas.</p>';
    }
}

// --- Renderizar las tareas en el HTML ---
function displayTasks(tasks) {
    tasksContainer.innerHTML = ''; // Limpia el contenedor

    if (tasks.length === 0) {
        tasksContainer.innerHTML = '<p>No hay tareas pendientes. ¡Añade una!</p>';
        return;
    }

    tasks.forEach(task => {
        const item = document.createElement('div');
        item.className = 'task-item ' + (task.completada ? 'completed' : '');
        item.innerHTML = `
            <span>${task.descripcion}</span>
            <div>
                <button onclick="toggleCompleted(${task.id}, ${task.completada})">
                    ${task.completada ? 'Deshacer' : 'Completar'}
                </button>
                <button onclick="deleteTask(${task.id})" style="background-color: #dc3545; color: white;">
                    Eliminar
                </button>
            </div>
        `;
        tasksContainer.appendChild(item);
    });
}

// --- 2. Crear nueva tarea (POST) ---
async function createTask() {
    const input = document.getElementById('task-description');
    const descripcion = input.value.trim();

    if (!descripcion) return;

    try {
        await fetch(BASE_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ descripcion: descripcion })
        });

        input.value = ''; // Limpia el campo
        fetchTasks(); // Vuelve a cargar la lista
    } catch (error) {
        console.error('Error al crear tarea:', error);
        alert('Error al crear tarea.');
    }
}

// --- 3. Eliminar tarea (DELETE) ---
async function deleteTask(id) {
    if (!confirm('¿Estás seguro de eliminar esta tarea?')) return;

    try {
        await fetch(`${BASE_URL}/${id}`, {
            method: 'DELETE'
        });
        fetchTasks(); // Vuelve a cargar la lista
    } catch (error) {
        console.error('Error al eliminar tarea:', error);
        alert('Error al eliminar tarea.');
    }
}

// --- 4. Alternar estado (PUT) ---
async function toggleCompleted(id, currentState) {
    // Aquí hacemos un GET para obtener el objeto completo, luego lo modificamos y enviamos un PUT.
    // En un proyecto real, usarías un método PATCH si lo tuvieras implementado.
    try {
        const response = await fetch(`${BASE_URL}/${id}`);
        const task = await response.json();

        // Enviamos el PUT con el estado opuesto
        await fetch(`${BASE_URL}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ 
                descripcion: task.descripcion, 
                completada: !currentState // Alternamos el estado
            })
        });
        fetchTasks(); // Vuelve a cargar la lista
    } catch (error) {
        console.error('Error al actualizar tarea:', error);
        alert('Error al actualizar tarea.');
    }
}

// Iniciar la carga de tareas al iniciar la página
fetchTasks();