// main.js

const hostUrl = "https://cuddly-lamp-pj5pxv7v5w3r5wq-8080.app.github.dev"

// Function to load items from the backend
function loadItems() {
    fetch(hostUrl + `/items`, {method: 'GET', mode: 'cors'})
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        // Add each item to the table
        data.forEach(item => addItemToTable(item));
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}

// Load items when the page loads
window.onload = loadItems;

// Function to add a new item
function addItem(event) {
    event.preventDefault();

    // Get form values
    const name = document.getElementById('name').value;
    const description = document.getElementById('description').value;
    const quantity = document.getElementById('quantity').value;
    const price = document.getElementById('price').value;

    // Validate form values
    if (!name || !quantity || !price) {
        alert('Please fill out all required fields.');
        return;
    }

    // Make a POST request to the backend
    fetch(hostUrl + `/items`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name, description, quantity, price }),
    })
    .then(response => response.json())
    .then(data => {
        // Add the new item to the table
        addItemToTable(data);
    })
    .catch((error) => {
        console.error('Error:', error);
    });
}

// Function to add an item to the table
function addItemToTable(item) {
    const table = document.getElementById('itemsTable');
    const row = table.insertRow();
    row.innerHTML = `
        <td>${item.name}</td>
        <td>${item.description}</td>
        <td>${item.quantity}</td>
        <td>${item.price}</td>
        <td>
            <button class="btn btn-danger" onclick="deleteItem(${item.id})">Delete</button>
        </td>
    `;
}

// Function to delete an item
function deleteItem(id) {
    // Show the delete confirmation modal
    $('#deleteModal').modal('show');

    // If the user confirms the deletion, make a DELETE request to the backend
    document.getElementById('confirmDeleteBtn').addEventListener('click', function () {
        fetch(hostUrl + `/items/${id}`, {
            method: 'DELETE',
        })
            .then(() => {
                // Remove the item from the table
                const table = document.getElementById('itemsTable');
                for (let i = 1; i < table.rows.length; i++) {
                    if (table.rows[i].cells[0].innerText == id) {
                        table.deleteRow(i);
                        break;
                    }
                }
            })
            .catch((error) => {
                console.error('Error:', error);
            });

        // Hide the delete confirmation modal
        $('#deleteModal').modal('hide');
    });
}

// Add the addItem function as an event listener for the form submission event
document.getElementById('addItemForm').addEventListener('submit', addItem);