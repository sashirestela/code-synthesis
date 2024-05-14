const { fireEvent } = require('@testing-library/dom');
const { JSDOM } = require('jsdom');
const fetchMock = require('jest-fetch-mock');

// Mock the fetch function
fetchMock.enableMocks();

let dom;
let container;

beforeEach(() => {
    // Set up DOM
    dom = new JSDOM(`
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Bakery Inventory Management System</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="styles.css" rel="stylesheet">
    </head>

    <body>
        <div class="container">
            <h1 class="my-4">Bakery Inventory Management System</h1>

            <!-- Form for adding items -->
            <form id="addItemForm">
                <h2>Add Item</h2>
                <div class="form-group">
                    <label for="name">Name</label>
                    <input type="text" id="name" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="description">Description</label>
                    <input type="text" id="description" class="form-control">
                </div>
                <div class="form-group">
                    <label for="quantity">Quantity</label>
                    <input type="number" id="quantity" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="price">Price</label>
                    <input type="number" step="0.01" id="price" class="form-control" required>
                </div>
                <button type="submit" class="btn btn-primary">Add</button>
            </form>
            <!-- Table for displaying items -->
            <h2 class="my-4">Inventory</h2>
            <table class="table" id="itemsTable">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Items will be dynamically added here -->
                </tbody>
            </table>

            <!-- Modal for delete confirmation -->
            <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel"
                aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="deleteModalLabel">Confirm Delete</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            Are you sure you want to delete this item?
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button type="button" class="btn btn-danger" id="confirmDeleteBtn">Confirm Delete</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div id="spinner" class="modal" tabindex="-1" role="dialog"
            style="display: none; background-color: rgba(0,0,0,0.5);">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content" style="background-color: transparent; border: none;">
                    <div class="spinner-border text-primary"></div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="main.js"></script>
    </body>

    </html>
    `);

    // Set global document    
    container = dom.window.document.body;
    global.window = dom.window;
    global.document = dom.window.document;
    global.bootstrap = {
        Modal: jest.fn(() => ({
            show: jest.fn(),
            hide: jest.fn(),
        })),
    };

    // Clear all mocks before each test
    fetchMock.resetMocks();
});

test('loadItems fetches items from the server and adds them to the table', async () => {
    require('../main');

    // Mock the fetch response
    fetchMock.mockResponseOnce(JSON.stringify([
        { id: 1, name: 'Item 1', description: 'Description 1', quantity: 10, price: 5 },
        { id: 2, name: 'Item 2', description: 'Description 2', quantity: 20, price: 10 },
    ]));

    // Wait for the fetch promise to resolve
    await new Promise(resolve => setImmediate(resolve));

    // Check that the items were added to the table
    expect(container.querySelector('#itemsTable').children.length).toBe(2);
});

test('addItem sends a POST request to the server and adds the new item to the table', async () => {
    const { setEventListeners } = require('../main');

    setEventListeners();

    // Mock the fetch response
    fetchMock.mockResponseOnce(JSON.stringify(
        { id: 3, name: 'Item 3', description: 'Description 3', quantity: 30, price: 15.5 }
    ));

    // Fill out the form
    container.querySelector('#name').value = 'Item 3';
    container.querySelector('#description').value = 'Description 3';
    container.querySelector('#quantity').value = '30';
    container.querySelector('#price').value = '15';

    // Submit the form
    fireEvent.submit(container.querySelector('#addItemForm'));

    // Wait for the fetch promise to resolve
    await new Promise(resolve => setImmediate(resolve));

    // Check that the new item was added to the table
    expect(container.querySelector('#itemsTable tbody').children.length).toBe(1);
});

test('deleteItem sends a DELETE request to the server and removes the item from the table', async () => {
    const { addItemToTable } = require('../main');

    // Mock the fetch response
    fetchMock.mockResponseOnce({ status: 200 });

    // Add an item to the table
    const data = JSON.stringify({ id: 1, name: 'Item 1', description: 'Description 1', quantity: 10, price: 5 });
    addItemToTable(JSON.parse(data));

    // Click the delete button
    const deleteButton = document.querySelector('.btn-danger');
    fireEvent.click(deleteButton);

    // Simulate the modal being shown and the delete button in the modal being clicked
    const deleteModal = container.querySelector('#deleteModal');
    const confirmDeleteBtn = deleteModal.querySelector('#confirmDeleteBtn');
    fireEvent.click(confirmDeleteBtn);

    // Wait for the fetch promise to resolve
    await new Promise(resolve => setImmediate(resolve));

    // Check that the item was removed from the table
    expect(container.querySelector('#itemsTable tbody').children.length).toBe(0);
});