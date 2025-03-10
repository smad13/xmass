document.addEventListener("DOMContentLoaded", function () {
    const cantidadInput = document.getElementById("cantidad");
    const precioUnitarioInput = document.getElementById("precio_unitario");
    const subtotalInput = document.getElementById("subtotal");

    function calcularSubtotal() {
        const cantidad = parseFloat(cantidadInput.value) || 0;
        const precioUnitario = parseFloat(precioUnitarioInput.value) || 0;
        const subtotal = cantidad * precioUnitario;

        // Actualiza el campo de subtotal con el valor calculado
        subtotalInput.value = subtotal.toFixed(2); // Redondea a dos decimales
    }

    // Escucha cambios en los campos de cantidad y precio unitario
    cantidadInput.addEventListener("input", calcularSubtotal);
    precioUnitarioInput.addEventListener("input", calcularSubtotal);
});
