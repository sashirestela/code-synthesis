const { TextEncoder, TextDecoder } = require('util');
global.TextEncoder = TextEncoder;
global.TextDecoder = TextDecoder;

const fs = require('fs');
const { JSDOM } = require('jsdom');
const fetchMock = require('jest-fetch-mock');
const { fireEvent } = require('@testing-library/dom');

// Mock the fetch function
fetchMock.enableMocks();

let dom;
let window;
let document;

beforeEach(async () => {
    // Load the HTML and store a reference to the container
    const html = fs.readFileSync('index.html', 'utf8');

    dom = await JSDOM.fromFile('index.html', { runScripts: 'dangerously', resources: 'usable' });

    window = dom.window;
    document = dom.window.document;

    // Wait for the elements to be added
    await new Promise((resolve) => {
        document.addEventListener('DOMContentLoaded', () => {
            resolve();
        });
    });

    global.window = window;
    global.document = document;
    window.fetch = fetchMock;
});

afterEach(() => {
    fetchMock.resetMocks();
    fetchMock.mockClear();
    dom.window.close();
});

test('loadItems fetches items from the server and adds them to the table', async () => {
    // Mock the fetch response
    fetchMock.mockResponseOnce(JSON.stringify([
        { id: 1, name: 'Item 1', description: 'Description 1', quantity: 10, price: 5.5 },
        { id: 2, name: 'Item 2', description: 'Description 2', quantity: 20, price: 10.5 }
    ]));

    window.loadItems();

    // Wait for the fetch promise to resolve
    await new Promise(resolve => setTimeout(resolve, 0));

    // Check that the items were added to the table
    expect(document.querySelectorAll('#itemsTable tbody tr').length).toBe(2);

    // Check that the items were added correctly
    const rows = document.querySelectorAll('#itemsTable tbody tr');
    expect(rows[0].children[0].textContent).toBe('1');
    expect(rows[0].children[1].textContent).toBe('Item 1');
    expect(rows[1].children[0].textContent).toBe('2');
    expect(rows[1].children[1].textContent).toBe('Item 2');

});

test('addItem sends a POST request to the server and adds the new item to the table', async () => {
    // Mock the fetch response
    fetchMock.mockResponseOnce(JSON.stringify({ id: 3, name: 'Item 3', description: 'Description 3', quantity: 30, price: 15.5 }));

    window.setEventListeners();

    // Set the form values
    document.getElementById('name').value = 'Item 3';
    document.getElementById('description').value = 'Description 3';
    document.getElementById('quantity').value = '30';
    document.getElementById('price').value = '15.5';

    // Submit the form
    fireEvent.submit(document.querySelector('#itemForm'));

    // Wait for the fetch promise to resolve
    await new Promise(resolve => setTimeout(resolve, 0));

    // Check that the item was added to the table
    expect(document.querySelectorAll('#itemsTable tbody tr').length).toBe(1);

    // Check that the item was added correctly
    const row = document.querySelector('#itemsTable tbody tr');
    expect(row.children[0].textContent).toBe('3');
    expect(row.children[1].textContent).toBe('Item 3');
});

test('deleteItem sends a DELETE request to the server and removes the item from the table', async () => {
    // Add an item to the table
    const data = JSON.stringify({ id: 1, name: 'Item 1', description: 'Description 1', quantity: 10, price: 5 });
    window.addItemToTable(JSON.parse(data));

    // Mock the fetch response
    fetchMock.mockResponseOnce({ status: 200 });

    // Click the delete button
    fireEvent.click(document.querySelector('.btn-danger'));

    // Simulate the modal being shown and the delete button in the modal being clicked
    const deleteModal = document.querySelector('#deleteModal');
    const confirmDeleteBtn = deleteModal.querySelector('#confirmDeleteBtn');
    fireEvent.click(confirmDeleteBtn);

    // Wait for the fetch promise to resolve
    await new Promise(resolve => setTimeout(resolve, 0));

    // Check that the item was removed from the table
    expect(document.querySelectorAll('#itemsTable tbody tr').length).toBe(0);
});