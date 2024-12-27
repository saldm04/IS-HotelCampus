document.addEventListener('DOMContentLoaded', function() {

    const checkinDate = document.getElementById('checkindate');
    const checkoutDate = document.getElementById('checkoutdate');
    const numOspiti = document.getElementById('numOspiti');
    const dateForm = document.getElementById('dateForm');

    const today = new Date().toISOString().split('T')[0];
    checkinDate.setAttribute('min', today);


    function validateCheckoutDate() {
        if (checkinDate.value) {
            checkoutDate.disabled = false;

            const checkin = new Date(checkinDate.value);
            const minCheckoutDate = new Date(checkin.setDate(checkin.getDate() + 1));
            checkoutDate.setAttribute('min', minCheckoutDate.toISOString().split('T')[0]);
        } else {
            checkoutDate.disabled = true;
            checkoutDate.value = '';
        }
    }


    function validateForm(event) {
        if (!checkinDate.value || !checkoutDate.value || !numOspiti.value) {
            event.preventDefault();
        }
    }

    checkinDate.addEventListener('change', validateCheckoutDate);

    dateForm.addEventListener('submit', validateForm);

    validateCheckoutDate();
});