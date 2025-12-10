// API endpoints
const API_BASE_URL = 'http://localhost:8080/api';
const ENDPOINTS = {
    transactions: `${API_BASE_URL}/transactions`,
    categories: `${API_BASE_URL}/categories`,
    reports: `${API_BASE_URL}/reports`
};

// DOM Elements
const modal = document.getElementById('addTransactionModal');
const transactionForm = document.getElementById('transactionForm');
const closeBtn = document.getElementsByClassName('close')[0];

// Initialize charts
let monthlyChart, categoryChart;

// Event Listeners
document.addEventListener('DOMContentLoaded', () => {
    initializeCharts();
    loadCategories();
    loadRecentTransactions();
    updateDashboardSummary();
});

// Navigation
document.querySelectorAll('.nav-links a').forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        const page = e.target.dataset.page;
        navigateToPage(page);
    });
});

function navigateToPage(page) {
    document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
    document.getElementById(page).classList.add('active');
    
    document.querySelectorAll('.nav-links a').forEach(link => {
        link.classList.remove('active');
        if (link.dataset.page === page) {
            link.classList.add('active');
        }
    });
}

// Modal Functions
function showAddTransactionModal() {
    modal.style.display = 'block';
}

function closeModal() {
    modal.style.display = 'none';
}

closeBtn.onclick = closeModal;

window.onclick = (event) => {
    if (event.target === modal) {
        closeModal();
    }
};

// Form Submission
transactionForm.onsubmit = async (e) => {
    e.preventDefault();
    
    const formData = {
        type: document.getElementById('transactionType').value,
        categoryId: document.getElementById('category').value,
        amount: parseFloat(document.getElementById('amount').value),
        date: document.getElementById('date').value,
        description: document.getElementById('description').value
    };

    try {
        const response = await fetch(ENDPOINTS.transactions, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        if (response.ok) {
            closeModal();
            transactionForm.reset();
            loadRecentTransactions();
            updateDashboardSummary();
        } else {
            throw new Error('Failed to save transaction');
        }
    } catch (error) {
        alert('Error saving transaction: ' + error.message);
    }
};

// Load Data Functions
async function loadCategories() {
    try {
        const response = await fetch(ENDPOINTS.categories);
        const categories = await response.json();
        
        const categorySelect = document.getElementById('category');
        categorySelect.innerHTML = categories.map(category => 
            `<option value="${category.id}">${category.name}</option>`
        ).join('');
    } catch (error) {
        console.error('Error loading categories:', error);
    }
}

async function loadRecentTransactions() {
    try {
        const response = await fetch(ENDPOINTS.transactions);
        const transactions = await response.json();
        
        const tbody = document.getElementById('recentTransactionsList');
        tbody.innerHTML = transactions.map(transaction => `
            <tr>
                <td>${new Date(transaction.date).toLocaleDateString()}</td>
                <td>${transaction.categoryName}</td>
                <td>${transaction.description}</td>
                <td class="${transaction.type.toLowerCase()}">${formatCurrency(transaction.amount)}</td>
                <td>${transaction.type}</td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error loading transactions:', error);
    }
}

async function updateDashboardSummary() {
    try {
        const response = await fetch(`${ENDPOINTS.reports}/summary`);
        const summary = await response.json();
        
        document.querySelector('.income .card-body h2').textContent = formatCurrency(summary.totalIncome);
        document.querySelector('.expenses .card-body h2').textContent = formatCurrency(summary.totalExpenses);
        document.querySelector('.balance .card-body h2').textContent = formatCurrency(summary.netSavings);
        
        updateCharts(summary);
    } catch (error) {
        console.error('Error updating dashboard:', error);
    }
}

// Chart Functions
function initializeCharts() {
    // Monthly Overview Chart
    const monthlyCtx = document.getElementById('monthlyChart').getContext('2d');
    monthlyChart = new Chart(monthlyCtx, {
        type: 'bar',
        data: {
            labels: [],
            datasets: [{
                label: 'Income',
                data: [],
                backgroundColor: '#2ecc71'
            }, {
                label: 'Expenses',
                data: [],
                backgroundColor: '#e74c3c'
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

    // Category Chart
    const categoryCtx = document.getElementById('categoryChart').getContext('2d');
    categoryChart = new Chart(categoryCtx, {
        type: 'doughnut',
        data: {
            labels: [],
            datasets: [{
                data: [],
                backgroundColor: [
                    '#3498db',
                    '#e74c3c',
                    '#2ecc71',
                    '#f1c40f',
                    '#9b59b6',
                    '#e67e22'
                ]
            }]
        },
        options: {
            responsive: true
        }
    });
}

function updateCharts(summary) {
    // Update Monthly Chart
    monthlyChart.data.labels = summary.monthlyData.map(item => item.month);
    monthlyChart.data.datasets[0].data = summary.monthlyData.map(item => item.income);
    monthlyChart.data.datasets[1].data = summary.monthlyData.map(item => item.expenses);
    monthlyChart.update();

    // Update Category Chart
    categoryChart.data.labels = summary.categoryData.map(item => item.category);
    categoryChart.data.datasets[0].data = summary.categoryData.map(item => item.amount);
    categoryChart.update();
}

// Utility Functions
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}