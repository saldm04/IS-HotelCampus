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

let totale = 0;
let tempCostoCamera = 0;

function mostraModale(cameraId, numeroCamera, costoCamera) {
    console.log(cameraId); // Controlla se questo è il valore giusto (1 in questo caso)
    const modal = document.getElementById('modale');
    const tbody = document.getElementById('serviceModalTbody');
    const totaleSpan = document.getElementById('modalTotale');

    tempCostoCamera = costoCamera;

    // Ottieni i valori dal form
    const checkIn = document.getElementById('checkindate').value;
    const checkOut = document.getElementById('checkoutdate').value;
    const numeroOspiti = parseInt(document.getElementById('numOspiti').value, 10) || 0;

    const cameraSelezionata = document.getElementById('cameraSelezionata');

    cameraSelezionata.innerHTML = `Camera Selezionata N°: ${numeroCamera}, costo ${costoCamera} €`;

    if (!checkIn || !checkOut || numeroOspiti <= 0) {
        alert("Per favore, completa i campi Check-in, Check-out e Numero di ospiti prima di procedere.");
        return;
    }

    document.getElementById('modalIdCamera').value = cameraId;
    document.getElementById('modalCheckIn').value = checkIn;
    document.getElementById('modalCheckOut').value = checkOut;
    document.getElementById('modalNumeroOspiti').value = numeroOspiti;


    fetch('/servizi', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(servizi => {
            tbody.innerHTML = '';
            servizi.forEach(servizio => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${servizio.nome}</td>
                    <td data-costo="${servizio.costo}">${servizio.costo} €</td>
                    <td>
                        <select name="quantita[${servizio.id}]" class="quantita-select">
                            ${Array.from({ length: numeroOspiti + 1 }, (_, i) => `<option value="${i}">${i}</option>`).join('')}
                        </select>
                    </td>
                `;
                tbody.appendChild(row);
            });

            // Aggiungi evento per aggiornare il totale
            document.querySelectorAll('.quantita-select').forEach(select => {
                select.addEventListener('change', () => aggiornaTotale(servizi));
            });

            aggiornaTotale(servizi);
        })
        .catch(error => console.error('Errore durante il caricamento dei servizi:', error));

    modal.style.display = 'flex';
}

function aggiornaTotale(servizi) {
    const totaleSpan = document.getElementById('modalTotale');

    totale = tempCostoCamera;

    document.querySelectorAll('.quantita-select').forEach(select => {
        const servizioId = select.name.match(/\[(\d+)\]/)[1];
        const servizio = servizi.find(s => s.id == servizioId);
        const quantita = parseInt(select.value, 10) || 0;
        totale += servizio.costo * quantita;
    });

    totaleSpan.textContent = totale.toFixed(2);
}

function chiudiModale() {
    document.getElementById('modale').style.display = 'none';
    document.body.classList.remove('modal-open');
}