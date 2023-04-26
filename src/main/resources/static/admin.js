const urlUsers = 'http://localhost:8080/users';
const urlRoles = 'http://localhost:8080/roles';
const tableUsers = document.querySelector('tbody');


updateUsersTable();

function getUsersRequest() {
    return fetch(urlUsers).then(response => response.json());
}

function getRolesRequest() {
    return fetch(urlRoles).then(response =>response.json());
}

function updateUserRequest(user) {
    return fetch(`${urlUsers}/${user.id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(user),
    });
}

function deleteUserRequest(id) {
    return fetch(`${urlUsers}/${id}`, {
        method: 'DELETE',
    });
}

function createUserRequest(user) {
    return fetch(urlUsers, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(user),
    })
}

function updateUsersTable() {
    tableUsers.innerHTML = '';
    getUsersRequest()
        .then(users => {
            for (const user of users) {
                addUserToTable(user);
            }
        });
}

function addUserToTable(user, row) {
    let trUsers;
    if (row == null) {
        trUsers = document.createElement('tr');
    } else {
        trUsers = row;
        trUsers.innerHTML = '';
    }
    trUsers.id = `rowUser${user.id}`;
    trUsers.insertAdjacentHTML('beforeend',
        `<td>${user.id}</td>
            <td>${user.firstName}</td>
            <td>${user.lastName}</td>
            <td>${user.age}</td>
            <td>${user.email}</td>
            <td></td>
            <td>
                <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#modalUserEdit">
                    Edit
                </button>
            </td>
            <td>
                <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#modalUserDelete">
                    Delete
                </button>
            </td>`);
    const tdRoles = trUsers.cells[5];
    for (const role of user.roles) {
        tdRoles.insertAdjacentHTML("beforeend",
            `<div class="d-inline">
                    <span>${role.slice(5)}</span>
                </div>`);
    }
    if (row == null) {
        tableUsers.append(trUsers);
    }
}

function deleteUserFromTable(row) {
    row.remove();
}

function fillModal(event, modal, actionPrefix) {
    const trUser = event.relatedTarget.parentElement.parentElement;
    modal.querySelector(`#${actionPrefix}Id`).value = trUser.cells[0].textContent;
    modal.querySelector(`#${actionPrefix}FirstName`).value = trUser.cells[1].textContent;
    modal.querySelector(`#${actionPrefix}LastName`).value = trUser.cells[2].textContent;
    modal.querySelector(`#${actionPrefix}Age`).value = trUser.cells[3].textContent;
    modal.querySelector(`#${actionPrefix}Email`).value = trUser.cells[4].textContent;

    const selectRoles = modal.querySelector(`#${actionPrefix}Roles`);

    getRolesRequest()
        .then(roles => {
            selectRoles.size = roles.length;
            selectRoles.innerHTML = '';
            for (const role of roles) {
                const optionRole = document.createElement('option');
                optionRole.value = role.id;
                optionRole.text = role.value.slice(5);
                if (trUser.cells[5].textContent.includes(optionRole.text)) {
                    optionRole.selected = true;
                }
                selectRoles.append(optionRole);
            }
        });
}

/*********************
 * table "edit" button
 *********************/

const modalUserEdit = document.querySelector('#modalUserEdit');

modalUserEdit.addEventListener('show.bs.modal',
        event => fillModal(event, modalUserEdit, 'edit'));

modalUserEdit.addEventListener('hide.bs.modal', () => {
    const inputPass = modalUserEdit.querySelector('#editPassword');
    inputPass.value = '';
})

/*********************
 * modal "edit" button
 ********************/

const editForm = document.querySelector('#editForm');

editForm.addEventListener('submit', event => {
    event.preventDefault();
    const currentActiveModal = bootstrap.Modal.getInstance(modalUserEdit);
    currentActiveModal.hide();

    const user = {
        id: modalUserEdit.querySelector('#editId').value,
        email: modalUserEdit.querySelector('#editEmail').value,
        firstName: modalUserEdit.querySelector('#editFirstName').value,
        lastName: modalUserEdit.querySelector('#editLastName').value,
        age: modalUserEdit.querySelector('#editAge').value,
        rawPassword: modalUserEdit.querySelector('#editPassword').value,
    }
    const userRoles = [];
    const optionsSelectRoles = modalUserEdit.querySelector('#editRoles').getElementsByTagName('option');
    for (const optionRole of optionsSelectRoles) {
        if (optionRole.selected) {
            const role = {
                id: optionRole.value,
                value: `ROLE_${optionRole.text}`,
            }
            userRoles.push(role);
        }
    }
    user.roles = userRoles;

    const trUser = document.querySelector(`#rowUser${user.id}`);
    updateUserRequest(user)
        .then(response => {
            if(!response.ok) {
                throw new Error(`Сервер не смог обработать запрос: ${response.status}`);
            }
            return response.json();
        })
        .then(user => addUserToTable(user, trUser))
        .catch(err => console.error(err));
})

/***********************
 * table "delete" button
 ***********************/

const modalUserDelete = document.querySelector('#modalUserDelete');

modalUserDelete.addEventListener('show.bs.modal',
    event => fillModal(event, modalUserDelete, 'delete'));

/***********************
 * modal "delete" button
 ***********************/

const deleteForm = document.querySelector('#deleteForm');

deleteForm.addEventListener('submit', event => {
    event.preventDefault();
    const currentActiveModal = bootstrap.Modal.getInstance(modalUserDelete);
    currentActiveModal.hide();

    const id = modalUserDelete.querySelector('#deleteId').value;
    const trUser = document.querySelector(`#rowUser${id}`);

    deleteUserRequest(id)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Сервер не смог обработать запрос: ${response.status}`);
            }
        })
        .then(() => deleteUserFromTable(trUser))
        .catch(err => console.error(err));
});

/******************
 * "add new" button
 ******************/

const createForm = document.querySelector('#createForm');

createForm.addEventListener('submit', event => {
    event.preventDefault();
    const user = {
        email: createForm.querySelector('#newEmail').value,
        firstName: createForm.querySelector('#newFirstName').value,
        lastName: createForm.querySelector('#newLastName').value,
        age: createForm.querySelector('#newAge').value,
        rawPassword: createForm.querySelector('#newPassword').value,
    }
    const userRoles = [];
    const optionsSelectRoles = createForm.querySelector('#newRoles').getElementsByTagName('option');
    for (const optionRole of optionsSelectRoles) {
        if (optionRole.selected) {
            const role = {
                id: optionRole.value,
                value: `ROLE_${optionRole.text}`,
            }
            userRoles.push(role);
        }
    }
    user.roles = userRoles;

    createUserRequest(user)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Сервер не смог обработать запрос: ${response.status}`);
            }
            return response.json();
        })
        .then(user => addUserToTable(user))
        .catch(err => console.error(err))
        .then(() => cleanCreateForm());

})

function cleanCreateForm(){
    createForm.querySelectorAll('input').forEach(input => input.value = '');
    createForm.querySelectorAll('option').forEach(option => option.selected = false);
}

/****************
 * "new user" tab
 ****************/
const buttonNewUserTab = document.querySelector('#new-user-tab');

buttonNewUserTab.addEventListener('click', () => {
    const selectRoles = createForm.querySelector('#newRoles');

    getRolesRequest()
        .then(roles => {
            selectRoles.size = roles.length;
            selectRoles.innerHTML = '';
            for (const role of roles) {
                const optionRole = document.createElement('option');
                optionRole.value = role.id;
                optionRole.text = role.value.slice(5);
                selectRoles.append(optionRole);
            }
        });
})
