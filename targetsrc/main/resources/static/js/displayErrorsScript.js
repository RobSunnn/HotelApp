function displayErrors(errors) {
    // Clear previous errors
    document.querySelectorAll('.invalid-feedback').forEach(el => el.textContent = '');
    document.querySelectorAll('.form-control').forEach(el => el.classList.remove('is-invalid'));

    if (errors.length > 0) {
        // Scroll to the first error
        const firstErrorField = errors[0].field;
        const firstErrorElement = document.getElementById(firstErrorField);
        if (firstErrorElement) {
            firstErrorElement.scrollIntoView({behavior: 'smooth', block: 'center'});
        }
    }

    // Iterate over the errors array
    errors.forEach(error => {
        const errorElement = document.getElementById(`${error.field}Error`);
        const inputElement = document.getElementById(error.field);
        if (errorElement && inputElement) {
            errorElement.textContent = error.defaultMessage;
            inputElement.classList.add('is-invalid');
        }
    });
}