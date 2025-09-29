/**
 * Admin JavaScript functions for Vehicle Insurance Management System
 */

// Global variables
const API_BASE_URL = '/api/admin';
let currentUser = null;

// Initialize on document ready
$(document).ready(function() {
    initializeAdmin();
});

/**
 * Initialize admin functionality
 */
function initializeAdmin() {
    initializeDataTables();
    initializeModals();
    initializeForms();
    initializeAlerts();
    loadDashboardData();
}

/**
 * Initialize DataTables
 */
function initializeDataTables() {
    if ($('.data-table').length > 0) {
        $('.data-table').DataTable({
            responsive: true,
            pageLength: 25,
            order: [[0, 'desc']],
            language: {
                search: "Search:",
                lengthMenu: "Show _MENU_ entries",
                info: "Showing _START_ to _END_ of _TOTAL_ entries",
                paginate: {
                    first: "First",
                    last: "Last",
                    next: "Next",
                    previous: "Previous"
                }
            },
            columnDefs: [
                { orderable: false, targets: -1 } // Disable sorting on last column (actions)
            ]
        });
    }
}

/**
 * Initialize modals
 */
function initializeModals() {
    // Auto-focus on first input when modal opens
    $('.modal').on('shown.bs.modal', function() {
        $(this).find('input:first').focus();
    });

    // Clear form when modal is hidden
    $('.modal').on('hidden.bs.modal', function() {
        $(this).find('form')[0].reset();
        $(this).find('.is-invalid').removeClass('is-invalid');
        $(this).find('.invalid-feedback').remove();
    });
}

/**
 * Initialize forms
 */
function initializeForms() {
    // Form validation
    $('form').on('submit', function(e) {
        if (!validateForm(this)) {
            e.preventDefault();
            return false;
        }
    });

    // Real-time validation
    $('input, select, textarea').on('blur', function() {
        validateField(this);
    });
}

/**
 * Initialize alerts
 */
function initializeAlerts() {
    // Auto-hide alerts after 5 seconds
    setTimeout(function() {
        $('.alert').fadeOut();
    }, 5000);
}

/**
 * Load dashboard data
 */
function loadDashboardData() {
    if (window.location.pathname.includes('/dashboard')) {
        loadDashboardStatistics();
        loadRecentActivities();
        loadSystemHealth();
    }
}

/**
 * Load dashboard statistics
 */
function loadDashboardStatistics() {
    $.ajax({
        url: API_BASE_URL + '/dashboard/statistics',
        method: 'GET',
        success: function(data) {
            updateStatisticsCards(data);
        },
        error: function(xhr, status, error) {
            console.error('Error loading dashboard statistics:', error);
            showAlert('Error loading dashboard statistics', 'danger');
        }
    });
}

/**
 * Update statistics cards
 */
function updateStatisticsCards(data) {
    $('.card-stats .h2').each(function() {
        const card = $(this).closest('.card-stats');
        const title = card.find('.card-title').text().toLowerCase();
        
        if (title.includes('total users')) {
            $(this).text(data.totalUsers || 0);
        } else if (title.includes('active users')) {
            $(this).text(data.activeUsers || 0);
        } else if (title.includes('blocked users')) {
            $(this).text(data.blockedUsers || 0);
        } else if (title.includes('pending users')) {
            $(this).text(data.pendingUsers || 0);
        }
    });
}

/**
 * Load recent activities
 */
function loadRecentActivities() {
    $.ajax({
        url: API_BASE_URL + '/activities/recent?hours=24',
        method: 'GET',
        success: function(data) {
            updateRecentActivitiesTable(data);
        },
        error: function(xhr, status, error) {
            console.error('Error loading recent activities:', error);
        }
    });
}

/**
 * Update recent activities table
 */
function updateRecentActivitiesTable(activities) {
    const tbody = $('.recent-activities tbody');
    tbody.empty();
    
    activities.slice(0, 10).forEach(function(activity) {
        const row = `
            <tr>
                <td>${activity.username}</td>
                <td>${activity.activityDescription}</td>
                <td>${formatDateTime(activity.activityTimestamp)}</td>
                <td>
                    <span class="badge ${activity.success ? 'bg-success' : 'bg-danger'}">
                        ${activity.success ? 'Success' : 'Failed'}
                    </span>
                </td>
            </tr>
        `;
        tbody.append(row);
    });
}

/**
 * Load system health
 */
function loadSystemHealth() {
    $.ajax({
        url: API_BASE_URL + '/system/health',
        method: 'GET',
        success: function(data) {
            updateSystemHealth(data);
        },
        error: function(xhr, status, error) {
            console.error('Error loading system health:', error);
        }
    });
}

/**
 * Update system health
 */
function updateSystemHealth(data) {
    // Update health indicators
    $('.health-indicator').each(function() {
        const indicator = $(this);
        const type = indicator.data('type');
        
        if (data[type] === 'HEALTHY') {
            indicator.removeClass('text-danger text-warning').addClass('text-success');
        } else if (data[type] === 'WARNING') {
            indicator.removeClass('text-success text-danger').addClass('text-warning');
        } else {
            indicator.removeClass('text-success text-warning').addClass('text-danger');
        }
    });
}

/**
 * User Management Functions
 */

/**
 * Create user
 */
function createUser(formData) {
    $.ajax({
        url: API_BASE_URL + '/users',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function(data) {
            showAlert('User created successfully', 'success');
            $('#createUserModal').modal('hide');
            location.reload();
        },
        error: function(xhr, status, error) {
            const response = JSON.parse(xhr.responseText);
            showAlert(response.message || 'Error creating user', 'danger');
        }
    });
}

/**
 * Block user
 */
function blockUser(userId) {
    if (confirm('Are you sure you want to block this user?')) {
        $.ajax({
            url: API_BASE_URL + '/users/' + userId + '/block?blockedBy=admin',
            method: 'POST',
            success: function(data) {
                showAlert('User blocked successfully', 'success');
                location.reload();
            },
            error: function(xhr, status, error) {
                const response = JSON.parse(xhr.responseText);
                showAlert(response.message || 'Error blocking user', 'danger');
            }
        });
    }
}

/**
 * Unblock user
 */
function unblockUser(userId) {
    if (confirm('Are you sure you want to unblock this user?')) {
        $.ajax({
            url: API_BASE_URL + '/users/' + userId + '/unblock?unblockedBy=admin',
            method: 'POST',
            success: function(data) {
                showAlert('User unblocked successfully', 'success');
                location.reload();
            },
            error: function(xhr, status, error) {
                const response = JSON.parse(xhr.responseText);
                showAlert(response.message || 'Error unblocking user', 'danger');
            }
        });
    }
}

/**
 * Delete user
 */
function deleteUser(userId) {
    if (confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
        $.ajax({
            url: API_BASE_URL + '/users/' + userId + '?deletedBy=admin',
            method: 'DELETE',
            success: function(data) {
                showAlert('User deleted successfully', 'success');
                location.reload();
            },
            error: function(xhr, status, error) {
                const response = JSON.parse(xhr.responseText);
                showAlert(response.message || 'Error deleting user', 'danger');
            }
        });
    }
}

/**
 * Reset password
 */
function resetPassword(userId) {
    const newPassword = prompt('Enter new password:');
    if (newPassword && newPassword.length >= 8) {
        $.ajax({
            url: API_BASE_URL + '/users/' + userId + '/reset-password?newPassword=' + encodeURIComponent(newPassword) + '&resetBy=admin',
            method: 'POST',
            success: function(data) {
                showAlert('Password reset successfully', 'success');
            },
            error: function(xhr, status, error) {
                const response = JSON.parse(xhr.responseText);
                showAlert(response.message || 'Error resetting password', 'danger');
            }
        });
    } else if (newPassword) {
        showAlert('Password must be at least 8 characters long', 'warning');
    }
}

/**
 * Lock user account
 */
function lockUserAccount(userId) {
    const minutes = prompt('Enter lock duration in minutes:');
    if (minutes && parseInt(minutes) > 0) {
        $.ajax({
            url: API_BASE_URL + '/users/' + userId + '/lock?minutes=' + minutes + '&lockedBy=admin',
            method: 'POST',
            success: function(data) {
                showAlert('User account locked successfully', 'success');
                location.reload();
            },
            error: function(xhr, status, error) {
                const response = JSON.parse(xhr.responseText);
                showAlert(response.message || 'Error locking user account', 'danger');
            }
        });
    }
}

/**
 * Unlock user account
 */
function unlockUserAccount(userId) {
    if (confirm('Are you sure you want to unlock this user account?')) {
        $.ajax({
            url: API_BASE_URL + '/users/' + userId + '/unlock?unlockedBy=admin',
            method: 'POST',
            success: function(data) {
                showAlert('User account unlocked successfully', 'success');
                location.reload();
            },
            error: function(xhr, status, error) {
                const response = JSON.parse(xhr.responseText);
                showAlert(response.message || 'Error unlocking user account', 'danger');
            }
        });
    }
}

/**
 * Form Validation Functions
 */

/**
 * Validate form
 */
function validateForm(form) {
    let isValid = true;
    
    $(form).find('input[required], select[required], textarea[required]').each(function() {
        if (!validateField(this)) {
            isValid = false;
        }
    });
    
    return isValid;
}

/**
 * Validate field
 */
function validateField(field) {
    const $field = $(field);
    const value = $field.val().trim();
    const type = $field.attr('type');
    const name = $field.attr('name');
    
    // Remove previous validation classes
    $field.removeClass('is-valid is-invalid');
    $field.siblings('.invalid-feedback').remove();
    
    // Required field validation
    if ($field.prop('required') && !value) {
        showFieldError($field, 'This field is required');
        return false;
    }
    
    // Email validation
    if (type === 'email' && value) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(value)) {
            showFieldError($field, 'Please enter a valid email address');
            return false;
        }
    }
    
    // Password validation
    if (name === 'password' && value) {
        if (value.length < 8) {
            showFieldError($field, 'Password must be at least 8 characters long');
            return false;
        }
    }
    
    // Username validation
    if (name === 'username' && value) {
        if (value.length < 3) {
            showFieldError($field, 'Username must be at least 3 characters long');
            return false;
        }
    }
    
    // If we get here, the field is valid
    $field.addClass('is-valid');
    return true;
}

/**
 * Show field error
 */
function showFieldError($field, message) {
    $field.addClass('is-invalid');
    $field.after('<div class="invalid-feedback">' + message + '</div>');
}

/**
 * Utility Functions
 */

/**
 * Show alert message
 */
function showAlert(message, type = 'info') {
    const alertHtml = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            <i class="bi bi-${getAlertIcon(type)}"></i>
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    
    $('.main-content').prepend(alertHtml);
    
    // Auto-hide after 5 seconds
    setTimeout(function() {
        $('.alert').fadeOut();
    }, 5000);
}

/**
 * Get alert icon
 */
function getAlertIcon(type) {
    const icons = {
        'success': 'check-circle',
        'danger': 'exclamation-triangle',
        'warning': 'exclamation-triangle',
        'info': 'info-circle'
    };
    return icons[type] || 'info-circle';
}

/**
 * Format date time
 */
function formatDateTime(dateTimeString) {
    const date = new Date(dateTimeString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
}

/**
 * Format date
 */
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString();
}

/**
 * Confirm action
 */
function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

/**
 * Loading functions
 */
function showLoading(element) {
    $(element).prop('disabled', true).html('<span class="spinner-border spinner-border-sm" role="status"></span> Loading...');
}

function hideLoading(element, originalText) {
    $(element).prop('disabled', false).text(originalText);
}

/**
 * Export functions
 */
function exportToCSV(data, filename) {
    const csv = convertToCSV(data);
    downloadCSV(csv, filename);
}

function convertToCSV(data) {
    if (data.length === 0) return '';
    
    const headers = Object.keys(data[0]);
    const csvContent = [
        headers.join(','),
        ...data.map(row => headers.map(header => `"${row[header] || ''}"`).join(','))
    ].join('\n');
    
    return csvContent;
}

function downloadCSV(csv, filename) {
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();
    window.URL.revokeObjectURL(url);
}

// Global function exports for HTML onclick handlers
window.blockUser = blockUser;
window.unblockUser = unblockUser;
window.deleteUser = deleteUser;
window.resetPassword = resetPassword;
window.lockUserAccount = lockUserAccount;
window.unlockUserAccount = unlockUserAccount;

