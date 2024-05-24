// cypress/integration/inventory.spec.js

describe('Bakery Inventory Management System', () => {
    beforeEach(() => {
      cy.visit('/');
    });
  
    it('should add a new item to the inventory', () => {
      cy.get('#addItemButton').click();
      cy.get('#name').type('Test Bread');
      cy.get('#description').type('Delicious test bread');
      cy.get('#quantity').type('10');
      cy.get('#price').type('2.99');
      cy.get('#itemForm').submit();
      
      cy.get('#itemsTable').contains('Test Bread').should('exist');
    });
  
    it('should show error message for missing required fields', () => {
      cy.get('#addItemButton').click();
      cy.get('#name').type('Incomplete Item');
      cy.get('#itemForm').submit();
      
      cy.on('window:alert', (txt) => {
        expect(txt).to.contains('Please fill out this field');
      });
    });
  
    it('should update an existing item', () => {
      cy.get('#itemsTable').contains('Test Bread').parent('tr').within(() => {
        cy.get('.btn-primary').click();
      });
      cy.get('#quantity').clear().type('20');
      cy.get('#itemForm').submit();
      
      cy.get('#itemsTable').contains('Test Bread').parent('tr').within(() => {
        cy.get('td').eq(3).should('contain', '20');
      });
    });
  
    it('should delete an item from the inventory', () => {
      cy.get('#itemsTable').contains('Test Bread').parent('tr').within(() => {
        cy.get('.btn-danger').click();
      });
      cy.get('#confirmDeleteBtn').click();
      
      cy.get('#itemsTable').contains('Test Bread').should('not.exist');
    });
  
    it('should search for items in the inventory', () => {
      cy.get('#addItemButton').click();
      cy.get('#name').type('Searchable Item');
      cy.get('#description').type('Item for searching');
      cy.get('#quantity').type('5');
      cy.get('#price').type('1.99');
      cy.get('#itemForm').submit();
  
      cy.get('#searchInput').type('Searchable');
      cy.get('#searchButton').click();
  
      cy.get('#itemsTable').contains('Searchable Item').should('exist');
    });
  
    it('should display alert when stock quantity reaches zero', () => {
      cy.get('#addItemButton').click();
      cy.get('#name').type('Low Stock Item');
      cy.get('#description').type('This item has low stock');
      cy.get('#quantity').type('0');
      cy.get('#price').type('0.99');
      cy.get('#itemForm').submit();
  
      cy.get('#alertBanner').click();
      cy.get('#alertList').contains('Low Stock Item').should('exist');
    });
  });
  