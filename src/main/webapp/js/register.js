document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("registerForm").addEventListener("submit", function (event) {
        event.preventDefault(); // Previene l'invio predefinito del form

        const nome = document.getElementById("nome").value.trim();
        const cognome = document.getElementById("cognome").value.trim();
        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("password").value;
        const confirmPassword = document.getElementById("confirm_password").value;

        // Espressione regolare per la validazione dell'email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (nome === "" || cognome === "" || email === "" || password === "" || confirmPassword === "") {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Tutti i campi sono obbligatori!",
            });
            return;
        }

        if (!emailRegex.test(email)) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Inserisci un'email valida!",
            });
            return;
        }

        if (password.length < 8) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "La password deve contenere almeno 8 caratteri!",
            });
            return;
        }

        if (password !== confirmPassword) {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Le password non coincidono",
            });
            return;
        }

        // Se tutti i controlli passano, invia il form
        this.submit();
    });
});
