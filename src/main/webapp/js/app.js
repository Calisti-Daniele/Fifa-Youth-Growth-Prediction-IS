// Seleziona il pulsante del menu utente e il dropdown
const userMenuButton = document.getElementById('user-menu-button');
const userMenuDetails = document.getElementById('user-menu-details');

// Aggiungi un listener per gestire l'apertura e la chiusura del menu
userMenuButton.addEventListener('click', () => {
    if(userMenuDetails.classList.contains("hidden"))
        userMenuDetails.classList.remove('hidden') // Alterna la classe 'hidden' per mostrare/nascondere il menu
    else
        userMenuDetails.classList.add('hidden')
});
