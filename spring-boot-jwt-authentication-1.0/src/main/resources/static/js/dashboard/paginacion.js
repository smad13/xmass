document.addEventListener("DOMContentLoaded", function() {
    const tableBody = document.querySelector("#productTable tbody");
    const rows = tableBody.querySelectorAll("tr");
    const rowsPerPage = 5; // Número de productos por página
    const paginationDiv = document.querySelector(".pagination");
    let currentPage = 1;
    const totalPages = Math.ceil(rows.length / rowsPerPage);

    // Función para mostrar la página solicitada
    function showPage(page) {
        currentPage = page;
        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        rows.forEach((row, index) => {
            row.style.display = (index >= start && index < end) ? "" : "none";
        });
        renderPagination();
    }

    // Función para generar los botones de paginación
    function renderPagination() {
        paginationDiv.innerHTML = "";
        for (let i = 1; i <= totalPages; i++) {
            const btn = document.createElement("button");
            btn.textContent = i;
            btn.classList.add("page-btn");
            if (i === currentPage) {
                btn.classList.add("active");
            }
            btn.addEventListener("click", function() {
                showPage(i);
            });
            paginationDiv.appendChild(btn);
        }
    }

    // Inicialmente muestra la primera página
    showPage(1);
});