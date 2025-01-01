document.addEventListener('DOMContentLoaded', function() {
    // Recupera il costo della camera
    const importoCameraElement = document.querySelector('input[name="importoCamera"]');
    const importoCamera = parseFloat(importoCameraElement ? importoCameraElement.value : 0) || 0;

    // Recupera il numero di ospiti, se necessario
    const numeroOspitiElement = document.querySelector('input[name="numeroOspiti"]');
    const numeroOspiti = parseInt(numeroOspitiElement ? numeroOspitiElement.value : 0) || 0;

    // Seleziona l'elemento dove verrà mostrato il totale
    const totaleElement = document.getElementById('totale');

    /**
     * Funzione per calcolare il costo totale
     */
    function calcolaTotale() {
        let totale = importoCamera;

        // Seleziona tutti i menu a tendina delle quantità dei servizi
        const selects = document.querySelectorAll('.quantita-select');

        selects.forEach(function(select) {
            // Ottiene la quantità selezionata, default a 0 se non è un numero valido
            const quantita = parseInt(select.value) || 0;

            // Naviga verso l'elemento <td> precedente per ottenere il costo
            const costoTd = select.parentElement.previousElementSibling;
            const costo = parseFloat(costoTd.dataset.costo) || 0;

            // Aggiunge al totale: costo per persona * quantità
            totale += costo * quantita;
        });

        // Aggiorna il totale nel DOM, formattato a due decimali
        totaleElement.textContent = totale.toFixed(2);
    }

    // Collega la funzione calcolaTotale all'evento 'change' di ogni select
    const selects = document.querySelectorAll('.quantita-select');
    selects.forEach(function(select) {
        select.addEventListener('change', calcolaTotale);
    });

    // Calcolo iniziale per impostare il totale al caricamento della pagina
    calcolaTotale();
});
