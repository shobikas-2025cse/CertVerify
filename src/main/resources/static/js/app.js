/**
 * CertVerify — Application JavaScript
 */

document.addEventListener('DOMContentLoaded', function () {
    // 1. Auto-dismiss flash alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            try {
                const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
                if (bsAlert) {
                    bsAlert.close();
                }
            } catch (e) {
                alert.style.display = 'none';
            }
        }, 5000);
    });

    // 2. Client-side table search filter
    const searchInputs = document.querySelectorAll('[data-table-search]');
    searchInputs.forEach(function (input) {
        const tableSelector = input.getAttribute('data-table-search');
        const table = document.querySelector(tableSelector);
        if (!table) return;

        input.addEventListener('keyup', function () {
            const query = input.value.toLowerCase().trim();
            const rows = table.querySelectorAll('tbody tr');

            rows.forEach(function (row) {
                // If it's empty state row, skip
                if (row.classList.contains('empty-row')) return;
                const text = row.textContent.toLowerCase();
                if (text.indexOf(query) !== -1) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        });
    });

    // 3. Form loading state on submission
    const forms = document.querySelectorAll('form[data-loading]');
    forms.forEach(function (form) {
        form.addEventListener('submit', function () {
            const submitBtn = form.querySelector('button[type="submit"]');
            if (submitBtn) {
                submitBtn.disabled = true;
                const originalHtml = submitBtn.innerHTML;
                submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span> Processing...';
            }
        });
    });
});

/**
 * Confirm deletion dialog
 */
function confirmDelete(message) {
    return confirm(message || 'Are you sure you want to delete this record? This action cannot be undone.');
}

/**
 * Confirm certificate revocation
 */
function confirmRevoke(certId) {
    return confirm('Are you sure you want to REVOKE Certificate #' + certId + '?\n\nWARNING: Once revoked, this certificate will permanently fail all future verification attempts.');
}

/**
 * Copy text to clipboard helper
 */
function copyToClipboard(text, triggerBtn) {
    if (!navigator.clipboard) {
        // Fallback
        const textarea = document.createElement('textarea');
        textarea.value = text;
        document.body.appendChild(textarea);
        textarea.select();
        document.execCommand('copy');
        document.body.removeChild(textarea);
    } else {
        navigator.clipboard.writeText(text);
    }

    if (triggerBtn) {
        const origText = triggerBtn.innerHTML;
        triggerBtn.innerHTML = '<i class="bi bi-check-lg"></i> Copied!';
        triggerBtn.classList.remove('btn-outline-secondary');
        triggerBtn.classList.add('btn-success');
        setTimeout(function () {
            triggerBtn.innerHTML = origText;
            triggerBtn.classList.remove('btn-success');
            triggerBtn.classList.add('btn-outline-secondary');
        }, 2000);
    }
}
