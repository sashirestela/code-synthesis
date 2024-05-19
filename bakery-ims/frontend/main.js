// main.js

const hostUrl = "https://cuddly-lamp-pj5pxv7v5w3r5wq-8080.app.github.dev"

const itemModal = new bootstrap.Modal(document.getElementById('itemModal'));
const deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
const spinner = document.getElementById('spinner');

// Function to load items from the backend
function loadItems() {
    showSpinner();

    fetch(hostUrl + `/items`, { method: 'GET', mode: 'cors' })
        .then(response => response.json())
        .then(data => {
            // Add each item to the table
            data.forEach(item => addItemToTable(item));

            hideSpinner();
        })
        .catch((error) => {
            console.error('Error:', error);

            hideSpinner();
        });
}

// Function to clear the form
function clearForm() {
    document.getElementById('id').value = '';
    document.getElementById('name').value = '';
    document.getElementById('description').value = '';
    document.getElementById('quantity').value = '';
    document.getElementById('price').value = '';

    document.getElementById('itemModalLabel').textContent = 'Add Item';
}

// Function to add a new item or update an existing one
function saveItem(event) {
    event.preventDefault();

    // Get form values
    const id = document.getElementById('id').value;
    const name = document.getElementById('name').value;
    const description = document.getElementById('description').value;
    const quantity = document.getElementById('quantity').value;
    const price = document.getElementById('price').value;

    showSpinner();

    // Determine the request method and URL
    const method = id ? 'PUT' : 'POST';
    const url = id ? `${hostUrl}/items/${id}` : `${hostUrl}/items`;

    // Make a request to the backend
    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name, description, quantity, price }),
    })
        .then(response => response.json())
        .then(data => {
            // Add or update the item in the table
            if (id) {
                updateItemInTable(data);
            } else {
                addItemToTable(data);
            }

            // Clean form values
            document.getElementById('id').value = '';
            document.getElementById('name').value = '';
            document.getElementById('description').value = '';
            document.getElementById('quantity').value = '';
            document.getElementById('price').value = '';

            itemModal.hide();

            hideSpinner();
        })
        .catch((error) => {
            console.error('Error:', error);

            hideSpinner();
        });
}

// Function to add an item to the table
function addItemToTable(item) {
    const tBody = document.querySelector('#itemsTable tbody');
    const row = tBody.insertRow();
    row.innerHTML = `
        <td class="d-none">${item.id}</td>
        <td>${item.name}</td>
        <td>${item.description}</td>
        <td>${item.quantity}</td>
        <td>${item.price}</td>
        <td>
            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#itemModal" onclick="populateForm(${item.id})">Update</button>
            <button class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal" data-item-id="${item.id}" onclick="deleteItem(${item.id})">Delete</button>
        </td>
    `;
}

// Function to update an item in the table
function updateItemInTable(item) {
    const row = document.querySelector(`#itemsTable tbody tr [data-item-id="${item.id}"]`).parentNode.parentNode;
    row.innerHTML = `
        <td class="d-none">${item.id}</td>
        <td>${item.name}</td>
        <td>${item.description}</td>
        <td>${item.quantity}</td>
        <td>${item.price}</td>
        <td>
            <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#itemModal" onclick="populateForm(${item.id})">Update</button>
            <button class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal" data-item-id="${item.id}" onclick="deleteItem(${item.id})">Delete</button>
        </td>
    `;
}

// Function to populate the form with item data
function populateForm(id) {
    const row = document.querySelector(`#itemsTable tbody tr [data-item-id="${id}"]`).parentNode.parentNode;
    document.getElementById('id').value = row.children[0].textContent;
    document.getElementById('name').value = row.children[1].textContent;
    document.getElementById('description').value = row.children[2].textContent;
    document.getElementById('quantity').value = row.children[3].textContent;
    document.getElementById('price').value = row.children[4].textContent;

    document.getElementById('itemModalLabel').textContent = 'Update Item';
}

// Function to delete an item
function deleteItem(id) {
    // If the user confirms the deletion, make a DELETE request to the backend
    document.getElementById('confirmDeleteBtn').addEventListener('click', function () {
        showSpinner();

        fetch(hostUrl + `/items/${id}`, {
            method: 'DELETE',
        })
            .then(() => {
                // Remove the item from the table
                const button = document.querySelector(`#itemsTable tbody tr [data-item-id="${id}"]`);
                if (button) {
                    const row = button.parentNode.parentNode;
                    row.remove();
                }

                deleteModal.hide();

                hideSpinner();
            })
            .catch((error) => {
                console.error('Error:', error);

                deleteModal.hide();

                hideSpinner();
            });
    });
}

// Function to search items
function searchItems() {
    const search = document.getElementById('searchInput').value;

    showSpinner();

    fetch(hostUrl + `/items?search=${search}`, { method: 'GET' })
        .then(response => response.json())
        .then(data => {
            // Get the table body
            const tableBody = document.getElementById('itemsTable').getElementsByTagName('tbody')[0];

            // Clear the table
            tableBody.innerHTML = '';

            if (data.length > 0) {
                // Add each item to the table
                data.forEach(item => addItemToTable(item));
            } else {
                // Add a message to the table
                const row = tableBody.insertRow();
                row.innerHTML = '<td colspan="6" class="text-center">No items found</td>';
            }

            hideSpinner();
        })
        .catch((error) => {
            console.error('Error:', error);

            hideSpinner();
        });
}

function setEventListeners() {
    // Add the saveItem function as an event listener for the form submission event
    document.getElementById('itemForm').addEventListener('submit', saveItem);

    // Add the clearForm function as an event listener for the Add Item button click event
    document.getElementById('addItemButton').addEventListener('click', clearForm);

    // Add the searchItems function as an event listener for the search button click event
    document.getElementById('searchButton').addEventListener('click', searchItems);

}

function showSpinner() {
    spinner.style.display = 'block';
}

function hideSpinner() {
    spinner.style.display = 'none';
}

window.onload = function () {
    loadItems();
    setEventListeners();
}

if (typeof module !== 'undefined') {
    module.exports = { loadItems, clearForm, setEventListeners, addItemToTable, updateItemInTable, populateForm, deleteItem, searchItems };
}
// main.js