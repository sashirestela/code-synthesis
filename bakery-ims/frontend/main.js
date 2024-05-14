// main.js

const hostUrl = "https://cuddly-lamp-pj5pxv7v5w3r5wq-8080.app.github.dev"

// Function to load items from the backend
function loadItems() {
    fetch(hostUrl + `/items`, { method: 'GET', mode: 'cors' })
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

// Function to add a new item
function addItem(event) {
    event.preventDefault();

    // Get form values
    const name = document.getElementById('name').value;
    const description = document.getElementById('description').value;
    const quantity = document.getElementById('quantity').value;
    const price = document.getElementById('price').value;

    // Show the spinner
    document.getElementById('spinner').style.display = 'block';

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

            // Clean form values
            document.getElementById('name').value = '';
            document.getElementById('description').value = '';
            document.getElementById('quantity').value = '';
            document.getElementById('price').value = '';

            // Hide the spinner
            document.getElementById('spinner').style.display = 'none';
        })
        .catch((error) => {
            console.error('Error:', error);

            // Hide the spinner
            document.getElementById('spinner').style.display = 'none';
        });
}

// Function to add an item to the table
function addItemToTable(item) {
    const tBody = document.querySelector('#itemsTable tbody');
    const row = tBody.insertRow();
    row.innerHTML = `
        <td>${item.name}</td>
        <td>${item.description}</td>
        <td>${item.quantity}</td>
        <td>${item.price}</td>
        <td>
            <button class="btn btn-danger" data-item-id="${item.id}">Delete</button>
        </td>
    `;

    // Add the event listener to the Delete button
    const deleteButton = row.querySelector('.btn-danger');
    deleteButton.addEventListener('click', () => {
        deleteItem(item.id);
    });
}

// Function to delete an item
function deleteItem(id) {
    // Show the delete confirmation modal
    var deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'))
    deleteModal.show()

    // If the user confirms the deletion, make a DELETE request to the backend
    document.getElementById('confirmDeleteBtn').addEventListener('click', function () {
        // Show the spinner
        document.getElementById('spinner').style.display = 'block';

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

                // Hide the spinner
                document.getElementById('spinner').style.display = 'none';
            })
            .catch((error) => {
                console.error('Error:', error);

                // Hide the spinner
                document.getElementById('spinner').style.display = 'none';
            });

        // Hide the delete confirmation modal
        deleteModal.hide();
    });
}

function setEventListeners() {
    // Add the addItem function as an event listener for the form submission event
    document.getElementById('addItemForm').addEventListener('submit', addItem);
}

window.onload = function () {
    loadItems();
    setEventListeners();
}

if (typeof module !== 'undefined') {
    module.exports = { loadItems, setEventListeners, addItemToTable };
}
// main.js